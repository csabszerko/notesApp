package com.csabszerko.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {

    EditText mAdd_title_of_note, mAdd_content_of_note;
    FloatingActionButton mSave_note_fab, mClose_note_fab;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore mFirestore;

    @SuppressLint("UseSupportActionBar")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mSave_note_fab=findViewById(R.id.save_note_fab);
        mClose_note_fab=findViewById(R.id.close_note_fab);
        mAdd_title_of_note=findViewById(R.id.add_title_of_note);
        mAdd_content_of_note=findViewById(R.id.add_content_of_note);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        mSave_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mAdd_title_of_note.getText().toString();
                String content = mAdd_content_of_note.getText().toString();
                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "both fields are required!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = mFirestore.collection("notes").document(user.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content", content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "note created successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddNoteActivity.this, NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed to create note!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mClose_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AddNoteActivity.this, NotesActivity.class));
            }
        });
    }
}