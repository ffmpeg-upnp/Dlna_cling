package com.wsd.sun.myapplication.ui.activity.dmc;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wsd.sun.myapplication.ui.activity.dmp.DeviceItem;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;


/**
 * @ClassName:  DMCControl
 * @Description:控制器
 */
public class DMCControl {
  public static final  String TAG = "DMCControl";
  public static final int ADD_VOC = 1;
  public static final int CUT_VOC = 0;
  public static final int TYPE_AUDIO = 2;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_VIDEO = 3;
  public static boolean isExit = false;
  private Activity activity;
  private int controlType = 0;
  int currentPlayPosition;
  private static int currentVolume = 0;
  private DeviceItem executeDeviceItem;

  public boolean isGetNoMediaPlay = false;
  public boolean isMute = false;


  /**
   * 
   * 
   * getProtocolInfos-->2 setAvURL()--> 3
   * 
   * DMCControl.this.getTransportInfo(false);
   * 
   * DMCControl.this.stop(Boolean.valueOf(true));-->停止之前的播放
   * 
   * DMCControl.this.play();--> 12 getMediaInfo() } while (DMCControl.this.controlType == 1);
   * DMCControl.this.threadGetState = true; DMCControl.this.startThreadGetMessage();-->开始播放
   * 
   * 
   * */
  private Handler mhandle = new Handler() {
    public void handleMessage(Message paramMessage) {
      switch (paramMessage.what) {
//        case 0:
//          break;
//        case 4:
//          break;
//        case 5:
//          break;
//        case 6:
//          break;
//        case 8:
//          // DMCControl.this.currentVolume = paramMessage.getData().getInt("getVolume");
//          break;
//        case 9:
//          break;
        case 12:

          DMCControl.this.getMediaInfo();
          break;
//        case 14:
//
//          break;
//        case 15:
//          break;
//        case 16:
//          break;
//        case 17:
//          break;
//        case 18:
//          break;
//        case 19:
//          break;
//        case 20:
//          break;
//        case 21:
//          break;

        case 1:
          DMCControl.this.getTransportInfo(false);// 发送url的给dmr

          break;
        case 10:
          break;
        case 13:
          // Log.v(TAG, "-------------正在调节进度-----");
          // do{
          // DMCControl.this.getTransportInfo(false);
          // while (DMCControl.this.controlType == 1);
          // DMCControl.this.getPositionInfo();
          // }
          // while ((DMCControl.isExit) || (DMCControl.this.controlType == 1));
          DMCControl.this.threadGetState = true;
          DMCControl.this.startThreadGetMessage();
          DMCControl.this.mhandle.sendEmptyMessageDelayed(13, 500L);

          break;



        case 3:
          DMCControl.this.mhandle.sendEmptyMessageDelayed(13, 500L);
          // DMCControl.this.play();



          // DMCControl.this.stop(Boolean.valueOf(true));

          // DMCControl.this.getPositionInfo();
          DMCControl.this.play();// 启动dmr播放
          // }
          // while (DMCControl.this.controlType == 1);
          // DMCControl.this.threadGetState = true;
          // DMCControl.this.startThreadGetMessage();
          break;
        case 22:
          DMCControl.this.setPlayErrorMessage();
          DMCControl.this.stopGetPosition();
          break;
        case 11:
          DMCControl.this.isMute = paramMessage.getData().getBoolean("mute");
          DMCControl localDmcControl = DMCControl.this;

          boolean bool2 = false;
          if (DMCControl.this.isMute) 
          {

            localDmcControl.setMute(bool2);

            bool2 = true;
            return;
          }

        case 23:
          // DMCControl.this.isMute = paramMessage.getData().getBoolean("mute");
          // DMCControl.this.setMuteToActivity(DMCControl.this.isMute);
          break;
        case 2:
          DMCControl.this.setAvUri();
          break;
        case 7:

          // if (paramMessage.getData().getInt("isSetVolume") == 0)
          // {
          // DMCControl.this.setVolume(paramMessage.getData().getLong("getVolume"), 0);
          // return;
          // }
          // DMCControl.this.setVolume(paramMessage.getData().getLong("getVolume"), 1);
          // DMCControl.this.currentVolume = paramMessage.getData().getLong("getVolume");
          DMCControl.this.currentVolume = paramMessage.getData().getInt("getVolume");
          int isSetVolume = paramMessage.getData().getInt("isSetVolume");

          DMCControl.this.setVolume(DMCControl.this.currentVolume, isSetVolume);
          // if(i==1)
          // {
          //
          // DMCControl.this.setDownVolume(DMCControl.this.currentVolume, i);
          // }else{
          // DMCControl.this.setUPVolume(DMCControl.this.currentVolume, i);
          break;
          // }
        default:
          break;
      }
    }
      
  };
  private String metaData;
  String relTime;
  private boolean threadGetState = false;
  int totalPlayTime;
  String trackTime;
  private AndroidUpnpService upnpService;
  private String uriString;

  
  /**
   *@Title:  DMCControl 
   *@Description:    通过下列的参数来传递给controller
   *@param:  @param paramActivity
   *@param:  @param paramInt
   *@param:  @param paramDeviceItem
   *@param:  @param paramAndroidUpnpService
   *@param:  @param paramString1
   *@param:  @param metaData
   *@throws
   */
  public DMCControl(Activity paramActivity, int paramInt, DeviceItem paramDeviceItem,
      AndroidUpnpService paramAndroidUpnpService, String paramString1, String metaData) {
    Log.v(TAG, "DMCControl is call");
    this.activity = paramActivity;
    this.controlType = paramInt;
    this.executeDeviceItem = paramDeviceItem;
    this.upnpService = paramAndroidUpnpService;
    this.uriString = paramString1;
    this.metaData = metaData;
    Log.v(TAG, this.executeDeviceItem + "");
    Log.v(TAG, this.uriString);
    // Log.v(TAG, this.upnpService + "");

    // Log.v(TAG,"medaDate-----"+this.metaData);

    // this.metaData = paramString2;
  }

