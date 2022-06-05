package com.example.firestore_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText ed_title,ed_description;
    TextView showDetails;
    private  static final String TAG = "MainActivity";

    private  static final String KEY_TITLE = "title";
    private  static final String KEY_DESCRIPTION = "description";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference notref = db.document("Notebook/my second note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_title = findViewById(R.id.titles);
        ed_description =findViewById(R.id.description);
        showDetails =findViewById(R.id.show_details);

    }

    @Override
    protected void onStart() {
        super.onStart();
        notref.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,error.toString());
                    return;
                }
                if (value.exists()){
//                    String title = value.getString(KEY_TITLE);
//                    String description =  value.getString(KEY_DESCRIPTION);

                         note note = value.toObject(note.class);
                                 String title = note.getTitle();
                                 String description = note.getDescription();
                    showDetails.setText("Title :: "+title+"\n"+"description :: "+description);
                }else {
                    showDetails.setText("");
                }
            }
        });
    } //auto loading deta

    public void saveNote(View view) {
        String title= ed_title.getText().toString();
        String description = ed_description.getText().toString();

//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE,title);
//        note.put(KEY_DESCRIPTION,description);

        note note = new note(title,description);

       // db.collection("Notebook/my first note")   //short form
        db.collection("Notebook").document("my second note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });

    }



    public void update_description(View view) {
        String des = ed_description.getText().toString();
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION,des);
//
//        notref.set(note, SetOptions.merge()); // only update description filed in firebase,without touching another filed.

        notref.update(KEY_DESCRIPTION,des);
    }



    public void showdetails(View view) {
             notref.get()
                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                         @Override
                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                             if (documentSnapshot.exists()){
//                                 String title = documentSnapshot.getString(KEY_TITLE);
//                                 String description =  documentSnapshot.getString(KEY_DESCRIPTION);

                                 note note = documentSnapshot.toObject(note.class);
                                 String title = note.getTitle();
                                 String description = note.getDescription();

                                 showDetails.setText("Title :: "+title+"\n"+"description :: "+description);
                             }else {
                                 Toast.makeText(MainActivity.this, "Document does not exists", Toast.LENGTH_SHORT).show();
                             }

                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                             Log.d(TAG,e.toString());
                         }
                     });
    }


    public void delete_description(View view) {
//        Map<String,Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION, FieldValue.delete());
//        notref.update(note);

//similar to this single code line
          notref.update(KEY_DESCRIPTION,FieldValue.delete());
    }

    public void delete_note(View view) {
        notref.delete();
    }
}
