package com.danielohagan.webapp.error.response;

import com.danielohagan.webapp.error.type.IErrorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponseBean implements Serializable {

    /*
    * To be passed to JSP file
    *  */

    private List<String> mErrorList;
    private List<String> mInfoList;

    public ErrorResponseBean() {}

    public ErrorResponseBean(ErrorResponse errorResponse) {
        mErrorList = new ArrayList<>();
        mInfoList = new ArrayList<>();
        if (errorResponse != null) {
            for (IErrorType errorType : errorResponse.getOnlyErrors()) {
                mErrorList.add(errorType.getErrorMessage());
            }
            for (IErrorType errorType : errorResponse.getOnlyInfo()) {
                mInfoList.add(errorType.getErrorMessage());
            }
        }
    }

    public void setErrorList(List<IErrorType> errorList) {
        if (errorList != null) {
            List<String> errorMessageList = new ArrayList<>();
            for (IErrorType errorType : errorList) {
                errorMessageList.add(errorType.getErrorMessage());
            }

            mErrorList = errorMessageList;
        }
    }

    public List<String> getErrorList() {
        return mErrorList;
    }

    public void setInfoList(List<IErrorType> infoList) {
        if (infoList != null) {
            List<String> infoMessageList = new ArrayList<>();

            for (IErrorType errorType : infoList) {
                infoMessageList.add(errorType.getErrorMessage());
            }

            mInfoList = infoMessageList;
        }
    }

    public List<String> getInfoList() {
        return mInfoList;
    }
}