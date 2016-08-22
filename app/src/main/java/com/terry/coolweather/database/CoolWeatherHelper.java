package com.terry.coolweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.terry.coolweather.constant.AppConstant;


/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class CoolWeatherHelper extends SQLiteOpenHelper{

    private static final String CREATE_PROVICE = "create table " + AppConstant.ProvinceDB.TABLE_NAME + "("
            + AppConstant.ProvinceDB.ID + " integer primary key autoincrement" + ","
            + AppConstant.ProvinceDB.NAME + " text" + ","
            + AppConstant.ProvinceDB.CODE + " text"
            + ")";
    private static final String CREATE_CITY = "create table " + AppConstant.CityDB.TABLE_NAME + "("
            + AppConstant.CityDB.ID + " integer primary key autoincrement" + ","
            + AppConstant.CityDB.NAME + " text" + ","
            + AppConstant.CityDB.CODE + " text" + ","
            + AppConstant.CityDB.ProvinceID + " text"
            + ")";
    private static final String CREATE_COUNTY = "create table " + AppConstant.CountyDB.TABLE_NAME + "("
            + AppConstant.CountyDB.ID + " integer primary key autoincrement" + ","
            + AppConstant.CountyDB.NAME + " text" + ","
            + AppConstant.CountyDB.CODE + " text" + ","
            + AppConstant.CountyDB.CityID + " text"
            + ")";

    public CoolWeatherHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVICE);
        sqLiteDatabase.execSQL(CREATE_CITY);
        sqLiteDatabase.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
