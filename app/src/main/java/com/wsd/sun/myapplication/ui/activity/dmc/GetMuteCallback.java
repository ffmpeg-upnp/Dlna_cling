package com.wsd.sun.myapplication.ui.activity.dmc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.GetMute;

/**
 * @ClassName:  GetMuteCallback
 * @Description: (获取设备 是否处于静音状态)
 */
public class GetMuteCallback extends GetMute {
  private Handler handler;

  public GetMuteCallback(Service paramService, Handler paramHandler) {
    super(paramService);
    this.handler = paramHandler;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    Log.i("DMC", "get mute failed");
  }

  /**
   *@see GetMute#received(ActionInvocation, boolean)
   *@Title: received
   *@Description: 获取设备是否处于静音状态
   *@param: @param paramActionInvocation
   *@param: @param paramBoolean  
   *@throws 
   */
  public void received(ActionInvocation paramActionInvocation, boolean paramBoolean) {
    Log.i("DMC", "get mute status:" + Boolean.toString(paramBoolean));
    Message localMessage = new Message();
    localMessage.what = 11;
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("mute", paramBoolean);
    localMessage.setData(localBundle);
    this.handler.sendMessage(localMessage);
  }
}
