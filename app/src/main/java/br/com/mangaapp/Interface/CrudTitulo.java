package br.com.mangaapp.Interface;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import br.com.mangaapp.Model.Editora;
import br.com.mangaapp.Model.Titulo;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.TituloRepository;
import butterknife.BindView;

public class CrudTitulo extends AppCompatActivity {


    private Editora editora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_titulo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.lbl_caracteristicas);
        setSupportActionBar(toolbar);

    }

}
