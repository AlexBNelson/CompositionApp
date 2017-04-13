package com.example.alexnelson.compositionapplication;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.Dimension;
import android.support.annotation.Size;

/**
 * Created by Alex Nelson on 27/03/2017.
 */

public class AppFragment extends Fragment {
    //Used for e.g. onPause() method
    public static Bitmap persistentBM;//Bitmap which is kept and reapplied during configuration changes (e.g. orientation change). forms one half of save file
    int[][] persistentTracings;//Array of tracings used like persistentBM
    int[] persistentUI;//Array of UI data (e.g. what overlays are selected) for configuration changes
    public static boolean FILE_OPEN_FLAG=false;//True if a composition file has been opened or created
    public static ModSurfaceView mGLSurfaceView;
    public static Point canvasDimensions; //Point but refers to dimensions
    public static ActionStates touchState; //what happens when you perform touch actions on the canvas

    //ALTERNATIVES TO ActionStates ENUM

    //public static final int NO_RESPONSE=0;
    //public static final int ZOOM_0_DOWN=1;  //in Zoom state with 0 fingers down
    //public static final int ZOOM_1_DOWN=2;
    //public static final int ZOOM_2_DOWN=3;






}


