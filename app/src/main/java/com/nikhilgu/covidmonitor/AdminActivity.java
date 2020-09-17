package com.nikhilgu.covidmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {

    private TextView completedTV;
    private TextView notCompletedTV;
    private TextView percentTV;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private Calendar calendar = Calendar.getInstance();

    private int month;
    private int day;
    private double numYesPeople = 0.0;
    private double totalPeople = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.getSupportActionBar().hide();

        // Hooks
        completedTV = findViewById(R.id.completed);
        notCompletedTV = findViewById(R.id.notCompleted);
        percentTV = findViewById(R.id.percent);

        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Setup firebase connection
        rootNode = FirebaseDatabase.getInstance("https://covid-monitor-1599596988334.firebaseio.com/");
        reference = rootNode.getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String successNames = "";
                String notSuccessNames = "";
                // Check who has/hasn't completed survey, and who has symptoms
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("Name").getValue(String.class);
                    String date = snapshot.child("Last Survey").getValue(String.class);

                    if(date.equalsIgnoreCase(month + "" + day)){
                        successNames += name + ", ";
                    }else{
                        notSuccessNames += name + ", ";
                    }

                    for(int x = 1; x <= 12; x++){
                        String answer = snapshot.child("Q" + x).getValue(String.class);
                        if(answer.equalsIgnoreCase("Yes")){
                            numYesPeople++;
                            break;
                        }
                    }
                    totalPeople++;
                }

                if(!successNames.equalsIgnoreCase("")){
                    completedTV.setText("Completed Survey: " + successNames.substring(0, successNames.length() - 2));
                }else{
                    completedTV.setText("Completed Survey:");
                }
                if(!notSuccessNames.equalsIgnoreCase("")){
                    notCompletedTV.setText("Not Completed Survey: " + notSuccessNames.substring(0, notSuccessNames.length() - 2));
                }else{
                    notCompletedTV.setText("Not Completed Survey:");
                }
                String percentPeople = String.format("%.2f", (numYesPeople / totalPeople) * 100);
                percentTV.setText("Percent with symptoms: " + percentPeople + "%");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Data Retrieval Failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}