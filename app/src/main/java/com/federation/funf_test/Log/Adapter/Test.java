package com.federation.funf_test.Log.Adapter;

public class Test {
    private String status;
    private String ansTime;
    private String result;
    private String date;

    public Test(String status, String ansTime, String result, String date) {
        this.status = status;
        this.ansTime = ansTime;
        this.result = result;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public String getAnsTime() {
        return ansTime;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAnsTime(String ansTime) {
        this.ansTime = ansTime;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
