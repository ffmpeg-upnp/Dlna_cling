package uata.Menu;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.uata.dlna_application.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:  DeviceActivity
 * @Description: (主页)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:04:49 
 */
public class DeviceActivity extends SlidingFragmentActivity {

  
  public static final int DMR_GET_NO = 0;
  public static final int DMR_GET_SUC = 1;
  private static final String LOGTAG = "DevicesActivity";
  private static final String TAG = "DeviceActivity";
//  private static boolean serverPrepared = false;
//  private AndroidUpnpService upnpService;
//  private MediaServer mediaServer;
//  private LinearLayout linear;

//  private ArrayAdapter<DeviceItem> deviceListAdapter;
//  private ArrayAdapter<ContentItem> contentListAdapter;
  // private DeviceListRegistryListener deviceListRegistryListener;
 // public DevAdapter mDmrDevAdapter;
  // private DevAdapter mDevAdapter;
//  private ListView lViDmr, mDevLv, contentListView;
//  public ArrayList<DeviceItem> arrDevList = new ArrayList();
//  private ArrayList<ContentItem> contentItem = new ArrayList();
//  public ArrayList<DeviceItem> arrDmrList = new ArrayList();
//  private ArrayList<ContentItem> list;
  // private ArrayList<ContentItem> listMusic = new ArrayList();
  // private ArrayList<ContentItem> listPhoto = new ArrayList();
  // private ArrayList<ContentItem> listVideo = new ArrayList();
//  private ArrayList<ContentItem> arrContentItems = new ArrayList();
//  public HashMap<String, ArrayList<ContentItem>> map = new HashMap();
  public int folder = -1;
//  private String dmr;
  private ImageView imgViewqq; 
  private ImageView imgViewpptv; 
  private ImageView imgsohu;
  private ImageView  imgViewstorm;
  

  private SlidingMenu menu;
//  private Fragment conFragment;
  private List<String> listDatas = new ArrayList<String>();
  private RelativeLayout movie;
  private MenuFragment menuFragment;

  private static final String URL_PPTV = "http://app.pptv.com/android/";
  private static final String URL_QQ = "http://v.qq.com/download_mobile.html#aphone";
  private static final String URL_SOHU = "http://tv.sohu.com/app/?x=1";
  private static final String URL_STORM = "http://shouji.baofeng.com/";
  private static final String URL_TARGETV = "http://www.targetv.com/index.php/site/index";

  private PackageManager packageManager;

//  private Handler handler = new Handler() {

//    @Override
//    public void handleMessage(Message msg) {
//      switch (msg.what) {
//        case 0:
//
//          break;
//        case 1:
//
////          BaseApplication localBaseApplication =
////              (BaseApplication) DeviceActivity.this.getApplication();
////          localBaseApplication.listcontent = DeviceActivity.this.arrContentItems;
////          Log.d("listcontent", "数组长度---" + localBaseApplication.listcontent.size());
//
//          break;
//        case 2:
//          // init1();
//          break;
//        default:
//          break;
//      }
//    }

//  };

  /**
   * \
   * 
   * @Title: initView
   * @Description:  (初始化侧滑布局)   *@param:     *@return: void
   * @throws 
   */

  private void initView() {

    setBehindContentView(R.layout.menu_layout);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    menuFragment = new MenuFragment();
    fragmentTransaction.replace(R.id.menu_frame, menuFragment);
    fragmentTransaction.commit();
    // getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
    // menuFragment);

    // conFragment = new Fragment5();//新建主页
    // configure the SlidingMenu
    // menu = new SlidingMenu(this);
    // 设置触摸屏幕的模式
    // menu = new SlidingMenu(this,SlidingMenu.SLIDING_CONTENT);
    menu = getSlidingMenu();
    menu.setMode(SlidingMenu.RIGHT);
    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

    // menu.setSecondaryShadowDrawable(R.drawable.shadow);
    menu.setShadowWidth(20);// 设置阴影有立体效果
    menu.setShadowDrawable(null); // 设置滑动阴影的图像资源
    menu.setBehindOffset(50);
    menu.setBehindScrollScale(1);// 设置SlidingMenu与下方视图的移动的速度比，当为1时同时移动，取值0-1
    menu.setBehindWidth((int) (getWindowManager().getDefaultDisplay().getWidth() * 0.6));
    menu.setFadeDegree(1.0f);

    // 设置主界面和滑动视图

    // menu.setMenu(R.layout.main);
    // menu.setMenu(R.layout.menu_main);
    // //menu.setSecondaryMenu(R.layout.second_menu);
    // ft.replace(R.id.menu_frame, menuFragment);
    // // ft.add(R.layout.menu_layout, menuFragment);
    // ft.commit();

    setSlidingActionBarEnabled(true);
    // getActionBar().setDisplayHomeAsUpEnabled(true);
  }

//  public ArrayList<DeviceItem> getDmrList() {
//    return this.arrDmrList;
//  }

  /**
   * 
   * @Title: initData
   * @Description:  (初始化控件及控件监听)   *@param:     *@return: void
   * @throws 
   */

  private void initData() {
    imgViewqq = (ImageView) findViewById(R.id.qq_live);
    imgViewpptv = (ImageView) findViewById(R.id.pptv_live);
    imgsohu = (ImageView) findViewById(R.id.sohu_live);
    imgViewstorm = (ImageView) findViewById(R.id.storm_live);

    imgViewqq.setOnClickListener(qqOnClickListener);
    imgViewpptv.setOnClickListener(pptvOnClickListener);
    imgsohu.setOnClickListener(sohuOnClickListener);
    imgViewstorm.setOnClickListener(stormOnClickListener);
  }

