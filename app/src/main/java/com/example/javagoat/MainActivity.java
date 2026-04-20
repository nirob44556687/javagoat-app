package com.example.javagoat;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private View pageHome, pageWallet, gameOverlay;
    private WebView webView;
    private TextView tvHeaderBal, tvFullBal;
    private int balance = 440;
    private String method = "bKash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageHome = findViewById(R.id.pageHome);
        pageWallet = findViewById(R.id.pageWallet);
        gameOverlay = findViewById(R.id.gameOverlay);
        webView = findViewById(R.id.gameWebView);
        tvHeaderBal = findViewById(R.id.tvBalance);
        tvFullBal = findViewById(R.id.tvFullBalance);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            pageHome.setVisibility(id == R.id.nav_home ? View.VISIBLE : View.GONE);
            pageWallet.setVisibility(id == R.id.nav_wallet ? View.VISIBLE : View.GONE);
            return true;
        });

        // উইথড্র মেথড সিলেকশন
        findViewById(R.id.btnBkash).setOnClickListener(v -> method = "bKash");
        findViewById(R.id.btnNagad).setOnClickListener(v -> method = "Nagad");
        findViewById(R.id.btnRocket).setOnClickListener(v -> method = "Rocket");

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            EditText etNum = findViewById(R.id.etNumber);
            EditText etAmt = findViewById(R.id.etAmount);
            String amtStr = etAmt.getText().toString();
            if(amtStr.isEmpty() || etNum.getText().toString().isEmpty()) return;
            
            int amt = Integer.parseInt(amtStr);
            if(amt > balance) {
                Toast.makeText(this, "পর্যাপ্ত টাকা নেই!", Toast.LENGTH_SHORT).show();
            } else {
                balance -= amt;
                updateUI();
                Toast.makeText(this, method + " উইথড্র রিকোয়েস্ট সফল!", Toast.LENGTH_LONG).show();
                etAmt.setText(""); etNum.setText("");
            }
        });

        setupWebView();
        findViewById(R.id.btnClose).setOnClickListener(v -> gameOverlay.setVisibility(View.GONE));
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "Android");
        webView.setWebViewClient(new WebViewClient());
    }

    private void updateUI() {
        tvHeaderBal.setText("৳ " + balance);
        tvFullBal.setText("৳ " + balance + ".00");
    }

    @JavascriptInterface
    public void onWin(int amount) {
        runOnUiThread(() -> {
            balance += amount;
            updateUI();
        });
    }

}
