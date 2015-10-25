package com.example.kyuhwan.texiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kyuhwan on 2015-10-04.
 */
public class Login extends Activity{
    String logInID = null; //아이디
    String logInPW = null; //비밀번호
    EditText ID;
    EditText PW;

    String SERVER_ADDRESS;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = getIntent();
        SERVER_ADDRESS = intent.getExtras().getString("SERVER_ADDRESS");

        Button enter = (Button)findViewById(R.id.logInCon); //로그인 버튼
        Button cancel = (Button)findViewById(R.id.logInCan); //취소 버튼
        Button membership = (Button)findViewById(R.id.membership); //회원가입 버튼

        ID = (EditText)findViewById(R.id.logInId);
        PW = (EditText)findViewById(R.id.logInPw);

        Toast.makeText(Login.this, SERVER_ADDRESS, Toast.LENGTH_SHORT).show();

        //회원가입 버튼에 대한 리스너 등록//////////////////////////////////////////////////////////
        membership.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Login.this, Membership.class);
                it.putExtra("SERVER_ADDRESS", SERVER_ADDRESS);
                startActivity(it);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        //취소 버튼에 대한 리스너 등록//////////////////////////////////////////////////////////////
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ///////////////////////////////////////////z/////////////////////////////////////////////////

        //로그인 버튼에 대한 리스너 등록////////////////////////////////////////////////////////////
        enter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInID = ID.getText().toString();
                logInPW = PW.getText().toString();

                //id, 비번을 DB로 넘겨주고 DB상에서 매칭 검색.
                new ExistTask().execute(SERVER_ADDRESS);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    public class ExistTask extends AsyncTask<String, Void, String > {

        @Override
        protected String doInBackground(String... urls) {
            //String ExistId = ExistIdData(urls[0]);
            //String ExistPw = ExistPwData(urls[0]);
            return null;
        }
    }
}
