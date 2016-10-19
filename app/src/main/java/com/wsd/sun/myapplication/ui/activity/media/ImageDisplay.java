package com.wsd.sun.myapplication.ui.activity.media;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wsd.sun.myapplication.R;
import com.wsd.sun.myapplication.component.SmartImageView;
import com.wsd.sun.myapplication.model.MediaImage;
import com.wsd.sun.myapplication.ui.activity.BaseActivity;
import com.wsd.sun.myapplication.ui.activity.dmp.ContentItem;
import com.wsd.sun.myapplication.ui.activity.dmr.BigBitMap;
import com.wsd.sun.myapplication.utils.ConfigData;

import org.fourthline.cling.support.model.Res;
import org.seamless.util.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:  ImageDisplay
 * @Description:  本地图片的列表
 */
public class ImageDisplay extends BaseActivity implements OnClickListener {

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
  private ArrayList<MediaImage> mImage;
  private List<Bitmap> bitmap;

  private ArrayList<ContentItem> marrImageList = new ArrayList<ContentItem>();
  private ContentAdapter adapterContent;
  private Context context;

  protected ImageLoader imageLoader = ImageLoader.getInstance();
  private ArrayList<ContentItem> marrListPhotos = new ArrayList();







  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_display);

    initToolbar("图片", "推送", new OnToolbarMenuClickListener() {
      @Override
      public void onItemClick() {

      }
    });
    context=this;

    // initData();
    // System.out.println();
    mImage= getLocalImage();
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



  /**
   * 点击Item的响应事件
   * 
   * */

  OnItemClickListener contentItemClickListener = new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      // Auto-generated method stub
      // ContentItem localItem =ImageDisplay.this.ImageList.get(arg2);

      String uri = ImageDisplay.this.mImage.get(arg2).getStrPath();
      Intent intent = new Intent();
      intent.setClass(ImageDisplay.this, BigBitMap.class);
      intent.putExtra("path", uri);
      intent.putExtra("ID", String.valueOf(arg2));
      startActivity(intent);
    }
  };

  /**
   * @Title: initView
   * @Description:  (初始化值和adapter)   *@param:     *@return: void
   * @throws 
   */

  private void initView() {
    girdView = (GridView) findViewById(R.id.image_list);
      this.adapterContent = new ContentAdapter(this.context, this.marrImageList, getLocalImage(), this.bitmap);

      girdView.setAdapter(this.adapterContent);
    girdView.setOnItemClickListener(contentItemClickListener);

  }


  /**
   * custom
   */
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
        MediaImage locaMediaImage = new MediaImage();
        locaMediaImage.setIntId(cursor.getInt(id));
        locaMediaImage.setStrPath(cursor.getString(data));;
        locaMediaImage.setStrName(cursor.getString(nameID));;
        locaMediaImage.setIntSize(cursor.getInt(durationID));;

        // Log.i("ImageID",cursor.getString(id) );
        // Log.i("ImageName",cursor.getString(nameID) );
        Log.e("ImageData", cursor.getString(data));
        mmImages.add(locaMediaImage);
      } while (cursor.moveToNext());
      return mmImages;
    }
    cursor.close();

    return null;
  }

  private void initDate() {
    marrImageList = ConfigData.arrlistcontent;
    Log.v("Image", "ImageList----" + marrImageList);
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
        viewHolder.tuImg = (SmartImageView) convert.findViewById(R.id.img_tu);
        str2 = this.aaImages.get(position).getStrPath();
        Log.e("imgurl", str2);

//        Glide.with(mContext)
//                .load(str1)
//                .asBitmap()
//                .centerCrop()
//                .into(new SimpleTarget<Bitmap>() {
//                  @Override
//                  public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    viewHolder.tuImg.setImageBitmap(resource);
////                    mViewSwitcher.showNext();
////                    mAttacher = new PhotoViewAttacher(imgView);
////                    // Lets attach some listeners, not required though!
////                    mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
////                    mAttacher.setOnPhotoTapListener(new PhotoTapListener());
////                    mAttacher.setOnSingleFlingListener(new SingleFlingListener());
//                  }
//
//                  @Override
//                  public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                    super.onLoadFailed(e, errorDrawable);
//                //    CheckUtil.showShortMessage(mContext, ErrMsg);
//                    finish();
//                  }
//                });

      } else {
        viewHolder = (ViewHolder) convert.getTag();
      }

      Glide.with(context)
              .load(new File( str2 ) )
              .asBitmap()
              .into( viewHolder.tuImg);
      convert.setTag(viewHolder);
      return convert;
    }

  }

  private static class ViewHolder {
    private SmartImageView tuImg;
  }


}
