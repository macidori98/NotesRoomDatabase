package com.example.roomdatabase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomdatabase.entities.Note;
import com.example.roomdatabase.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> notes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }
}
