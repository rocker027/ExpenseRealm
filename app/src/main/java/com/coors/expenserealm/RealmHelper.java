package com.coors.expenserealm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by z8v on 2017/8/26.
 */

public class RealmHelper {
    private static RealmHelper instance;
    private Realm mRealm;
    private Context mContext;

    public RealmHelper(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Expense.TABLE_NAME)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        mRealm = Realm.getInstance(realmConfiguration);
    }

    public static RealmHelper getInstance(Context context) {
        if (instance ==null) {
            instance = new RealmHelper(context);
        }
        return instance;
    }

    public Realm getmRealm() {
        return mRealm;
    }

    public void addExpense(final Expense expense) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(expense);
            }
        });
    }

    public int getNextKey() {
        // PK id 自增主鍵
        Number max_id = mRealm.where(Expense.class).max(Expense.COL_ID);
        Integer id = null;
        if (max_id == null) {
            id = (0);
        } else {
            id = Integer.parseInt(max_id.toString()) + 1;
        }
        return id;
    }

    public void delExpense(final int id) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Expense exp = realm.where(Expense.class).equalTo(Expense.COL_ID, id).findFirst();
                exp.deleteFromRealm();
            }
        });
    }
}
