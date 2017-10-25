package com.fxlc.truckadmin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fxlc.truckadmin.bean.Truck;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by cyd on 2017/7/19.
 */

public class CarDao {
     MySqliteHelper helper;
     SQLiteDatabase db;

    public CarDao(Context context) {
        helper = new MySqliteHelper(context);
    }

    public  void readExcel(InputStream is) {
        db = helper.getWritableDatabase();
        try {

            Workbook book = Workbook.getWorkbook(is);
            int num = book.getNumberOfSheets();

            Sheet sheet = book.getSheet(0);

            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            // getCell(Col,Row)获得单元格的值
            for (int i = 1; i < rows; ++i) {
                Truck truck = new Truck();
                truck.setBrand(sheet.getCell(0, i).getContents());
                Log.d("excel", truck.getBrand());
                truck.setStyle(sheet.getCell(1, i).getContents());
                truck.setDrive(sheet.getCell(4, i).getContents());
                truck.setSoup(sheet.getCell(2, i).getContents());
                saveTruck(truck);

            }
            book.close();
            db.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void saveTruck(Truck truck) {

        String sql = "insert into truck(brand,style,drive,soup) values (?,?,?,?)";
        db.execSQL(sql, new String[]{truck.getBrand(), truck.getStyle(), truck.getDrive(), truck.getSoup()});

    }

    public  boolean hasData() {
        db = helper.getWritableDatabase();

        String sql = "select count(*) c from truck";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {
             if (cursor.getInt(cursor.getColumnIndex("c")) > 0)
                 return true;
        }
        return false;

//        while (cursor.moveToNext()) {
//            String brand = cursor.getString(1);
//            String style = cursor.getString(2);
//            String drive = cursor.getString(3);
//            String soup = cursor.getString(4);
//            Log.d("database", brand + "-" + style + "-" + drive + "-" + soup);

//        }


    }
}
