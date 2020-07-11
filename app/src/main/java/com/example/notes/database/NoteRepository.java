package com.example.notes.database;

import androidx.lifecycle.LiveData;

import com.example.notes.database.model.Note;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
        allNotes = noteDao.getAllNotes();
    }

    public void insert(final Note note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.insert(note));
    }

    public void update(final Note note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.update(note));
    }

    public void delete(final Note note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.delete(note));
    }

    public void deleteAllNotes() {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.deleteAllNotes());
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}