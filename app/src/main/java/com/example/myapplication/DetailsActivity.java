package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    TextView login;
    TextView followers;
    TextView following;
    TextView signature;
    TextView time;
    ImageView avatar;
    String user;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        login = findViewById(R.id.name);
        followers = findViewById(R.id.fansNum);
        following = findViewById(R.id.focusNum);
        signature = findViewById(R.id.signature);
        avatar = findViewById(R.id.headPhoto);
        time = findViewById(R.id.time);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendRequestWithOkHttp();
    }
    private void sendRequestWithOkHttp() {//网站有大问题
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        System.out.println(user);

        HttpUtil.sendOkHttpRequest("https://api.github.com/users/"+user,
                new okhttp3.Callback(){
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                            throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON(responseData);
                    }
                });
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        final UserBean userBean = gson.fromJson(jsonData, UserBean.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DetailsActivity.this,RepositoryActivity.class);
                intent.putExtra("user",userBean.getLogin());
//                System.out.println(user);
                login.setText("Name:"+ userBean.getLogin());
                followers.setText("Followers:"+ userBean.getFollowers());
                following.setText("Following:"+userBean.getFollowing());
                signature.setText("signature:"+ userBean.getBio());
                Glide.with(DetailsActivity.this).load(userBean.getAvatar_url()).into(avatar);
                time.setText("Joining time:"+userBean.getCreated_at());
            }
        });
    }
}
