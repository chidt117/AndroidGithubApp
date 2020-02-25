package com.example.myapplication;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
