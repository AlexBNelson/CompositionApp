package com.example.alexnelson.compositionapplication;

import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alex Nelson on 21/03/2017.
 */

public class UtilityGraphics {
    //Utility class for graphics
    // Program variables
    public static int sp_SolidColor;
    public static int sp_Image;

    /* SHADER Solid
     *
     * This shader is for rendering a colored primitive.
     *
     */
    public static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";

    public static final String vs_SolidColor =
            "uniform    mat4        uMVPMatrix;" +
                    "attribute  vec4        vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static final String fs_SolidColor =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(0.5,0,0,1);" +
                    "}";

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }


    //the skew is the value added to a vector position to account for magnification level

    public static float leftSkew(float imageWidth){
        return AppFragment.xFocus-(imageWidth*AppFragment.scaleFactor/2);
    }

    public static float rightSkew(float imageWidth){
        return AppFragment.xFocus+(imageWidth*AppFragment.scaleFactor/2);
    }

    public static float topSkew(float imageHeight){
        return AppFragment.yFocus-(imageHeight*AppFragment.scaleFactor/2);
    }

    public static float bottomSkew(float imageWidth){
        return AppFragment.yFocus+(imageWidth*AppFragment.scaleFactor/2);
    }
    /*public static ArrayList zoomCentreOut(float x1, float x2, float y1, float y2){ // returns centre of zoom
        double x;
        double y;

        if(x2>x1){
            x=(x1+((x2-x1)/2));
        }
        else{
            x=(x2+((x1-x2)/2));
        }

        if(y2>y1){
            y=(y1+((y2-y1)/2));
        }
        else{
            y=(y2+((y1-y2)/2));
        }

        ArrayList a=new ArrayList();
        //Log.i("x", String.valueOf(y));
        //Log.i("width", String.valueOf(ModGLRenderer.vectorY));
        a.add((float)x/ModGLRenderer.vectorX); // get coordinate between 0 and 1
        a.add((float)y/ModGLRenderer.vectorY);
        return a;
    }*/

    /*public static float zoomMagOut(float x1, float x2, float y1, float y2){
        float x;
        float y;

        if(x2>x1){
            x=x2-x1;
        }
        else{
            x=x1-x2;
        }

        if(y2>y1){
            y=y2-y1;
        }
        else{
            y=y1-y2;
        }

            return (float)Math.sqrt((x*x)+(y*y));
    }*/



    /*public static int[] lineVertex(int[] coords){

    return {0f, mScreenHeight, 0.0f,
                0f, 0.0f,0.0f,
                ((mImageWidth*mScreenHeight)/mImageHeight), 0.0f, 0.0f,
                ((mImageWidth*mScreenHeight)/mImageHeight), mScreenHeight, 0.0f};
    }*/
}
