package org.nuaa.tomax.dubboserver;

import com.alibaba.dubbo.config.annotation.Service;
import org.nuaa.tomax.dlearn.domain.City;
import org.nuaa.tomax.dlearn.service.CityDubboService;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/3/22 11:25
 */
@Service(version = "1.0.0")
public class CityDubboServiceImpl implements CityDubboService{
    @Override
    public City findCityByName(String cityName) {
        System.out.println("visit : " + cityName);
        return new City(1L, 2L, cityName, "my home");
    }
}
