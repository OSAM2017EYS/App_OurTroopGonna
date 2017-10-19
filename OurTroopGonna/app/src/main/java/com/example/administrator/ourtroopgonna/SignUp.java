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
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class SignUp extends AppCompatActivity implements OnClickListener{





    Button res;
    EditText SolNum;
    EditText pw;
    EditText tc0 ;
    EditText tc1 ;
    EditText tc2 ;
    EditText tc3;
    EditText tc4;
    EditText name;
    EditText[] tcs;


    private static void sendTosat(Context context, String Tmsg)
    {
        Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_SHORT);
        t.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        res = (Button) findViewById(R.id.res);
        SolNum = (EditText) findViewById((R.id.solNumSU));
        pw = (EditText) findViewById((R.id.pwSU));
        tc0 = (EditText) findViewById((R.id.tc0SU));
        tc1 = (EditText) findViewById((R.id.tc1SU));
        tc2 = (EditText) findViewById((R.id.tc2SU));
        tc3 = (EditText) findViewById((R.id.tc3SU));
        tc4 = (EditText) findViewById((R.id.tc4SU));
        name = (EditText) findViewById((R.id.nameSU));
        tcs = new EditText[]{tc0,tc1,tc2,tc3,tc4};


        res.setOnClickListener(this);
        //사용자 정보 입력이 완료되어 등록 버튼을 클릭한 상황
    }

    private void SignUpSuccess()
    {
        res.setText("");
        SolNum.setText("");
        pw.setText("");
        tc0.setText("");
        tc1.setText("");
        tc2.setText("");
        tc3.setText("");
        tc4.setText("");
        name.setText("");

        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        //사용자 등록 정보를 서버로 전송하여 등록시킨다.

        switch (v.getId()) {
            case R.id.res: {
                String SolnumSend = SolNum.getText().toString();
                String PWSend = pw.getText().toString();
                String tcSend = "";
                String NameSend = name.getText().toString();
                boolean tcValid = true;

                for (EditText tc : tcs) {
                    tcSend += tc.getText().toString() + " ";
                    if(tc.getText().toString().isEmpty())
                    {
                        tcValid = false;
                    }
                }

                tcSend = tcSend.trim();


                if (!tcValid || SolnumSend.isEmpty() || PWSend.isEmpty() || NameSend.isEmpty()) {
                    sendTosat(getApplicationContext(),"Fill All Blanks");
                } else {

                    GetConnManaFinal conn = new GetConnManaFinal();
                    final String url = getString(R.string.serverhost) + "SU";
                    final String params = "user=" + SolnumSend + "&pw=" + PWSend + "&troop=" + tcSend + "&name=" + NameSend;


                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0) {   // Message id 가 0 이면

                                Bundle bundle = msg.getData();

                                String res = bundle.getString("valid"); // 메인스레드의 UI 내용 변경

                                sendTosat(getApplicationContext(), res);
                                SignUpSuccess();
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
                                        bundle.putString("invalid", "Already Resistered");

                                        Message msg = handler.obtainMessage();

                                        msg.setData(bundle);
                                        msg.what = 1;

                                        handler.sendMessage(msg);
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("valid", "Resister Successful");

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
                    break;
                }
            }
        }

    }


}
