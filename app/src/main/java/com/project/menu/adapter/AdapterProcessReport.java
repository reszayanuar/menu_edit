package com.project.menu.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.menu.R;
import com.project.menu.ReportFormEdit;
import com.project.menu.app.Controller;
import com.project.menu.data.ModelReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterProcessReport extends RecyclerView.Adapter<AdapterProcessReport.ViewProcessHolder> {

    Context context;
    private ArrayList<ModelReport> item; //memanggil modelReport

    public AdapterProcessReport(Context context, ArrayList<ModelReport> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false); //memanggil layout list recyclerview
        ViewProcessHolder processHolder = new ViewProcessHolder(view);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(ViewProcessHolder holder, int position) {

        final ModelReport data = item.get(position);
        Log.d("lat", data.toString());
//        holder.nama_data.setText(data.getNamaData());
//        holder.nama_alamat.setText(data.getNamaAlamat());//menampilkan data
        holder.report_id.setText(data.get_report_id());
        holder.report_seri_no.setText(data.get_report_seri_no());
        holder.report_gardu_code.setText(data.get_report_gardu_code());
//        holder.report_first_image.setText(data.get_report_first_image());
//        holder.report_file_name.setText(data.get_report_file_name());
        holder.report_location.setText(data.get_report_location());
        holder.report_time.setText(data.get_report_time().toString());

//        holder.report_first_condition.setText(data.get_report_first_condition());
//        holder.report_recent_image.setText(data.get_report_recent_image());
//        holder.report_recent_condition.setText(data.get_report_recent_condition());
//        holder.report_staf_name.setText(data.get_report_staf_name());
    }

    public CharSequence timeFormat(String inputTime) throws ParseException {
//        return android.text.format.DateFormat.format("EEE, d MMM yyyy HH:mm:ss", Long.parseLong(inputTime));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = fmt.parse(inputTime);

        SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        return fmtOut.format(date);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder {

        TextView report_id;
        TextView report_seri_no ;
        TextView report_gardu_code ;
        TextView report_first_image ;
        TextView report_file_name ;
        TextView report_location ;
        TextView report_time ;
        TextView report_first_condition ;
        TextView report_recent_image ;
        TextView report_recent_condition ;
        TextView report_staf_name ;

        public ViewProcessHolder(final View itemView) {
            super(itemView);
//            nama_data = (TextView) itemView.findViewById(R.id.nama_data_list);
            report_id =  (TextView) itemView.findViewById(R.id.report_id);
            report_seri_no =  (TextView) itemView.findViewById(R.id.report_seri_no);
            report_gardu_code =  (TextView) itemView.findViewById(R.id.report_gardu_code);
//            report_first_image =  (TextView) itemView.findViewById(R.id.report_first_image);
//            report_file_name =  (TextView) itemView.findViewById(R.id.report_file_name);
            report_location =  (TextView) itemView.findViewById(R.id.report_location);
            report_time =  (TextView) itemView.findViewById(R.id.report_time);
//            report_first_condition =  (TextView) itemView.findViewById(R.id.report_first_condition);
//            report_recent_image =  (TextView) itemView.findViewById(R.id.report_recent_image);
//            report_recent_condition =  (TextView) itemView.findViewById(R.id.report_recent_condition);
//            report_staf_name =  (TextView) itemView.findViewById(R.id.report_staf_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] pilihan = {"Edit Record", "Hapus Record"};
                    final String Rid = report_id.getText().toString();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Aksi Lanjutan");
                    builder.setItems(pilihan, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String result = Rid;
                            String mode  = "";
                            if(pilihan[which] == "Edit Record"){
                                mode ="edit";
                                Intent i = new Intent(context, ReportFormEdit.class);
                                i.putExtra("rid",result);
                                context.startActivity(i);
                            }else{
                                delete(result);

                                mode = "Hapus";
                            }
//                            Toast.makeText(context,mode + result , Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

//                    Toast.makeText(v.getContext(), "Position is", Toast.LENGTH_SHORT).show();
                    return false;
                }

            });

        }



        private void delete(final String rid) {
            String mainServer = Controller.getMainServer();
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle("Login Account");
            progressDialog.show();
            String uRl = mainServer+"dat_laporan/delete_mobile/"+rid;
            StringRequest request = new StringRequest(Request.Method.GET, uRl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Error",response.toString());
                    progressDialog.dismiss();
                    Toast.makeText(context, "Reload untuk melihat perubahan", Toast.LENGTH_LONG).show();
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error",error.toString());
                    progressDialog.dismiss();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(context).addToRequestQueue(request);

        }

    }
}
