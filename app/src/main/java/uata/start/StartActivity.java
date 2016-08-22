package uata.start;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uata.LocalMedia.LocalImage;
import com.uata.LocalMedia.LocalMusic;
import com.uata.LocalMedia.LocalVideo;
import com.uata.Menu.DeviceActivity;
import com.uata.adapter.DevAdapter;
import com.uata.application.BaseApplication;
import com.uata.application.ConfigData;
import com.uata.dlna_application.R;
import com.uata.dmp.DeviceItem;
import com.uata.dms.ContentNode;
import com.uata.dms.ContentTree;
import com.uata.dms.MediaServer;
import com.uata.guidePage.GuideActivity;
import com.uata.util.Utils;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:  StartActivity
 * @Description: 欢迎页界面
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:29:56 
 */
public class StartActivity extends Activity {

  private static final String TAG = "StartActicity";
  private static final int GET_IP_FAIL = 0;
  private static final int GET_IP_SUC = 1;
  private String hostAddress;
  private String hostName;
  private Context context;
  private SharedPreferences preferences;
  private Editor editor;
  private RelativeLayout splash;
  private OutputStream os;
  private MediaServer mediaServer;
//  private List<Map<String, String>> mVideoFilePaths;
  private static boolean serverPrepared = false;
  public static final  String LOGTAG = "StartActivity";
  private DeviceListRegistryListener deviceListRegistryListener;
  private AndroidUpnpService upnpService;
  public ArrayList<DeviceItem> arrDmrList = new ArrayList<DeviceItem>();
  public ArrayList<DeviceItem> arrDevList = new ArrayList<DeviceItem>();
  public DevAdapter mdmrDevAdapter;
  private Handler mhandle = new Handler() {

    // Send a message to Handle
    @Override
    public void handleMessage(Message msg) {
      // Auto-generated method stub
      switch (msg.what) {
        case 0:
          Toast.makeText(StartActivity.this, "获取Ip失败", 0).show();
          break;
        case 1:
          if (msg.obj != null) {
            InetAddress localInetAddress = (InetAddress) msg.obj;
            if (localInetAddress == null) {
              StartActivity.this.setIp(localInetAddress);
            }
            Log.v(TAG, localInetAddress + "");
            StartActivity.this.setIpfo();

            StartActivity.this.jumpToMain();
          }
          break;
        default:
          Toast.makeText(StartActivity.this, "获取Ip失败", 0).show();
          break;
      }
    }

  };


  /**
   * All rights reserved by uata.
   * @Title: getIp
   * @Description: 获取ip地址
   *   
   * @return: void
   * @throws 
   */
  private void getIp() {
    new Thread(new Runnable() {

      @Override
      public void run() {
        WifiManager wifimanager = (WifiManager) StartActivity.this.context.getSystemService("wifi");
        int i = wifimanager.getConnectionInfo().getIpAddress();

        Message localMessage = new Message();
        try {
          Object[] arrayOfObject = new Object[4];
          arrayOfObject[0] = Integer.valueOf(i & 0xFF);
          arrayOfObject[1] = Integer.valueOf(0xFF & i >> 8);
          arrayOfObject[2] = Integer.valueOf(0xFF & i >> 16);
          arrayOfObject[3] = Integer.valueOf(0xFF & i >> 24);
          InetAddress localInetAddress =
              InetAddress.getByName(String.format("%d.%d.%d.%d", arrayOfObject));
          Log.v(TAG, localInetAddress + "");
          StartActivity.this.hostName = localInetAddress.getHostName();
          StartActivity.this.hostAddress = localInetAddress.getHostAddress();
          localMessage.obj = localInetAddress;
          localMessage.what = 1;
          StartActivity.this.mhandle.sendMessage(localMessage);
          return;
        } catch (UnknownHostException localUnknownHostException) {
          StartActivity.this.mhandle.sendEmptyMessage(0);
        }
      }
    }).start();
  }

  private void jumpToMain() {
    // startActivity(new Intent(this,IndexActivity.class));
    // finish();

    splash = (RelativeLayout) findViewById(R.id.start_sp);

    AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);

