package com.mantou.advice;

import com.fasterxml.jackson.databind.JsonNode;
import com.mantou.anno.SignProcess;
import com.mantou.entity.DemoSign;
import com.mantou.entity.Response;
import com.mantou.utils.JsonUtil;
import com.mantou.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@ControllerAdvice
@Slf4j
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    @Value("${PRIVATE_KEY_STR}")
    private String PRIVATE_KEY_STR;
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        SignProcess signProcess = returnType.getMethodAnnotation(SignProcess.class);
        return null != signProcess && signProcess.sign();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //如果是rest接口统一封装返回对象
        if (body instanceof Response) {
            Response res = (Response) body;
            //如果返回成功
            if (res.isOk()) {
                Object data = res.getData();
                if (null != data) {
                    JsonNode json = JsonUtil.beanToNode(data);
                    //仅处理object类型
                    if (json.isObject()) {
                        TreeMap<String, Object> map = new TreeMap<>();
                        Iterator<Map.Entry<String, JsonNode>> fields = json.fields();
                        while(fields.hasNext()){
                            Map.Entry<String, JsonNode> entry = fields.next();
                            if (entry.getKey() != "signatureByte")
                            map.put(entry.getKey(), JsonUtil.toStr(entry.getValue()));
                        }
                        log.info("map:{}",map.toString());
                        //加签
                        byte[] signByte = null;
                        try {
                            signByte = SignUtil.sign(map.toString(), PRIVATE_KEY_STR);
                            log.info("sign:{}", signByte);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }
                        int[] signInt = new int[signByte.length];
                        for (int i = 0; i < signByte.length; i++) {
                            signInt[i] = signByte[i];
                        }
                        //TODO
                        map.put("signatureByte",signInt);
                        return Response.success(map);
                    }

                }
            }
        }
        return body;
    }
}
