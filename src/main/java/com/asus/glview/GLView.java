package com.asus.glview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.asus.glview.geometry.World;

/**
 * Created by root on 8/12/16.
 */

public class GLView extends GLSurfaceView {
    private GLES20Renderer mRenderer;

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new GLES20Renderer();
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setZOrderOnTop(true);
        setRenderer(mRenderer);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public World getWorld() {
        return mRenderer.getWorld();
    }
}
