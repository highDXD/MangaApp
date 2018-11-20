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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.mangaapp.Adapter.VolumeDeletarListAdapter;
import br.com.mangaapp.Adapter.VolumeListAdapter;
import br.com.mangaapp.Model.Titulo;
import br.com.mangaapp.Model.Volume;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.VolumeRepository;
import br.com.mangaapp.Util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class TelaTituloSelecionado extends AppCompatActivity {

    @BindView(R.id.lbl_total_de_volumes_cadastrados)
    TextView lbl_total;

    @BindView(R.id.fab_add)
    FloatingActionButton fab_add;

    @BindView(R.id.list_volumes)
    ListView list_volumes;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_volume_pesquisa)
    EditText txt_volume_pesquisa;

    private Titulo titulo;

    private VolumeRepository volumeRepository;
    private List<Volume> volumes;

    private boolean deletar_selecionados;

    static final int SALVAR = Menu.FIRST;
    static final int DELETAR = Menu.FIRST + 1;
    static final int ATUALIZAR = Menu.FIRST + 2;
    static final int CANCELAR = Menu.FIRST + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_titulo_selecionado);
        ButterKnife.bind(this);

        try {
            titulo = (Titulo) getIntent().getSerializableExtra("titulo");
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
            Log.e("ERRO INTENT", e.getMessage());
            e.printStackTrace();
            finish();
        }

        toolbar.setTitle(titulo.getNome());
        setSupportActionBar(toolbar);

        volumeRepository = new VolumeRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deletar_selecionados = false;
        volumes = volumeRepository.listar(titulo.getId());
        populaTela();
    }


    private void populaTela() {
        int qtdeDeVolumesCadastrados = volumeRepository.contarVolumesCadastrados(titulo.getId());

        if (titulo.getNumTotalDeVolumes() == 0)
            lbl_total.setText("Volumes: " + qtdeDeVolumesCadastrados + "/??");
        else
            lbl_total.setText("Volumes: " + qtdeDeVolumesCadastrados + "/" + titulo.getNumTotalDeVolumes());

        VolumeListAdapter adapter = new VolumeListAdapter(this, this, volumes);
        list_volumes.setAdapter(adapter);
    }

    @OnItemSelected(R.id.list_volumes)
    public void chamarTelaDeVolumes(int position) {
        final Volume volume = volumes.get(position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Editar Volume");
        alertDialog.setMessage("Número do volume: ");

        final EditText input = new EditText(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.setText("" + volume.getNum());
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String strNum = input.getText().toString();

                        if (Utils.isCampoVazio(strNum)) {
                            Toast.makeText(TelaTituloSelecionado.this, "Campo vazio!", Toast.LENGTH_SHORT).show();
                        } else {
                            volume.setNum(Integer.parseInt(strNum));
                            volume.setNomeDoVolume(titulo.getNome() + " - " + strNum);

                            if (volumeRepository.atualizar(volume)) {

                                Toast.makeText(TelaTituloSelecionado.this, "Volume atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                                volumes = volumeRepository.listar(titulo.getId());
                                populaTela();
                            } else
                                Toast.makeText(TelaTituloSelecionado.this, "Houve um erro ao salvar as alterações!", Toast.LENGTH_SHORT).show();


                        }


                    }
                });
        alertDialog.show();

    }

    @OnClick(R.id.fab_add)
    public void dialogAdicionarVolume() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Novo volume");
        alertDialog.setMessage("Número do volume:");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Volume volume = new Volume();

                        String strNumVolume = input.getText().toString().trim();

                        if (Utils.isCampoVazio(strNumVolume)) {
                            Toast.makeText(TelaTituloSelecionado.this, "Campo vazio!", Toast.LENGTH_SHORT).show();
                        } else {
                            volume.setNum(Integer.parseInt(strNumVolume));
                            volume.setNomeDoVolume(titulo.getNome() + " - " + strNumVolume);
                            volume.setId_titulo(titulo.getId());

                            if (volumeRepository.salvar(volume)) {
                                Toast.makeText(TelaTituloSelecionado.this, "Volume salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                volumes = volumeRepository.listar(titulo.getId());
                                populaTela();
                            } else {
                                Toast.makeText(TelaTituloSelecionado.this, "Houve um erro ao salvar o volume!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        alertDialog.setNegativeButton("Cancelar", null);
        alertDialog.show();
    }

    @OnTextChanged(R.id.txt_volume_pesquisa)
    public void pesquisa() {
        String volume = txt_volume_pesquisa.getText().toString().trim();
        if (volume.equals("")) {
            volumes = volumeRepository.listar(titulo.getId());
        } else {
            volumes = volumeRepository.pesquisar(titulo.getId(), Integer.parseInt(volume));
        }
        populaTela();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.add(0, SALVAR, 0, "Novo Volume");
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
                dialogAdicionarVolume();
                return true;
            case DELETAR:
                if (deletar_selecionados) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("AVISO!");
                    dlg.setMessage("Tem certeza em deletar esses registros?");
                    dlg.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (list_volumes != null) {
                                VolumeDeletarListAdapter adapter = (VolumeDeletarListAdapter) list_volumes.getAdapter();

                                List<Volume> volumesParaExcluir = new ArrayList<>();

                                for (int i = 0; i < adapter.getCount(); i++) {
                                    if (adapter.isSelecionado(i)) {
                                        volumesParaExcluir.add(volumes.get(i));
                                    }
                                }

                                boolean res = volumeRepository.deletarLista(volumesParaExcluir);

                                if (res) {
                                    Toast.makeText(TelaTituloSelecionado.this, "Volumes deletados com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TelaTituloSelecionado.this, "Houve um erro ao deletar os volumes!", Toast.LENGTH_SHORT).show();
                                }

                                fab_add.setVisibility(View.VISIBLE);
                                deletar_selecionados = false;
                                volumes = volumeRepository.listar(titulo.getId());
                                populaTela();
                            }
                        }
                    });
                    dlg.show();
                } else {
                    VolumeDeletarListAdapter adapter = new VolumeDeletarListAdapter(this, this, volumes);
                    deletar_selecionados = true;
                    list_volumes.setAdapter(adapter);
                    fab_add.setVisibility(View.GONE);
                }
                return true;
            case ATUALIZAR:
                Toast.makeText(this, "Dê um clique em um item para editar!", Toast.LENGTH_LONG).show();
                return true;
            case CANCELAR:
                populaTela();
                fab_add.setVisibility(View.VISIBLE);
                deletar_selecionados = false;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
