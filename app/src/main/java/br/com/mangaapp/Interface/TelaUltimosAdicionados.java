package br.com.mangaapp.Interface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.mangaapp.Adapter.VolumeListAdapter;
import br.com.mangaapp.Model.Volume;
import br.com.mangaapp.R;
import br.com.mangaapp.Repository.VolumeRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class TelaUltimosAdicionados extends AppCompatActivity {

    @BindView(R.id.spin_qtde_de_volumes)
    Spinner spin_qtde;

    @BindView(R.id.list_volumes)
    ListView list_volumes;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<Volume> volumes = new ArrayList<>();
    private VolumeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_ultimos_adicionados);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.lbl_btn_ultimos);
        setSupportActionBar(toolbar);

        repository = new VolumeRepository(TelaUltimosAdicionados.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        volumes = repository.listar(1);
        populaTela();
    }

    private void populaTela() {
        VolumeListAdapter adapter = new VolumeListAdapter(this, this, volumes);
        list_volumes.setAdapter(adapter);
    }

   /* @OnItemSelected(R.id.spin_qtde_de_volumes)
    public void listarQtde(int position) {

        *//*String[] qtdes = getResources().getStringArray(R.array.qtde_volumes);
        int position = spin_qtde.getSelectedItemPosition();
*//*
        populaTela();
    }*/
}
