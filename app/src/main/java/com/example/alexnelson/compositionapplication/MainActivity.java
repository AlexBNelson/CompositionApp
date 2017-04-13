package com.example.alexnelson.compositionapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.example.alexnelson.compositionapplication.ActionStates.ZOOM_0_DOWN;

public class MainActivity extends AppCompatActivity {
    final int DRAW_LINE=11;
    final int PICK_IMAGE=5; //request code for activity request
    ImageView ImageView; //views and components are capitalised






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null && AppFragment.FILE_OPEN_FLAG==true) {
            setContentView(R.layout.activity_main);
            createOGLSurface(AppFragment.persistentBM);

        } else {
            setContentView(R.layout.activity_main);



            // Fullscreen mode
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
    super.onSaveInstanceState(outState);
    }



@Override
    public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;

}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_new:
                createNew();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void drawLine(){
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, DRAW_LINE);
    }


    public void createNew(){
        //creates new composition from image selected

        //MAY NEED TO FIND A BETTER WAY OF ORGANISING THESE REQUEST CODES
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Receives result from request to other app
        if (requestCode == DRAW_LINE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                AppFragment.touchState=ActionStates.DRAW_LINE;
            }
        };

        if (requestCode == PICK_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri imageUri=data.getData();

                    ContentResolver conRes = this.getContentResolver();
                try {
                    Bitmap temp=UtilityMain.getBitmapFromUri(imageUri, conRes);
                    AppFragment.persistentBM=temp;
                    createOGLSurface(temp);





                }
                catch(IOException e){
                    Log.e("error", "error");//ERROR HANDLING HERE-MODIFY
                }

            }
            else{

            }
        }}

        public void createOGLSurface(Bitmap b){
        // We create our Surfaceview for our OpenGL here
            //RelativeLayout rl = (RelativeLayout) findViewById(R.id.canvas);
            //AppFragment.canvasDimensions=new Point(rl.getWidth(), rl.getHeight());

            AppFragment.FILE_OPEN_FLAG=true;
            AppFragment.touchState=ZOOM_0_DOWN;

            AppFragment.mGLSurfaceView = new ModSurfaceView(this, AppFragment.persistentBM);
        // Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.canvas);

        // Attach our surface view to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(AppFragment.mGLSurfaceView, glParams);

    }
    }








