package team.loser.plantdiseasedetection.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "disease")
public class Disease {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "disease_name")
    private String response_class;
    @ColumnInfo(name = "confident")
    private String response_confident;
    @ColumnInfo(name = "time_stamp")
    private String response_time;

    public Disease(String response_class, String response_confident, String response_time) {
        this.response_class = response_class;
        this.response_confident = response_confident;
        this.response_time = response_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponse_class() {
        return response_class;
    }

    public void setResponse_class(String response_class) {
        this.response_class = response_class;
    }

    public String getResponse_confident() {
        return response_confident;
    }

    public void setResponse_confident(String response_confident) {
        this.response_confident = response_confident;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }
}
