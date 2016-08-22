package com.terry.coolweather.model;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class Province {
    private int mProvinceId;
    private String mProvinceName;
    private String mProvinceCode;

    public int getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(int provinceId) {
        mProvinceId = provinceId;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    public void setProvinceName(String provinceName) {
        mProvinceName = provinceName;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        mProvinceCode = provinceCode;
    }
}
