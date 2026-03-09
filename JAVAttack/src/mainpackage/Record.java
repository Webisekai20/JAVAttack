package mainpackage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Record {
    private static final String Ranking = "ranks.csv";
    String timestamp;
    String playerName;
    int score;
    int level;
    void RecordStruct(String playerName, String timeStamp, int level, int score){
        this.playerName = playerName;
        this.timestamp = timeStamp;
        this.score = score;
        this.level = level;
    }
    ArrayList<RecordStruct> recordStructArr = new ArrayList<>();
    public static void SaveGame(String playerName, int score, int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Ranking, true))) {
                 String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.write(timestamp + "," + playerName + "," + score + "," + level);
                writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }sortRanks();
    }
    
    
    public static void sortRanks() {
        ArrayList<RecordStruct> recordStructArr = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Ranking))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String timestamp = parts[0];
                String playerName = parts[1];
                int score = Integer.parseInt(parts[2]);
                int level = Integer.parseInt(parts[3]);
                recordStructArr.add(new RecordStruct(playerName, timestamp, level, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(recordStructArr, (a, b) -> Integer.compare(b.score, a.score));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Ranking))) {
            for (RecordStruct data : recordStructArr) {
                writer.write(data.timestamp + "," + data.playerName + "," + data.score + "," + data.level);
                writer.newLine();
            }           
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<RecordStruct> loadGame(){
        ArrayList<RecordStruct> recordStructArr = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Ranking))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String timestamp = parts[0];
                String playerName = parts[1];
                int score = Integer.parseInt(parts[2]);
                int level = Integer.parseInt(parts[3]);
                recordStructArr.add(new RecordStruct(playerName, timestamp, level, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordStructArr.isEmpty()? null:new ArrayList<>(recordStructArr.subList(0, recordStructArr.size()<5? recordStructArr.size()-1:5));
    }
}
