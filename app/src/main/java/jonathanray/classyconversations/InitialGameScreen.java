package jonathanray.classyconversations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class InitialGameScreen extends AppCompatActivity {

    public static final int NAME_CHAR_LIMIT = 20;
    public static final int PLAYER_LIMIT = 16;
    public ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_game_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playerList = new ArrayList<Player>();
    }

    // Add player method for the button
    public void addPlayer(View view) {
        TextView textView = (TextView) findViewById(R.id.nameInput);
        if (playerList.size() >= PLAYER_LIMIT) {
            textView.setText(R.string.player_name_too_many);
        }
        else {
            EditText editText = (EditText) findViewById(R.id.nameInput);
            String inputName = editText.getText().toString();
            // If name is invalid, display message. If it works, empty input box.
            if (inputName.length() > NAME_CHAR_LIMIT || inputName.length() <= 0 || isNameInUse(inputName)) {
                textView.setText(R.string.player_name_input_too_long);
            } else {
                textView.setText(R.string.player_name_input);
                Player newGuy = new Player(inputName);
                playerList.add(newGuy);
                textView.setText("");
            }
        }
    }

    // Returns true if name is in use already
    public boolean isNameInUse(String name) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // Class to represent a player, keeps track of statistics for this game
    public class Player {
        private String playerName;
        private int roundsWon;
        private int roundsInGame;

        public Player(String name) {
            playerName = name;
            roundsWon = 0;
            roundsInGame = 0;
        }

        public void wonRound() {
            roundsWon++;
        }

        public void playedRound() {
            roundsInGame++;
        }

        public String getName() {
            return playerName;
        }
    }
}
