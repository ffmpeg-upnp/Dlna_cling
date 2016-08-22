package uata.dmc;

import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.Play;


/**
 * @ClassName:  PlayerCallback
 * @Description: 开始播放
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:07:06 
 */
public class PlayerCallback extends Play {
  private Handler handler;

  public PlayerCallback(Service paramService, Handler paramHandler) {
    super(paramService);
    this.handler = paramHandler;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    this.handler.sendEmptyMessage(17);
    Log.e("play failed", "play failed");
  }

  public void run() {
    super.run();
  }

  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
    Log.v("play success", "play success");
    this.handler.sendEmptyMessage(12);
  }
}
