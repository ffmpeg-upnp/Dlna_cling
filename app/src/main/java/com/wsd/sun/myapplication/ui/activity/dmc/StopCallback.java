package com.wsd.sun.myapplication.ui.activity.dmc;

import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.Stop;

/**
 * @ClassName:  StopCallback
 * @Description: (停止播放)
 */
public class StopCallback extends Stop {
  private Handler handler;
  private Boolean isRePlay = Boolean.FALSE;
  private int type;

  public StopCallback(Service service, Handler handler, Boolean param, int aint) {
    super(service);
    this.handler = handler;
    this.isRePlay = param;
    this.type = aint;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    if (this.type == 1) {
      this.handler.sendEmptyMessage(16);
      return;
    }
    if (this.type == 2) {
      this.handler.sendEmptyMessage(15);
      return;
    }
    if (this.type == 3) {

      this.handler.sendEmptyMessage(17);
      return;
    }
  }

 
  /**
   *@see Stop#success(ActionInvocation)
   *@Title: success
   *@Description: 停止正在播放的媒体
   *@param: @param paramActionInvocation  
   *@throws 
   */
  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
    if (!this.isRePlay.booleanValue()) {
      this.handler.sendEmptyMessage(2);
      Log.e("dmc stop ", " to set url");
      return;
    }
    this.handler.sendEmptyMessage(14);
  }
}
