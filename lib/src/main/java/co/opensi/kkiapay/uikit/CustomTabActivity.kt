package co.opensi.kkiapay.uikit

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import co.opensi.kkiapay.R
import co.opensi.kkiapay.uikit.Me.Companion.KKIAPAY_TRANSACTION_ID
import kotlinx.android.synthetic.main.custom_tab_activity.*

/**
 * @author Armel FAGBEDJI ( armel.fagbedji@opensi.co )
 * created  at 01/03/2019
 */

internal class CustomTabActivity: AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_tab_activity)

        intent?.run {
            val url = getStringExtra(EXTRA_URL)
            web_view.run {
                settings.run {
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
                        Log.i("CustomTabActivity", "shouldInterceptRequest")
                        if (url.startsWith(Me.KKIAPAY_REDIRECT_URL)) {
                            val transactionId = url.split("=")[1].trim()
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(KKIAPAY_TRANSACTION_ID, transactionId)
                            })
                            finish()
                        }
                    }
                }
                loadUrl(url)
            }

        }
    }

    companion object {
        internal const val EXTRA_URL = "me.kkiapay.uikit.EXTRA_URL"
    }
}