  // 播放错误
  private void setPlayErrorMessage() {
    Intent localIntent = new Intent();
    if (this.controlType == 3) {
      localIntent.setAction("cn.fnic.xbk.msi.action.video.play.error");

      this.activity.sendBroadcast(localIntent);
      return;
    }
    if (this.controlType == 2) {
      localIntent.setAction("cn.fnic.xbk.msi.action.audio.play.error");
      return;
    }
    localIntent.setAction("cn.fnic.xbk.msi.action.image.play.error");
  }

  private void stopGetPosition() {
    Message localMessage = new Message();
    localMessage.what = 13;
    localMessage.arg1 = 1;
    this.mhandle.sendMessage(localMessage);
  }

  /**
   * All rights reserved by uata.
   * @Title: getCurrentConnectionInfo
   * @Description: getCurrentConnectionInfo
   * @param paramInt  
   * @return: void
   * @throws 
   */
  public void getCurrentConnectionInfo(int paramInt) {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("ConnectionManager"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(
            new CurrentConnectionInfoCallback(localService, this.upnpService.getControlPoint(),
                paramInt));
      }
    } catch (Exception localException) {
     
    }
  }

  // 拿到deviceCapability
  /**
   * All rights reserved by uata.
   * @Title: getDeviceCapability
   * @Description: getDeviceCapability
   *   
   * @return: void
   * @throws 
   */
  public void getDeviceCapability() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null){
        this.upnpService.getControlPoint().execute(new GetDeviceCapabilitiesCallback(localService));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  
  /**
   * All rights reserved by uata.
   * @Title: getMediaInfo
   * @Description: // 媒体信息
   *   
   * @return: void
   * @throws 
   */
  public void getMediaInfo() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(new GetMediaInfoCallback(localService));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  /**
   * All rights reserved by uata.
   * @Title: getMute
   * @Description: 
   *   
   * @return: void
   * @throws 
   */
  public void getMute() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(new GetMuteCallback(localService, this.mhandle));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }


  /**
   * All rights reserved by uata.
   * @Title: getPositionInfo
   * @Description: 播放的节点的信息
   *   
   * @return: void
   * @throws 
   */
  public void getPositionInfo() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null){
        this.upnpService.getControlPoint().execute(
            new GetPositionInfoCallback(localService, this.mhandle, this.activity));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  
  /**
   * All rights reserved by uata.
   * @Title: getProtocolInfos
   * @Description: 连接的管理器
   * @param paramString  
   * @return: void
   * @throws 
   */
  public void getProtocolInfos(String paramString) {
    // Log.v(TAG, paramString);
    // Log.v(TAG,this.executeDeviceItem+"");
    Log.v(TAG, "device==" + this.executeDeviceItem.getDevice() + "");
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("ConnectionManager"));
      Log.v(TAG, "ConnectionManager==" + localService);
      if (localService != null) {
        Log.v(TAG, localService + "");
        // Log.v(TAG, this.upnpService.getControlPoint() + "");
        // Log.v(TAG, paramString);
        // Log.v(TAG, this.mHandle + "");
        // upnpService.getControlPoint():return The client API of the
        // UPnP service.
        upnpService.getControlPoint().execute(
            new GetProtocolInfoCallback(localService, upnpService.getControlPoint(), paramString,
                this.mhandle));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

 
  /**
   * All rights reserved by uata.
   * @Title: getTransportInfo
   * @Description:   信息 传输
   * @param paramBoolean  
   * @return: void
   * @throws 
   */
  public void getTransportInfo(boolean paramBoolean) {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        this.upnpService.getControlPoint()
            .execute(
                new GetTransportInfoCallback(localService, this.mhandle, paramBoolean,
                    this.controlType));
      }
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  
  /**
   * All rights reserved by uata.
   * @Title: getVolume
   * @Description:  获取dmr的音量
   * @param paramInt  
   * @return: void
   * @throws 
   */
  public void getVolume(int paramInt) {
    try {
      Log.v(TAG, "设备的名称是：" + this.executeDeviceItem.getDevice());
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
      Log.v(TAG, "服务获取：" + localService);
      if (localService != null) {
        Log.e("get volume", "get volume");
        this.upnpService.getControlPoint().execute(
            new GetVolumeCallback(this.activity, this.mhandle, paramInt, localService,
                this.controlType));
        return;
      }
      Log.e("null", "null");
      return;
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  /**
   * All rights reserved by uata.
   * @Title: pause
   * @Description:  pause
   *   
   * @return: void
   * @throws 
   */
  public void pause() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        Log.e("pause", "pause");
        this.upnpService.getControlPoint().execute(new PauseCallback(localService));
        return;
      }
      Log.e("null", "null");
      return;
    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  /**
   * All rights reserved by uata.
   * @Title: play
   * @Description: play
   *   
   * @return: void
   * @throws 
   */
  public void play() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        Log.e("start play", "start play");
        this.upnpService.getControlPoint().execute(new PlayerCallback(localService, this.mhandle));

      }


    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  /**
   *@Title: rePlayControl
   *@Description:  
   *@param:   
   *@return: void
   *@throws 
   */
  public void rePlayControl() {
    if (this.isGetNoMediaPlay) {
      return;
    }
    this.isGetNoMediaPlay = true;
    new Thread(new Runnable() {
      public void run() {
        try {
          Thread.sleep(2000L);
          DMCControl.this.setAvUri();
          DMCControl.this.isGetNoMediaPlay = false;

        } catch (Exception localException) {
          localException.printStackTrace();
        }
      }
    }).start();
  }


  /**
   *@Title: seekBarPosition
   *@Description: 进度条的节点
   *@param: @param paramString  
   *@return: void
   *@throws 
   */
  public void seekBarPosition(String paramString) {

    Log.v("seekBarPosition------", "进度条-----" + paramString);
    try {
      Device localDevice = this.executeDeviceItem.getDevice();
      Log.e("control action", "seekBarPosition");
      Service localService = localDevice.findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(
            new SeekCallback(this.activity, localService, paramString, this.mhandle));

      }

    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  
  /**
   *@Title: setAvURL
   *@Description: url 
   *@param:   
   *@return: void
   *@throws 
   */
  public void setAvUri() {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        Log.e("set url", "set url" + this.uriString);
        this.upnpService.getControlPoint().execute(
            new SetAVTransportURIActionCallback(localService, this.uriString, this.metaData,
                this.mhandle, this.controlType));


      }

    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

  public void setCurrentPlayPath(String paramString) {
    Log.v(TAG, "下一首的url----" + paramString);
    this.uriString = paramString;
  }

  /**
   *@Title: setCurrentPlayPath
   *@Description: 
   *@param: @param paramString1
   *@param: @param paramString2  
   *@return: void
   *@throws 
   */
  public void setCurrentPlayPath(String paramString1, String paramString2) {
    this.uriString = paramString1;
    this.metaData = paramString2;

  }

  /**
   *@Title: setMute
   *@Description: 
   *@param: @param paramBoolean  
   *@return: void
   *@throws 
   */
  public void setMute(boolean paramBoolean) {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(
            new SetMuteCalllback(localService, paramBoolean, this.mhandle));

      }
      Log.e("null", "null");

    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }

 

  /**
   *@Title: setVolume
   *@Description: 音量设置
   *@param: @param paramLong
   *@param: @param paramInt  
   *@return: void
   *@throws 
   */
  public void setVolume(int Volume, int paramInt) {
    int getVolume=Volume;
    while (true) {
      Service localService = null;
      int upVoice;
      try {
        localService =
            this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
        if (localService == null) {
          return;
        }
        Log.v("SetVolume", "SetVolume====" + getVolume + "--zong--" + paramInt);
        // if (paramInt == 0) {
        // if (paramLong < 0L)
        // if((paramInt>0L)&&(paramInt==1)){
        // paramLong -= 1L;
        if (paramInt == 1) {
          getVolume = -5 + getVolume;
          if (getVolume < 0) {
            getVolume = 0;

          }
          this.upnpService.getControlPoint().execute(
              new SetVolumeCallback(localService, getVolume, paramInt));

          // Toast.makeText(this.activity, "正在---调整音量", 0).show();
          return;
        }


        else if (getVolume < 100 && paramInt == 2) {

          // paramLong += 1L;
          getVolume = getVolume + 5;
          // if(upVoice>100)
          // {
          // upVoice=100;
          // }
          this.upnpService.getControlPoint().execute(
              new SetVolumeCallback(localService, getVolume, paramInt));
          // //Toast.makeText(this.activity, "正在+++调整音量", 0).show();
          return;

        }

        // }
      } catch (Exception localException) {
        localException.printStackTrace();

      }
      // paramLong += 1L;
    }

  }

//  public void setDownVolume(int paramLong, int paramInt) {
//    while (true) {
//      Service localService = null;
//
//      try {
//        localService =
//            this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
//        if (localService == null) {
//          return;
//        }
//        Log.v("SetVolume", "SetVolume====" + paramLong + "--zong--" + paramInt);
//        // if (paramInt == 0) {
//        // if (paramLong < 0L)
//        // if((paramInt>0L)&&(paramInt==1)){
//        // paramLong -= 1L;
//        if (paramInt == 1) {
//          paramLong = -5 + paramLong;
//          if (paramLong < 0) {
//            paramLong = 0;
//
//          }
//          this.upnpService.getControlPoint().execute(
//              new SetVolumeCallback(localService, paramLong, paramInt));
//
//          // Toast.makeText(this.activity, "正在---调整音量", 0).show();
//          return;
//
//        }
//
//
//
//        // if ((paramLong <= 0L) || (paramInt != 2)){
//        // Log.v("SetVolume", "SetVolume====" +paramLong+"--zong--"+paramInt);
//        // paramLong += 1L;
//        // this.upnpService.getControlPoint().execute(
//        // new SetVolumeCallback(localService, paramLong));
//        // //Toast.makeText(this.activity, "正在+++调整音量", 0).show();
//        // return;
//        //
//        // }
//
//
//      } catch (Exception localException) {
//        localException.printStackTrace();
//
//      }
//      // paramLong += 1L;
//
//    }
//
//
//  }

//  public void setUPVolume(int paramLong, int paramInt) {
//    while (true) {
//      Service localService = null;
//      int upVoice;
//      try {
//        localService =
//            this.executeDeviceItem.getDevice().findService(new UDAServiceType("RenderingControl"));
//        if (localService == null) {
//          return;
//        }
//
//
//
//        // if ((paramInt <= 0L) || (paramInt != 2)){
//        // paramLong += 1L;
//        if (paramLong < 100 && paramInt == 2) {
//
//          // paramLong += 1L;
//          upVoice = paramLong + 5;
//          if (upVoice > 100) {
//            upVoice = 100;
//          }
//          Log.v("SetVolume", "SetVolume====" + upVoice + "--zong--" + paramInt);
//          this.upnpService.getControlPoint().execute(
//              new SetVolumeCallback(localService, upVoice, paramInt, this.mhandle));
//          // Toast.makeText(this.activity, "正在+++调整音量", 0).show();
//          return;
//        }
//
//      } catch (Exception localException) {
//        localException.printStackTrace();
//
//      }
//    
//    }
//  }

  /**
   *@Title: startThreadGetMessage
   *@Description: 开启任务
   *@param:   
   *@return: void
   *@throws 
   */
  public void startThreadGetMessage() {
    // Log.e("startThreadGetMessage", "startThreadGetMessage"
    // + this.threadGetState);
//    DMCControlMessage.runing = true;
    // if(!this.threadGetState)
    // {
    // return;
    // }
    if (this.threadGetState) {


      this.threadGetState = false;
      new Thread(new Runnable() {
        public void run() {
          try {
            if (!DMCControlMessage.runing) {
              DMCControl.this.threadGetState = true;

            }
            Thread.sleep(500L);
            DMCControl.this.getPositionInfo();

          } catch (Exception localException) {
            localException.printStackTrace();
          }
        }
      }).start();
    }
  }


  /**
   *@Title: stop
   *@Description: 播放前先暂停 
   *@param: @param paramBoolean  
   *@return: void
   *@throws 
   */
  public void stop(Boolean paramBoolean) {
    try {
      Service localService =
          this.executeDeviceItem.getDevice().findService(new UDAServiceType("AVTransport"));
      if (localService != null) {
        this.upnpService.getControlPoint().execute(
            new StopCallback(localService, this.mhandle, paramBoolean, this.controlType));

      }

    } catch (Exception localException) {
      localException.printStackTrace();
    }
  }


}
