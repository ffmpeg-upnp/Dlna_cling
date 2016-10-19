package com.wsd.sun.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.wsd.sun.myapplication.ui.activity.media.ImageDisplay;
import com.wsd.sun.myapplication.ui.activity.media.textActivity;
import com.wsd.sun.myapplication.utils.Contants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wsd.sun.myapplication.utils.Contants.URL_SOHU;
import static com.wsd.sun.myapplication.utils.Contants.URL_STORM;

public class MainActivity extends Activity {

    @BindView(R.id.main_first) TextView tvPPtv;
    @BindView(R.id.main_second) TextView tvQQ;
    @BindView(R.id.main_third) TextView tvSOHU;
    @BindView(R.id.main_four) TextView tvSTORM;
    @BindView(R.id.main_music) TextView tvMusic;
    @BindView(R.id.main_video) TextView tvVideo;
    @BindView(R.id.main_img) TextView tvImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
    }

   @OnClick(R.id.main_first) void submit(){
       Intent intent = getPackageManager().getLaunchIntentForPackage("com.pplive.androidphone");
       if (intent != null) {

           startActivity(intent);
       } else {

           startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Contants.URL_PPTV)));
       }
   }
    @OnClick(R.id.main_second) void second(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.pplive.androidphone");
        if (intent != null) {

            startActivity(intent);
        } else {

            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Contants.URL_QQ)));
        }
   }
    @OnClick(R.id.main_third) void third(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.sohu.sohuvideo");
        if (intent != null) {

            startActivity(intent);
        } else {

            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_SOHU)));

        }
   }
    @OnClick(R.id.main_four) void four(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.storm.smart");
        if (intent != null) {

            startActivity(intent);
        } else {

            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_STORM)));
        }
    }


    @OnClick(R.id.main_music) void music(){

    }
    @OnClick(R.id.main_video) void video(){
        Intent intent = new Intent(this, textActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.main_img) void img(){
     Intent intent = new Intent(this, ImageDisplay.class);
        startActivity(intent);
    }


}
