package com.project.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidquery.AQuery;
import com.project.menu.adapter.MySingleton;
import com.project.menu.app.Controller;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReportFormEdit extends AppCompatActivity implements AsyncTaskCompleteListener {
    private static final int IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int URUTAN_CAPTURE = 1;

    private ParseContent parseContent;
    private static final String IMAGE_DIRECTORY = "/camera_upload";
    private final int CAMERA = 1;
    private AQuery aQuery;
    String R_id = "1";

    MaterialEditText    report_seri_no,
                        report_gardu_code,
                        report_first_image,
                        report_file_name,
                        report_location,
                        report_time,
                        report_first_condition,
                        report_recent_image,
                        report_recent_condition,
                        report_staf_name;
    Button kirim;
    ImageView img_thumb1, img_thumb2;
    Bitmap bitmap;

    private String Document_img1="";
    private String Document_img2="";
    private Object AsyncTaskCompleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form_edit);
        String Rid = "1";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Rid = extras.getString("rid");
            R_id = Rid;
            //The key argument here must match that used in the other activity
        }

        parseContent = new ParseContent(this);
        aQuery = new AQuery(this);

        report_seri_no          = findViewById(R.id.report_seri_no);
        report_gardu_code       = findViewById(R.id.report_gardu_code);
        report_first_image      = findViewById(R.id.report_first_image);
        report_file_name        = findViewById(R.id.report_file_name);
        report_location         = findViewById(R.id.report_location);
        report_time             = findViewById(R.id.report_time);
        report_first_condition  = findViewById(R.id.report_first_condition);
        report_recent_image     = findViewById(R.id.report_recent_image);
        report_recent_condition = findViewById(R.id.report_recent_condition);
        report_staf_name        = findViewById(R.id.report_staf_name);
        kirim                   = findViewById(R.id.kirim);
        img_thumb1              = findViewById(R.id.img_thumb1);
        img_thumb2              = findViewById(R.id.img_thumb2);

        readData(Rid);

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_report_seri_no = report_seri_no.getText().toString();
                String text_report_gardu_code = report_gardu_code.getText().toString();
//                String text_report_first_image = report_gardu_code.getText().toString();
                String text_report_file_name = report_file_name.getText().toString();
                String text_report_location = report_location.getText().toString();
                String text_report_time = report_time.getText().toString();
                String text_report_first_condition = report_time.getText().toString();
