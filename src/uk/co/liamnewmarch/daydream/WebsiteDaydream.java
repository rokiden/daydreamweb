package uk.co.liamnewmarch.daydream;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebsiteDaydream extends DreamService {

	private WebView webView;
	private WebSettings webSettings;
	private SharedPreferences sharedPreferences;
	private boolean preferenceFullscreen;
	private boolean preferenceInteractive;
	private String preferenceUrl;
	private boolean preferenceRefresh;
	private Integer preferenceInterval;
	private boolean preferenceOverviewMode;
	private boolean preferenceWideViewPort;

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		setContentView(R.layout.main);

		webView = (WebView) findViewById(R.id.webView);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		preferenceFullscreen = sharedPreferences.getBoolean("pref_key_fullscreen", true);
		preferenceInteractive = sharedPreferences.getBoolean("pref_key_interactive", false);
		preferenceUrl = sharedPreferences.getString("pref_key_url", "http://www.bbc.co.uk/news");
		preferenceRefresh = sharedPreferences.getBoolean("pref_key_refresh", false);
		preferenceInterval = Integer.parseInt(sharedPreferences.getString("pref_key_interval", "5"));
		preferenceOverviewMode = sharedPreferences.getBoolean("pref_key_overviewmode", false);
		preferenceWideViewPort = sharedPreferences.getBoolean("pref_key_wideviewport", false);

		setFullscreen(preferenceFullscreen);
		setInteractive(preferenceInteractive);

		webView.setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
			View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
			View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
			View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
			View.SYSTEM_UI_FLAG_FULLSCREEN |
			View.SYSTEM_UI_FLAG_IMMERSIVE
		);

		webSettings = webView.getSettings();
		webSettings.setAppCacheEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSettings.setLoadWithOverviewMode(preferenceOverviewMode);
		webSettings.setUseWideViewPort(preferenceWideViewPort);
		webSettings.setSavePassword(false);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (preferenceRefresh) {
					String javascript = String.format("setTimeout(function() { location.reload(); }, %d * 6E4);", preferenceInterval);
					webView.loadUrl("javascript:" + javascript);
				}
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				Log.d("WebsiteDaydream", "Alert: \"" + message + "\"");
				result.confirm();
				return true;
			}
		});

		webView.loadUrl(preferenceUrl);
	}
}
