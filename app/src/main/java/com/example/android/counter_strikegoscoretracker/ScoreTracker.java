package com.example.android.counter_strikegoscoretracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreTracker extends AppCompatActivity {

    int scoreT;
    int scoreCT;
    int round;
    int rowTRounds;
    int rowCTRounds;
    boolean gameOver;
    String team;
    CountDownTimer bombCountDownTimer;
    boolean bombCount = false;
    ArrayList<Round> rounds = new ArrayList<Round>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_tracker);

        if (savedInstanceState == null || !savedInstanceState.containsKey("ROUNDS_ARRAY")) {
            rounds.clear();
            if (bombCount == true) {
                bombCountDownTimer.cancel();
            }
            scoreT = 0;
            scoreCT = 0;
            round = 0;
            rowTRounds = 0;
            rowCTRounds = 0;
            gameOver = false;
            //bombCountDownTimer.cancel();
            selectTeam();
            displayTip(round);
            displayForTeamT(scoreT);
            displayForTeamCT(scoreCT);
            roundUpdate();
            resetBonus();
            resetRoundHistory();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ROUNDS_ARRAY", rounds);
        outState.putString("PLAYER_TEAM", team);
        outState.putInt("T_SCORE", scoreT);
        outState.putInt("CT_SCORE", scoreCT);
        outState.putInt("ROUND_COUNT", round);
        outState.putInt("ROW_T_ROUNDS", rowTRounds);
        outState.putInt("ROW_CT_ROUNDS", rowCTRounds);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        rounds = savedInstanceState.getParcelableArrayList("ROUNDS_ARRAY");
        team = savedInstanceState.getString("PLAYER_TEAM");
        scoreT = savedInstanceState.getInt("T_SCORE");
        scoreCT = savedInstanceState.getInt("CT_SCORE");
        rowTRounds = savedInstanceState.getInt("ROW_T_ROUNDS");
        rowCTRounds = savedInstanceState.getInt("ROW_CT_ROUNDS");

        selectTeam();
        displayForTeamT(scoreT);
        displayForTeamCT(scoreCT);

        if (bombCount == true) {
            bombCountDownTimer.cancel();
        }

        int firstRoundHistoryUpdate;
        if (savedInstanceState.getInt("ROUND_COUNT") > 15) {
            firstRoundHistoryUpdate = 15;
            switchTeam();
        } else {
            firstRoundHistoryUpdate = 0;
        }
        round = firstRoundHistoryUpdate + 1;


        for (int i = firstRoundHistoryUpdate; i < (rounds.size()); i++) {
            Round currentRound = rounds.get(i);
            String winner = currentRound.getRoundWinner();
            String type = currentRound.getWinType();

            if (winner.equals("Terrorists")) {
                switch (type) {
                    case "5k":
                        displayBonusT(0, 0);
                        displayBonusCT(0);
                        updateRoundHistoryT(0);
                        break;
                    case "bomb":
                        displayBonusT(250, 0);
                        displayBonusCT(0);
                        updateRoundHistoryT(250);
                        break;
                }
            } else if (winner.equals("Counter-Terrorists")) {
                switch (type) {
                    case "5k":
                        displayBonusT(0, 0);
                        displayBonusCT(0);
                        updateRoundHistoryCT(0, false);
                        break;
                    case "defuse":
                        displayBonusT(0, 800);
                        displayBonusCT(250);
                        updateRoundHistoryCT(250, false);
                        break;
                    case "time":
                        displayBonusT(0, 0);
                        displayBonusCT(0);
                        updateRoundHistoryCT(0, true);
                        break;
                }
            }
            round++;
        }

        round = savedInstanceState.getInt("ROUND_COUNT");

        TextView roundView = (TextView) findViewById(R.id.roundCount);
        roundView.setText("Round: " + String.valueOf(round) + "/30");
        displayTip(round);
    }

    public void bombTimer(View v) {

        if (bombCount == false) {

            bombCount = true;
            bombCountDownTimer = new CountDownTimer(44000, 1000) {

                Button bombTimer = (Button) findViewById(R.id.bomb_trigger);

                public void onTick(long millisUntilFinished) {

                    bombTimer.setText("" + millisUntilFinished / 1000);

                    if (millisUntilFinished <= 21000 && millisUntilFinished >= 20000) {

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);

                    } else if (millisUntilFinished <= 11000 && millisUntilFinished >= 10000) {

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);

                    } else if (millisUntilFinished <= 6000) {

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                }

                public void onFinish() {
                    bombCount = false;
                    bombTimer.setText("Bomb!");
                }

            }.start();

        }
    }

    public void selectTeam() {
        team = getIntent().getStringExtra("SELECTED_TEAM");

        if (team.equals("Terrorists1") || team.equals("Terrorists2")) {
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.VISIBLE);
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.GONE);
        } else /*team = "Counter-Terrorists1";*/ {
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.VISIBLE);
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.GONE);
        }
    }

    public void switchTeam() {
        if (team.equals("Terrorists1")) {
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.VISIBLE);
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.GONE);
            team = "Counter-Terrorists2";
        }
        if (team.equals("Counter-Terrorists1")) {
            View tPlayer = findViewById(R.id.t_player);
            tPlayer.setVisibility(View.VISIBLE);
            View ctPlayer = findViewById(R.id.ct_player);
            ctPlayer.setVisibility(View.GONE);
            team = "Terrorists2";
        }
    }

    public void roundUpdate() {

        round += 1;
        String verdict;

        if (scoreT == 16) {
            String winner = "Terrorists2";
            if (winner.equals(team)) {
                verdict = "You Won! Congrats!";
            } else {
                verdict = "You Lost! Maybe Next Time!";
            }
            gameOver = true;
            showFinalScore(verdict);
            resetRoundHistory();
            return;
        }

        if (scoreCT == 16) {
            String winner = "Counter-Terrorists2";
            if (winner.equals(team)) {
                verdict = "You Won! Congrats!";
            } else {
                verdict = "You Lost! Maybe Next Time!";
            }
            gameOver = true;
            showFinalScore(verdict);
            resetRoundHistory();
            return;
        }

        if (round == 16) {
            switchTeam();

            TextView teamTScoreView = (TextView) findViewById(R.id.team_a_score);
            TextView teamCTScoreView = (TextView) findViewById(R.id.team_b_score);
            scoreT = Integer.valueOf(teamCTScoreView.getText().toString());
            scoreCT = Integer.valueOf(teamTScoreView.getText().toString());

            displayForTeamT(scoreT);
            displayForTeamCT(scoreCT);

            rowCTRounds = 0;
            rowTRounds = 0;
            resetBonus();
            resetRoundHistory();
        }

        if (round == 31) {
            verdict = "You Draw!";
            showFinalScore(verdict);
            resetRoundHistory();
            return;
        }


        TextView roundView = (TextView) findViewById(R.id.roundCount);
        roundView.setText("Round: " + String.valueOf(round) + "/30");

        displayTip(round);

        if (bombCount == true) {
            bombCountDownTimer.cancel();
            bombCount = false;
            Button bombTimer = (Button) findViewById(R.id.bomb_trigger);
            bombTimer.setText("Bomb!");
        }
    }

    public void displayTip(int round) {

        String tip;

        switch (round) {
            case 1:
                tip = "gl hf";
                break;
            case 15:
                tip = "Last round! Don't save money.";
                break;
            case 30:
                tip = "Last round! Don't save money.";
                break;
            default:
                tip = "";
        }

        TextView tipTextView = (TextView) findViewById(R.id.tip);
        tipTextView.setText(tip);
    }

    public void showFinalScore(String message) {

        Intent finalScore = new Intent(ScoreTracker.this, FinalScore.class);
        finalScore.putExtra("CT_SCORE", "" + scoreCT);
        finalScore.putExtra("T_SCORE", "" + scoreT);
        finalScore.putExtra("VERDICT", message);
        finalScore.putExtra("TEAM", team);
        startActivity(finalScore);
    }

    public void displayForTeamT(int scoreT) {

        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(scoreT));
    }

    public void displayForTeamCT(int scoreCT) {

        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(scoreCT));
    }

    public void displayBonusT(int bomb, int bombPlant) {

        int tBonus;
        int t2EcoBonus;

        if (rowCTRounds == 0) {
            tBonus = 3250 + bomb;

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_a_2eco_bonus);
            dbEcoBonusView.setText("");

            TextView saveAmountView = (TextView) findViewById(R.id.team_a_save_amount);
            saveAmountView.setText("");
        } else if (rowCTRounds >= 5) {
            tBonus = 3400 + bombPlant;
            t2EcoBonus = tBonus + 3400;

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_a_2eco_bonus);
            dbEcoBonusView.setText("$" + String.valueOf(t2EcoBonus));

            TextView saveAmountView = (TextView) findViewById(R.id.team_a_save_amount);
            saveAmountView.setText("$" + (4400 - 3400));
        } else {
            tBonus = 900 + 500 * rowCTRounds + bombPlant;
            t2EcoBonus = tBonus + (900 + 500 * (rowCTRounds + 1));

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_a_2eco_bonus);
            dbEcoBonusView.setText("$" + String.valueOf(t2EcoBonus));

            TextView saveAmountView = (TextView) findViewById(R.id.team_a_save_amount);
            saveAmountView.setText("$" + (4400 - (900 + 500 * (rowCTRounds + 1))));
        }

        TextView tBonusView = (TextView) findViewById(R.id.team_a_bonus);
        tBonusView.setText("$" + String.valueOf(tBonus));
    }

    public void updateRoundHistoryT(int bomb) {
        String source;
        int srcT;
        int historyRound = round;

        if (round > 15) {
            historyRound = round - 15;
        }


        if (bomb == 250) {
            source = "@drawable/icon_c4_wh";
            srcT = getResources().getIdentifier(source, "id", getPackageName());
        } else {
            source = "@drawable/icon_kill_wh";
            srcT = getResources().getIdentifier(source, "id", getPackageName());
        }

        String tRoundImageResId = "t_round" + (historyRound);
        int resId = getResources().getIdentifier(tRoundImageResId, "id", getPackageName());

        ImageView tRoundImage = (ImageView) findViewById(resId);
        tRoundImage.setVisibility(View.VISIBLE);
        tRoundImage.setImageResource(srcT);

        String ctRoundImageResId = "ct_round" + (historyRound);
        resId = getResources().getIdentifier(ctRoundImageResId, "id", getPackageName());

        ImageView ctRoundImage = (ImageView) findViewById(resId);
        ctRoundImage.setVisibility(View.VISIBLE);
        ctRoundImage.setImageResource(0);
    }

    public void resetRoundHistory() {

        for (int i = 1; i < 16; i++) {
            String tRoundImageResId = "t_round" + (i);
            int tResId = getResources().getIdentifier(tRoundImageResId, "id", getPackageName());
            ImageView tRoundImage = (ImageView) findViewById(tResId);
            tRoundImage.setVisibility(View.GONE);
            tRoundImage.setImageResource(0);

            String ctRoundImageResId = "ct_round" + (i);
            int ctResId = getResources().getIdentifier(ctRoundImageResId, "id", getPackageName());
            ImageView ctRoundImage = (ImageView) findViewById(ctResId);
            ctRoundImage.setVisibility(View.GONE);
            ctRoundImage.setImageResource(0);
        }
    }

    public void updateRoundHistoryCT(int defuse, boolean outOfTime) {
        String source;
        int srcCT;
        int historyRound = round;

        if (round > 15) {
            historyRound = round - 15;
        }


        if (defuse == 250) {
            source = "@drawable/icon_defuser_wh";
            srcCT = getResources().getIdentifier(source, "id", getPackageName());
        } else if (outOfTime == true) {
            source = "@drawable/icon_time_wh";
            srcCT = getResources().getIdentifier(source, "id", getPackageName());
        } else {
            source = "@drawable/icon_kill_wh";
            srcCT = getResources().getIdentifier(source, "id", getPackageName());
        }

        String crToundImageResId = "ct_round" + (historyRound);
        int resId = getResources().getIdentifier(crToundImageResId, "id", getPackageName());

        ImageView crToundImage = (ImageView) findViewById(resId);
        crToundImage.setVisibility(View.VISIBLE);
        crToundImage.setImageResource(srcCT);

        String tRoundImageResId = "t_round" + (historyRound);
        resId = getResources().getIdentifier(tRoundImageResId, "id", getPackageName());

        ImageView tRoundImage = (ImageView) findViewById(resId);
        tRoundImage.setVisibility(View.VISIBLE);
        tRoundImage.setImageResource(0);
    }

    public void displayBonusCT(int defuse) {

        int ctBonus;
        int ct2EcoBonus;

        if (rowTRounds == 0) {
            ctBonus = 3250 + defuse;

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_b_2eco_bonus);
            dbEcoBonusView.setText("");

            TextView saveAmountView = (TextView) findViewById(R.id.team_b_save_amount);
            saveAmountView.setText("");
        } else if (rowTRounds >= 5) {
            ctBonus = 3400;
            ct2EcoBonus = ctBonus * 2;

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_b_2eco_bonus);
            dbEcoBonusView.setText("$" + String.valueOf(ct2EcoBonus));

            TextView saveAmountView = (TextView) findViewById(R.id.team_b_save_amount);
            saveAmountView.setText("$" + (5200 - 3400));
        } else {
            ctBonus = 900 + 500 * rowTRounds;
            ct2EcoBonus = ctBonus * 2 + 500;

            TextView dbEcoBonusView = (TextView) findViewById(R.id.team_b_2eco_bonus);
            dbEcoBonusView.setText("$" + String.valueOf(ct2EcoBonus));

            TextView saveAmountView = (TextView) findViewById(R.id.team_b_save_amount);
            saveAmountView.setText("$" + (5200 - (900 + 500 * (rowTRounds + 1))));
        }

        TextView scoreView = (TextView) findViewById(R.id.team_b_bonus);
        scoreView.setText("$" + String.valueOf(ctBonus));
    }

    public void resetBonus() {
        TextView tBonusView = (TextView) findViewById(R.id.team_a_bonus);
        tBonusView.setText("$800");
        TextView teamAEcoBonusView = (TextView) findViewById(R.id.team_a_2eco_bonus);
        teamAEcoBonusView.setText("");
        TextView ctBonusView = (TextView) findViewById(R.id.team_b_bonus);
        ctBonusView.setText("$800");
        TextView teamBEcoBonusView = (TextView) findViewById(R.id.team_b_2eco_bonus);
        teamBEcoBonusView.setText("");

        TextView teamASaveAmountView = (TextView) findViewById(R.id.team_a_save_amount);
        teamASaveAmountView.setText("");
        TextView teamBSaveAmountView = (TextView) findViewById(R.id.team_b_save_amount);
        teamBSaveAmountView.setText("");
    }

    public void visitWebGuide(View v) {

        Uri webpage = Uri.parse("http://clutchround.com/csgo-economy-system-explained/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void t5k(View v) {

        if (!gameOver) {
            scoreT += 1;
            rowTRounds += 1;
            rowCTRounds = 0;
            int bombBonus = 0;
            int bombDefuse = 0;
            int defuse = 0;
            rounds.add(new Round("Terrorists", "5k"));

            updateRoundHistoryT(bombBonus);
            displayForTeamT(scoreT);
            displayBonusT(bombBonus, bombDefuse);
            displayBonusCT(defuse);
            roundUpdate();
        }
    }

    public void tBomb(View v) {

        if (!gameOver) {
            scoreT += 1;
            rowTRounds += 1;
            rowCTRounds = 0;
            int bombBonus = 250;
            int bombDefuse = 0;
            int defuse = 0;
            rounds.add(new Round("Terrorists", "bomb"));

            updateRoundHistoryT(bombBonus);
            displayForTeamT(scoreT);
            displayBonusT(bombBonus, bombDefuse);
            displayBonusCT(defuse);
            roundUpdate();
        }
    }

    public void ct5k(View v) {

        if (!gameOver) {
            scoreCT += 1;
            rowCTRounds += 1;
            rowTRounds = 0;
            int bombBonus = 0;
            int bombPlant = 0;
            int defuse = 0;
            boolean hasTimeRanOut = false;
            rounds.add(new Round("Counter-Terrorists", "5k"));

            updateRoundHistoryCT(defuse, hasTimeRanOut);
            displayForTeamCT(scoreCT);
            displayBonusT(bombBonus, bombPlant);
            displayBonusCT(defuse);
            roundUpdate();
        }
    }

    public void ctDefuse(View v) {

        if (!gameOver) {
            scoreCT += 1;
            rowCTRounds += 1;
            rowTRounds = 0;
            int bombBonus = 0;
            int bombPlant = 800;
            int defuse = 250;
            boolean hasTimeRanOut = false;
            rounds.add(new Round("Counter-Terrorists", "defuse"));

            updateRoundHistoryCT(defuse, hasTimeRanOut);
            displayForTeamCT(scoreCT);
            displayBonusT(bombBonus, bombPlant);
            displayBonusCT(defuse);
            roundUpdate();
        }
    }

    public void ctTime(View v) {

        if (!gameOver) {
            scoreCT += 1;
            rowCTRounds += 1;
            rowTRounds = 0;
            int bombBonus = 0;
            int bombPlant = 0;
            int defuse = 0;
            boolean timeRanOut = true;
            rounds.add(new Round("Counter-Terrorists", "time"));

            updateRoundHistoryCT(defuse, timeRanOut);
            displayForTeamCT(scoreCT);
            displayBonusT(bombBonus, bombPlant);
            displayBonusCT(defuse);
            roundUpdate();
        }
    }

    public void resetScore(View v) {

        Intent teamSelector = new Intent(ScoreTracker.this, TeamSelector.class);
        startActivity(teamSelector);

        /*View sidePick = findViewById(R.id.pick_side_layout);
        sidePick.setVisibility(View.VISIBLE);

        Button tBtn1 = (Button) findViewById(t5k);
        tBtn1.setVisibility(View.VISIBLE);
        Button tBtn2 = (Button) findViewById(R.id.t_bomb);
        tBtn2.setVisibility(View.VISIBLE);
        Button tBtn3 = (Button) findViewById(R.id.t_null);
        tBtn3.setVisibility(View.GONE);
        Button ctBtn1 = (Button) findViewById(R.id.ct5k);
        ctBtn1.setVisibility(View.VISIBLE);
        Button ctBtn2 = (Button) findViewById(R.id.ct_defuse);
        ctBtn2.setVisibility(View.VISIBLE);
        Button ctBtn3 = (Button) findViewById(R.id.ct_time);
        ctBtn3.setVisibility(View.VISIBLE);
        TextView tBonusView = (TextView) findViewById(R.id.team_a_bonus);
        tBonusView.setVisibility(View.VISIBLE);
        TextView ctBonusView = (TextView) findViewById(R.id.team_b_bonus);
        ctBonusView.setVisibility(View.VISIBLE);

        TextView teamAEcoBonusView = (TextView) findViewById(R.id.team_a_2eco_bonus);
        teamAEcoBonusView.setVisibility(View.VISIBLE);
        TextView teamBEcoBonusView = (TextView) findViewById(R.id.team_b_2eco_bonus);
        teamBEcoBonusView.setVisibility(View.VISIBLE);
        TextView teamAEcoView = (TextView) findViewById(R.id.team_a_eco_header);
        teamAEcoView.setVisibility(View.VISIBLE);
        TextView teamBEcoView = (TextView) findViewById(R.id.team_b_eco_header);
        teamBEcoView.setVisibility(View.VISIBLE);

        TextView teamASaveAmountView = (TextView) findViewById(R.id.team_a_save_amount);
        teamASaveAmountView.setVisibility(View.VISIBLE);
        TextView teamBSaveAmountView = (TextView) findViewById(R.id.team_b_save_amount);
        teamBSaveAmountView.setVisibility(View.VISIBLE);
        TextView teamASaveView = (TextView) findViewById(R.id.team_a_save_header);
        teamASaveView.setVisibility(View.VISIBLE);
        TextView teamBSaveView = (TextView) findViewById(R.id.team_b_save_header);
        teamBSaveView.setVisibility(View.VISIBLE);

        Button bombTimer = (Button) findViewById(R.id.bomb_trigger);
        bombTimer.setVisibility(View.GONE);*/
    }
}