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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShowPersonalInfo extends AppCompatActivity implements View.OnClickListener{


    TextView SN;
    TextView name;
    TextView troop;
    TextView section;
    TextView auth;
    TextView secret;
    Button setAuth;

    private void LoadSuccess(String jsonString)
    {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(jsonString);

            String psdSection = (String)jsonObj.get("section");
            String psdAdmin = (String)jsonObj.get("admin");
            String psdSecret = (String)jsonObj.get("secretLevel");

            section.setText(psdSection);
            auth.setText(psdAdmin);
            secret.setText(psdSecret);


            // Log.i("dbg",troop);
        }catch (ParseException e){e.printStackTrace();}


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_personal_info);

        Intent intent = getIntent();
        String snVal =  intent.getStringExtra("sn");
        String troopVal =  intent.getStringExtra("troop");
        String nameVal =  intent.getStringExtra("name");


        SN= (TextView)findViewById(R.id.solNumInfo);
        name= (TextView)findViewById(R.id.nameInfo);
        troop=(TextView) findViewById(R.id.troopInfo);
        section= (TextView)findViewById(R.id.sectionInfo);
        auth=(TextView) findViewById(R.id.adminInfo);
        secret=(TextView) findViewById(R.id.secretInfo);
        setAuth = (Button)findViewById(R.id.toSetAuth);
        setAuth.setOnClickListener(this);

        SN.setText(snVal);
        name.setText(nameVal);
        troop.setText(troopVal);


        sendTosat(getApplicationContext(),"Connecting Server");

        Log.i("dbg", snVal);
        final String url = getString(R.string.serverhost)+"GMI";
        final String query = "?user=" + snVal;



        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){   // Message id 가 0 이면

                    Bundle bundle = msg.getData();

                    String res = bundle.getString("jsonObj"); // 메인스레드의 UI 내용 변경

                    sendTosat(getApplicationContext(),"Info Loaded");

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

        switch (v.getId())
        {

            case R.id.toSetAuth: {

                if(auth.getText().toString().matches("Y"))
                {
                    Intent intent = new Intent(this, SetAuth.class);
                    intent.putExtra("user",SN.getText().toString());
                    startActivity(intent);
                }
                else
                {
                    sendTosat(getApplicationContext(),"You Have No Permission");
                }

                break;
            }
        }

    }

    private static void sendTosat(Context context, String Tmsg)
    {
        Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_SHORT);
        t.show();
    }
}
