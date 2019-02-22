package com.quickenloans.titanicsurvivalgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
  Unbinder unbinder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);
    getSupportActionBar().setTitle("Welcome to AI Games");
  }

  @OnClick(R.id.titanic_survival) void titanicSurvival(View v) {
    startActivity(new Intent(this, TitanicSurvivalActivity.class));
  }

  @OnClick(R.id.boston_housing) void bostonHousing(View v) {
    startActivity(new Intent(this, BostonHousingActivity.class));
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }
}
