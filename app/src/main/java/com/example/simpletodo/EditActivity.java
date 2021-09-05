package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText edText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edText = findViewById(R.id.editTxt);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit the Item");

        edText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //When User is done editing, save button is clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an intent which will contain the results
                Intent intent = new Intent();

                //Pass the results of editing
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, edText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //Set the result of the intent
                setResult(RESULT_OK, intent);

                //Finish activity, close screen and go back
                finish();
            }
        });
    }
}