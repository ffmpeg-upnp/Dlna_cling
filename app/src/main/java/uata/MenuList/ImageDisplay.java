package uata.MenuList;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.uata.MediaModel.MediaImage;
import com.uata.application.ConfigData;
import com.uata.dlna_application.R;
import com.uata.dmp.ContentItem;
import com.uata.dmr.BigBitMap;

import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:  ImageDisplay
 * @Description:  本地图片的列表
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:27:40 
 */
public class ImageDisplay extends Activity implements OnClickListener {

  public static final  String TAG = "ImageDisplay";
 private String currentContentFormatMimeType = "";
  private boolean isRemote = false;
//  private boolean isRender = false;
//  private DMCControl dmcControl = null;
//  private DeviceItem dmrDeviceItem = null;
//  private int imageDisplayCount = 0;
//  private int mcurrentPosition;
  DisplayImageOptions options;
  private GridView girdView;
  private ArrayList<MediaImage> mmImage;
  private List<Bitmap> bitmap;

  private ArrayList<ContentItem> marrImageList = new ArrayList<ContentItem>();
  private ContentAdapter adapterContent;
  private Context context;

  protected ImageLoader imageLoader = ImageLoader.getInstance();
  private ArrayList<ContentItem> marrListPhotos = new ArrayList();
  private Handler mhandler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
//      switch (msg.what) {
//        case 0:
//          System.out.println("success");
//          break;
//        case 1:
//
//          initView();
//          break;
//        default:
//          break;
//      }
      
      if (msg.what == 1) {
        initView();
      } else if (msg.what == 0) {
        
      }


    }
  };



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.image_display, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_display);
    getActionBar().setDisplayHomeAsUpEnabled(true);


    // initData();
    // System.out.println();
    getLocalImage();
    bitmap = new ArrayList<Bitmap>();
    // getImageThumbnail(getLocalImage(), 100, 80, Thumbnails.MINI_KIND);
    initDate();
    this.options =
        new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty)
            .showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true)

            .cacheInMemory(true)
            // 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)
            // 设置下载的图片是否缓存在SD卡中
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(300)).build();
    initView();

    // image_zoom(playURI);
  }

  // private void initData()
  // {
  // Intent localIntent = getIntent();
  // this.playURI = localIntent.getStringExtra("playURI");
  //
  // this.mCurrentPosition = ConfigData.photoPosition;
  // this.mListPhotos = ConfigData.listPhotos;
  //
  // Log.v(TAG, "list==="+ConfigData.listPhotos.size());
  // // this.dmrDeviceItem = BaseApplication.dmrDeviceItem;
  // // this.upnpService = BaseApplication.upnpService;
  // // this.isLocalDmr = BaseApplication.isLocalDmr;
  // // if (!this.isLocalDmr)
  // // {
  // // this.currentContentFormatMimeType =
  // localIntent.getStringExtra("currentContentFormatMimeType");
  // // this.metaData = localIntent.getStringExtra("metaData");
  // // this.dmcControl = new DMCControl(this, 1, this.dmrDeviceItem, this.upnpService,
  // this.mPlayUri, this.metaData);
  // // }
  // }



  /**
   * 点击Item的响应事件
   * 
   * */

  OnItemClickListener contentItemClickListener = new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      // Auto-generated method stub
      // ContentItem localItem =ImageDisplay.this.ImageList.get(arg2);

      String uri = ImageDisplay.this.mmImage.get(arg2).getStrPath();
      // if(localItem.isContainer())
      // {
      // ImageDisplay.this.upnpService.getControlPoint().execute(
      // new ContentBrowseActionCallback( ImageDisplay.this,
      // localItem.getService(), localItem
      // .getContainer(),
      // ImageDisplay.this.ImageList,
      // ImageDisplay.this.handler));
      //
      //
      //
      // }else{
      // Log.v("localContentItem",localItem.getItem()+"" );
      // List<Res> localList =localItem.getItem().getResources();
      // Res type = localList.get(0);
      // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      // String str=localMimeType.getType();

      Intent intent = new Intent();
      intent.setClass(ImageDisplay.this, BigBitMap.class);
      intent.putExtra("path", uri);
      // intent.putExtra("playURI", localItem.getItem().getFirstResource().getValue());
      intent.putExtra("ID", String.valueOf(arg2));
      // intent.putExtra("name", localItem.toString());
      //
      // intent.putExtra("currentContentFormatMimeType", localMimeType.toString());
      // try {
      // intent.putExtra("metaData",new GenerateXml().generate(localItem).toString());
      // } catch (Exception e) {
      // // Auto-generated catch block
      // e.printStackTrace();
      // }
      startActivity(intent);
      // }
    }
  };

