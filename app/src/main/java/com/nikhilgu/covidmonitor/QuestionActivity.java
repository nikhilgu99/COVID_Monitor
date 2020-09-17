package com.nikhilgu.covidmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class QuestionActivity extends AppCompatActivity {

    private TextView nameTV;
    private Button symptomSurveyBtn;
    private Button dailyTrackerBtn;
    private Button adminDashboardBtn;
    private Button covidInfoBtn;
    private Button signOutBtn;

    Calendar calendar = Calendar.getInstance();
    private int month;
    private int day;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        this.getSupportActionBar().hide();

        //Hooks
        nameTV = findViewById(R.id.name);
        symptomSurveyBtn = findViewById(R.id.symptom_survey_button);
        dailyTrackerBtn = findViewById(R.id.daily_tracker_button);
        adminDashboardBtn = findViewById(R.id.admin_dashboard_button);
        covidInfoBtn = findViewById(R.id.covid_info_button);
        signOutBtn = findViewById(R.id.sign_out_button);

        //Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Calendar date retrieval
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //Setup firebase connection
        rootNode = FirebaseDatabase.getInstance("https://covid-monitor-1599596988334.firebaseio.com/");
        reference = rootNode.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = dataSnapshot.child(account.getId()).child("Last Survey").getValue(String.class);

                // Disable survey button if it has already been taken today
                if(date.equalsIgnoreCase(month + "" + day)){
                    symptomSurveyBtn.setText("Completed Daily Survey");
                    symptomSurveyBtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Data Retrieval Failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        // Button listeners below
        symptomSurveyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
            Intent intent = new Intent(QuestionActivity.this, SymptomActivity.class);
            intent.putExtra("ACCOUNT", account);
            startActivity(intent);
            }
        });

        dailyTrackerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                nameTV.setText("Daily Tracker");
            }
        });

        adminDashboardBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(QuestionActivity.this, AdminActivity.class));
            }
        });

        covidInfoBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(QuestionActivity.this, InfoActivity.class));
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                signOut();
            }
        });
    }
    // Listener for Google Sign-Out
    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Go back to main activity (Sign in screen)
                    startActivity(new Intent(QuestionActivity.this, MainActivity.class));
                    finish();
                }
            });
    }
    // Get Google account information
    @Override
    public void onStart(){
        super.onStart();
        account = getIntent().getParcelableExtra("ACCOUNT");

        nameTV.setText("Welcome " + account.getDisplayName());
    }
}
