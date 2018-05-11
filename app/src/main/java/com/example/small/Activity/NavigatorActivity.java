package com.example.small.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.small.R;

public class NavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        //매장선택후 인텐트로 선택한 매장번호 넘기기
        //프린트 -> 7번
        //info -> 5번
        //그라지에 -> 9번
        //ATM -> 1번

        final Intent intent = new Intent(this,MyLocationActivity.class);


        Button grazzie = (Button)findViewById(R.id.grazzie);
        grazzie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("nodeNum",9);
                startActivity(intent);
                finish();
            }
        });
        Button atm = (Button)findViewById(R.id.atm);
        atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("nodeNum",1);
                startActivity(intent);
                finish();
            }
        });
        Button info = (Button)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("nodeNum",5);
                startActivity(intent);
                finish();
            }
        });
        Button printerCenter = (Button)findViewById(R.id.printerCenter);
        printerCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("nodeNum",7);
                startActivity(intent);
                finish();
            }
        });



    }


}
