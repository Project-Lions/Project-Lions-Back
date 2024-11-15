package com.project_lions.back.apiPayload.exception.handler;

import com.project_lions.back.apiPayload.code.BaseErrorCode;
import com.project_lions.back.apiPayload.exception.GeneralException;

public class FileUploadHandler extends GeneralException {

  public FileUploadHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
