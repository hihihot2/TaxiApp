package com.example.kyuhwan.texiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Kyuhwan on 2015-10-04.
 */
public class insideRoom extends Activity{
    String maker_name = "";
    String ListName = "";
    String temp_maker = "";
    String main_title = "";
    String before_num = "";
    TextView IT;
    TextView Contents;
    TextView[] guest = new TextView[4];

    //TextView IT = (TextView)findViewById(R.id.Insidetitle);

    protected void onCreate(Bundle savedInstanceState) {
        //////////////////Before Activity/////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insideroom);
        Intent intent = getIntent();
        IT = (TextView)findViewById(R.id.Insidetitle);
        Contents = (TextView)findViewById(R.id.InsideContents);
        guest[0] = (TextView)findViewById(R.id.maker);
        guest[1] = (TextView)findViewById(R.id.login1);
        guest[2] = (TextView)findViewById(R.id.login2);
        guest[3] = (TextView)findViewById(R.id.login3);
        Button cancle_btn = (Button)findViewById(R.id.insideRoom_cancle);

        ////////////////////////////////////////////////////////////////
        //Server Connect!//////
        final String SERVER_ADDR = "http://timetable1st.zz.mu";
        //new AmountTask().execute(SERVER_ADDR + "/Hong_Amount.php?owner=" + owner_List.get(pos) + "&amount=" + man_num);
        try{
            maker_name = intent.getExtras().getString("maker_name");
            before_num = intent.getExtras().getString("man_num");
            temp_maker = maker_name;
            maker_name = URLEncoder.encode(maker_name, "utf-8");
            ListName = URLEncoder.encode(intent.getExtras().getString("ListName"), "utf-8");
            new RssReaderTask().execute(SERVER_ADDR + "/HongIkInside.php?name="+maker_name);
            //main_title = getXmlDatas(SERVER_ADDR + "/HongIk"+maker_name+".xml", "title");
        }catch(UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
        //////////////////////////////////////////////////////////

        new AT_Title().execute(SERVER_ADDR + "/HongIk"+maker_name+".xml");      //Ÿ��Ʋ ��������
        new AT_Contents().execute(SERVER_ADDR + "/HongIk"+maker_name+".xml");	//���� ��������
        //IT.setText(main_title);
        int in_man = Integer.parseInt(before_num);
        String login_name = intent.getExtras().getString("login_name");
        if(in_man == 1)															//�����ο� ��� ó���κ�
            guest[0].setText(temp_maker);
        else if(in_man == 2)
        {
            guest[0].setText(temp_maker);
            guest[1].setText(login_name);
            try{
                login_name = URLEncoder.encode(login_name, "utf-8");
            }catch(UnsupportedEncodingException e1){
                e1.printStackTrace();
            }
            new AmountTask().execute(SERVER_ADDR + "/Hong_Member_Make.php?owner=" + maker_name + "&guest1=" + login_name + "&guest2=guest2&guest3=guest3");

        }
        else if(in_man == 3)
        {
            try{
                login_name = URLEncoder.encode(login_name, "utf-8");
            }catch(UnsupportedEncodingException e1){
                e1.printStackTrace();
            }
            new AmountTask().execute(SERVER_ADDR + "/Hong_Member_Update3.php?owner=" + maker_name + "&guest=" + login_name);

            //new guestList().execute(SERVER_ADDR + "/Hong_Member_Make.php?owner=" + maker_name + "&guest1=" + login_name + "&guest2=guest2&guest3=guest3");
            guest[0].setText(temp_maker);
            new guestList().execute(SERVER_ADDR + "/HongIkMember"+maker_name+".xml");
        }
        else
        {


            try{
                login_name = URLEncoder.encode(login_name, "utf-8");
            }catch(UnsupportedEncodingException e1){
                e1.printStackTrace();
            }
            new AmountTask().execute(SERVER_ADDR + "/Hong_Member_Update4.php?owner=" + maker_name + "&guest=" + login_name);
            guest[0].setText(temp_maker);
            new guestList().execute(SERVER_ADDR + "/HongIkMember"+maker_name+".xml");
        }
        Toast.makeText(insideRoom.this, before_num, Toast.LENGTH_SHORT).show();

        cancle_btn.setOnClickListener(new Button.OnClickListener(){             //�� ���ĵɶ� ȣ���Լ�
            public void onClick(View v){


                int man_num = Integer.parseInt(before_num);		// �ο��� �޾ƿ��� �Լ�;
                man_num--;
                new AmountTask().execute(SERVER_ADDR + "/Hong_Amount.php?owner=" + maker_name + "&amount=" + man_num);
                if(man_num == 0)
                    new AmountTask().execute(SERVER_ADDR + "/Hong_Delete_Inroom.php?owner=" + maker_name);
                Toast.makeText(insideRoom.this, before_num, Toast.LENGTH_SHORT).show();
                Intent it_cancle = new Intent(insideRoom.this, TaxiList.class);
                it_cancle.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(it_cancle);


            }

        });

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
                    if (xpp.getName().equals("title")) {
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


    public ArrayList<String> getXmlDatas(String rss, String target) {
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
                    if (xpp.getName().equals(target)) {
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
            //Toast.makeText(insideRoom.this, result, Toast.LENGTH_LONG).show();

        }
    }

    private class AT_Title extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            ArrayList<String> titleList = getXmlDatas(urls[0], "title");

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < titleList.size(); i++) {
                sb.append(titleList.get(i));
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            main_title = result;
            IT.setText("    " +main_title);
            Toast.makeText(insideRoom.this, main_title, Toast.LENGTH_SHORT).show();
        }
    }
    private class AT_Contents extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            ArrayList<String> titleList = getXmlDatas(urls[0], "contents");

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < titleList.size(); i++) {
                sb.append(titleList.get(i));
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            //main_title = result;
            Contents.setText("    " +result);
            Toast.makeText(insideRoom.this, main_title, Toast.LENGTH_LONG).show();
        }
    }
    public ArrayList<String> getAmountData(String rss) {
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
                    if (xpp.getName().equals("amount")) {
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

    private class AmountTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            ArrayList<String> amountList = getAmountData(urls[0]);
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < amountList.size(); i++) {
                sb.append(amountList.get(i)).append("\n\n");


            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            //Toast.makeText(CustomAdapter.this, "���� �� á���ϴ�", Toast.LENGTH_SHORT).show();

            //tv.setText(result);
        }
    }

    public ArrayList<String> getGuestList(String rss) {
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
                    if (xpp.getName().equals("guest")) {
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

    private class guestList extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            ArrayList<String> guestList = getGuestList(urls[0]);

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < guestList.size(); i++) {
                sb.append(guestList.get(i)).append(":");

            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            String guestName[];
            guestName = result.split(":");
            guest[1].setText(guestName[0]);
            guest[2].setText(guestName[1]);
            guest[3].setText(guestName[2]);
            //Toast.makeText(insideRoom.this, result, Toast.LENGTH_LONG).show();

        }
    }
}
