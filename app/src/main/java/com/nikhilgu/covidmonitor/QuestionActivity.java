package com.nikhilgu.covidmonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class QuestionActivity extends AppCompatActivity {

    private TextView nameTV;
    private Button symptomSurveyBtn;
    private Button dailyTrackerBtn;
    private Button adminDashboardBtn;
    private Button covidInfoBtn;
    private Button signOutBtn;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        this.getSupportActionBar().hide();

        nameTV = findViewById(R.id.name);
        symptomSurveyBtn = findViewById(R.id.symptom_survey_button);
        dailyTrackerBtn = findViewById(R.id.daily_tracker_button);
        adminDashboardBtn = findViewById(R.id.admin_dashboard_button);
        covidInfoBtn = findViewById(R.id.covid_info_button);
        signOutBtn = findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Button listeners below
        symptomSurveyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(QuestionActivity.this, SymptomActivity.class));
                //finish();
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
                nameTV.setText("Admin Dashboard");
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

    @Override
    public void onStart(){
        super.onStart();
        account = getIntent().getParcelableExtra("ACCOUNT");

        nameTV.setText("Welcome " + account.getDisplayName());
    }
}
