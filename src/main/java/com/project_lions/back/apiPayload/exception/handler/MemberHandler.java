package com.project_lions.back.apiPayload.exception.handler;

import com.project_lions.back.apiPayload.code.BaseErrorCode;
import com.project_lions.back.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
