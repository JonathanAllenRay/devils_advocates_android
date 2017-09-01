package jonathanray.classyconversations;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Player {
    private String playerName;
    private int roundsWon;
    private int roundsInGame;
    private int lastIndex;

    public Player(String name) {
        playerName = name;
        roundsWon = 0;
        roundsInGame = 0;
        lastIndex = 0;
    }

    protected Player(Parcel in) {
        playerName = in.readString();
        roundsWon = in.readInt();
        roundsInGame = in.readInt();
        lastIndex = in.readInt();
    }

    public void wonRound() {
        roundsWon++;
    }

    public void playedRound() {
        roundsInGame++;
    }

    public void setRoundsWon(int n) {
        roundsWon = n;
    }

    public void setRoundsInGame(int n) {
        roundsInGame = n;
    }

    public void setName(String name) {
        playerName = name;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public int getRoundsPlayed() {
        return roundsInGame;
    }

    public String getName() {
        return playerName;
    }

    public int getLastIndex() { return lastIndex; }

    public void setLastIndex(int num) { lastIndex = num; }
}
