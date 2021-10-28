package com.mantou.entity;

import lombok.Data;

@Data
public class DemoReq {
    Integer id ;
    String name ;
    Integer age ;
    String desc ;              //描述,将描述内容作为原始报文
    String signature ;      //数字签名
}
