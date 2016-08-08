package com.example.rod.myapplication;

import android.opengl.GLES20;
import android.opengl.Matrix;
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
    // Normal buffer
    private FloatBuffer normalBuffer = null;

    // The number of indices.
    private int numOfIndices = -1;
    private int numOfVertices = -1;

    // Flat Color
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    // Smooth Colors
    private FloatBuffer colorBuffer = null;

    private static FloatBuffer lightBuffer = null;


    private float[] transformM = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,1f, 0f, 0f, 0f, 0f, 1f};

    public void draw(float[] matrixView, float[] matrixProjection) {
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
        GLES20.glVertexAttribPointer(ShaderUtils.sNormalHandle, 3, GLES20.GL_FLOAT,
                false, 0, normalBuffer);
        if (colorBuffer == null) {
            float[] colors = new float[numOfVertices * 4];
            for (int i = 0; i < numOfVertices; i++) {
                for (int j = 0; j < 4; j++) {
                    colors[i * 4 + j] = rgba[j];
                }
            }
            setColors(colors);
        }
        GLES20.glVertexAttribPointer(ShaderUtils.sColorHandle, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);
        GLES20.glUniform3fv(ShaderUtils.sLightPosHandle, 1, lightBuffer);
        GLES20.glUniformMatrix4fv(ShaderUtils.sMVMatrixHandle, 1, false, transformM, 0);
        Matrix.multiplyMM(transformM, 0, matrixView, 0, transformM, 0);
        Matrix.multiplyMM(transformM, 0, matrixProjection, 0, transformM, 0);
        GLES20.glUniformMatrix4fv(ShaderUtils.sMVPMatrixHandle, 1, false, transformM, 0);
        Matrix.setIdentityM(transformM, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numOfIndices,
                GLES20.GL_UNSIGNED_SHORT, indicesBuffer);
        // Disable face culling.
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    protected void setVertices(float[] vertices) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        for (int i = 0; i < vertices.length; i++) {
            Log.d("Rod", vertices[i] + " ");
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
        numOfVertices = vertices.length;
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

    protected void setNormals(float[] normals) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        normalBuffer = cbb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);
    }

    public static void setLightPos(float[] lightPos) {
        Log.d("Rod", "setLightPos");
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(lightPos.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        lightBuffer = cbb.asFloatBuffer();
        lightBuffer.put(lightPos);
        lightBuffer.position(0);
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(transformM, 0, x, y, z);
    }

    public void rotateX(float angle) {
        Matrix.rotateM(transformM, 0, angle, 1f, 0f, 0f);
    }

    public void rotateY(float angle) {
        Matrix.rotateM(transformM, 0, angle, 0f, 1f, 0f);
    }

    public void rotateZ(float angle) {
        Matrix.rotateM(transformM, 0, angle, 0f, 0f, 1f);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(transformM, 0, x, y, z);
    }

}
