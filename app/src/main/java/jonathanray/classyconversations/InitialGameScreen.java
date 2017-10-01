package jonathanray.classyconversations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;


public class InitialGameScreen extends AppCompatActivity {

    public static final int NAME_CHAR_LIMIT = 20;
    public static final int PLAYER_LIMIT = 16;
    public static final int UNDO_LIMIT = 8;
    public PlayerList playerList;
    public LinkedList<Player> undoQueue;
    public PlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_game_screen);
        playerList = new PlayerList();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            playerList = b.getParcelable("players");
        }
        undoQueue = new LinkedList<Player>();
        RecyclerView rvPlayerList = (RecyclerView) findViewById(R.id.playerListRV);
        // Create adapter passing in the sample user data
        adapter = new PlayerAdapter();
        // Attach the adapter to the recyclerview to populate items
        rvPlayerList.setAdapter(adapter);
        // Set layout manager to position the items
        rvPlayerList.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    public void undoRemoval(View view) {
        Player temp = null;
        if (undoQueue.size() > 0) {
            temp = undoQueue.removeFirst();
        }
        if (temp != null){
            playerList.add(0, temp);
            adapter.notifyItemInserted(0);
        }
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameplayMain.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("players", playerList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Add player method for the button
    public void addPlayer(View view) {
        undoQueue.clear();;
        TextView textView = (TextView) findViewById(R.id.nameInput);
        if (playerList.size() >= PLAYER_LIMIT) {
            textView.setText(R.string.player_name_too_many);
        } else {
            EditText editText = (EditText) findViewById(R.id.nameInput);
            String inputName = editText.getText().toString();
            // If name is invalid, display message. If it works, empty input box.
            if (inputName.length() > NAME_CHAR_LIMIT || inputName.length() <= 0 || isNameInUse(inputName)) {
                textView.setText(R.string.player_name_input_too_long);
            } else {
                // Successful add
                textView.setText(R.string.player_name_input);
                Player newGuy = new Player(inputName);
                playerList.add(0, newGuy);
                adapter.notifyItemInserted(0);
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

    // Code format adapted from https://guides.codepath.com/android/using-the-recyclerview
    public class PlayerAdapter extends
            RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

        @Override
        public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.rv_player_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(context, contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PlayerAdapter.ViewHolder viewHolder, int position) {
            Player player = playerList.get(position);

            TextView textView = viewHolder.nameTextView;
            textView.setText(player.getName());
            Button button = viewHolder.deleteButton;
            button.setText("Remove Player");
            button.setTag(this);
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView nameTextView;
            public Button deleteButton;
            private Context context;

            public ViewHolder(Context context, View itemView) {
                super(itemView);
                this.context = context;
                nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
                deleteButton = (Button) itemView.findViewById(R.id.remove_button);
                deleteButton.setOnClickListener(this);
            }

            // Function to remove player
            @Override
            public void onClick(View view) {
                int index = getAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    Player temp = playerList.remove(index);
                    temp.setLastIndex(index);
                    undoQueue.add(0, temp);
                    if (undoQueue.size() > UNDO_LIMIT) {
                        undoQueue.remove(undoQueue.size() - 1);
                    }
                    adapter.notifyItemRemoved(index);
                }
            }
        }
    }
}
