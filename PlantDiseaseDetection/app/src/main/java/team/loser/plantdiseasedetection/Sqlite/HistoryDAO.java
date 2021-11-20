package team.loser.plantdiseasedetection.Sqlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import team.loser.plantdiseasedetection.models.Disease;

@Dao
public interface HistoryDAO {
    @Insert
    void insertDisease(Disease disease);
    @Query("SELECT * FROM disease")
    List<Disease> getListDiseases();
    @Delete
    void deleteDisease(Disease disease);
}
