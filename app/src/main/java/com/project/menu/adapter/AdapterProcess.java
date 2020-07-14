package com.project.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.menu.R;
import com.project.menu.data.ModelData;

import java.util.ArrayList;

public class AdapterProcess extends RecyclerView.Adapter<AdapterProcess.ViewProcessHolder> {

    Context context;
    private ArrayList<ModelData> item; //memanggil modelData

    public AdapterProcess(Context context, ArrayList<ModelData> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false); //memanggil layout list recyclerview
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewProcessHolder holder, int position) {

        final ModelData data = item.get(position);
        holder.nama_data.setText(data.getNamaData());
        holder.nama_nomorseri.setText(data.getNamaNomorSeri());
        holder.nama_alamat.setText(data.getNamaAlamat());
        holder.nama_gardu.setText(data.getNamaGardu());
        holder.nama_merk.setText(data.getNamaMerk());
        holder.nama_status.setText(data.getNamaStatus());
        holder.nama_kondisi.setText(data.getNamaKondisi());
        //menampilkan data
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        TextView nama_data;
        TextView nama_nomorseri;
        TextView nama_alamat;
        TextView nama_gardu;
        TextView nama_merk;
        TextView nama_status;
        TextView nama_kondisi;


        public ViewProcessHolder(View itemView) {
            super(itemView);

            nama_data = (TextView) itemView.findViewById(R.id.nama_data_list);
            nama_nomorseri = (TextView) itemView.findViewById(R.id.text_nomorseri);
            nama_alamat = (TextView) itemView.findViewById(R.id.text_subhead);
            nama_gardu = (TextView) itemView.findViewById(R.id.text_gardu);
            nama_merk = (TextView) itemView.findViewById(R.id.text_merk);
            nama_status = (TextView) itemView.findViewById(R.id.text_status);
            nama_kondisi = (TextView) itemView.findViewById(R.id.text_kondisi);
        }

    }
}
