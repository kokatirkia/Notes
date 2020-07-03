package com.example.notes.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notes.database.model.Note;

import java.util.List;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(final Note note) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });
    }

    public void update(final Note note) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }

    public void delete(final Note note) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });
    }

    public void deleteAllNotes() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteAllNotes();
            }
        });
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}