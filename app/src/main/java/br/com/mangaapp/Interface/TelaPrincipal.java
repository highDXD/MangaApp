package br.com.mangaapp.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.mangaapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelaPrincipal extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        ButterKnife.bind(this);
        
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btn_editoras)
    public void telaEditoras(){
        Intent intent = new Intent(this, TelaEditoras.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_todos)
    public void telaTodosOsTitulos(){
        Intent intent = new Intent(this, TelaTodosOsTitulos.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ultimos)
    public void telaUltimosAdicionados(){
        Intent intent = new Intent(this, TelaUltimosAdicionados.class);
        startActivity(intent);
    }

}
