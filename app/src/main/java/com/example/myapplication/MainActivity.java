package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.SAXParserFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;
    RecyclerView recyclerView;
    EditText input_text;

    static public String niCheng;//昵称
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button sendRequest =  findViewById(R.id.send_request);
        input_text = findViewById(R.id.input_text);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        responseText = (TextView) findViewById(R.id.response_text);

        sendRequest.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {

            if (input_text.getText().toString().equals(""))
                return;
            niCheng = input_text.getText().toString();
            input_text.setText("");
            sendRequestWithOkHttp();

        }
    }

    private void sendRequestWithOkHttp() {
        HttpUtil.sendOkHttpRequest("https://api.github.com/search/users?q="+niCheng,
                new okhttp3.Callback(){
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        System.out.println("FAILURE!!!!!!!");
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                            throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON(responseData);
                    }
                });


    }


    @SuppressLint("SetTextI18n")
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        SearchBean searchBean = gson.fromJson(jsonData, SearchBean.class);
        responseText.setText("共搜索到"+searchBean.getTotal_count() + "条");
        final List<SearchBean.ItemsBean> items = searchBean.getItems();

//        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
//        intent.putExtra("name",);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new SearchUserAdapter(MainActivity.this, items));
            }
        });


    }

//    private void showResponse(final String response) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // 在这里进行UI操作，将结果显示到界面上
//                responseText.setText(response);
//            }
//        });
//    }

}
