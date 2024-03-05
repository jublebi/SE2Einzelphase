package com.example.seiieinzelphasejava;

public interface NetworkTaskListener {
    void onSuccess(String response);
    void onError(Exception e);
}