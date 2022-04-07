package com.wip.model;
import java.io.Serializable;
import java.util.Map;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "Result")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "code")
    private int code;
    @ApiModelProperty(value = "success")
    private boolean success;

    /**
     *
     */
    @ApiModelProperty(value = "message")
    private String message;

    /**
     *
     */
    @ApiModelProperty(value = "result")
    private T result;

    public Result() {
        this(1, null, null, null);
    }

    public Result(int code) {
        this(code, null, null, null);
    }

    public Result(int code, Map<String, String> params) {
        this(code, params, null, null);
    }

    public Result(int code, T result) {
        this(code, null, null, result);
    }

    public Result(int code, Map<String, String> params, T result) {
        this(code, params, null, result);
    }

    public Result(int code, String message, T result) {
        this(code, null, message, result);
    }

    public Result(boolean isSuccess, String message, T result) {
        this((isSuccess ? 1 : 0), null, message, result);
    }

    public Result(int code, Map<String, String> params, String message, T result) {
        this.code = code;
        this.success = 1 == code;
        this.message = message;
        this.result = result;
    }

    /**
     *
     *
     * @param isSuccess
     * @return
     */
    public static <T> Result<T> newResult(boolean isSuccess) {
        return new Result<T>(isSuccess, null, null);
    }

    /**
     *
     *
     * @param code
     * @param result
     * @return
     */
    public static <T> Result<T> newResult(int code, T result) {
        return new Result<T>(code, result);
    }

    /**
     *
     *
     * @param code
     * @param params
     * @return
     */
    public static <T> Result<T> newResult(int code, Map<String, String> params) {
        return new Result<T>(code, params, null);
    }

    /**
     *
     *
     * @param code
     * @param params
     * @param result
     * @return
     */
    public static <T> Result<T> newResult(int code, Map<String, String> params, T result) {
        return new Result<T>(code, params, result);
    }

    /**
     *
     *
     * @param code
     * @param message
     * @param result
     * @return
     */
    public static <T> Result<T> newResult(int code, String message, T result) {
        return new Result<T>(code, message, result);
    }

    /**
     *
     *
     * @param isSuccess
     * @param message
     * @param result
     * @return
     */
    public static <T> Result<T> newResult(boolean isSuccess, String message, T result) {
        return new Result<T>(isSuccess, message, result);
    }

    /**
     *
     *
     * @return
     */
    public static <T> Result<T> newSuccessResult() {
        return newResult(true, null, null);
    }

    /**
     *
     *
     * @param result
     * @return
     */
    public static <T> Result<T> newSuccessResult(T result) {
        return newResult(true, null, result);
    }

    /**
     *
     *
     * @return
     */
    public static <T> Result<T> newFailureResult(String message) {
        return new Result<T>(0, null, message, null);
    }

    /**
     *
     *
     * @param code
     * @return
     */
    public static <T> Result<T> newFailureResult(int code) {
        return new Result<T>(code, null, null, null);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
