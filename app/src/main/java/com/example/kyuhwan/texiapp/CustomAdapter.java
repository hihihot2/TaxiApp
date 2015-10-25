package com.example.kyuhwan.texiapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class CustomAdapter extends BaseAdapter{
    private ArrayList<String> title_List;
    private ArrayList<String> time_List;
    private ArrayList<String> amount_List;
    private ArrayList<String> owner_List;
    private int counter= 0; // �� ��� ���� �ľ�

    String login_name = "null";
    private final String SERVER_ADDRESS = "http://192.168.0.6/TaxiApp"; //서버 주소-<현재아이피>/TaxiApp 폴더
    //private final String SERVER_ADDRESS = "http://172.16.28.16/TaxiApp";
    String ListName = "";

    public CustomAdapter() { //생성자
        title_List = new ArrayList<String>();
        time_List = new ArrayList<String>();
        amount_List = new ArrayList<String>();
        owner_List = new ArrayList<String>();
    }

    @Override
    public int getCount() { //extends 된 BaseAdapter에서 오버로드된 함수
        // TODO Auto-generated method stub
        return title_List.size();
    }

    @Override
    public Object getItem(int position) { //extends 된 BaseAdapter에서 오버로드된 함수
        // TODO Auto-generated method stub
        return title_List.get(position);
    }

    @Override
    public long getItemId(int position) { //extends 된 BaseAdapter에서 오버로드된 함수
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //extends 된 BaseAdapter에서 오버로드된 함수
        final int pos = position;
        final Context context = parent.getContext();

        TextView title = null;
        TextView time =  null;
        TextView amount = null;
        TextView owner = null;
        CustomHolder  holder = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            title = (TextView) convertView.findViewById(R.id.title); //제목
            time = (TextView) convertView.findViewById(R.id.time); //만들어진 시간
            amount = (TextView) convertView.findViewById(R.id.amount); //인원 수
            owner  = (TextView)convertView.findViewById(R.id.owner); //작성자
            //btn = (Button) convertView.findViewById(R.id.btnTest);

            holder = new CustomHolder();
            holder.title_TextView = title;
            holder.owner_TextView = owner;
            holder.time_TextView = time;
            holder.amount_TextView = amount;

            //holder.m_Btn = btn;
            convertView.setTag(holder);
        }
        else {
            holder = (CustomHolder)convertView.getTag();
            title = holder.title_TextView;
            time = holder.time_TextView;
            amount = holder.amount_TextView;
            owner = holder.owner_TextView;
            //btn = holder.m_Btn;
        }
        title.setText(title_List.get(position));
        time.setText(time_List.get(position));
        amount.setText(amount_List.get(position));
        owner.setText(owner_List.get(position));
		/*btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(context, m_List.get(pos), Toast.LENGTH_SHORT).show();
			}
		});*/
        // TODO Auto-generated method stub

        convertView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context, "����Ʈ Ŭ�� : " + owner_List.get(pos), Toast.LENGTH_SHORT).show();
                int man_num;
                String filter = amount_List.get(pos);
                String split[];
                split = filter.split("/");
                man_num = Integer.parseInt(split[0]);
                man_num++;
                String te = Integer.toString(man_num);
                Toast.makeText(context, te , Toast.LENGTH_SHORT).show();
                if(man_num == 5)
                    Toast.makeText(context, "���� �� á���ϴ�", Toast.LENGTH_SHORT).show();
                else if(owner_List.get(pos).equals(login_name))
                    Toast.makeText(context, "�ڽ��� �濡�� �� �� �����ϴ�", Toast.LENGTH_SHORT).show();
                else{


                    try{
                        String temp_owner = URLEncoder.encode(owner_List.get(pos), "utf-8");

                        new AmountTask().execute(SERVER_ADDRESS + "/Hong_Amount.php?owner=" + temp_owner + "&amount=" + man_num);

                    }catch(UnsupportedEncodingException e1){
                        e1.printStackTrace();
                    }
                    Intent intent = new Intent(context, insideRoom.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("maker_name", owner_List.get(pos));
                    intent.putExtra("ListName", ListName);
                    intent.putExtra("login_name", login_name);
                    intent.putExtra("man_num", Integer.toString(man_num));
                    context.startActivity(intent);
                }

            }
        });

		/*convertView.setOnLongClickListener(new OnLongClickListener(){         //��Ŭ�� �̺�Ʈ ó�� �κ� ���ٰ� ����
			public boolean onLongClick(View v){
				Toast.makeText(context, "����Ʈ �� Ŭ�� : "+ title_List.get(pos), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, insideRoom.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("maker_name", owner_List.get(pos));
				intent.putExtra("ListName", ListName);

				intent.putExtra("man_num", Integer.toString(man_num));
				context.startActivity(intent);
				return true;
			}
		});*/

        return convertView;
    }

    private class CustomHolder {
        TextView title_TextView;
        TextView time_TextView;
        TextView amount_TextView;
        TextView owner_TextView;

        //Button m_Btn;
    }

    public void TitleAdd(String _title, String _name, String _time, String _amount){
        title_List.add(0, _title);
        time_List.add(0, _time);
        amount_List.add(0, _amount+"/4");
        owner_List.add(0, _name);
        counter++;
    }
    public void OwnerAdd(String _owner){
        owner_List.add(0, _owner);
    }
    public void TimeAdd(String _time){
        time_List.add(0, _time);
    }
    public void add(String _amount){
        amount_List.add(0, _amount);
    }
    public void add(String _title, String _owner, String _time, String _amount){
        title_List.add(0, _title);
        time_List.add(0, _time);
        amount_List.add(0, _amount);
        owner_List.add(0, _owner);
    }

    public void remove(int _position){

        title_List.remove(_position);
    }
    public void remove_all(){
        for(int i = 0; i < counter; i++)
            title_List.remove(0);
        counter = 0;
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
}
