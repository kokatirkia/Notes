package com.example.notes.ui;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.database.NoteRepository;
import com.example.notes.database.model.Note;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SharedViewModel extends ViewModel {
    private NoteRepository repository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Note>> allNotes;
    private MutableLiveData<Note> noteMutableLiveData;

    @ViewModelInject
    public SharedViewModel(NoteRepository noteRepository) {
        this.repository = noteRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.allNotes = new MutableLiveData<>();
        noteMutableLiveData = new MutableLiveData<>();
        fetchNotesMutableLIveData();
    }

    public void fetchNotesMutableLIveData() {
        repository.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        allNotes.setValue(notes);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void setNote(Note note) {
        noteMutableLiveData.setValue(note);
    }

    public LiveData<Note> getNote() {
        return noteMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
