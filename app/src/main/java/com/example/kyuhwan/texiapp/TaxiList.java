package com.example.kyuhwan.texiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Kyuhwan on 2015-10-03.
 */
public class TaxiList extends Activity{
    private ListView m_ListView;  //item 을 리스트 형식으로 보여주는 컨테이너-뷰
    private CustomAdapter m_Adapter;  //ListView 를 원하는 형식으로 보여지게 하는 Adapter
    Vector<String> title;  //Vector 클래스는 List 인터페이스를 구현한 클래스. http://apphappy.tistory.com/79
    ProgressDialog progressDialog; //진행상황을 알리는 프로그레스Dialog
    ArrayList<String> testS; // http://apphappy.tistory.com/81
    TextView tv;

    String ListName = "";  //어떤 선택지에 대한 리스트인지 판별 변수
    String login_name = "/0"; //방에 들어갈 때 로그인 여부를 물으므로, 초기 값은 0

    //private final String SERVER_ADDRESS = "http://192.168.0.6/TaxiApp"; //서버 주소-<현재아이피>/TaxiApp 폴더
    private final String SERVER_ADDRESS = "http://172.16.19.106/TaxiApp"; //서버 주소-<현재아이피>/TaxiApp 폴더

    String Time_php_early, Time_php, List_xml; //

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxilist);
        Intent intent = getIntent();
        tv = (TextView)findViewById(R.id.testTV);
        ListName = intent.getExtras().getString("key"); // PlaceinMap 에서 intent 로 같이 넘겨진 key 값을 가져온다

        switch (ListName){ //넘겨받은 key 값에 맞춰 불러올 php 파일 변경
            case "0": //홍대
                Time_php_early = SERVER_ADDRESS + "/Hong/Hong_Come_List_early.php?";
                Time_php = SERVER_ADDRESS + "/Hong/Hong_Come_List.php?";
                List_xml = SERVER_ADDRESS + "/Hong/HongIk_List.xml";
                break;
            case "1": //고대 기숙사
                Time_php_early = SERVER_ADDRESS + "/Korea/Kor_Come_List_early.php?";
                Time_php = SERVER_ADDRESS + "/Korea/Kor_Come_List.php?";
                List_xml = SERVER_ADDRESS + "/Korea/Korea_List.xml";
                break;
            case "2": //조치원 여고
                Time_php_early = SERVER_ADDRESS + "/School/School_Come_List_early.php?";
                Time_php = SERVER_ADDRESS + "/School/School_Come_List.php?";
                List_xml = SERVER_ADDRESS + "/School/School_List.xml";
                break;
            case "3": //한마음 마트
                Time_php_early = SERVER_ADDRESS + "/Mart/Mart_Come_List_early.php?";
                Time_php = SERVER_ADDRESS + "/Mart/Mart_Come_List.php?";
                List_xml = SERVER_ADDRESS + "/Mart/Mart_List.xml";
                break;
            case "4": //조치원 역
                Time_php_early = SERVER_ADDRESS + "/Station/Station_Come_List_early.php?";
                Time_php = SERVER_ADDRESS + "/Station/Station_Come_List.php?";
                List_xml = SERVER_ADDRESS + "/Station/Station_List.xml";
                break;
        }

        title = new Vector<String>();  //Vector 클래스 형식으로 객체 생성
        testS = new ArrayList<String>();  //배열리스트 객체 생성
        m_Adapter = new CustomAdapter();  //객체 생성
        m_ListView = (ListView) findViewById(R.id.listView1);
        m_ListView.setAdapter(m_Adapter);  //listView에 커스텀어뎁터 설정

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로딩중입니다...");
        progressDialog.show();

        m_ListView.setSelection(0);
        Button loginpage = (Button) findViewById(R.id.login);
        Button mkroom = (Button)findViewById(R.id.makeroom);
        Button re_btn = (Button)findViewById(R.id.List_reset_btn);

        //<로그인> 버튼에 대한 리스너 등록//////////////////////////////////////////////////////////
        loginpage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TaxiList.this, Login.class);
                it.putExtra("ListName", ListName);  //선택지 번호를 넘긴다
                it.putExtra("SERVER_ADDRESS", SERVER_ADDRESS);
                startActivity(it);
            }
        });

        //<방만들기> 버튼에 대한 리스너 등록////////////////////////////////////////////////////////
        mkroom.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(TaxiList.this, makeRoom.class); //Activity 전환
                //it.addFlags(it.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("ListName", ListName);  //선택지 번호를 넘긴다
                it.putExtra("login_name", login_name); //로그인된 아이디 값을 넘긴다
                startActivity(it);

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        //DB에 현재 시간을 기록하기 위한 Parsing 과정///////////////////////////////////////////////
        long now_M  = System.currentTimeMillis(); // 1/1000초를 long 값으로 리턴해주는 함수
        Date date_M = new Date(now_M); //Date() - 현재 날짜정보를 저장
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
        String time_M = sdfNow.format(date_M); // "HH:mm"형식으로 현재 시간을 문자열에 저장
        String[] split_M;
        split_M = time_M.split(":"); //split() - 구분자를 경계로 자르는 함수 split_m[0], split_m[1]
        int fron_M, back_M, cur_T_M, limit_T_M;
        fron_M = Integer.parseInt(split_M[0]); //parseInt() = char - > int
        back_M = Integer.parseInt(split_M[1]); //문자열을 정수형태로 바꿔준다
        cur_T_M = fron_M * 60 + back_M; //시간을 분 형태로 나타내어 저장


        if(cur_T_M >=0 && cur_T_M < 60)
        { //한시간 이내에 생성된 방목록
            limit_T_M = cur_T_M + 1380;
            new RssReaderTask().execute(Time_php_early+"limit_T=" +limit_T_M+ "&cur_T=" +cur_T_M);
        }
        else
        { //Exception = 12시(0시)가 지났을 시 예외처리
            limit_T_M = cur_T_M - 60;
            cur_T_M = 0;
            new RssReaderTask().execute(Time_php+"limit_T=" +limit_T_M+ "&cur_T=" +cur_T_M);
        }

        m_Adapter.remove_all(); //기존 list 아이템들을 지운다
        progressDialog.show();
        new TitleTask().execute(List_xml); //현재 서버에 생성되어있는 방목록을 가져온다

        //reset버튼에 대한 리스너 등록//////////////////////////////////////////////////////////////
        re_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                progressDialog = new ProgressDialog(TaxiList.this);
                progressDialog.setMessage("리셋중입니다...");
                progressDialog.show();
                //리스트를 초기화하므로 현재 시간을 다시 DB에 기록한다//////////////////////////////
                long now  = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                String time = sdfNow.format(date);
                String[] split;
                split = time.split(":");
                int fron, back, cur_T, limit_T;
                fron = Integer.parseInt(split[0]);
                back = Integer.parseInt(split[1]);
                cur_T = fron * 60 + back;
                if(cur_T >=0 && cur_T < 60)
                {
                    limit_T = cur_T + 1380;
                    new RssReaderTask().execute(Time_php_early+"limit_T=" +limit_T+ "&cur_T=" +cur_T);
                }
                else
                {
                    limit_T = cur_T - 60;
                    cur_T = 0;
                    new RssReaderTask().execute(Time_php+"limit_T=" +limit_T+ "&cur_T=" +cur_T);
                }
                m_Adapter.remove_all();
                Toast.makeText(TaxiList.this, "Reset Complete", Toast.LENGTH_SHORT).show();
                new TitleTask().execute(List_xml);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //TextView tv = (TextView)findViewById(R.id.Text1);
        //tv.setText(ListName);
    }


    public ArrayList<String> getXmlData(String rss) {
        ArrayList<String> titleList = new ArrayList<String>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            URL url = new URL(rss);

            InputStream is = url.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("exist")) {
                        String title = xpp.nextText();
                        titleList.add(title);
                    }
                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return titleList;
    }

    //네트워크 사용을 위한 Background 쓰레드 함수///////////////////////////////////////////////////
    private class RssReaderTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            ArrayList<String> titleList = getXmlData(urls[0]);

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < titleList.size(); i++) {
                sb.append(titleList.get(i)).append("\n\n");

            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            //Toast.makeText(makeRoom.this, result, Toast.LENGTH_LONG).show();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Title 에 대한 정보를 가져오기 위한 xml 파싱 함수//////////////////////////////////////////////
    public ArrayList<String> getTitleData(String rss) {
        ArrayList<String> titleList = new ArrayList<String>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            URL url = new URL(rss);

            InputStream is = url.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("title")) {
                        String titles = xpp.nextText();
                        titleList.add(titles);
                    }
                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return titleList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Name 에 대한 정보를 가져오기 위한 xml 파싱 함수///////////////////////////////////////////////
    public ArrayList<String> getNameData(String rss) {
        ArrayList<String> nameList = new ArrayList<String>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            URL url = new URL(rss);

            InputStream is = url.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("name")) {
                        String title = xpp.nextText();
                        nameList.add(title);
                    }
                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nameList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Time 에 대한 정보를 가져오기 위한 xml 파싱 함수///////////////////////////////////////////////
    public ArrayList<String> getTimesData(String rss) {
        ArrayList<String> timeList = new ArrayList<String>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            URL url = new URL(rss);

            InputStream is = url.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("times")) {
                        String title = xpp.nextText();
                        timeList.add(title);
                    }
                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Amount 에 대한 정보를 가져오기 위한 xml 파싱 함수/////////////////////////////////////////////
    public ArrayList<String> getAmountData(String rss) {
        ArrayList<String> amountList = new ArrayList<String>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            URL url = new URL(rss);

            InputStream is = url.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("amount")) {
                        String title = xpp.nextText();
                        amountList.add(title);
                    }
                }
                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amountList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //네트워크 사용을 위한 Background 쓰레드 함수///////////////////////////////////////////////////
    public class TitleTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            ArrayList<String> titleList = getTitleData(urls[0]);
            ArrayList<String> nameList = getNameData(urls[0]);
            ArrayList<String> timeList = getTimesData(urls[0]);
            ArrayList<String> amountList = getAmountData(urls[0]);
            StringBuffer sb = new StringBuffer();
            //publishProgress("end");
            //progressDialog = ProgressDialog.show(TaxiList.this, "ProgressDialog", "Wait!");

            for (int i = 0; i < titleList.size(); i++) {
                sb.append(titleList.get(i)).append("\n\n");
                testS.add(titleList.get(i));
                int temp_time = Integer.parseInt(timeList.get(i)), hour, minute;
                hour = temp_time / 60;
                minute = temp_time % 60;
                String cur_hour = Integer.toString(hour);
                String cur_minute = Integer.toString(minute);
                if(minute <10)
                    m_Adapter.TitleAdd(titleList.get(i), nameList.get(i), cur_hour+":0"+cur_minute, amountList.get(i));
                else;
                m_Adapter.TitleAdd(titleList.get(i), nameList.get(i), cur_hour+":"+cur_minute, amountList.get(i));

                //publishProgress("ing");
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //Toast.makeText(TaxiList.this, testS.get(1), Toast.LENGTH_LONG).show();
            m_Adapter.notifyDataSetChanged();
            //tv.setText(result);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
