package com.wsd.sun.myapplication.ui.activity.dmc;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.GetMediaInfo;
import org.fourthline.cling.support.model.MediaInfo;

/**
 * @ClassName:  GetMediaInfoCallback
 * @Description: (获取媒体的信息)
 */
public class GetMediaInfoCallback extends GetMediaInfo {
  public GetMediaInfoCallback(Service paramService) {
    super(paramService);
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {}

  public void received(ActionInvocation paramActionInvocation, MediaInfo paramMediaInfo) {}

  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
  }
}
