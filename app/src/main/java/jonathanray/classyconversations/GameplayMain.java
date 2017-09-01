package jonathanray.classyconversations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameplayMain extends AppCompatActivity {

    private PlayerList playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_main);
        Bundle b = getIntent().getExtras();
        playerList = b.getParcelable("players");
    }
}
