package com.wdy.cheemate.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 统一返回相应参数
 *
 * @author liugh 53182347@qq.com
 */
public class ResponseHelper {
    public final static int RESPONSE_OK = 1;
    public final static int RESPONSE_BADREQUEST = 2;
    public final static String OPERATION_OK = "操作成功";
    public final static String OPERATION_BADREQUEST = "操作失败";

    public ResponseHelper() {
    }

    public static <T> ResponseModel<T> validationFailure(String message) {
        ResponseModel response = new ResponseModel();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseEntity<ResultBean> buildResultBean(T successResult, T failResult, Integer responseType) {
        switch (responseType) {
            case ResponseHelper.RESPONSE_OK:
                return ResponseEntity.ok(ResultBean.success(successResult));
            case ResponseHelper.RESPONSE_BADREQUEST:
                return ResponseEntity.badRequest().body(ResultBean.error(failResult));
            default:
                return ResponseEntity.ok(ResultBean.success(successResult));
        }
    }

    public static <T> ResponseEntity<ResultBean> BooleanResultBean(T successResult, T failResult, Boolean flag) {
        if (flag)
            return ResponseEntity.ok(ResultBean.success(successResult));
        else
            return ResponseEntity.badRequest().body(ResultBean.error(failResult));

    }

    public static <T> ResponseEntity<ResultBean> buildResultBean(T result, Integer responseType) {
        switch (responseType) {
            case ResponseHelper.RESPONSE_OK:
                return ResponseEntity.ok(ResultBean.success(result));
            case ResponseHelper.RESPONSE_BADREQUEST:
                return ResponseEntity.badRequest().body(ResultBean.error(result));
            default:
                return ResponseEntity.ok(ResultBean.success(result));
        }
    }

    public static <T> ResponseEntity<ResultBean> OK() {
        return ResponseEntity.ok(ResultBean.success(OPERATION_OK));
    }

    public static <T> ResponseEntity<ResultBean> BadRequest() {
        return ResponseEntity.badRequest().body(ResultBean.error(OPERATION_BADREQUEST));
    }

    public static <T> ResponseEntity<ResultBean> OK(T result) {
        return ResponseEntity.ok(ResultBean.success(result));
    }

    public static <T> ResponseEntity<ResultBean> OK(T result, T failResult, Boolean flag) {
        if (flag)
            return ResponseEntity.ok(ResultBean.success(result));
        else
            return ResponseEntity.ok(ResultBean.success(failResult));
    }

    public static <T> ResponseEntity<ResultBean> BadRequest(T result) {
        return ResponseEntity.badRequest().body(ResultBean.error(result));
    }

    public static <T> ResponseEntity<ResultBean> OK(T result, Integer size) {
        return ResponseEntity.ok(new ResultBean(result, size));
    }

    public static <T> ResponseEntity<ResultBean> buildResultBean(T result, Integer size, Integer responseType) {
        switch (responseType) {
            case ResponseHelper.RESPONSE_OK:
                return ResponseEntity.ok(new ResultBean(result, size));
            case ResponseHelper.RESPONSE_BADREQUEST:
                return ResponseEntity.badRequest().body(new ResultBean(result, size));
            default:
                return ResponseEntity.ok(new ResultBean(result, size));
        }
    }
}
