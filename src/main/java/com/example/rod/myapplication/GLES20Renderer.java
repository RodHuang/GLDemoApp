package com.example.rod.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by rod on 8/4/16.
 */

public class GLES20Renderer implements GLSurfaceView.Renderer {

    private final float[] mMatrixProjection = new float[16];
    private final float[] mMatrixView = new float[16];
    private final float[] mMatrixProjectionAndView = new float[16];
    private Context mContext;

    int satelliteCount = 15;
    Geometry[] spheres = new Geometry[satelliteCount];
    Geometry[] nodes = new Geometry[satelliteCount];
    Geometry sun;
    World world = new World();

    public GLES20Renderer(Context context) {
        mContext = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        ShaderUtils.setupShader();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        TextView text = new TextView(mContext);
        text.setText("Hello World!   ");
        text.setTextSize(30);
        text.setTextColor(Color.BLUE);
        text.setBackgroundColor(Color.RED);
        Bitmap b = loadBitmapFromView(text);
        TextureUtils.generateTexture(b);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        GLES20.glClearColor(0f, 0f, 0f, 0f);
        for (int i = 0; i < 16; i++) {
            mMatrixProjection[i] = 0.0f;
            mMatrixView[i] = 0.0f;
            mMatrixProjectionAndView[i] = 0.0f;
        }
        float ratio = (float) width / (float) height;
        Matrix.frustumM(mMatrixProjection, 0, -ratio, ratio, -1, 1, 1, 100);
//        Matrix.perspectiveM(mMatrixProjection, 0, 1f, ratio, 0, 1000);
        GLES20.glViewport(0, 0, width, height);
        Mesh.setLightPos(new float[]{-15f, 15f, 15f});

        Sphere s = new Sphere(1f, 48, 24);
        s.setAmbientColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
        s.setDiffuseColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
        s.setSpecColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
        s.setSpecPow(4);
        s.setTextureDiffuseLevel(.8f);
        s.setUseTexture(false);
        sun = new Geometry(s);
        world.add(sun);
        for (int i = 0; i < satelliteCount; i++) {
            Sphere c = new Sphere(1f, 12, 6);
            c.setAmbientColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
            c.setDiffuseColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
            c.setSpecColor(new float[]{(float) Math.random(), (float) Math.random(), (float) Math.random()});
            c.setSpecPow(8);
            c.setUseTexture(false);
            // test
            spheres[i] = new Geometry(c);
            nodes[i] = new Geometry(null);
            nodes[i].add(spheres[i]);
            world.add(nodes[i]);
        }

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mMatrixView, 0, 0, 0, 3f, 0, 0,
                0f, 0, 1, 0.0f);

        for (int i = 0; i < satelliteCount; i++) {
            int d = 1000 * (i + 1);
            float time = (float) (System.currentTimeMillis() % d) / (float) d;
            spheres[i].identityMatrixModel();
            nodes[i].identityMatrixModel();
            spheres[i].scale(.05f, .05f, .05f);
            spheres[i].translate(.06f * i * i + .5f, 0f, 0f);
            nodes[i].rotateY(30 * i);
            nodes[i].rotateY(360f * time);
            nodes[i].rotateZ(1f * i);
        }
        sun.identityMatrixModel();
        sun.scale(.2f, .2f, .2f);
        world.rotateX(45f);
        world.draw(mMatrixView, mMatrixProjection);
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
