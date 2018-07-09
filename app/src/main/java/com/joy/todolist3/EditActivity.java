package com.joy.todolist3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    public DbAdapter dbAdapter;
    EditText edt_name, edt_alarm;
    Button btn_alarm,btn_ok,btn_back;
    TextView txtTitle;
    public String new_name, new_color, new_alarm;
    public Bundle bData;
    public int index;

    private Button dateButton;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private TextView dateText;
    private DatePickerDialog datePickerDialog;
    Spinner spinner;
    ArrayAdapter<CharSequence> data;
    ArrayList<ColorItem> colorItems;
    SpinnerAdapter spinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //spinner 選色
        spinner = findViewById(R.id.etd_color);
        colorItems = new ArrayList<ColorItem> ();
        colorItems.add(new ColorItem("Red","#ff0000"));
        colorItems.add(new ColorItem("Green","#00c7a4"));
        colorItems.add(new ColorItem("Blue","#4b7bd8"));
        colorItems.add(new ColorItem("DarkViolet","#9400D3"));
        spinnerAdapter = new SpinnerAdapter(this,colorItems);
        spinner.setAdapter(spinnerAdapter);


        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        dateText = (TextView)findViewById(R.id.edt_alarm);
        btn_alarm = (Button)findViewById(R.id.btn_alarm);
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
                datePickerDialog.updateDate(mYear, mMonth, mDay);
            }
        });

        initView();
        bData = this.getIntent().getExtras();
        dbAdapter = new DbAdapter(this);

        if(bData.getString("type").equals("edit")){
            txtTitle.setText("編輯聯絡人");
     //         Log.i("DB_DbAdapter_queryByName",item_id);
            Cursor cursor = dbAdapter.queryById(bData.getInt("item_id"));
            index = cursor.getInt(0);
            edt_name.setText(cursor.getString(1));
            edt_alarm.setText(cursor.getString(2));
        }
        //按鈕行為設定
        edt_alarm.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.del_menu,menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                AlertDialog dialog = null;
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("警告")
                        .setMessage(" 請確認是否刪除?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbAdapter.deleteContacts(index);
                                Intent i = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void initView(){
        txtTitle = findViewById(R.id.txtTitle);
        edt_name = findViewById(R.id.edtName);
        //etd_color = findViewById(R.id.etd_color);
        edt_alarm = findViewById(R.id.edt_alarm);
        btn_ok = findViewById(R.id.btn_ok);
        btn_back = findViewById(R.id.btn_back);
        edt_alarm.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                dateText.setText(setDateFormat(year,month,day));
            }
        }, mYear,mMonth, mDay);
        return datePickerDialog;
    }
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                new_name = edt_name.getText().toString();
                //new_color = etd_color.getText().toString();
                new_alarm = edt_alarm.getText().toString();

                dbAdapter = new DbAdapter(EditActivity.this);
                if(bData.getString("type").equals("add")){
                    try{
                        //dbAdapter.createContacts(new_name, "Green", "1980/02/16");
                        dbAdapter.createContacts(new_name,  new_alarm);
                        //dbAdapter.createContacts(new_name, new_color, new_alarm);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        //回到列表
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                }else{
                    try{
                        Log.i("DB_EditActivity","onClick1");
                        dbAdapter.updateContacts(index, new_name, new_alarm);
                        Log.i("DB_EditActivity","onClick2");
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally {
                        Log.i("DB_EditActivity","onClick3");
                        Intent i = new Intent(this, ShowActivity.class);
                        i.putExtra("item_id",index);
                        startActivity(i);
                    }
                }
                break;
            case R.id.btn_back:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}
