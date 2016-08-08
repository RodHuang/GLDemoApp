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
    public static int sMVPMatrixHandle;
    public static int sMVMatrixHandle;
    public static int sLightPosHandle;
    public static int sNormalHandle;
    public static int sSamplerLoc;

    private static final String _vertexShader =
                    "uniform mat4 u_MVPMatrix;                                             \n"
                    + "uniform mat4 u_MVMatrix;                                            \n"

                    + "attribute vec4 a_Position;                                          \n"
                    + "attribute vec4 a_Color;                                             \n"
                    + "attribute vec3 a_Normal;                                            \n"

                    + "varying vec3 v_Position;                                            \n"
                    + "varying vec4 v_Color;                                               \n"
                    + "varying vec3 v_Normal;                                              \n"

                    + "void main()                                                         \n"
                    + "{                                                                   \n"
                    + "   v_Position = vec3(u_MVMatrix * a_Position);                      \n"
                    + "   v_Color = a_Color;                                               \n"
                    + "   v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));               \n"
                    + "   gl_Position = u_MVPMatrix                                        \n"
                    + "               * a_Position;                                        \n"
                    + "}                                                                   \n";
    private static final String _fragmentShader =
                    "precision mediump float;                                              \n"

                    + "uniform vec3 u_LightPos;                                             \n"

                    + "varying vec4 v_Color;                                               \n"
                    + "varying vec3 v_Position;                                            \n"
                    + "varying vec3 v_Normal;                                              \n"

                    + "void main()                                                         \n"
                    + "{                                                                   \n"
                    + "   float distance = length(u_LightPos - v_Position);                \n"
                    + "   vec3 lightVector = normalize(u_LightPos - v_Position);           \n"
                    + "   float diffuse = max(dot(v_Normal, lightVector), 0.1);            \n"
                    + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));\n"
                    + "   gl_FragColor = (v_Color * diffuse) + (v_Color * 0.25);                                \n"
                    + "}                                                                   \n";

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d("Rod", "Could not compile shader " + type + ":");
            Log.d("Rod", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        // return the shader
        return shader;
    }


    public static void initShaderHandles() {

        sPositionHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Position");
        sColorHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Color");
        sNormalHandle =  GLES20.glGetAttribLocation(sShaderProgram,
                "a_Normal");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_MVPMatrix");
        sMVMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_MVMatrix");
        sLightPosHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_LightPos");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glEnableVertexAttribArray(sColorHandle);
        GLES20.glEnableVertexAttribArray(sNormalHandle);
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
        GLES20.glDisableVertexAttribArray(sNormalHandle);
        GLES20.glDeleteShader(sShaderProgram);
    }
}
