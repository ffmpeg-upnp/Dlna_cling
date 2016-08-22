package uata.LocalMedia;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.uata.MediaModel.MediaImage;

import java.util.ArrayList;

/**
 * @ClassName:  LocalImage
 * @Description: (获取本地的图片)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:30:02 
 */
public class LocalImage {

  private Context context;
  private ArrayList<MediaImage> mmImages = new ArrayList<MediaImage>();



  // private LocalImage (Context paramContext)
  // {
  // this.mContext = paramContext;
  // }

  
  /**
   * All rights reserved by uata.
   * @Title: getLocalImage
   * @Description:  通过ContentResolver 来获取视频的缩略图
   * @param paramContext
   * @return  
   * @return: ArrayList<MediaImage>
   * @throws 
   */
  public ArrayList<MediaImage> getLocalImage(Context context) {
    this.mmImages.clear();
    // String[] str={"_data", "_display_name", "duration"};
    String[] str = new String[] {Media.DATA, Media._ID, Media.DISPLAY_NAME, Media.SIZE};
    ContentResolver cr = context.getContentResolver();
    Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, str, null, null, null);
    if (cursor == null || cursor.getCount() == 0) {
      Log.d("getLocalImage", "first return");
      return null;

    }

    if (cursor.moveToFirst()) {
      int id;
      int data;
      int nameId;
      int durationId;

      data = cursor.getColumnIndex(Media.DATA);
      id = cursor.getColumnIndex(Media._ID);
      nameId = cursor.getColumnIndex(Media.DISPLAY_NAME);
      durationId = cursor.getColumnIndex(Media.SIZE);

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
        locaMediaImage.setStrName(cursor.getString(nameId));;
        locaMediaImage.setIntSize(cursor.getInt(durationId));;
        mmImages.add(locaMediaImage);

        // Log.i("ImageID",cursor.getString(id) );
        // Log.i("ImageName",cursor.getString(nameID) );
      } while (cursor.moveToNext());
      return mmImages;
    }
    cursor.close();
    return null;

  }
}