//                String text_report_recent_image = report_first_condition.getText().toString();
                String text_report_recent_condition = report_recent_condition.getText().toString();
                String text_report_staf_name = report_staf_name.getText().toString();
                Log.d("String",text_report_seri_no);
                Log.d("String",report_seri_no.getText().toString());
                if(
                       TextUtils.isEmpty(text_report_seri_no) ||
                       TextUtils.isEmpty(text_report_gardu_code) ||
                       TextUtils.isEmpty(text_report_file_name) ||
                       TextUtils.isEmpty(text_report_location) ||
                       TextUtils.isEmpty(text_report_first_condition) ||
                       TextUtils.isEmpty(text_report_recent_condition) ||
                       TextUtils.isEmpty(text_report_staf_name)
                )
                {
                    Toast.makeText(ReportFormEdit.this, "All Field Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        uploadImageToServer();
                    } catch (IOException e) {
                        Log.d("err 1",e.toString());
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.d("err 2",e.toString());
                        e.printStackTrace();
                    }
                }
            }

        });

        img_thumb1 = (ImageView) findViewById(R.id.img_thumb1);
        img_thumb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(1);
            }
        });

        img_thumb2 = (ImageView) findViewById(R.id.img_thumb2);
        img_thumb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(2);
            }
        });
    }

    private void readData(String rid) {
        final String mainServer = ((Controller) this.getApplication()).getMainServer();
        final ProgressDialog progressDialog = new ProgressDialog(ReportFormEdit.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Read Data ...");
        progressDialog.show();
        String uRl = mainServer + "dat_laporan/getData/"+rid;
        StringRequest request = new StringRequest(Request.Method.GET, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject reportObj = new JSONObject(response);
//                    String title = reportObj.getString("Title");
                    if(reportObj.getBoolean("success")){
                        JSONObject data = new JSONObject(reportObj.getString("data"));


                        report_seri_no.setText(data.getString("report_seri_no"));
                        report_gardu_code.setText(data.getString("report_gardu_code"));
                        report_first_image.setText(data.getString("report_file_name"));
                        String url_img1 = mainServer+"assets/img/"+data.getString("report_first_image");
                        Picasso.get().load(url_img1).into(img_thumb1);
                        report_file_name.setText(data.getString("report_file_name"));
                        report_location.setText(data.getString("report_location"));
                        report_time.setText(data.getString("report_time").toString());
                        report_first_condition.setText(data.getString("report_first_condition"));
                        String url_img2 = mainServer+"assets/img/"+data.getString("report_recent_image");
                        Picasso.get().load(url_img2).into(img_thumb2);
                        report_recent_image.setText(data.getString("report_file_name"));
                        report_recent_condition.setText(data.getString("report_recent_condition"));
                        report_staf_name.setText(data.getString("report_staf_name"));

                    }else{

                    }
                    Log.d("Response", reportObj.toString());
                }catch (JSONException e){
                    Log.d("Response Err", e.toString());
                }
                progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                progressDialog.dismiss();
                Toast.makeText(ReportFormEdit.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ReportFormEdit.this).addToRequestQueue(request);
    }

    private CharSequence timeFormat(String inputTime) throws ParseException {
//        return android.text.format.DateFormat.format("EEE, d MMM yyyy HH:mm:ss", Long.parseLong(inputTime));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = fmt.parse(inputTime);

        SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        return fmtOut.format(date);
    }

    private void selectImage(int tipe) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
        URUTAN_CAPTURE = tipe;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            String path = saveImage(thumbnail);
            if(URUTAN_CAPTURE == 1){
                img_thumb1.setImageBitmap(thumbnail);
                Document_img1 = path;
            }else{
                img_thumb2.setImageBitmap(thumbnail);
                Document_img2 = path;
            }

        }
    }
    private void uploadImageToServer() throws IOException, JSONException {

        if (!AndyUtils.isNetworkAvailable(ReportFormEdit.this)) {
            Toast.makeText(ReportFormEdit.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        String mainServer = ((Controller) this.getApplication()).getMainServer();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", mainServer+"dat_laporan/update_mobile");

        if(Document_img1 != "") {
            map.put("report_first_image", Document_img1);
        }
        if(Document_img2 != "") {
            map.put("report_recent_image", Document_img2);
        }
        map.put("id",R_id);
        map.put("report_seri_no",report_seri_no.getText().toString());
        map.put("report_gardu_code",report_gardu_code.getText().toString());

        map.put("report_file_name",report_file_name.getText().toString());
        map.put("report_location",report_location.getText().toString());
        map.put("report_time",report_time.getText().toString());
        map.put("report_first_condition",report_first_condition.getText().toString());

        map.put("report_recent_condition",report_recent_condition.getText().toString());
        map.put("report_staf_name",report_staf_name.getText().toString());

        new MultiPartRequester(this, map, CAMERA, this);
        AndyUtils.showSimpleProgressDialog(this);
    }

//    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AndyUtils.removeSimpleProgressDialog();
        Log.d("res", response.toString());
        String msg = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            msg = jsonObject.optString("message");
            finish();
        } catch (JSONException e) {
            Log.d("errot issuccess", e.toString());
            e.printStackTrace();
            msg = "Operasi gagal";
        }
        Toast.makeText(ReportFormEdit.this, msg, Toast.LENGTH_LONG).show();
//        finish();
//        startActivities(new Intent[]{new Intent(ReportFormEdit.this, ReportList.class)});
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("Log Dir", wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


}
