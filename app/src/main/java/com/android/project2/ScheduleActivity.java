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

    // 수정할 일정의 _id값, 디폴트는 -1
    private int _id;
    // 수정할 일정 객체
    private Schedule schedule;

    // 수정하는 연,월,일
    private int year;
    private int month;
    private int date;

    // 제목
    private EditText titleEditText;
    // 시작시간
    private TimePicker startPicker;
    // 종료시간
    private TimePicker endPicker;
    // 지역
    private EditText placeEditText;
    // 메모
    private EditText memoEditText;

    // 장소 검색 버튼
    private Button placeFindButton;
    // 구글지도
    GoogleMap mGoogleMap = null;
    // 구글지도 좌표
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        // default 날짜 설정을 위한 캘린더 객체
        Calendar calendar = Calendar.getInstance();

        // 인텐트 선언 및 year, month, date, _id값 전달 받음
        Intent intent = getIntent();
        year = intent.getIntExtra("year", -1);
        month = intent.getIntExtra("month", -1);
        date = intent.getIntExtra("date", -1);
        _id = intent.getIntExtra("_id", -1);

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
        if ( startHour == -1 ) startHour = calendar.get(Calendar.HOUR_OF_DAY);
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

        // 저장 버튼 누르면
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //취소 버튼 누르면 액티비티 종료
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
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
                if (mGoogleMap != null) {
                    LatLng location = new LatLng(37.5882827, 127.006390);
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title("한성대입구역").
                                    alpha(0.8f).
                                    snippet("4호선")
                    );
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                }
            }
        });

    }

    @Override
  public void onMapReady(GoogleMap googleMap) {
      mGoogleMap = googleMap;

      LatLng hansung = new LatLng(37.5817891, 127.009854);
      googleMap.addMarker(
              new MarkerOptions().
                      position(hansung).
                      title("한성대학교"));

      // move the camera
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hansung,15));

      mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
  }

            class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTitle().equals("한성대입구역")) {
                        Toast.makeText(getApplicationContext(),"한성대입구역을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            }
        }