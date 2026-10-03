package com.example.slovo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Вся игра написана на HTML/JS (папка assets), Activity только показывает её
 * во весь экран и передаёт в неё нажатия кнопок пульта.
 */
public class MainActivity extends Activity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        web = new WebView(this);
        web.setBackgroundColor(0xFF0F1B24);
        web.setFocusable(true);
        web.setFocusableInTouchMode(true);
        web.setWebViewClient(new WebViewClient());

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);   // для сохранения статистики

        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
        web.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemBars();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }

    @SuppressWarnings("deprecation")
    private void hideSystemBars() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    /** Кнопки пульта: стрелки двигают выбор на экранной клавиатуре, OK нажимает выбранную клавишу. */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        String cmd;
        switch (e.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:     cmd = "up";    break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   cmd = "down";  break;
            case KeyEvent.KEYCODE_DPAD_LEFT:   cmd = "left";  break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  cmd = "right"; break;
            case KeyEvent.KEYCODE_DPAD_CENTER: cmd = "ok";    break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER: cmd = "enter"; break;
            default:
                return super.dispatchKeyEvent(e);   // буквы с USB-клавиатуры, «Назад» и т.д.
        }
        if (e.getAction() == KeyEvent.ACTION_DOWN) {
            web.evaluateJavascript("window.tvKey && window.tvKey('" + cmd + "')", null);
        }
        return true;
    }
}
