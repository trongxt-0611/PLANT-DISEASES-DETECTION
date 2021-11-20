package team.loser.plantdiseasedetection.Sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import team.loser.plantdiseasedetection.models.Disease;

@Database(entities = {Disease.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "history.db";
    private static HistoryDatabase instance;
    public static synchronized HistoryDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract HistoryDAO historyDAO();

}
