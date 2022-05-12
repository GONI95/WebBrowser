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
        1. webViewClient로 어플리케이션에서 만든 것으로 사용
        보안상의 문제로 기본적으로 js 관련된 것들을 허용하지 않음
        2. webView.settings.javaScriptEnabled로 허용
         */
        webView.apply {
            webViewClient = this@MainActivity.webViewClient
            webChromeClient = this@MainActivity.webChromeClient
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    // 일반적으로 https를 지원하는 웹 브라우저는 http로 접근해도 https로 Redirecting 시켜줌
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

    // 네비게이션바의 뒤로가기 클릭 시 기록이 남아있다면 뒤로가기 아니라면 종료
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