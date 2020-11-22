package com.example.notes.ui;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.notes.database.NoteRepository;
import com.example.notes.database.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private final NoteRepository repository;
    private final LiveData<List<Note>> notes;
    private Note note;

    @ViewModelInject
    public SharedViewModel(@Assisted SavedStateHandle savedStateHandle, NoteRepository noteRepository) {
        this.savedStateHandle = savedStateHandle;
        this.repository = noteRepository;
        this.notes = LiveDataReactiveStreams.fromPublisher(repository.getAllNotes());
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
        savedStateHandle.set("note", note);
        this.note = note;
    }

    public Note getNote() {
        if (note == null) return savedStateHandle.get("note");
        else return note;
    }

    public LiveData<List<Note>> getAllNotes() {
        return notes;
    }

    public List<Note> filterNotes(String query) {
        if (notes.getValue() != null) {
            List<Note> filteredList = new ArrayList<>();
            for (Note item : notes.getValue()) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            return filteredList;
        }
        return null;
    }
}
