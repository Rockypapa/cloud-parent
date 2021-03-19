package me.rocky.common.exception;


import me.rocky.common.result.ResultCode;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2020/9/15 上午11:28
 * @log
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = -1383653503902931540L;


    private long code;

    private String message;

    public BusinessException() {
        super(ResultCode.FAILED.getMessage());
        this.code = ResultCode.FAILED.getCode();
    }

    public BusinessException(String message, long code) {
        super(message);
        this.code = code;
    }



    public BusinessException(final long code) {
        super(ResultCode.FAILED.getMessage());
        this.code = code;
    }

    public BusinessException(final String detailedMessage) {
        super(detailedMessage);
        this.code = ResultCode.FAILED.getCode();
    }

    public BusinessException(final Throwable t) {
        super(t.getMessage());
        this.code = ResultCode.FAILED.getCode();
    }


    public BusinessException(ResultCode res, final Throwable t) {
        super(res.getMessage(), t);
        this.code = res.getCode();
    }

    public BusinessException(final String message, final Throwable t) {
        super(message, t);
        this.code = ResultCode.FAILED.getCode();
    }


}