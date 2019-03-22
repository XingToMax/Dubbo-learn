package org.nuaa.tomax.dubboclient;

import com.alibaba.dubbo.config.annotation.Reference;
import org.nuaa.tomax.dlearn.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/3/22 13:10
 */
@RestController
@RequestMapping
public class HelloController {
    @Reference
    private HelloService helloService;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable(name = "name") String name) {
        return helloService.sayHello(name);
    }
}
