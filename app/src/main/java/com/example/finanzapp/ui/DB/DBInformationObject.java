package com.example.finanzapp.ui.DB;

public class DBInformationObject {
    private boolean isSuccess;
    private String massage;
    private boolean isLastParentDelete;

    public DBInformationObject(){
        this.isSuccess = false;
        this.massage = null;
        this.isLastParentDelete = false;
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

    public boolean isLastParentDelete() {
        return isLastParentDelete;
    }

    public void setLastParentDelete(boolean lastParentDelete) {
        isLastParentDelete = lastParentDelete;
    }
}
