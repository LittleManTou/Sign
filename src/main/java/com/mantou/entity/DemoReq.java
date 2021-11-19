package com.mantou.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DemoReq  extends MySign {
    Integer id ;
    String name ;
    Integer age ;
    String desc ;
}
