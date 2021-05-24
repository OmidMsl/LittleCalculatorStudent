package com.omidmsl.multiplyanddivisiononline.models;

import com.sardari.daterangepicker.utils.PersianCalendar;

public class Message {
    private String content;
    private int id, senderId, receiverId;
    private boolean isFromTeacher, isObserved;
    private PersianCalendar date;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isFromTeacher() {
        return isFromTeacher;
    }

    public void setFromTeacher(boolean fromTeacher) {
        isFromTeacher = fromTeacher;
    }

    public boolean isObserved() {
        return isObserved;
    }

    public void setObserved(boolean observed) {
        isObserved = observed;
    }

    public PersianCalendar getDate() {
        return date;
    }

    public void setDate(PersianCalendar date) {
        this.date = date;
    }
}
