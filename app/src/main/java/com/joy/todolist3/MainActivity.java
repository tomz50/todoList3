package com.joy.todolist3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listData;
    private DbAdapter dbAdapter;
    private SimpleCursorAdapter dataAdapter;
    private Intent intent;
    private ImageButton edit;
    private ArrayAdapter<String> arrayList;
    private Context mContext;
    private final static String TAG = "StartBroadcast";
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaylistView();


    }


    private void displaylistView(){
        listData = findViewById(R.id.listData);
        listData.setEmptyView(findViewById(R.id.noContact));
        dbAdapter = new DbAdapter(this);
        Cursor cursor = dbAdapter.listContacts();
        String[] dataColumns = new String[]{
                dbAdapter.KEY_NAME,
                dbAdapter.KEY_ALARM
        };
        int[] viewColumns = new int[]{
                R.id.txtName,
                R.id.txtAlarm
        };

        //取得點下去當下的資料列
        dataAdapter = new SimpleCursorAdapter(this,R.layout.item_view, cursor, dataColumns, viewColumns, 0);
        listData.setAdapter(dataAdapter);
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item_cursor = (Cursor) listData.getItemAtPosition(position);
//                String item_id = item_cursor.getString(item_cursor.getColumnIndexOrThrow("_id"));
                int item_id = item_cursor.getInt(item_cursor.getColumnIndexOrThrow("_id"));
                Log.i("DBitem_id=",Integer.toString(item_id));
                intent = new Intent();
                intent.putExtra("item_id",item_id);
                intent.setClass(MainActivity.this, ShowActivity.class );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                intent = new Intent();
                intent.putExtra("type","add");
                intent.setClass(MainActivity.this, EditActivity.class );
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
