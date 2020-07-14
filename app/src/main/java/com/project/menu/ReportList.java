package com.project.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.menu.adapter.AdapterProcessReport;
import com.project.menu.app.Controller;
import com.project.menu.data.ModelReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportList extends AppCompatActivity {

    String mainServer = ((Controller) this.getApplication()).getMainServer();
    private final String data_url = mainServer+"dat_laporan/getAll"; // kasih link prosesnya contoh : http://domainname or ip/folderproses/namaproses
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    ProgressDialog pd;
    ArrayList<ModelReport> mItems;
    FloatingActionButton addReport,btnRefresh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        addReport = findViewById(R.id.addReport);
        btnRefresh =  findViewById(R.id.btnRefresh);
        pd = new ProgressDialog(ReportList.this);
        mItems = new ArrayList<>();
        mAdapter = new AdapterProcessReport(ReportList.this, mItems);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_data_report);

        mManager = new LinearLayoutManager(ReportList.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        loadjson();


        mRecyclerView.setAdapter(mAdapter);

        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(ReportList.this, ReportForm.class)});
                finish();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadjson();
            }
        });

    };

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initializeCamera() {
        loadjson();
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        // put your code here...
//        loadjson();
//        Log.d("volley", "Resume call");
//    }

    //proses mengambil data
    public void loadjson(){
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        mItems.clear();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, data_url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                Log.d("volley", "response : " + response.toString());
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ModelReport md = new ModelReport();
                        Log.d("volley", "response data : " + data.getString("report_gardu_code"));
                        md.set_report_id(data.getString("report_id"));
                        md.set_report_seri_no(data.getString("report_seri_no"));
                        md.set_report_gardu_code(data.getString("report_gardu_code"));
                        md.set_report_first_image(data.getString("report_first_image"));
                        md.set_report_file_name(data.getString("report_file_name"));
                        md.set_report_location(data.getString("report_location"));
                        md.set_report_time(data.getString("report_time"));
                        md.set_report_first_condition(data.getString("report_first_condition"));
                        md.set_report_recent_image(data.getString("report_recent_image"));
                        md.set_report_recent_condition(data.getString("report_recent_condition"));
                        md.set_report_staf_name(data.getString("report_staf_name"));
                        // memanggil nama array yang kita buat
                        mItems.add(md);
                    } catch (JSONException e) {
                        Log.d("volley", "Failed ");
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