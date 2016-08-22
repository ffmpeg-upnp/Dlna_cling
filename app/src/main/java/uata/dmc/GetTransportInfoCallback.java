package uata.dmc;

import android.os.Handler;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.GetTransportInfo;
import org.fourthline.cling.support.model.TransportInfo;


/**
 * @ClassName:  GetTransportInfoCallback
 * @Description: (数据传输)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:20:44 
 */
public class GetTransportInfoCallback extends GetTransportInfo {
  private Handler handler;
  private boolean isOnlyGetState;
  private int type;

  public GetTransportInfoCallback(Service paramService, Handler paramHandler, boolean paramBoolean,
      int paramInt) {
    super(paramService);
    this.handler = paramHandler;
    this.isOnlyGetState = paramBoolean;
    this.type = paramInt;
  }

  public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse,
      String paramString) {
    // if (this.type == 1)
    // this.handler.sendEmptyMessage(16);
    // do
    // {
    // return;
    // if (this.type != 2)
    // continue;
    // this.handler.sendEmptyMessage(15);
    // return;
    // }
    // while (this.type != 3);
    // this.handler.sendEmptyMessage(17);


    // if(this.type==1)
    // {
    // this.handler.sendEmptyMessage(16);
    // return;
    //
    // }
    // if(this.type!=2)
    // {
    // this.handler.sendEmptyMessage(15);
    // return;
    // }
    // if(this.type!=3)
    // {
    // this.handler.sendEmptyMessage(17);
    // return;
    // }
  }

  /**
   *@see org.fourthline.cling.support.avtransport.callback.GetTransportInfo#received(org.fourthline.cling.model.action.ActionInvocation, org.fourthline.cling.support.model.TransportInfo)
   *@Title: received
   *@Description: 
   *@param: @param paramActionInvocation
   *@param: @param paramTransportInfo  
   *@throws 
   */
  public void received(ActionInvocation paramActionInvocation, TransportInfo paramTransportInfo) {
    Log.e("GetTransportInfoCallback", paramTransportInfo.getCurrentTransportState() + "");
    Log.e("GetTransportInfoCallback", paramTransportInfo.getCurrentTransportStatus() + "");
    Log.e("isOnlyGetState", Boolean.toString(this.isOnlyGetState));
    Log.e("GetTransportInfoCallback", "GetTransportInfoCallback is call");
    this.handler.sendEmptyMessage(2);
  }
}
