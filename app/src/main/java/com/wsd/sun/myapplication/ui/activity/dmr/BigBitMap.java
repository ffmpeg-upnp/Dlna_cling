package com.wsd.sun.myapplication.ui.activity.dmr;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wsd.sun.myapplication.R;
import com.wsd.sun.myapplication.adapter.GroupAdapter;
import com.wsd.sun.myapplication.component.SmartImageView;
import com.wsd.sun.myapplication.ui.activity.BaseActivity;
import com.wsd.sun.myapplication.ui.activity.BaseApplication;
import com.wsd.sun.myapplication.ui.activity.dmc.ControlActivity;
import com.wsd.sun.myapplication.ui.activity.dmc.GenerateXml;
import com.wsd.sun.myapplication.ui.activity.dmp.ContentItem;
import com.wsd.sun.myapplication.ui.activity.dmp.DeviceItem;
import com.wsd.sun.myapplication.ui.activity.dms.ContentBrowseActionCallback;
import com.wsd.sun.myapplication.ui.activity.dms.MediaServer;
import com.wsd.sun.myapplication.utils.ConfigData;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BigBitMap extends BaseActivity {


  private String name;
  private String path;
  private int position;
  private String metaData;
  private SmartImageView img;
  private Bitmap bmp;
  private String playUri;
  private String currentContentFormatMimeType = "";
  public static final  String LOGTAG = " BigBitMap";
  private GroupAdapter groupAdapter;
  private PopupWindow popupWindow;
  private ArrayList<DeviceItem> popDmrList;
  private ListView lviGplayer;
  private ListView lviDmr;
  private MediaServer mediaServer;
  private AndroidUpnpService upnpService;
  private ArrayList<ContentItem> arrImageList;
  public ArrayList<DeviceItem> arrDevList = new ArrayList();
  private DeviceListRegistryListener deviceListRegistryListener;
  private Context context;


  Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      // Auto-generated method stub
//      switch (msg.what) {
//        case 0:
//         
//          break;
//
//        default:
//          break;
//      }
      
      if (msg.what == 0) {
        bmp = (Bitmap) msg.obj;
        img.setImageBitmap(bmp);
      }

    }


  };

  Runnable runnable = new Runnable() {

    @Override
    public void run() {
      // Bitmap bmp = getLoacalBitmap(path);
      Bitmap bmp = compressImageFromFile(path);
      Message msg = new Message();
      msg.what = 0;
      msg.obj = bmp;
  
      handler.sendMessage(msg);

    }
  };

  private void initData()

  {
    img = (SmartImageView) findViewById(R.id.big_map);
    Intent intent = getIntent();
    if (intent == null) {
      Toast.makeText(BigBitMap.this, "数据加载异常", Toast.LENGTH_LONG).show();
    } else {
      this.path = intent.getStringExtra("path");
      this.position = Integer.parseInt(intent.getStringExtra("ID"));
      Glide.with(context)
              .load(new File( path ) )
              .asBitmap()
              .into( img);
    }
  }

  /**
   * 
   * @param图片压缩
   * @return
   */

  private Bitmap compressImageFromFile(String srcPath) {
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    newOpts.inJustDecodeBounds = true;// 只读边,不读内容
    Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;
    float hh = 800f;//
    float ww = 480f;//
    int be = 1;
    if (w > h && w > ww) {
      be = (int) (newOpts.outWidth / ww);
    } else if (w < h && h > hh) {
      be = (int) (newOpts.outHeight / hh);
    }
    if (be <= 0) be = 1;
    newOpts.inSampleSize = be;// 设置采样率

    newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
    newOpts.inPurgeable = true;// 同时设置才会有效
    newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

    bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
    // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
    // 其实是无效的,大家尽管尝试
    return bitmap;
  }



  /**
   * 加载本地图片
   * 
   * @param url
   * @return
   */
  public static Bitmap getLoacalBitmap(String url) {
    try {
      FileInputStream fis = new FileInputStream(url);
      return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  
  /**
   *  
   *@Title: getUriImage
   *@Description: 加载图片
   *@param: @param url
   *@param: @return  
   *@return: Bitmap
   *@throws 
   */
//  public Bitmap getUriImage(String url) {
//    Bitmap bmp = null;
//    try {
//      URL myurl = new URL(url);
//      // 获得连接
//      HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
//      conn.setConnectTimeout(6000);// 设置超时
//      conn.setDoInput(true);
//      conn.setUseCaches(false);// 不缓存
//      conn.connect();
//      InputStream is = conn.getInputStream();// 获得图片的数据流
//      bmp = BitmapFactory.decodeStream(is);
//      is.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return bmp;
//  }

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.big_bit_map, menu);
//
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle action bar item clicks here. The action bar will
//    // automatically handle clicks on the Home/Up button, so long
//    // as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();
//    if (id == R.id.image_push) {
//      getPopupWindow();
//      return true;
//    }
//    if (id == android.R.id.home) {
//      finish();
//      return true;
//    }
//    return super.onOptionsItemSelected(item);
//  }



  @Override
  protected void onDestroy() {
    // Auto-generated method stub
    if (bmp != null && !bmp.isRecycled()) {
      bmp.recycle();
    }
    super.onDestroy();
  }


  private void getUriImage() {
    ContentItem localItem = BigBitMap.this.arrImageList.get(position);
    if (localItem.isContainer()) {
      BigBitMap.this.upnpService.getControlPoint().execute(
          new ContentBrowseActionCallback(BigBitMap.this, localItem.getService(), localItem
              .getContainer(), BigBitMap.this.arrImageList, BigBitMap.this.handler));

    } else {
      Log.v("localContentItem", localItem.getItem() + "");
      List<Res> localList = localItem.getItem().getResources();
      Res type = localList.get(0);
      MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      String str = localMimeType.getType();

      this.playUri = localItem.getItem().getFirstResource().getValue();
      this.name = localItem.toString();
      this.currentContentFormatMimeType = localMimeType.toString();
      try {
        metaData = new GenerateXml().generate(localItem).toString();
      } catch (Exception e) {
        // Auto-generated catch block
        e.printStackTrace();
      }

      Log.i(LOGTAG, name);
      Log.i(LOGTAG, playUri);
      Log.i(LOGTAG, currentContentFormatMimeType);
      


    }
  }


  /**
   * pop
   * 
   */

  // popwindow 的显示/隐藏
  protected void getPopupWindow() {
    Log.v("popwindow", popupWindow + "");

    if (null != popupWindow) {
      popupWindow.dismiss();
      popupWindow = null;
      return;
    } else {
      initPopuptWindow();
    }
  }

  private void initPopuptWindow() {

 
    // activity.getDmrList();
    this.popDmrList = ConfigData.arrDmrList;
    // this.popDmrList=new ArrayList<DeviceItem>();
    Log.v(LOGTAG, "pop---" + this.popDmrList);
    View popupWindow_View;

    if (this.popupWindow == null) {
      // 获取自定义布局文件activity_popupwindow_left.xml的视图
      popupWindow_View = getLayoutInflater().inflate(R.layout.player_phone_popup_top, null);


      this.lviGplayer = (ListView) popupWindow_View.findViewById(R.id.gplay_list);

      groupAdapter = new GroupAdapter(BigBitMap.this, this.popDmrList);

      this.lviGplayer.setAdapter(groupAdapter);
      // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
      this.popupWindow = new PopupWindow(popupWindow_View, 350, LayoutParams.WRAP_CONTENT);
    }

    // MusicControl.this.popupWindow.showAtLocation(findViewById(R.id.audio), Gravity.RIGHT, 0, 0);
    BigBitMap.this.popupWindow.showAsDropDown(findViewById(R.id.toolbar), 100, 0);
    this.lviGplayer.setOnItemClickListener(new OnItemClickListener(
    ) {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Auto-generated method stub
       Toast.makeText(BigBitMap.this, "您选择了第" + arg2 + "播放器", Toast.LENGTH_LONG).show();
        if (arg2 == 0) {

        } else {

          BaseApplication.dmrDeviceItem = (DeviceItem) BigBitMap.this.popDmrList.get(arg2);
          Intent intent = new Intent(BigBitMap.this, ControlActivity.class);
          intent.putExtra("ID", String.valueOf(position));
          intent.putExtra("name", name);
          intent.putExtra("playURI", playUri);
          intent.putExtra("currentContentFormatMimeType", currentContentFormatMimeType);
          intent.putExtra("metaData", metaData);
          popupWindow.dismiss();
          startActivity(intent);
          finish();
        }
      }
    });

  }


  /**
   * 
   * @author Administrator 远端设备和本地设备的搜索
   *
   */
  // 监听设备的移除与添加
  public class DeviceListRegistryListener extends DefaultRegistryListener {



    /**
     * @see DefaultRegistryListener#remoteDeviceDiscoveryStarted(Registry,
     *      RemoteDevice)
     * @Title: remoteDeviceDiscoveryStarted
     * @Description:  (这里用一句话描述这个方法的作用)   *@param: @param registry  *@param: @param device  
     * @throws 
     */
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
      // Auto-generated method stub
      super.remoteDeviceDiscoveryStarted(registry, device);
    }



    /* Discovery performance optimization for very slow Android devices! */
    public DeviceListRegistryListener() {

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
      // Log.v(LOGTAG, "localDeviceAdder is called");
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

   
    public void deviceAdded(final DeviceItem di) {
      // Log.v(LOGTAG, "deviceAdded is called");

      runOnUiThread(new Runnable() {
        public void run() {
          arrDevList.add(di);
          BaseApplication.deviceItem = (DeviceItem) BigBitMap.this.arrDevList.get(0);
        }
      });
    }

    /**
     * All rights reserved by uata.
     * @Title: deviceRemoved
     * @Description: 设备移除
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
     * @Description: 添加远端dmr
     * @param di  
     * @return: void
     * @throws 
     */
    public void dmrAdded(final DeviceItem di) {
      // Log.v(LOGTAG, "dmrAdded is called");

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
          // Log.v(TAG, "DMR ---"+ StartActivity.this.mDmrDevAdapter);
          // DeviceActivity.this.mDmrDevAdapter.add(di);
          popDmrList.add(di);
          Log.v(LOGTAG, "StartActivity dmr--" + popDmrList + "");


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


          popDmrList.remove(di);
          // mDmrDevAdapter.remove(di);
        }
      });
    }
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_big_bit_map);
    initToolbar("钱包", "推送", new OnToolbarMenuClickListener() {
      @Override
      public void onItemClick() {
        getPopupWindow();
      }
    });
    this.arrImageList = ConfigData.arrlistcontent;
    context=this;
    initData();
    getUriImage();

    new Thread(runnable).start();
    // getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
    // this.serviceConnection, Context.BIND_AUTO_CREATE);
    deviceListRegistryListener = new DeviceListRegistryListener();// 可以在任何时刻添加与检索设备
    getActionBar().setDisplayHomeAsUpEnabled(true);

  }



}
