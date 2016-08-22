package uata.Menu;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.uata.MenuList.ContentActivity;
import com.uata.MenuList.ImageDisplay;
import com.uata.MenuList.MovieList;
import com.uata.MenuList.MusicListActivity;
import com.uata.application.BaseApplication;
import com.uata.application.ConfigData;
import com.uata.dlna_application.R;
import com.uata.dmp.ContentItem;
import com.uata.dms.ContentBrowseActionCallback;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.container.Container;

import java.util.ArrayList;


/**
*@ClassName:  MenuFragment
*@Description:侧滑的页面
*@author: 王少栋 
*@date:   2015年8月4日 下午4:13:33 
*/
public class MenuFragment extends Fragment {

  private LinearLayout  linear;
  private ListView list;


  public static final  String TAG = "MenuFragMent";
  private AndroidUpnpService upnpService;
  private ArrayList<ContentItem> contentItems = new ArrayList<ContentItem>();
  private ArrayList<ContentItem> arrContentList = new ArrayList<ContentItem>();
  private ContentAdapter adapterContent;
  protected ImageLoader imageLoader = ImageLoader.getInstance();
  private TextView gy;
  private TextView version;
  private ProgressBar pro;
  OnItemClickListener contentItemClickListener = new OnItemClickListener() {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
        long paramLong) {
      
//      activity.getDmrList();
      // Log.v(TAG, activity.getDmrList()+"");
      ContentItem localContentItem = (ContentItem) MenuFragment.this.arrContentList.get(paramInt);

      String str = localContentItem.toString();





      if (localContentItem.isContainer()) {

        MenuFragment.this.upnpService.getControlPoint().execute(
            new ContentBrowseActionCallback(MenuFragment.this, localContentItem.getService(),
                localContentItem.getContainer(), MenuFragment.this.arrContentList,
                MenuFragment.this.handler));



      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Auto-generated catch block
        e.printStackTrace();
      }

      if (str.equals("本地视频")) {

        Intent localIntent = new Intent(MenuFragment.this.getActivity(), MovieList.class);

        // localIntent.p
        startActivity(localIntent);
      } else if (str.equals("本地图片")) {

        Intent intent = new Intent(MenuFragment.this.getActivity(), ImageDisplay.class);
        startActivity(intent);
      } else if (str.equals("本地音乐")) {
        Intent intent = new Intent(MenuFragment.this.getActivity(), MusicListActivity.class);
        startActivity(intent);
      }



      // }else{
      // Log.v("localContentItem",localContentItem.getItem()+"" );
      // List<Res> localList =localContentItem.getItem().getResources();
      // Res type = localList.get(0);
      // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      // String str=localMimeType.getType();
      //
      // if (str.equals("image"))
      // {
      //
      // MenuFragment.this.jumpToImage(localContentItem);
      // return;
      // }else{
      // MenuFragment.this.jumpToControl(localContentItem);
      // }
      //
      // }

    }
  };

  private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0:



          new ArrayList();
          MenuFragment.this.arrContentList = ConfigData.arrlistDirctory;

          for (int i = 0; i < MenuFragment.this.arrContentList.size(); i++) {
            if (arrContentList.get(i).getContainer().getTitle().equals("Videos")) {
              arrContentList.get(i).getContainer().setTitle("本地视频");
            } else if (arrContentList.get(i).getContainer().getTitle().equals("Images")) {
              arrContentList.get(i).getContainer().setTitle("本地图片");
            } else if (arrContentList.get(i).getContainer().getTitle().equals("Audios")) {
              arrContentList.get(i).getContainer().setTitle("本地音乐");
            }

          }

          Log.v("目录列表", "" + MenuFragment.this.arrContentList);
          ConfigData.arrlistDirctory = MenuFragment.this.arrContentList;
          Log.v("MovieList", "MovieList---" + ConfigData.arrlistcontent + "");
          initview();


          break;
        case 4002:
          try {
            Thread.sleep(3000L);

          } catch (InterruptedException e) {
            // Auto-generated catch block
            e.printStackTrace();
          }
          pro.setVisibility(View.INVISIBLE);
          Toast.makeText(getActivity(), "暂无更新", 0).show();
          break;
        case 4001:
            break;
        default:
          break;
      }

    }
  };

  /**
   * 在deviceActivity中的侧滑菜单中已经加载过一次布局
   */
  // @Override
  // public View onCreateView(LayoutInflater inflater, ViewGroup container,
  // Bundle savedInstanceState) {

  // layout = (LinearLayout) inflater.inflate(R.layout.menu_layout,
  // container, false);
  //
  //
  // return layout;
  // }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    // Auto-generated method stub
    super.onActivityCreated(savedInstanceState);

    init();
    // setHasOptionsMenu(true);
    initView();

  }


  protected Container createRootContainer(Service service) {
    Container rootContainer = new Container();
    rootContainer.setId("0");
    String str = service.getDevice().getDisplayString();

    rootContainer.setTitle("Content Directory on " + str);
    return rootContainer;
  }

  private void initView() {
    linear = (LinearLayout) getActivity().findViewById(R.id.menu_home);
    linear.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        // Auto-generated method stub
        ((SlidingFragmentActivity) getActivity()).toggle();
      }
    });

    gy = (TextView) getActivity().findViewById(R.id.gy_menu);
    gy.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {

        Intent intent = new Intent(getActivity(), ContentActivity.class);
        startActivity(intent);
      }
    });
    pro = (ProgressBar) getActivity().findViewById(R.id.content_pro);
    version = (TextView) getActivity().findViewById(R.id.tx_menu);
    version.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // Auto-generated method stub
        pro.setVisibility(View.VISIBLE);
        MenuFragment.this.handler.sendEmptyMessageDelayed(4002, 2000L);

      }
    });

  }

  private void init() {


    this.upnpService = BaseApplication.upnpService;
    // this.mTitleView.setText(BaseApplication.deviceItem.toString());
    Log.v("Fragment", "deviceItem" + BaseApplication.deviceItem);
    Service localService =
        BaseApplication.deviceItem.getDevice().findService(new UDAServiceType("ContentDirectory"));

    upnpService.getControlPoint().execute(
        new ContentBrowseActionCallback(MenuFragment.this, localService,
            createRootContainer(localService), contentItems, handler));

    // this.mLastDevice = BaseApplication.deviceItem.toString();



  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Auto-generated method stub
    inflater.inflate(R.menu.frag, menu);

  }

  private void jumpToControl(ContentItem paramContentItem) {
    Intent localIntent = new Intent(MenuFragment.this.getActivity(), MovieList.class);

    startActivity(localIntent);
  }

  private void jumpToImage(ContentItem paramContentItem) {
    Intent intent = new Intent(MenuFragment.this.getActivity(), ImageDisplay.class);
    startActivity(intent);
  }

  private void initview() {
    // this.arrContentList=ConfigData.listDirctory;
    Log.v("Dirctory ListView", "list------" + this.arrContentList);
    list = (ListView) getActivity().findViewById(R.id.cadao_list);
    // Log.v("Fragment", "listview------"+list);
    this.adapterContent = new ContentAdapter(getActivity(), this.arrContentList, imageLoader);
    // ArrayAdapter<ContentItem> adapter=new
    // ArrayAdapter<ContentItem>(MenuFragment.this.getActivity(),R.layout.list_item,
    // this.arrContentList);
    list.setAdapter(this.adapterContent);
    list.setOnItemClickListener(contentItemClickListener);
  }



  private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
      // Auto-generated method stub
      super.onLoadingCancelled(imageUri, view);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      // Auto-generated method stub
      super.onLoadingComplete(imageUri, view, loadedImage);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
      // Auto-generated method stub
      super.onLoadingFailed(imageUri, view, failReason);
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
      // Auto-generated method stub
      super.onLoadingStarted(imageUri, view);
    }

  }



  class ContentAdapter extends BaseAdapter {

    private Bitmap audioIcon;
    public int dmrPosition = 0;
    private Bitmap folderIcon;
    private Bitmap imageIcon;
    private ArrayList<ContentItem> arrDeviceItems;
    private Bitmap videoIcon;
    public ContentAdapter(Context context, ArrayList<ContentItem> arg2, ImageLoader imageLoader) {
      this.arrDeviceItems = arg2;

      this.imageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.image_png);
      this.videoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.movie_png);
      this.audioIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mp3_png);
      this.folderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder);

      // this.mImageLoader=imageLoader;
      // this.mImageLoader=imageLoader;
      // this.options = new DisplayImageOptions.Builder()
      // .showImageForEmptyUri(R.drawable.ic_empty)
      // .showImageOnFail(R.drawable.ic_error)
      // .resetViewBeforeLoading(true)
      // .cacheOnDisc(true)
      // .imageScaleType(ImageScaleType.EXACTLY)
      // .bitmapConfig(Bitmap.Config.RGB_565)
      // .displayer(new FadeInBitmapDisplayer(300))
      // .build();
      // animateFirstListener = new AnimateFirstDisplayListener();

    }

    @Override
    public int getCount() {
      // Auto-generated method stub
      return arrDeviceItems.size();
    }

    @Override
    public Object getItem(int position) {
      // Auto-generated method stub
      return arrDeviceItems.get(position);
    }

    @Override
    public long getItemId(int position) {
      // Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // Auto-generated method stub
      ViewHolder viewHolder;
      View convert = convertView;
      if (convert == null) {
        convert =
            LayoutInflater.from(MenuFragment.this.getActivity()).inflate(R.layout.line_img1, null);
        viewHolder = new ViewHolder();
        viewHolder.tuImg = (ImageView) convert.findViewById(R.id.img_tu);
        viewHolder.tuText = (TextView) convert.findViewById(R.id.tv_tu);
        convert.setTag(viewHolder);
        ContentItem localItem = this.arrDeviceItems.get(position);
        viewHolder.tuText.setText(localItem.toString());


        if (localItem.toString().equals("本地视频")) {
          viewHolder.tuImg.setImageBitmap(this.videoIcon);
        } else if (localItem.toString().equals("本地图片")) {
          viewHolder.tuImg.setImageBitmap(this.imageIcon);
        } else {
          viewHolder.tuImg.setImageBitmap(this.audioIcon);
        }
        // if(localItem.isContainer())
        // {
        // MenuFragment.this.upnpService.getControlPoint().execute(
        // new ContentBrowseActionCallback(MenuFragment.this,
        // localItem.getService(), localItem
        // .getContainer(),
        // MenuFragment.this.arrContentList,
        // MenuFragment.this.handler));
        //
        //
        //
        // }else{
        // Log.v("localContentItem",localItem.getItem()+"" );
        // List<Res> localList =localItem.getItem().getResources();
        // Res type = localList.get(0);
        // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
        // String str=localMimeType.getType();
        //
        // str1=localItem.getItem().getFirstResource().getValue();
        // System.out.println(str1);
        // // if (str.equals("image"))
        // // {
        // //
        // // viewHolder.tuImg.setImageBitmap(imageIcon);
        // // str1=localItem.getItem().getFirstResource().getValue();
        // //
        // // }else
        // // if(str.equals("video")){
        // // viewHolder.tuImg.setImageBitmap(videoIcon);
        // // }else
        // // {
        // // viewHolder.tuImg.setImageBitmap(audioIcon);
        // // str1=localItem.getItem().getFirstResource().getValue();
        // // }
        // }
      } else {
        viewHolder = (ViewHolder) convert.getTag();

      }
      // imageLoader.displayImage(str1,
      // viewHolder.tuImg, options,
      // this.animateFirstListener);

      return convert;
    }

  }

  private static class ViewHolder {
    private ImageView tuImg;
    private TextView tuText;
  }

}
