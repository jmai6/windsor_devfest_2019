package com.quickenloans.titanicsurvivalgame;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TitanicSurvivalActivity extends AppCompatActivity {
  CompositeSubscription subscription;
  @BindView(R.id.input_age) EditText ageInput;
  @BindView(R.id.input_number_siblingAndSpouse) EditText numSiblingsSpouseInput;
  @BindView(R.id.input_number_parentsAndChildren) EditText numParentChildrenInput;
  @BindView(R.id.input_fare) EditText fareInput;
  @BindView(R.id.spinner_class) Spinner classInput;
  @BindView(R.id.spinner_gender) Spinner genderInput;
  @BindView(R.id.progress_overlay) View overlay;
  @BindView(R.id.btn) Button submit;
  TensorFlowInferenceInterface inferenceInterface;
  Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_titanic_survival);
    unbinder = ButterKnife.bind(this);
    getSupportActionBar().setTitle("Guess to Survive");
    overlay.setVisibility(VISIBLE);
    subscription = new CompositeSubscription();
    subscription.add(Observable.just(new Object()).map(new Func1<Object, String>() {
      @Override public String call(Object s) {
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), "titanic-frozen_model.pb");
        return "done";
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
      @Override public void call(String input) {
        if ("done".equals(input)) {
          overlay.setVisibility(GONE);
        }
      }
    }));
  }

  @OnClick(R.id.btn) void predict(View v) {
    overlay.setVisibility(VISIBLE);
    final float age = Float.parseFloat(ageInput.getText().toString());
    final float numSiblings = Float.parseFloat(numSiblingsSpouseInput.getText().toString());
    final float numParents = Float.parseFloat(numParentChildrenInput.getText().toString());
    final float fare = Float.parseFloat(fareInput.getText().toString());
    final float gender = "male".equals(genderInput.getSelectedItem().toString()) ? 0f : 1f;
    final float cabinClass;
    if ("2nd".equals(classInput.getSelectedItem().toString())) {
      cabinClass = 2f;
    } else if ("3rd".equals(classInput.getSelectedItem().toString())) {
      cabinClass = 3f;
    } else {
      cabinClass = 1f;
    }
    subscription.add(Observable.just(new Object()).map(new Func1<Object, String>() {
      @Override public String call(Object s) {
        inferenceInterface.feed("my_input/X", new float[] {cabinClass, gender, age, numSiblings, numParents, fare}, 1, 6);
        inferenceInterface.run(new String[] {"my_output/Softmax"});
        float[] output = new float[2];
        inferenceInterface.fetch("my_output/Softmax", output);
        return Math.round(output[1]) + "";
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
      @Override public void call(String input) {
        overlay.setVisibility(GONE);
        Log.d("", "survival code: " + input);
        if ("1".equals(input)) {
          alert("Congratulation", "You will survive!", Color.BLUE);
        } else {
          alert("Sorry", "Thank you for your sacrifice.", Color.RED);
        }
      }
    }));
  }

  public void alert(final String title, final String description, @ColorInt final int color) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    // Initialize a new foreground color span instance
    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);

    // Initialize a new spannable string builder instance
    SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

    // Apply the text color span
    ssBuilder.setSpan(foregroundColorSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    // set title
    alertDialogBuilder.setTitle(ssBuilder);

    // set dialog message
    alertDialogBuilder.setMessage(description).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });

    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
    if (subscription != null) {
      subscription.unsubscribe();
    }
    if (inferenceInterface != null) {
      inferenceInterface.close();
    }
  }
}
