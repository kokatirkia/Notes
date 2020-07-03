package com.example.notes.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.database.NoteRepository;
import com.example.notes.database.model.Note;

import java.util.List;

public class SharedViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private MutableLiveData<Note> noteMutableLiveData;


    public SharedViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        noteMutableLiveData = new MutableLiveData<>();
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

}
