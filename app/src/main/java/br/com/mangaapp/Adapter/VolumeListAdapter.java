package br.com.mangaapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.mangaapp.Model.Volume;
import br.com.mangaapp.R;

public class VolumeListAdapter extends BaseAdapter {

    private Activity activity;
    private Context mContext;
    private List<Volume> volumes;

    public VolumeListAdapter(Activity a, Context context, List<Volume> volumes) {
        activity = a;
        mContext = context;
        this.volumes = volumes;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return volumes.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return volumes.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return volumes.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.icone_texto_layout, parent, false);

        TextView nome = (TextView) vi.findViewById(R.id.txtTitle); // title
        ImageView icone = (ImageView) vi.findViewById(R.id.imgIcon); // thumb image

        nome.setText(volumes.get(position).getNomeDoVolume());
        icone.setImageResource(R.drawable.ic_right_arrow);
        return vi;
    }
}