    alpha.setDuration(2000);

    splash.startAnimation(alpha);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    // 判断是否需要进入引导页
    new Handler().postDelayed(new Runnable() {

      public void run() {
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        if (preferences.getBoolean("firststart", true)) {
          editor = preferences.edit();
          // 将登录标志位设置为false，下次登录时不在显示首次登录界面
          editor.putBoolean("firststart", false);
          editor.commit();
          // Intent intent = new Intent("com.yutao.business.GUIDE");
          Intent intent = new Intent(StartActivity.this, GuideActivity.class);
          StartActivity.this.startActivity(intent);
          finish();
        } else {


          Intent intent = new Intent(StartActivity.this, DeviceActivity.class);
          // 通过Intent打开最终真正的主界面Main这个Activity
          StartActivity.this.startActivity(intent); // 启动Main界面
          StartActivity.this.finish(); // 关闭自己这个开场屏
        }
      }
    }, 3000);

  }

  private void setIp(InetAddress paramInetAddress) {
    BaseApplication.setLocalIpAddress(paramInetAddress);
  }

  private void setIpfo() {
    BaseApplication.setHostAddress(this.hostAddress);
    BaseApplication.setHostName(this.hostName);
  }


  /**
   * @Title: isWiFiActive
   * @Description:  检查wifi是否连接  *@param: @param inContext  *@param: @return    *@return: boolean
   * @throws 
   */

  public static boolean isWiFiActive(Context inContext) {
    Context context = inContext.getApplicationContext();
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {

            return true;
          }
        }
      }
    }
    Toast.makeText(context, "网络连接异常，请检查网络", 0).show();
    return false;
  }


 
  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      Log.v(LOGTAG, "Connected to UPnP Service");
      upnpService = (AndroidUpnpService) service;

      BaseApplication.upnpService = StartActivity.this.upnpService;
      if (mediaServer == null) {
        try {
          Log.v(LOGTAG, getLocalIpAddress() + "");
          mediaServer = new MediaServer(getLocalIpAddress());


          upnpService.getRegistry().addDevice(mediaServer.getDevice());

          prepareMediaServer();



        } catch (Exception ex) {

          // log.log(Level.SEVERE, "Creating demo device failed", ex);
          Toast.makeText(StartActivity.this, R.string.create_demo_failed, Toast.LENGTH_SHORT)
              .show();
          Log.v(LOGTAG, "Connected to UPnP Service is failed");
          return;
        }
      }
      // GstMediaRenderer localGstMediaRenderer = new GstMediaRenderer(1, DeviceActivity.this);
      // DeviceActivity.this.upnpService.getRegistry().addDevice(localGstMediaRenderer.getDevice());
      // DeviceActivity.this.deviceListRegistryListener.dmrAdded(new
      // DeviceItem(localGstMediaRenderer.getDevice()));
      // DeviceActivity.this.startService(new Intent(DeviceActivity.this,RenderService.class));



      // GstMediaRenderer localRender=new GstMediaRenderer(1, DeviceActivity.this);



      // deviceListAdapter.clear();
      for (Device device : upnpService.getRegistry().getDevices()) {
        Log.v(LOGTAG, "Device---" + device);
        deviceListRegistryListener.deviceAdded(new DeviceItem(device));
        deviceListRegistryListener.dmrAdded(new DeviceItem(device));
      }

      // Getting ready for future device advertisements
      upnpService.getRegistry().addListener(deviceListRegistryListener);

      // Refresh device list
      upnpService.getControlPoint().search();

      // try {
      // Thread.sleep(1000);
      // } catch (InterruptedException e) {
      // // Auto-generated catch block
      // e.printStackTrace();
      // }



    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      // Auto-generated method stub
      Log.v(LOGTAG, "onServiceDisconnected IS call");
      upnpService = null;
    }
  };



  /**
   * @ClassName:  DeviceListRegistryListener
   * @Description: (远端设备和本地设备的搜索)
   * @author: 王少栋 
   * @date:   2015年7月29日 上午10:43:41 
   */
  public class DeviceListRegistryListener extends DefaultRegistryListener {



    /* Discovery performance optimization for very slow Android devices! */
    public DeviceListRegistryListener() {

    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {

    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device,
        final Exception ex) {}

    /*
     * End of optimization, you can remove the whole block if your Android handset is fast (>= 600
     * Mhz)
     */

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaServer")) {
        final DeviceItem display =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        deviceAdded(display);
      }
      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaRenderer")) {
        final DeviceItem display =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        dmrAdded(display);
      }
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
      final DeviceItem display = new DeviceItem(device, device.getDisplayString());
      deviceRemoved(display);

      if (device.getType().getNamespace().equals("schemas-upnp-org")
          && device.getType().getType().equalsIgnoreCase("MediaRenderer")) {
        final DeviceItem dis =
            new DeviceItem(device, device.getDetails().getFriendlyName(),
                device.getDisplayString(), "(REMOTE) " + device.getType().getDisplayString());
        dmrRemoved(dis);
      }

    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
      Log.v(LOGTAG, "localDeviceAdder is called");
      final DeviceItem display =
          new DeviceItem(device, device.getDetails().getFriendlyName(), device.getDisplayString(),
              "(REMOTE) " + device.getType().getDisplayString());
      deviceAdded(display);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
      final DeviceItem display = new DeviceItem(device, device.getDisplayString());
      deviceRemoved(display);
    }

    /**
     * All rights reserved by uata.
     * @Title: deviceAdded
     * @Description: 添加dms
     * @param di  
     * @return: void
     * @throws 
     */
    public void deviceAdded(final DeviceItem di) {
      Log.v(LOGTAG, "deviceAdded is called");

      runOnUiThread(new Runnable() {
        public void run() {

          // int position = deviceListAdapter.getPosition(di);
          // Log.v(LOGTAG, "deviceAdded-----"+position);
          // if (position >= 0) {
          // // Device already in the list, re-set new value at same
          // // position
          //
          // deviceListAdapter.remove(di);
          // deviceListAdapter.insert(di, position);
          // } else {
          // DeviceActivity.this.mDevList.add(di);
          // if(!DeviceActivity.this.mDmrList.contains(di))
          // {
          arrDevList.add(di);
          BaseApplication.deviceItem = (DeviceItem) StartActivity.this.arrDevList.get(0);
          // DeviceActivity.this.handler.sendEmptyMessage(2);

          // }
          // deviceListAdapter.add(di);
          // deviceListAdapter.notifyDataSetChanged();
          // }

          // Sort it?
          // listAdapter.sort(DISPLAY_COMPARATOR);
          // listAdapter.notifyDataSetChanged();
        }
      });
    }

    /**
     * All rights reserved by uata.
     * @Title: deviceRemoved
     * @Description: 移除dms
     * @param di  
     * @return: void
     * @throws 
     */
    public void deviceRemoved(final DeviceItem di) {
      runOnUiThread(new Runnable() {
        public void run() {
          // mDevList.remove(di);
          // deviceListAdapter.remove(di);
          arrDevList.remove(di);
          // deviceListAdapter.notifyDataSetChanged();
        }
      });
    }


    /**
     * All rights reserved by uata.
     * @Title: dmrAdded
     * @Description: 添加远端播放dmr
     * @param di  
     * @return: void
     * @throws 
     */
    public void dmrAdded(final DeviceItem di) {
      Log.v(LOGTAG, "dmrAdded is called");

      runOnUiThread(new Runnable() {

        @Override
        public void run() {
          // Auto-generated method stub
          // int position=mDmrDevAdapter.getPosition(di);
          // if(position>=0)
          // {
          // mDmrDevAdapter.remove(di);
          // mDmrDevAdapter.insert(di, position);
          // }else
          // {
          Log.v(TAG, "DMR ---" + StartActivity.this.mdmrDevAdapter);
          // DeviceActivity.this.mDmrDevAdapter.add(di);
          arrDmrList.add(di);
          Log.v(TAG, "StartActivity dmr--" + arrDmrList + "");


          // BaseApplication.dmrDevice=mDevList;

          // mDmrDevAdapter.notifyDataSetChanged();

          // }
        }
      });
    }

    /**
     * All rights reserved by uata.
     * @Title: dmrRemoved
     * @Description: 移除远端dmr
     * @param di  
     * @return: void
     * @throws 
     */
    public void dmrRemoved(final DeviceItem di) {
      runOnUiThread(new Runnable() {

        @Override
        public void run() {
          // Auto-generated method stub


          arrDmrList.remove(di);
          // mDmrDevAdapter.remove(di);
        }
      });
    }
  }



  /**
   * @Title: prepareMediaServer
   * @Description:  video image audio 资源解析  *@param:     *@return: void
   * @throws 
   */

  private void prepareMediaServer() {
    Log.v(TAG, "prepareMedia is call  ");
    if (serverPrepared) {
      return;
    }
    ContentNode rootNode = ContentTree.getRootNode();
    // Video Container
    Container videoContainer = new Container();
    videoContainer.setClazz(new DIDLObject.Class("object.container"));
    videoContainer.setId(ContentTree.VIDEO_ID);
    videoContainer.setParentID(ContentTree.ROOT_ID);
    videoContainer.setTitle("Videos");
    videoContainer.setRestricted(true);
    videoContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
    videoContainer.setChildCount(0);

    rootNode.getContainer().addContainer(videoContainer);
    rootNode.getContainer().setChildCount(rootNode.getContainer().getChildCount() + 1);
    ContentTree
        .addNode(ContentTree.VIDEO_ID, new ContentNode(ContentTree.VIDEO_ID, videoContainer));

    Cursor cursor;
    String[] videoColumns =
        {MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.ARTIST, MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.RESOLUTION};
    cursor =
        managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        String id =
            ContentTree.VIDEO_PREFIX
                + cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
        String creator =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
        String filePath =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        String mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
        String resolution =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
        Res res =
            new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                mimeType.substring(mimeType.indexOf('/') + 1)), size, "http://"
                + mediaServer.getAddress() + "/" + id);
        res.setDuration(duration / (1000 * 60 * 60) + ":" + (duration % (1000 * 60 * 60))
            / (1000 * 60) + ":" + (duration % (1000 * 60)) / 1000);
        res.setResolution(resolution);

        VideoItem videoItem = new VideoItem(id, ContentTree.VIDEO_ID, title, creator, res);
        videoContainer.addItem(videoItem);
        videoContainer.setChildCount(videoContainer.getChildCount() + 1);
        ContentTree.addNode(id, new ContentNode(id, videoItem, filePath));

        Log.v(LOGTAG, "added video item " + title + "from " + filePath);
      } while (cursor.moveToNext());
    }

    // Audio Container
    Container audioContainer =
        new Container(ContentTree.AUDIO_ID, ContentTree.ROOT_ID, "Audios", "GNaP MediaServer",
            new DIDLObject.Class("object.container"), 0);
    audioContainer.setRestricted(true);
    audioContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
    rootNode.getContainer().addContainer(audioContainer);
    rootNode.getContainer().setChildCount(rootNode.getContainer().getChildCount() + 1);
    ContentTree
        .addNode(ContentTree.AUDIO_ID, new ContentNode(ContentTree.AUDIO_ID, audioContainer));

    String[] audioColumns =
        {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM};
    cursor =
        managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        String id =
            ContentTree.AUDIO_PREFIX
                + cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String creator =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String filePath =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        String mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
        long duration =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        Res res =
            new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                mimeType.substring(mimeType.indexOf('/') + 1)), size, "http://"
                + mediaServer.getAddress() + "/" + id);
        res.setDuration(duration / (1000 * 60 * 60) + ":" + (duration % (1000 * 60 * 60))
            / (1000 * 60) + ":" + (duration % (1000 * 60)) / 1000);

        // Music Track must have `artist' with role field, or
        // DIDLParser().generate(didl) will throw nullpointException
        MusicTrack musicTrack =
            new MusicTrack(id, ContentTree.AUDIO_ID, title, creator, album, new PersonWithRole(
                creator, "Performer"), res);
        audioContainer.addItem(musicTrack);
        audioContainer.setChildCount(audioContainer.getChildCount() + 1);
        ContentTree.addNode(id, new ContentNode(id, musicTrack, filePath));

        // Log.v(LOGTAG, "added audio item " + title + "from " + filePath);
      } while (cursor.moveToNext());
    }

    // Image Container
    Container imageContainer =
        new Container(ContentTree.IMAGE_ID, ContentTree.ROOT_ID, "Images", "GNaP MediaServer",
            new DIDLObject.Class("object.container"), 0);
    imageContainer.setRestricted(true);
    imageContainer.setWriteStatus(WriteStatus.NOT_WRITABLE);
    rootNode.getContainer().addContainer(imageContainer);
    rootNode.getContainer().setChildCount(rootNode.getContainer().getChildCount() + 1);
    ContentTree
        .addNode(ContentTree.IMAGE_ID, new ContentNode(ContentTree.IMAGE_ID, imageContainer));

    String[] imageColumns =
        {MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE};
    cursor =
        managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        String id =
            ContentTree.IMAGE_PREFIX
                + cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        String title =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
        String creator = "unkown";
        String filePath =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        String mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

        Res res =
            new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                mimeType.substring(mimeType.indexOf('/') + 1)), size, "http://"
                + mediaServer.getAddress() + "/" + id);

        ImageItem imageItem = new ImageItem(id, ContentTree.IMAGE_ID, title, creator, res);
        imageContainer.addItem(imageItem);
        imageContainer.setChildCount(imageContainer.getChildCount() + 1);
        ContentTree.addNode(id, new ContentNode(id, imageItem, filePath));

        // Log.v(LOGTAG, "added image item " + title + "from " + filePath);
      } while (cursor.moveToNext());
    }

    serverPrepared = true;
  }

  private InetAddress getLocalIpAddress() throws UnknownHostException {
    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    int ipAddress = wifiInfo.getIpAddress();
    return InetAddress.getByName(String.format("%d.%d.%d.%d", (ipAddress & 0xff),
        (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
  }


  private void init() {


    ConfigData.arrDmrList = arrDmrList;
    Log.v(TAG, "DMR LIST--" + arrDmrList);
    mdmrDevAdapter = new DevAdapter(this, R.layout.dmr_item, arrDmrList);

  }

  private void localVideo() {
    LocalVideo lv = new LocalVideo();
    ConfigData.arrMediaVideo = lv.getLocalVideo(getApplicationContext());
    // Log.e(TAG, "数组的值为--"+ConfigData.mediaVideo);
    Utils ut = new Utils();
    List<Bitmap> bit = ut.getVideoThumbnail(ConfigData.arrMediaVideo, 350, 200, Thumbnails.MINI_KIND);
    ConfigData.bitMap = bit;
    // Log.e(TAG, "图片为--"+ConfigData.bitMap);
  }

  private void localImage() {
    LocalImage image = new LocalImage();
    ConfigData.arrMeidaImage = image.getLocalImage(getApplicationContext());
    // Log.v(TAG, "本地的图片是："+ConfigData.meidaImage);
  }


  private void localAudio() {
    LocalMusic audio = new LocalMusic();
    ConfigData.arrMediaAudio = audio.getLocalAudio(getApplicationContext());

    Log.v(TAG, "本地的音乐为：" + ConfigData.arrMediaAudio);
  }


  @Override
  protected void onDestroy() {
    // Auto-generated method stub
    super.onDestroy();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.start_lay);

    localVideo();
    localImage();
    localAudio();
    this.context = this;
    init();
    getIp();
    isWiFiActive(this.context);
    getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
        this.serviceConnection, Context.BIND_AUTO_CREATE);
    deviceListRegistryListener = new DeviceListRegistryListener();


    Log.v(TAG, "startActicity is start");
  }
}
