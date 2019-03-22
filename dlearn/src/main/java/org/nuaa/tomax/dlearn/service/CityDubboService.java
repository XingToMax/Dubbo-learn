package org.nuaa.tomax.dlearn.service;

import org.nuaa.tomax.dlearn.domain.City;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/3/22 11:20
 */
public interface CityDubboService {
    /**
     * 根据城市名称，查询城市信息
     * @param cityName city name
     * @return city domain
     */
    City findCityByName(String cityName);
}
