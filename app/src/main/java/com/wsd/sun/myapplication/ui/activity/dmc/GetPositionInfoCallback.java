package com.wsd.sun.myapplication.ui.activity.dmc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.model.PositionInfo;

/**
 * @ClassName:  GetPositionInfoCallback
 * @Description: (音频和视频 的时间点 实时更新)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:29:45 
 */
public class GetPositionInfoCallback extends GetPositionInfo {
  private static final String TAG = "GetPositionInfoCallback";
  private Activity activity;
  private Handler handler;

  /**
   *@Title:  GetPositionInfoCallback 
   *@Description:    
   *@param:  @param service
   *@param:  @param handler
   *@param:  @param activity
   *@throws
   */
  public GetPositionInfoCallback(Service service, Handler handler, Activity activity) {
    super(service);
    this.handler = handler;
    this.activity = activity;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    Log.e(this.TAG, "failed");
  }

  /**
   *@see GetPositionInfo#received(ActionInvocation, PositionInfo)
   *@Title: received
   *@Description: 获取进度条的状态
   *@param: @param paramActionInvocation
   *@param: @param paramPositionInfo  
   *@throws 
   */
  public void received(ActionInvocation paramActionInvocation, PositionInfo paramPositionInfo) {
    Bundle localBundle = new Bundle();
    localBundle.putString("TrackDuration", paramPositionInfo.getTrackDuration());
    localBundle.putString("RelTime", paramPositionInfo.getRelTime());
    // Log.i(TAG, "TrackDuration---"+paramPositionInfo.getTrackDuration());
    // Log.i(TAG, "RelTime----"+paramPositionInfo.getRelTime());

    Intent localIntent = new Intent("cn.fnic.xbk.msi.action.play.update");
    localIntent.putExtras(localBundle);
    this.activity.sendBroadcast(localIntent);
  }

  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
  }



}
