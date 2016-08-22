package uata.dmc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.SetMute;

// 设置静音

/**
 * @ClassName:  SetMuteCalllback
 * @Description: (设置静音)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:58:06 
 */
public class SetMuteCalllback extends SetMute {
  private boolean desiredMute;
  private Handler handler;

  public SetMuteCalllback(Service paramService, boolean paramBoolean, Handler paramHandler) {
    super(paramService, paramBoolean);
    this.handler = paramHandler;
    this.desiredMute = paramBoolean;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    Log.e("set mute failed", "set mute failed");
  }

  public void success(ActionInvocation paramActionInvocation) {
    Log.e("set mute success", "set mute success");
    if (this.desiredMute) {
      this.desiredMute = false;
    }
    Message localMessage = new Message();
    localMessage.what = 23;
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("mute", this.desiredMute);
    localMessage.setData(localBundle);
    this.handler.sendMessage(localMessage);
  }
}
