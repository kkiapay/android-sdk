package co.opensi.kkiapay.uikit

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import co.opensi.kkiapay.R
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_TRANSACTION_ID
import kotlinx.android.synthetic.main.custom_tab_activity.*

/**
 * @author Armel FAGBEDJI ( armel.fagbedji@opensi.co )
 * created  at 01/03/2019
 */

internal class CustomTabActivity: Activity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_tab_activity)

        kkiapay_web_view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        intent?.run {
            val url = getStringExtra(EXTRA_URL)
            val theme = getIntExtra(EXTRA_THEME,R.color.pink )
            tintIndeterminateProgress(progressbar, ContextCompat.getColor(applicationContext,theme))
            kkiapay_web_view.run {
                settings.run {
                    domStorageEnabled = true
                    javaScriptEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                    scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
                }

                webViewClient = object : WebViewClient(){

                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        shouldOverrideUrlLoading(url ?: "")
                        return super.shouldOverrideUrlLoading(view, url)
                    }

                    override fun onLoadResource(view: WebView?, url: String?) {
                        return super.onLoadResource(view, url)

                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        shouldOverrideUrlLoading(request?.url?.toString() ?: "")
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    private fun shouldOverrideUrlLoading(url: String){
                        Log.i("CustomTabActivity", "shouldOverrideUrlLoading")
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.i("CustomTabActivity", "onPageStarted")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.i("CustomTabActivity", "onPageFinished")
                        val timer = object: CountDownTimer(990,990) {
                            override fun onTick(millisUntilFinished: Long) {}

                            override fun onFinish() {
                                progressbar.visibility = View.GONE
                            }
                        }
                        timer.start()
                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        shouldInterceptRequest(request?.url?.toString() ?: "")
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                        shouldInterceptRequest(url ?: "")
                        return super.shouldInterceptRequest(view, url)
                    }

                    private fun shouldInterceptRequest(url: String){
                        if (url.startsWith(Me.KKIAPAY_REDIRECT_URL)) {
                            val transactionId = url.split("=")[1].trim()
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(KKIAPAY_TRANSACTION_ID, transactionId)
                            })
                            finish()
                        }
                    }
                }
                loadUrl(url ?: "")
            }

        }
    }

    companion object {
        internal const val EXTRA_URL = "me.kkiapay.uikit.EXTRA_URL"
        internal const val EXTRA_THEME = "me.kkiapay.uikit.EXTRA_THEME"

    }

    fun tintIndeterminateProgress(progress: ProgressBar, @ColorInt color: Int ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progress.indeterminateTintList = ColorStateList.valueOf(color)
        } else {
            (progress.indeterminateDrawable as? LayerDrawable)?.apply {
                if (numberOfLayers >= 2) {
                    setId(0, android.R.id.progress)
                    setId(1, android.R.id.secondaryProgress)
                    val progressDrawable = findDrawableByLayerId(android.R.id.progress).mutate()
                    progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
            }
        }
    }
}