package jonathanray.classyconversations;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JudgingScreen extends AppCompatActivity {

    private PlayerList playerList;
    private boolean juryMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judging_screen);
        Bundle b = getIntent().getExtras();
        playerList = b.getParcelable("players");
        setupPrefs();
        setJudgeText();
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
}
