package com.arthurgichuhi.aopengl.my_opengl
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES20.GL_FALSE
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glClearColor
import android.opengl.GLSurfaceView
import com.arthurgichuhi.aopengl.R
import com.arthurgichuhi.aopengl.objects.Triangle
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(context: Context):GLSurfaceView.Renderer {
    private var _context:Context = context
    private lateinit var mTriangle: Triangle

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mTriangle=Triangle(context = _context)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mTriangle.draw()
    }
}