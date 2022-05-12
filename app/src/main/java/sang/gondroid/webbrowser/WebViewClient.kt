package sang.gondroid.webbrowser

import android.graphics.Bitmap
import android.webkit.WebView

class WebViewClient(
    private val startedListener: () -> Unit,
    private val finishedListener: (Boolean, String?) -> Unit
) : android.webkit.WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        startedListener()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        finishedListener(false, url)
    }
}