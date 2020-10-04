package com.federation.funf_test.ListAdapter;

public class Question{
    String question;
    int answerId;

    public Question(String question, int answerId) {
        this.question = question;
        this.answerId = answerId;
    }

    public String getQuestion() {
        return question;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
