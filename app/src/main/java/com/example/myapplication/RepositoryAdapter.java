package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.MyviewHolder>{
//    private MyDatabaseHelper dbHelper;
    private Context context;
    private List<RepositoryBean>mRepositoryBeanList;
    private SQLiteDatabase sqLiteDatabase;
    String user;


    public RepositoryAdapter(Context context, List<RepositoryBean> repositoryBeanList,
                             SQLiteDatabase sqLiteDatabase){
        this.context = context;
        mRepositoryBeanList = repositoryBeanList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public RepositoryAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository,
                parent,false);
        final MyviewHolder myviewHolder = new MyviewHolder(view);
        return myviewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryAdapter.MyviewHolder holder, int position) {
        final RepositoryBean bean = mRepositoryBeanList.get(position);
        holder.name.setText("repository's name:"+bean.getName());
        holder.author.setText("author's name:"+bean.getOwner().getLogin());
        holder.forkNum.setText("forkNum:"+bean.getForks_count());
        holder.starNum.setText("starNum:"+bean.getStargazers_count());
        holder.language.setText("language:"+bean.getLanguage());

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = sqLiteDatabase.query("Repository",null,null,null,null
                ,null,null);
                if(cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        String language = cursor.getString(cursor.getColumnIndex("language"));
                        int forkNum = cursor.getInt(cursor.getColumnIndex("forkNum"));
                        int starNum =cursor.getInt(cursor.getColumnIndex("starNum"));
                        System.out.println(name + "\n" +author + "\n" + language + "\n"
                                + forkNum + "\n" + starNum + "\n" );

                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyDatabaseHelper a = new MyDatabaseHelper();
//                a.getWritableDatabase();
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装数据
                values.put("name",bean.getName());
                values.put("author",bean.getOwner().getLogin());
                values.put("forkNum",bean.getForks_count());
                values.put("starNum",bean.getStargazers_count());
                values.put("language",bean.getLanguage());
                System.out.println("1111111");//测试
                sqLiteDatabase.insert("Repository",null,values);
                values.clear();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRepositoryBeanList.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{
//        View view;
        TextView name;
        TextView author;
        TextView forkNum;
        TextView starNum;
        TextView language;
        Button check;
        Button add;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
//            view = itemView;
            check = itemView.findViewById(R.id.check);
            add = itemView.findViewById(R.id.add);
            name = itemView.findViewById(R.id.name);
            author = itemView.findViewById(R.id.author);
            forkNum = itemView.findViewById(R.id.forkNum);
            starNum = itemView.findViewById(R.id.starNum);
            language = itemView.findViewById(R.id.language);

        }
    }
}
