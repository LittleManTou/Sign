package com.mantou.advice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mantou.anno.SignProcess;
import com.mantou.exception.MyException;
import com.mantou.utils.JsonUtil;
import com.mantou.utils.SignUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedHashMap;

@Slf4j
@ControllerAdvice
public class MyRequestBodyAdvice implements RequestBodyAdvice {

    @Value("${PUBLIC_KEY_STR}")
    private String PUBLIC_KEY_STR;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        SignProcess signProcess = methodParameter.getMethodAnnotation(SignProcess.class);
        return null != signProcess && signProcess.verify();
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        HttpHeaders headers = httpInputMessage.getHeaders();
        //源请求参数
        String bodyStr = StreamUtils.copyToString(httpInputMessage.getBody(), StandardCharsets.UTF_8);
        //转换成TreeMap结构
        LinkedHashMap<String,Object> map = JsonUtil.parse(bodyStr, new TypeReference<LinkedHashMap<String, Object>>() {});
        //将编码的数字签名取出
        String signature = (String)map.get("signature");
        log.info("请求携带的数字签名:{}",signature);
        //解码后的数字签名
        byte[] signatureByte = new BASE64Decoder().decodeBuffer(signature);
        //将数字签名去掉
        map.remove("signature");
        log.info("map:{}",map);
        //校验签名
        boolean verifyResult;
        try {
            verifyResult = SignUtil.verify(map.toString(),signatureByte, PUBLIC_KEY_STR);
            if (!verifyResult) throw new MyException("验签失败");
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        String outStr = JsonUtil.toStr(map);
        return new MyHttpInputMessage(headers, outStr.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @AllArgsConstructor
    public static class MyHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders headers;

        private final byte[] body;

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

}
