package com.example.alexnelson.compositionapplication;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.view.ScaleGestureDetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.R.attr.bitmap;

/**
 * Created by Alex Nelson on 19/03/2017.
 */

public class UtilityMain {

    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver conRes) throws FileNotFoundException, IOException  {
        //Separate method needed to handle obtaining of image from uri because exceptions need to be handled
        InputStream input = conRes.openInputStream(uri);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling

        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);

            input.close();


        return bitmap;
    }


    public static int loadTexture(final Context context, final Bitmap bitmap)
            //loads texture from bitmap into OpenGL
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }



   // private boolean hasGLES20() {
       // ActivityManager am = (ActivityManager)
       //         getSystemService(Context.ACTIVITY_SERVICE);
     //   ConfigurationInfo info = am.getDeviceConfigurationInfo();
      //  return info.reqGlEsVersion >= 0x20000;
  //  }
}
