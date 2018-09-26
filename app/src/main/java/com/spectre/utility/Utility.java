package com.spectre.utility;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.rey.material.app.DialogFragment;
import com.spectre.R;
import com.spectre.activity.ChatActivity;
import com.spectre.activity.LoginActivity;
import com.spectre.activity.ViewCatalogImagesActivity;
import com.spectre.activity.ZoomActivity;
import com.spectre.beans.CarName;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.CustomPbSliderView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    private final static String VERSION = "V-2.1";

    public static Context appContext;
    private static String LIRA_TAXI_PREFERENCE = "SPECTRE_PREFERENCE";
    // private static int MAX_IMAGE_DIMENSION = 720;
    static List<String> listImage = new ArrayList<>();
    // 2018-02-24
    public static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void setLog(String logMessage) {
        if (logMessage != null) {
            if (AppConstants.IS_DEVELOPER_PREVIEW) {
                Log.e("**TAG**" + VERSION + "**", logMessage);
                Log.d("**TAG**", "----------------------------------------");
            }
        }
    }

    public static String getEditTextString(EditText editText) {
        if (editText != null) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static String getTextViewString(TextView textView) {
        if (textView != null) {
            return textView.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * Function is to check internet availability
     *
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    // for username string preferences
    /*public static void setSharedPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }*/

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public static String validateString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /*device id*/
    public static String getDeviceId(Context context) {
        try {
            return String.valueOf(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setSnackBar(Activity activity, String message) {
//        Snackbar snackbar = Snackbar.make(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0),
//                message, Snackbar.LENGTH_LONG);
//        View view = snackbar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//        view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//        view.setLayoutParams(params);
//        if (!snackbar.isShown())
//            snackbar.show();
        makeToast(activity, message);
    }

    public static void makeToast(Context context, String msg) {
        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        context.startService(new Intent(context, serviceClass));
        return false;
    }


    public static boolean setLocale(Context appContext, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //SharedPrefUtils.setPreference (appContext, Constant.SET_LANGUAGE, lang);
        return true;
    }


    // for username string preferences
    /*public static void setIntegerSharedPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }*/

    //Drawable
    /*public static void setDrawableSharedPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }*/

    /*public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        if (name.equalsIgnoreCase(Constant.USER_TOKEN))
            return settings.getString(name, "ef73781effc5774100f87fe2f437a435");
        else
            return settings.getString(name, "");
    }*/

    /*public static int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getInt(name, 0);
    }*/

    /*public static void setSharedPreferenceBoolean(Context context, String name, boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }*/

    /*public static String getLanguagePreference(Context context) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getString(Constant.LANGUAGE, "en");
    }*/

    /*public static void setLanguagePreference(Context context, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constant.LANGUAGE, value);
        editor.commit();
    }*/

    /*public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getBoolean(name, false);
    }*/

    /*public static boolean getSharedPreferencesBoolean_(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getBoolean(name, true);
    }*/

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

/*	public static Drawable getImageFromURL(String photoDomain) {
        Drawable drawable = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(photoDomain.trim());
			HttpResponse response = httpClient.execute(request);
			InputStream is = response.getEntity().getContent();
			drawable = Drawable.createFromStream (is, "src");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return drawable;
	}*/

/*	public static String postParamsAndfindJSON(String url,
                                               ArrayList<NameValuePair> params) {
		String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url+params);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout (httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout (httpParams, TIMEOUT_MILLISEC);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder ();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			result="";
			return result;
		}
		return result;
	}*/

	/*public static String multiPart(String url,MultipartEntity entity) {
        String result="";
		HttpClient httpclient;

		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			httppost.setEntity(entity);
			System.out.println("cvsc" + httppost);
			HttpResponse response = httpclient.execute(httppost);
			result = EntityUtils.toString(response.getEntity());
			System.out.println("Given Result to photo  " + result);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}*/


    /*public static Bitmap DownloadImageDirect(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        // String imageEncoded = Base64Coder.encodeTobase64(image);

        // Log.d("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static void alertBoxShow(Context context, int msg) {
        // set dialog for user's current location set for searching theaters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert");
        dialog.setMessage(msg);
        dialog.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void ShowStringAlertWithMessage(Context context, int alerttitle,
                                                  int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void ShowStringAlert2WithMessage(final Context context, int alerttitle,
                                                   int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("GET STARTED", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((HomeFormatActivity) context).moveToNextActivity("GET STARTED");
                //Intent intent=new Intent(context,GetStartedActivity.class);
            }
        });
        builder.setNegativeButton("FAQ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((HomeFormatActivity) context).moveToNextActivity("FAQ");
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public static boolean MobileValidator(String mobile) {
        Pattern pattern;
        Matcher matcher;
        final String MOBILE_PATTERN = "^[0-9]*$";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }*/

	/*public static String findJSONFromUrl(String url) {
        String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout (httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout (httpParams, TIMEOUT_MILLISEC);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder ();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			System.out.println("result  in jsonparser class ........" + result);

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			result=null;
		}
		return result;
	}*/

    /*public static Bitmap getBitmap(String url) {
        Bitmap imageBitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                imageBitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.out.println("exception in get bitma putility");
            }

            bis.close();
            is.close();
            final int IMAGE_MAX_SIZE = 50;
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                // b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = imageBitmap.getHeight();
                int width = imageBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) x, (int) y, true);
                imageBitmap.recycle();
                imageBitmap = scaledBitmap;

                System.gc();
            } else {
                // b = BitmapFactory.decodeStream(in);
            }

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.out.println("exception in get bitma putility");
        } catch (Exception e) {
            System.out.println("exception in get bitma putility");
            e.printStackTrace();
        }
        return imageBitmap;
    }*/

   /* public static void sendError(Context context,String stackTrace) {
        Intent intent=new Intent(context,CrashActivity.class);
        intent.putExtra("data",stackTrace);
        context.startActivity(intent);
    }*/

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
    }

    /*public static byte[] scaleImage(Context context, Uri photoUri)
            throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = 0;// getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION
                || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth)
                    / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight)
                    / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

		*//*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     *//*
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        *//*
     * if (type.equals("image/png")) {
     * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
     * (type.equals("image/jpg") || type.equals("image/jpeg")) {
     * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
     *//*
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }*/

    static int mMaxWidth, mMaxHeight;

    @SuppressWarnings("deprecation")
    /*public static Bitmap loadResizedImage(Context mContext, final File imageFile) {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mMaxWidth = display.getWidth();
        mMaxHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int scale = calculateInSampleSize(options, mMaxWidth, mMaxHeight);
        while (options.outWidth / scale > mMaxWidth
                || options.outHeight / scale > mMaxHeight) {
            scale++;
        }
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;
        if (scale > 1) {
            try {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPurgeable = true;
                options.inTempStorage = new byte[16 * 100];
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                        options);
                if (bitmap == null) {
                    return null;
                }

                // resize to desired dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double newWidth;
                double newHeight;
                if ((double) width / mMaxWidth < (double) height / mMaxHeight) {
                    newHeight = mMaxHeight;
                    newWidth = (newHeight / height) * width;
                } else {
                    newWidth = mMaxWidth;
                    newHeight = (newWidth / width) * height;
                }

                scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        Math.round((float) newWidth),
                        Math.round((float) newHeight), true);
                bitmap.recycle();
                bitmap = scaledBitmap;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }
            System.gc();
        } else {
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        return rotateImage(bitmap, imageFile);
    }*/

    public static Bitmap rotateImage(final Bitmap bitmap,
                                     final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        int newOrintation = 0;
        try {
            // orientation = getImageOrientation(fileWithExifInfo.getAbsolutePath());
            int orientation = 0;
            try {
                ExifInterface exif = new ExifInterface(fileWithExifInfo.getAbsolutePath());
                orientation = exif
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
         /*    if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }*/
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    newOrintation = 0;
                    return bitmap;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    newOrintation = 180;
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    newOrintation = 180;
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    newOrintation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    newOrintation = 90;
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    newOrintation = -90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    newOrintation = -90;
                    break;
                default:
                    return bitmap;
            }

            if (newOrintation != 0) {
                matrix.postRotate(newOrintation, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                // return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    /*public static int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }*/

    /*public static Typeface Appttf(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "AE100132.TTF");
    }*/

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    // remove for preferences

    /*public static void removepreference(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        settings.edit().remove(name).commit();
    }*/

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /*public static boolean validateLastName(String lastName) {
        return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
    }*/

    /* public static void ShowAlertDialog(String msg)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setTitle("SwipeMe");
            builder.setMessage(msg);
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

    // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    */
    /*public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    } */// end

    /**
     * Function to show settings alert dialog
     */
    /*public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }*/

    /*public static String getMonth(String month) {

        switch (month) {
            case "1":
                return "January";
            case "2":
                return "February";
            case "3":
                return "March";
            case "4":
                return "April";
            case "5":
                return "May";
            case "6":
                return "June";
            case "7":
                return "July";
            case "8":
                return "August";
            case "9":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return "January";
        }
    }*/

    /*public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/


  /*  public static void resetPreferences(Context appcontext) {
        appcontext.getSharedPreferences(Utility.LIRA_TAXI_PREFERENCE, Context.MODE_PRIVATE).edit().clear().commit();
        myPrefrence = new MyPrefrence(appcontext);
        //	myDetail = myPrefrence.getDetail();
        myPrefrence.resetLoginPreference();
        Intent logout = new Intent(appcontext, RegisterActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appcontext.startActivity(logout);
    }*/

    /*public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/

  /*  public static Drawable buildCounterDrawable(Context appContext, int count, int bgResourceId) {
        LayoutInflater inflater = LayoutInflater.from(appContext);
        View view = inflater.inflate(R.layout.layout_menu_counter, null);
        ImageView imgCounterBackground = (ImageView) view.findViewById(R.id.img_counter_background);
        imgCounterBackground.setImageResource(bgResourceId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.tv_count);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.tv_count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(((Activity) appContext).getResources(), bitmap);
    }*/
    public static void checkIfPermissionsGranted(final Context appContext) {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(appContext);
        alertDialogBuilder.setMessage(appContext.getString(R.string.permission, appContext.getString(R.string.app_name)));
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                goToSettings(appContext);
            }
        });

        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(appContext.getResources().getColor(R.color.colorAccent));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(appContext.getResources().getColor(R.color.colorAccent));
    }

    private static void goToSettings(Context appContext) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appContext.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(myAppSettings);
    }

    public static void setContentView(Context context, int layout) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        activity.setContentView(layout);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static ActionBar setUpToolbar_(Context appContext, String title, boolean home) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(home);
        actionBar.setHomeButtonEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        // ((CustomTextView) activity.findViewById(R.id.headet_text)).setText(title);
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return actionBar;
    }


    /*public static void setUpToolbar(Context appContext, String title, Spanned title2) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setTitle(title2);
        ((CustomTextView) activity.findViewById(R.id.toolbar_title)).setText(title);
        ((CustomTextView) activity.findViewById(R.id.toolbar_title)).setVisibility(View.GONE);

        //  actionBar.setTitle(title);
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }*/

    public static void contectDialog(String phoneNumber, final Activity activity){
        final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contect_seller);
        TextView tv_callNow = (TextView) dialog.findViewById(R.id.tv_callNow);
        TextView tv_chatNow = (TextView) dialog.findViewById(R.id.tv_chatNow);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final int REQUEST_PHONE_CALL = 1;
        final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        tv_callNow.setText("Call on "+phoneNumber);
        tv_chatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,ChatActivity.class));
            }
        });
        tv_callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    activity.startActivity(intent);

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp =  new WindowManager.LayoutParams();
        wlp.copyFrom(dialog.getWindow().getAttributes());
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static Toolbar setUpToolbarWithColor(final Context context, String title, int backgroundResource) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        Toolbar actionBarToolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(actionBarToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarToolbar.setBackgroundColor(backgroundResource);
        actionBarToolbar.setNavigationIcon(R.mipmap.back);
        //  actionBarToolbar.setNavigationContentDescription(title);
      /*  if (backgroundResource == context.getResources().getColor(R.color.colorWhite)) {
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.mipmap.back, null);
// "Enable" tinting process
            drawable = DrawableCompat.wrap(drawable);
// Tint it
            DrawableCompat.setTint(drawable, Color.GRAY);
// Set it as action bar icon
            actionBarToolbar.setNavigationIcon(R.mipmap.back);
        }*/
        actionBarToolbar.setTitle("");
        actionBarToolbar.setSubtitle("");
        CustomTextView tvTitle = (CustomTextView) actionBarToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(Html.fromHtml(title));
        actionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return actionBarToolbar;
    }

 /*   public static void setUpToolbarVerification(Context appContext, String title, Spanned title2) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        //actionBar.setTitle(title2);
        ((CustomTextView) activity.findViewById(R.id.headet_text)).setText(title);
        ((CustomTextView) activity.findViewById(R.id.header_text)).setVisibility(View.VISIBLE);
        ((CustomTextView) activity.findViewById(R.id.header_text)).setText(title2);

        //  actionBar.setTitle(title);
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        //  toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }*/

    /*public static void setUpToolbarHome(Context appContext, String title) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        ((CustomTextView) activity.findViewById(R.id.toolbar_title)).setText(title);

        //  actionBar.setTitle(title);
        // actionBar.setHomeAsUpIndicator(R.mipmap.menu);
    }*/


    public static void resetPreferences(Context appcontext) {
        appcontext.getSharedPreferences(Utility.LIRA_TAXI_PREFERENCE, Context.MODE_PRIVATE).edit().clear().commit();
        Intent logout = new Intent(appcontext, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appcontext.startActivity(logout);
    }


    /*public static String convertNumber(String numberString, String string) {
        if (numberString == null) {
            return "0";
        }

        if (numberString.equalsIgnoreCase("")) {
            return "0";
        }

        if (Integer.parseInt(numberString) <= 999) {
            return numberString;
        }
        BigInteger number = new BigInteger(numberString);
        //  System.out.println(number+" is "+createString(number)+" should be "+string);
        return createString(number);
    }*/


    private static final String NAMES[] = new String[]{
            "k",
            "M",
            "G",
            "T",
            "P",
            "E",
            "Z",
            "Y",
            /* "Octillion",
             "Nonillion",
             "Decillion",
             "Undecillion",
             "Duodecillion",
             "Tredecillion",
             "Quattuordecillion",
             "Quindecillion",
             "Sexdecillion",
             "Septendecillion",
             "Octodecillion",
             "Novemdecillion",
             "Vigintillion",*/
    };
    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final NavigableMap<BigInteger, String> MAP;

    static {
        MAP = new TreeMap<BigInteger, String>();
        for (int i = 0; i < NAMES.length; i++) {
            MAP.put(THOUSAND.pow(i + 1), NAMES[i]);
        }
    }

    /*public static String createString(BigInteger number) {
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null) {
            return "Nearly nothing";
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        float f = m.floatValue() / 1000.0f;
        float rounded = ((int) (f * 100.0)) / 100.0f;
        if (rounded % 1 == 0) {
            return ((int) rounded) + " " + entry.getValue();
        }
        return rounded + " " + entry.getValue();
    }*/

    /*public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Kartavya", null);
        return Uri.parse(path);
    }*/


    public static Bitmap getBitmapFromUrl(Context context, String myUrl) {
        URL url = null;
        try {
            if (myUrl != null && !myUrl.isEmpty()) {
                url = new URL(myUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap image = null;
        try {
            if (url != null) {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image == null) {
            image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        return image;
    }

    /*public static void ShareImage(Bitmap bm, Context context, String description) {
        //  String newString = description + "\n\n\nDownload Now:\nhttp://base3.engineerbabu.com:8282/BR_App/index/share.php/" + SharedPrefUtils.getPreference(appContext, Constant.REFERRAL_CODE);
        String newString = description + "\n\nFor the All latest update related to ATMC download our App \n\nFor the android App: " + "https://play.google.com/store/apps/details?id=com.ebabu.atmc";//\n\nFor the iOS App: https://itunes.apple.com/in/app/atmc/id1204240498?ls=1&mt=8";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, newString);
        // shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bm));
        ((Activity) context).startActivityForResult(Intent.createChooser(shareIntent, "Share with friends"), 123);
    }*/

    /*public static void ShareImage(Bitmap bm, Context context, String description) {
        //  String newString = description + "\n\n\nDownload Now:\nhttp://base3.engineerbabu.com:8282/BR_App/index/share.php/" + SharedPrefUtils.getPreference(appContext, Constant.REFERRAL_CODE);
        String newString = description + "\n\n\nDownload Now:\n" + Urls.BASE_URL1 + "/index/share.php/" + SharedPrefUtils.getPreference(context, Constant.REFERRAL_CODE);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image*//*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "kartavya");
        shareIntent.putExtra(Intent.EXTRA_TEXT, newString);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bm));
        ((Activity) context).startActivityForResult(Intent.createChooser(shareIntent, "Share with friends"), 123);
    }

    public static void setUpViewPager(SliderLayout sliderLayout, Context appContext, HashMap<String, String> url_from_api, String status) {

        sliderLayout.setDuration(5000);
        sliderLayout.getPagerIndicator().setDefaultIndicatorColor(appContext.getResources().getColor(R.color.colorPrimary), appContext.getResources().getColor(R.color.white));
        // findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);
        if (url_from_api.size() > 1) {
            for (String name : url_from_api.keySet()) {

                // TextSliderView textSliderView = new TextSliderView(this);
                // CustomSliderView textSliderView= new CustomSliderView(this);
                if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("")) {
                    CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
                    // initialize a SliderLayout
                    textSliderView
                            .image(name)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView);
                } else {
                    CustomUserSliderView textSliderView1 = new CustomUserSliderView(appContext);
                    textSliderView1
                            .image(name)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView1.bundle(new Bundle());
                    textSliderView1.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView1);
                }


            }

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        } else {
            for (String name : url_from_api.keySet()) {
                if (status.equalsIgnoreCase("0")) {
                    CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
                    // initialize a SliderLayout
                    textSliderView
                            .image(name)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView);
                } else {
                    CustomUserSliderView textSliderView1 = new CustomUserSliderView(appContext);
                    textSliderView1
                            .image(name)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView1.bundle(new Bundle());
                    textSliderView1.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView1);
                }

            }
            sliderLayout.stopAutoCycle();
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
        }
        sliderLayout.setDuration(5000);
    }


    public static void setUpViewPagerNew(SliderLayout sliderLayout, Context appContext) {


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.seniorcitizen);
        //  file_maps.put("2", R.drawable.default_image);
        // file_maps.put("3", R.drawable.default_image);

        for (String name : file_maps.keySet()) {
            CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "no");

            sliderLayout.addSlider(textSliderView);
            sliderLayout.stopAutoCycle();
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            // ((PagerIndicator)findViewById(R.id.custom_indicator)).setVisibility(View.GONE);
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
        }

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = ((Activity) context).getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public static Drawable buildCounterDrawable(Context appContext, int count, int bgResourceId) {
        LayoutInflater inflater = LayoutInflater.from(appContext);
        View view = inflater.inflate(R.layout.layout_menu_counter, null);
        ImageView imgCounterBackground = (ImageView) view.findViewById(R.id.img_counter_background);
        imgCounterBackground.setImageResource(bgResourceId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.tv_count);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.tv_count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(((Activity) appContext).getResources(), bitmap);
    }*/

    /*public static int initcolor(Context context) {
        // Context context = null;
        List<Integer> colors = new ArrayList<Integer>();
        // int[] colorsTxt = {Color.BLACK,Color.BLUE,Color.GREEN,Color.MAGENTA,Color.GREEN,Color.LTGRAY};
        String[] colorsTxt = {"#4dd0e2", "#ff9700", "#545cfd", "#ec407a", "#00c853", "#9575cd", "#64b5f6", "#26a69a", "#c0ca33"
                , "#8d6e63", "#4dd0e2", "#ff9700", "#545cfd", "#ec407a", "#00c853", "#9575cd", "#64b5f6", "#26a69a", "#c0ca33", "#8d6e63"};
        //int[] colorsTxt ={-16502410,-16097156};
        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            // int newColor = parseColor(colorsTxt[i]);
            colors.add(newColor);
        }
        int rand = new Random().nextInt(colors.size());
        Integer color = colors.get(rand);
        if (color == SharedPrefUtils.getPreference(context, "color", 0)) {
            color = colors.get(rand);
        }
        SharedPrefUtils.setPreference(context, "color", color);
        return color;
    }*/

    public static void openDialogToLogin(final Context context) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage("You need to login to perform this action.");
        builder.setTitle("Login");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Utils.logout(context);
            }
        });
        builder.create().show();
    }


    /* public static Drawable buildCounterDrawable(Context appContext, int count, int bgResourceId) {
         LayoutInflater inflater = LayoutInflater.from(appContext);
         View view = inflater.inflate(R.layout.layout_menu_counter, null);
         ImageView imgCounterBackground = (ImageView) view.findViewById(R.id.img_counter_background);
         imgCounterBackground.setImageResource(bgResourceId);

         if (count == 0) {
             View counterTextPanel = view.findViewById(R.id.tv_count);
             counterTextPanel.setVisibility(View.GONE);
         } else {
             TextView textView = (TextView) view.findViewById(R.id.tv_count);
             textView.setText("" + count);
         }

         view.measure(
                 View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                 View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
         view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

         view.setDrawingCacheEnabled(true);
         view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
         Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
         view.setDrawingCacheEnabled(false);

         return new BitmapDrawable(((Activity) appContext).getResources(), bitmap);
     }*/
    public static void openDialogToLogout(final Context context) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?");
        builder.setTitle("Logout");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout(context);
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Utils.logout(context);
            }
        });
        builder.create().show();
    }


    public static void logout(Context context) {
        try {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
            clearSharedPreferences(context);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //  AppPreference.getInstance(context).clear();
            context.startActivity(intent);
            ((Activity) context).finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void clearSharedPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = ((Activity) context).getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static CarName getCarName(Context context) {
        return new CarName("", (context.getString(R.string.select_car)));
    }
public static CarName getOtherName(Context context){
        return new CarName("","Other");
}
    public static ModelName getModelName(Context context) {
        return new ModelName("", (context.getString(R.string.select_model)));
    }

    public static VersionName getVersionName(Context context) {
        return new VersionName("", (context.getString(R.string.select_version)));
    }

    public static void openCalendarDialog(Context context, final TextView tvDob) {
        try {
            int mDay, mMonth, mYear, mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear;
            Calendar cal = Calendar.getInstance();
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);


            mMinDay = mDay;
            mMinMonth = mMonth;
            mMinYear = mYear;

            mMaxDay = mDay;
            mMaxMonth = mMonth;
            mMaxYear = mYear + 1;

//            if (dob != null && !dob.isEmpty()) {
//                try {
//                    Calendar dobCal = Calendar.getInstance();
//                    dobCal.setTime(completeTimestampFormat.parse(dob));
//                    mDay = dobCal.get(Calendar.DAY_OF_MONTH);
//                    mMonth = dobCal.get(Calendar.MONTH);
//                    mYear = dobCal.get(Calendar.YEAR);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }


            com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.DatePickerDialog.Builder(mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear, mDay, mMonth, mYear) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    com.rey.material.app.DatePickerDialog dialog = (com.rey.material.app.DatePickerDialog) fragment.getDialog();
                    //   mDob = dialog.getFormattedDate(displayDateFormat);

                    tvDob.setText(dialog.getFormattedDate(displayDateFormat));
                    fragment.dismiss();
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    fragment.dismiss();
                }
            };

            builder.positiveAction("OK")
                    .negativeAction("CANCEL").style(R.style.MyCalendarTheme);
            DialogFragment fragment = DialogFragment.newInstance(builder);


            fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openCalendarDialogEdit(Context context, final EditText tvDob) {
        try {
            int mDay, mMonth, mYear, mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear;
            Calendar cal = Calendar.getInstance();
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);


            mMinDay = mDay;
            mMinMonth = mMonth;
            mMinYear = mYear;

            mMaxDay = mDay;
            mMaxMonth = mMonth;
            mMaxYear = mYear + 1;

//            if (dob != null && !dob.isEmpty()) {
//                try {
//                    Calendar dobCal = Calendar.getInstance();
//                    dobCal.setTime(completeTimestampFormat.parse(dob));
//                    mDay = dobCal.get(Calendar.DAY_OF_MONTH);
//                    mMonth = dobCal.get(Calendar.MONTH);
//                    mYear = dobCal.get(Calendar.YEAR);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }


            com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.DatePickerDialog.Builder(mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear, mDay, mMonth, mYear) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    com.rey.material.app.DatePickerDialog dialog = (com.rey.material.app.DatePickerDialog) fragment.getDialog();
                    //   mDob = dialog.getFormattedDate(displayDateFormat);

                    tvDob.setText(dialog.getFormattedDate(displayDateFormat));
                    fragment.dismiss();
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    fragment.dismiss();
                }
            };

            builder.positiveAction("OK")
                    .negativeAction("CANCEL").style(R.style.MyCalendarTheme);
            DialogFragment fragment = DialogFragment.newInstance(builder);


            fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setUpViewPager(SliderLayout sliderLayout, final Context appContext, HashMap<String, String> url_from_api, String status) {

        sliderLayout.setDuration(5000);
        sliderLayout.getPagerIndicator().setDefaultIndicatorColor(appContext.getResources().getColor(R.color.colorPrimary), appContext.getResources().getColor(R.color.colorWhite));
        // findViewById(R.id.description_layout).setVisibility(View.INVISIBLE);

        listImage.clear();
        if (url_from_api.size() > 1) {
            for (final String name : url_from_api.keySet()) {
                listImage.add(name);
                // TextSliderView textSliderView = new TextSliderView(this);
                // CustomSliderView textSliderView= new CustomSliderView(this);
                if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("")) {
                    final CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
                    // initialize a SliderLayout
                    textSliderView
                            .image(name)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(appContext, ViewCatalogImagesActivity.class);
                                    intent.putStringArrayListExtra(Constant.IMAGE, (ArrayList<String>) listImage);
                                    intent.putExtra(Constant.POSITION, listImage.indexOf(name));
                                    appContext.startActivity(intent);

                                }
                            })
                            //    .empty(R.drawable.ic_launcher_web)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.Fit);


                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView);
                } else {
                    CustomPbSliderView textSliderView1 = new CustomPbSliderView(appContext);
                    textSliderView1
                            .image(name)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(appContext, ViewCatalogImagesActivity.class);
                                    intent.putExtra(Constant.IMAGE, name);
                                    intent.putExtra(Constant.POSITION, 0);
                                    appContext.startActivity(intent);
                                }
                            })
                            //  .empty(R.drawable.ic_launcher_web)
                            //   .empty(R.drawable.seniorcitizen)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView1.bundle(new Bundle());
                    textSliderView1.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView1);
                }


            }

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        } else {
            for (final String name : url_from_api.keySet()) {
                if (status.equalsIgnoreCase("0")) {
                    CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
                    // initialize a SliderLayout
                    textSliderView
                            .image(name)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(appContext, ZoomActivity.class);
                                    intent.putExtra(Constant.IMAGE, name);
                                    appContext.startActivity(intent);
                                }
                            })
                            // .empty(R.drawable.ic_launcher_web)
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView);
                } else {
                    CustomPbSliderView textSliderView1 = new CustomPbSliderView(appContext);
                    textSliderView1
                            .image(name)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(appContext, ZoomActivity.class);
                                    intent.putExtra(Constant.IMAGE, name);
                                    appContext.startActivity(intent);
                                }
                            })
                            // .empty(R.drawable.ic_launcher_web)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                    //  .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView1.bundle(new Bundle());
                    textSliderView1.getBundle()
                            .putString("extra", url_from_api.get(name));

                    sliderLayout.addSlider(textSliderView1);
                }

            }
            sliderLayout.stopAutoCycle();
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
        }
        sliderLayout.setDuration(5000);
    }


    public static void setUpViewPagerNew(SliderLayout sliderLayout, final Context appContext) {


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.ic_launcher_web);
        //  file_maps.put("2", R.drawable.default_image);
        // file_maps.put("3", R.drawable.default_image);

        for (final String name : file_maps.keySet()) {
            CustomPbSliderView textSliderView = new CustomPbSliderView(appContext);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(appContext, ZoomActivity.class);
                            intent.putExtra(Constant.IMAGE, name);
                            appContext.startActivity(intent);
                        }
                    })
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "no");

            sliderLayout.addSlider(textSliderView);
            sliderLayout.stopAutoCycle();
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            // ((PagerIndicator)findViewById(R.id.custom_indicator)).setVisibility(View.GONE);
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
        }

    }

    public static Drawable getDrawable(Context context, int type) {
        Drawable drawable = null;
        if (type == 1) {
            drawable = ContextCompat.getDrawable(context, R.mipmap.user);
        } else if (type == 2) {
            drawable = ContextCompat.getDrawable(context, R.mipmap.pin);
        } else if (type == 3) {
            drawable = ContextCompat.getDrawable(context, R.mipmap.contact);
        } else if (type == 4) {
            drawable = ContextCompat.getDrawable(context, R.mipmap.email);
        }

        if (drawable != null) {
            drawable.setColorFilter(Color.parseColor("#7400bc"), PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }

    public static boolean checkDate(String trim, String s) {
        String a = convertMili(trim);
        String b = convertMili(s);

        try {
            if ((a != null && !a.isEmpty()) && (b != null && !b.isEmpty())) {
                if (Long.parseLong(b) >= Long.parseLong(a)) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertMili(String format) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");


        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        //  String inputString = "00:01:30.500";

        Date date = null;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("in milliseconds: " + date.getTime());
        return String.valueOf(date.getTime());
    }

    /* [START] - toast message */
    private static int time;

    /**
     * display toast message in center
     *
     * @param message
     */
    public static void setToast(Context context, String message) {
        /*if (message.toString().trim().length() > 0) {
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }*/
        if (time == 0) {
            time = 10;
            Log.d("Tag", "time " + time);
        }
        if (time == 10) {
            Utility x = new Utility();
            x.new toastTime().execute();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private class toastTime extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            time = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (time >= 5)
                    return null;
                try {
                    Thread.sleep(500L);
                    time++;
                } catch (InterruptedException localInterruptedException) {
                    while (true)
                        localInterruptedException.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            time = 10;
        }
    }
    /* [END] - toast message */
}

