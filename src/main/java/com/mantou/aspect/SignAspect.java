package com.mantou.aspect;

import com.mantou.anno.SignProcess;
import com.mantou.entity.MySign;
import com.mantou.exception.MyException;
import com.mantou.utils.ClazzUtil;
import com.mantou.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedHashMap;

/**
 * @author mantou
 * @date 2021/10/29 18:59
 * @return
 * @desc 用于加签验签的切面
 */
@Component
@Aspect
@Slf4j
public class SignAspect {


    @Pointcut("@annotation(signProcess)")
    public void SignProecssPointCut(SignProcess signProcess){}

    @Value("${PUBLIC_KEY_STR}")
    private String PUBLIC_KEY_STR;

    //验证数字签名
    @Before(value = "SignProecssPointCut(signProcess)", argNames = "joinPoint,signProcess")
    public void doVerify(JoinPoint joinPoint,SignProcess signProcess) throws IOException {
        Object obj = joinPoint.getArgs()[0];
        //是否开启校验数字签名
        if (signProcess.verify()){
            if (obj instanceof MySign){
                //获取数字签名
                MySign mySign = (MySign) obj ;
                String signature = mySign.getSignature();
                log.info("signatureReq:{}",signature);
                byte[] signatureBytes = new BASE64Decoder().decodeBuffer(signature);
                log.info("obj:{}",obj);
                //获取obj中的数据
                LinkedHashMap<String, Object> data = ClazzUtil.getData(obj);
                log.info("dataReq:{}",data.toString());
                //验证签名
                boolean verify ;
                try {
                     verify = SignUtil.verify(data.toString(), signatureBytes, PUBLIC_KEY_STR);
                    if (!verify) throw new MyException("验签失败");
                } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }else{
                throw new MyException("请携带数字签名！");
            }

        }
    }

    @Value("${PRIVATE_KEY_STR}")
    private String PRIVATE_KEY_STR;

    //对响应数据加上数字签名
    @After(value = "SignProecssPointCut(signProcess)", argNames = "joinPoint,signProcess")
    public void doSign(JoinPoint joinPoint ,SignProcess signProcess){
        Object obj = joinPoint.getArgs()[0];
        if (signProcess.sign()){
            if (obj instanceof MySign){
                MySign mySign = (MySign) obj ;
                log.info("mySign:{}", mySign);
                LinkedHashMap<String, Object> data = ClazzUtil.getData(obj);
                log.info("dataRes:{}",data.toString());
                //添加数字签名
                try {
                    byte[] signatureBytes = SignUtil.sign(data.toString(), PRIVATE_KEY_STR);
                    String signature  = new BASE64Encoder().encode(signatureBytes);
                    log.info("signature:{}",signature);
                    mySign.setSignature(signature);
                } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
                    e.printStackTrace();
                }
            }else{
                throw new MyException("该类不能携带数字签名!");
            }
        }
    }
}
