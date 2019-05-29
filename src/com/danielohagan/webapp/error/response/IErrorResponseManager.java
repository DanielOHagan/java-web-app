package com.danielohagan.webapp.error.response;

import com.danielohagan.webapp.error.ErrorSeverity;
import com.danielohagan.webapp.error.type.IErrorType;

import java.util.List;

public interface IErrorResponseManager {

    void add(IErrorType errorType);
    void add(List<IErrorType> errorTypeList);
    void removeError(IErrorType errorType);

    boolean contains(IErrorType errorType);
    boolean containsSeverity(ErrorSeverity errorSeverity);
    boolean hasError();
    boolean hasInfo();

    List<IErrorType> getBySeverity(ErrorSeverity errorSeverity);
    List<IErrorType> getOnlyErrors();
    List<IErrorType> getOnlyInfo();

    ErrorSeverity highestSeverity();

    ErrorResponseBean convertToBean();
}