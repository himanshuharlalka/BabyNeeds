package com.example.babyneeds.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.ListActivity;
import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.name.setText(String.format("Item: %s", item.getItemName()));
        holder.quantity.setText(MessageFormat.format("Quantity: {0}", String.valueOf(item.getItemQuantity())));
        holder.color.setText(String.format("Color: %s", item.getItemColor()));
        holder.size.setText(String.format("Size: %s", item.getItemSize()));
        holder.date.setText(String.format("Added on: %s", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView quantity;
        public TextView color;
        public TextView size;
        public TextView date;
        public Button editItem;
        public Button deleteItem;
        public int id;
        private Button saveButton;
        private EditText enterItem;
        private EditText itemQuantity;
        private EditText itemColor;
        private EditText itemSize;
        private TextView title;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            name=itemView.findViewById(R.id.item_name);
            quantity=itemView.findViewById(R.id.item_quantity);
            color=itemView.findViewById(R.id.item_color);
            size=itemView.findViewById(R.id.item_size);
            date=itemView.findViewById(R.id.item_date);
            editItem=itemView.findViewById(R.id.edit_button);
            deleteItem=itemView.findViewById(R.id.delete_button);
            editItem.setOnClickListener(this);
            deleteItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.edit_button:
                    editItem(itemList.get(getAdapterPosition()));
                    break;
                case R.id.delete_button:
                    id=getAdapterPosition();
                    deleteItem(id);

                    break;
            }
        }
        private void deleteItem(final int id) {
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confirmation_pop,null);

            final Button noButton=view.findViewById(R.id.no_button);
            Button yesButton=view.findViewById(R.id.yes_button);

            builder.setView(view);
            dialog=builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db=new DatabaseHandler(context);
                    db.deleteItem(itemList.get(id));
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
        }
        private void editItem(final Item newItem) {
         //   Item item=itemList.get(getAdapterPosition());
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.pop_up,null);

            enterItem=view.findViewById(R.id.babyItem);
            itemColor=view.findViewById(R.id.itemColor);
            itemQuantity=view.findViewById(R.id.itemQuantity);
            itemSize=view.findViewById(R.id.itemSize);
            saveButton=view.findViewById(R.id.saveButton);

            saveButton.setText(R.string.update_text);
            title=view.findViewById(R.id.title);
            title.setText(R.string.edit_item);

            enterItem.setText(newItem.getItemName());
            itemColor.setText(newItem.getItemColor());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemSize.setText(newItem.getItemSize());

            builder.setView(view);
            dialog=builder.create();
            dialog.show();
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db=new DatabaseHandler(context);
                    if(!enterItem.getText().toString().isEmpty()
                            &&!itemColor.getText().toString().isEmpty()
                            &&!itemQuantity.getText().toString().isEmpty()
                            &&!itemSize.getText().toString().isEmpty()) {
                        newItem.setItemName(enterItem.getText().toString().trim());
                        newItem.setItemQuantity(Long.parseLong(itemQuantity.getText().toString().trim()));
                        newItem.setItemColor(itemColor.getText().toString().trim());
                        newItem.setItemSize(itemSize.getText().toString().trim());
                        db.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(),newItem);
                    } else{
                        Snackbar.make(view,"Empty Fields not allowed.",Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

        }
    }




}
