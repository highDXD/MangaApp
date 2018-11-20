package br.com.mangaapp.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "Volumes",
        foreignKeys = {@ForeignKey(entity = Titulo.class,
                parentColumns = "id",
                childColumns = "id_titulo")},
        indices = {@Index(value = {"id"}, unique = true),
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

    @NonNull
    private String nomeDoVolume;

    public Volume() {
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

    @NonNull
    public String getNomeDoVolume() {
        return nomeDoVolume;
    }

    public void setNomeDoVolume(@NonNull String nomeDoVolume) {
        this.nomeDoVolume = nomeDoVolume;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", num=" + num +
                ", id_titulo=" + id_titulo +
                ", nomeDoVolume='" + nomeDoVolume + '\'' +
                '}';
    }
}
