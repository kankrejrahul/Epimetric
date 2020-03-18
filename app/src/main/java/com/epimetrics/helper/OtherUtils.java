package com.epimetrics.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class OtherUtils
{
    public static boolean isNetworkAvailable(Context context)
    {
        boolean g ;
        g = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        return g;
    }

    public static void showMessage(Context context, String message)
    {
        Toast.makeText(context ,message, Toast.LENGTH_LONG).show();
    }

    public static int getDisplayHeight(Context context)
    {
        return ((context.getResources().getDisplayMetrics().heightPixels));
    }

    public static int getDisplayWidth(Context context)
    {
        return ((context.getResources().getDisplayMetrics().widthPixels));
    }

    public static boolean isEmailValid(String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean validateName( String Name )
    {
        String firstNameRegex = "^[a-zA-Z]+(([\\'\\,\\.\\-][a-zA-Z])?[a-zA-Z]*)*$";
        return Name.matches(firstNameRegex);
    }

    public static int getRandomNumber()//get nom in range of 0,1,2,3....4
    {
        Random r = new Random();
        return (r.nextInt(5 - 0) + 0);
    }

    private static final String ALLOWED_CHARACTERS ="0123456789";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static boolean isDeviceBuildVersionMarshmallow()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isDeviceBuildVersionN()
    {
        return Build.VERSION.SDK_INT>= Build.VERSION_CODES.N;
    }

    public static boolean isDeviceBuildVersionOreo26()
    {
        return Build.VERSION.SDK_INT>= Build.VERSION_CODES.O;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getNewPath(Context context, Uri uri) {
        String strImagePath = null;
        boolean isImageFromGoogleDrive = false;

        if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                strImagePath = Environment.getExternalStorageDirectory() + "/" + split[1];
            } else {
                Pattern DIR_SEPORATOR = Pattern.compile("/");
                Set<String> rv = new HashSet<>();
                String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                    if (TextUtils.isEmpty(rawExternalStorage)) {
                        rv.add("/storage/sdcard0");
                    } else {
                        rv.add(rawExternalStorage);
                    }
                } else {
                    String rawUserId;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        rawUserId = "";
                    } else {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String[] folders = DIR_SEPORATOR.split(path);
                        String lastFolder = folders[folders.length - 1];
                        boolean isDigit = false;
                        try {
                            Integer.valueOf(lastFolder);
                            isDigit = true;
                        } catch (NumberFormatException ignored) {
                        }
                        rawUserId = isDigit ? lastFolder : "";
                    }
                    if (TextUtils.isEmpty(rawUserId)) {
                        rv.add(rawEmulatedStorageTarget);
                    } else {
                        rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                    }
                }
                if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                    String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                    Collections.addAll(rv, rawSecondaryStorages);
                }
                String[] temp = rv.toArray(new String[rv.size()]);

                for (int i = 0; i < temp.length; i++) {
                    File tempf = new File(temp[i] + "/" + split[1]);
                    if (tempf.exists()) {
                        strImagePath = temp[i] + "/" + split[1];
                    }
                }
            }
        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
            String id = DocumentsContract.getDocumentId(uri);
            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            Cursor cursor = null;
            String column = "_data";
            String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    strImagePath = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            String selection = "_id=?";
            String[] selectionArgs = new String[]{split[1]};
            Cursor cursor = null;
            String column = "_data";
            String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(column);
                    strImagePath = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
            isImageFromGoogleDrive = true;
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            String column = "_data";
            String[] projection = {column};

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(column);
                    strImagePath = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            strImagePath = uri.getPath();
        }


        if (isImageFromGoogleDrive) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
                //  iv.setImageBitmap(bitmap);
                // tvImagePath.setText(getResources().getString(R.string.str_image_google_drive));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File f = new File(strImagePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            // Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bmOptions);
            //  iv.setImageBitmap(bitmap);
            //  tvImagePath.setText(getResources().getString(R.string.str_image_path) + ": " + strImagePath);
        }
        return strImagePath;
    }

    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public static void setEditTextFont(Context context,EditText field)
    {
        String FontName="font/AvenirNextLTPro-Regular.otf";
        Typeface textViewTypeface = Typeface.createFromAsset(context.getAssets(), FontName);
        field.setTypeface(textViewTypeface);
    }


    public static Bitmap decodeWithSampleing(File f)
    {
        try
        {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp / 2 < 600 || height_tmp / 2 < 450)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    public static Bitmap getThumbnail(Bitmap targetBitmap,int THUMBNAIL_SIZE)
    {
        return ThumbnailUtils.extractThumbnail(targetBitmap, THUMBNAIL_SIZE,THUMBNAIL_SIZE);
    }


/*    public boolean getSimVerificationComplete(Context context,DataHandler dataHandler)
    {
        if (Build.VERSION.SDK_INT >= 22)
        {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0)
            {
                return false;
            }

            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex3 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(2);
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex4 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(3);
            SimUtils simUtils = new SimUtils();
            String simInfo = simUtils.getSimInfo(activeSubscriptionInfoForSimSlotIndex, activeSubscriptionInfoForSimSlotIndex2, activeSubscriptionInfoForSimSlotIndex3, activeSubscriptionInfoForSimSlotIndex4);
            String data = dataHandler.getData();
            if (TextUtils.isEmpty(data) || !simUtils.equalsSimInfo(data, simInfo))
            {
                dataHandler.addData(simInfo);
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0)
            {
                Log.d("processSimInfo","android.permission.READ_PHONE_STATE"+telephonyManager.getSimSerialNumber());

                String simSerialNumber = telephonyManager.getSimSerialNumber();

                SimUtils simUtils = new SimUtils();
                String data = dataHandler.getData();
                if (TextUtils.isEmpty(data) || !simUtils.equalsSimInfo(data, simSerialNumber))
                {
                    dataHandler.addData(simSerialNumber);
                    return false;
                }
                else
                {
                    Log.d("processSimInfo","validateSimInfo()s");
                    return true;
                }
            }
        }
        return false;
    }*/

// HashMyPassword -- Send a Password as a string
// Make sure to put this in a try - catch block
    public static String HashMyPassword(String passPhraseOrPin) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int length = passPhraseOrPin.length();
        int iHashCode = 0;
        for (int iCntChars = 0; iCntChars < length; iCntChars++)
        {
            if (iCntChars < (length - 1))
                iHashCode += (passPhraseOrPin.charAt(iCntChars) * (iCntChars + 1)) + (passPhraseOrPin.charAt(iCntChars + 1) * (iCntChars + 2));
            else
            iHashCode += (passPhraseOrPin.charAt(iCntChars) * (iCntChars + 1)) + passPhraseOrPin.charAt(0);
        }

    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
        final int iterations = 1000;

    // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1And8BIT");
        KeySpec keySpec = new PBEKeySpec(Integer.toString(iHashCode, 3).toCharArray(), "eaa8e3ab-6823-4914-88c1-5affe2a7a751".getBytes(), iterations, outputKeyLength);
        byte[] baKey = secretKeyFactory.generateSecret(keySpec).getEncoded();

        StringBuilder sb = new StringBuilder(baKey.length * 2);
        for (byte b : baKey)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    // CreateAPIEncyption_Key_IV -- Send a LayoutXML as a string
    // Make sure to put this in a try - catch block
    public static String CreateAPIEncyption_Key_IV(String strLayoutXML) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int length = strLayoutXML.length();
        int iHashCode = 0;
        for (int iCntChars = 0; iCntChars < length; iCntChars++)
        {
            if (iCntChars < (length - 1))
                iHashCode += (strLayoutXML.charAt(iCntChars) * (iCntChars + 1)) + (strLayoutXML.charAt(iCntChars + 1) * (iCntChars + 2));
            else
                iHashCode += (strLayoutXML.charAt(iCntChars) * (iCntChars + 1)) + strLayoutXML.charAt(0);
        }

        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1And8BIT");
        KeySpec keySpec = new PBEKeySpec(Integer.toString(iHashCode, 3).toCharArray(), "f56ca0a3-aec9-4744-bec6-aa2c63094a7f".getBytes(), iterations, outputKeyLength);
        byte[] baKey = secretKeyFactory.generateSecret(keySpec).getEncoded();

        StringBuilder sb = new StringBuilder(baKey.length * 2);
        for (byte b : baKey)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    public static String CreateAPIChecksum(String key, String data) throws Exception
    {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Base64.encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")), Base64.NO_WRAP);
    }

    public static String bin2hex(byte[] in) {
        StringBuilder sb = new StringBuilder(in.length * 2);
        for (byte b : in) {
            sb.append(
                    forDigit((b & 0xF0) >> 4)
            ).append(
                    forDigit(b & 0xF)
            );
        }
        return sb.toString();
    }

    public static char forDigit(int digit) {
        if (digit < 10) {
            return (char) ('0' + digit);
        }
        return (char) ('A' - 10 + digit);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String get12HourFormat(String time)
    {
        String formattedTime = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj;
            dateObj = sdf.parse(time);
            formattedTime = new SimpleDateFormat("h:mm aa").format(dateObj);
        }
        catch (final ParseException e)
        {
            e.printStackTrace();
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static JSONObject addDataToObject(String id, String value)
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("ElementID",id);
            object.put("ElementValue",value);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return object;
    }


    public static String parseDate(int inputType,String dates)
    {
        String inputPattern = "";

        switch (inputType)
        {
            case 1:
                inputPattern = "dd/MM/yyyy";
                break;
        }

        Log.d("inputPattern","inputPattern: "+inputPattern+" time: "+dates);

        String outputPattern = "d MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try
        {
            date = inputFormat.parse(dates);
            str = outputFormat.format(date);
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }
        return str;
    }


}