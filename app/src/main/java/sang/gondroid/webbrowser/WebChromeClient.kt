package sang.gondroid.webbrowser

import android.webkit.WebView

class WebChromeClient(
    private val progressChangedListener: (Int) -> Unit
) : android.webkit.WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        progressChangedListener(newProgress)
    }
}