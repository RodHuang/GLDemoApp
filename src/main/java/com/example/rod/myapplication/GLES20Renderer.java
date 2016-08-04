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
        Plane p = new Plane(200,200,1,1);
        p.draw(mMatrixProjectionAndView);
    }
}
