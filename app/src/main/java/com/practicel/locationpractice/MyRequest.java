package com.practicel.locationpractice;

import java.util.Map;

public class MyRequest {
public String to;
public Map<String,String> data;

    public MyRequest() {
    }

    public MyRequest(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
