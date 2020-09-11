package com.example.notes.database;

import com.example.notes.database.model.Note;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class NoteRepository {
    private NoteDao noteDao;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public void insert(final Note note) {
        noteDao.insert(note).subscribeOn(Schedulers.io()).subscribe();
    }

    public void update(final Note note) {
        noteDao.update(note).subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete(final Note note) {
        noteDao.delete(note).subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteAllNotes() {
        noteDao.deleteAllNotes().subscribeOn(Schedulers.io()).subscribe();
    }

    public Observable<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

}