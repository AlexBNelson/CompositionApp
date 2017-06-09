package com.example.alexnelson.compositionapplication;

import android.util.Log;
import android.view.ScaleGestureDetector;

/**
 * Created by Alex Nelson on 11/05/2017.
 */

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener  {
    public boolean onScale(ScaleGestureDetector detector) {
        AppFragment.scaleFactor *= detector.getScaleFactor();

        // Don't let the object get too small or too large.
        AppFragment.scaleFactor = Math.max(1f, Math.min(AppFragment.scaleFactor, 10.0f));

        AppFragment.xFocus=detector.getFocusX();
        AppFragment.yFocus=detector.getFocusY();

        AppFragment.HAS_BEEN_MAGNIFIED=true;

        AppFragment.mGLSurfaceView.requestRender();



        return true;
    }
}
