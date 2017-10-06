package jonathanray.classyconversations;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JudgingScreen extends AppCompatActivity {

    private PlayerList playerList;
    private boolean juryMode;
    private boolean winnerPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging_screen);
        Bundle b = getIntent().getExtras();
        playerList = b.getParcelable("players");
        winnerPicked = false;
        setupPrefs();
        setupText();
    }

    private void setupPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        juryMode = prefs.getBoolean("jury_mode", false);
    }

    private void setJudgeText() {
        TextView judgeText = (TextView) findViewById(R.id.judgeText);
        Player judgeTemp = playerList.get(playerList.size() - 3);
        if (juryMode) {
            judgeText.setText(judgeTemp.getName() + " is the judge. Poll the other players to select" +
                    " a winner. If there is a tie, the judge votes to breaks the tie.");
        }
        else {
            judgeText.setText(judgeTemp.getName() + " is the judge. Select a winner.");
        }
    }

    private void setupText() {
        setJudgeText();
        Button playerOneButton = (Button) findViewById(R.id.playerOneWins);
        Button playerTwoButton = (Button) findViewById(R.id.playerTwoWins);
        playerOneButton.setText(playerList.get(playerList.size() - 1).getName() + " wins.");
        playerTwoButton.setText(playerList.get(playerList.size() - 2).getName() + " wins.");
    }

    // Pattern borrowed from https://stackoverflow.com/questions/3412180/
    // how-to-determine-which-button-pressed-on-android
    public void selectWinner(View view) {
        if (!winnerPicked) {
            switch(view.getId()) {
                case R.id.playerOneWins:
                    playerWins(true, view);
                    break;
                case R.id.playerTwoWins:
                    playerWins(false, view);
                    break;
                default:
                    throw new RuntimeException("Unknown button ID");
            }
        }
        winnerPicked = true;
    }

    private void playerWins(boolean isOne, View view) {
        TextView victoryText = (TextView) findViewById(R.id.victoryText);
        Player playerOne = playerList.get(playerList.size() - 1);
        Player playerTwo = playerList.get(playerList.size() - 2);
        if (isOne) {
            victoryText.setText(playerOne.getName() + " wins the round.");
            playerOne.wonRound();
            playerList.setLoserName(playerTwo.getName());
        }
        else {
            victoryText.setText(playerTwo.getName() + " wins the round.");
            playerTwo.wonRound();
            playerList.setLoserName(playerOne.getName());
        }
        Button toNextRound = (Button) findViewById(R.id.toNextRound);
        Button toPlayerScreen = (Button) findViewById(R.id.toPlayerScreen);
        toNextRound.setVisibility(View.VISIBLE);
        toPlayerScreen.setVisibility(View.VISIBLE);
    }
}
