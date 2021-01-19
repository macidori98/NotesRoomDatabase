package com.example.roomdatabase.databases;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.roomdatabase.daos.NoteDao;
import com.example.roomdatabase.entities.Note;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
    private static final RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database").fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }

        return instance;
    }

    public abstract NoteDao noteDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase db) {
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title", "Desc"));
            noteDao.insert(new Note("Title", "Desc"));
            noteDao.insert(new Note("Title", "Desc"));
            noteDao.insert(new Note("Title", "Desc"));
            noteDao.insert(new Note("Title", "Desc"));
            return null;
        }
    }
}
