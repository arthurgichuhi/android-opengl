package com.arthurgichuhi.aopengl.objects

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import com.arthurgichuhi.aopengl.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(context: Context) {
    private val COORDS_PER_VERTEX = 3
    private var triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,      // top
        -0.5f, -0.311004243f, 0.0f,    // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
    )
    private var vertexBuffer: FloatBuffer?=null
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.72265625f, 1.0f)
    private var program:Int = 0

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    init {
        vertexBuffer=ByteBuffer.allocateDirect(triangleCoords.size*4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(triangleCoords)
                position(0)
            }
        }
        val vs:Int=loadShader(GLES20.GL_VERTEX_SHADER,getShaderCode("vs",context))
        val fs:Int=loadShader(GLES20.GL_FRAGMENT_SHADER,getShaderCode("fs",context))
        program=GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it,vs)
            GLES20.glAttachShader(it,fs)
            GLES20.glLinkProgram(it)
        }
    }


    private fun loadShader(type:Int, shaderCode:String):Int{
        return GLES20.glCreateShader(type).also {shader->
            //add shader code and compile it
            GLES20.glShaderSource(shader,shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4

    private fun getShaderCode(type:String, context:Context):String{
        val stringBuilder=StringBuilder()
        var inputStream:InputStream?=null
        if(type=="vs"){
            try{
                inputStream=context.resources.openRawResource(R.raw.vs)
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line:String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.deleteCharAt(stringBuilder.length - 1)
                }

            }
            catch (e:Error){
                Log.d("A-OPENGL","Error reading shader file \n$e")
            }
            return stringBuilder.toString()
        }
        else{
            inputStream=context.resources.openRawResource(R.raw.fs)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line:String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
            }
            return stringBuilder.toString()
        }
    }

    fun draw(){
        GLES20.glUseProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program,"vPosition").also{
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(it,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer!!)
            mColorHandle= GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }
            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }

    }
}