package com.terry.coolweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.terry.coolweather.constant.AppConstant;
import com.terry.coolweather.model.City;
import com.terry.coolweather.model.County;
import com.terry.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class CoolWeatherDB {
    private static final String DATABASE_NAME = "cool_weather";
    private static final int DATABASE_VERSION = 1;

    private static CoolWeatherDB mCoolWeatherDB;
    private CoolWeatherHelper mCoolWeatherHelper;
    private SQLiteDatabase mCoolWeatherDatabase;

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (mCoolWeatherDB == null) {
            mCoolWeatherDB = new CoolWeatherDB(context);
        }
        return mCoolWeatherDB;
    }

    private CoolWeatherDB(Context context) {
        mCoolWeatherHelper = new CoolWeatherHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCoolWeatherDatabase = mCoolWeatherHelper.getWritableDatabase();
    }

    public void saveProvince(Province province) {
        if(province != null){
            ContentValues values = new ContentValues();
            values.put(AppConstant.ProvinceDB.NAME, province.getProvinceName());
            values.put(AppConstant.ProvinceDB.CODE, province.getProvinceCode());
            mCoolWeatherDatabase.insert(AppConstant.ProvinceDB.TABLE_NAME, null, values);
        }
    }

    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<>();

        Cursor cursor = mCoolWeatherDatabase.query(AppConstant.ProvinceDB.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProvinceId(cursor.getInt(cursor.getColumnIndex(AppConstant.ProvinceDB.ID)));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex(AppConstant.ProvinceDB.NAME)));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex(AppConstant.ProvinceDB.CODE)));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        return provinceList;
    }

    public void saveCity(City city) {
        if(city != null){
            ContentValues values = new ContentValues();
            values.put(AppConstant.CityDB.NAME, city.getCityName());
            values.put(AppConstant.CityDB.CODE, city.getCityCode());
            values.put(AppConstant.CityDB.ProvinceID, city.getProvinceId());
            mCoolWeatherDatabase.insert(AppConstant.CityDB.TABLE_NAME, null, values);
        }
    }

    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<>();

        Cursor cursor = mCoolWeatherDatabase.query(AppConstant.CityDB.TABLE_NAME, null, AppConstant.CityDB.ProvinceID + " = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityId(cursor.getInt(cursor.getColumnIndex(AppConstant.CityDB.ID)));
                city.setCityName(cursor.getString(cursor.getColumnIndex(AppConstant.CityDB.NAME)));
                city.setCityCode(cursor.getString(cursor.getColumnIndex(AppConstant.CityDB.CODE)));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex(AppConstant.CityDB.ProvinceID)));
                cityList.add(city);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cityList;
    }

    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put(AppConstant.CountyDB.NAME, county.getCountyName());
            values.put(AppConstant.CountyDB.CODE, county.getCountyCode());
            values.put(AppConstant.CountyDB.CityID, county.getCityId());
            mCoolWeatherDatabase.insert(AppConstant.CountyDB.TABLE_NAME, null, values);
        }
    }

    public List<County> loadCounties(int cityId) {
        List<County> countyList = new ArrayList<>();

        Cursor cursor = mCoolWeatherDatabase.query(AppConstant.CountyDB.TABLE_NAME, null, AppConstant.CountyDB.CityID + " = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setCountyId(cursor.getInt(cursor.getColumnIndex(AppConstant.CountyDB.ID)));
                county.setCountyName(cursor.getString(cursor.getColumnIndex(AppConstant.CountyDB.NAME)));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex(AppConstant.CountyDB.CODE)));
                county.setCityId(cursor.getInt(cursor.getColumnIndex(AppConstant.CountyDB.CityID)));
                countyList.add(county);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return countyList;
    }
}
