package com.example.alexnelson.compositionapplication;


import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.support.v4.view.MotionEventCompat.ACTION_POINTER_DOWN;
import static android.support.v4.view.MotionEventCompat.getActionIndex;
import static android.support.v4.view.MotionEventCompat.getX;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.example.alexnelson.compositionapplication.ActionStates.DRAW_LINE;
import static com.example.alexnelson.compositionapplication.ActionStates.ZOOM_0_DOWN;
import static com.example.alexnelson.compositionapplication.ActionStates.ZOOM_1_DOWN;
import static com.example.alexnelson.compositionapplication.ActionStates.ZOOM_2_DOWN;

/**
 * Created by Alex Nelson on 21/03/2017.
 */

public class ModGLRenderer implements GLSurfaceView.Renderer {


    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];
    private Bitmap mImage;
    // Our screen resolution
    float   mScreenWidth;
    float   mScreenHeight;

    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;

    public int mImageHeight;
    public int mImageWidth;
    public static float vertices[];
    public static short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;//Byte buffer to store vertices in native order
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    //Fields for even handling-----------------------
    final int INVALID_POINTER_ID = -1;
    int firstPointerID=INVALID_POINTER_ID; //ID of first pointer down  // value is -1 so that exception is thrown if this value isnt changed
    int secondPointerID=INVALID_POINTER_ID;
    //-----------------------------------------------






    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        mImage=AppFragment.persistentBM;
        mImageWidth=AppFragment.persistentBM.getWidth();
        mImageHeight=AppFragment.persistentBM.getHeight();
    }

    public void processTouchEvent(MotionEvent e){

        switch(AppFragment.touchState){//MAYBE HAVE A METHOD FOR EACH CASE-START WITH THE CODE IN THE CAE STATEMENTS
            case NO_RESPONSE: break; //no touch response, e.g. when no file is open

            case ZOOM_0_DOWN: if(e.getActionMasked()==ACTION_DOWN){
                AppFragment.touchState=ZOOM_1_DOWN;
                int index = MotionEventCompat.getActionIndex(e);
                e.getX();
                e.getY();
                firstPointerID=e.getPointerId(index);
                break;
            }
                else{break;}//zooms (default)

            case ZOOM_1_DOWN: if(e.getActionMasked()==ACTION_MOVE) { //pan
                int index = MotionEventCompat.getActionIndex(e);
                e.getX();
                e.getY();
                break;
            }
                else if(e.getActionMasked()==ACTION_POINTER_DOWN&&e.getPointerCount()==2){ //go into zoom mode // also means there cannot be more than 2 pointers
                    AppFragment.touchState=ZOOM_2_DOWN;
                    secondPointerID=e.getPointerId(MotionEventCompat.getActionIndex(e));
                break;
            } else if (e.getActionMasked() == ACTION_UP){
                firstPointerID=INVALID_POINTER_ID;
                AppFragment.touchState=ZOOM_0_DOWN;
            }


            case ZOOM_2_DOWN: if(e.getActionMasked()==ACTION_MOVE){
                //pointer ID remains constant, index not necessarily
                int index = MotionEventCompat.getActionIndex(e);

                float X1;
                float Y1;
                float X2;
                float Y2;

                if(e.getPointerId(index)==firstPointerID){ //if the pointer that is moved is the first one
                    X1=e.getX(index);
                    Y1=e.getY(index);
                    X2=e.getX(e.findPointerIndex(secondPointerID));
                    Y2=e.getY(e.findPointerIndex(secondPointerID));
                }
                else{
                    X1=e.getX(index);
                    Y1=e.getY(index);
                    X2=e.getX(e.findPointerIndex(firstPointerID));
                    Y2=e.getY(e.findPointerIndex(firstPointerID));
                }



                try{
                e.findPointerIndex(firstPointerID);}
                catch(Exception n){
                    Log.e(" ERROR", "first pointer not initialized to real index");
                }

                break;

            }
                else if(e.getActionMasked()==ACTION_UP){

                    if(firstPointerID==e.getPointerId(MotionEventCompat.getActionIndex(e))){
                    firstPointerID=INVALID_POINTER_ID;
                    }
                    else if(secondPointerID==e.getPointerId(MotionEventCompat.getActionIndex(e)))
                    AppFragment.touchState=ZOOM_1_DOWN;
            }


            case DRAW_LINE:    {



            }


        }

    }

    public void drawImage(){
        //draws base image
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        //mScreenHeight=AppFragment.canvasDimensions.y;
        //mScreenWidth=AppFragment.canvasDimensions.x;

//if ratio of the image height over the image width is greater than that of the screen
        if(((100*mImageHeight)/mImageWidth)>(100*mScreenHeight/mScreenWidth)){ // *100 because dividing the floats results in 0 if less than 1
            vertices=new float[]{
                    0f, mScreenHeight, 0.0f,
                    0f, 0.0f,0.0f,
                    ((mImageWidth*mScreenHeight)/mImageHeight), 0.0f, 0.0f,
                    ((mImageWidth*mScreenHeight)/mImageHeight), mScreenHeight, 0.0f



            };

        }else{
            vertices=new float[]{
                    0f, mScreenHeight-350, 0.0f,//WILL NEED TO GET RID OF MAGIC NUMBER
                    0f, (mScreenHeight-(mImageHeight*mScreenWidth)/mImageWidth)-350,0.0f,
                    mScreenWidth, (mScreenHeight-(mImageHeight*mScreenWidth)/mImageWidth)-350, 0.0f,
                    mScreenWidth, mScreenHeight-350, 0.0f

            };


            Log.i("INFO", Float.toString((100*mImageHeight)/mImageWidth));
            }

        SetupTriangle();

        // Create the image information
        SetupImage();
        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        int vertexShader = UtilityGraphics.loadShader(GLES20.GL_VERTEX_SHADER, UtilityGraphics.vs_SolidColor);
        int fragmentShader = UtilityGraphics.loadShader(GLES20.GL_FRAGMENT_SHADER, UtilityGraphics.fs_SolidColor);

        UtilityGraphics.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(UtilityGraphics.sp_SolidColor, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(UtilityGraphics.sp_SolidColor, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(UtilityGraphics.sp_SolidColor);                  // creates OpenGL ES program executables

        // Set our shader program
        GLES20.glUseProgram(UtilityGraphics.sp_SolidColor);

        // Create the shaders, images
        vertexShader = UtilityGraphics.loadShader(GLES20.GL_VERTEX_SHADER,
                UtilityGraphics.vs_Image);
        fragmentShader = UtilityGraphics.loadShader(GLES20.GL_FRAGMENT_SHADER,
                UtilityGraphics.fs_Image);

        UtilityGraphics.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(UtilityGraphics.sp_Image, vertexShader);
        GLES20.glAttachShader(UtilityGraphics.sp_Image, fragmentShader);
        GLES20.glLinkProgram(UtilityGraphics.sp_Image);

        // Set our shader programm
        GLES20.glUseProgram(UtilityGraphics.sp_Image);
        Render(mtrxProjectionAndView);
        //mImageHeight*(768/mImageWidth)
    }

    public void onDrawFrame(GL10 unused) {


            drawImage();
            // Create the triangle


    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        Log.i("info",  String.valueOf(mScreenHeight));
        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);


    }



    public void SetupImage()
    {
        // Create our UV coordinates.
        uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);


        // Temporary create a bitmap
        Bitmap bmp = mImage;
        try{
        }
        catch(NullPointerException e){
            Log.e("error", "no image selected");
        }

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        //bmp.recycle();

    }

    public void SetupTriangle()
    {
        // We have create the vertices of our view.

        indices = new short[] {0, 1, 2, 0, 2, 3}; // loop in the android official tutorial opengles why different order.

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

    }

    private void Render(float[] m) {

        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(UtilityGraphics.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(UtilityGraphics.sp_Image,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(UtilityGraphics.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (UtilityGraphics.sp_Image,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }


}
