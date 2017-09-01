package jonathanray.classyconversations;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

// Some code borrowed from
// http://www.anddev.org/novice-tutorials-f8/simple-tutorial-passing-arraylist-across-activities-t9996.html
public class PlayerList extends ArrayList<Player> implements Parcelable {

    int cursor;

    public PlayerList(){
        cursor = 0;
    }


    public PlayerList(Parcel in){
        this.clear();
        int size = in.readInt();
        this.cursor = in.readInt();

        for (int i = 0; i < size; i++) {
            Player player = new Player(in.readString());
            player.setRoundsWon(in.readInt());
            player.setRoundsInGame(in.readInt());
            this.add(player);
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        dest.writeInt(size);
        dest.writeInt(this.cursor);
        for (int i = 0; i < size; i++) {
            Player player = this.get(i);
            dest.writeString((player.getName()));
            dest.writeInt(player.getRoundsWon());
            dest.writeInt(player.getRoundsPlayed());
        }

    }

    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public PlayerList createFromParcel(Parcel in) { return new PlayerList(in); }

        public Object[] newArray(int arg) { return null; }
    };
}
