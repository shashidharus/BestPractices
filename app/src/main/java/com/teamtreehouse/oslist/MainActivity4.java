package com.teamtreehouse.oslist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity4 extends MainActivity implements View.OnClickListener {

    CalendarView calendar;
    Button trackAttendanceButton;
    Button trackSessionsButton;
    Button trackActivities;
    String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Tracking");
        //initializes the calendarview
        initializeCalendar();

    }

   /* @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(MainActivity4.this);
    }
*/
    public void initializeCalendar() {
        trackAttendanceButton = (Button)findViewById(R.id.trackAttendance);
        trackAttendanceButton.setOnClickListener(this);
        trackSessionsButton = (Button)findViewById(R.id.trackSessions);
        trackSessionsButton.setOnClickListener(this);
        trackActivities = (Button)findViewById(R.id.trackActivities);
        trackActivities.setOnClickListener(this);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setVerticalScrollBarEnabled(true);
        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Sunday as the first day of the Calendar
        calendar.setFirstDayOfWeek(1);

        //The background color for the selected week.
        //calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        //calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.grey));

        //sets the color for the separator line between weeks.
       // calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        //calendar.setSelectedDateVerticalBar(R.color.darkgreen);



        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                // Toast.makeText(getApplicationContext(), month+1 + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
                curDate = month + 1 + "/" + day + "/" + year;
            }
        });
    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.trackAttendance:
                trackAttendanceMethod();
                break;
            case R.id.trackSessions:
                trackSessionsMethod();
                break;
            case R.id.trackActivities:
                trackActivitiesMethod();
                break;
        }
    }

    private  void trackAttendanceMethod()
    {
        if(curDate == null || curDate.isEmpty())
        {
            startActivity(new Intent("com.teamtreehouse.oslist.MainActivity5"));
        }
        else
        {
            Intent addNewClass = new Intent(MainActivity4.this, MainActivity5.class);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            Date dt1 = new Date();
            try {
                dt1 = format1.parse(curDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putLong("SelectedDate", dt1.getTime());

            addNewClass.putExtras(bundle);
            startActivity(addNewClass);
        }
        //startActivity(new Intent("com.teamtreehouse.oslist.MainActivity5"));
    }

    private  void trackSessionsMethod()
    {
        if(curDate == null || curDate.isEmpty())
        {
            Log.d("In4","Activity4to7");
            startActivity(new Intent("com.teamtreehouse.oslist.MainActivity6"));
        }
        else
        {
            Intent addNewClass = new Intent(MainActivity4.this, MainActivity6.class);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            Date dt1 = new Date();
            try {
                dt1 = format1.parse(curDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putLong("SelectedDate", dt1.getTime());

            addNewClass.putExtras(bundle);
            startActivity(addNewClass);
        }
        //startActivity(new Intent("com.teamtreehouse.oslist.MainActivity6"));
    }
    private  void trackActivitiesMethod(){
        if(curDate == null || curDate.isEmpty())
        {
            Log.d("In4","Activity4to6");
            startActivity(new Intent("com.teamtreehouse.oslist.MainActivity7"));
        }
        else
        {
            Intent addNewClass = new Intent(MainActivity4.this, MainActivity7.class);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            Date dt1 = new Date();
            try {
                dt1 = format1.parse(curDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putLong("SelectedDate", dt1.getTime());

            addNewClass.putExtras(bundle);
            startActivity(addNewClass);
        }
    }
}
