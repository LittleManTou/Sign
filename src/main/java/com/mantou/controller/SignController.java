package com.mantou.controller;

import com.mantou.anno.SignProcess;
import com.mantou.entity.DemoRes;
import com.mantou.entity.DemoSign;
import com.mantou.entity.Response;
import com.mantou.entity.DemoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class SignController {
    @SignProcess
    @PostMapping("/verify")

    public Response verify(@RequestBody Map data){
        //log.info("signatureByte:{}",data.get("signatureByte"));
        data.put("name","zs");
        return Response.success(data);
    }
}
