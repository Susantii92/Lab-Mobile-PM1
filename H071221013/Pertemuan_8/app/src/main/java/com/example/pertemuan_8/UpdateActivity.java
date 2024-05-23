package com.example.pertemuan_8;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateActivity extends AppCompatActivity {
    EditText et_title, et_desc;
    Button btn_update;
    ImageView iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);
        btn_update = findViewById(R.id.btn_update);
        iv_delete = findViewById(R.id.iv_delete);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(v -> showCancelDialog());
        }

        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");

        et_title.setText(title);
        et_desc.setText(desc);

        btn_update.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(UpdateActivity.this);
            databaseHelper.updateTask(id, et_title.getText().toString(), et_desc.getText().toString());
            setResult(RESULT_OK);
            finish();
        });

        iv_delete.setOnClickListener(v ->
                showDeleteDialog(id));
    }

    @Override
    public void onBackPressed() {

        showCancelDialog();
    }

    private void showDeleteDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.card_delete, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm_delete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(UpdateActivity.this);
            databaseHelper.deleteTask(id);
            setResult(RESULT_OK, new Intent().putExtra("delete", true));
            dialog.dismiss();
            finish();
        });
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.card_update, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnCancelNo = dialogView.findViewById(R.id.btn_cancel_no);
        Button btnCancelYes = dialogView.findViewById(R.id.btn_cancel_yes);

        btnCancelNo.setOnClickListener(v -> dialog.dismiss());

        btnCancelYes.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }
}
