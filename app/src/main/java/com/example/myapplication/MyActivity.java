package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.myapplication.Bean.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.MD5;
import cz.msebera.android.httpclient.Header;

public class MyActivity extends AppCompatActivity {

    Button recharge;//充值按钮
    EditText money;//充入金额
    TextView account_money;//可用账户余额
    TextView bond;//保证金
    String url = "http://192.168.56.1:8080/renren-fast/app/zfb/appPayOrder";
    String orderString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //允许安卓app调用沙箱环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //找到各个组件
        recharge = findViewById(R.id.recharge);
        money = findViewById(R.id.money);
        account_money = findViewById(R.id.account_money);
        bond = findViewById(R.id.bond);
        User user = new User();
        //按钮绑定点击事件
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivity myActivity = new MyActivity();
                String userAccountMoney;
                String userBond;
                String userMoney;
                //将数据封装在user类中
                userAccountMoney = account_money.getText().toString();
                userBond = bond.getText().toString();
                userMoney = money.getText().toString();
                //这里先暂且设定用户id为1，后续可以从前端页面获取
                user.setUser_id(1);
                //暂且设定用户名为
                user.setUsername("charlie");
                user.setAccount_money(Integer.valueOf(userAccountMoney));
                user.setBond(Integer.valueOf(userBond));
                user.setMoney(Integer.valueOf(userMoney));
                user.setCreate_time(new Date());
                //开启发送json数据
                if(myActivity.sendJson(user)){
                    System.out.println(orderString);
                    myActivity.pay(orderString);
                }else{
                    System.out.println("发送错误");
                }


            }
        });
    }


    /**
     * 开启一个新线程，将前端页面数据以json格式存放
     */
    public boolean sendJson(User user)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("user_id",user.getUser_id());
                    userJSON.put("username",user.getUsername());
                    userJSON.put("userAccountMoney",user.getAccount_money());
                    userJSON.put("userMoney",user.getMoney());
                    userJSON.put("userBond",user.getBond());
                    userJSON.put("userCreateTime",user.getCreate_time());
                    String content = String.valueOf(userJSON);

                    //发送到服务器
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    OutputStream os = connection.getOutputStream();
                    System.out.println(content);
                    //将user字符串发送到服务器
                    os.write(content.getBytes());
                    os.close();

                    //服务器返回结果,根据服务器返回的结果调用支付宝支付入
                    if(connection.getResponseCode()==200){
                        InputStream inputStream = connection.getInputStream();
                        byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes);
                        orderString = new String(bytes);
                    }

                    //开启支付宝支付接口




                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    /**
     * 根据后端传送回来的orderString调用支付宝支付
     * @param orderString
     */
    public  void pay(String orderString) {
        //由得到的orderString开启一个线程调用支付宝支付入口
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask task = new PayTask(MyActivity.this);
                Map<String, String> result = task.payV2(orderString, true);
                System.out.println(result);
                }
            };
        Thread thread = new Thread(payRunnable);
        thread.start();
    }

}


