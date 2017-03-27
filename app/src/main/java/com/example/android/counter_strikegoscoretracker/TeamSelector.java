package com.example.android.counter_strikegoscoretracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by dts on 23/03/17.
 */

public class TeamSelector extends AppCompatActivity {

    String team;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_pick);
    }

    public void pickT(View v) {
        team = "Terrorists1";

        Intent scoreTracker = new Intent(TeamSelector.this, ScoreTracker.class);
        scoreTracker.putExtra("SELECTED_TEAM", team);
        startActivity(scoreTracker);
    }

    public void pickCT(View v) {
        team = "Counter-Terrorists1";

        Intent scoreTracker = new Intent(TeamSelector.this, ScoreTracker.class);
        scoreTracker.putExtra("SELECTED_TEAM", team);
        startActivity(scoreTracker);

    }
}
