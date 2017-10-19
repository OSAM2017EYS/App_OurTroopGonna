package com.example.administrator.ourtroopgonna;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String user;
    String pw;
    String troop;
    String name;
    String section;
    String secret;
    String admin;


    private void LoadSuccess(String jsonString)
    {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(jsonString);

            String psdSection = (String)jsonObj.get("section");
            String psdAdmin = (String)jsonObj.get("admin");
            String psdSecret = (String)jsonObj.get("secretLevel");

            section=psdSection;
            admin = psdAdmin;
            secret= psdSecret;


            // Log.i("dbg",troop);
        }catch (ParseException e){e.printStackTrace();}


    }

    private static void sendTosat(Context context, String Tmsg)
    {
        Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        pw = intent.getStringExtra("pw");
        troop = intent.getStringExtra("troop");
        name = intent.getStringExtra("name");

        sendTosat(getApplicationContext(),"Welcome "+name+"!");


        Log.i("dbg","app start");

        Button show = (Button)findViewById(R.id.show);
        Button write = (Button)findViewById(R.id.write);
        Button info = (Button)findViewById(R.id.info);
        Button out = (Button)findViewById(R.id.out);

        show.setOnClickListener(this);
        write.setOnClickListener(this);
        info.setOnClickListener(this);
        out.setOnClickListener(this);

        final String url = getString(R.string.serverhost)+"GMI";
        final String query = "?user=" + user;

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){   // Message id 가 0 이면

                    Bundle bundle = msg.getData();

                    String res = bundle.getString("jsonObj"); // 메인스레드의 UI 내용 변경

                    sendTosat(getApplicationContext(),"Info full Loaded");

                    LoadSuccess(res);
                }
                else if(msg.what == 1)
                {
                    Bundle bundle = msg.getData();

                    String res = bundle.getString("invalid"); // 메인스레드의 UI 내용 변경

                    sendTosat(getApplicationContext(),res);
                }

            }
        };

        try{
            Log.i("dbg","we just try");
            Thread tmp = new Thread() {
                public void run() {
                    Log.i("dbg","we just thread");
                    try {

                        GetConnManaFinal conn = new GetConnManaFinal();

                        String toReturn =
                                conn.sendGet(url+query);
                        Log.i("dbg",toReturn);
                        if(toReturn.matches("false")) {
                            Log.i("dbg", toReturn);

                            Bundle bundle = new Bundle();
                            bundle.putString("invalid", "invalid user info");

                            Message msg = handler.obtainMessage();

                            msg.setData(bundle);
                            msg.what = 1;

                            handler.sendMessage(msg);
                        }
                        else
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("jsonObj", toReturn);

                            Message msg = handler.obtainMessage();

                            msg.setData(bundle);
                            msg.what = 0;

                            handler.sendMessage(msg);
                        }
                    } catch (Exception e){e.printStackTrace();}


                }
            };
            tmp.start();

        }catch (Exception e){Log.i("dbg",e.toString());}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show:
            {
                Intent intent = new Intent(this,ShowSchedule.class);
                startActivity(intent);
                break;
            }

            case R.id.write:
            {
                Intent intent = new Intent(this,WriteSchedule.class);
                intent.putExtra("name",name);
                intent.putExtra("troop",troop);
                intent.putExtra("section",section);
                startActivity(intent);
                break;
            }
            case R.id.info:
            {
                Intent intent = new Intent(this,ShowPersonalInfo.class);
                intent.putExtra("sn",user);
                intent.putExtra("troop",troop);
                intent.putExtra("name",name);
                startActivity(intent);
                break;
            }
            case R.id.out:
            {

                Intent intent = new Intent(this,SignIn.class);
                startActivity(intent);
                break;
            }
        }
    }
}
