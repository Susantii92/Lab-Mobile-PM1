package com.example.pertemuan_8;

import android.os.Parcel;
import android.os.Parcelable;

//Parcelable
public class Note implements Parcelable {
    private int id;
    private String title;
    private String desc;
    private long createdTimestamp;
    private long updatedTimestamp;

    public Note() {
    }

    public Note(int id, String title, String desc, long createdTimestamp, long updatedTimestamp) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    protected Note(Parcel in) { //konstruktor
        id = in.readInt();
        title = in.readString();
        desc = in.readString();
        createdTimestamp = in.readLong();
        updatedTimestamp = in.readLong();
    }

   public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) { //membuat instance baru dari Note menggunakan data yang ada di dalam Parcel
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    // setter dan getter
    public int getId() { //mengembalikan nilai dari variabel id
        return id;
    }

    public void setId(int id) { //Mengatur nilai dari variabel id
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(long updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public boolean isNew() {
        return createdTimestamp == updatedTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeLong(createdTimestamp);
        dest.writeLong(updatedTimestamp);
    }
}

