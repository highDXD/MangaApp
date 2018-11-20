package br.com.mangaapp.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import br.com.mangaapp.Adapter.TituloListAdapter;
import br.com.mangaapp.Model.Titulo;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.TituloRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class TelaTodosOsTitulos extends AppCompatActivity {

    @BindView(R.id.list_titulos)
    ListView list_titulos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_nome_pesquisa)
    EditText txt_nome_pesquisa;

    private TituloRepository tituloRepository;
    private List<Titulo> titulos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_todos_os_titulos);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.lbl_btn_todos);
        setSupportActionBar(toolbar);

        tituloRepository = new TituloRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        titulos = tituloRepository.listar();
        populaTela();
    }


    private void populaTela() {
        TituloListAdapter adapter = new TituloListAdapter(this, this, titulos);
        list_titulos.setAdapter(adapter);
    }

    @OnItemClick(R.id.list_titulos)
    public void visualizarVolumes(int position) {
        Titulo titulo = titulos.get(position);
        Intent intent = new Intent(this, TelaTituloSelecionado.class);
        intent.putExtra("titulo", titulo);
        startActivity(intent);
    }

    @OnTextChanged(R.id.txt_nome_pesquisa)
    public void pesquisa() {
        String nome = txt_nome_pesquisa.getText().toString().trim();
        if (nome.equals("")) {
            titulos = tituloRepository.listar();
        } else {
            titulos = tituloRepository.pesquisar(nome);
        }
        populaTela();
    }
}
