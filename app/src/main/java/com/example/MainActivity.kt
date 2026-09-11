package com.example

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) { innerPadding ->
          PeriodicQuestWebView(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PeriodicQuestWebView(modifier: Modifier = Modifier) {
  var webViewRef by remember { mutableStateOf<WebView?>(null) }

  BackHandler(enabled = webViewRef?.canGoBack() == true) {
    webViewRef?.goBack()
  }

  AndroidView(
    modifier = modifier.fillMaxSize(),
    factory = { context ->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(Color.parseColor("#0B132B"))

        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)

        settings.apply {
          javaScriptEnabled = true
          domStorageEnabled = true
          allowFileAccess = true
          databaseEnabled = true
          useWideViewPort = true
          loadWithOverviewMode = true
          mediaPlaybackRequiresUserGesture = false
          cacheMode = WebSettings.LOAD_DEFAULT
        }

        webViewClient = object : WebViewClient() {
          override fun onRenderProcessGone(
            view: WebView?,
            detail: android.webkit.RenderProcessGoneDetail?
          ): Boolean {
            view?.post {
              view.loadUrl("file:///android_asset/index.html")
            }
            return true
          }
        }
        webChromeClient = WebChromeClient()

        loadUrl("file:///android_asset/index.html")
        webViewRef = this
      }
    },
    update = {
      webViewRef = it
    }
  )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}
