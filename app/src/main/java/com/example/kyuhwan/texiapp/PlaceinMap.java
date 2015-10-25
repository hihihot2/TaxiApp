package com.example.kyuhwan.texiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PlaceinMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProgressDialog progressDialog;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placein_map);

        Button[] btn = new Button[10];
        btn[0] = (Button)findViewById(R.id.HongIk); //홍대
        btn[1] = (Button)findViewById(R.id.Korea_Dom); //호연학사
        btn[2] = (Button)findViewById(R.id.GirlSchool); //조여고
        btn[3] = (Button)findViewById(R.id.Mart);  //한마음
        btn[4] = (Button)findViewById(R.id.Station); //조치원역


        btn[5] = (Button)findViewById(R.id.HongIkPin); //
        btn[6] = (Button)findViewById(R.id.Korea_DomPin); //
        btn[7] = (Button)findViewById(R.id.GirlSchoolPin); //
        btn[8] = (Button)findViewById(R.id.MartPin);  //
        btn[9] = (Button)findViewById(R.id.StationPin); //

        btn[0].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "0");
                //it.addFlags(it.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);

            }
        });

        btn[5].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "0");
                //it.addFlags(it.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);

            }
        });

        btn[1].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "1");
                startActivity(it);
            }
        });

        btn[6].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "1");
                startActivity(it);
            }
        });

        btn[2].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "2");
                startActivity(it);
            }
        });

        btn[7].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "2");
                startActivity(it);
            }
        });

        btn[3].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "3");
                startActivity(it);
            }
        });

        btn[8].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "3");
                startActivity(it);
            }
        });

        btn[4].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "4");
                startActivity(it);
            }
        });

        btn[9].setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(PlaceinMap.this, TaxiList.class);
                it.putExtra("key", "4");
                startActivity(it);
            }
        });
    }
}
