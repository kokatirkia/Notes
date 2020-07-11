package com.example.notes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notes.database.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

}