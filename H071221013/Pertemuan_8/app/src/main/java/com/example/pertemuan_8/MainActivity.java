package com.example.pertemuan_8;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    FloatingActionButton btn_add;
    DatabaseHelper databaseHelper;
    ArrayList<Note> notes;
    AdapterHome adapter;
    TextView noDataText;
    SearchView searchView;
    //ini untuk mengidentifikasi jenis permintaan aktivitas
    static final int REQUEST_CODE_ADD = 1;
    static final int REQUEST_CODE_UPDATE = 2;
    static final int REQUEST_CODE_DELETE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_home);
        btn_add = findViewById(R.id.btn_add);
        noDataText = findViewById(R.id.no_data_text);
        searchView = findViewById(R.id.searchView);

        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });


        databaseHelper = new DatabaseHelper(MainActivity.this);

        notes = new ArrayList<>();

        adapter = new AdapterHome(MainActivity.this, notes);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchData(newText.trim());
                return true;
            }
        });

        storeData(); //memnaggil metodenya
    }

    void storeData() {
        Cursor cursor = databaseHelper.readAllData();
        notes.clear();
        if (cursor.getCount() == 0) {
            rv.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)); //mengambil nilai kolom id dalam cursor
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
                long createdTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_TIMESTAMP));
                long updatedTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_TIMESTAMP));
                notes.add(new Note(id, title, desc, createdTimestamp, updatedTimestamp)); //membuat objek note baru
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            storeData();
        } else if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
            storeData();
        } else if (requestCode == REQUEST_CODE_DELETE && resultCode == RESULT_OK) {
            storeData();
        }
    }

    void searchData(String q){
        if (q.isEmpty()) {
            storeData();
            return;
        }
        ArrayList<Note> searchedNotes = databaseHelper.searchNotes(q.trim());
        if (searchedNotes.isEmpty()) {
            notes.clear();
            adapter.notifyDataSetChanged();
            rv.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            notes.clear();
            notes.addAll(searchedNotes);
            adapter.notifyDataSetChanged();
            rv.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);
        }
    }
}
