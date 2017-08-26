package com.coors.expenserealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;

public class AddExpense extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = AddExpense.class.getSimpleName();

    private EditText et_info;
    private EditText et_amount;
    private Button add_btn;
    private DatePicker datePicker;
    private Realm realm;
    private RealmHelper realmHelper;
    private Expense expense;
    private ImageView iv_del;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        realmInit();
        findViews();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getInt(Expense.MODE) == Expense.MODE_EDIT) {
            isEditMode = true;
            int exp_id = bundle.getInt(Expense.COL_ID);
            expense = realm.where(Expense.class).equalTo(Expense.COL_ID, exp_id).findFirst();
            iv_del.setVisibility(View.VISIBLE);
        }
        assignValues();
    }

    private void realmInit() {
        realm = RealmHelper.getInstance(this).getmRealm();
        realmHelper = RealmHelper.getInstance(this);

    }

    private void assignValues() {
        if (expense!=null){
            String[] d1 = expense.getDate().split("-");

            datePicker.updateDate(
                    Integer.parseInt(d1[0]),
                    Integer.parseInt(d1[1])-1,
                    Integer.parseInt(d1[2]));

            et_info.setText(expense.getInfo());
            et_amount.setText(expense.getAmount()+"");

        }else{

            Calendar c = Calendar.getInstance();
            datePicker.updateDate(
                    c.get(c.YEAR),
                    c.get(c.MONTH),
                    c.get(c.DAY_OF_MONTH));
        }
    }

    private void findViews() {
        et_info = (EditText) findViewById(R.id.et_info);
        et_amount = (EditText) findViewById(R.id.et_amount);
        add_btn = (Button) findViewById(R.id.add_btn);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        iv_del = (ImageView) findViewById(R.id.iv_del);
        iv_del.setVisibility(View.GONE);
        iv_del.setOnClickListener(this);
        add_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                final Expense add_expense = new Expense();

                // DatePicker
                StringBuilder date = new StringBuilder();
                date.append(datePicker.getYear() + "-");
                date.append((datePicker.getMonth() + 1) + "-");
                date.append(datePicker.getDayOfMonth());

                add_expense.setId(RealmHelper.getInstance(this).getNextKey());
                Log.d("getNext key", RealmHelper.getInstance(this).getNextKey() +"");
                add_expense.setDate(date.toString());
                add_expense.setInfo(et_info.getText().toString());
                add_expense.setAmount(Integer.parseInt(et_amount.getText().toString()));

                realmHelper.addExpense(add_expense);

                if (isEditMode) {
                    Toast.makeText(AddExpense.this, "edit success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddExpense.this, "insert success", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.iv_del:
                realmHelper.delExpense(expense.getId());
                Toast.makeText(AddExpense.this, "delete success", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
