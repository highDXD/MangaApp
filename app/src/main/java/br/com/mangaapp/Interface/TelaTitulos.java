package br.com.mangaapp.Interface;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.mangaapp.Adapter.TituloDeletarListAdapter;
import br.com.mangaapp.Adapter.TituloListAdapter;
import br.com.mangaapp.Model.Editora;
import br.com.mangaapp.Model.Titulo;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.TituloRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnTextChanged;

public class TelaTitulos extends AppCompatActivity {

    @BindView(R.id.fab_add)
    FloatingActionButton fab_add;

    @BindView(R.id.list_titulos)
    ListView list_titulos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_nome_pesquisa)
    EditText txt_nome_pesquisa;

    private TituloRepository tituloRepository;
    private List<Titulo> titulos;

    private boolean deletar_selecionados;

    private Editora editora;

    static final int SALVAR = Menu.FIRST;
    static final int DELETAR = Menu.FIRST + 1;
    static final int ATUALIZAR = Menu.FIRST + 2;
    static final int CANCELAR = Menu.FIRST + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_titulos);
        ButterKnife.bind(this);

        try {
            editora = (Editora) getIntent().getSerializableExtra("editora");
        }catch (Exception e) {
            Toast.makeText(this, "Houve um erro ao recuperar os dados", Toast.LENGTH_SHORT).show();
            Log.e("ERRO INTENT", e.getMessage());
            e.printStackTrace();
            finish();
        }

        toolbar.setTitle(editora.getNome());
        setSupportActionBar(toolbar);

        tituloRepository = new TituloRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        titulos = tituloRepository.listar(editora.getId());
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

    @OnItemLongClick(R.id.list_titulos)
    public boolean editarTitulo(int position) {

        final Titulo titulo = titulos.get(position);

        Intent intent = new Intent(this, CrudTitulo.class);
        intent.putExtra("editora", editora);
        intent.putExtra("titulo", titulo);
        startActivity(intent);

        return true;
    }

    @OnClick(R.id.fab_add)
    public void adicionarTitulo() {
        Intent intent = new Intent(this, CrudTitulo.class);
        intent.putExtra("editora", editora);
        startActivity(intent);
    }

    @OnTextChanged(R.id.txt_nome_pesquisa)
    public void pesquisa() {
        String nome = txt_nome_pesquisa.getText().toString().trim();
        if (nome.equals("")) {
            titulos = tituloRepository.listar(editora.getId());
        } else {
            titulos = tituloRepository.pesquisar(editora.getId(), nome);
        }
        populaTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.add(0, SALVAR, 0, "Novo Título");
        menu.add(0, DELETAR, 0, "Deletar");
        menu.add(0, ATUALIZAR, 0, "Editar");
        menu.add(0, CANCELAR, 0, "Cancelar");

        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case SALVAR:
                adicionarTitulo();
                return true;
            case DELETAR:
                if (deletar_selecionados) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("AVISO!");
                    dlg.setMessage("Tem certeza em deletar esses registros?");
                    dlg.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (list_titulos != null) {
                                TituloDeletarListAdapter adapter = (TituloDeletarListAdapter) list_titulos.getAdapter();

                                List<Titulo> titulosParaExcluir = new ArrayList<>();

                                for (int i = 0; i < adapter.getCount(); i++) {
                                    if (adapter.isSelecionado(i)) {
                                        titulosParaExcluir.add(titulos.get(i));
                                    }
                                }

                                if (tituloRepository.deletarLista(titulosParaExcluir))
                                    Toast.makeText(TelaTitulos.this, "Titulos deletadas com sucesso!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(TelaTitulos.this, "Houve um erro ao deletar as titulos!", Toast.LENGTH_SHORT).show();

                                titulos = tituloRepository.listar(editora.getId());
                                populaTela();
                            }
                        }
                    });
                    dlg.show();
                } else {
                    deletar_selecionados = true;
                    TituloDeletarListAdapter adapter = new TituloDeletarListAdapter(this, this, titulos);
                    list_titulos.setAdapter(adapter);
                    fab_add.setVisibility(View.GONE);
                }
                return true;
            case ATUALIZAR:
                Toast.makeText(this, "Dê um clique longo em um item para editar!", Toast.LENGTH_LONG).show();
                return true;
            case CANCELAR:
                deletar_selecionados = false;
                populaTela();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
