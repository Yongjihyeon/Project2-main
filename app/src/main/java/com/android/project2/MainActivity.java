package com.android.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new MonthFragment()).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ScheduleActivity를 띄우기 위한 intent 생성
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);

                // DateInfo 클래스로부터 연,월,일,시간 정보 가져옴
                DateInfo dateInfo = DateInfo.getInstance();
                int year = dateInfo.getYear();
                int month = dateInfo.getMonth();
                int date = dateInfo.getDate();
                int hour = dateInfo.getHour();

                // intent에 추가
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("date", date);
                intent.putExtra("hour", hour);
                startActivityForResult(intent, 0);

            }
        });
    }

    @Override


    public boolean onCreateOptionsMenu(Menu menu) {
        // 앱바의 옵션 버튼을 누를 경우 mainmenu.xml의 레이아웃을 보여줌
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 옵션 메뉴에서 '월간' 클릭 시 MonthViewFragment를 띄워 월간 달력 보여줌
            case R.id.action_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MonthFragment()).commit();
                Toast.makeText(getApplicationContext(), "monthview", Toast.LENGTH_SHORT).show();
                return true;

            // 옵션 메뉴에서 '주간' 클릭 시 WeekViewFragment를 띄워 주간 달력 보여줌
            case R.id.action_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new WeekViewFragment()).commit();
                Toast.makeText(getApplicationContext(), "weekview", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
