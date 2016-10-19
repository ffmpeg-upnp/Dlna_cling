package com.wsd.sun.myapplication.ui.activity.dmc;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wsd.sun.myapplication.R;
import com.wsd.sun.myapplication.ui.activity.BaseApplication;
import com.wsd.sun.myapplication.ui.activity.dmp.ContentItem;
import com.wsd.sun.myapplication.ui.activity.dmp.DeviceItem;
import com.wsd.sun.myapplication.utils.ConfigData;
import com.wsd.sun.myapplication.utils.Utils;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.util.ArrayList;




/**
 * @ClassName:  ControlActivity
 * @Description: (控制器界面 可控制远端dmr)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午1:58:22 
 */
public class ControlActivity extends Activity implements OnClickListener {


  private ImageButton imgBtnCut;
  private ImageButton imgBtnPlus;
  private ImageButton imgBtnForward;
  private ImageButton imgBtnReplay;
  
  private ImageView imgViewPlay;
  private ImageView imgViewUp;
  private ImageView imgViewDown;
  private ImageView imgViewLeft;
  private ImageView imgViewRight;
  private ImageView  imgViewOk;
  
  private TextView txtTime;
  private TextView txtTotalTime;
  private TextView txtMediaTitle;
  private SeekBar skBarSeekbar;
  public static boolean isplay = false;
  private static final String TAG = "VideoControl";
  private DMCControl dmcControl ;
  private DeviceItem dmrDeviceItem = null;
  private boolean isUpdatePlaySeek = true;
  public ArrayList<ContentItem> listcontent;
  private String currentContentFormatMimeType = "";
  public String name;
  private String path;
  private int position;
  private String metaData;
  private String str1;
  private String str2;
  private ArrayList<DeviceItem> popDmrList;
  // private ProgressDialog progDialog = null;
  // private Timer timer;

  private AndroidUpnpService upnpService = null;

