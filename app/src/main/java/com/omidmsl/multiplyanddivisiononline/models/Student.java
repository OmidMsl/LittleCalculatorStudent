package com.omidmsl.multiplyanddivisiononline.models;

public class Student {

    public static final String KEY_ID = "studentId";
    public static final String KEY_NAME = "studentName";
    public static final String KEY_FATHER_NAME = "studentsFatherName";

    private int id;
    private String name;
    private String fatherName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
