package com.yerim.dust;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int PERMISSION_REQUEST_CODE = 100;
    Button button, near;
    static EditText etAddr;
    String from = "WGS84";
    String to = "TM";
    static Spinner sido, station;
    static String sidolist[]
            = {"서울", "부산", "대전", "대구", "광주", "울산", "경기", "강원", "충북", "충남", "경북", "경남", "전북", "전남", "제주"};
    static String stationlist[]; //측정소목록
    static ArrayAdapter<String> spinnerSido, spinnerStation; //spinner에 붙일 array adapter
    static TextView totalcnt, tvAddr, tvDate, tvGrade;
    static TextView tvpm10, tvpm10grade, tvpm25, tvpm25grade, tvo3, tvo3grade,
            tvno2, tvno2grade, tvco, tvcograde, tvso2, tvso2grade;
    static LinearLayout frame1, frame2, frame3, frame4;
    static int stationCnt = 0;
    static ImageView imageView;
    static Context mContext;
    private String[] permissions
            = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.layout_test2);
        init();
        tvDate.setText(getTime());
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
        imageView = (ImageView) findViewById(R.id.imageView);

        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvGrade = (TextView) findViewById(R.id.tvGrade);

        totalcnt = (TextView) findViewById(R.id.totalcnt);

        tvpm10 = (TextView) findViewById(R.id.tvpm10);
        tvpm10grade = (TextView) findViewById(R.id.tvpm10grade);
        tvpm25 = (TextView) findViewById(R.id.tvpm25);
        tvpm25grade = (TextView) findViewById(R.id.tvpm25grade);
        tvo3 = (TextView) findViewById(R.id.tvo3);
        tvo3grade = (TextView) findViewById(R.id.tvo3grade);
        tvno2 = (TextView) findViewById(R.id.tvno2);
        tvno2grade = (TextView) findViewById(R.id.tvno2grade);
        tvco = (TextView) findViewById(R.id.tvco);
        tvcograde = (TextView) findViewById(R.id.tvcograde);
        tvso2 = (TextView) findViewById(R.id.tvso2);
        tvso2grade = (TextView) findViewById(R.id.tvso2grade);

        etAddr = (EditText) findViewById(R.id.etAddr);
        button = (Button) findViewById(R.id.button);
        near = (Button) findViewById(R.id.near);

        frame1 = (LinearLayout) findViewById(R.id.frame1);
        frame2 = (LinearLayout) findViewById(R.id.frame2);
        frame3 = (LinearLayout) findViewById(R.id.frame3);
        frame4 = (LinearLayout) findViewById(R.id.frame4);

        sido = (Spinner) findViewById(R.id.sido);
        station = (Spinner) findViewById(R.id.station);
        sido.setOnItemSelectedListener(this);
        station.setOnItemSelectedListener(this);

        button.setOnClickListener(this);    //대기정보 가져오는 버튼 리스너
        near.setOnClickListener(this); //측정소정보
        spinnerSido = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, sidolist);    //array adapter에 시도 리스트를 넣어줌
        sido.setAdapter(spinnerSido);    //스피너에 adapter를 연결

        checkPermissions();

    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static void getDust(String name) {

        GetDustThread3.active = true;
        GetDustThread3 getdustthread = new GetDustThread3(false, name);
        getdustthread.start();
    }

    public static void DustThreadResponse(String getCnt, String[] stvDate, String[] stvpm10, String[] stvpm10grade,
                                          String[] stvpm25, String[] stvpm25grade, String[] stvo3, String[] stvo3grade,
                                          String[] stvno2, String[] stvno2grade, String[] stvco, String[] stvcograde,
                                          String[] stvso2, String[] stvso2grade) {
        if (getCnt == null) {
            // getCnt 변수가 null인 경우 처리할 내용 작성
            return; // 예를 들어, return 또는 에러 처리 로직 추가
        }

        stationCnt = 0;
        stationCnt = Integer.parseInt(getCnt);
        Log.w("stationcnt", String.valueOf(stationCnt));

        if (stationCnt == 0) {    //만약 측정정보가 없다면
            totalcnt.setText("측정소 정보가 없거나 측정정보가 없습니다.");
            tvDate.setText("");
            tvpm10.setText("");
            tvpm10grade.setText("");
            tvpm25.setText("");
            tvpm25grade.setText("");
            tvo3.setText("");
            tvo3grade.setText("");
            tvno2.setText("");
            tvno2grade.setText("");
            tvco.setText("");
            tvcograde.setText("");
            tvso2.setText("");
            tvso2grade.setText("");

        } else {    //측정정보있으면
            totalcnt.setText(stvDate[0] + "에 대기정보가 업데이트 되었습니다.");
            tvDate.setText(stvDate[0]);
            tvpm10.setText(stvpm10[0] + "μg/m³");
            tvpm10grade.setText(stvpm10grade[0]);
            tvpm25.setText(stvpm25[0] + "μg/m³");
            tvpm25grade.setText(stvpm25grade[0]);
            tvo3.setText(stvo3[0] + "ppm");
            tvo3grade.setText(stvo3grade[0]);
            tvno2.setText(stvno2[0] + "ppm");
            tvno2grade.setText(stvno2grade[0]);
            tvco.setText(stvco[0] + "ppm");
            tvcograde.setText(stvcograde[0]);
            tvso2.setText(stvso2[0] + "ppm");
            tvso2grade.setText(stvso2grade[0]);
            grade();
        }

        GetDustThread3.active = false;
        GetDustThread3.interrupted();
    }


    static void grade() {
        ////////////////등급 처리
        if (tvpm10grade.getText().equals("1")) {
            tvpm10grade.setText("좋음"); tvGrade.setText(tvpm10grade.getText().toString());
            frame1.setVisibility(View.VISIBLE); imageView.setImageResource(R.drawable.happy1);
        } else if (tvpm10grade.getText().equals("2")) {
            tvpm10grade.setText("보통"); tvGrade.setText(tvpm10grade.getText().toString());
            frame2.setVisibility(View.VISIBLE); imageView.setImageResource(R.drawable.happy3);
        } else if (tvpm10grade.getText().equals("3")) {
            tvpm10grade.setText("나쁨"); tvGrade.setText(tvpm10grade.getText().toString());
            frame3.setVisibility(View.VISIBLE); imageView.setImageResource(R.drawable.bad1);
        } else if (tvpm10grade.getText().equals("4")) {
            tvpm10grade.setText("매우나쁨"); tvGrade.setText(tvpm10grade.getText().toString());
            frame4.setVisibility(View.VISIBLE); imageView.setImageResource(R.drawable.bad3);
        }

        if (tvpm25grade.getText().equals("1")) {
            tvpm25grade.setText("좋음");
        } else if (tvpm25grade.getText().equals("2")) {
            tvpm25grade.setText("보통");
        } else if (tvpm25grade.getText().equals("3")) {
            tvpm25grade.setText("나쁨");
        } else if (tvpm25grade.getText().equals("4")) {
            tvpm25grade.setText("매우나쁨");
        }

        if (tvo3grade.getText().equals("1")) {
            tvo3grade.setText("좋음");
        } else if (tvo3grade.getText().equals("2")) {
            tvo3grade.setText("보통");
        } else if (tvo3grade.getText().equals("3")) {
            tvo3grade.setText("나쁨");
        } else if (tvo3grade.getText().equals("4")) {
            tvo3grade.setText("매우나쁨");
        }

        if (tvno2grade.getText().equals("1")) {
            tvno2grade.setText("좋음");
        } else if (tvno2grade.getText().equals("2")) {
            tvno2grade.setText("보통");
        } else if (tvno2grade.getText().equals("3")) {
            tvno2grade.setText("나쁨");
        } else if (tvno2grade.getText().equals("4")) {
            tvno2grade.setText("매우나쁨");
        }

        if (tvcograde.getText().equals("1")) {
            tvcograde.setText("좋음");
        } else if (tvcograde.getText().equals("2")) {
            tvcograde.setText("보통");
        } else if (tvcograde.getText().equals("3")) {
            tvcograde.setText("나쁨");
        } else if (tvcograde.getText().equals("4")) {
            tvcograde.setText("매우나쁨");
        }

        if (tvso2grade.getText().equals("1")) {
            tvso2grade.setText("좋음");
        } else if (tvso2grade.getText().equals("2")) {
            tvso2grade.setText("보통");
        } else if (tvso2grade.getText().equals("3")) {
            tvso2grade.setText("나쁨");
        } else if (tvso2grade.getText().equals("4")) {
            tvso2grade.setText("매우나쁨");
        }
    }

    public static void getStationList(String name) {    //이건 측정소 정보가져올 스레드

        GetStationThread3.active = true;
        GetStationThread3 getstationthread = new GetStationThread3(false, name);
        getstationthread.start();    //스레드 시작
    }

    public static void StationThreadResponse(String cnt, String[] sStation) {    //측정소 정보를 가져온 결과

        stationCnt = 0;
        stationCnt = Integer.parseInt(cnt);
        stationlist = new String[stationCnt];
        for (int i = 0; i < stationCnt; i++) {
            stationlist[i] = sStation[i];
            Log.e("station", stationlist[i]);
        }

        spinnerStation = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, sidolist);
        spinnerStation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station.setAdapter(spinnerStation);

        GetDustThread3.active = false;
        GetDustThread3.interrupted();
    }

    public static void getNearStation(String yGrid, String xGrid) {    //이건 측정소 정보가져올 스레드

        GetStationThread3.active = true;
        GetStationThread3 getstationthread = new GetStationThread3(false, yGrid, xGrid);        //스레드생성(UI 스레드사용시 system 뻗는다)
        getstationthread.start();    //스레드 시작

    }

    public static void NearStationThreadResponse(String[] sStation, String[] sAddr, String[] sTm) {    //측정소 정보를 가져온 결과
        etAddr.setText(sStation[0]);
        totalcnt.setText("가까운 측정소 :" + sStation[0] + "\r\n측정소 주소 :" + sAddr[0] + "\r\n측정소까지 거리 :" + sTm[0] + "km");
        GetStationThread3.active = false;
        GetStationThread3.interrupted();
    }

    void getStation(String yGrid, String xGrid) {

        if (xGrid != null && yGrid != null) {
            GetCoordThread4.active = true;
            GetCoordThread4 getCoordthread = new GetCoordThread4(false, xGrid, yGrid, from, to);        //스레드생성(UI 스레드사용시 system 뻗는다)
            getCoordthread.start();    //스레드 시작
        } else {
            Toast.makeText(getApplication(), "좌표값 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void TransCoordThreadResponse(String x, String y) {
        if (x.equals("NaN") || y.equals("NaN")) {
            totalcnt.setText("좌표값이 잘못 입력되었거나 해당값이 없습니다.");
        } else {
            //totalcnt.append("\r\n변환된 좌표값은 " + x + "," + y + "입니다.");
            getNearStation(y, x);
        }
        GetCoordThread4.active = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button:    //대기정보 가져오는 버튼
                String stationName;
                stationName = etAddr.getText().toString();
                getDust(stationName);
                break;
            case R.id.near:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    location.setLatitude(36.3522327);
                    location.setLongitude(127.387204);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getStation(String.valueOf(latitude),String.valueOf(longitude));
                        Log.d("현재위경도","위도:"+ latitude+" / 경도:"+longitude);
                        // 위치 정보 사용
                    } else {
                        // 위치 정보가 없는 경우 처리
                    }
                } else {
                    // 위치 권한이 허용되지 않은 경우 처리
                }
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
