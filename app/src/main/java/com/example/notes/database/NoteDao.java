package com.example.notes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.database.model.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface NoteDao {

    @Insert
    Completable insert(Note note);

    @Update
    Completable update(Note note);

    @Delete
    Completable delete(Note note);

    @Query("DELETE FROM note_table")
    Completable deleteAllNotes();

    @Query("SELECT * FROM note_table")
    Flowable<List<Note>> getAllNotes();
}