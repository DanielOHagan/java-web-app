package com.danielohagan.webapp.error.response;

import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.type.IErrorType;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse implements IErrorResponseManager {

    //TODO:: Pass an object of this to the presentation layer instead of a single Error Type

    private List<IErrorType> mErrorList;

    public ErrorResponse() {
        mErrorList = new ArrayList<>();
    }

    @Override
    public void add(IErrorType error) {
        if (mErrorList == null) {
            mErrorList = new ArrayList<>();
        }

        mErrorList.add(error);
    }

    @Override
    public void add(List<IErrorType> errorTypeList) {
        if (mErrorList == null) {
            mErrorList = new ArrayList<>();
        }

        mErrorList.addAll(errorTypeList);
    }

    @Override
    public void removeError(IErrorType errorType) {
        if (mErrorList != null) {
            mErrorList.remove(errorType);
        }
    }

    @Override
    public boolean contains(IErrorType errorType) {
        if (mErrorList != null) {
            return mErrorList.contains(errorType);
        }

        return false;
    }

    @Override
    public boolean containsSeverity(ErrorSeverity errorSeverity) {
        if (mErrorList != null) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() == errorSeverity) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<IErrorType> getBySeverity(ErrorSeverity errorSeverity) {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() == errorSeverity) {
                    errorTypeList.add(errorType);
                }
            }
        }

        if (errorTypeList.isEmpty()) {
            errorTypeList = null;
        }

        return errorTypeList;
    }

    @Override
    public List<IErrorType> getOnlyErrors() {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() != ErrorSeverity.INFO) {
                    errorTypeList.add(errorType);
                }
            }
        }

        if (errorTypeList.isEmpty()) {
            errorTypeList = null;
        }

        return errorTypeList;
    }

    @Override
    public List<IErrorType> getOnlyInfo() {
        List<IErrorType> errorTypeList = new ArrayList<>();

        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() == ErrorSeverity.INFO) {
                    errorTypeList.add(errorType);
                }
            }
        }

        if (errorTypeList.isEmpty()) {
            errorTypeList = null;
        }

        return errorTypeList;
    }

    @Override
    public boolean hasError() {
        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() != ErrorSeverity.INFO) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasInfo() {
        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                if (errorType.getErrorSeverity() == ErrorSeverity.INFO) {
                    return true;
                }
            }
        }

        return false;
    }


    //TODO:: Fix this mess of a method
    @Override
    public ErrorSeverity highestSeverity() {
        ErrorSeverity errorSeverity = null;
        int currentSeverity = 0;
        final int infoSev = 1;
        final int minorSev = 2;
        final int majorSev = 3;

        if (mErrorList != null && !mErrorList.isEmpty()) {
            for (IErrorType errorType : mErrorList) {
                switch (errorType.getErrorSeverity()) {
                    case INFO:
                        if (currentSeverity < infoSev) {
                            currentSeverity = infoSev;
                            errorSeverity = ErrorSeverity.INFO;
                        }
                        break;
                    case MINOR:
                        if (currentSeverity < minorSev) {
                            currentSeverity = minorSev;
                            errorSeverity = ErrorSeverity.MINOR;
                        }
                        break;
                    case MAJOR:
                        if (currentSeverity < majorSev) {
                            currentSeverity = majorSev;
                            errorSeverity = ErrorSeverity.MAJOR;
                        }
                        break;
                    case FATAL:
                        return ErrorSeverity.FATAL;
                }
            }
        }

        return errorSeverity;
    }

    @Override
    public ErrorResponseBean convertToBean() {
        return new ErrorResponseBean(this);
    }

    public List<IErrorType> getErrorList() {
        return mErrorList;
    }

    public void setErrorList(List<IErrorType> errorList) {
        mErrorList = errorList;
    }
}