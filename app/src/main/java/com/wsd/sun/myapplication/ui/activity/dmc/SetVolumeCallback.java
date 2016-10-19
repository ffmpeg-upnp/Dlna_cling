package com.wsd.sun.myapplication.ui.activity.dmc;

import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;


/**
 * @ClassName:  SetVolumeCallback
 * @Description: (设置音量)
 */
public class SetVolumeCallback extends SetVolume {
  private int paramLong;
  private Handler handler;
  private int setVolumeFlag;

  public SetVolumeCallback(Service paramService, int paramLong, int setVolumeFlag) {
    super(paramService, paramLong);
    this.paramLong = paramLong;
    this.setVolumeFlag = setVolumeFlag;
  }

  public SetVolumeCallback(Service service, int param, int setVolumeFlag, Handler handler) {
    super(service, param);
    this.paramLong = paramLong;
    this.setVolumeFlag = setVolumeFlag;
    this.handler = handler;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    Log.e("set volume failed", "set volume failed");
  }

  public void success(ActionInvocation actionInvocation, int param) {
    super.success(actionInvocation);
    Log.e("set volume success", "set volume success----" + param);
    // if( this.setVolumeFlag==2){
    // Message localMessage = new Message();
    // localMessage.what = 8;
    // Bundle localBundle = new Bundle();
    // // localBundle.putLong("getVolume", paramInt);
    // localBundle.putInt("getVolume",this.paramLong);
    // System.out.println(this.paramLong);
    // System.out.println(this.setVolumeFlag);
    // localBundle.putInt("isSetVolume", this.setVolumeFlag);
    // // Log.e("getcurrentvolume", "isSetVolume："+this.isSetVolumeFlag+"");
    //
    // localMessage.setData(localBundle);
    // this.handler.sendMessage(localMessage);
    // }
  }

  @Override
  public void success(ActionInvocation invocation) {
    // Auto-generated method stub
    super.success(invocation);
    Log.e("set volume success", "set invocation----" + this.paramLong);
    // if( this.setVolumeFlag==2){
    // Message localMessage = new Message();
    // localMessage.what = 8;
    // Bundle localBundle = new Bundle();
    // // localBundle.putLong("getVolume", paramInt);
    // localBundle.putInt("getVolume",this.paramLong);
    // System.out.println(this.paramLong);
    // System.out.println(this.setVolumeFlag);
    // localBundle.putInt("isSetVolume", this.setVolumeFlag);
    // // Log.e("getcurrentvolume", "isSetVolume："+this.isSetVolumeFlag+"");
    //
    // localMessage.setData(localBundle);
    // this.handler.sendMessage(localMessage);
    // }
  }


}
