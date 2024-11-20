package com.project_lions.back.apiPayload.exception.handler;

import com.project_lions.back.apiPayload.code.BaseErrorCode;
import com.project_lions.back.apiPayload.exception.GeneralException;

public class ReviewHandler extends GeneralException {

  public ReviewHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
