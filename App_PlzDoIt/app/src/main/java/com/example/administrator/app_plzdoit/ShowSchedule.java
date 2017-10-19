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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShowSchedule extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    TextView date;
    TextView command;
    TextView operation ;
    TextView insa;
    TextView logistic;
    TextView signal;
    TextView intel;

    EditText year;
    EditText week;

    Button load;
    Button high;

    SeekBar seekBar;

    String troop="";

    JSONArray TitleCommand ;
    JSONArray TitleSignal;
    JSONArray TitleInsa ;
    JSONArray TitleIntel ;
    JSONArray TitleLogistic;
    JSONArray TitleOpertion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule);


        year=(EditText)findViewById(R.id.year);
        week=(EditText)findViewById(R.id.week);

        Intent intent = getIntent();
        year.setText(intent.getStringExtra("year"));
        week.setText(intent.getStringExtra("week"));
        troop = intent.getStringExtra("troop");


        date = (TextView)findViewById(R.id.date);
        command = (TextView)findViewById(R.id.comma);
        operation = (TextView)findViewById(R.id.oper);
        intel = (TextView)findViewById(R.id.intel);
        insa = (TextView)findViewById(R.id.insa);
        logistic = (TextView)findViewById(R.id.logi);
        signal = (TextView)findViewById(R.id.sign);


        load = (Button)findViewById(R.id.load);
        high = (Button)findViewById(R.id.showHT);

        seekBar = (SeekBar)findViewById(R.id.selDay);

        load.setOnClickListener(this);

        high.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(this);

    }


        private static void sendTosat(Context context, String Tmsg)
        {
            Toast t = Toast.makeText(context, Tmsg, Toast.LENGTH_SHORT);
            t.show();
        }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.load:
            {

                final String url = getString(R.string.serverhost)+"RST";
                final String query = "?year=" + year.getText().toString()+"&week="+week.getText().toString()+"&troop="+troop;



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

                           String toReturn =conn.sendGet(url+query);
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

            case R.id.showHT:
            {

            }
        }

    }

    private void LoadSuccess(String res) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(res);




            TitleCommand = (JSONArray)jsonObj.get("command");
            TitleSignal = (JSONArray)jsonObj.get("signal");
            TitleInsa = (JSONArray)jsonObj.get("insa");
            TitleIntel = (JSONArray)jsonObj.get("intel");
            TitleLogistic = (JSONArray)jsonObj.get("logistic");
            TitleOpertion = (JSONArray)jsonObj.get("operation");





            // Log.i("dbg",troop);
        }catch (ParseException e){e.printStackTrace();}
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser)
        {

            changeDay(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void changeDay(int index)
    {
        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

        date.setText(days[index]);
        if(TitleCommand!=null) {
            if (TitleCommand.get(index) != null && !(TitleCommand.get(index) instanceof String))
                command.setText((String) (((JSONArray) TitleCommand.get(index)).get(0)));
        else command.setText("");
        }
        if(TitleOpertion!=null){
            if(TitleOpertion.get(index)!=null&&!(TitleOpertion.get(index) instanceof String))
                operation.setText((String)(((JSONArray)(TitleOpertion.get(index))).get(0)));
            else operation.setText("");
        }
        if(TitleInsa!=null){
            if(TitleInsa.get(index)!=null&&!(TitleInsa.get(index) instanceof String))
                insa.setText((String)(((JSONArray)TitleInsa.get(index)).get(0)));
            else insa.setText("");
        }
        if(TitleLogistic!=null){
            if(TitleLogistic.get(index)!=null&&!(TitleLogistic.get(index) instanceof String))
                logistic.setText((String)(((JSONArray)TitleLogistic.get(index)).get(0)));
            else command.setText("");
        }
        if(TitleSignal!=null){
            if(TitleSignal.get(index)!=null&&!(TitleSignal.get(index) instanceof String))
                signal.setText((String)(((JSONArray)TitleSignal.get(index)).get(0)));
            else signal.setText("");
        }
        if(TitleIntel!=null){
            if(TitleIntel.get(index)!=null&&!(TitleIntel.get(index) instanceof String))
                intel.setText((String)(((JSONArray)TitleIntel.get(index)).get(0)));
            else intel.setText("");
        }




    }
}
