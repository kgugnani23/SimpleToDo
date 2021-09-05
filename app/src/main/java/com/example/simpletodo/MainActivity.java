package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button addButton;
    EditText editTxt;
    RecyclerView recViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();

        addButton = findViewById(R.id.addButton);
        editTxt = findViewById(R.id.editTxt);
        recViewItems = findViewById(R.id.recViewItems);

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);

                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);

                //Give user feedback for removing
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Create the new Edit Activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //Pass the data to the Edit Activity
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                //Display the Edit Activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        recViewItems.setAdapter(itemsAdapter);
        recViewItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTxt.getText().toString();


                    //Add the item to the model
                    items.add(todoItem);

                    //Notify adapter that an item was inserted
                    itemsAdapter.notifyItemInserted(items.size() - 1);

                    //Clear the text input field
                    editTxt.setText("");

                    //Give user feedback for adding
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();

                    saveItems();

            }
        });
    }

    //Handle the result of edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //Extract the original position of the edited item from the key position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with the new item text
            items.set(position, itemText);

            //Notify the adapter
            itemsAdapter.notifyItemChanged(position);

            //Persist the changes
            saveItems();

            //Give user feedback for saving
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();


        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //Function to load items by reading every line of the data file.
    private void loadItems() {

        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //Function saves items by writing them to the data file.
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}