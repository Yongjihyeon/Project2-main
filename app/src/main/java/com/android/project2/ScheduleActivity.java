package com.android.project2;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// 일정을 생성/수정/삭제하기 위한 액티비티
public class ScheduleActivity extends AppCompatActivity implements OnMapReadyCallback {

    // 수정하는 연도 ,월,일
    private int year;
    private int month;
    private int date;

    private EditText titleEditText;

    private TimePicker startPicker;

    private TimePicker endPicker;

    private EditText placeEditText;

    private EditText memoEditText;


    private Button placeFindButton;

    GoogleMap mGoogleMap = null;
    private Geocoder geocoder;//구글 지도 좌표를 위한...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        // default 날짜 설정을 위한 캘린더 객체
        Calendar calendar = Calendar.getInstance();

        // 인텐트 선언 및 year, month, date 값 전달
        Intent intent = getIntent();
        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);
        date = intent.getIntExtra("date", -1);

        // 각 입력 필드 수정
        titleEditText = findViewById(R.id.schedule_title);
        startPicker = findViewById(R.id.schedule_start);
        endPicker = findViewById(R.id.schedule_end);
        placeEditText = findViewById(R.id.schedule_place);
        memoEditText = findViewById(R.id.schedule_memo);
        placeFindButton = findViewById(R.id.schedule_find_btn);

        // 구글지도 수정용
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.schedule_map);
        mapFragment.getMapAsync(this);

        // startHour 변수를 입력받은 시간이 있으면 (=주간 달력에서 넘어온 경우) 해당 시간으로, 없으면 현재 시간으로 설정
        int startHour = intent.getIntExtra("hour", -1);
        if (startHour == -1) startHour = calendar.get(Calendar.HOUR_OF_DAY);
        // endHour는 startHour의 1시간 뒤
        int endHour = (startHour + 1) % 24;

        titleEditText.setText(String.format(Locale.KOREA, "%d년 %d월 %d일 %d시", year, month + 1, date, startHour));

        // 시작 시간은 현재 시간, 종료 시간은 거기서 +1한 시간으로 설정
        startPicker.setCurrentHour(startHour);
        startPicker.setCurrentMinute(0);

        endPicker.setCurrentHour(endHour);
        endPicker.setCurrentMinute(0);

        // 저장, 취소, 삭제 버튼
        Button saveButton = findViewById(R.id.schedule_save_btn);
        Button cancelButton = findViewById(R.id.schedule_cancel_btn);
        Button delButton = findViewById(R.id.schedule_del_btn);

        // 저장 버튼
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //취소 버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                ScheduleActivity.this.finish();
            }
        });

        // 삭제 버튼 누르면
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        placeFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        geocoder = new Geocoder(this);

        LatLng hansung = new LatLng(37.5817891, 127.009854);
        googleMap.addMarker(
                new MarkerOptions().
                        position(hansung).
                        title("한성대학교"));

        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung, 15));

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("한성대입구역")) {
                    Toast.makeText(getApplicationContext(), "한성대입구역을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        // 장소 찾기 버튼 이벤트 설정
        placeFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = placeEditText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            place, // 주소
                            1 // 최대 검색 결과 개수
                    );
                } catch (IOException e) {
                    Log.e("[[ScheduleActivity]]", e.getMessage());
                }

                if (addressList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 콤마를 기준으로 split
                String[] splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소

                // 검색결과 문자열 예시:
                // Address[addressLines=[0:"Seoul, South Korea"],feature=Seoul,admin=Seoul,sub-admin=null,locality=null,thoroughfare=null,postalCode=null,countryCode=KR,countryName=South Korea,hasLatitude=true,latitude=37.566535,hasLongitude=true,longitude=126.97796919999999,phone=null,url=null,extras=null]
                // ,split 후에 11번째와 13번째 요소에서 위도(latitude), 경도(longitude) 를 알아냄

                String latitude = splitStr[11].substring(splitStr[11].indexOf("=") + 1); // 위도
                String longitude = splitStr[13].substring(splitStr[13].indexOf("=") + 1); // 경도

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(address);
                mOptions2.position(point);
                // 마커 추가
                mGoogleMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
            }
        });
    }
}