  OnClickListener qqOnClickListener = new OnClickListener() {

    @Override
    public void onClick(View v) {
      // Auto-generated method stub
      Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.qqlive");
      if (intent != null) {

        startActivity(intent);
      } else {

        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_QQ)));
      }
    }
  };
  OnClickListener pptvOnClickListener = new OnClickListener() {

    @Override
    public void onClick(View v) {
      // Auto-generated method stub
      Intent intent = getPackageManager().getLaunchIntentForPackage("com.pplive.androidphone");
      if (intent != null) {

        startActivity(intent);
      } else {

        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_PPTV)));
      }
    }
  };
  OnClickListener sohuOnClickListener = new OnClickListener() {

    @Override
    public void onClick(View v) {
      // Auto-generated method stub
      Intent intent = getPackageManager().getLaunchIntentForPackage("com.sohu.sohuvideo");
      if (intent != null) {

        startActivity(intent);
      } else {

        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_SOHU)));

      }
    }
  };
  OnClickListener stormOnClickListener = new OnClickListener() {

    @Override
    public void onClick(View view) {
      // Auto-generated method stub
      Intent intent = getPackageManager().getLaunchIntentForPackage("com.storm.smart");
      if (intent != null) {

        startActivity(intent);
      } else {

        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(URL_STORM)));
      }
    }
  };

  @Override
  protected void onDestroy() {
    // Auto-generated method stub
    super.onDestroy();
    // if (upnpService != null) {
    // upnpService.getRegistry()
    // .removeListener(deviceListRegistryListener);
    // getApplicationContext().unbindService(serviceConnection);
    // }
  }

  // protected Container createRootContainer(Service service) {
  // Container rootContainer = new Container();
  // rootContainer.setId("0");
  // rootContainer.setTitle("Content Directory on "
  // + service.getDevice().getDisplayString());
  // return rootContainer;
  // }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.device, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_top:
        // toggle就是程序自动判断是打开还是关闭
        toggle();
        // menu.showMenu();// show menu
        // getSlidingMenu().showContent();//show content
        // ActionBar actionBar = getActionBar();
        // actionBar.hide();
        return true;
      case 1:
        break;
      case 2:
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

//  private void init() {

//    ConfigData.dmrList = arrDmrList;
//    Log.v(TAG, "DMR LIST" + arrDmrList);
//    mDmrDevAdapter = new DevAdapter(this, R.layout.dmr_item, arrDmrList);

//  }

  //
  // public class ContentAdapter extends BaseAdapter{
  //
  // private static final String TAG="ContentAdapter";
  // private ArrayList<ContentItem> mContentItem;
  // private LayoutInflater mInflater;
  // private Context mContext;
  //
  //
  //
  //
  // public ContentAdapter(ArrayList<ContentItem> mContent) {
  // super();
  // this.mContentItem = mContent;
  // this.mInflater = ((LayoutInflater)getSystemService("layout_inflater"));
  // }
  // @Override
  // public int getCount() {
  // // Auto-generated method stub
  // return this.mContentItem.size();
  // }
  // @Override
  // public ContentItem getItem(int position) {
  // // Auto-generated method stub
  // return (ContentItem)this.mContentItem.get(position);
  // }
  // @Override
  // public long getItemId(int position) {
  // // Auto-generated method stub
  // return position;
  // }
  // @Override
  // public View getView(int position, View convertView, ViewGroup parent) {
  // // Auto-generated method stub
  // ContentHolder localContentHolder;
  // ContentItem localContentItem;
  // if(convertView==null)
  // {
  // convertView=this.mInflater.inflate(R.layout.dmr_item, null);
  // localContentHolder = new ContentHolder();
  // localContentItem=(ContentItem)mContentItem.get(position);
  // localContentHolder.filename =
  // ((TextView)convertView.findViewById(R.id.dmr_name_tv));
  // // localDevHolder.checkBox =
  // ((CheckBox)convertView.findViewById(R.id.dmr_cb));
  // localContentHolder.filename.setText(localContentItem.toString());
  // convertView.setTag(localContentHolder);
  // }else{
  // localContentHolder = (ContentHolder)convertView.getTag();
  // localContentItem=this.mContentItem.get(position);
  // localContentHolder.filename.setText(localContentItem.toString());
  // }
  // return convertView;
  //
  // }
  // public final class ContentHolder
  // {
  // public TextView filename;
  //
  // public ContentHolder()
  // {
  // }
  // }
  //
  // }
  //
  //
  //
  //
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.main);
    // getApplicationContext().bindService(new Intent(this,
    // AndroidUpnpServiceImpl.class), this.serviceConnection,
    // Context.BIND_AUTO_CREATE);
    Log.v(TAG, "Oncreate into");
//    list = new ArrayList<ContentItem>();
    setContentView(R.layout.fragment_5);
    // mDmrDevAdapter=new DevAdapter(this, R.layout.dmr_item, mDmrList);
    // deviceListRegistryListener = new DeviceListRegistryListener();
    this.packageManager = getPackageManager();
    // 设置是否能够使用ActionBar来滑动

    initView();
    // getActionBar().setDisplayHomeAsUpEnabled(true);
    Log.v(TAG, "oncreate goto");
    // init();
    initData();

  }

}
