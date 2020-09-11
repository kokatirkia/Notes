package com.example.notes.ui;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.database.NoteRepository;
import com.example.notes.database.model.Note;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class SharedViewModel extends ViewModel {
    private NoteRepository repository;
    private MutableLiveData<Note> noteMutableLiveData;

    @ViewModelInject
    public SharedViewModel(NoteRepository noteRepository) {
        this.repository = noteRepository;
        noteMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Note>> getAllNotes() {
        return LiveDataReactiveStreams.fromPublisher(
                repository.getAllNotes().subscribeOn(Schedulers.io())
        );
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

    public void setNote(Note note) {
        noteMutableLiveData.setValue(note);
    }

    public LiveData<Note> getNote() {
        return noteMutableLiveData;
    }

}
