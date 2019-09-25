package com.example.auburnacmmailapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Objects;

public class SendEmailActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference emailCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        emailCollection = db.collection("emailCollection");
    }

    public void send(View view) {
        final HashSet<String> emails = new HashSet<>();
        final EditText subjectET = findViewById(R.id.subjectET);
        final EditText bodyET = findViewById(R.id.bodyET);

        emailCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    emails.add(documentSnapshot.getString("Email"));
                }
                String[] recipients = new String[emails.size()];
                emails.toArray(recipients);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, recipients);
                i.putExtra(Intent.EXTRA_SUBJECT, subjectET.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT   , bodyET.getText().toString());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SendEmailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
