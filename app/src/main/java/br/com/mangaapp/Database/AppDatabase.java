package br.com.mangaapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.mangaapp.Dao.EditoraDao;
import br.com.mangaapp.Dao.TituloDao;
import br.com.mangaapp.Dao.VolumeDao;
import br.com.mangaapp.Model.Editora;
import br.com.mangaapp.Model.Titulo;
import br.com.mangaapp.Model.Volume;

@Database(entities = {Editora.class, Titulo.class, Volume.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EditoraDao editoraDao();
    public abstract TituloDao tituloDao();
    public abstract VolumeDao volumeDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mangaApp")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
