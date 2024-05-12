package com.csabszerko.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class EditNoteActivity extends AppCompatActivity {

    EditText mEdit_title_of_note, mEdit_content_of_note;
    FloatingActionButton mUpdate_note_fab, mDelete_note_fab;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mUpdate_note_fab=findViewById(R.id.update_note_fab);
        mDelete_note_fab=findViewById(R.id.delete_note_fab);
        mEdit_title_of_note=findViewById(R.id.edit_title_of_note);
        mEdit_content_of_note=findViewById(R.id.edit_content_of_note);

        Intent data = getIntent();
        mEdit_title_of_note.setText(data.getStringExtra("title"));
        mEdit_content_of_note.setText(data.getStringExtra("content"));
        String docId = data.getStringExtra("noteId");

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        mDelete_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = mFirestore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "note deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(EditNoteActivity.this, NotesActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "failed to delete note!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mUpdate_note_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mEdit_title_of_note.getText().toString();
                String newContent = mEdit_content_of_note.getText().toString();

                if(newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "both fields required!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = mFirestore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newTitle);
                    note.put("content", newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "note updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(EditNoteActivity.this, NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed to update note!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}