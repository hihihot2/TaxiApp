package com.example.kyuhwan.texiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kyuhwan on 2015-10-04.
 */
public class makeRoom extends Activity{
    String title = "";  // 방제목에 대한 변수 생성
    String contents = "";  //방내용에 대한 변수 생성
    String maker_name = "";  //작성자
    String ListName = "";  //선택지
    final int amount = 1;
    private final String SERVER_ADDRESS = "http://192.168.0.6/TaxiApp"; //서버 주소-<현재아이피>/TaxiApp 폴더
    //private final String SERVER_ADDRESS = "http://172.16.28.12/TaxiApp"; //서버 주소-<현재아이피>/TaxiApp 폴더

    String DB_Name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makeroom);

        Intent intent = getIntent();  //intent 사용을 위한 선언
        ListName = intent.getExtras().getString("ListName");  //TaxiList 클래스에서 넘겨진 값을 받는다
        maker_name = intent.getExtras().getString("login_name");  //TaxiList 클래스에서 넘겨진 값을 받는다

        switch (ListName){ //넘겨받은 key 값에 맞춰 불러올 php 파일 변경
            case "0": //홍대
                DB_Name = SERVER_ADDRESS + "/HongIkDB.php?";
                break;
            case "1": //고대 기숙사
                DB_Name = SERVER_ADDRESS + "/KoreaDB.php?";
                break;
            case "2": //조치원 여고
                DB_Name = SERVER_ADDRESS + "/SchoolDB.php?";
                break;
            case "3": //한마음 마트
                DB_Name = SERVER_ADDRESS + "/MartDB.php?";
                break;
            case "4": //조치원 역
                DB_Name = SERVER_ADDRESS + "/StationDB.php?";
                break;
        }

        Button cancel = (Button)findViewById(R.id.roomCancel); //취소 버튼
        final Button makeRoom  = (Button)findViewById(R.id.roomCreate);  //방만들기 버튼

        final EditText E_Title  = (EditText)findViewById(R.id.roomTitle); //방제목
        final EditText E_Contents = (EditText)findViewById(R.id.roomContent);  //내용

        //취소버튼에 대한 리스너 등록///////////////////////////////////////////////////////////////
        cancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                finish(); //이전 화면으로 돌아간다
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        //방만들기 버튼에 대한 리스너 등록//////////////////////////////////////////////////////////
        makeRoom.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                try{
                    title = URLEncoder.encode(E_Title.getText().toString(), "utf-8"); //유니코드 형식으로 encoding
                    contents = URLEncoder.encode(E_Contents.getText().toString(), "utf-8"); ////유니코드 형식으로 encoding
                    String name = URLEncoder.encode(maker_name, "utf-8");

                    //DB 에 현재 시간을 기록하기 위한 파싱 과정/////////////////////////////////////
                    long now  = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
                    String time = sdfNow.format(date);
                    String[] split;
                    split = time.split(":");
                    int fron, back, T_result;
                    fron = Integer.parseInt(split[0]);
                    back = Integer.parseInt(split[1]);
                    T_result = fron * 60 + back;

                    new RssReaderTask().execute(DB_Name+"title="+title+"&contents="+contents +
                            "&name="+name+"&times="+T_result+"&amount="+amount+"&listname="+ListName);
                }catch(UnsupportedEncodingException e1){
                    e1.printStackTrace();
                }
                //방 내부액티비티 or 로그인 액티비티로 전환////////////////////////////////////////////////////////////
                if(maker_name.equals("/0")){
                    Intent intent = new Intent(makeRoom.this, Login.class);
                    intent.putExtra("ListName", ListName);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(makeRoom.this, insideRoom.class);
                    intent.putExtra("maker_name", maker_name);
                    intent.putExtra("ListName", ListName);
                    intent.putExtra("man_num", "1");
                    startActivity(intent);
                }
                ////////////////////////////////////////////////////////////////////////////////////
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
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
}
