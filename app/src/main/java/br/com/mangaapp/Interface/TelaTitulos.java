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
import android.widget.LinearLayout;
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
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
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
        } catch (Exception e) {
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
        deletar_selecionados = false;
        titulos = tituloRepository.listar(editora.getId());
        populaTela();
    }


    private void populaTela() {
        TituloListAdapter adapter = new TituloListAdapter(this, this, titulos);
        list_titulos.setAdapter(adapter);
    }

    @OnItemSelected(R.id.list_titulos)
    public void visualizarVolumes(int position) {
        Titulo titulo = titulos.get(position);
        Intent intent = new Intent(this, TelaTituloSelecionado.class);
        intent.putExtra("titulo", titulo);
        startActivity(intent);
    }

    @OnItemLongClick(R.id.list_titulos)
    public boolean editarTitulo(int position) {

        final Titulo titulo = titulos.get(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Editar Titulo");
        alertDialog.setMessage(R.string.hint_txt_pesquisa_titulo);

        final EditText input = new EditText(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.setText(titulo.getNome());
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        titulo.setNome(input.getText().toString());

                        boolean res = tituloRepository.atualizar(titulo);

                        if (res)
                            Toast.makeText(TelaTitulos.this, "Titulo atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(TelaTitulos.this, "Houve um erro ao salvar as alterações!", Toast.LENGTH_SHORT).show();

                        titulos = tituloRepository.listar(editora.getId());
                        populaTela();
                    }
                });
        alertDialog.show();


        return true;
    }

    @OnClick(R.id.fab_add)
    public void dialogAdicionarTitulo() {
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
                dialogAdicionarTitulo();
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

                                boolean res = tituloRepository.deletarLista(titulosParaExcluir);

                                if (res) {
                                    Toast.makeText(TelaTitulos.this, "Titulos deletadas com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TelaTitulos.this, "Houve um erro ao deletar as titulos!", Toast.LENGTH_SHORT).show();
                                }

                                deletar_selecionados = false;
                                titulos = tituloRepository.listar(editora.getId());
                                populaTela();
                            }
                        }
                    });
                    dlg.show();
                } else {
                    TituloDeletarListAdapter adapter = new TituloDeletarListAdapter(this, this, titulos);
                    deletar_selecionados = true;
                    list_titulos.setAdapter(adapter);
                    fab_add.setVisibility(View.GONE);
                }
                return true;
            case ATUALIZAR:
                Toast.makeText(this, "Dê um clique longo em um item para editar!", Toast.LENGTH_LONG).show();
                return true;
            case CANCELAR:
                populaTela();
                deletar_selecionados = false;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
