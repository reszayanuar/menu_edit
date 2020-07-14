package com.project.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.menu.adapter.MySingleton;
import com.project.menu.app.Controller;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    MaterialEditText username,email,password,alamat,ttl;
    Button register,login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        alamat = findViewById(R.id.alamat);
        ttl = findViewById(R.id.ttl);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(RegisterActivity.this, LoginActivity.class)});
                finish();
            }


        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  txtUserName = username.getText().toString();
                String  txtEmail = email.getText().toString();
                String  txtPassword = password.getText().toString();
                String  txtAlamat = alamat.getText().toString();
                String  txtTtl = ttl.getText().toString();
                if (TextUtils.isEmpty(txtUserName)|| TextUtils.isEmpty(txtEmail)
                        || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtAlamat) || TextUtils.isEmpty(txtTtl)) {
                    Toast.makeText(RegisterActivity.this, "All Field Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerNewAccounts(txtUserName,txtEmail,txtPassword,txtTtl,txtAlamat);
                }
                //else {
                //  String selectGender = selected_Gender.getText().toString();
                //registerNewAccounts(txtUserName,txtEmail,txtPassword,txtMobile,selectGender);
                //}
            }

        });




    }



    private void registerNewAccounts(final String username, final String email, final String password, final String alamat, final String ttl){
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        String mainServer = ((Controller) this.getApplication()).getMainServer();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering New Account");
        progressDialog.show();
        //String uRl = mainServer+"register.php";
        String uRl = "http://plnulpmalang.com/pln/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("successfully Registered")){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> param = new HashMap<>();
                param.put("username",username);
                param.put("email",email);
                param.put("psw",password);
                param.put("alamat",alamat);
                param.put("ttl",ttl);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
    }
}

