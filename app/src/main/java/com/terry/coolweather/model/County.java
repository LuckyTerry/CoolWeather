package com.terry.coolweather.model;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class County {
    private int mCountyId;
    private String mCountyName;
    private String mCountyCode;
    private int mCityId;

    public int getCountyId() {
        return mCountyId;
    }

    public void setCountyId(int countyId) {
        mCountyId = countyId;
    }

    public String getCountyName() {
        return mCountyName;
    }

    public void setCountyName(String countyName) {
        mCountyName = countyName;
    }

    public String getCountyCode() {
        return mCountyCode;
    }

    public void setCountyCode(String countyCode) {
        mCountyCode = countyCode;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }
}
