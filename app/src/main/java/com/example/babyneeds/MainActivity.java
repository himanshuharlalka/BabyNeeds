package com.example.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.babyneeds.adapter.RecyclerViewAdapter;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText enterItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    public DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler=new DatabaseHandler(this);
        byPassActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        List<Item> itemList=databaseHandler.getAllItems();

//        for(Item item:itemList){
//            Log.d("date", "onCreate: "+item.getDateItemAdded());
//        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {
        if(databaseHandler.getCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

    private void saveItem(View view){
        Item item=new Item();
        item.setItemName(enterItem.getText().toString().trim());
        item.setItemQuantity(Long.parseLong(itemQuantity.getText().toString().trim()));
        item.setItemColor(itemColor.getText().toString().trim());
        item.setItemSize(itemSize.getText().toString().trim());
        databaseHandler.addItem(item);
        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },1200);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this,ListActivity.class));


            }
        },1200);

    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.pop_up,null);
        enterItem=view.findViewById(R.id.babyItem);
        itemColor=view.findViewById(R.id.itemColor);
        itemQuantity=view.findViewById(R.id.itemQuantity);
        itemSize=view.findViewById(R.id.itemSize);
        saveButton=view.findViewById(R.id.saveButton);
        builder.setView(view);
        dialog=builder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!enterItem.getText().toString().isEmpty()
                &&!itemColor.getText().toString().isEmpty()
                &&!itemQuantity.getText().toString().isEmpty()
                &&!itemSize.getText().toString().isEmpty()) {
                    saveItem(view);
                } else{
                    Snackbar.make(view,"Empty Fields not allowed.",Snackbar.LENGTH_SHORT).show();
                }
            }
        });



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
}