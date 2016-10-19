package com.wsd.sun.myapplication.ui.activity.dmc;

import android.util.Log;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.connectionmanager.callback.GetCurrentConnectionInfo;
import org.fourthline.cling.support.model.ConnectionInfo;


/**
*@ClassName:  CurrentConnectionInfoCallback
*@Description:  当前的连接的状态和id

*/
public class CurrentConnectionInfoCallback extends GetCurrentConnectionInfo
{
  private static final String TAG = "CurrentConnectionInfoCallback";

  public CurrentConnectionInfoCallback(Service service, ControlPoint controlPoint, int aint)
  {
    super(service, controlPoint, aint);
  }

  public void failure(ActionInvocation invocation, UpnpResponse upnpResponse, String str) {
    Log.e(this.TAG, "failed");
  }

  public void received(ActionInvocation paramActionInvocation, ConnectionInfo paramConnectionInfo) {
    Log.e(TAG, paramConnectionInfo.getConnectionID() + "");
    Log.e(TAG, paramConnectionInfo.getConnectionStatus() + "");
  }
}

