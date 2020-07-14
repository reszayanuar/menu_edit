package com.project.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.project.menu.adapter.AdapterProcess;
import com.project.menu.app.Controller;
import com.project.menu.data.ModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Data extends AppCompatActivity {
    String mainServer = ((Controller) this.getApplication()).getMainServer();
    private final String data_url = mainServer+"data_trafo/getAll"; // kasih link prosesnya contoh : http://domainname or ip/folderproses/namaproses

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    ProgressDialog pd;
    ArrayList<ModelData> mItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        pd = new ProgressDialog(Data.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_data);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(Data.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new AdapterProcess(Data.this, mItems);
        mRecyclerView.setAdapter(mAdapter);

        loadjson();
    }

    //proses mengambil data
    private void loadjson(){
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, data_url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                Log.d("volley", "response : " + response.toString());
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ModelData md = new ModelData();
                        md.setNamaData(data.getString("kd_gardu"));
                        md.setNamaNomorSeri(data.getString("nomor_seri"));
                        md.setNamaAlamat(data.getString("alamat"));
                        md.setNamaGardu(data.getString("jenis"));
                        md.setNamaMerk(data.getString("merk"));
                        md.setNamaStatus(data.getString("status"));
                        md.setNamaKondisi(data.getString("kondisi"));// memanggil nama array yang kita buat
                        mItems.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
            }
        });
        Controller.getInstance().addToRequestQueue(arrayRequest);
    }

}