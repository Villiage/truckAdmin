package com.fxlc.truckadmin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cyd on 2017/4/18.
 */

public class BitmapUtil {



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        Log.d("type", "mime:" + options.outMimeType);

        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 1;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.max(ratioH, ratioW);
        }
        return Integer.highestOneBit(inSampleSize);
    }

    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        String mimeType = options.outMimeType;
        Bitmap.CompressFormat format = null;
        if (mimeType.equals("image/jpeg")) {
            format = Bitmap.CompressFormat.JPEG;
        } else if (mimeType.equals("image/png")) {
            format = Bitmap.CompressFormat.PNG;
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 80, baos);
        byte[] buff = baos.toByteArray();
        Log.d(TAG, "bitsize:" + buff.length);

        return bitmap;
    }
  public static int ReqSize = 800;
  public synchronized static byte[] cpPicToByte(String filePath, int reqSize) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options,ReqSize , ReqSize);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        String mimeType = options.outMimeType;
        Bitmap.CompressFormat format = null;
        Log.d("mime",mimeType);

//        if (mimeType.equals("image/jpeg")) {
//            format = Bitmap.CompressFormat.JPEG;
//        } else if (mimeType.equals("image/png")) {
//            format = Bitmap.CompressFormat.PNG;
//        }else if (mimeType.equals("image/webp")){
//            format = Bitmap.CompressFormat.WEBP;
//        }

        format = Bitmap.CompressFormat.valueOf(mimeType.substring(mimeType.indexOf("/") + 1).toUpperCase());

        int q = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, q, baos);
        int size =  baos.toByteArray().length;
        while (size > reqSize * 1024){
            q-=10;
            baos.reset();
            bitmap.compress(format, q, baos);
            size = baos.toByteArray().length;
            Log.d(TAG, "bitsize:" + q + "--" + size );
            if (q  < 30) break;
        }

        return baos.toByteArray();
    }

    public static byte[] bitmapToByte(Bitmap bmp, Bitmap.CompressFormat format, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(format, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap.CompressFormat getMime(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        String mimeType = options.outMimeType;

        if (mimeType == null) return null;
        if (mimeType.equals("image/jpeg")) {
            return Bitmap.CompressFormat.JPEG;
        } else if (mimeType.equals("image/png")) {
            return Bitmap.CompressFormat.PNG;
        }


        return Bitmap.CompressFormat.JPEG;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    private static String TAG = "BtmapUtil";

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }
}














