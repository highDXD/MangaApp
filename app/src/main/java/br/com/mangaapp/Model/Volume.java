package br.com.mangaapp.Model;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "Volumes",
        foreignKeys = {@ForeignKey( entity = Titulo.class,
                                    parentColumns = "id",
                                    childColumns = "id_titulo")},
        indices = { @Index(value = {"id"}, unique = true),
                    @Index(value = {"id_titulo"})})
public class Volume implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private int num;

    @NonNull
    @ColumnInfo(name = "id_titulo")
    private long id_titulo;

    public Volume() {
    }

    @Ignore
    public Volume(@NonNull int num) {
        this.num = num;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public int getNum() {
        return num;
    }

    public void setNum(@NonNull int num) {
        this.num = num;
    }

    @NonNull
    public long getId_titulo() {
        return id_titulo;
    }

    public void setId_titulo(@NonNull long id_titulo) {
        this.id_titulo = id_titulo;
    }
}
