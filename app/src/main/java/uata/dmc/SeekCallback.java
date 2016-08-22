package uata.dmc;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.Seek;


/**
 * @ClassName:  SeekCallback
 * @Description: (设置进度条)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:02:43 
 */
public class SeekCallback extends Seek {
  private static final String TAG = "seekcallback";
  private Activity activity;
  private Handler mHandler;

  public SeekCallback(Activity paramActivity, Service paramService, String paramString,
      Handler paramHandler) {
    super(paramService, paramString);
    this.activity = paramActivity;
    this.mHandler = paramHandler;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    Log.e(this.TAG, "failed");
  }

  // public void sendBroadcast()
  // {
  // Intent localIntent = new Intent("com.continue.display");
  // System.out.println("拖拽进度条");
  // this.activity.sendBroadcast(localIntent);
  // }

  public void success(ActionInvocation paramActionInvocation) {
    super.success(paramActionInvocation);
    // this.mHandler.sendEmptyMessage(3);
    Log.i(this.TAG, "success");
  }
}
