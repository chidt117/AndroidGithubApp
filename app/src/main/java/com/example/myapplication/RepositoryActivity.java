package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import android.os.Bundle;

public class RepositoryActivity extends AppCompatActivity {

    TextView name;
    TextView author;
    TextView forkNum;
    TextView starNum;
    TextView language;

    String user;
//    String repositoryName;
//    String repositoryForkNum;
//    String repositoryStarNum;
//    String repositoryLanguage;

    RecyclerView recyclerView;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase writableDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        author = findViewById(R.id.author);
        forkNum = findViewById(R.id.forkNum);
        starNum = findViewById(R.id.starNum);
        language = findViewById(R.id.language);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendRequestWithOkHttp();
        dbHelper = new MyDatabaseHelper(this, "Repository.db", null, 1);
        writableDatabase = dbHelper.getWritableDatabase();
//        };
////        dbHelper = new MyDatabaseHelper(this,"Repository.db",null,1);
////        Button add = findViewById(R.id.add);
////        add.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                dbHelper.getWritableDatabase();
////                SQLiteDatabase db = dbHelper.getWritableDatabase();
////                ContentValues values = new ContentValues();
////                //开始组装数据
////                values.put("name",repositoryName);
////                values.put("author",user);
////                values.put("forkNum",repositoryForkNum);
////                values.put("starNum",repositoryStarNum);
////                values.put("language",repositoryLanguage);
////                db.insert("Repository",null,values);
////                values.clear();
////            }
////        });
    }




    private void sendRequestWithOkHttp() {
        try {
            OkHttpClient client = new OkHttpClient();
            Intent intent = getIntent();
            user = intent.getStringExtra("user");
            Request request = new Request.Builder()
                    // 指定访问的服务器地址
                    .url("https://api.github.com/users/"+user+"/repos")
                    .build();
            System.out.println(user);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("errorMessage " +  e.getMessage());
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                }
            });

            //                    parseJSONWithJSONObject(responseData);
            //                    parseXMLWithSAX(responseData);
            //                    parseXMLWithPull(responseData);
            //                    showResponse(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONWithGSON(final String jsonData) {
//        String repositoryName;
//        String repositoryForkNum;
//        String repositoryStarNum;
//        String repositoryLanguage;
        final Gson gson = new Gson();
//        Intent intent = getIntent();
//        user = intent.getStringExtra("user");
//        repositoryName = intent.getStringExtra("name");
//        repositoryForkNum = intent.getStringExtra("forkNum");
//        repositoryStarNum = intent.getStringExtra("starNum");
//        repositoryLanguage = intent.getStringExtra("language");

//        final UserBean userBean = gson.fromJson(jsonData, UserBean.class);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 List<RepositoryBean> repositoryBeanList = gson.fromJson(jsonData,
                        new TypeToken<List<RepositoryBean>>(){}.getType());

                    recyclerView.setAdapter(new RepositoryAdapter(RepositoryActivity.this,
                            repositoryBeanList, writableDatabase));



            }
        });
    }







}
