package br.com.mangaapp.Repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import br.com.mangaapp.Database.AppDatabase;
import br.com.mangaapp.Model.Volume;

public class VolumeRepository {

    private final Context context;

    public VolumeRepository(Context context) {
        this.context = context;
    }

    public boolean salvar(Volume volume) {
        try {
            return new Salvar().execute(volume).get();
        } catch (Exception e) {
            Log.e("ERRO REPO VOLUME", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(Volume volume) {
        try {
            return new Deletar().execute(volume).get();
        } catch (Exception e) {
            Log.e("ERRO REPO VOLUME", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Volume volume) {
        try {
            return new Atualizar().execute(volume).get();
        } catch (Exception e) {
            Log.e("ERRO REPO VOLUME", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Volume> pesquisar(long id_titulo, int num) {
        try {
            return new Pesquisar(id_titulo).execute(num).get();
        } catch (Exception e) {
            Log.e("ERRO REPO VOLUME", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Volume> listar(long id_titulo) {
        try {
            return new ListarTodos().execute(id_titulo).get();
        } catch (Exception e) {
            Log.e("ERRO REPO VOLUME", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private class Salvar extends AsyncTask<Volume, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Volume... volumes) {
            try {
                AppDatabase.getAppDatabase(context).volumeDao().salvar(volumes[0]);
                return true;
            } catch (Exception e) {
                Log.e("ERRO ASYNC VOLUME", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private class Deletar extends AsyncTask<Volume, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Volume... volumes) {
            try {
                AppDatabase.getAppDatabase(context).volumeDao().deletar(volumes[0]);
                return true;
            } catch (Exception e) {
                Log.e("ERRO ASYNC VOLUME", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private class Atualizar extends AsyncTask<Volume, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Volume... volumes) {
            try {
                AppDatabase.getAppDatabase(context).volumeDao().atualizar(volumes[0]);
                return true;
            } catch (Exception e) {
                Log.e("ERRO ASYNC VOLUME", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }

    private class Pesquisar extends AsyncTask<Integer, Void, List<Volume>> {
        long id_titulo;

        public Pesquisar(long id_titulo) {
            this.id_titulo = id_titulo;
        }

        @Override
        protected List<Volume> doInBackground(Integer... integers) {
            try {
                return AppDatabase.getAppDatabase(context).volumeDao().pesquisar(integers[0], id_titulo);
            } catch (Exception e) {
                Log.e("ERRO ASYNC VOLUME", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }


    private class ListarTodos extends AsyncTask<Long, Void, List<Volume>> {

        @Override
        protected List<Volume> doInBackground(Long... longs) {
            try {
                return AppDatabase.getAppDatabase(context).volumeDao().getAllFromTitulo(longs[0]);
            } catch (Exception e) {
                Log.e("ERRO ASYNC VOLUME", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
