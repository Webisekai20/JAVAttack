package mainpackage;

public class RecordStruct {
    String timestamp;
    String playerName;
    int score;
    int level;
    RecordStruct(String playerName, String timeStamp, int level, int score){
        this.playerName = playerName;
        this.timestamp = timeStamp;
        this.score = score;
        this.level = level;
    }
}
