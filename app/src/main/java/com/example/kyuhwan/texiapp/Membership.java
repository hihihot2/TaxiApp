package com.example.kyuhwan.texiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kyuhwan on 2015-10-07.
 */
public class Membership extends Activity{
    TextView search_id;
    EditText getId, getPw, getPw2, getName, getGrade, getPhone;
    Button Join, Join_cancel;

    Spinner collegeList, MajorList;
    ArrayAdapter coll_Adapter, major_Adapter;
    String col = "";
    String maj = "";
    String result = "";
    String SERVER_ADDRESS;

    boolean idcheck = false, stu_numcheck = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership);
        Intent intent = getIntent();
        SERVER_ADDRESS = intent.getExtras().getString("SERVER_ADDRESS");

        //소속대학-과 스피너등록////////////////////////////////////////////////////////////////////
        collegeList = (Spinner) findViewById(R.id.getCollege);
        MajorList = (Spinner) findViewById(R.id.getMajor);

        coll_Adapter = ArrayAdapter.createFromResource(this,
                R.array.college, android.R.layout.simple_spinner_dropdown_item);
        collegeList.setAdapter(coll_Adapter);


        collegeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "인문대학":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.liberal, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    case "과학기술대학":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.science, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        maj = MajorList.getSelectedItem().toString();
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    case "경상대학":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.economy, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        maj = MajorList.getSelectedItem().toString();
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    case "공공행정학부":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.admin, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        maj = MajorList.getSelectedItem().toString();
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    case "국제스포츠학부":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.sports, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        maj = MajorList.getSelectedItem().toString();
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    case "약학대학":
                        col = parent.getItemAtPosition(position).toString();
                        major_Adapter = ArrayAdapter.createFromResource(Membership.this,
                                R.array.pharmacy, android.R.layout.simple_spinner_dropdown_item);
                        MajorList.setAdapter(major_Adapter);
                        maj = MajorList.getSelectedItem().toString();
                        MajorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                maj = parent.getItemAtPosition(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}});
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        getId = (EditText) findViewById(R.id.getId);
        getPw = (EditText) findViewById(R.id.getPw);
        getPw2 = (EditText) findViewById(R.id.getPw2);
        getName = (EditText) findViewById(R.id.getName);
        getGrade = (EditText) findViewById(R.id.getGrade);
        getPhone = (EditText) findViewById(R.id.getPhone);

        Join = (Button) findViewById(R.id.Join); //가입버튼
        Join_cancel = (Button) findViewById(R.id.Join_cancel); //취소버튼
        search_id = (TextView) findViewById(R.id.search1);

        //id중복검사////////////////////////////////////////////////////////////////////////////////
        search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = getId.getText().toString();

                try {
                    String id_xml = SERVER_ADDRESS + "/id_search.php?" + "ID=" + URLEncoder.encode(ID, "UTF-8");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Join_cancel버튼에 대한 리스너 등록////////////////////////////////////////////////////////
        Join_cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Join버튼에 대한 리스너 등록///////////////////////////////////////////////////////////////
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!idcheck) {
                    Toast.makeText(Membership.this, "ID중복체크 필수", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!stu_numcheck) {
                    Toast.makeText(Membership.this, "학번중복체크 필수", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ID = getId.getText().toString();
                String PW = getPw.getText().toString();
                String PW2 = getPw2.getText().toString();
                String Name = getName.getText().toString();
                String Grade = getGrade.getText().toString();
                String Phone = getPhone.getText().toString();
                String Major = col+" "+maj;

                if(PW.equals(PW2)) {
                    try {
                        String Join_xml = SERVER_ADDRESS + "/JoinMembership.php?" + "ID=" + URLEncoder.encode(ID, "UTF-8")
                                + "&PW=" + URLEncoder.encode(PW, "UTF-8") + "&Name=" + URLEncoder.encode(Name, "UTF-8")
                                + "&Grade=" + URLEncoder.encode(Grade, "UTF-8") + "&Phone=" + URLEncoder.encode(Phone, "UTF-8")
                                + "&Major=" + URLEncoder.encode(Major, "UTF-8");

                        new JoinTask().execute(Join_xml);
                        Log.v("tag", Join_xml);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(Membership.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getXmlData(String urls){
        String rss = SERVER_ADDRESS+"/insertresult.xml";
        String ret = "";

        BufferedReader br = null;

        try{
            final URL url = new URL(urls);

            br = new BufferedReader(new InputStreamReader(url.openStream()));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            URL server = new URL(rss);
            InputStream is = server.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    if(xpp.getName().equals("result")){
                        ret = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  ret;
    }

    private class JoinTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try{
                result = getXmlData(urls[0]);
            }catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            if(result.equals("1")){
                Toast.makeText(Membership.this, "회원가입이 되었습니다", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(Membership.this, "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show();
        }

    }
}
