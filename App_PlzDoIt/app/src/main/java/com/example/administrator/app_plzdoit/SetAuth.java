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
import android.widget.Toast;

public class SetAuth extends AppCompatActivity implements View.OnClickListener {

    String user;

    EditText setSecSol;
    EditText setSecSec;
    EditText setAdmSol;
    EditText setHandSol;
    EditText setHandLevel;
    Button secCon;
    Button admCon;
    Button admDis;
    Button handCon;





    private static void sendTosat(Context context, String Tmsg)
    {
        Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_auth);

        Intent intent = getIntent();
        user=intent.getStringExtra("user");

        setSecSol = (EditText)findViewById(R.id.solNumSetSec);
        setSecSec = (EditText)findViewById(R.id.sectionSetSec);
        setAdmSol = (EditText)findViewById(R.id.solNumSetAdm);
        setHandSol = (EditText)findViewById(R.id.solNumSetHandle);
        setHandLevel = (EditText)findViewById(R.id.secretLevelSetHandle);

        secCon = (Button)findViewById(R.id.secCon);
        admCon = (Button)findViewById(R.id.admCon);
        admDis = (Button)findViewById(R.id.admDis);
        handCon = (Button)findViewById(R.id.secretCon);

        secCon.setOnClickListener(this);
        admCon.setOnClickListener(this);
        handCon.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.secCon:
            {
                GetConnManaFinal conn = new GetConnManaFinal();
                final String url = getString(R.string.serverhost) + "SAu";
                final String params = "user=" + user + "&target=" + setSecSol.getText().toString() + "&sec=" + setSecSec.getText().toString() ;


                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {   // Message id 가 0 이면

                            Bundle bundle = msg.getData();

                            String res = bundle.getString("valid"); // 메인스레드의 UI 내용 변경

                            sendTosat(getApplicationContext(), res);

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
                                    bundle.putString("invalid", "Out of Permission");

                                    Message msg = handler.obtainMessage();

                                    msg.setData(bundle);
                                    msg.what = 1;

                                    handler.sendMessage(msg);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("valid", "Section Setted");

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

            case R.id.admCon:
            {
                GetConnManaFinal conn = new GetConnManaFinal();
                final String url = getString(R.string.serverhost) + "SAd";
                final String params = "user=" + user + "&target=" + setAdmSol.getText().toString() + "&value=Y"  ;


                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {   // Message id 가 0 이면

                            Bundle bundle = msg.getData();

                            String res = bundle.getString("valid"); // 메인스레드의 UI 내용 변경

                            sendTosat(getApplicationContext(), res);

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
                                    bundle.putString("invalid", "Out of Permission");

                                    Message msg = handler.obtainMessage();

                                    msg.setData(bundle);
                                    msg.what = 1;

                                    handler.sendMessage(msg);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("valid", "Admin Setted");

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
            case R.id.admDis:
            {
                GetConnManaFinal conn = new GetConnManaFinal();
                final String url = getString(R.string.serverhost) + "SAd";
                final String params = "user=" + user + "&target=" + setAdmSol.getText().toString() + "&value=N"  ;


                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {   // Message id 가 0 이면

                            Bundle bundle = msg.getData();

                            String res = bundle.getString("valid"); // 메인스레드의 UI 내용 변경

                            sendTosat(getApplicationContext(), res);

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
                                    bundle.putString("invalid", "Out of Permission");

                                    Message msg = handler.obtainMessage();

                                    msg.setData(bundle);
                                    msg.what = 1;

                                    handler.sendMessage(msg);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("valid", "Admin Setted");

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

            case R.id.secretCon:
            {

                GetConnManaFinal conn = new GetConnManaFinal();
                final String url = getString(R.string.serverhost) + "SSL";
                final String params = "user=" + user + "&target=" + setHandSol.getText().toString() + "&secret="+setHandLevel.getText().toString()  ;


                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {   // Message id 가 0 이면

                            Bundle bundle = msg.getData();

                            String res = bundle.getString("valid"); // 메인스레드의 UI 내용 변경

                            sendTosat(getApplicationContext(), res);

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
                                    bundle.putString("invalid", "Out of Permission");

                                    Message msg = handler.obtainMessage();

                                    msg.setData(bundle);
                                    msg.what = 1;

                                    handler.sendMessage(msg);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("valid", "Handling Level Setted");

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
