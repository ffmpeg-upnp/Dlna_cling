package com.wsd.sun.myapplication.ui.activity.dmc;

import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.connectionmanager.callback.GetProtocolInfo;
import org.fourthline.cling.support.model.ProtocolInfos;

/**
 * @ClassName:  GetProtocolInfoCallback
 * @Description: (启动远端设备和本地control)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:21:16 
 */
public class GetProtocolInfoCallback extends GetProtocolInfo {
  private static final String TAG = "GetProtocolInfoCallback";
  private Handler handler;
  private boolean hasType = false;
  private String requestPlayMimeType = "";

  /**
   *@Title:  GetProtocolInfoCallback 
   *@Description:    
   *@param:  @param paramService
   *@param:  @param paramControlPoint
   *@param:  @param paramString
   *@param:  @param paramHandler
   *@throws
   */
  public GetProtocolInfoCallback(Service paramService, ControlPoint paramControlPoint,
      String paramString, Handler paramHandler) {
    super(paramService, paramControlPoint);
    this.requestPlayMimeType = paramString;
    this.handler = paramHandler;
    Log.v(TAG, TAG);
    Log.v(TAG, "" + paramService);
    Log.v(TAG, "" + paramControlPoint);
    Log.v(TAG, "" + requestPlayMimeType);
    Log.v(TAG, "" + paramHandler);
  }



  @Override
  public void received(ActionInvocation arg0, ProtocolInfos arg1, ProtocolInfos arg2) {
    // Auto-generated method stub
    Log.d(TAG, "received is call ");
    // this.handler.sendEmptyMessage(1);
  }

  @Override
  public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
    // Auto-generated method stub
    Log.e("DMC", "GetProtocolInfo  failure");
    // this.handler.sendEmptyMessage(1);
  }


  // 拿到url后 调用success进行对dmr的播放
  @Override
  public void success(ActionInvocation arg0) {
    // Auto-generated method stub
    super.success(arg0);
    this.handler.sendEmptyMessage(2);// 设备连接后 直接返回后发送url到dmr
  }



}
