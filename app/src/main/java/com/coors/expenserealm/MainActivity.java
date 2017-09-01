package com.coors.expenserealm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coors.expenserealm.app.App;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements ExpenseRecyclerViewAdapter.OnRecyclerViewItemClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private RealmHelper realmHelper;
    private ExpenseRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RealmResults<Expense> expenses;
    private Realm realm;
    private RealmChangeListener<RealmResults<Expense>> realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化 Realm database
        init();
        //初始化UI元件
        setUpRecyclerViiew();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddExpense.class));
            }
        });
    }

    private void setUpRecyclerViiew() {
        realm = Realm.getDefaultInstance();
        expenses = realm.where(Expense.class).findAll().sort(Expense.COL_ID, Sort.DESCENDING);
        Log.d("query size : ", expenses.size() + "");
        adapter = new ExpenseRecyclerViewAdapter(expenses);
        adapter.setOnRecyclerViewItemClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // 幫Realm資料庫設置改變監聽器
        realmChangeListener = new RealmChangeListener<RealmResults<Expense>>() {
            @Override
            public void onChange(RealmResults<Expense> element) {
                adapter.notifyDataSetChanged();
            }
        };
        expenses.addChangeListener(realmChangeListener);
    }

    private void init() {
        realmHelper = ((App)getApplication()).getRealmHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try {
                // 直接讀取json格式，指定Model格式，存入Realm database中
                final InputStream is = getAssets().open("exp.json");
                realmHelper.getmRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realmHelper.getmRealm().createOrUpdateAllFromJson(Expense.class, is);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickEdit(Expense expense) {
        Log.d("click", expense.getId() + "");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setClass(this, AddExpense.class);
        bundle.putInt(Expense.MODE, Expense.MODE_EDIT);
        bundle.putInt(Expense.COL_ID, expense.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void clickDelete(Expense expense) {
        Log.d("click", expense.getId() + "");
        realmHelper.delExpense(expense.getId());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        realm.close();
        expenses.removeChangeListener(realmChangeListener);
    }
}
