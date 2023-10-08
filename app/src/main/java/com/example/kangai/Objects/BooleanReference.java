package com.example.kangai.Objects;

public class BooleanReference {
    private Boolean value;
    private String stringValue = null;

    public BooleanReference(Boolean value) {
        this.value = value;
    }
    public void switchValue(){
        this.value = !value;
    }
    public Boolean value(){
        return this.value;
    }
    public void lock(){
        this.value = true;
    }
    public void unlock(){
        this.value = false;
    }
    public Boolean isLocked(){
        return this.value;
    }
    public void setGetValue(String stringValue){
        this.stringValue = stringValue;
    }
    public String getGetValue(){
        return this.stringValue;
    }
    public Boolean hasValue(){
        return stringValue!=null;
    }
}
