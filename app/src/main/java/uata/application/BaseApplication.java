package uata.application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.uata.dmp.ContentItem;
import com.uata.dmp.DeviceItem;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.support.model.DIDLContent;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @ClassName:  BaseApplication
 * @Description: 用于处理一些全局的变量
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:20:08 
 */
public class BaseApplication extends Application {
  public static DeviceItem deviceItem;
  public static DeviceItem dmrDeviceItem;
  private static String hostAddress;
  private static String hostName;
  private static InetAddress inetAddress;
  public static boolean isLocalDmr = true;
  public static Context mContext;
  public static AndroidUpnpService upnpService;
  public DIDLContent didl;
  public ArrayList<ContentItem> listMusic;
  public ArrayList<ContentItem> listPhoto;
  public ArrayList<ContentItem> listPlayMusic = new ArrayList();
  public ArrayList<ContentItem> listVideo;
  public ArrayList<ContentItem> listcontent;
  public static ArrayList<DeviceItem> dmrDevice = new ArrayList<DeviceItem>();
  public HashMap<String, ArrayList<ContentItem>> map;
  public int position;

  public static Context getContext() {
    return mContext;
  }

  public static String getHostAddress() {
    return hostAddress;
  }

  public static String getHostName() {
    return hostName;
  }

  public static InetAddress getLocalIpAddress() {
    return inetAddress;
  }



  /**
   * @Title: initImageLoader
   * @Description: 
   * @param: context    
   * @return: void
   * @throws 
   */
  public static void initImageLoader(Context context) {

    File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache"); // 缓存文件的存放地址
    ImageLoaderConfiguration config =
        new ImageLoaderConfiguration.Builder(context)
            .memoryCacheExtraOptions(480, 800)
            // max width, max height
            .threadPoolSize(3)
            // 线程池内加载的数量
            .threadPriority(Thread.NORM_PRIORITY - 2)
            // 降低线程的优先级保证主UI线程不受太大影响
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
            // 建议内存设在5-10M,可以有比较好的表现
            .memoryCacheSize(5 * 1024 * 1024)
            .discCacheSize(50 * 1024 * 1024)
            .discCacheFileNameGenerator(new Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .discCacheFileCount(100)
            // 缓存的文件数量
            .discCache(new UnlimitedDiscCache(cacheDir))
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
            .writeDebugLogs() // Remove for release app
            .build();
    // ImageLoaderConfiguration config2;
    // Builder config1 = config2.Builder(paramContext);
    // config1.memoryCacheExtraOptions(480, 800); // max width, max height
    // config1.threadPoolSize(3);//线程池内加载的数量
    // config1.threadPriority(Thread.NORM_PRIORITY - 2); //降低线程的优先级保证主UI线程不受太大影响
    // config1.denyCacheImageMultipleSizesInMemory();
    // config1.memoryCache(new LruMemoryCache(5 * 1024 * 1024)); //建议内存设在5-10M,可以有比较好的表现
    // config1.memoryCacheSize(5 * 1024 * 1024);
    // config1.discCacheSize(50 * 1024 * 1024);
    // config1.discCacheFileNameGenerator(new Md5FileNameGenerator());
    // config1.tasksProcessingOrder(QueueProcessingType.LIFO);
    // config1.discCacheFileCount(100); //缓存的文件数量
    // config1.discCache(new UnlimitedDiscCache(cacheDir)) ;
    // config1.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
    // config1.imageDownloader(new BaseImageDownloader(paramContext, 5 * 1000, 30 * 1000)); //
    // connectTimeout (5 s), readTimeout (30 s)
    // config1.writeDebugLogs(); // Remove for release app
    // config1.build();

    ImageLoader.getInstance().init(config);
  }

  public static void setHostAddress(String paramString) {
    hostAddress = paramString;
  }

  public static void setHostName(String paramString) {
    hostName = paramString;
  }

  public static void setLocalIpAddress(InetAddress paramInetAddress) {
    inetAddress = paramInetAddress;
  }

  @Override
  public void onCreate() {
    super.onCreate();
//    mContext = getApplicationContext();
    initImageLoader(getApplicationContext());
  }
}
