package jonathanray.classyconversations;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameplayMain extends AppCompatActivity {

    private PlayerList playerList;

    boolean randomPlayers;
    boolean randomJudge;
    boolean loserStays;
    boolean juryMode;
    int timeLimit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gameplay_main);
        Bundle b = getIntent().getExtras();
        playerList = b.getParcelable("players");
        TextView textView = (TextView) findViewById(R.id.titleView);
        textView.setText("Round " + playerList.getRoundNum());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        randomPlayers = prefs.getBoolean("random_players", true);
        randomJudge = prefs.getBoolean("random_judge", true);
        loserStays = prefs.getBoolean("loser_stays", true);
        juryMode = prefs.getBoolean("jury_mode", true);
        timeLimit = Integer.valueOf(prefs.getString("round_time", "20"));
        if (playerList.size() < 3) {
            textView.setText("Round " + playerList.getRoundNum() +": Insufficient players, 3 or more" +
                    " required to play. Return to player screen and add more players.");
        }
    }
}
