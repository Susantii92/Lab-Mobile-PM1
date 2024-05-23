package com.example.pertemuan_8;

import static com.example.pertemuan_8.MainActivity.REQUEST_CODE_UPDATE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    Context context;
    ArrayList<Note> notes;

    public AdapterHome(Context context, ArrayList<Note> notes) {
        this.context = context;
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return Long.compare(note1.getCreatedTimestamp(), note2.getCreatedTimestamp());
            }
        });
        this.notes = notes;
    }


    @NonNull
    @Override
    public AdapterHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHome.ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_desc.setText(note.getDesc());
        String formattedTimestamp;
        if (note.isNew()) {
            formattedTimestamp = "Created at " + formatDate(note.getCreatedTimestamp());
        } else {
            formattedTimestamp = "Updated at " + formatDate(note.getUpdatedTimestamp());
        }
        holder.tv_time.setText(formattedTimestamp);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), UpdateActivity.class);
            int data = notes.get(holder.getAdapterPosition()).getId();
            String id = Integer.toString(data);
            intent.putExtra("id", id);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("desc", note.getDesc());
            ((MainActivity) context).startActivityForResult(intent, REQUEST_CODE_UPDATE);
        });
    }

//get Item Count
    @Override
    public int getItemCount() {

        return notes.size();
    }

    //inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_desc, tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }


    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }
}

