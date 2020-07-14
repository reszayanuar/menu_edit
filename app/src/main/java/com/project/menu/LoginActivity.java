package com.project.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button login, register;
    CheckBox loginState;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("users info", Context.MODE_PRIVATE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginState = findViewById(R.id.Checkbox);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivities(new Intent[]{new Intent(LoginActivity.this, RegisterActivity.class)});
                finish();
            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(LoginActivity.this,"All Field Required",Toast.LENGTH_SHORT).show();

                }
                else{
                    login(txtEmail,txtPassword);
                }
            }

        });

        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");

        if (loginStatus.equals("Logged in")){
            startActivities(new Intent[]{new Intent(LoginActivity.this, Menu.class)});
        }
    }

    private void login(final String email, final String password) {
        String mainServer = ((Controller) this.getApplication()).getMainServer();
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Login Account");
        progressDialog.show();
        //String uRl = mainServer+"login/login_mobile";
        String uRl="http://plnulpmalang.com/pln/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Login Success")){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (loginState.isChecked()){
                        editor.putString(getResources().getString(R.string.prefLoginState),"Logged in");
                    }
                    else{
                        editor.putString(getResources().getString(R.string.prefLoginState),"Logged out");
                    }
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this,Menu.class));
                }
                else{
                    Log.d("Error",response.toString());
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("psw", password);
                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);

    }



}

