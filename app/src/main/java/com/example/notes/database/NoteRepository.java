package com.example.notes.database;

import androidx.lifecycle.LiveData;

import com.example.notes.database.model.Note;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
        allNotes = noteDao.getAllNotes();
    }

    public void insert(final Note note) {
        Single.just(note)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Note>() {
                    @Override
                    public void onSuccess(@NonNull Note note) {
                        noteDao.insert(note);
                        dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dispose();
                    }
                });
    }

    public void update(final Note note) {
        Single.just(note)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Note>() {
                    @Override
                    public void onSuccess(@NonNull Note note) {
                        noteDao.update(note);
                        dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dispose();
                    }
                });
    }

    public void delete(final Note note) {
        Single.just(note)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Note>() {
                    @Override
                    public void onSuccess(@NonNull Note note) {
                        noteDao.delete(note);
                        dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dispose();
                    }
                });
    }

    public void deleteAllNotes() {
        Single.just(1)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer i) {
                        noteDao.deleteAllNotes();
                        dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dispose();
                    }
                });
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}