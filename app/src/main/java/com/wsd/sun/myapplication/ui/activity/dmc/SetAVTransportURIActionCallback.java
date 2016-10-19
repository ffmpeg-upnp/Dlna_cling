package com.wsd.sun.myapplication.ui.activity.dmc;

import android.os.Handler;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;


/**
 * @ClassName:  SetAVTransportURIActionCallback
 * @Description: (设置uri)
 */
public class SetAVTransportURIActionCallback extends SetAVTransportURI {
  private Handler handler;
  private int type;

  public SetAVTransportURIActionCallback(Service paramService, String paramString1,
      String paramString2, Handler paramHandler, int paramInt) {
    super(paramService, paramString1, paramString2);
    this.handler = paramHandler;
  }

  // public SetAVTransportURIActionCallback(Service paramService, String paramString1, Handler
  // paramHandler, int paramInt)
  // {
  // super(paramService, paramString1);
  // this.handler = paramHandler;
  // }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    // if (this.type == 1){
    // this.handler.sendEmptyMessage(16);
    // return;
    // }
    // if (this.type == 2){
    //
    // this.handler.sendEmptyMessage(15);
    // return;
    // }
    // if (this.type == 3){
    //
    // this.handler.sendEmptyMessage(17);
    // return;
    // }
  }

  /**
   *@see SetAVTransportURI#success(ActionInvocation)
   *@Title: success
   *@Description: 接收到uri以后返回主页通知播放
   *@param: @param paramActionInvocation  
   *@throws 
   */
  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
    // try
    // {
    // Thread.sleep(2000L);
    this.handler.sendEmptyMessage(3);

    // }
    // catch (Exception localException)
    // {
    // localException.printStackTrace();
    // }
  }
}
