package com.razman;


public class Person {

    String recordID;
    String parentID;
    String gender;
    String name;

    public Person(String recordID, String parentID, String gender, String name) {
        this.recordID = recordID;
        this.parentID = parentID;
        this.gender = gender;
        this.name = name;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void println(){
        System.out.println(recordID+", "+parentID+", "+gender+", "+name);
    }
}
