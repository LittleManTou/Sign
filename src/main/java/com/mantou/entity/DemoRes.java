package com.mantou.entity;

import lombok.Data;

@Data
public class DemoRes extends DemoSign {
    Integer id ;
    String name ;
    Integer age ;
    String desc ;              //描述,将描述内容作为原始报文
}
