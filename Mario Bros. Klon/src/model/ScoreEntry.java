package model;

public class ScoreEntry {
    public String name;
    public long time;
    public int score;

    public ScoreEntry(String name, long time, int score) {
        this.name = name;
        this.time = time;
        this.score = score;
    }
}