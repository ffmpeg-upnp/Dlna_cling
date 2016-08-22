package uata.dmr;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.uata.adapter.DevAdapter;
import com.uata.adapter.GroupAdapter;
import com.uata.application.BaseApplication;
import com.uata.application.ConfigData;
import com.uata.dlna_application.ControlActivity;
import com.uata.dlna_application.R;
import com.uata.dmc.GenerateXml;
import com.uata.dmp.ContentItem;
import com.uata.dmp.DeviceItem;
import com.uata.dms.ContentBrowseActionCallback;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:  GPlayer
 * @Description: (视频播放器)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:12:01 
 */

public class GPlayer extends Activity
    implements
      OnCompletionListener,
      OnErrorListener,
      OnInfoListener,
      OnPreparedListener,
      OnSeekCompleteListener,
      OnVideoSizeChangedListener,
      SurfaceHolder.Callback,
      MediaController.MediaPlayerControl,
      OnClickListener {


  private LinearLayout mlayoutBottom;
  private RelativeLayout mlayoutTop;
  private Button mleftButton;
  private View mviewparent;
  private ImageButton mimgBtnPause;
  private ProgressBar mlcbPreparing;
  private Button mbtnRight;
  private SeekBar mskBar;
//  private SeekBar mSeekBarSound;
  private ImageView mimgViewSound ;
  private ImageView mimgViewPushtv;
  private TextView mtxtInfo;
  private TextView mtxtProgress;
  private TextView mtxtViewLength;
  private TextView mtxtViewTime;
  private TextView mtxtVideoTitle;
  public PopupWindow mpopupWindow;
  private ListView lviDmr;
  private ListView lviGplayer;
  public DevAdapter mdmrDevAdapter;
  private String currentContentFormatMimeType = "";
  public String name;
  private String path;
  private int position;
  private String metaData;
  private String localUrl;
  private int intVideoId;
  // private static final int MEDIA_PLAYER_BUFFERING_UPDATE = 4001;
  // private static final int MEDIA_PLAYER_COMPLETION = 4002;
  // private static final int MEDIA_PLAYER_ERROR = 4003;
  // private static final int MEDIA_PLAYER_HIDDEN_CONTROL = 4009;
  // private static final int MEDIA_PLAYER_INFO = 4004;
  // private static final int MEDIA_PLAYER_PREPARED = 4005;
  private ArrayList<DeviceItem> popDmrList;
  private GroupAdapter groupAdapter;
  private ArrayList<ContentItem> arrMovieList = new ArrayList();
  private AndroidUpnpService upnpService;


  // private PlayBrocastReceiver playRecevieBrocast = new PlayBrocastReceiver();



  Display currentDisplay;
  SurfaceView surfaceView;
  SurfaceHolder surfaceHolder;
  MediaPlayer mediaPlayer;
  MediaController mediaController;
  int videoWidth = 0;
  int videoHeight = 0;
  boolean readyToPlay = false;
  String playUrl;
  // public static MediaListener mMediaListener;
  private boolean isMute;
  private AudioManager maudioManager;
  private int mintBackCount;
  private RelativeLayout mlayoutBuffer;

  private LinearLayout layoutTop;
  private LinearLayout  mlayoutBootom;
  ArrayAdapter<DeviceItem> adapter;
  private FrameLayout layoutDmrMovie;
  public static final  String LOGTAG = "Gnap-GPlayer";

  private AudioManager audioManager;// 音量管理者
  private int maxVolume;// 最大音量
  private int currentVolume;// 当前音量
  private SeekBar seekBarVolume;
  public ArrayList<DeviceItem> marrDevList = new ArrayList();
  private DeviceListRegistryListener deviceListRegistryListener;


  private Handler mhandler = new Handler() {
    boolean open;

    @Override
    public void handleMessage(Message msg) {

      switch (msg.what) {
        case 4006:
          if ((GPlayer.this.mediaPlayer != null)) {
            // Log.v("GPLAYER", "mediaPlayer--"+mediaPlayer);
            int time = GPlayer.this.mediaPlayer.getCurrentPosition();
            int totalTime = GPlayer.this.mediaPlayer.getDuration();
            // dmc的position
            // if(GPlayer.this.mediaPlayer!=null)
            // {
            // GPlayer.this.mMediaListener.positionChanged(i);
            // GPlayer.this.mMediaListener.durationChanged(j);
            // //
            // Log.v(LOGTAG, "mTextViewTime"+toTime(i));
            GPlayer.this.mtxtViewTime.setText(toTime(time));
            GPlayer.this.mtxtViewLength.setText(toTime(totalTime));
            GPlayer.this.mskBar.setMax(totalTime);
            GPlayer.this.mskBar.setProgress(time);
            GPlayer.this.mhandler.sendEmptyMessageDelayed(4006, 500L);
          } else {
            finish();
          }

          // }
          break;
        case 4001:

//          System.out.println(open);
          if (open == false) {
            GPlayer.this.mlayoutBootom.setVisibility(View.VISIBLE);
            open = true;

          } else {
            GPlayer.this.mlayoutBootom.setVisibility(View.INVISIBLE);
            open = false;
          }
          // GPlayer.this.top_layout.setVisibility(View.VISIBLE);
          break;
        default:
          break;
      }
    }

  };



  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();



    // if (mMediaListener != null){
    // mMediaListener.stop();
    // }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    exit();
    // unregisterBrocast();

  }

  /**
   * @see Activity#onTouchEvent(MotionEvent)
   * @Title: onTouchEvent
   * @Description:  (点击屏幕的触碰 事件)   *@param: @param ev  *@param: @return  
   * @throws 
   */
  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    // if (mediaController.isShowing()) {
    // mediaController.hide();
    // } else {
    // mediaController.show(10000);
    // }
    // return false;

    if (ev.getAction() == MotionEvent.ACTION_DOWN) {


      Toast.makeText(GPlayer.this, "点击成功", 0).show();
      this.mhandler.sendEmptyMessage(4001);
    }
    return false;
  }



  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // Auto-generated method stub
    Log.v(LOGTAG, "surfaceChanged Called");

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    // Auto-generated method stub
    Log.v(LOGTAG, "surfaceCreated Called");
    mediaPlayer.setDisplay(holder);

    try {

      mediaPlayer.prepare();

    } catch (IllegalStateException e) {
      Log.v(LOGTAG, e.getMessage());
      finish();
    } catch (IOException e) {
      Log.v(LOGTAG, e.getMessage());
      finish();
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // Auto-generated method stub
    Log.v(LOGTAG, "surfaceDestroyed Called");
  }


  @Override
  public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onVideoSizeChanged Called");
    // DisplayMetrics dm = new DisplayMetrics();
    //
    // getWindowManager().getDefaultDisplay().getMetrics(dm);
    //
    // mSurfaceViewWidth = dm.widthPixels;
    //
    // mSurfaceViewHeight = dm.heightPixels;
    //
    // if (width == 0 || height == 0) {
    //
    //
    //
    // return;
    //
    // }
    //
    //
    //
    // mIsVideoSizeKnown = true;
    //
    // mVideoHeight = height;
    //
    // mVideoWidth = width;
    //
    //
    //
    // int w = mSurfaceViewHeight * width / height;
    //
    // int margin = (mSurfaceViewWidth - w) / 2;
    //
    // Logger.d(TAG, "margin:" + margin);
    //
    // RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
    //
    // RelativeLayout.LayoutParams.MATCH_PARENT,
    //
    // RelativeLayout.LayoutParams.MATCH_PARENT);
    //
    // lp.setMargins(margin, 0, margin, 0);
    //
    // mSurfaceView.setLayoutParams(lp);
    //
    //
    //
    // if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
    //
    // startVideoPlayback();
    //
    // }

    LinearLayout.LayoutParams lp =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    surfaceView.setLayoutParams(lp);
    // this.mHandler.sendEmptyMessageDelayed(4006, 200L);
  }


  @Override
  public void onSeekComplete(MediaPlayer mp) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onSeekComplete Called");
  }



  /**
   * @see OnPreparedListener#onPrepared(MediaPlayer)
   * @Title: onPrepared
   * @Description:  (加载资源 调节屏幕大小并准备播放)   *@param: @param mp  
   * @throws 
   */
  @Override
  public void onPrepared(MediaPlayer mp) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onPrepared Called");
    // 按照视频本身大小的宽和高
    // videoWidth = mp.getVideoWidth();
    // videoHeight = mp.getVideoHeight();
    // 全屏播放
    videoHeight = currentDisplay.getHeight();
    videoWidth = currentDisplay.getWidth();

    // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
    if (videoWidth > currentDisplay.getWidth() || videoHeight > currentDisplay.getHeight()) {
      float heightRatio = (float) videoHeight / (float) currentDisplay.getHeight();
      float widthRatio = (float) videoWidth / (float) currentDisplay.getWidth();

      // 选择大的一个进行缩放
      if (heightRatio > 1.0F || widthRatio > 1.0F) {
        if (heightRatio > widthRatio) {
          videoHeight = (int) Math.ceil((float) videoHeight / (float) heightRatio);
          videoWidth = (int) Math.ceil((float) videoWidth / (float) heightRatio);
        } else {
          videoHeight = (int) Math.ceil((float) videoHeight / (float) widthRatio);
          videoWidth = (int) Math.ceil((float) videoWidth / (float) widthRatio);
        }
      }
    }
    // 设置surfaceView的布局参数
    surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    // surfaceView.getHolder().setFixedSize(400, 500);
    mp.start();
    this.mhandler.sendEmptyMessageDelayed(4006, 200L);
    // mediaController.setMediaPlayer(this);
    // mediaController.setAnchorView(this
    // .findViewById(R.id.gplayer_surfaceview));
    // mediaController.setEnabled(true);
    // mediaController.show(10000);



  }

  /**
   * @see OnInfoListener#onInfo(MediaPlayer, int, int)
   * @Title: onInfo
   * @Description:  (打印一些信息)   *@param: @param mp  *@param: @param whatInfo  *@param: @param extra
   *                 *@param: @return  
   * @throws 
   */
  @Override
  public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
    // Auto-generated method stub
    // 当一些特定信息出现或者警告时触发
    if (whatInfo == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
      Log.v(LOGTAG, "Media Info, Media Info Bad Interleaving " + extra);
    } else if (whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
      Log.v(LOGTAG, "Media Info, Media Info Not Seekable " + extra);
    } else if (whatInfo == MediaPlayer.MEDIA_INFO_UNKNOWN) {
      Log.v(LOGTAG, "Media Info, Media Info Unknown " + extra);
    } else if (whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
      Log.v(LOGTAG, "MediaInfo, Media Info Video Track Lagging " + extra);
    } else if (whatInfo == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
      Log.v(LOGTAG, "MediaInfo, Media Info Metadata Update " + extra);
    }
    return false;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onCompletion Called");
    // if (PlayListener.getGstMediaPlayer() != null)
    // PlayListener.getGstMediaPlayer().transportStateChanged(TransportState.NO_MEDIA_PRESENT);
    // finish();
    // if(mMediaListener !=null)
    // {
    // mMediaListener.endOfMedia();
    // exit();
    //
    // }
  }

  @Override
  public boolean onError(MediaPlayer mp, int whatError, int extra) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onError Called");
    if (whatError == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
      Log.v(LOGTAG, "Media Error, Server Died " + extra);
    } else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
      Log.v(LOGTAG, "Media Error, Error Unknown " + extra);
    }
    return false;
  }



  /**
   * Implements interface
   */

  @Override
  protected void onNewIntent(Intent intent) {
    // Auto-generated method stub
    super.onNewIntent(intent);

    this.playUrl = intent.getStringExtra("playURI");
    if (!TextUtils.isEmpty(this.playUrl)) {
      setUri(this.playUrl);
      // setTitle(intent);
    }
  }

  @Override
  public boolean canPause() {
    // Auto-generated method stub
    return true;
  }

  @Override
  public boolean canSeekBackward() {
    // Auto-generated method stub
    return true;
  }

  @Override
  public boolean canSeekForward() {
    // Auto-generated method stub
    return true;
  }

  @Override
  public int getBufferPercentage() {
    // Auto-generated method stub
    return 0;
  }

  // 保存播放位置
  @Override
  public int getCurrentPosition() {
    // Auto-generated method stub
    return mediaPlayer.getCurrentPosition();
  }

  @Override
  public int getDuration() {
    // Auto-generated method stub
    return mediaPlayer.getDuration();
  }

  @Override
  public boolean isPlaying() {
    // Auto-generated method stub
    return mediaPlayer.isPlaying();
  }

  @Override
  public void pause() {
    // Auto-generated method stub
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.pause();

      // if (mMediaListener != null)
      // mMediaListener.pause();
    }
  }

  @Override
  public void seekTo(int pos) {
    // Auto-generated method stub
    mediaPlayer.seekTo(pos);
    // if(mMediaListener!=null)
    // {
    // mMediaListener.positionChanged(pos);
    // }
  }

  @Override
  public void start() {
    // Auto-generated method stub
    Log.v(LOGTAG, "start is call");

    mediaPlayer.start();
    // this.mHandler.sendEmptyMessage(4006);
    this.mhandler.sendEmptyMessageDelayed(4006, 200L);
    // if(mMediaListener!=null)
    // {
    // mMediaListener.start();
    // }
  }

  public void stop() {

    // if(mMediaListener!=null)
    // {
    // mMediaListener.stop();
    //
    // }
  }

  @Override
  public int getAudioSessionId() {
    // Auto-generated method stub
    return 0;
  }


  /**
   * custom
   */

  public void registerBrocast() {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("cn.fnic.xbk.msi.action.dmr");
    localIntentFilter.addAction("cn.fnic.xbk.msi.action.video.play");
    // registerReceiver(this.playRecevieBrocast, localIntentFilter);
  }

  // public void unregisterBrocast()
  // {
  // unregisterReceiver(this.playRecevieBrocast);
  // }

  // 获取资源的url
  public void setUri(String paramString) {
    try {
      this.mediaPlayer.reset();
      this.playUrl = paramString;
      this.mediaPlayer.setDataSource(this.playUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // public static abstract interface MediaListener
  // {
  // public abstract void durationChanged(int paramInt);
  //
  // public abstract void endOfMedia();
  //
  // public abstract void pause();
  //
  // public abstract void positionChanged(int paramInt);
  //
  // public abstract void start();
  //
  // public abstract void stop();
  // }

  /**
   * @Title: initControl
   * @Description:  (初始化 控制器的控件)   *@param:     *@return: void
   * @throws 
   */

  private void initControl() {
    Log.v(LOGTAG, "initControl");
    this.mediaController = new MediaController(this);
    // top_layout=(LinearLayout) findViewById(R.id.top_layout);
    mlayoutBootom = (LinearLayout) findViewById(R.id.bottom_layout);

    // this.dmrMovie_List=(FrameLayout) findViewById(R.id.dmrlist_Fl);
    this.mlayoutBuffer = (RelativeLayout) findViewById(R.layout.player_buffer);
    this.mlcbPreparing = (ProgressBar) findViewById(R.id.player_prepairing);
    this.mtxtProgress = ((TextView) findViewById(R.id.prepare_progress));
    this.mtxtInfo = ((TextView) findViewById(R.id.info));
    this.mlayoutTop = ((RelativeLayout) findViewById(R.layout.player_phone_popup_top));
    // this.mVideoTitle = ((TextView)findViewById(R.id.video_title));
    // this.mLeftButton = ((Button)findViewById(R.id.topBar_back));
    // this.mRightButton = ((Button)findViewById(R.id.topBar_list_switch));
    this.lviDmr = (ListView) findViewById(R.id.dmr_list);
    this.mimgViewPushtv = (ImageView) findViewById(R.id.push);
    this.lviGplayer = (ListView) findViewById(R.id.gplay_list);
    this.mimgViewPushtv.setOnClickListener(this);
    // this.mLeftButton.setOnClickListener(this);
    // this.mRightButton.setOnClickListener(this);

    this.mtxtViewTime = ((TextView) findViewById(R.id.current_time));
    this.mtxtViewLength = ((TextView) findViewById(R.id.totle_time));
    this.mimgBtnPause = ((ImageButton) findViewById(R.id.play));
    this.mimgBtnPause.setOnClickListener(this);
    this.mlayoutBottom = (LinearLayout) findViewById(R.layout.player_phone_popup_bottom);
    this.mtxtProgress = ((TextView) findViewById(R.id.prepare_progress));
    this.mtxtInfo = ((TextView) findViewById(R.id.info));
    this.mskBar = ((SeekBar) findViewById(R.id.seekBar_progress));

    // this.mSound=(ImageView) findViewById(R.id.sound);
    // mSound.setOnClickListener(this);
    // this.mSeekBarSound = ((SeekBar)findViewById(R.id.seekBar_sound));
    // audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    // maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获得最大音量
    // currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
    // mSeekBarSound.setMax(maxVolume);
    // mSeekBarSound.setProgress(currentVolume);
    // mSeekBarSound.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
    // {
    //
    // @Override
    // public void onStopTrackingTouch(SeekBar seekBar)
    // {
    // // Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onStartTrackingTouch(SeekBar seekBar)
    // {
    // // Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onProgressChanged(SeekBar seekBar, int progress,
    // boolean fromUser)
    // {
    // // Auto-generated method stub
    // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
    // progress, AudioManager.FLAG_ALLOW_RINGER_MODES);
    // }
    // });

    this.mskBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        // Auto-generated method stub

        mskBar.setProgress(seekBar.getProgress());


        GPlayer.this.seekTo(seekBar.getProgress());

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // Auto-generated method stub

      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Auto-generated method stub

      }
    });



  }

  // 设置媒体监听
  // public static void setMediaListener(MediaListener paramMediaListener)
  // {
  // mMediaListener=paramMediaListener;
  // }
  //
  // private void setTitle(Intent intent)
  // {
  // String str =intent.getStringExtra("name");
  // if(!TextUtils.isEmpty(str))
  // {
  // this.mVideoTitle.setText(str);
  // }
  // }

  private void updatePausePlay() {
    if ((this.mediaPlayer == null) || (this.mimgBtnPause == null)) {
      return;
    }

    else if (this.mediaPlayer.isPlaying()) {

      Log.v(LOGTAG, "updatePausePlay is call");
      this.mimgBtnPause.setBackgroundResource(R.drawable.phone_480_play);

    } else {
      this.mimgBtnPause.setBackgroundResource(R.drawable.phone_480_pause);
    }
  }


  private void doPauseResume() {
    Log.v(LOGTAG, "doPauseResume is call");
    if (this.mediaPlayer == null) {
      return;
    }


    updatePausePlay();
    if (this.mediaPlayer.isPlaying()) {
      this.mediaPlayer.pause();
      // if(mMediaListener!=null)
      // {
      // mMediaListener.pause();
      // }
    } else if (!this.mediaPlayer.isPlaying()) {
      this.mediaPlayer.start();
    }
  }

  private void exit() {
    if (this.mediaPlayer != null) {
      this.mediaPlayer.release();
      this.mediaPlayer = null;
    }
    // mMediaListener=null;
    finish();
  }


  /**
   * @Title: initPopuptWindow
   * @Description:  (弹出菜单窗口)   *@param:     *@return: void
   * @throws 
   */

  private void initPopuptWindow() {

   
    // activity.getDmrList();
    this.popDmrList = ConfigData.arrDmrList;
    Log.v(LOGTAG, "pop---" + this.popDmrList);
    View popupWindow_View;

    if (this.mpopupWindow == null) {
      // 获取自定义布局文件activity_popupwindow_left.xml的视图
      popupWindow_View = getLayoutInflater().inflate(R.layout.player_phone_popup_top, null);


      this.lviGplayer = (ListView) popupWindow_View.findViewById(R.id.gplay_list);

      groupAdapter = new GroupAdapter(GPlayer.this, this.popDmrList);

      this.lviGplayer.setAdapter(groupAdapter);
      // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
      this.mpopupWindow =
          new PopupWindow(popupWindow_View, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }



    // popupWindow.setFocusable(true);
    // popupWindow.setOutsideTouchable(true);
    // int[] location = new int[2];
    // this.push_tv.getLocationOnScreen(location);
    GPlayer.this.mpopupWindow.showAtLocation(this.mimgViewPushtv, Gravity.NO_GRAVITY, 0, 0);



    // pop.showAtLocation(findViewById(R.id.btLl), Gravity.BOTTOM, 0, 0);
    // parent=findViewById(R.id.top_layout);
    // GPlayer.this.popupWindow.showAsDropDown( GPlayer.this.mRightButton, 50, 1);
    // this.popupWindow.showAsDropDown(parent, 50, 1);



    this.lviGplayer.setOnItemClickListener(new OnItemClickListener(

    ) {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Auto-generated method stub
        Toast.makeText(GPlayer.this, "您选择了第" + arg2 + "播放器", 0).show();
        if (arg2 == 0) {

        } else {

          BaseApplication.dmrDeviceItem = (DeviceItem) GPlayer.this.popDmrList.get(arg2);
          Intent intent = new Intent(GPlayer.this, ControlActivity.class);
          intent.putExtra("ID", String.valueOf(position));
          intent.putExtra("name", name);
          intent.putExtra("playURI", playUrl);
          intent.putExtra("currentContentFormatMimeType", currentContentFormatMimeType);
          intent.putExtra("metaData", metaData);

          Log.v(LOGTAG, "" + position);
          Log.v(LOGTAG, "" + name);
          Log.v(LOGTAG, "" + playUrl);
          Log.v(LOGTAG, "" + currentContentFormatMimeType);



          // System.out.println(metaData);
          // IndexActivity.mTabhost.setCurrentTabByTag(getString(R.string.control));
          // Log.v("222222", "22222222222222");
          // IndexActivity.setSelect();
          // Log.v("333333333333", "3333333333333333333");
          // sendBroadcast(intent);
          mpopupWindow.dismiss();
          startActivity(intent);
          finish();

        }
      }
    });



  }

  /**
   * @Title: getURLVideo
   * @Description:  (获取查询本地服务器的资源)   *@param:     *@return: void
   * @throws 
   */

  private void getURLVideo() {
    ContentItem localItem = GPlayer.this.arrMovieList.get(intVideoId);

    if (localItem.isContainer()) {
      GPlayer.this.upnpService.getControlPoint().execute(
          new ContentBrowseActionCallback(GPlayer.this, localItem.getService(), localItem
              .getContainer(), GPlayer.this.arrMovieList, GPlayer.this.mhandler));



    } else {
      Log.v("localContentItem", localItem.getItem() + "");
      List<Res> localList = localItem.getItem().getResources();
      Res type = localList.get(0);
      MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      String str = localMimeType.getType();

      this.name = localItem.toString();
      this.playUrl = localItem.getItem().getFirstResource().getValue();
      this.position = intVideoId;
      this.currentContentFormatMimeType = localMimeType.toString();
      try {
        metaData = new GenerateXml().generate(localItem).toString();
      } catch (Exception e) {
        // Auto-generated catch block
        e.printStackTrace();
      }

    }

  }



  @Override
  public void onClick(View view) {
    // Auto-generated method stub
    switch (view.getId()) {
    // case R.id.topBar_back:
    // exit();
    // break;
      case R.id.play:

        doPauseResume();
        break;
      case R.id.push:
//        System.out.println("点击push");
        getPopupWindow();
        break;
      default:
        break;
    }
  }

  // popwindow 的显示/隐藏
  protected void getPopupWindow() {
    Log.v("popwindow", mpopupWindow + "");

    if (null != mpopupWindow) {
      mpopupWindow.dismiss();
      mpopupWindow = null;
      return;
    } else {
      initPopuptWindow();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gplayer);
    arrMovieList = ConfigData.arrlistcontent;
    this.maudioManager = ((AudioManager) getSystemService("audio"));
    // surfaceView 设置
    surfaceView = (SurfaceView) findViewById(R.id.gplayer_surfaceview);
    surfaceHolder = surfaceView.getHolder();
    surfaceHolder.addCallback(this);
    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    mediaPlayer = new MediaPlayer();

    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnErrorListener(this);
    mediaPlayer.setOnInfoListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnSeekCompleteListener(this);
    mediaPlayer.setOnVideoSizeChangedListener(this);

    mediaController = new MediaController(this);



    Intent intent = getIntent();
    localUrl = intent.getStringExtra("localURL");
    intVideoId = Integer.parseInt(intent.getStringExtra("videoID"));
    // playURI = intent.getStringExtra("playURI");
    // this.name =intent.getStringExtra("name");
    // this.currentContentFormatMimeType = intent.getStringExtra("currentContentFormatMimeType");
    // this.metaData = intent.getStringExtra("metaData");
    // this.position=Integer.parseInt(intent.getStringExtra("ID"));
    try {
      mediaPlayer.setDataSource(localUrl);
      setUri(this.localUrl);
      // setTitle(intent);
    } catch (IllegalArgumentException e) {
      Log.v(LOGTAG, e.getMessage());
      finish();
    } catch (IllegalStateException e) {
      Log.v(LOGTAG, e.getMessage());
      finish();
    } catch (IOException e) {
      Log.v(LOGTAG, e.getMessage());
      finish();
    }
    currentDisplay = getWindowManager().getDefaultDisplay();
    initControl();
    getURLVideo();
    deviceListRegistryListener = new DeviceListRegistryListener();// 可以在任何时刻添加与检索设备
    registerBrocast();
  }


  /**
   * 
   * @author Administrator 远端设备和本地设备的搜索
   *
   */
  // 监听设备的移除与添加
  public class DeviceListRegistryListener extends DefaultRegistryListener {



    /* Discovery performance optimization for very slow Android devices! */
    public DeviceListRegistryListener() {

    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {

    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device,
        final Exception ex) {}

    /*
     * End of optimization, you can remove the whole block if your Android handset is fast (>= 600
     * Mhz)
     */

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaServer")) {
        final DeviceItem display =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        deviceAdded(display);
      }
      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaRenderer")) {
        final DeviceItem display =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        dmrAdded(display);
      }
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
      final DeviceItem display = new DeviceItem(device, device.getDisplayString());
      deviceRemoved(display);

      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaRenderer")) {
        final DeviceItem dis =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        dmrRemoved(dis);
      }

    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
      // Log.v(LOGTAG, "localDeviceAdder is called");
      final DeviceItem display =
          new DeviceItem(device, device.getDetails().getFriendlyName(), device.getDisplayString(),
              "(REMOTE) " + device.getType().getDisplayString());
      deviceAdded(display);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
      final DeviceItem display = new DeviceItem(device, device.getDisplayString());
      deviceRemoved(display);
    }

    public void deviceAdded(final DeviceItem di) {
      // Log.v(LOGTAG, "deviceAdded is called");

      runOnUiThread(new Runnable() {
        public void run() {

          // int position = deviceListAdapter.getPosition(di);
          // Log.v(LOGTAG, "deviceAdded-----"+position);
          // if (position >= 0) {
          // // Device already in the list, re-set new value at same
          // // position
          //
          // deviceListAdapter.remove(di);
          // deviceListAdapter.insert(di, position);
          // } else {
          // DeviceActivity.this.mDevList.add(di);
          // if(!DeviceActivity.this.mDmrList.contains(di))
          // {
          marrDevList.add(di);
          BaseApplication.deviceItem = (DeviceItem) GPlayer.this.marrDevList.get(0);
          // DeviceActivity.this.handler.sendEmptyMessage(2);

          // }
          // deviceListAdapter.add(di);
          // deviceListAdapter.notifyDataSetChanged();
          // }

          // Sort it?
          // listAdapter.sort(DISPLAY_COMPARATOR);
          // listAdapter.notifyDataSetChanged();
        }
      });
    }

    public void deviceRemoved(final DeviceItem di) {
      runOnUiThread(new Runnable() {
        public void run() {
          // mDevList.remove(di);
          // deviceListAdapter.remove(di);
          marrDevList.remove(di);
          // deviceListAdapter.notifyDataSetChanged();
        }
      });
    }


    public void dmrAdded(final DeviceItem di) {
      // Log.v(LOGTAG, "dmrAdded is called");

      runOnUiThread(new Runnable() {

        @Override
        public void run() {
          // Auto-generated method stub
          // int position=mDmrDevAdapter.getPosition(di);
          // if(position>=0)
          // {
          // mDmrDevAdapter.remove(di);
          // mDmrDevAdapter.insert(di, position);
          // }else
          // {
          // Log.v(TAG, "DMR ---"+ StartActivity.this.mDmrDevAdapter);
          // DeviceActivity.this.mDmrDevAdapter.add(di);
          popDmrList.add(di);
          Log.v(LOGTAG, "StartActivity dmr--" + popDmrList + "");


          // BaseApplication.dmrDevice=mDevList;

          // mDmrDevAdapter.notifyDataSetChanged();

          // }
        }
      });
    }

    public void dmrRemoved(final DeviceItem di) {
      runOnUiThread(new Runnable() {

        @Override
        public void run() {
          // Auto-generated method stub


          popDmrList.remove(di);
          // mDmrDevAdapter.remove(di);
        }
      });
    }
  }


  /**
   * 时间格式转换
   * 
   * @param time
   * @return
   */
  public static String toTime(int min) {
    int time = min;
    time /= 1000;
    int minute = time / 60;
    int hour = minute / 60;
    int second = time % 60;
    minute %= 60;
    return String.format("%02d:%02d", minute, second);
  }
}
