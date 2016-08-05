package com.example.rod.myapplication;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by rod on 8/4/16.
 */

public class ShaderUtils {
    public static final String TAG = "ShaderUtils";

    public static int sShaderProgram;

    public static int sPositionHandle;
    public static int sTexCoordLoc;
    public static int sColorHandle;
    public static int sMatrixhandle;
    public static int sSamplerLoc;

    private static final String _vertexShader =
                    "uniform mat4 u_MVPMatrix;        \n"     // A constant representing the combined model/view/projection matrix.

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";    // normalized screen coordinates.
    private static final String _fragmentShader =
                    "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d(TAG, "Could not compile shader " + type + ":");
            Log.d(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        // return the shader
        return shader;
    }


    public static void initShaderHandles() {

        sPositionHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Position");
//        sTexCoordLoc = GLES20.glGetAttribLocation(sShaderProgram,
//                "a_texCoord");
        sColorHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Color");
        sMatrixhandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_MVPMatrix");
//        sSamplerLoc = GLES20.glGetUniformLocation(sShaderProgram,
//                "s_texture");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glEnableVertexAttribArray(sColorHandle);
    }

    public static void setupShader() {
        cleanup();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                _vertexShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                _fragmentShader);

        // Shader for original scene
        sShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(sShaderProgram, vertexShader);
        GLES20.glAttachShader(sShaderProgram, fragmentShader);
        GLES20.glLinkProgram(sShaderProgram);
        GLES20.glUseProgram(sShaderProgram);

        initShaderHandles();
    }

    public static void cleanup() {
        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glDisableVertexAttribArray(sColorHandle);
        GLES20.glDeleteShader(sShaderProgram);
    }
}
