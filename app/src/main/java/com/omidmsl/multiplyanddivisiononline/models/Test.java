package com.omidmsl.multiplyanddivisiononline.models;

import com.sardari.daterangepicker.utils.PersianCalendar;

public class Test {

    public static final String KEY_IS_MUL = "isMul";

    private int id;
    private int allNum;
    private int correctNum;
    private boolean isMul;
    private int numOfDigits;
    private PersianCalendar date;

    public Test() {
        correctNum = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    public void addCorrectNum() {
        this.correctNum ++;
    }

    public boolean isMul() {
        return isMul;
    }

    public void setMul(boolean mul) {
        isMul = mul;
    }

    public int getNumOfDigits() {
        return numOfDigits;
    }

    public void setNumOfDigits(int numOfDigits) {
        this.numOfDigits = numOfDigits;
    }

    public PersianCalendar getDate() {
        return date;
    }

    public void setDate(PersianCalendar date) {
        this.date = date;
    }
}
