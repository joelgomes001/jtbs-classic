package com.example.jtbs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : Activity() {
    private var webView: WebView? = null
    private var splashLayout: LinearLayout? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isSplashDismissed = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force Landscape Orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Fullscreen setup (no title bar, immersive sticky, keep screen awake)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }

        // Root FrameLayout
        val root = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#0A0F1D"))
        }

        // Hardware-Accelerated WebView
        val wv = WebView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            setBackgroundColor(Color.parseColor("#0A0F1D"))

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dismissSplashWithDelay()
                }
            }

            webChromeClient = object : WebChromeClient() {
                private var customView: View? = null
                private var customViewCallback: CustomViewCallback? = null

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    if (customView != null) {
                        callback?.onCustomViewHidden()
                        return
                    }
                    customView = view
                    customViewCallback = callback
                    val decorView = window.decorView as FrameLayout
                    decorView.addView(view, FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    ))
                    this@apply.visibility = View.GONE
                }

                override fun onHideCustomView() {
                    if (customView == null) return
                    val decorView = window.decorView as FrameLayout
                    decorView.removeView(customView)
                    customView = null
                    customViewCallback?.onCustomViewHidden()
                    customViewCallback = null
                    this@apply.visibility = View.VISIBLE
                }
            }

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                cacheMode = WebSettings.LOAD_DEFAULT
                safeBrowsingEnabled = false

                // Desktop user agent string for smooth HTML5 media playback
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 JTBS-Android-App"

                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true
            }

            // Load live JTBS Classic web app URL with NEW UI
            loadUrl("https://jtbs-classic.web.app/")
        }

        webView = wv
        root.addView(wv)

        // -- NATIVE SPLASH SCREEN OVERLAY -----------------------------
        val density = resources.displayMetrics.density
        val splash = LinearLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#0A0F1D"))
            setPadding((24 * density).toInt(), (16 * density).toInt(), (24 * density).toInt(), (16 * density).toInt())

            // 1. Channel Logo Image
            val logoImg = ImageView(this@MainActivity).apply {
                val logoResId = resources.getIdentifier("logo", "drawable", packageName)
                if (logoResId != 0) {
                    setImageResource(logoResId)
                } else {
                    setImageResource(android.R.drawable.ic_menu_camera)
                }
                val logoSize = (130 * density).toInt()
                layoutParams = LinearLayout.LayoutParams(logoSize, logoSize).apply {
                    bottomMargin = (14 * density).toInt()
                }
            }
            addView(logoImg)

            // 2. Channel Name
            val titleText = TextView(this@MainActivity).apply {
                text = getString(R.string.channel_name)
                setTextColor(Color.parseColor("#F59E0B"))
                textSize = 24f
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                letterSpacing = 0.08f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = (6 * density).toInt()
                }
            }
            addView(titleText)

            // 3. Proprietor Info
            val propText = TextView(this@MainActivity).apply {
                text = getString(R.string.proprietor_info)
                setTextColor(Color.parseColor("#E2E8F0"))
                textSize = 14f
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                letterSpacing = 0.04f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = (4 * density).toInt()
                }
            }
            addView(propText)

            // 4. Contact / Mobile Number
            val contactText = TextView(this@MainActivity).apply {
                text = getString(R.string.contact_number)
                setTextColor(Color.parseColor("#94A3B8"))
                textSize = 12f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = (16 * density).toInt()
                }
            }
            addView(contactText)

            // 5. Circular Progress Bar
            val pBar = ProgressBar(this@MainActivity).apply {
                val pSize = (32 * density).toInt()
                layoutParams = LinearLayout.LayoutParams(pSize, pSize).apply {
                    bottomMargin = (8 * density).toInt()
                }
            }
            addView(pBar)

            // 6. Loading Status Text
            val statusText = TextView(this@MainActivity).apply {
                text = "CONNECTING TO LIVE STREAM..."
                setTextColor(Color.parseColor("#64748B"))
                textSize = 11f
                gravity = Gravity.CENTER
                letterSpacing = 0.05f
            }
            addView(statusText)
        }

        splashLayout = splash
        root.addView(splash)
        setContentView(root)

        // Safety fallback timer: Automatically dismiss splash screen after 6 seconds maximum
        mainHandler.postDelayed({ dismissSplashWithDelay() }, 6000)
    }

    private fun dismissSplashWithDelay() {
        if (isSplashDismissed) return
        isSplashDismissed = true

        mainHandler.postDelayed({
            splashLayout?.animate()
                ?.alpha(0f)
                ?.setDuration(500)
                ?.withEndAction {
                    splashLayout?.visibility = View.GONE
                }
                ?.start()
        }, 1200) // Keep splash screen visible for 1.2s so viewer reads channel/prop info
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        webView?.destroy()
        webView = null
        super.onDestroy()
    }
}
