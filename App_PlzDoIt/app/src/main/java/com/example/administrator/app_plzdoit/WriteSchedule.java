package com.example.administrator.app_plzdoit;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WriteSchedule extends AppCompatActivity implements View.OnClickListener {

    EditText year;
    EditText month;
    EditText date;
    EditText hour;
    EditText minute;
    EditText title;
    EditText charge;
    EditText desc;
    EditText secret;

    TextView WriterView;
    TextView TroopView;
    TextView SectionView;

    String writer;
    String troop;
    String section;

    Button submit;


    private static void sendTosat(Context context, String Tmsg)
    {
        Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_SHORT);
        t.show();
    }
    private void WriteSuccess(String res)
    {
        String showYear="";
        String showWeek="";
        String showDay="";
        try {
            JSONParser parser = new JSONParser();
            JSONObject JsonObj =  (JSONObject) parser.parse(res);
            showYear = (String)JsonObj.get("year");
            showWeek = (String)JsonObj.get("week");
            showDay = (String)JsonObj.get("day");



        }catch (ParseException e){e.printStackTrace();}

        sendTosat(getApplicationContext(),"Writed in "+showYear+"-"+showWeek);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_schedule);

        Intent intent = getIntent();
        writer = intent.getStringExtra("name");
        troop =  intent.getStringExtra("troop");
        section = intent.getStringExtra("section");

        WriterView =(TextView)findViewById(R.id.WriterName);
        TroopView = (TextView)findViewById(R.id.WriterTroop);
        SectionView = (TextView)findViewById(R.id.WriterSection);

        WriterView.setText(writer);
        TroopView.setText(troop);
        SectionView.setText(section);


        year = (EditText)findViewById(R.id.yearWrite);
        month = (EditText)findViewById(R.id.monthWrite);
        date = (EditText)findViewById(R.id.dateWrite);
        hour = (EditText)findViewById(R.id.hourWrite);
        minute = (EditText)findViewById(R.id.minuteWrite);
        secret = (EditText)findViewById(R.id.secretWrite);
        title = (EditText)findViewById(R.id.writeTitle);
        charge = (EditText)findViewById(R.id.writeCharge);
        desc = (EditText)findViewById(R.id.writeDescription);



        submit = (Button)findViewById(R.id.WriterSubmit);

        submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.WriterSubmit:
            {
                String sendYear = year.getText().toString();
                String sendMonth = month.getText().toString();
                String sendDate = date.getText().toString();
                String sendTime = hour.getText().toString()+":"+minute.getText().toString();
                String sendSecret = secret.getText().toString();
                String sendTitle = title.getText().toString();
                String sendcharge = charge.getText().toString();
                String sendDesc = desc.getText().toString();




                if (sendYear.isEmpty()||sendMonth.isEmpty()||sendDate.isEmpty()||sendTime.isEmpty()||sendTitle.isEmpty()||sendcharge.isEmpty()||sendDesc.isEmpty()||writer.isEmpty()||troop.isEmpty()||section.isEmpty()||sendSecret.isEmpty()) {
                    sendTosat(getApplicationContext(),"Fill All Blanks");
                } else {

                    GetConnManaFinal conn = new GetConnManaFinal();
                    final String url = getString(R.string.serverhost) + "WS";
                    final String params = "year=" + sendYear + "&month=" + sendMonth + "&date=" + sendDate + "&time=" + sendTime+ "&title=" + sendTitle+ "&desc=" + sendDesc+ "&section=" + section+ "&writer=" + writer+ "&secret=" + sendSecret + "&troop=" + troop+"&charge="+sendcharge;


                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0) {   // Message id 가 0 이면

                                Bundle bundle = msg.getData();

                                String totoast = bundle.getString("valid"); // 메인스레드의 UI 내용 변경
                                String res = bundle.getString("weekDay");
                                sendTosat(getApplicationContext(), res);
                                WriteSuccess(res);
                            } else if (msg.what == 1) {
                                Bundle bundle = msg.getData();

                                String res = bundle.getString("invalid"); // 메인스레드의 UI 내용 변경

                                sendTosat(getApplicationContext(), res);
                            }

                        }
                    };


                    try {
                        Log.i("dbg", "we just try");
                        Thread tmp = new Thread() {
                            public void run() {
                                Log.i("dbg", "we just thread");
                                try {

                                    GetConnManaFinal conn = new GetConnManaFinal();

                                    String toReturn =
                                            conn.sendPost(url, params);
                                    Log.i("dbg", toReturn);
                                    if (toReturn.matches("false")) {
                                        Log.i("dbg", toReturn);

                                        Bundle bundle = new Bundle();
                                        bundle.putString("invalid", "Something Bad Happend");

                                        Message msg = handler.obtainMessage();

                                        msg.setData(bundle);
                                        msg.what = 1;

                                        handler.sendMessage(msg);
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("valid", "Successfully Write");
                                        bundle.putString("weekDay",toReturn);

                                        Message msg = handler.obtainMessage();

                                        msg.setData(bundle);
                                        msg.what = 0;

                                        handler.sendMessage(msg);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        };
                        tmp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }



        }

    }
}
