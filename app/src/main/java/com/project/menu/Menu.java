package com.project.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    private CardView datatrafo,locationtrafo,fixtrafo,logouttrafo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //defining card
        datatrafo = (CardView) findViewById(R.id.data_trafo);
        locationtrafo = (CardView) findViewById(R.id.location_trafo);
        fixtrafo = (CardView) findViewById(R.id.fix_trafo);
        logouttrafo = (CardView) findViewById(R.id.logout_trafo);
        //add click Listener to the card
        datatrafo.setOnClickListener(this);
        locationtrafo.setOnClickListener(this);
        fixtrafo.setOnClickListener(this);
        logouttrafo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.data_trafo : i = new Intent(this,Data.class);startActivity(i); break;
            case R.id.location_trafo : i = new Intent(this,MapsActivity.class);startActivity(i); break;
            case R.id.fix_trafo : i = new Intent(this,ReportList.class);startActivity(i); break;
            case R.id.logout_trafo : i = new Intent(this,LoginActivity.class);startActivity(i); break;
            default:break;
        }

    }
}
