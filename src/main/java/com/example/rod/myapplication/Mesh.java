package com.example.rod.myapplication;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by rod on 8/4/16.
 */

public abstract class Mesh {

    // Our vertex buffer.
    private FloatBuffer verticesBuffer = null;

    // Our index buffer.
    private ShortBuffer indicesBuffer = null;

    // The number of indices.
    private int numOfIndices = -1;

    // Flat Color
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    // Smooth Colors
    private FloatBuffer colorBuffer = null;

    public void draw(float[] m) {
        // Counter-clockwise winding.
        GLES20.glFrontFace(GLES20.GL_CCW);
        // Enable face culling.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // What faces to remove with the face culling.
        GLES20.glCullFace(GLES20.GL_BACK);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        GLES20.glVertexAttribPointer(ShaderUtils.sPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, verticesBuffer);
//        GLES20.glVertexAttribPointer(ShaderUtils.sTexCoordLoc, 2, GLES20.GL_FLOAT, false,
//                0, colorBuffer);
        if (colorBuffer == null) {
            float[] colors = new float[numOfIndices * 4];
            for (int i = 0; i < numOfIndices; i++) {
                for (int j = 0; j < 4; j++) {
                    colors[i * 4 + j] = rgba[j];
                }
            }
            setColors(colors);
        }
        GLES20.glVertexAttribPointer(ShaderUtils.sColorHandle, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glUniformMatrix4fv(ShaderUtils.sMatrixhandle, 1, false, m, 0);
        GLES20.glUniform1i(ShaderUtils.sSamplerLoc, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numOfIndices,
                GLES20.GL_UNSIGNED_SHORT, indicesBuffer);
        // Disable face culling.
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    protected void setVertices(float[] vertices) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        for (int i = 0; i < vertices.length; i++) {
            Log.d("Rod", vertices[i] + "");
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    protected void setIndices(short[] indices) {
        for (int i = 0; i < indices.length; i++) {
            Log.d("Rod", indices[i] + "");
        }
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        numOfIndices = indices.length;
    }

    protected void setColor(float red, float green, float blue, float alpha) {
        // Setting the flat color.
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = alpha;
    }

    protected void setColors(float[] colors) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

}
