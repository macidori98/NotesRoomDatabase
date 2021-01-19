package com.example.roomdatabase.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.roomdatabase.daos.NoteDao;
import com.example.roomdatabase.databases.NoteDatabase;
import com.example.roomdatabase.entities.Note;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> notes;

    public NoteRepository(Application application) {

        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        notes = noteDao.getNotes();
    }

    public void insert(Note note) {
        new InsertNoteAsync(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsync(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsync(noteDao).execute(note);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    private static class InsertNoteAsync extends AsyncTask<Note, Void, Void> {

        private final NoteDao noteDao;

        public InsertNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsync extends AsyncTask<Note, Void, Void> {

        private final NoteDao noteDao;

        public UpdateNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsync extends AsyncTask<Note, Void, Void> {

        private final NoteDao noteDao;

        public DeleteNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
}
