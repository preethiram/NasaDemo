package com.demo.nasaapod.model;

import java.util.ArrayList;
import java.util.List;

public class APODUiData {
    private List<APODModel> apodModels = new ArrayList<>();
    private String errorMsg;


    public List<APODModel> getApodModels() {
        return apodModels;
    }

    public void setApodModels(List<APODModel> apodUiDataList) {
        this.apodModels = apodUiDataList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
