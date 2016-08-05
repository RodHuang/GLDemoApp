package com.example.rod.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by rod on 8/4/16.
 */

public class GLES20Renderer implements GLSurfaceView.Renderer {

    private final float[] mMatrixProjection = new float[16];
    private final float[] mMatrixView = new float[16];
    private final float[] mMatrixProjectionAndView = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        ShaderUtils.setupShader();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        GLES20.glClearColor(0f, 0f, 0f, 1f);
        for (int i = 0; i < 16; i++) {
            mMatrixProjection[i] = 0.0f;
            mMatrixView[i] = 0.0f;
            mMatrixProjectionAndView[i] = 0.0f;
        }
        float ratio = (float) width / (float) height;
        Matrix.frustumM(mMatrixProjection, 0, -ratio, ratio, -1, 1, 1, 100);
//        Matrix.perspectiveM(mMatrixProjection, 0, 1f, ratio, 0, 1000);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mMatrixView, 0, 0, 0, 1f, 0, 0,
                0f, 0, 1, 0.0f);
        Matrix.multiplyMM(mMatrixProjectionAndView, 0, mMatrixProjection, 0,
                mMatrixView, 0);
        Triangle t = new Triangle();
        t.setColors(new float[]{1f ,0f, 0f, 1f, 0f, 1f, 0f, 1f, 0f, 0f, 1f, 1f});
        Plane p = new Plane(1, 1, 1, 1);
        p.setColors(new float[]{1f ,0f, 0f, 1f, 0f, 1f, 0f, 1f, 0f, 0f, 1f, 1f, 1f, 1f, 1f, 1f});
//        p.rotateZ(45f);
//        p.scale(2f, .5f, 1f);
//        p.translate(.5f, .5f, 0f);
//        p.rotateZ(10f);
        p.draw(mMatrixProjectionAndView);
        t.draw(mMatrixProjectionAndView);
    }
}
