package com.coors.expenserealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by z8v on 2017/8/26.
 */
public class Expense extends RealmObject {
    public static final String TABLE_NAME = "expense";
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_INFO = "info";
    public static final String COL_AMOUNT = "amount";
    public static final String MODE = "select_mode";
    public static final int MODE_EDIT = 100;
    public static final int MODE_ADD = 100;
    //    private static AtomicInteger auto_key = new AtomicInteger(0);

    @PrimaryKey
    private int id;
    private String date;
    private String info;
    private int amount;

    public int getId() {
        return id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id =id;
    }


//    public int getNextKey() {
//        return auto_key.getAndIncrement();
//    }
}
