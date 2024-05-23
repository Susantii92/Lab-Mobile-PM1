package com.example.pertemuan_8;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddActivity extends AppCompatActivity {
    EditText et_title, et_desc;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);
        btn_submit = findViewById(R.id.btn_submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(v ->
                    showCancelDialog());
        }

        btn_submit.setOnClickListener(v -> {
            String title = et_title.getText().toString().trim();
            String desc = et_desc.getText().toString().trim();

            if (title.isEmpty()) {
                et_title.setError("Title tidak boleh kosong");
                et_title.requestFocus();
            } else if (desc.isEmpty()) {
                et_desc.setError("Description tidak boleh kosong");
                et_desc.requestFocus();
            } else {
                DatabaseHelper databaseHelper = new DatabaseHelper(AddActivity.this);
                databaseHelper.addTask(title, desc);

                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        showCancelDialog();
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.card_add, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnCancelNo = dialogView.findViewById(R.id.btn_cancel_add_no);
        Button btnCancelYes = dialogView.findViewById(R.id.btn_cancel_add_yes);

        btnCancelNo.setOnClickListener(v -> dialog.dismiss());

        btnCancelYes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }
}
