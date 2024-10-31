/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.yhp.distributedata;

/**
 * SDK异常类封装
 *
 * @since 2021-07-07
 */
public class StarFlashException extends Exception {
    // 错误码
    private String errorCode;

    // 错误详细描述信息
    private String detailArgs;

    /**
     * SDK异常类封装
     *
     * @param errorCode 错误码
     * @param cause 捕获的异常
     */
    public StarFlashException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * SDK异常类封装
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public StarFlashException(String errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
    }

    /**
     * SDK异常类封装
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param detailMessage 详细错误信息
     */
    public StarFlashException(String errorCode, String errorMessage, String detailMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
        this.detailArgs = detailMessage;
    }

    /**
     * SDK异常封装
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param cause 捕获的异常
     */
    public StarFlashException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    /**
     * ActionException
     *
     * @param errorCode errorCode
     */
    public StarFlashException(String errorCode) {
//        this(errorCode, ErrorCode.getErrorMessage(errorCode));
        this(errorCode, errorCode);
    }

    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误描述信息
     * 
     * @return 错误描述信息
     */
    public String getErrorMessage() {
        return this.getMessage();
    }

    @Override
    public String toString() {
        String name = getClass().getName();
        return name + ":: [errorCode=" + errorCode + "; errorMessage=" + getErrorMessage();
    }

    public String getDetailArgs() {
        return detailArgs;
    }

    public void setDetailArgs(String detailArgs) {
        this.detailArgs = detailArgs;
    }
}