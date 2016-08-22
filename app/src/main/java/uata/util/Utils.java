package uata.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.uata.MediaModel.MediaVideo;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:  Utils
 * @Description: 工具类
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:05:26 
 */
public class Utils {


  /**
   * @Title: getRealTime
   * @Description: 拿到远端dmr的进度时间   *@param: @param paramString  *@param: @return    *@return: int
   * @throws 
   */
  public static int getRealTime(String paramString) {
    int arr = paramString.indexOf(":");
    int time = 0;
    if (arr > 0) {
      String[] arrayOfString = paramString.split(":");
      time =
          Integer.parseInt(arrayOfString[2]) + 60 * Integer.parseInt(arrayOfString[1]) + 3600
              * Integer.parseInt(arrayOfString[0]);
    }
    return time;
  }


  /**
   * @Title: getDevName
   * @Description: 获取设备名称   *@param: @param paramString  *@param: @return    *@return: String
   * @throws 
   */
  public static String getDevName(String paramString) {
    String str = "";
    if ((paramString.contains("(")) && (paramString.contains(")"))) {
      str = paramString.substring(1 + paramString.indexOf("("), paramString.indexOf(")"));
    }
    return str;
  }


  /**
   * @Title: secToTime
   * @Description: 转换时间
   * @param: @param paramLong  
   * @param: @return    
   * @return: String
   * @throws 
   */
  public static String secToTime(long minTime) {
    long time = minTime;
  //  Long add = new  Long(time);
    Long add = Long.valueOf(time);
    int value = add.intValue();
//     new Long(paramLong).intValue();
    if (value <= 0) {
      return "00:00";
    }

    int min = value / 60;

    int i1 = value % 60;;
    // if (j < 60)
    // {
    // i1 = i % 60;
    // }
    int sec;
    int min1;
    int sec1;
    for (String str = "00:" + unitFormat(min) + ":" + unitFormat(i1);; str =
        unitFormat(sec) + ":" + unitFormat(min1) + ":" + unitFormat(sec1)) {
      sec = min / 60;
      if (sec > 99){
        return "99:59:59";
      }
      min1 = min % 60;
      sec1 = value - sec * 3600 - min1 * 60;
      return str;
    }
  }



  /**
   * @Title: getVideoThumbnail
   * @Description: 获取视频缩略图   *@param: @param mVideos  *@param: @param width  *@param: @param height
   *                         *@param: @param kind  *@param: @return    *@return: List<Bitmap>
   * @throws 
   */
  public List<Bitmap> getVideoThumbnail(ArrayList<MediaVideo> mVideos, int width, int height,
      int kind) {


    Bitmap mbitmap = null;
    ArrayList<MediaVideo> videos = new ArrayList<MediaVideo>();
    List<Bitmap> bitmap = new ArrayList<Bitmap>();
    videos = mVideos;
    // 如果手机没有视频 就会报空指针
    if (videos == null) {
     
    } else {
      // 获取视频的缩略图
      for (int i = 0; i < videos.size(); i++) {

        mbitmap = ThumbnailUtils.createVideoThumbnail(videos.get(i).getStrVideoPath(), kind);
        mbitmap =
            ThumbnailUtils.extractThumbnail(mbitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        bitmap.add(mbitmap);
      }

    }
    return bitmap;
  }

  // public static String format(long paramLong)
  // {
  // int i = 60 * 60;
  // long l1 = paramLong / i;
  // long l2 = (paramLong - l1 * i) / 60;
  // long l3 = paramLong - l1 * i - l2 * 60;
  // String str1;
  // String str2;
  // String str3;
  // if (l1 < 10L)
  // {
  // str1 = "0" + l1;
  // }
  // if (l2 >= 10L)
  // {
  // str2 = "0" + l2;
  // }
  // if (l3 >= 10L)
  // {
  // str3="0"+l3;
  // }
  // for (String str3 = "0" + l3; ; str3 = l3)
  // {
  // str1 = l1;
  // str2 = l2;
  // return str1 + ":" + str2 + ":" + str3;
  //
  // }
  // }

  /**
   * @Title: unitFormat
   * @Description:   *@param: @param paramInt  *@param: @return    *@return: String
   * @throws 
   */
  public static String unitFormat(int paramInt) {
    if ((paramInt >= 0) && (paramInt < 10)) {
      return "0" + Integer.toString(paramInt);
    }
    return Integer.toString(paramInt);

  }

}
