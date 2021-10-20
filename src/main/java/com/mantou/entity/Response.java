package com.mantou.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mantou
 * @date 2021/10/19 14:19
 * @desc 统一结果返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 4921114729569667431L;

    //状态码，200为成功，其它为失败
    private Integer code;

    //消息提示
    private String message;

    //数据对象
    private T data;

    //成功状态码
    public static final int SUCCESS = 200;

    //失败状态码
    public static final int ERROR = 1000;

    public static <R> Response<R> success(R data) {
        return new Response<>(SUCCESS, "success", data);
    }

    public static <R> Response<R> error(String msg) {
        return new Response<>(ERROR, msg, null);
    }

    @JsonIgnore
    public boolean isOk() {
        return null != getCode() && SUCCESS == getCode();
    }

}
