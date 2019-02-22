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
import android.widget.SeekBar;
import android.widget.TextView;
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

public class BostonHousingActivity extends AppCompatActivity {
  CompositeSubscription subscription;
  @BindView(R.id.crime_rate) SeekBar crimeRateInput;
  @BindView(R.id.crime_rate_text) TextView crimeRateText;
  @BindView(R.id.large_lots_zone) SeekBar largeLotsInput;
  @BindView(R.id.large_lots_zone_text) TextView largeLotsText;
  @BindView(R.id.industrial_zone) SeekBar industrialZoneInput;
  @BindView(R.id.industrial_zone_text) TextView industrialZoneText;
  @BindView(R.id.air_pollution) SeekBar airPollutionInput;
  @BindView(R.id.air_pollution_text) TextView airPollutionText;
  @BindView(R.id.num_rooms) SeekBar numRoomsInput;
  @BindView(R.id.num_rooms_text) TextView numRoomsText;
  @BindView(R.id.old_houses_proportion) SeekBar oldHousesInput;
  @BindView(R.id.old_houses_proportion_text) TextView oldHousesText;
  @BindView(R.id.dis_employment) SeekBar disEmpInput;
  @BindView(R.id.dis_employment_text) TextView disEmpText;
  @BindView(R.id.accessibility_highway) SeekBar accessHighwayInput;
  @BindView(R.id.accessibility_highway_text) TextView accessHighwayText;
  @BindView(R.id.tax_rate) SeekBar taxRateInput;
  @BindView(R.id.tax_rate_text) TextView taxRateText;
  @BindView(R.id.pupil_teacher) SeekBar pupilTeacherRatioInput;
  @BindView(R.id.pupil_teacher_text) TextView pupilTeacherRatioText;
  @BindView(R.id.progress_overlay) View overlay;
  TensorFlowInferenceInterface inferenceInterface;
  Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_boston_housing);
    unbinder = ButterKnife.bind(this);
    getSupportActionBar().setTitle("Find a Deal");
    overlay.setVisibility(VISIBLE);
    subscription = new CompositeSubscription();
    subscription.add(Observable.just(new Object()).map(new Func1<Object, String>() {
      @Override public String call(Object s) {
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), "bostonhousing-frozen_model.pb");
        return "done";
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
      @Override public void call(String input) {
        if ("done".equals(input)) {
          overlay.setVisibility(GONE);
        }
      }
    }));

    crimeRateInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        crimeRateText.setText("crime rate per capita: " + progress / 10f);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    largeLotsInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        largeLotsText.setText("number of large residential lots in town: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    industrialZoneInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        industrialZoneText.setText("number of industrial zones in town: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    airPollutionInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        airPollutionText.setText("nitrogen oxides concentration: " + progress / 100f);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    numRoomsInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        numRoomsText.setText("num_rooms: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    oldHousesInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        oldHousesText.setText("proportion of units built prior to 1940: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    disEmpInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        disEmpText.setText("mean of distances to Boston employment centers: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    accessHighwayInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        accessHighwayText.setText("accessibility to radial highways: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    taxRateInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        taxRateText.setText("tax per $10,000: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    pupilTeacherRatioInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        pupilTeacherRatioText.setText("pupil to teacher ratio: " + progress);
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  @OnClick(R.id.btn) void predict(View v) {
    overlay.setVisibility(VISIBLE);
    final float crimeRate = crimeRateInput.getProgress() / 10f;
    final float largeLots = largeLotsInput.getProgress();
    final float industrialZone = industrialZoneInput.getProgress();
    final float airPollution = airPollutionInput.getProgress() / 100f;
    final float numRooms = numRoomsInput.getProgress();
    final float oldHouses = oldHousesInput.getProgress();
    final float disEmp = disEmpInput.getProgress() / 10f;
    final float accessHighway = accessHighwayInput.getProgress() / 10f;
    final float taxRate = taxRateInput.getProgress();
    final float pupilTeacherRatio = pupilTeacherRatioInput.getProgress();
    Log.d("", crimeRate
        + ", "
        + largeLots
        + ", "
        + industrialZone
        + ", "
        + airPollution
        + ", "
        + numRooms
        + ", "
        + oldHouses
        + ", "
        + disEmp
        + ", "
        + accessHighway
        + ", "
        + taxRate
        + ", "
        + pupilTeacherRatio);
    subscription.add(Observable.just(new Object()).map(new Func1<Object, String>() {
      @Override public String call(Object s) {
        inferenceInterface.feed("my_input/X",
            new float[] {crimeRate, largeLots, industrialZone, airPollution, numRooms, oldHouses, disEmp, accessHighway, taxRate, pupilTeacherRatio},
            1, 10);
        inferenceInterface.run(new String[] {"my_output/BiasAdd"});
        float[] output = new float[1];
        inferenceInterface.fetch("my_output/BiasAdd", output);

        return Math.round(output[0] * 1000) + "";
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
      @Override public void call(String input) {
        overlay.setVisibility(GONE);
        Log.d("", "house price: " + input);
        alert("Congratulation", "Your dream house costs $" + input, Color.BLUE);
      }
    }));

    //inferenceInterface.feed("my_input/X",
    //    new float[] {crimeRate, largeLots, industrialZone, airPollution, numRooms, oldHouses, disEmp, accessHighway, taxRate, pupilTeacherRatio}, 1,
    //    10);
    //inferenceInterface.run(new String[] {"my_output/BiasAdd"});
    //float[] output = new float[1];
    //inferenceInterface.fetch("my_output/BiasAdd", output);
    //alert("Congratulation", "Your dream house costs $" + Math.round(output[1] * 1000), Color.BLUE);
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