//  private void getImageThumbnail(ArrayList<MediaImage> mImages, int width, int height, int kind) {
//    Bitmap mbitmap = null;
//    ArrayList<MediaImage> images = new ArrayList<MediaImage>();
//    images = mImages;
//    ContentResolver cr = getContentResolver();
//    // 获取视频的缩略图
//    for (int i = 0; i < images.size(); i++) {
//
//      mbitmap = ThumbnailUtils.createVideoThumbnail(images.get(i).getStrPath(), kind);
//      mbitmap =
//          ThumbnailUtils.extractThumbnail(mbitmap, width, height,
//              ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//
//      bitmap.add(mbitmap);
//    }
//    Log.v("bitmap", bitmap + "");
//    mhandler.sendEmptyMessage(1);
//    // System.out.println("w"+bitmap.getWidth());
//    // System.out.println("h"+bitmap.getHeight());
//    // String str = MovieActivity.this.convertIconToString(bitmap);
//
//  }

  /**
   * All rights reserved by uata.
   * @Title: lessenUriImage
   * @Description: 
   * @param path
   * @return  
   * @return: Bitmap
   * @throws 
   */
  public static final  Bitmap lessenUriImage(String path) {
    Options options = new Options();
    options.inJustDecodeBounds = true;
    Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
    options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = (int) (options.outHeight / (float) 320);
    if (be <= 0){
      be = 1;
    }
    options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
    bitmap = BitmapFactory.decodeFile(path, options);
    bitmap.getWidth();
    bitmap.getHeight();

    return bitmap;
  }



  /**
   * @Title: initView
   * @Description:  (初始化值和adapter)   *@param:     *@return: void
   * @throws 
   */

  private void initView() {
    this.marrImageList = ConfigData.arrlistcontent;
    this.mmImage = ConfigData.arrMeidaImage;
    // pre_but =(Button) findViewById(R.id.preButton);
    // next_but =(Button) findViewById(R.id.nextButton);
    // image=(ImageView) findViewById(R.id.imag);
    // pre_but.setOnClickListener(this);
    // next_but.setOnClickListener(this);
    Log.v(TAG, "initview is call");
    girdView = (GridView) findViewById(R.id.image_list);
    if (this.mmImage == null || this.marrImageList == null) {
      Toast.makeText(ImageDisplay.this, "还没有图片", 0).show();
    } else {

      this.adapterContent =
          new ContentAdapter(this.context, this.marrImageList, this.mmImage, this.bitmap);
      // ArrayAdapter<ContentItem> adapter=new
      // ArrayAdapter<ContentItem>(MenuFragment.this.getActivity(),R.layout.list_item,
      // this.mContentList);
      girdView.setAdapter(this.adapterContent);
    }
    girdView.setOnItemClickListener(contentItemClickListener);
    // list.setOnItemClickListener(contentItemClickListener);
  }


  /**
   * custom
   */

  // public void nextPic()
  // {
  //
  // if(this.mCurrentPosition >=-1+this.mListPhotos.size())
  // {
  // this.mCurrentPosition =(1+this.mCurrentPosition );
  //
  // Log.v(TAG, "nextPic--"+mCurrentPosition+"---"+"list--"+mListPhotos.size());
  //
  // // setImageFormat((ContentItem)this.mListPhotos.get(this.mCurrentPosition ));
  // this.path = ((ContentItem)this.mListPhotos.get(this.mCurrentPosition
  // )).getItem().getFirstResource().getValue();
  // Log.v(TAG, "next----"+path);
  //
  //
  //
  // }
  //
  // }
  //
  // public void prePic()
  // {
  //
  // if (this.mCurrentPosition >= 0)
  // {
  // this.mCurrentPosition = (-1 + this.mCurrentPosition );
  // // setImageFormat((ContentItem)this.mListPhotos.get(this.mCurrentPosition ));
  // this.path = ((ContentItem)this.mListPhotos.get(this.mCurrentPosition
  // )).getItem().getFirstResource().getValue();
  //
  // }
  // }

  public void setImageFormat(ContentItem paramContentItem) {
    if (this.isRemote) {
      MimeType localMimeType =
          ((Res) paramContentItem.getItem().getResources().get(0)).getProtocolInfo()
              .getContentFormatMimeType();
      if (localMimeType != null) {
        this.currentContentFormatMimeType = localMimeType.toString();

      }
    }
  }


  @Override
  public void onClick(View view) {

//    switch (view.getId()) {
//    // case R.id.preButton:
//    // prePic();
//    // break;
//    // case R.id.nextButton:
//    // nextPic();
//    // break;
//      default:
//        break;
//    }
  }

  /**
   * Initialization
   */
  public ArrayList<MediaImage> getLocalImage() {
    // this.mImages.clear();
    // String[] str={"_data", "_display_name", "duration"};
    ArrayList<MediaImage> mmImages = new ArrayList<MediaImage>();

    String[] str = new String[] {Media.DATA, Media._ID, Media.DISPLAY_NAME, Media.SIZE};
    ContentResolver cr = getContentResolver();
    Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, str, null, null, null);
    if (cursor == null || cursor.getCount() == 0) {
      Log.d("getLocalImage", "first return");
      return null;

    }

    if (cursor.moveToFirst()) {
      int id;
      int data;
      int nameID;
      int durationID;

      data = cursor.getColumnIndex(Media.DATA);
      id = cursor.getColumnIndex(Media._ID);
      nameID = cursor.getColumnIndex(Media.DISPLAY_NAME);
      durationID = cursor.getColumnIndex(Media.SIZE);

      do {
        // 把数据拿到以后存入MediaImage的数组中
        // MediaVideo localMediaVideo=new MediaVideo();
        // localMediaVideo.setVideoPath(cursor.getString(id));
        // localMediaVideo.setVideoName(cursor.getString(nameID));
        // localMediaVideo.setVideoDuration(cursor.getInt(durationID));
        // mVideos.add(localMediaVideo);

        MediaImage locaMediaImage = new MediaImage();
        locaMediaImage.setIntId(cursor.getInt(id));
        locaMediaImage.setStrPath(cursor.getString(data));;
        locaMediaImage.setStrName(cursor.getString(nameID));;
        locaMediaImage.setIntSize(cursor.getInt(durationID));;

        // Log.i("ImageID",cursor.getString(id) );
        // Log.i("ImageName",cursor.getString(nameID) );
        Log.i("ImageData", cursor.getString(data));
      } while (cursor.moveToNext());
      return mmImages;
    }
    cursor.close();

    return null;
  }

  private void initDate() {
    marrImageList = ConfigData.arrlistcontent;


    Log.v("Image", "ImageList----" + marrImageList);
    // ContentItem content = MovieList.get(0);
    // System.out.println(content.getContainer());
    // System.out.println(content.getService());

    // if (content.isContainer())
    // {
    //
    // MovieActivity.this.upnpService.getControlPoint().execute(
    // new ContentBrowseActionCallback(MovieActivity.this,
    // content.getService(), content
    // .getContainer(),
    // MovieActivity.this.MovieList,
    // MovieActivity.this.handler));
    //
    //
    //
    // }else{
    //
    // List<Res> localList =content.getItem().getResources();
    // Res type = localList.get(0);
    // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
    // String str=localMimeType.getType();
    //
    // if (str.equals("image"))
    // {
    //
    // System.out.println("image");
    // return;
    // }else{
    // System.out.println("video");
    // }
    //
    // }
  }


  class ContentAdapter extends BaseAdapter {

    private static final String TAG = "ContentAdapter";
    // private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Bitmap audioIcon;
    public int dmrPosition = 0;
    private Bitmap folderIcon;
    private Bitmap imageIcon;
    private Bitmap videoIcon;
    private String str1;
    private String str2;
    private ArrayList<MediaImage> aaImages = new ArrayList<MediaImage>();
    public ContentAdapter(Context context, ArrayList<ContentItem> arg2,
        ArrayList<MediaImage> mmImages, List<Bitmap> bitmap) {
      this.aaImages = mmImages;
      Log.v(TAG, mmImages + "");
      this.imageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
      this.videoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_video);
      this.audioIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_audio);
      this.folderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder);

    }

    @Override
    public int getCount() {
      // Auto-generated method stub
      return this.aaImages.size();
    }

    @Override
    public Object getItem(int position) {
      // Auto-generated method stub
      return this.aaImages.get(position);
    }

    @Override
    public long getItemId(int position) {
      // Auto-generated method stub
      return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      // Auto-generated method stub
      ViewHolder viewHolder = null;
      View convert=convertView;
      str1 = "file:/" + this.aaImages.get(position).getStrPath();
      if (convert == null) {
        convert = LayoutInflater.from(ImageDisplay.this).inflate(R.layout.image_item, null);
        viewHolder = new ViewHolder();
        viewHolder.tuImg = (ImageView) convert.findViewById(R.id.img_tu);
        str2 = this.aaImages.get(position).getStrPath();
        // viewHolder.tuText=(TextView) convertView.findViewById(R.id.tv_tu);

        // ContentItem localItem = this.mDeviceItems.get(position);
        // viewHolder.tuText.setText(localItem.toString());

        // viewHolder.tuImg.setImageBitmap(folderIcon);

        convert.setTag(viewHolder);
        // if(localItem.isContainer())
        // {
        // ImageDisplay.this.upnpService.getControlPoint().execute(
        // new ContentBrowseActionCallback(ImageDisplay.this,
        // localItem.getService(), localItem
        // .getContainer(),
        // ImageDisplay.this.ImageList,
        // ImageDisplay.this.handler));



        // }else{
        // Log.v("localContentItem",localItem.getItem()+"" );
        // List<Res> localList =localItem.getItem().getResources();
        // Res type = localList.get(0);
        // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
        // String str=localMimeType.getType();
        //
        // // str1=localItem.getItem().getFirstResource().getValue();
        // // System.out.println(str1);
        // Log.i("listview", "str1-----"+mImage.size());
        // if (str.equals("image"))
        // {
        //
        // str1=localItem.getItem().getFirstResource().getValue();
        //
        // Log.e(TAG, "video--"+str1);

        // }
        // }else
        // if(str.equals("video")){
        // str1=localItem.getItem().getFirstResource().getValue();
        // viewHolder.tuImg.setImageBitmap(videoIcon);
        // }else
        // {
        // str1=localItem.getItem().getFirstResource().getValue();
        // viewHolder.tuImg.setImageBitmap(audioIcon);
        // }
        // System.out.println(str1);
        // ImageAware imageAware = new ImageViewAware(viewHolder.tuImg, false);


        // }
      } else {


        // if(viewHolder.tuImg.getTag()==this.mImages.get(position).getPath())
        // {
        //
        // }else{
        viewHolder = (ViewHolder) convert.getTag();

        // }

      }
      viewHolder.tuImg.setImageBitmap(this.imageIcon);
      ImageAware imageAware = new ImageViewAware(viewHolder.tuImg, false);
      ImageDisplay.this.imageLoader.displayImage(str1, imageAware);
      // //会重复加载
      // ImageAware imageAware = new ImageViewAware(viewHolder.tuImg, false);
      // ImageDisplay.this.imageLoader.displayImage(str1,
      // imageAware, ImageDisplay.this.options,
      // this.animateFirstListener);



      return convert;
    }

  }

  private static class ViewHolder {
    private ImageView tuImg;
  }


  // public Bitmap getBitImage(String uri,boolean small)
  // {
  // if(uri != null) {
  // FileInputStream in = null;
  // try {
  // in = new FileInputStream(uri);
  // BitmapFactory.Options options = new BitmapFactory.Options();
  // //先制定原始大小
  // options.inSampleSize = 1;
  // //只进行大小判断
  // options.inJustDecodeBounds = true;
  // //调用此方法得到options得到图片的大小
  // BitmapFactory.decodeStream(in, null, options);
  // /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 **/
  // /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 **/
  // if(small){
  // options.inSampleSize = computeSampleSize(options, 200);
  // } else{
  // options.inSampleSize = computeSampleSize(options, 600);
  // }
  // // 我们得到了缩放比例，现在开始正式读入Bitmap数据
  // options.inJustDecodeBounds = false;
  // options.inDither = false;
  // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
  // // in = res.openInputStream(uri);
  // return BitmapFactory.decodeStream(in, null,options);
  // } catch (FileNotFoundException e) {
  //
  // }
  // }
  // return null;
  // }



  // private String getImage(String uri,boolean small)
  // {
  // if(uri != null) {
  // FileInputStream in = null;
  // try {
  // in = new FileInputStream(uri);
  // BitmapFactory.Options options = new BitmapFactory.Options();
  // //先制定原始大小
  // options.inSampleSize = 1;
  // //只进行大小判断
  // options.inJustDecodeBounds = true;
  // //调用此方法得到options得到图片的大小
  // BitmapFactory.decodeStream(in, null, options);
  // /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 **/
  // /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 **/
  // if(small){
  // options.inSampleSize = computeSampleSize(options, 200);
  // } else{
  // options.inSampleSize = computeSampleSize(options, 600);
  // }
  // // 我们得到了缩放比例，现在开始正式读入Bitmap数据
  // options.inJustDecodeBounds = false;
  // options.inDither = false;
  // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
  // // in = res.openInputStream(uri);
  // // return BitmapFactory.decodeStream(in, null,options);
  // return "file:/"+uri;
  // } catch (FileNotFoundException e) {
  //
  // }
  // }
  // return null;
  // }



  /**
   * 对图片进行合适的缩放
   * 
   * @param options
   * @param target
   * @return
   */
  public static int computeSampleSize(Options options, int target) {
    int width = options.outWidth;
    int hight = options.outHeight;
    int candidateW = width / target;
    int candidateH = hight / target;
    int candidate = Math.max(candidateW, candidateH);
    if (candidate == 0) {
      return 1;
    }
    if (candidate > 1) {
      if ((width > target) && (width / candidate) < target) {
        candidate -= 1;
      }
    }
    if (candidate > 1) {
      if ((hight > target) && (hight / candidate) < target) {
        candidate -= 1;
      }
    }
    return candidate;
  }

}
