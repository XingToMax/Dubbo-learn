package org.nuaa.tomax.dubboclient;

import com.alibaba.dubbo.config.annotation.Reference;
import org.nuaa.tomax.dlearn.domain.City;
import org.nuaa.tomax.dlearn.service.CityDubboService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/3/22 11:28
 */
@RestController
@CrossOrigin
@RequestMapping
public class CityController {
    @Reference(version = "1.0.0")
    private CityDubboService cityDubboService;

    @GetMapping("/city/{name}")
    public City getCity(@PathVariable(name = "name") String name) {
        return cityDubboService.findCityByName(name);
    }
}
