package br.com.mangaapp.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "Titulos",
        foreignKeys = {@ForeignKey( entity = Editora.class,
                                    parentColumns = "id",
                                    childColumns = "id_editora")},
        indices = { @Index(value = {"id"}, unique = true),
                    @Index(value = {"id_editora"})})
public class Titulo implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "id_editora")
    private long id_editora;

    @NonNull
    private String nome;

    @NonNull
    private String tipoDeTitulo;

    private int numTotalDeVolumes;

    public Titulo() {
    }

    @Ignore
    public Titulo(@NonNull long id_editora, @NonNull String nome, @NonNull String tipoDeTitulo, int numTotalDeVolumes) {
        this.id_editora = id_editora;
        this.nome = nome;
        this.tipoDeTitulo = tipoDeTitulo;
        this.numTotalDeVolumes = numTotalDeVolumes;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getId_editora() {
        return id_editora;
    }

    public void setId_editora(@NonNull long id_editora) {
        this.id_editora = id_editora;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    @NonNull
    public String getTipoDeTitulo() {
        return tipoDeTitulo;
    }

    public void setTipoDeTitulo(@NonNull String tipoDeTitulo) {
        this.tipoDeTitulo = tipoDeTitulo;
    }

    public int getNumTotalDeVolumes() {
        return numTotalDeVolumes;
    }

    public void setNumTotalDeVolumes(int numTotalDeVolumes) {
        this.numTotalDeVolumes = numTotalDeVolumes;
    }


    @Override
    public String toString() {
        return "Titulo{" +
                "id=" + id +
                ", id_editora=" + id_editora +
                ", nome='" + nome + '\'' +
                ", tipoDeTitulo='" + tipoDeTitulo + '\'' +
                ", numTotalDeVolumes=" + numTotalDeVolumes +
                '}';
    }
}
