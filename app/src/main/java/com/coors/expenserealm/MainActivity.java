package com.coors.expenserealm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements ExpenseRecyclerViewAdapter.OnRecyclerViewItemClickListener{
    public static final String TAG = MainActivity.class.getSimpleName();
    private RealmHelper realmHelper;
    private ExpenseRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        findViews();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddExpense.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        query();
    }

    private void findViews() {
        RealmResults<Expense> query_all = realmHelper.getmRealm().where(Expense.class).findAll();
        query_all.addChangeListener(new RealmChangeListener<RealmResults<Expense>>() {
            @Override
            public void onChange(RealmResults<Expense> element) {
                adapter.notifyDataSetChanged();

            }
        });
        adapter = new ExpenseRecyclerViewAdapter(realmHelper.getmRealm().where(Expense.class).findAll());
        adapter.setOnRecyclerViewItemClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

//    private void query() {
//        adapter.notifyDataSetChanged();
//    }

    private void init() {
        Realm.init(this);
        realmHelper = RealmHelper.getInstance(this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, Expense expense) {
        Log.d("click", expense.getId()+"");
        realmHelper.delExpense(expense.getId());

//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        intent.setClass(this, AddExpense.class);
//        bundle.putInt(Expense.MODE,Expense.MODE_EDIT);
//        bundle.putInt(Expense.COL_ID,expense.getId());
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
    }
}
