package com.nikhilgu.covidmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                if(questionCounter == questions.length){
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
                if(questionCounter == questions.length){
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
}