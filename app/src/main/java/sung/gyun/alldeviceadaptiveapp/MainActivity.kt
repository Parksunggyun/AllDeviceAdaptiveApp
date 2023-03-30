package sung.gyun.alldeviceadaptiveapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView

class MainActivity : AppCompatActivity() {

    private lateinit var id: HaveIconEditText
    private lateinit var pw: HaveIconEditText
    private lateinit var btn: AppCompatTextView
    private var showPwChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Log.d("density", "${resources.displayMetrics.density}")
        Log.d("densityDpi", "${resources.displayMetrics.densityDpi}")
        Log.d("scaledDensity", "${resources.displayMetrics.scaledDensity}")
        Log.d("xdpi", "${resources.displayMetrics.xdpi}")
        Log.d("ydpi", "${resources.displayMetrics.ydpi}")
        Log.d("widthPixels", "${resources.displayMetrics.widthPixels}")
        Log.d("heightPixels", "${resources.displayMetrics.heightPixels}")

        id = findViewById(R.id.idTxtView)
        pw = findViewById(R.id.pwTxtiew)
        btn = findViewById(R.id.loginBtn)
        id.editIdValue.observe(this) { id ->
            pw.editPwValue.observe(this) { pw ->
                if (id && pw) btn.setBackgroundResource(R.drawable.green_btn_ripple)
                else btn.setBackgroundResource(R.drawable.unfocused_btn_ripple)
            }
        }



        id.apply {
            watchText(isId = true)
            setOnClickListener {
                text = ""
            }
            imgView02.visibility = View.GONE
        }

        pw.apply {
            watchText(isId = false)
            imgView01.setOnClickListener {
                pw.text = ""
            }

            imgView02.setOnClickListener {
                showPwChecked = !showPwChecked
                changeInputType(!showPwChecked)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUi()
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUi() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController!!.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            window.insetsController!!.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}