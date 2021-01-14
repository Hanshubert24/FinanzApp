package com.example.finanzapp.ui.DB;

public class DBInformationObject {
    private boolean isSuccess;
    private String massage;

    public DBInformationObject(){
        this.isSuccess = false;
        this.massage = null;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getMassage() {
        return this.massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
