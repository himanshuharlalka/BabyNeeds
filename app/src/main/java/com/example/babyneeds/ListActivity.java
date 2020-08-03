package com.example.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.adapter.RecyclerViewAdapter;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public DatabaseHandler db;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText enterItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();
        fab=findViewById(R.id.fab1);
        db=new DatabaseHandler(this);
        itemList=db.getAllItems();
        for(Item item:itemList){
            Log.d("D", "onCreate: "+item.getItemName());
        }
        recyclerViewAdapter=new RecyclerViewAdapter(ListActivity.this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopUpDialog();
            }
        });
    }

    private void createPopUpDialog() {
        builder=new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.pop_up,null);
        enterItem=view.findViewById(R.id.babyItem);
        itemColor=view.findViewById(R.id.itemColor);
        itemQuantity=view.findViewById(R.id.itemQuantity);
        itemSize=view.findViewById(R.id.itemSize);
        saveButton=view.findViewById(R.id.saveButton);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
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

    private void saveItem(View view) {
        Item item=new Item();
        item.setItemName(enterItem.getText().toString().trim());
        item.setItemQuantity(Long.parseLong(itemQuantity.getText().toString().trim()));
        item.setItemColor(itemColor.getText().toString().trim());
        item.setItemSize(itemSize.getText().toString().trim());
        db.addItem(item);
        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT).show();
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();

                startActivity(new Intent(ListActivity.this,ListActivity.class));
                finish();

            }
        },1200);
    }
}