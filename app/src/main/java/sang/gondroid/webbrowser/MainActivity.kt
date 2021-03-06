package sang.gondroid.webbrowser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val homeButton: ImageButton by lazy {
        findViewById(R.id.homeButton)
    }

    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val forWardButton: ImageButton by lazy {
        findViewById(R.id.forwardButton)
    }

    private val addressEditText: EditText by lazy {
        findViewById(R.id.addressEditText)
    }

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }

    private val loadingProgressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.loadingProgressBar)
    }

    private val webViewClient: WebViewClient by lazy {
        WebViewClient(
            startedListener = {
               loadingProgressBar.show()
            },
            finishedListener = { b, url ->
                refreshLayout.isRefreshing = b
                loadingProgressBar.hide()
                backButton.isEnabled = webView.canGoBack()
                forWardButton.isEnabled = webView.canGoForward()
                addressEditText.setText(url)
            }
        )
    }

    private val webChromeClient: WebChromeClient by lazy {
        WebChromeClient(
            progressChangedListener = {
                loadingProgressBar.progress = it
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        /*
        Default Web Browser : Chrome
        1. webViewClient??? ???????????????????????? ?????? ????????? ??????
        ???????????? ????????? ??????????????? js ????????? ????????? ???????????? ??????
        2. webView.settings.javaScriptEnabled??? ??????
         */
        webView.apply {
            webViewClient = this@MainActivity.webViewClient
            webChromeClient = this@MainActivity.webChromeClient
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    // ??????????????? https??? ???????????? ??? ??????????????? http??? ???????????? https??? Redirecting ?????????
    private fun bindViews() {
        addressEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = v.text.toString()

                webView.loadUrl(
                    if (URLUtil.isNetworkUrl(loadingUrl)) {
                        loadingUrl
                    } else {
                        "http://$loadingUrl"
                    }
                )
            }

            return@setOnEditorActionListener false
        }

        homeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        backButton.setOnClickListener {
            webView.goBack()
        }

        forWardButton.setOnClickListener {
            webView.goForward()
        }

        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    // ????????????????????? ???????????? ?????? ??? ????????? ??????????????? ???????????? ???????????? ??????
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}