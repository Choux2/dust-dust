package com.yerim.dust;

import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button button;
    EditText etAddr;
    static Spinner sido, station;
    static String sidolist[]
            = {"선택","서울","부산","대전","대구","광주","울산","경기","강원","충북","충남","경북","경남","전북","전남","제주"};
    static String stationlist[]; //측정소목록
    static ArrayAdapter<String> spinnerSido, spinnerStation; //spinner에 붙일 array adapter
    static TextView totalcnt, tvAddr, tvDate, tvGrade;
    static TextView tvpm10, tvpm10grade, tvpm25, tvpm25grade, tvo3, tvo3grade,
            tvno2, tvno2grade, tvco, tvcograde, tvso2, tvso2grade;
    static LinearLayout frame1, frame2, frame3, frame4, frame5;
    static int stationCnt=0;
    static ImageView imageView;
//    public static String format_yyyyMMdd_HHmmss = "yyyy-MM-dd hh:mm";

    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        init();

    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = dateFormat.format(date);

        return getTime;
    }
    public void init() {
        mContext = getApplicationContext(); //static에서 context를 쓰기위해..
        imageView= (ImageView)findViewById(R.id.imageView);

        tvAddr= (TextView)findViewById(R.id.tvAddr);
        tvDate= (TextView)findViewById(R.id.tvDate);
        tvGrade= (TextView)findViewById(R.id.tvGrade);

        totalcnt = (TextView)findViewById(R.id.totalcnt);

        tvpm10= (TextView)findViewById(R.id.tvpm10); tvpm10grade= (TextView)findViewById(R.id.tvpm10grade);
        tvpm25= (TextView)findViewById(R.id.tvpm25); tvpm25grade= (TextView)findViewById(R.id.tvpm25grade);
        tvo3= (TextView)findViewById(R.id.tvo3); tvo3grade= (TextView)findViewById(R.id.tvo3grade);
        tvno2= (TextView)findViewById(R.id.tvno2); tvno2grade= (TextView)findViewById(R.id.tvno2grade);
        tvco= (TextView)findViewById(R.id.tvco); tvcograde= (TextView)findViewById(R.id.tvcograde);
        tvso2= (TextView)findViewById(R.id.tvso2); tvso2grade= (TextView)findViewById(R.id.tvso2grade);

        etAddr = (EditText) findViewById(R.id.etAddr);
        button = (Button) findViewById(R.id.button);

        frame1 = (LinearLayout) findViewById(R.id.frame1);
        frame2 = (LinearLayout) findViewById(R.id.frame2);
        frame3 = (LinearLayout) findViewById(R.id.frame3);
        frame4 = (LinearLayout) findViewById(R.id.frame4);
        frame5 = (LinearLayout) findViewById(R.id.frame5);

        sido = (Spinner) findViewById(R.id.sido);
        station = (Spinner) findViewById(R.id.station);
        sido.setOnItemSelectedListener(this);
        station.setOnItemSelectedListener(this);

        button.setOnClickListener(this);	//대기정보 가져오는 버튼 리스너
        spinnerSido = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item,sidolist);	//array adapter에 시도 리스트를 넣어줌
        spinnerSido.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	//array adapter 설정
        sido.setAdapter(spinnerSido);	//스피너에 adapter를 연결

    }

        public static void getDust(String name){

        GetDustThread.active = true;
        GetDustThread getdustthread = new GetDustThread(false, name);
        getdustthread.start();
        }

        public static void DustThreadResponse(String stotalCount, String[] stvDate, String[] stvpm10, String[] stvpm10grade,
                                              String[] stvpm25, String[] stvpm25grade, String[] stvo3, String[] stvo3grade,
                                              String[] stvno2,  String[] stvno2grade, String[] stvco, String[] stvcograde,
                                              String[] stvso2, String[] stvso2grade)
        {
            stationCnt = 0;
            stationCnt = Integer.parseInt(stotalCount);
            Log.w("stationcnt", String.valueOf(stationCnt));

            if(stationCnt==0) {	//만약 측정정보가 없다면
                totalcnt.setText("측정소 정보가 없거나 측정정보가 없습니다.");
                tvDate.setText("");
                tvpm10.setText("-"); tvpm10grade.setText("");
                tvpm25.setText("-"); tvpm25grade.setText("");
                tvo3.setText("-"); tvo3grade.setText("");
                tvno2.setText("-"); tvno2grade.setText("");
                tvco.setText("-"); tvcograde.setText("");
                tvso2.setText("-"); tvso2grade.setText("");
                tvAddr.setText("");
                tvGrade.setText("정보가 없습니다.");
                imageView.setImageResource(R.drawable.bad2);
                frame5.setVisibility(View.VISIBLE);

            }else {    //측정정보있으면
                totalcnt.setText(stvDate[0] + "에 대기정보가 업데이트 되었습니다.");
                tvpm10.setText(stvpm10[0] + " μg/m³");
                tvpm10grade.setText(stvpm10grade[0]);
                tvpm25.setText(stvpm25[0] + " μg/m³");
                tvpm25grade.setText(stvpm25grade[0]);
                tvo3.setText(stvo3[0] + "ppm");
                tvo3grade.setText(stvo3grade[0]);
                tvno2.setText(stvno2[0] + " ppm");
                tvno2grade.setText(stvno2grade[0]);
                tvco.setText(stvco[0] + " ppm");
                tvcograde.setText(stvcograde[0]);
                tvso2.setText(stvso2[0] + " ppm");
                tvso2grade.setText(stvso2grade[0]);
                grade();
            }

            GetDustThread.active = false;
            GetDustThread.interrupted();
        }

        static void grade(){
            frame1.setVisibility(View.INVISIBLE);
            frame2.setVisibility(View.INVISIBLE);
            frame3.setVisibility(View.INVISIBLE);
            frame4.setVisibility(View.INVISIBLE);
            frame5.setVisibility(View.INVISIBLE);

            ////////////////등급 처리
            if (tvpm10grade.getText().equals("1")){tvpm10grade.setText("좋음");
                tvGrade.setText(tvpm10grade.getText().toString()); frame1.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.happy1);}
            else if (tvpm10grade.getText().equals("2")){tvpm10grade.setText("보통");
                tvGrade.setText(tvpm10grade.getText().toString()); frame2.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.happy3);}
            else if (tvpm10grade.getText().equals("3")){tvpm10grade.setText("나쁨");
                tvGrade.setText(tvpm10grade.getText().toString()); frame3.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.bad1);}
            else if (tvpm10grade.getText().equals("4")){tvpm10grade.setText("매우 나쁨");
                tvGrade.setText(tvpm10grade.getText().toString()); frame4.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.bad3);}

            if (tvpm25grade.getText().equals("1")){tvpm25grade.setText("좋음");}
            else if (tvpm25grade.getText().equals("2")){tvpm25grade.setText("보통");}
            else if (tvpm25grade.getText().equals("3")){tvpm25grade.setText("나쁨");}
            else if (tvpm25grade.getText().equals("4")){tvpm25grade.setText("매우 나쁨");}

            if (tvo3grade.getText().equals("1")){tvo3grade.setText("좋음");}
            else if (tvo3grade.getText().equals("2")){tvo3grade.setText("보통");}
            else if (tvo3grade.getText().equals("3")){tvo3grade.setText("나쁨");}
            else if (tvo3grade.getText().equals("4")){tvo3grade.setText("매우 나쁨");}

            if (tvno2grade.getText().equals("1")){tvno2grade.setText("좋음");}
            else if (tvno2grade.getText().equals("2")){tvno2grade.setText("보통");}
            else if (tvno2grade.getText().equals("3")){tvno2grade.setText("나쁨");}
            else if (tvno2grade.getText().equals("4")){tvno2grade.setText("매우 나쁨");}

            if (tvcograde.getText().equals("1")){tvcograde.setText("좋음");}
            else if (tvcograde.getText().equals("2")){tvcograde.setText("보통");}
            else if (tvcograde.getText().equals("3")){tvcograde.setText("나쁨");}
            else if (tvcograde.getText().equals("4")){tvcograde.setText("매우 나쁨");}

            if (tvso2grade.getText().equals("1")){tvso2grade.setText("좋음");}
            else if (tvso2grade.getText().equals("2")){tvso2grade.setText("보통");}
            else if (tvso2grade.getText().equals("3")){tvso2grade.setText("나쁨");}
            else if (tvso2grade.getText().equals("4")){tvso2grade.setText("매우 나쁨");}
        }
    public static void getStationList(String name){	//이건 측정소 정보가져올 스레드

        GetStationThread.active=true;
        GetStationThread getstationthread=new GetStationThread(false,name);
        getstationthread.start();	//스레드 시작

    }

    public static void  StationThreadResponse(int cnt,String[] sStation) {    //측정소 정보를 가져온 결과

        GetDustThread.active = false;
        GetDustThread.interrupted();

        Log.e("stationlist cnt=", cnt + "");
        stationlist = new String[cnt];
        for (int i = 0; i < cnt; i++) {
            stationlist[i] = sStation[i];
            Log.e("stationlist=", (i + 1) + " " + stationlist[i]);
        }

        spinnerStation = new ArrayAdapter<>(mContext, R.layout.spinner, stationlist);
        spinnerStation.setDropDownViewResource(R.layout.spinner);
        station.setAdapter(spinnerStation);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button:	//대기정보 가져오는 버튼
                String stationName;
                stationName=etAddr.getText().toString();
                getDust(stationName);
                tvAddr.setText(etAddr.getText().toString());
                tvDate.setText(getTime());
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.sido:		//시도 변경 스피너
                getStationList(sidolist[position]);
                break;

            case R.id.station:	//측정소 변경 스피너
                try{
                    Log.e("station name", stationlist[position]);
                }catch (Exception e){
                    Log.e("exception",""+e);
                }

                etAddr.setText(stationlist[position]);	//측정소이름을 바로 입력해 준다.
                break;


            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
