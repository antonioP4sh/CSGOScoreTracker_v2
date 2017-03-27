package com.example.android.counter_strikegoscoretracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.android.counter_strikegoscoretracker.R.id.tip;

/**
 * Created by dts on 23/03/17.
 */

public class FinalScore extends AppCompatActivity {

    String team;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_final);

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamSelect();
            }
        });

        TextView tipTextView = (TextView) findViewById(tip);
        tipTextView.setText("gg wp");

        TextView teamTScoreView = (TextView) findViewById(R.id.team_a_score);
        TextView teamCTScoreView = (TextView) findViewById(R.id.team_b_score);

        teamTScoreView.setText(getIntent().getStringExtra("T_SCORE"));
        teamCTScoreView.setText(getIntent().getStringExtra("CT_SCORE"));

        TextView roundView = (TextView) findViewById(R.id.roundCount);
        roundView.setText(getIntent().getStringExtra("VERDICT"));

        team = getIntent().getStringExtra("TEAM");

        if (team.equals("Terrorists2")) {
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.VISIBLE);
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.GONE);
        } else {
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.VISIBLE);
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        teamSelect();
    }

    public void teamSelect() {

        Intent teamSelector = new Intent(FinalScore.this, TeamSelector.class);
        startActivity(teamSelector);
    }
}
