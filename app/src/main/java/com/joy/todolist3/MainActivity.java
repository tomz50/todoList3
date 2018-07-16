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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ListView listData;
    TextView noContact;
    private DbAdapter dbAdapter;
    private Intent intent;
    private ListAdapter dataAdapter;
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
        dbAdapter = new DbAdapter(this);
        Log.i("dbCount=",String.valueOf(dbAdapter.listContacts().getCount()));
        noContact = findViewById(R.id.noContact);
        listData = findViewById(R.id.listData);
        //判斷目前是否有資料並設定顯示元件，如果是0，就顯示「目前無資料」
        if(dbAdapter.listContacts().getCount() == 0){
            listData.setVisibility(View.INVISIBLE);
            noContact.setVisibility(View.VISIBLE);
        }else{
            listData.setVisibility(View.VISIBLE);
            noContact.setVisibility(View.INVISIBLE);
        }
        displaylistView();
    }

    private void displaylistView(){
        Cursor cursor = dbAdapter.listContacts ();
        dataAdapter = new ListAdapter(this, cursor);
        listData.setAdapter(dataAdapter);
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor current_cursor = (Cursor) listData.getItemAtPosition(position);
                int item_id = current_cursor.getInt(current_cursor.getColumnIndexOrThrow("_id"));
                intent = new Intent();
                intent.putExtra("item_id", item_id);
                intent.putExtra("type","edit");
                intent.setClass(MainActivity.this, EditActivity.class);
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
