package uata.dmp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * @ClassName:  AsncImageLoader
 * @Description: (图片大小处理)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午3:03:05 
 */
public class AsncImageLoader {
  private static final String TAG = "AsncImageLoader";


  public Bitmap fetchBitmap(String paramString) {
    try {
      Bitmap localBitmap =
          getBitmapFromStream((InputStream) new URL(paramString).getContent(), 48, 48);
      return localBitmap;
    } catch (IOException localIoexception) {
      return null;
    } catch (Exception localException) {}
    return null;
  }

  public Bitmap getBitmapFromStream(InputStream input, int paramInt, int paramInt2) {
    Bitmap localObject = null;
    BitmapFactory.Options localOptions = null;
    if (input != null) {
      localOptions = null;
      if (paramInt > 0) {
        localOptions = null;
        if (paramInt2 > 0) {
          localOptions = new BitmapFactory.Options();
          localOptions.inJustDecodeBounds = true;// 确保图片不加载到内存
          localOptions.inSampleSize =
              computeSampleSize(localOptions, Math.min(paramInt, paramInt2), paramInt * paramInt2);
          localOptions.inJustDecodeBounds = false;
          localOptions.inInputShareable = true;
          localOptions.inPurgeable = true;
        }
      }
    }
    try {
      Bitmap localBitmap = BitmapFactory.decodeStream(input, null, localOptions);
      localObject = localBitmap;
      return localObject;
    } catch (Exception e) {
      // : handle exception
    }
    return null;
  }

  public static int computeSampleSize(BitmapFactory.Options option, int minSide, int maxNum) {
    int initialSize = computeInitialSampleSize(option, minSide, maxNum);

    int roundedSize;
    if (initialSize <= 8) {
      roundedSize = 1;
      while (roundedSize < initialSize) {
        roundedSize <<= 1;
      }

    } else {
      roundedSize = (initialSize + 7) / 8 * 8;
    }
    return roundedSize;
  }


  private static int computeInitialSampleSize(BitmapFactory.Options Options, int minSide, int maxNum) {
    double width = Options.outWidth;
    double height = Options.outHeight;
    int lowerBound = (maxNum == -1) ? 1 : (int) Math.ceil(Math.sqrt(width * height / maxNum));
    int upperBound =
        (minSide == -1) ? 128 : (int) Math.min(Math.floor(width / minSide),
            Math.floor(Math.floor(height / maxNum)));

    if (upperBound < lowerBound) {
      return lowerBound;
    }
    if ((maxNum == -1) && (minSide == -1)) {
      return 1;
    } else if (minSide == -1) {
      return lowerBound;
    } else {
      return upperBound;
    }
  }

  public abstract static  interface ImageCallback {
    public abstract void imageLoaded(Bitmap paramBitmap, DeviceItem paramDeviceItem);
  }

}
