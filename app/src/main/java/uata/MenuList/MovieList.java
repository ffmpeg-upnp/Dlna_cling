package uata.MenuList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.uata.MediaModel.MediaVideo;
import com.uata.application.BaseApplication;
import com.uata.application.ConfigData;
import com.uata.dlna_application.R;
import com.uata.dmp.ContentItem;
import com.uata.dmp.DeviceItem;
import com.uata.dmr.GPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName:  MovieActivity
 * @Description: 本地视频
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:37:07 
 */
public class MovieList extends Activity {
  private ArrayList<ContentItem> arrMovieList = new ArrayList<ContentItem>();
  private ContentAdapter madapterContent;
  public ArrayList<DeviceItem> arrMovieDevice;
  private ImageView img;
  private Context context;
  protected ImageLoader imageLoader = ImageLoader.getInstance();
  private GridView list;
  private File path = Environment.getExternalStorageDirectory();
  // private List<VideoInfo> mList;
  private List<Bitmap> bitmap;
  private ArrayList<MediaVideo> marrVideos;
  public static final  String TAG = "MovieActivity";
  private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0:
//          System.out.println("success");
          break;
        case 1:
          Log.v("bitmap", bitmap + "");
          // Log.v("mList", mList+"");
          initView();
          break;
        default:
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.movie);


    Log.v("path", path + "");
    // new ScanningThrread(path, mCallBack).start();
    bitmap = new ArrayList<Bitmap>();
    // mList = new ArrayList<VideoInfo>();
   initDate();
    // System.out.println( getLocalVideo());
    // getVideoThumbnail(getLocalVideo(),200,100,Thumbnails.MINI_KIND);
    initView();


  }

  private void localVideo(Context paramContext) {
    this.context = paramContext;
  }

  // private ArrayList<MediaVideo> getLocalVideo()
  // {
  // // this.mVideos.clear();
  // ArrayList<MediaVideo> video=new ArrayList<MediaVideo>();
  // String[] str={"_data", "_display_name", "duration"};
  // ContentResolver cr = getContentResolver();
  // Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
  // if(cursor==null||cursor.getCount()==0)
  // {
  // Log.d("getLocalVideo", "first return");
  // return null;
  //
  // }
  //
  // if(cursor.moveToFirst())
  // {
  // int id;
  // int nameID;
  // int durationID;
  // id= cursor.getColumnIndex("_data");
  // nameID= cursor.getColumnIndex("_display_name");
  // durationID= cursor.getColumnIndex("duration");
  //
  // do{
  // //把数据拿到以后存入MediaVideo的数组中
  // MediaVideo localMediaVideo=new MediaVideo();
  // localMediaVideo.setVideoPath(cursor.getString(id));
  // localMediaVideo.setVideoName(cursor.getString(nameID));
  // localMediaVideo.setVideoDuration(cursor.getInt(durationID));
  // Log.i("movieID",cursor.getString(id) );
  // Log.i("movieName",cursor.getString(nameID) );
  // video.add(localMediaVideo);
  //
  //
  // }while(cursor.moveToNext());
  // return video;
  // }
  // cursor.close();
  // return null;
  // }


  private void initView() {
    this.bitmap = ConfigData.bitMap;
    this.marrVideos = ConfigData.arrMediaVideo;
    Log.e(TAG, "图片为--" + this.bitmap);
    Log.e(TAG, "视频为--" + this.marrVideos);
    list = (GridView) findViewById(R.id.gridView1);

    if (this.marrVideos == null) {
      Toast.makeText(MovieList.this, "还没有视频", 0).show();
    } else {


      this.madapterContent =
          new ContentAdapter(this.context, this.arrMovieList, this.marrVideos, imageLoader, this.bitmap);
      // ArrayAdapter<ContentItem> adapter=new
      // ArrayAdapter<ContentItem>(MenuFragment.this.getActivity(),R.layout.list_item,
      // this.mContentList);
      list.setAdapter(this.madapterContent);
      list.setOnItemClickListener(contentItemClickListener);
    }
  }

  OnItemClickListener contentItemClickListener = new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      // Auto-generated method stub

      String localPath = MovieList.this.marrVideos.get(arg2).getStrVideoPath();
      // ContentItem localItem =MovieActivity.this.MovieList.get(arg2);
      //
      // if(localItem.isContainer())
      // {
      // MovieActivity.this.upnpService.getControlPoint().execute(
      // new ContentBrowseActionCallback(MovieActivity.this,
      // localItem.getService(), localItem
      // .getContainer(),
      // MovieActivity.this.MovieList,
      // MovieActivity.this.handler));
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
      Intent intent = new Intent();
      intent.setClass(MovieList.this, GPlayer.class);
      intent.putExtra("localURL", localPath);
      intent.putExtra("videoID", String.valueOf(arg2));
      // intent.putExtra("playURI", localItem.getItem().getFirstResource().getValue());
      // intent.putExtra("ID", String.valueOf(arg2));
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

  private void initDate() {

    this.marrVideos = ConfigData.arrMediaVideo;
    Log.e(TAG, "视频的资源为" + this.marrVideos);
    arrMovieList = ConfigData.arrlistcontent;
    this.arrMovieDevice = BaseApplication.dmrDevice;
    img = (ImageView) findViewById(R.id.video_img);
//    System.out.println(arrMovieList);
//    System.out.println(this.arrMovieDevice);
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

  private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
      // Auto-generated method stub
      super.onLoadingCancelled(imageUri, view);
    }

    // 监听图片异步加载
    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      super.onLoadingComplete(imageUri, view, loadedImage);
      final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean isFirstDisplay = !displayedImages.contains(imageUri);
        if (isFirstDisplay) {
          // 图片的淡入效果
          FadeInBitmapDisplayer.animate(imageView, 500);
          displayedImages.add(imageUri);
//          System.out.println("===> loading " + imageUri);
        }
      }
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

  /**
   * All rights reserved by uata.
   * @Title: convertIconToString
   * @Description: 无用
   * @param bitmap
   * @return  
   * @return: String
   * @throws 
   */
  public static String convertIconToString(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
    bitmap.compress(CompressFormat.PNG, 100, baos);
    byte[] appicon = baos.toByteArray();// 转为byte数组
    return Base64.encodeToString(appicon, Base64.DEFAULT);

  }

  private void getVideoThumbnail(ArrayList<MediaVideo> avideos, int width, int height, int kind) {
    Bitmap mbitmap = null;
    ArrayList<MediaVideo> videos = new ArrayList<MediaVideo>();
    videos = avideos;
    // 获取视频的缩略图
    for (int i = 0; i < videos.size(); i++) {
      // 如果手机没有视频 就会报空指针
      if (videos.get(i).getStrVideoPath() == null) {
        Toast.makeText(MovieList.this, "没有视频", 0).show();
      } else {

        mbitmap = ThumbnailUtils.createVideoThumbnail(videos.get(i).getStrVideoPath(), kind);
        mbitmap =
            ThumbnailUtils.extractThumbnail(mbitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        bitmap.add(mbitmap);
      }
    }
    // Log.v("bitmap", bitmap+"");
    handler.sendEmptyMessage(1);
    // System.out.println("w"+bitmap.getWidth());
    // System.out.println("h"+bitmap.getHeight());
    // String str = MovieActivity.this.convertIconToString(bitmap);

  }



  class ContentAdapter extends BaseAdapter {

    public int dmrPosition = 0;
    private Bitmap videoIcon;
    private String str1;
    private ImageLoadingListener animateFirstListener;
    private DisplayImageOptions options;
    private List<Bitmap> llistBitmaps;
    private ArrayList<MediaVideo> videos;


    String path;

    public ContentAdapter(Context context, ArrayList<ContentItem> arg2,
        ArrayList<MediaVideo> avideos, ImageLoader imageLoader, List<Bitmap> alistBitmap) {
      this.videos = avideos;
      // this.imageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
      this.videoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_video);
      // this.audioIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_audio);
      // this.folderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder);

      this.options =
          new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty)
              .showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true).cacheOnDisc(true)
              .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
              .bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300))
              .build();
      animateFirstListener = new AnimateFirstDisplayListener();
      llistBitmaps = alistBitmap;
      // Log.v("ContentAdapter", this.VideoList+"");
      // Log.v("ContentAdapter", this.mBitmaps+"");

    }

    @Override
    public int getCount() {
      // Auto-generated method stub
      // return mDeviceItems.size();
      return videos.size();
    }

    @Override
    public Object getItem(int position) {
      // Auto-generated method stub
      // return mDeviceItems.get(position);
      return videos.get(position);
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
      str1 = "file:/" + videos.get(position).getStrVideoPath();
      View convert=convertView;
      if (convert == null) {
        convert= LayoutInflater.from(MovieList.this).inflate(R.layout.mvideo_item, null);
        viewHolder = new ViewHolder();
        viewHolder.tuImg = (ImageView) convert.findViewById(R.id.img_tu);
        // viewHolder.tuText=(TextView) convertView.findViewById(R.id.tv_tu);
        convert.setTag(viewHolder);
        // ContentItem localItem = this.mDeviceItems.get(position);
        // viewHolder.tuText.setText(localItem.toString());
        // viewHolder.tuText.setText(videos.get(position).getVideoName());
        // viewHolder.tuImg.setImageBitmap(videoIcon);
        // Log.v("bitmap", mBitmaps+"");
        viewHolder.tuImg.setImageBitmap(llistBitmaps.get(position));
        // String str2 = Environment.getExternalStorageDirectory().getPath() ;
        // str1 = videos.get(position).getVideoPath();

        // Log.e(TAG, "videos---"+str1);
        // Log.v(TAG, "path---"+str2);
        // if(localItem.isContainer())
        // {
        // MovieActivity.this.upnpService.getControlPoint().execute(
        // new ContentBrowseActionCallback(MovieActivity.this,
        // localItem.getService(), localItem
        // .getContainer(),
        // MovieActivity.this.MovieList,
        // MovieActivity.this.handler));
        //
        //
        //
        // }else{
        // Log.v("localContentItem",localItem.getItem()+"" );
        // List<Res> localList =localItem.getItem().getResources();
        // Res type = localList.get(0);
        // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
        // String str=localMimeType.getType();
        // // int i = localItem.getItem().getProperties().size();
        // // System.out.println(i);
        // // Property app = localItem.getItem().getProperties().get(i);
        // // String ic_image = app.getValue().toString();
        // // Log.v(ic_image, ic_image);
        // // Log.v(ic_image, ic_image);
        // // Log.v(ic_image, ic_image);
        // Log.v("Itemposition", "position=="+position);
        //
        // // ID=String.valueOf(position);
        // // name=localItem.toString();
        // // playURL=localItem.getItem().getFirstResource().getValue();
        // // currentContentFormatMimeType=localMimeType.toString();
        // // try {
        // // metaData=new GenerateXml().generate(localItem).toString();
        // // } catch (Exception e) {
        // // // Auto-generated catch block
        // // e.printStackTrace();
        // // }
        //
        //
        //
        //
        //
        // if(str.equals("video")){
        // str1=localItem.getItem().getFirstResource().getValue();
        // // viewHolder.tuImg.setImageBitmap(videoIcon);
        // // for (int i = 0; i < mDeviceItems.size(); i++) {
        // // Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(str1, 30);
        // // mBitmaps.add(bitmap);
        // // System.out.println(mBitmaps);
        // // }
        // // Bitmap bit = MovieActivity.this.getVideoThumbnail(str1, 100, 80, 40);
        // // System.out.println(bit);
        // //viewHolder.tuImg.setImageBitmap();
        // }
        // // }else
        // // {
        // // str1=localItem.getItem().getFirstResource().getValue();
        // // viewHolder.tuImg.setImageBitmap(audioIcon);
        // // }
        // // System.out.println(str1);
        // }
        // mImageLoader.displayImage(str1,
        // viewHolder.tuImg, options,
        // this.animateFirstListener);
      } else {
        // localMediaVideo = (MediaVideo)this.videos.get(position);
        // path = localMediaVideo.getVideoPath();
        viewHolder = (ViewHolder) convert.getTag();

      }
      // Log.d("listview", "str1-----"+str1);

      // for(int i=0;i<this.videos.size();i++)
      // {
      // ImageAware imageAware= new ImageViewAware(viewHolder.tuImg, false);
      // mImageLoader.displayImage(str1,
      // imageAware);
      // }
      // viewHolder.tuText.setText(mList.get(position).getDisplayName());
      //
      // // viewHolder.tuImg.setImageBitmap(videoIcon);
      // Log.v("bitmap", mList.get(position).getDisplayName()+"");
      // viewHolder.tuImg.setImageBitmap(mBitmaps.get(position));

      return convert;
    }

  }


  private static class ViewHolder {
    private ImageView tuImg;
  }

}
