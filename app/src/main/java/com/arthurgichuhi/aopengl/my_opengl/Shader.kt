package com.arthurgichuhi.aopengl.my_opengl

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class Shader {
    val logTag = "Shader Class"
    var program: Int = 0
    var vertexShader:Int=0
    var fragmentShader:Int=0
    var vertexS: String =""
    var fragmentS: String = ""

    // Takes in ids for files to be read
    fun getMyShaders(vID: Int, fID: Int, context: Context, hasTextures: Boolean, numTextures: Int) {
        val vs = StringBuffer()
        val fs = StringBuffer()

        // read the files
        try {
            // Read the file from the resource
            //Log.d("loadFile", "Trying to read vs");
            // Read VS first
            var inputStream = context.resources.openRawResource(vID)
            // setup Bufferedreader
            var `in` = BufferedReader(InputStreamReader(inputStream))

            var read = `in`.readLine()
            while (read != null) {
                vs.append(read + "\n")
                read = `in`.readLine()
            }

            vs.deleteCharAt(vs.length - 1)

            // Now read FS
            inputStream = context.resources.openRawResource(fID)
            // setup Bufferedreader
            `in` = BufferedReader(InputStreamReader(inputStream))

            read = `in`.readLine()
            while (read != null) {
                fs.append(read + "\n")
                read = `in`.readLine()
            }

            fs.deleteCharAt(fs.length - 1)
        } catch (e: Exception) {
            Log.d("ERROR-readingShader", "Could not read shader: " + e.localizedMessage)
        }
        // Setup everything
        setup(vs.toString(), fs.toString())
    }

    fun setup(vs:String, fs:String){
        this.vertexS=vs
        this.fragmentS=fs

        val create:Int = createProgram()
    }

    fun createProgram():Int{
        //Vertex Shader
        loadShader(GLES20.GL_VERTEX_SHADER,vertexS)
        if(vertexShader==0){
            return 0
        }
        //fragment shader
        loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentS)
        //create program
        program=GLES20.glCreateProgram()
        if(program==0){
            Log.d("CreateProgram", "Could not create program");
            return 0
        }
        //attach vertex shader
        GLES20.glAttachShader(program,vertexShader)
        //attach fragment shader
        GLES20.glAttachShader(program,fragmentShader)

        GLES20.glLinkProgram(program)
        val linkStatus:IntArray=IntArray(1)
        GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0)
        if(linkStatus[0]!=GLES20.GL_TRUE){
            Log.e(logTag,"Could not link program")
            Log.e(logTag,GLES20.glGetProgramInfoLog(program))
            GLES20.glDeleteProgram(program)
            return 0
        }
        else{
            Log.d(logTag, "Could not create program");
            return 1;
        }

    }

    fun loadShader(shaderType:Int,source:String):Int{
        val shader:Int = GLES20.glCreateShader(shaderType)
        if(shader!=0){
            GLES20.glShaderSource(shader,source)
            GLES20.glCompileShader(shader)
            val compiled:IntArray= intArrayOf(1)
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0)
            if(compiled[0]==0){
                Log.e(logTag,"Could not compile shader $shaderType :")
                Log.e(logTag,GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
            }
        }
        return shader
    }
}