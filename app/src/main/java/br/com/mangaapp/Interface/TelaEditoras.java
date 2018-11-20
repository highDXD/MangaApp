package br.com.mangaapp.Interface;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.mangaapp.Adapter.EditoraDeletarListAdapter;
import br.com.mangaapp.Adapter.EditoraListAdapter;
import br.com.mangaapp.Model.Editora;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.EditoraRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnTextChanged;

public class TelaEditoras extends AppCompatActivity {
    @BindView(R.id.fab_add)
    FloatingActionButton fab_add;

    @BindView(R.id.list_editoras)
    ListView list_editoras;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_nome_pesquisa)
    EditText txt_nome_pesquisa;

    private EditoraRepository editoraRepository;
    private List<Editora> editoras;

    private boolean deletar_selecionados;

    static final int SALVAR = Menu.FIRST;
    static final int DELETAR = Menu.FIRST + 1;
    static final int ATUALIZAR = Menu.FIRST + 2;
    static final int CANCELAR = Menu.FIRST + 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_editoras);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.lbl_btn_editoras);
        setSupportActionBar(toolbar);

        editoraRepository = new EditoraRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deletar_selecionados = false;
        editoras = editoraRepository.listar();
        populaTela();
    }


    private void populaTela() {
        EditoraListAdapter adapter = new EditoraListAdapter(this, this, editoras);
        list_editoras.setAdapter(adapter);
    }

    @OnItemClick(R.id.list_editoras)
    public void chamarTelaDeTitulos(int position) {
        Editora editora = editoras.get(position);
        Toast.makeText(this, editora.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TelaTitulos.class);
        intent.putExtra("editora", editora);
        startActivity(intent);
    }


    @OnItemLongClick(R.id.list_editoras)
    public boolean editarEditora(int position) {

        final Editora editora = editoras.get(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Editar Editora");
        alertDialog.setMessage(R.string.hint_txt_pesquisa_editora);

        final EditText input = new EditText(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.setText(editora.getNome());
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        editora.setNome(input.getText().toString());

                        boolean res = editoraRepository.atualizar(editora);

                        if (res)
                            Toast.makeText(TelaEditoras.this, "Editora atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(TelaEditoras.this, "Houve um erro ao salvar as alterações!", Toast.LENGTH_SHORT).show();

                        editoras = editoraRepository.listar();
                        populaTela();
                    }
                });
        alertDialog.show();


        return true;
    }

    @OnClick(R.id.fab_add)
    public void dialogAdicionarEditora() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Nova editora");
        alertDialog.setMessage("Nome da editora:");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Editora editora = new Editora();
                        editora.setNome(input.getText().toString().trim());

                        boolean res = editoraRepository.salvar(editora);

                        if (res) {
                            Toast.makeText(TelaEditoras.this, "Editora salva com sucesso!", Toast.LENGTH_SHORT).show();
                            editoras = editoraRepository.listar();
                            populaTela();
                        } else {
                            Toast.makeText(TelaEditoras.this, "Houve um erro ao salvar a editora!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        alertDialog.setNegativeButton("Cancelar", null);
        alertDialog.show();
    }

    @OnTextChanged(R.id.txt_nome_pesquisa)
    public void pesquisa() {
        String nome = txt_nome_pesquisa.getText().toString().trim();
        if (nome.equals("")) {
            editoras = editoraRepository.listar();
        } else {
            editoras = editoraRepository.pesquisar(nome);
        }
        populaTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.add(0, SALVAR, 0, "Nova Editora");
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
                dialogAdicionarEditora();
                return true;
            case DELETAR:
                if (deletar_selecionados) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("AVISO!");
                    dlg.setMessage("Tem certeza em deletar esses registros?");
                    dlg.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (list_editoras != null) {
                                EditoraDeletarListAdapter adapter = (EditoraDeletarListAdapter) list_editoras.getAdapter();

                                List<Editora> editorasParaExcluir = new ArrayList<>();

                                for (int i = 0; i < adapter.getCount(); i++) {
                                    if (adapter.isSelecionado(i)) {
                                        editorasParaExcluir.add(editoras.get(i));
                                    }
                                }

                                boolean res = editoraRepository.deletarLista(editorasParaExcluir);

                                if (res) {
                                    Toast.makeText(TelaEditoras.this, "Editoras deletadas com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TelaEditoras.this, "Houve um erro ao deletar as editoras!", Toast.LENGTH_SHORT).show();
                                }

                                deletar_selecionados = false;
                                editoras = editoraRepository.listar();
                                populaTela();
                            }
                        }
                    });
                    dlg.show();
                } else {
                    EditoraDeletarListAdapter adapter = new EditoraDeletarListAdapter(this, this, editoras);
                    deletar_selecionados = true;
                    list_editoras.setAdapter(adapter);
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
