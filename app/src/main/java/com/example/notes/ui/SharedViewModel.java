package com.example.notes.ui;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.database.NoteRepository;
import com.example.notes.database.model.Note;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private MutableLiveData<Note> noteMutableLiveData;

    @ViewModelInject
    public SharedViewModel(NoteRepository noteRepository) {
        this.repository = noteRepository;
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
