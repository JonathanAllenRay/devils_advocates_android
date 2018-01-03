package jonathanray.classyconversations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameplayMain extends AppCompatActivity {

    public static final int MIN_PLAYERS = 3;
    public static final float TEXT_SIZE_NOT_ENOUGH_DUDES = 20;

    private boolean enoughPlayers;
    private PlayerList playerList;
    private Player playerOne;
    private Player playerTwo;
    private Player judge;

    private boolean randomPlayers;
    private boolean randomJudge;
    private boolean loserStays;
    private boolean juryMode;
    private int timeLimit;

    private boolean firstTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gameplay_main);
        Bundle b = getIntent().getExtras();
        playerList = b.getParcelable("players");
        firstTurn = true;
        TextView clockText = (TextView) findViewById(R.id.countClock);
        TextView textView = (TextView) findViewById(R.id.titleView);
        textView.setText("Round " + playerList.getRoundNum());
        if (playerList.size() < MIN_PLAYERS) {
            enoughPlayers = false;
        }
        else {
            enoughPlayers = true;
        }
        setupPrefs();
        if (!enoughPlayers) {
            TextView preText = (TextView) findViewById(R.id.preround_text);
            preText.setVisibility(View.INVISIBLE);
            textView.setTextSize(TEXT_SIZE_NOT_ENOUGH_DUDES);
            textView.setText("Round " + playerList.getRoundNum() +": Insufficient players, 3 or more" +
                    " required to play. Return to player screen and add more players.");
            Button playerButton = (Button)findViewById(R.id.playerListButton);
            playerButton.setVisibility(View.VISIBLE);
            Button startTurn = (Button)findViewById(R.id.startTurn);
            startTurn.setVisibility(View.GONE);
        }
        else {
            if (randomPlayers) {
                selectPlayersRandom();
            }
            else {
                selectPlayersOrder();
            }
            setupPreRoundText();
            playerOne.playedRound();
            playerTwo.playedRound();
        }
        setupClock();
    }

    // Some code borrowed from http://abhiandroid.com/ui/countdown-timer
    private void setupClock() {
        final TextView clockText = (TextView) findViewById(R.id.countClock);
        final Button clockButton = (Button) findViewById(R.id.startTurn);
        final TextView topicText = (TextView) findViewById(R.id.topicView);
        clockText.setVisibility(View.GONE);
        clockButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                topicText.setText("Tell us why: " + pickNewTopic());
                final TextView playersTurn = (TextView) findViewById(R.id.playersTurn);
                clockText.setVisibility(View.VISIBLE);
                clockButton.setVisibility(View.GONE);
                new CountDownTimer(timeLimit*1000, 1000){
                    public void onTick(long millisUntilFinished){
                        clockText.setText("Time left: " + millisUntilFinished / 1000);
                    }
                    public void onFinish(){
                        clockText.setText("Done. Click start turn to continue.");
                        clockButton.setVisibility(View.VISIBLE);
                        if (firstTurn) {
                            firstTurn = false;
                            playersTurn.setText(playerTwo.getName() + "'s turn");
                        }
                        else {
                            goToEndRound();
                        }
                    }
                }.start();
            }
        });
    }

    private String pickNewTopic() {
        List<String> tempTopics = Arrays.asList(getResources().getStringArray(R.array.game_topics));
        Random r = new Random();
        return tempTopics.get(r.nextInt(tempTopics.size()));
    }

    public void onBackPressed() {
        // nothing
    }
    //Send Player List back to list edit screen
    public void returnToPlayerScreen(View view) {
        Intent intent = new Intent(this, InitialGameScreen.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("players", playerList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // This isn't working, need to fix this shieeet
    private void setupPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        randomPlayers = prefs.getBoolean("random_players", true);
        randomJudge = prefs.getBoolean("random_judge", true);
        loserStays = prefs.getBoolean("loser_stays", true);
        juryMode = prefs.getBoolean("jury_mode", false);
        timeLimit = Integer.valueOf(prefs.getString("round_time", "20"));
    }

    private void selectPlayersRandom() {
        assert(playerList.size() >= MIN_PLAYERS);
        Random random = new Random();
        int num = random.nextInt(playerList.size());
        playerOne = playerList.remove(num);
        num = random.nextInt(playerList.size());;
        playerTwo = playerList.remove(num);
        getRandomJudge();
    }

    private void selectPlayersOrder() {
        assert(playerList.size() >= MIN_PLAYERS);
        playerOne = playerList.remove(0);
        playerTwo = playerList.remove(0);
        getRandomJudge();
    }

    private void setupPreRoundText() {
        TextView textView = (TextView) findViewById(R.id.preround_text);
        String jury = "";
        if (juryMode) {
            jury = " + Jury";
        }
        textView.setText("Players: \n" + playerOne.getName() + "\nvs.\n" + playerTwo.getName()
            + "\n\nJudge: " + judge.getName() + jury);
        TextView playersTurn = (TextView) findViewById(R.id.playersTurn);
        playersTurn.setText(playerOne.getName() + "'s turn");
        playersTurn.setVisibility(View.VISIBLE);
    }

    // Originally I planned to have in order and random judge selection, but
    // for now, I'm just going to implement random judge order to make it easier.
    private void getRandomJudge() {
        Random random = new Random();
        int num = random.nextInt(playerList.size());
        judge = playerList.remove(num);
    }

    private void goToEndRound() {
        playerList.add(judge);
        playerList.add(playerTwo);
        playerList.add(playerOne);
        Intent intent = new Intent(this, JudgingScreen.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("players", playerList);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
