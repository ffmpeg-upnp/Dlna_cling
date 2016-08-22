package uata.dmr;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.uata.LocalMedia.LocalMusic;
import com.uata.MediaModel.MediaAudio;
import com.uata.adapter.DevAdapter;
import com.uata.adapter.GroupAdapter;
import com.uata.application.BaseApplication;
import com.uata.application.ConfigData;
import com.uata.dlna_application.ControlActivity;
import com.uata.dlna_application.R;
import com.uata.dmc.DMCControl;
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
 * @ClassName:  MusicControl
 * @Description: (本地音乐的控制器，并且推送音乐到dmr)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:43:18 
 */


public class MusicControl extends Activity
    implements
      OnCompletionListener,
      OnErrorListener,
      OnInfoListener,
      OnPreparedListener,
      OnSeekCompleteListener,
      MediaController.MediaPlayerControl,
      OnClickListener {

  public static final  String TAG = "MusicControl";



  private ImageButton imgBtnCut;
  private ImageButton imgBtnPlus;
  private ImageButton imgBtnPause;

  private ImageView imgBtnForward;
  private ImageView imgBtnReplay;
  private ImageView imgViewPlay;
  private ImageView imgViewThumb;

  private TextView txtTime;
  private TextView txtTotalTime;
  private TextView txtMediaTitle;
  private TextView txtMediaAuthor;
  private SeekBar skBarSeekbar;

  private ArrayList<ContentItem> listcontent;
  public static boolean isplay = false;
  private GroupAdapter groupAdapter;
  public ArrayList<DeviceItem> arrDevList = new ArrayList();
  private DeviceListRegistryListener deviceListRegistryListener;
  private AndroidUpnpService upnpService;
  private ArrayList<ContentItem> arrAudioList;
  private Long albumId;
  private ArrayList<MediaAudio> arrAudioMedia;



  /**
   * @see Activity#onCreateOptionsMenu(Menu)
   * @Title: onCreateOptionsMenu
   * @Description:    *@param: @param menu  *@param: @return  
   * @throws 
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.music_control, menu);
    return true;
  }

  /**
   * @see Activity#onOptionsItemSelected(MenuItem)
   * @Title: onOptionsItemSelected
   * @Description:    *@param: @param item  *@param: @return  
   * @throws 
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.audio_push) {
      getPopupWindow();
      return true;
    }
    if (id == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }




 
//  private Button mRightButton;

  public PopupWindow popupWindow;
  private ListView lviDmr;
  private ListView  lviGplayer;
  public DevAdapter dmrDevAdapter;
  private String currentContentFormatMimeType = "";
  public String  strLocalName;
  public String strName;
  private String path;
  private int position;
  private String metaData;
  private String artist;

  private ArrayList<DeviceItem> popDmrList;

  // private PlayBrocastReceiver playRecevieBrocast = new PlayBrocastReceiver();



  Display currentDisplay;
  SurfaceView surfaceView;
  SurfaceHolder surfaceHolder;
  MediaPlayer mediaPlayer;
  MediaController mediaController;
  int videoWidth = 0;
  int videoHeight = 0;
  boolean readyToPlay = false;
  String playUri;

  private boolean isMute;

  private int mbackCount;
  private RelativeLayout layoutBuffer;
//  private volatile boolean mcanSeek = true;
  private LinearLayout layoutTop;
  private LinearLayout  layoutBootom;
  ArrayAdapter<DeviceItem> arrAdapter;
  private FrameLayout layoutDmrMovie;
  public final static String LOGTAG = "Gnap-GPlayer";

  private AudioManager audioManager;// 音量管理者
  private int maxVolume;// 最大音量
  private int currentVolume;// 当前音量
  private SeekBar seekBarVolume;

  /**
   * 
   * custom
   */

  private Handler mhandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {

      switch (msg.what) {
        case 4006:
          if ((MusicControl.this.mediaPlayer != null)) {
            // Log.v("GPLAYER", "mediaPlayer--"+mediaPlayer);
            int time = MusicControl.this.mediaPlayer.getCurrentPosition();
            int maxTime = MusicControl.this.mediaPlayer.getDuration();
            // dmc的position
            // if(GPlayer.this.mediaPlayer!=null)
            // {
            // MusicControl.this.mMediaListener.positionChanged(i);
            // MusicControl.this.mMediaListener.durationChanged(j);
            //
            // Log.v(LOGTAG, "mTextViewTime"+toTime(i));
            MusicControl.this.txtTime.setText(toTime(time));
            MusicControl.this.txtTotalTime.setText(toTime(maxTime));
            MusicControl.this.skBarSeekbar.setMax(maxTime);
            MusicControl.this.skBarSeekbar.setProgress(time);
            MusicControl.this.mhandler.sendEmptyMessageDelayed(4006, 500L);
          } else {
            finish();
          }

          // }
          break;
        case 4001:
         
          // MusicControl.this.bootom_layout.setVisibility(View.VISIBLE);
          // MusicControl.this.top_layout.setVisibility(View.VISIBLE);
          break;
        default:
          break;
      }
    }

  };

  /**
   * All rights reserved by uata.
   * @Title: setVideoFormat
   * @Description: 
   * @param paramContentItem  
   * @return: void
   * @throws 
   */
  public void setVideoFormat(ContentItem paramContentItem) {
    MimeType localMimeType =
        ((Res) paramContentItem.getItem().getResources().get(0)).getProtocolInfo()
            .getContentFormatMimeType();
    if (localMimeType != null) {
      this.currentContentFormatMimeType = localMimeType.toString();

    }
  }



  /**
   * button LISTENING
   */

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    exit();
    // unregisterBrocast();

  }

  // @Override
  // public boolean onTouchEvent(MotionEvent ev) {
  // if (mediaController.isShowing()) {
  // mediaController.hide();
  // } else {
  // mediaController.show(10000);
  // }
  // return false;

  // if(ev.getAction()== MotionEvent.ACTION_DOWN)
  // {
  //
  //
  // Toast.makeText(MusicControl.this, "点击成功", 0).show();
  // this.mHandler.sendEmptyMessage(4001);
  // }
  // return false;
  // }



  @Override
  public void onSeekComplete(MediaPlayer mp) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onSeekComplete Called");
  }


  // 是否加载并准备播放
  @Override
  public void onPrepared(MediaPlayer mp) {
    // Auto-generated method stub
    Log.v(LOGTAG, "onPrepared Called");

    mp.start();
    this.mhandler.sendEmptyMessageDelayed(4006, 200L);



  }

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
    exit();
    // finish();

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

  // 设置媒体监听
  // public static void setMediaListener(MediaListener paramMediaListener)
  // {
  // mMediaListener=paramMediaListener;
  // }

  private void setTitle(Intent intent) {
    String str = intent.getStringExtra("name");
    if (!TextUtils.isEmpty(str)) {
      this.txtMediaTitle.setText(str);
    }
  }

  private void updatePausePlay() {
    if ((this.mediaPlayer == null) || (this.imgBtnPause == null)) {
      return;
    }

    else if (this.mediaPlayer.isPlaying()) {

      Log.v(LOGTAG, "updatePausePlay is call");
      this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_play);

    } else {
      this.imgViewPlay.setBackgroundResource(R.drawable.phone_480_pause);
    }
  }

  public void playNext() {

    Log.v(TAG, "position==" + this.position + "---listcontent--" + this.listcontent.size());
    if (this.position <= -2 + this.listcontent.size()) {
      this.position = (1 + this.position);

      if (this.position <= -1 + this.listcontent.size()) {
        // String name1;
        // String str;
        //
        //
        // setVideoFormat((ContentItem)this.listcontent.get(this.position));
        // name1=((ContentItem)this.listcontent.get(this.position)).toString();
        // str = (this.listcontent.get(this.position)).getItem().getFirstResource().getValue();
        String str = this.arrAudioMedia.get(this.position).getStruri();
        Long album = this.arrAudioMedia.get(this.position).getLongalbumid();
        String name1 = this.arrAudioMedia.get(position).getStrName();
        Log.v(TAG, "下一首的content=-----" + str);
        // Log.v(TAG,"歌曲的名称=-----"+name1);

        // this.dmcControl.setCurrentPlayPath(((ContentItem)this.listcontent.get(this.position)).getItem().getFirstResource().getValue(),this.metaData);
        //
        // this.dmcControl.getProtocolInfos(this.currentContentFormatMimeType);
        // nameTitle.setText(((ContentItem)this.listcontent.get(this.position)).toString());
        // this.playButton.setBackgroundResource(2130837616);
        this.txtMediaTitle.setText(name1);
        this.imgViewPlay.setBackgroundResource(R.drawable.audio_play);
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();
        getThumb(this.position, album);
        setUri(str);

        // setTitle(name1);
        start();
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

        String str = this.arrAudioMedia.get(this.position).getStruri();
        Long album = this.arrAudioMedia.get(this.position).getLongalbumid();
        String name1 = this.arrAudioMedia.get(position).getStrName();
        // setVideoFormat((ContentItem)this.listcontent.get(this.position));
        // String name1=((ContentItem)this.listcontent.get(this.position)).toString();
        // String str =
        // (this.listcontent.get(this.position)).getItem().getFirstResource().getValue();
        // this.dmcControl.setCurrentPlayPath(((ContentItem)this.listcontent.get(this.position)).getItem().getFirstResource().getValue());
        // this.dmcControl.getProtocolInfos(this.currentContentFormatMimeType);
        // nameTitle.setText(((ContentItem)this.listcontent.get(this.position)).toString());
        // this.playButton.setBackgroundResource(2130837616);
        this.txtMediaTitle.setText(name1);
        this.imgViewPlay.setBackgroundResource(R.drawable.audio_play);
        stop();
        getThumb(this.position, album);
        setUri(str);
        // setTitle(name1);
        start();
      }
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
      // this.music_play.setBackgroundResource(R.drawable.phone_480_play);

    } else if (!this.mediaPlayer.isPlaying()) {
      this.mediaPlayer.start();
      // this.music_play.setBackgroundResource(R.drawable.phone_480_pause);
    }
  }

  private void playPause() {

    Log.v(TAG, isplay + " come  in");
    // 第一次点击进入时为 isplay=true
    if (isplay)// 判断语句当为true是才能执行语句
    {
      isplay = false;
      // this.music_play.setBackgroundResource(R.drawable.phone_480_play);
      this.imgViewPlay.setBackgroundResource(R.drawable.audio_play);
      // this.mediaPlayer.pause();
      this.mediaPlayer.start();
      // this.dmcControl.pause();

      Log.v(TAG, isplay + "=====pause");

    } else {
      isplay = true;


      this.imgViewPlay.setBackgroundResource(R.drawable.audio_stop);
      this.mediaPlayer.pause();
      // this.dmcControl.play();

      Log.v(TAG, isplay + "=======play");
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
   * 
   * Implements interface
   * 
   */



  @Override
  protected void onNewIntent(Intent intent) {
    // Auto-generated method stub
    super.onNewIntent(intent);

    this.playUri = intent.getStringExtra("playURI");
    if (!TextUtils.isEmpty(this.playUri)) {
      setUri(this.playUri);
      setTitle(intent);
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
    }

  }

  @Override
  public void seekTo(int pos) {
    // Auto-generated method stub
    mediaPlayer.seekTo(pos);

  }

  @Override
  public void start() {
    // Auto-generated method stub
    Log.v(LOGTAG, "start is call");
    try {
      mediaPlayer.prepare();
    } catch (IllegalStateException e) {
      // Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // Auto-generated catch block
      e.printStackTrace();
    }
    mediaPlayer.start();
    // this.mHandler.sendEmptyMessage(4006);
    this.mhandler.sendEmptyMessageDelayed(4006, 200L);

  }

  public void stop() {
    mediaPlayer.stop();

  }

  @Override
  public int getAudioSessionId() {
    // Auto-generated method stub
    return 0;
  }


  // public void registerBrocast()
  // {
  // IntentFilter localIntentFilter=new IntentFilter();
  // localIntentFilter.addAction("cn.fnic.xbk.msi.action.dmr");
  // localIntentFilter.addAction("cn.fnic.xbk.msi.action.video.play");
  // registerReceiver(this.playRecevieBrocast, localIntentFilter);
  // }
  //
  // public void unregisterBrocast()
  // {
  // unregisterReceiver(this.playRecevieBrocast);
  // }

  /**
   * 
   * initialize
   */

  // 获取资源的url
  public void setUri(String paramString) {
    try {
      this.mediaPlayer.reset();
      this.playUri = paramString;
      this.mediaPlayer.setDataSource(this.playUri);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void initControl() {
    Log.v(LOGTAG, "initControl");
    this.mediaController = new MediaController(this);
    this.listcontent = ConfigData.arrlistcontent;
    // Log.v(TAG, "音乐是："+ this.listcontent);

    // this.mBufferLayout=(RelativeLayout) findViewById(R.layout.player_buffer);
    // this.mProgressBarPreparing = (ProgressBar)findViewById(R.id.player_prepairing);
    // this.mTextProgress = ((TextView)findViewById(R.id.prepare_progress));
    // this.mTextInfo = ((TextView)findViewById(R.id.info));
    imgViewPlay = (ImageView) findViewById(R.id.audio_play);
    // media_iv_voc_cut=(ImageButton) findViewById(R.id.audio_iv_voc_cut);
    // media_iv_voc_plus=(ImageButton) findViewById(R.id.audio_iv_voc_plus);
    skBarSeekbar = (SeekBar) findViewById(R.id.audio_seekBar);
    txtTime = (TextView) findViewById(R.id.audio_tv_time);
    txtTotalTime = (TextView) findViewById(R.id.audio_tv_total_time);
    txtMediaTitle = (TextView) findViewById(R.id.audio_title);
    this.txtMediaAuthor = (TextView) findViewById(R.id.audio_author);
    imgBtnForward = (ImageView) findViewById(R.id.audio_foward);
    imgBtnReplay = (ImageView) findViewById(R.id.audio_rewind);
    imgViewThumb = (ImageView) findViewById(R.id.audio_iv_thumb);



    imgViewPlay.setOnClickListener(this);
    imgBtnForward.setOnClickListener(this);
    imgBtnReplay.setOnClickListener(this);

    skBarSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        // Auto-generated method stub

        skBarSeekbar.setProgress(seekBar.getProgress());

        MusicControl.this.mediaPlayer.seekTo(seekBar.getProgress());
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



  /**
   * pop
   * 
   */

  // popwindow 的显示/隐藏
  protected void getPopupWindow() {
    Log.v("popwindow", popupWindow + "");

    if (null != popupWindow) {
      popupWindow.dismiss();
      popupWindow = null;
      return;
    } else {
      initPopuptWindow();
    }
  }

  private void initPopuptWindow() {

   
    // activity.getDmrList();
    this.popDmrList = ConfigData.arrDmrList;
    Log.v(TAG, "pop---" + this.popDmrList);
    View popupWindowView;

    if (this.popupWindow == null) {
      // 获取自定义布局文件activity_popupwindow_left.xml的视图
      popupWindowView = getLayoutInflater().inflate(R.layout.player_phone_popup_top, null);


      this.lviGplayer = (ListView) popupWindowView.findViewById(R.id.gplay_list);

      groupAdapter = new GroupAdapter(MusicControl.this, this.popDmrList);

      this.lviGplayer.setAdapter(groupAdapter);
      // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
      this.popupWindow = new PopupWindow(popupWindowView, 350, LayoutParams.WRAP_CONTENT);
    }

    // MusicControl.this.popupWindow.showAtLocation(findViewById(R.id.audio), Gravity.RIGHT, 0, 0);
    MusicControl.this.popupWindow.showAsDropDown(findViewById(R.id.audio_push), 100, 0);
    this.lviGplayer.setOnItemClickListener(new OnItemClickListener(

    ) {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Auto-generated method stub
        Toast.makeText(MusicControl.this, "您选择了第" + arg2 + "播放器", 0).show();
        if (arg2 == 0) {

        } else {

          BaseApplication.dmrDeviceItem = (DeviceItem) MusicControl.this.popDmrList.get(arg2);
          Intent intent = new Intent(MusicControl.this, ControlActivity.class);
          intent.putExtra("ID", String.valueOf(position));
          intent.putExtra("name", strName);
          intent.putExtra("playURI", playUri);
          intent.putExtra("currentContentFormatMimeType", currentContentFormatMimeType);
          intent.putExtra("metaData", metaData);

          // System.out.println(metaData);
          // IndexActivity.mTabhost.setCurrentTabByTag(getString(R.string.control));
          // Log.v("222222", "22222222222222");
          // IndexActivity.setSelect();
          // Log.v("333333333333", "3333333333333333333");
          // sendBroadcast(intent);
          popupWindow.dismiss();
          startActivity(intent);
          finish();

        }
      }
    });

  }



  @Override
  public void onClick(View view) {
    // Auto-generated method stub
    DMCControl dmcControl = null;
    switch (view.getId()) {
    // case R.id.topBar_back:
    // exit();
    // break;
    // case R.id.audio_iv_voc_cut:
    // soundDown();
    // break;
    // case R.id.audio_iv_voc_plus:
    // soundUp();
    // break;
      case R.id.audio_play:
     
        playPause();
        break;
      case R.id.audio_foward:
        Toast.makeText(this, "下一曲", 0).show();

        playNext();
        break;
      case R.id.audio_rewind:

        Toast.makeText(this, "上一曲", 0).show();

        rePlay();
        break;
      default:
        break;
    }
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
          arrDevList.add(di);
          BaseApplication.deviceItem = (DeviceItem) MusicControl.this.arrDevList.get(0);
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
          arrDevList.remove(di);
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

    
   
   
    /**
     * All rights reserved by uata.
     * @Title: dmrRemoved
     * @Description: 
     * @param di  
     * @return: void
     * @throws 
     */
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
   * @Title: getAudio
   * @Description:  (获取服务器的对应id资源)   *@param:     *@return: void
   * @throws 
   */

  private void getAudio() {


    ContentItem localItem = MusicControl.this.arrAudioList.get(position);
    if (localItem.isContainer()) {
      MusicControl.this.upnpService.getControlPoint().execute(
          new ContentBrowseActionCallback(MusicControl.this, localItem.getService(), localItem
              .getContainer(), MusicControl.this.arrAudioList, MusicControl.this.mhandler));



    } else {
      Log.v("localContentItem", localItem.getItem() + "");
      List<Res> localList = localItem.getItem().getResources();
      Res type = localList.get(0);
      MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      String str = localMimeType.getType();

      this.playUri = localItem.getItem().getFirstResource().getValue();
      this.strName = localItem.toString();
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
  protected void onCreate(Bundle savedInstanceState) {
    // Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music_control);
    this.arrAudioList = ConfigData.arrlistcontent;
    this.arrAudioMedia = ConfigData.arrMediaAudio;
    this.audioManager = ((AudioManager) getSystemService("audio"));
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#007090")));
    initControl();
    mediaPlayer = new MediaPlayer();

    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnErrorListener(this);
    mediaPlayer.setOnInfoListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnSeekCompleteListener(this);
    // mediaPlayer.setOnVideoSizeChangedListener(this);

    mediaController = new MediaController(this);



    Intent intent = getIntent();
    path = intent.getStringExtra("path");
    this.position = Integer.parseInt(intent.getStringExtra("ID"));
    this.albumId = Long.valueOf(intent.getStringExtra("album_id"));
    strLocalName = intent.getStringExtra("name");
    this.artist = intent.getStringExtra("artist");
    Log.e(TAG, "localName--" + strLocalName);
    //
    // this.name =intent.getStringExtra("name");
    // this.currentContentFormatMimeType = intent.getStringExtra("currentContentFormatMimeType");
    // this.metaData = intent.getStringExtra("metaData");

    try {
      mediaPlayer.setDataSource(path);
      setUri(this.path);


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
    // currentDisplay = getWindowManager().getDefaultDisplay();

    this.txtMediaTitle.setText(strLocalName);;
    Log.e(TAG, "name==" + strLocalName);
    this.txtMediaAuthor.setText(artist);
    this.imgViewPlay.setBackgroundResource(R.drawable.audio_play);
    getThumb(position, albumId);
    try {
      mediaPlayer.prepare();
    } catch (IllegalStateException e1) {
      // Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e1) {
      // Auto-generated catch block
      e1.printStackTrace();
    }

    deviceListRegistryListener = new DeviceListRegistryListener();// 可以在任何时刻添加与检索设备
    getAudio();

    // registerBrocast();
  }

  // 专辑图片的加载
  private void getThumb(int songid, Long album) {
    int pos = -1;


    if (songid == pos) {
      imgViewThumb.setImageResource(R.drawable.music);
    } else {
      Bitmap bitmap = LocalMusic.getArtwork(MusicControl.this, songid, album, true, true);
      imgViewThumb.setImageBitmap(bitmap);
    }
  }

 
  /**
   *@Title: toTime
   *@Description: 时间格式转换
   *@param: @param time
   *@param: @return  
   *@return: String
   *@throws 
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
