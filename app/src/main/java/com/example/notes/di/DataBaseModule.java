package com.example.notes.di;

import android.app.Application;

import androidx.room.Room;

import com.example.notes.database.NoteDao;
import com.example.notes.database.NoteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DataBaseModule {

    @Provides
    @Singleton
    public static NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(application,
                NoteDatabase.class, "note_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public static NoteDao provideNoteDao(NoteDatabase noteDatabase){
        return noteDatabase.noteDao();
    }
}