package uata.dms;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.uata.application.BaseApplication;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @ClassName:  MediaServer
 * @Description: (进入http 服务器之前的工具类)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午1:47:27 
 */
public class MediaServer {

  /*
   * ①public class UDN extends Object
   * 
   * A unique device name. 一个独立设备的名称 UDN.uniqueSystemIdentifier：生成一个全球唯一的标识UDN（UUID）
   * 
   * ②LocalService：元数据通过代码绑定到主机服务上 After instantiation
   * setManager(org.fourthline.cling.model.ServiceManager) must be called to bind the service
   * metadata to the service implementation. 在实例化setManager这个方法前必须绑定服务 ③LocalDevice：元数据通过代码绑定到主机设备上
   */

  private UDN udn = MediaServer.uniqueSystemIdentifier("GNaP-MediaServer");
  private LocalDevice localDevice;

  private static final  String DEVICETYPE = "MediaServer";
  private static final  int VERSION = 1;
  private static final  String LOGTAG = "GNaP-MediaServer";
  private static final  String TAG = "run way";
  private static final  int MPORT = 8192;
  private Context mcontext;
  private static InetAddress localAddress;

  /**
   *@Title:  MediaServer 
   *@Description:    通过ip去查找设备资源
   *@param:  @param localAddress
   *@param:  @throws ValidationException
   *@throws
   */
  public MediaServer(InetAddress localAddress) throws ValidationException {

    DeviceType type = new UDADeviceType(DEVICETYPE, VERSION);
    // 获得手机的制造商信息和型号
    // DeviceDetails details = new DeviceDetails( "Local"+"("+android.os.Build.MODEL+")",
    DeviceDetails details =
        new DeviceDetails("本地设备", new ManufacturerDetails(Build.MANUFACTURER),
            new ModelDetails("GNaP", "GNaP MediaServer for Android", "v1"));

    LocalService service = new AnnotationLocalServiceBinder().read(ContentDirectoryService.class);
    /*
     * Binds the metadata of a service to a service implementation, unified interface for accessing
     * local services. 绑定的元数据通过服务来实现，而访问服务必须用唯一标识 The UPnP core will always access a local service
     * implementation through this manager, available with LocalService.getManager(): 所以
     * UPNP代码是通过服务管理者来访问本地的服务，可用LocalService.getManager()；
     */



    service.setManager(new DefaultServiceManager<ContentDirectoryService>(service,
        ContentDirectoryService.class));

    localDevice = new LocalDevice(new DeviceIdentity(udn), type, details, service);
    this.localAddress = localAddress;
    Log.v(LOGTAG, "MediaServer device created: ");
    Log.v(LOGTAG, "friendly name: " + details.getFriendlyName());
    Log.v(LOGTAG, "manufacturer: " + details.getManufacturerDetails().getManufacturer());
    Log.v(LOGTAG, "model: " + details.getModelDetails().getModelName());
    // start http server
    try {
      Log.d(LOGTAG, MPORT + "");
      new HttpServer(MPORT);
    } catch (IOException ioe) {
    
      System.exit(-1);
    }

    Log.v(LOGTAG, "Started Http Server on port " + MPORT);
  }

  public LocalDevice getDevice() {
    return localDevice;
  }

  public String getAddress() {
    return localAddress.getHostAddress() + ":" + MPORT;
  }


//  protected Icon createDefaultDeviceIcon() {
//    try {
//      Icon localIcon =
//          new Icon("image/png", 48, 48, 32, "msi.png", this.mcontext.getResources().getAssets()
//              .open("ic_launcher.png"));
//      return localIcon;
//    } catch (IOException localIoexception) {
//      Log.w("MediaServer", "createDefaultDeviceIcon IOException");
//    }
//    return null;
//  }


  /**
   * All rights reserved by uata.
   * @Title: uniqueSystemIdentifier
   * @Description: unn
   * @param paramString
   * @return  
   * @return: UDN
   * @throws 
   */
  public static UDN uniqueSystemIdentifier(String str) {
    StringBuilder localStringBuilder = new StringBuilder();
    Log.d("UpnpUtil",
        "host:" + BaseApplication.getHostName() + " ip:" + BaseApplication.getHostAddress());
    if ((BaseApplication.getHostName() != null) && (BaseApplication.getHostAddress() != null)){
      localStringBuilder.append(BaseApplication.getHostName()).append(
          BaseApplication.getHostAddress());
      localStringBuilder.append(Build.MODEL);
      localStringBuilder.append(Build.MANUFACTURER);
    }
    try {
      UDN localUdn =
          new UDN(new UUID(new BigInteger(-1, MessageDigest.getInstance("MD5").digest(
              localStringBuilder.toString().getBytes())).longValue(), str.hashCode()));
      return localUdn;
    } catch (Exception localException) {
      
    }

    return null;
  }
}
