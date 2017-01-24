package com.example.testimsi;

/**
 * Created by li on 2017/1/10.
 */

public class TextBean {

    private String text;
    private String mCurTextColor;

    public TextBean() {
    }

    public TextBean(String text, String mCurTextColor) {
        this.text = text;
        this.mCurTextColor = mCurTextColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getmCurTextColor() {
        return mCurTextColor;
    }

    public void setmCurTextColor(String mCurTextColor) {
        this.mCurTextColor = mCurTextColor;
    }

    @Override
    public String toString() {
        return "TextBean{" +
                "text='" + text + '\'' +
                ", mCurTextColor='" + mCurTextColor + '\'' +
                '}';
    }
}