  /**
   * @Fields updatePlayTime :  接收消息然后实时同步更新进度条的时间   
   */
  private BroadcastReceiver updatePlayTime = new BroadcastReceiver() {



    @Override
    public void onReceive(Context context, Intent intent) {
      // Auto-generated method stub

      if (intent.getAction().equals("cn.fnic.xbk.msi.action.play.update")) {
        // Log.d("cn.fnic.xbk.msi.action.play.update", "cn.fnic.xbk.msi.action.play.update");
        if (ControlActivity.this.isUpdatePlaySeek) {
          Bundle localBundle = intent.getExtras();
          str1 = localBundle.getString("TrackDuration");// 媒体总时间
          str2 = localBundle.getString("RelTime");// 播放中的时间
          int time = Utils.getRealTime(str1);// 拿到dmr的播放时间

          int totalTime = Utils.getRealTime(str2);
          // Log.v("iiiiiii", i+"");
          // Log.v("jjjjjjj", j+"");
          ControlActivity.this.skBarSeekbar.setMax(time);
          ControlActivity.this.skBarSeekbar.setProgress( totalTime);
          ControlActivity.this.txtTotalTime.setText(str1);
          ControlActivity.this.txtTime.setText(str2);

          // if (!ControlActivity.this.initGetMute)
          // {
          // ControlActivity.this.initGetMute = true;
          // //ControlActivity.this.dmcControl.getMute();
          // }
          //
          if (str1.equals(str2)) {
            Log.v("play finished", "play finished");
            ControlActivity.this.dmcControl.play();
          }
          //
        }
      }
      if (intent.getAction().equals("com.continue.display")) {
        ControlActivity.this.isUpdatePlaySeek = true;

      }
      // if(intent.getAction().equals("com.transport.info"))
      // {
      // Log.v(TAG, "com.transport.info  is one");
      // ControlActivity.this.initDate(intent);
      // }


      if (intent.getAction().equals("cn.fnic.xbk.msi.action.video.play.error")) {
        Toast.makeText(ControlActivity.this, "play ertor",Toast.LENGTH_LONG).show();

      }
    }

  };



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.control);

    this.popDmrList = ConfigData.arrDmrList;
    initDate();
    initView();
    this.txtMediaTitle.setText(name);
    this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_pause);
    IntentFilter localIntent = new IntentFilter();
    localIntent.addAction("cn.fnic.xbk.msi.action.play.update");
    // localIntent.addAction("com.transport.info");

    // localIntent.addAction("cn.fnic.xbk.msi.action.video.play.error");
    registerReceiver(updatePlayTime, localIntent);
    this.listcontent = ConfigData.arrlistcontent;

    Log.v(TAG, "listcontent Size" + this.listcontent.size());
    Log.v(TAG, "Video oncreate is one");
    Log.v(TAG, "Video oncreate is call");
  }



  @Override
  protected void onDestroy() {
    // Auto-generated method stub
    super.onDestroy();
    /**
     * tabhost不能调用ondestroy 所以只能在 DeviceActivity中停止播放代替
     * */
    // unregisterReceiver(updatePlayTime);
    // DMCControlMessage.runing = false;
    // if (this.dmcControl != null)
    // this.dmcControl.stop(Boolean.valueOf(false));
    BaseApplication.dmrDeviceItem = (DeviceItem) ControlActivity.this.popDmrList.get(0);
    DMCControl.isExit = true;
  }

  /**
   * 
   * @Title: initDate
   * @Description:  (初始化 控件和一些传递过来的数值)   *@param:     *@return: void
   * @throws 
   */

  private void initDate() {
    Log.v(TAG, "initDate is call");
    Intent intent = getIntent();
    if (intent == null) {
      Toast.makeText(ControlActivity.this, "数据加载异常", Toast.LENGTH_LONG).show();
    } else {

      this.path = intent.getStringExtra("playURI");
      this.name = intent.getStringExtra("name");
      this.currentContentFormatMimeType = intent.getStringExtra("currentContentFormatMimeType");
      this.metaData = intent.getStringExtra("metaData");
      this.position = Integer.parseInt(intent.getStringExtra("ID"));

      Log.v(TAG, "" + path);
      Log.v(TAG, name);
      Log.v(TAG, currentContentFormatMimeType);
      Log.v(TAG, "position" + this.position);
      // Log.d(TAG, this.metaData);



      // this.mNameTitle.setText(this.name);
      // this.mAuthorName.setText(this.name);

      if ((this.path != null) && (this.currentContentFormatMimeType != null)
          && (this.metaData != null)) {
        isplay = true;
        this.dmrDeviceItem = BaseApplication.dmrDeviceItem;
        this.upnpService = BaseApplication.upnpService;

        Log.v(TAG, "dmr==" + this.dmrDeviceItem + "------" + BaseApplication.dmrDeviceItem);
        Log.v(TAG, "upnpService==" + this.upnpService + "----------" + BaseApplication.upnpService);
        this.dmcControl =
            new DMCControl(this, 3, this.dmrDeviceItem, this.upnpService, this.path, this.metaData);
        this.dmcControl.getProtocolInfos(this.currentContentFormatMimeType);
        Log.v(TAG, "jump to dmcListener");
      }
    }

  }

  private void initView() {
    imgViewPlay = (ImageView) findViewById(R.id.music_play);
    imgBtnCut = (ImageButton) findViewById(R.id.media_iv_voc_cut);
    imgBtnPlus = (ImageButton) findViewById(R.id.media_iv_voc_plus);
    skBarSeekbar = (SeekBar) findViewById(R.id.media_seekBar);
    txtTime = (TextView) findViewById(R.id.media_tv_time);
    txtTotalTime = (TextView) findViewById(R.id.media_tv_total_time);
    txtMediaTitle = (TextView) findViewById(R.id.media_tv_title);
    imgBtnForward = (ImageButton) findViewById(R.id.music_foward);
    imgBtnReplay = (ImageButton) findViewById(R.id.music_rewind);

    imgViewRight = (ImageView) findViewById(R.id.right);
    imgViewLeft = (ImageView) findViewById(R.id.left);
    imgViewUp = (ImageView) findViewById(R.id.up);
    imgViewDown = (ImageView) findViewById(R.id.down);
    imgViewOk = (ImageView) findViewById(R.id.ok);



    imgViewPlay.setOnClickListener(this);
    imgBtnCut.setOnClickListener(this);
    imgBtnPlus.setOnClickListener(this);
    imgBtnForward.setOnClickListener(this);
    imgBtnReplay.setOnClickListener(this);
    skBarSeekbar.setOnSeekBarChangeListener(new PlaySeekBarListener());


    imgViewOk.setOnClickListener(this);
    imgViewLeft.setOnClickListener(this);
    imgViewRight.setOnClickListener(this);
    imgViewUp.setOnClickListener(this);
    imgViewDown.setOnClickListener(this);
  }



  private void playPause() {

    Log.v(TAG, isplay + " come  in");
    // 第一次点击进入时为 isplay=true
    if (isplay)// 判断语句当为true是才能执行语句
    {
      isplay = false;
      this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_play);
      this.dmcControl.pause();

      Log.v(TAG, isplay + "=====pause");

    } else {
      isplay = true;

      this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_pause);
      this.dmcControl.play();
      Log.v(TAG, isplay + "=======play");
    }



  }


  /**
   *@Title: playNext
   *@Description: 
   *@param:   
   *@return: void
   *@throws 
   */
  public void playNext() {


    Log.v(TAG, "position==" + this.position + "---listcontent--" + this.listcontent.size());
    if (this.position <= -2 + this.listcontent.size()) {
      this.position = (1 + this.position);

      if (this.position <= -1 + this.listcontent.size()) {


        try {
          setVideoFormat((ContentItem) this.listcontent.get(this.position));
          String name1 = ((ContentItem) this.listcontent.get(this.position)).toString();
          String str =
              (this.listcontent.get(this.position)).getItem().getFirstResource().getValue();
          String metaData1 =
              new GenerateXml().generate((ContentItem) this.listcontent.get(this.position))
                  .toString();
          Log.v(TAG, "下一首的content=-----" + str);
          Log.v(TAG, "歌曲的名称=-----" + name1);

          this.dmcControl.setCurrentPlayPath(((ContentItem) this.listcontent.get(this.position))
              .getItem().getFirstResource().getValue(), metaData1);

          this.dmcControl.getProtocolInfos(this.currentContentFormatMimeType);
          // nameTitle.setText(((ContentItem)this.listcontent.get(this.position)).toString());
          // this.playButton.setBackgroundResource(2130837616);
          this.txtMediaTitle.setText(name1);
          this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_pause);
        } catch (Exception e) {
          // Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  /**
   *@Title: rePlay
   *@Description: 
   *@param:   
   *@return: void
   *@throws 
   */
  public void rePlay() {
    if (this.position >= 1) {
      this.position = (-1 + this.position);

      if (this.position >= 0) {

        try {
          setVideoFormat((ContentItem) this.listcontent.get(this.position));
          new GenerateXml().generate((ContentItem) this.listcontent.get(this.position))
              .toString();
          this.dmcControl.setCurrentPlayPath(((ContentItem) this.listcontent.get(this.position))
              .getItem().getFirstResource().getValue());
          this.dmcControl.getProtocolInfos(this.currentContentFormatMimeType);
          // nameTitle.setText(((ContentItem)this.listcontent.get(this.position)).toString());
          // this.playButton.setBackgroundResource(2130837616);
          this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_pause);
        } catch (Exception e) {
          // Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }

  private void soundDown() {
    if (this.dmcControl != null) {
      this.dmcControl.getVolume(1);
    }
  }

  /**
   *@Title: setVideoFormat
   *@Description:  
   *@param: @param paramContentItem  
   *@return: void
   *@throws 
   */
  public void setVideoFormat(ContentItem paramContentItem) {
    MimeType localMimeType =
        ((Res) paramContentItem.getItem().getResources().get(0)).getProtocolInfo()
            .getContentFormatMimeType();
    if (localMimeType != null) {
      this.currentContentFormatMimeType = localMimeType.toString();

    }
  }

  // 静音
  private void soundMute() {
    if (this.dmcControl != null) {
      this.dmcControl.getMute();
    }
  }

  private void soundUp() {
    if (this.dmcControl != null) {
      this.dmcControl.getVolume(2);
    }
  }

  // 静音的判断方法
  public void setVideoRemoteMuteState(boolean paramBoolean) {
    // Auto-generated method stub

  }

  /**
   * @ClassName:  PlaySeekBarListener
   * @Description: (进度条)
   * @author: 王少栋 
   * @date:   2015年7月29日 下午2:03:48 
   */
  class PlaySeekBarListener implements OnSeekBarChangeListener {
    PlaySeekBarListener() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      // Auto-generated method stub
      // int max=media_seekbar.getMax();
      // int grt=media_seekbar.getProgress();
      // progress=(progress/max)*grt;
      // Log.v(TAG, ""+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      // Auto-generated method stub
      // ControlActivity.this.isUpdatePlaySeek=false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      // Auto-generated method stub
      // if(ControlActivity.this.dmcControl !=null)
      // {
      int max = skBarSeekbar.getMax();
      int grt = skBarSeekbar.getProgress();

//      String str1 = GPlayer.toTime(max);
//      String str2 = GPlayer.toTime(grt);

      Log.v(TAG, "max---" + max);
      Log.v(TAG, "grt---" + grt);

      int time = seekBar.getProgress();
      Log.v(TAG, "time---" + time);

      String str = Utils.secToTime(time);
      Log.v(TAG, "时间是====" + str);

      ControlActivity.this.dmcControl.seekBarPosition(str);
      // }
    }

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.media_iv_voc_cut:
        soundDown();
        break;
      case R.id.media_iv_voc_plus:
        soundUp();
        break;
      case R.id.music_play:
        playPause();
        break;
      case R.id.music_foward:

        if (dmcControl != null) {
          dmcControl.stop(Boolean.FALSE);
        }
        playNext();
        Toast.makeText(this, "下一曲", Toast.LENGTH_LONG).show();
        break;
      case R.id.music_rewind:
        DMCControlMessage.runing = false;
        if (dmcControl != null) {
          dmcControl.stop(Boolean.FALSE);
        }
        rePlay();
        Toast.makeText(this, "上一曲", Toast.LENGTH_LONG).show();
        break;
      case R.id.ok:
        playPause();
        break;

      case R.id.left:

        if (dmcControl != null) {
          dmcControl.stop(Boolean.FALSE);
        }
        rePlay();
        break;
      case R.id.right:

        if (dmcControl != null) {
          dmcControl.stop(Boolean.FALSE);
        }
        playNext();
        Toast.makeText(this, "下一曲", Toast.LENGTH_LONG).show();
        break;
      case R.id.up:
        soundUp();
        break;
      case R.id.down:
        soundDown();
        break;


      default:
        break;
    }

  }
}
