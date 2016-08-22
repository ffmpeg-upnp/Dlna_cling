package uata.dmc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;


/**
 * @ClassName:  GetVolumeCallback
 * @Description: (获取设备的音量返回值)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:09:51 
 */
public class GetVolumeCallback extends GetVolume {
  private Activity activity;
  private Handler handler;
  private int isSetVolumeFlag = 2;
  private int type;
  private int volume;

  public GetVolumeCallback(Activity paramActivity, Handler paramHandler, int paramInt1,
      Service paramService, int paramInt2) {
    super(paramService);
    this.activity = paramActivity;
    this.handler = paramHandler;
    this.isSetVolumeFlag = paramInt1;
    this.volume = paramInt2;
  }

  /**
   *@see org.fourthline.cling.controlpoint.ActionCallback#failure(org.fourthline.cling.model.action.ActionInvocation, org.fourthline.cling.model.message.UpnpResponse, String)
   *@Title: failure
   *@Description: 
   *@param: @param paramActionInvocation
   *@param: @param paramUpnpResponse
   *@param: @param paramString  
   *@throws 
   */
  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    // if (this.type == 1){
    // this.handler.sendEmptyMessage(16);
    // return;
    // }
    // if (this.type != 2){
    //
    // this.handler.sendEmptyMessage(15);
    // return;
    // }
    //
    // if(this.type!=3)
    // {
    //
    // this.handler.sendEmptyMessage(17);
    // return;
    // }
  }

  public void received(ActionInvocation paramActionInvocation, int volume) {
    Log.e("getcurrentvolume", "音量位：" + volume + "");
    int setVolume=volume;
    if (this.isSetVolumeFlag == 2) {
      Message localMessage = new Message();
      localMessage.what = 7;
      Bundle localBundle = new Bundle();
      // localBundle.putLong("getVolume", paramInt);
      setVolume =  setVolume + 5;//
      localBundle.putInt("getVolume",  setVolume);

      localBundle.putInt("isSetVolume", this.isSetVolumeFlag);
      Log.e("getcurrentvolume", "isSetVolume：" + this.isSetVolumeFlag + "");

      localMessage.setData(localBundle);
      this.handler.sendMessage(localMessage);
    } else {
      // paramInt=this.volume;
      // paramInt=-5+ paramInt;
      Message localMessage = new Message();
      localMessage.what = 7;
      Bundle localBundle = new Bundle();
      // localBundle.putLong("getVolume", paramInt);
      localBundle.putInt("getVolume",  setVolume);

      localBundle.putInt("isSetVolume", this.isSetVolumeFlag);
      Log.e("getcurrentvolume", "isSetVolume：" + this.isSetVolumeFlag + "");

      localMessage.setData(localBundle);
      this.handler.sendMessage(localMessage);
    }

  }
}
