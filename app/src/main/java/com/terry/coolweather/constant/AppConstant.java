package com.terry.coolweather.constant;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class AppConstant {

    public static final class ProvinceDB{
        public static final String TABLE_NAME = "province";
        public static final String ID = "_id";
        public static final String NAME = "province_name";
        public static final String CODE = "province_code";
    }
    public static final class CityDB{
        public static final String TABLE_NAME = "city";
        public static final String ID = "_id";
        public static final String NAME = "city_name";
        public static final String CODE = "city_code";
        public static final String ProvinceID = "province_id";
    }
    public static final class CountyDB{
        public static final String TABLE_NAME = "county";
        public static final String ID = "_id";
        public static final String NAME = "county_name";
        public static final String CODE = "county_code";
        public static final String CityID = "city_id";
    }
    public static final class WeatherInfo{
        public static final String CITY_SELECTED = "city_selected";
        public static final String WENDU = "wendu";
        public static final String GANMAO = "ganmao";
        public static final String FENG_XIANG = "fengxiang";
        public static final String FENG_LI = "fengli";
        public static final String HIGH = "high";
        public static final String TYPE = "type";
        public static final String LOW = "low";
        public static final String DATE = "date";
        public static final String CITY = "city";
    }
    public static final String IS_FROM_WEATHER_ACTIVITY = "from_weather_activity";
}
