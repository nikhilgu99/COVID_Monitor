package com.nikhilgu.covidmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SymptomActivity extends AppCompatActivity {

    private TextView questionNum;
    private TextView questionLbl;
    private Button yesBtn;
    private Button noBtn;

    private int questionCounter = 1;
    private int yesCounter = 0;
    private int noCounter = 0;
    String[] questions = {
        "Are you experiencing chills or fever?",
        "Are you experiencing a cough?",
        "Are you experiencing shortness of breath or difficulty breathing?",
        "Are you experiencing fatigue?",
        "Are you experiencing muscle or body aches?",
        "Are you experiencing a headache?",
        "Are you experiencing new loss of taste or smell?",
        "Are you experiencing a sore throat?",
        "Are you experiencing congestion or runny nose?",
        "Are you experiencing nausea or vomiting?",
        "Are you experiencing diarrhea?",
        "Have you been in contact with someone tested COVID positive in the past 14 days?",
    };

    GoogleSignInAccount account;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        this.getSupportActionBar().hide();

        questionNum = findViewById(R.id.questionNum);
        questionLbl = findViewById(R.id.symptom);
        yesBtn = findViewById(R.id.yes_button);
        noBtn = findViewById(R.id.no_button);

        questionNum.setText("Q" + questionCounter);
        questionLbl.setText(questions[questionCounter - 1]);

        yesBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
            reference.child(account.getId()).child("Q" + questionCounter).setValue("Yes");

            if(questionCounter == questions.length){
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                reference.child(account.getId()).child("Last Survey").setValue(month + "" + day);

                Toast.makeText(getApplicationContext(), "Finished Survey - Yes", Toast.LENGTH_SHORT).show();
            }else {
                yesCounter++;
                questionCounter++;

                questionNum.setText("Q" + questionCounter);
                questionLbl.setText(questions[questionCounter - 1]);
            }
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
            reference.child(account.getId()).child("Q" + questionCounter).setValue("No");

            if(questionCounter == questions.length){
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                reference.child(account.getId()).child("Last Survey").setValue(month + "" + day);

                Toast.makeText(getApplicationContext(), "Finished Survey - No", Toast.LENGTH_SHORT).show();
            }else {
                noCounter++;
                questionCounter++;

                questionNum.setText("Q" + questionCounter);
                questionLbl.setText(questions[questionCounter - 1]);
            }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        account = getIntent().getParcelableExtra("ACCOUNT");

        rootNode = FirebaseDatabase.getInstance("https://covid-monitor-1599596988334.firebaseio.com/");
        reference = rootNode.getReference("Users");
    }
}