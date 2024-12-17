import android.content.Context
import android.opengl.GLSurfaceView
import com.arthurgichuhi.aopengl.my_opengl.MyGLRenderer
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer
        private var value: IntArray = IntArray(1)

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer(context)
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}

