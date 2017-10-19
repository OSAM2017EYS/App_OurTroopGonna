package com.example.administrator.ourtroopgonna;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import android.app.DownloadManager;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.Header;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private int count = 0;
    private String user = "";
    private String pw = "";
    private String troop = "";
    private  String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button si = (Button) findViewById(R.id.SI);
        Button su = (Button) findViewById((R.id.SU));
        si.setOnClickListener(this);
        su.setOnClickListener(this);


    }

    public void LoginSuccess(String jsonString)
    {
        try {
            JSONParser parser = new JSONParser();
            JSONArray JsonArray = (JSONArray) parser.parse(jsonString);
            troop = (String) JsonArray.get(1);
            name = (String) JsonArray.get(2);
            Log.i("dbg",troop);
        }catch (ParseException e){e.printStackTrace();}


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("pw",pw);
        intent.putExtra("troop",troop);
        intent.putExtra("name",name);

        ((EditText) findViewById(R.id.solNumber_SI)).setText("");
        ((EditText) findViewById(R.id.pw_SI)).setText("");


        startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        switch ((v.getId())) {


            case R.id.SI: {



                sendTosat(getApplicationContext(),"Connecting Server");
                user = ((EditText) findViewById(R.id.solNumber_SI)).getText().toString();
                pw = ((EditText) findViewById(R.id.pw_SI)).getText().toString();
                Log.i("dbg", user + " " + pw);
                final String url = getString(R.string.serverhost)+"SI";
                final String query = "?user=" + user + "&pw=" + pw;



                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what == 0){   // Message id 가 0 이면

                            Bundle bundle = msg.getData();

                            String res = bundle.getString("jsonObj"); // 메인스레드의 UI 내용 변경

                            sendTosat(getApplicationContext(),"Success");
                            LoginSuccess(res);
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

                break;

            }

            case R.id.SU: {
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
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




