package com.example.roomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.adaptors.NoteAdaptor;
import com.example.roomdatabase.entities.Note;
import com.example.roomdatabase.viewmodels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteAdaptor noteAdaptor = new NoteAdaptor();
        recyclerView.setAdapter(noteAdaptor);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update recyclerview
                noteAdaptor.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdaptor.getNotes(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        noteAdaptor.setOnItemClickListener(new NoteAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("description",note.getDescription());
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.top_menu) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String title = data.getStringExtra("noteTitle");
            String desc = data.getStringExtra("noteDescription");

            Note note = new Note(title, desc);
            noteViewModel.insert(note);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("titlelast");
            String description = data.getStringExtra("descriptionlast");
            int id = data.getIntExtra("noteid", -1);

            Note note = new Note(title, description);
            note.setId(id);
            noteViewModel.update(note);
        }
    }
}