package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.Hutool;
import cn.hutool.http.HttpRequest;

public class OrderActivity extends AppCompatActivity {
    private WebView orderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderView=findViewById(R.id.order);
        WebSettings settings=orderView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        orderView.addJavascriptInterface(new OrderScript(),"order");
        orderView.loadUrl("file:///android_asset/order.html");

    }

    @Override
    protected void onResume() {
        super.onResume();
        OrderActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderView.loadUrl("file:///android_asset/order.html");
            }
        });
    }

    public class OrderScript{
        @JavascriptInterface
        public void pay(String orderString,String token,String orderId){
            Runnable payRunnable=new Runnable() {
                @Override
                public void run() {
                    PayTask task=new PayTask(OrderActivity.this);
                    Map<String,String> result=task.payV2(orderString,true);
//                    System.out.println(result);
                    String resultStatus=result.get("resultStatus");
                    if(resultStatus.equals("9000")){
                        String path="http://192.168.99.100:8080/renren-fast/app/zfb/updateOrderStatus";
                        try{
                            URL url=new URL(path);
                            HttpURLConnection con= (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5000);
                            con.setRequestMethod("POST");
                            JSONObject json=new JSONObject();
                            json.put("orderId",orderId);
                            con.setRequestProperty("Content-Type","application/json");
                            con.setRequestProperty("token",token);
                            con.setDoOutput(true);
                            OutputStream out=con.getOutputStream();
                            out.write(json.toString().getBytes());
                            out.close();
                            if(con.getResponseCode()==200){
                                OrderActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        orderView.loadUrl("file:///android_asset/order.html");
                                    }
                                });
                            }
                            con.disconnect();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }
            };
            Thread thread=new Thread(payRunnable);
            thread.start();
        }

        @JavascriptInterface
        public void wapPay(String code){
            OrderActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://192.168.99.100:8080/renren-fast/app/unionpay/wapPayPage?code="+code);
                    intent.setData(content_url);
                    startActivity(intent);
                }
            });
        }
    }
}
