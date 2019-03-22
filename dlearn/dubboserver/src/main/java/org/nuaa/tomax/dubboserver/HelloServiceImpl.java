package org.nuaa.tomax.dubboserver;

import com.alibaba.dubbo.config.annotation.Service;
import org.nuaa.tomax.dlearn.service.HelloService;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/3/22 13:05
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
