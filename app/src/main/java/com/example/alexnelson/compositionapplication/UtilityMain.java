package com.example.alexnelson.compositionapplication;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex Nelson on 19/03/2017.
 */

public class UtilityMain {

    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver conRes) throws FileNotFoundException  {
        //Separate method needed to handle obtaining of image from uri because exceptions need to be handled
        InputStream input = conRes.openInputStream(uri);


        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }
}
