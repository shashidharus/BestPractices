package com.teamtreehouse.oslist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class MainActivity6 extends ActionBarActivity implements View.OnClickListener{

    Bundle extras;
    CalendarView calendar;
    private TableLayout queryTableLayout;
    TextView classNameView;
    TextView courseIDView;
    TextView textTargetStudySessions;
    TextView wtdSessionsView;
    TextView ttdSessionsView;
    TextView txtCompletedSessions;

    ImageButton buttonHelp;
    HashMap<String,String> weekDays = new HashMap<String, String>();
    String curDate;
    String selectedWeekDay;
    Date currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity6);
        populateHashMap();
        //initializes the calendarview
        initializeCalendar();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        populateSelectedDateData();
    }

    public View.OnClickListener saveButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {


          RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
           // RelativeLayout buttonTableRow = (RelativeLayout) v.getParent().getParent().getParent();
           //TableLayout buttonTableRow = (TableLayout) v.getParent();
            TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumber);
            String courseIDString = getCourseID.getText().toString();
            TextView getcompletedStudySessions = (TextView) buttonTableRow.findViewById(R.id.txtCompletedSessions);
            String completedStudySessionsString = getcompletedStudySessions.getText().toString();

            TextView targetedWeeklySessions = (TextView)buttonTableRow.findViewById(R.id.txtTargetSessions);


            DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity6.this);
            databaseConnector.open();

            if(curDate == null || curDate.isEmpty())
            {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                curDate =  month + "/" + day + "/" + year;
                Log.d("Current Date",curDate);
            }

                    if (completedStudySessionsString != null && !completedStudySessionsString.isEmpty()) {
                        //if(Integer.parseInt(completedStudySessionsString) <= Integer.parseInt(targetedWeeklySessions.getText().toString())) {
                        databaseConnector.manageStudySessions(courseIDString, curDate, Integer.parseInt(completedStudySessionsString));
                        DecimalFormat f = new DecimalFormat("00.00");
                        wtdSessionsView = (TextView) buttonTableRow.findViewById(R.id.txtWTDSessions);
                        wtdSessionsView.setText(f.format(calculateWeeklySessionsCompleted(courseIDString, Integer.parseInt(targetedWeeklySessions.getText().toString())) * 100) + " %");

                        ttdSessionsView = (TextView) buttonTableRow.findViewById(R.id.txtTTD);
                        ttdSessionsView.setText(f.format(calculateTermSessionsCompleted(courseIDString, Integer.parseInt(targetedWeeklySessions.getText().toString())) * 100) + " %");
                   /* } else {
                            // create a new AlertDialog Builder
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(MainActivity6.this);

                            // set dialog title & message, and provide Button to dismiss
                            builder.setTitle("Error");
                            builder.setMessage("Enter valid study sessions and then save.");
                            builder.setPositiveButton(R.string.errorButton, null);
                            builder.show();
                    }*/
            }
            else
            {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity6.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle("Error");
                builder.setMessage("Enter number of sessions and then save");
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show();
            }
        } // end method onClick
    };



    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendarView2);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Sunday as the first day of the Calendar
        calendar.setFirstDayOfWeek(1);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.grey));

        //sets the color for the separator line between weeks.
       // calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        extras = getIntent().getExtras(); // get Bundle of extras
        String courseIDValue = null;

        if (extras != null && extras.getLong("SelectedDate") != 0.0)
        {
            long sDate = extras.getLong("SelectedDate");
            curDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(sDate));

            calendar.setDate(sDate);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            Date dt1 = new Date();
            try
            {
                dt1=format1.parse(curDate);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            c1.setTime(dt1);
            currentDate = dt1;

            //These lines of code is to format date accordingly so that while saving in the Database it will saved correctly
            int day = c1.get(Calendar.DAY_OF_MONTH);
            int month = c1.get(Calendar.MONTH);
            int year = c1.get(Calendar.YEAR);
            curDate =  month+1 + "/" + day + "/" + year;


            selectedWeekDay = Integer.toString(c1.get(Calendar.DAY_OF_WEEK));
            populateSelectedDateData();
        }


        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), month+1 + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
                curDate = month+1 + "/" + day + "/" + year;//month+1 because month value start from 0 [0-11]
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
                Date dt1 = new Date();
                try
                {
                    dt1=format1.parse(curDate);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                c1.setTime(dt1);
                currentDate = dt1;
                selectedWeekDay = Integer.toString(c1.get(Calendar.DAY_OF_WEEK));
                populateSelectedDateData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.help:
                sessionsGuideLinesViewMethod();
                break;
        }
    }

    private  void sessionsGuideLinesViewMethod(){
        startActivity(new Intent("com.teamtreehouse.oslist.StudySessionsGuideLines"));
    }

    private void populateSelectedDateData()
    {
        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity6.this);
        databaseConnector.open();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();

        //get a cursor for the classes
        if(selectedWeekDay == null || selectedWeekDay.isEmpty())
        {
            Calendar cal = Calendar.getInstance();
            selectedWeekDay = Integer.toString(cal.get(Calendar.DAY_OF_WEEK));
            //Log.d("Weekday1:",selectedWeekDay);
        }
        if(curDate == null || curDate.isEmpty() || currentDate == null)
        {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            curDate =  month+1 + "/" + day + "/" + year;
            //Log.d("Current Date",curDate);

            Calendar c1 = Calendar.getInstance();
            try
            {
                dt1=format1.parse(curDate);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            currentDate = dt1;
        }
        //Cursor c = databaseConnector.getClassesforSelectedDate(weekDays.get(selectedWeekDay));
        Cursor c = databaseConnector.getAllClasses();
        int index = c.getCount();
        c.moveToFirst();
        Log.d("Index:", "" + index);
        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
        queryTableLayout.removeAllViews();
        for (int i=0;i<index;i++)
        {
            int semesterStartDateIndex = c.getColumnIndex("StartDate");
            int semesterEndDateIndex = c.getColumnIndex("EndDate");
            int courseIDIndex = c.getColumnIndex("CourseID");
            String completedSessionsforSelectedDate = "";

            try
            {
                Cursor c1 = databaseConnector.getStudySessions(c.getString(courseIDIndex));
                c1.moveToFirst();
                for(int j =0;j<c1.getCount();j++)
                {
                    int dateFromDBIndex = c1.getColumnIndex("Date");
                    int completedSessionsIndex = c1.getColumnIndex("CompletedSessions");
                    if(currentDate.equals(format1.parse(c1.getString(dateFromDBIndex)))) {
                        completedSessionsforSelectedDate = c1.getString(completedSessionsIndex);
                    }
                    c1.moveToNext();
                }

                if((currentDate.after(format1.parse(c.getString(semesterStartDateIndex))) || currentDate.equals(format1.parse(c.getString(semesterStartDateIndex)))) && (currentDate.before(format1.parse(c.getString(semesterEndDateIndex))) || currentDate.equals(format1.parse(c.getString(semesterEndDateIndex)))))
                {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

                    // inflate new_tag_view.xml to create new tag and edit Buttons
                    View newTagView = inflater.inflate(R.layout.activity_study_sessions_sub_layout, null);

                    queryTableLayout.addView(newTagView, 0);
                    classNameView = (TextView) findViewById(R.id.className);
                    courseIDView = (TextView) findViewById(R.id.courseNumber);
                    textTargetStudySessions = (TextView) findViewById(R.id.txtTargetSessions);
                    wtdSessionsView = (TextView) findViewById(R.id.txtWTDSessions);
                    ttdSessionsView = (TextView) findViewById(R.id.txtTTD);

                    txtCompletedSessions = (TextView) findViewById(R.id.txtCompletedSessions);


                    // get the column index for each data item
                    int classNameIndex = c.getColumnIndex("ClassName");
                    int courseIdIndex = c.getColumnIndex("CourseID");
                    int targetStudySessionsIndex = c.getColumnIndex("NumberOfStudySessions");


                    // fill TextViews with the retrieved data
                    classNameView.setText(c.getString(classNameIndex));
                    courseIDView.setText(c.getString(courseIdIndex));
                    textTargetStudySessions.setText(c.getString(targetStudySessionsIndex));
                    txtCompletedSessions.setText(completedSessionsforSelectedDate);

                    DecimalFormat f = new DecimalFormat("00.00");

                    wtdSessionsView.setText(f.format(calculateWeeklySessionsCompleted(c.getString(courseIdIndex),Integer.parseInt(c.getString(targetStudySessionsIndex)))*100) + " %");
                    ttdSessionsView.setText(f.format(calculateTermSessionsCompleted(c.getString(courseIdIndex),Integer.parseInt(c.getString(targetStudySessionsIndex)))*100) + " %");


                    buttonHelp = (ImageButton)findViewById(R.id.help);
                    buttonHelp.setOnClickListener(this);


                    Button newEditButton =  (Button) newTagView.findViewById(R.id.editSessions);
                    newEditButton.setOnClickListener(editButtonListener);


                    Button newSaveButton =  (Button) newTagView.findViewById(R.id.save);
                    newSaveButton.setOnClickListener(saveButtonListener);
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            c.moveToNext();
        }
        if(index < 1)
        {
            queryTableLayout.removeAllViews();
        }
    }


    public View.OnClickListener editButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            Intent addNewClass =
                    new Intent(MainActivity6.this, MainActivity3.class);
            RelativeLayout buttonTableRow = (RelativeLayout) v.getParent().getParent().getParent();
            TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumber);
            String courseIDString = getCourseID.getText().toString();
            //System.out.println(getCourseID.getText().toString());



            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("CourseIDString", courseIDString);

            addNewClass.putExtras(bundle);
            startActivity(addNewClass); // start the AddEditContact Activity



            // set EditTexts to match the chosen tag and query
            //tagEditText.setText(tag);
            //queryEditText.setText(savedSearches.getString(tag, ""));
        } // end method onClick
    }; // end OnClickListener anonymous inner class


    public double calculateWeeklySessionsCompleted(String courseID,int targetedWeeklySessions)
    {
        Log.d("WeeklySessionPercentage","Calculating Sessions Percentage");
        Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);

        c1.setTime(currentDate);
        weekStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);

        c1.setTime(currentDate);
        weekEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);


        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity6.this);
        databaseConnector.open();

        int weeklySessionsCompleted = 0;

        //get a cursor for the sessions
        Cursor c = databaseConnector.getStudySessions(courseID);
        c.moveToFirst();

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();

        for (int i=0;i<c.getCount();i++)
        {
            int dateIndex = c.getColumnIndex("Date");

            try
            {
                dt1=format1.parse(c.getString(dateIndex));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if((dt1.after(weekStartDate) || dt1.equals(weekStartDate)) && (dt1.before(weekEndDate) || dt1.equals(weekEndDate)))
            {
                int completedSessionsIndex = c.getColumnIndex("CompletedSessions");
                int completedSessions =  c.getInt(completedSessionsIndex);
                weeklySessionsCompleted = weeklySessionsCompleted + completedSessions;
            }
            c.moveToNext();
        }
        double weeklySessionsPercentage = 0.0;
        if (targetedWeeklySessions != 0)
            weeklySessionsPercentage = (double)weeklySessionsCompleted/targetedWeeklySessions;

        return weeklySessionsPercentage;
    }

    public double calculateTermSessionsCompleted(String courseID,int targetedWeeklySessions)
    {
        Log.d("TermSessionsPercentage","Calculating Sessions Term Percentage");
        Date semesterStartDate = new Date();
        Date semesterEndDate = new Date();
        Date todaysDate = new Date();
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity6.this);
        databaseConnector.open();

        //get a cursor for the classes
        Cursor c = databaseConnector.getOneClass(courseID);

        c.moveToFirst();
        int startDateIndex = c.getColumnIndex("StartDate");
        int endDateIndex = c.getColumnIndex("EndDate");

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        try
        {
            dt1=format1.parse(c.getString(startDateIndex));
            c1.setTime(dt1);
            semesterStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);

            dt1=format1.parse(c.getString(endDateIndex));
            c1.setTime(dt1);
            semesterEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);

            dt1=format1.parse(curDate);
            c1.setTime(dt1);
            todaysDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        int numberofWeeks = getWeeksBetweenDates(semesterStartDate,todaysDate);//getWeeksBetweenDates(semesterStartDate,semesterEndDate);
        int totalSessionsinTerm = 0;
        int termSessionsCompleted = 0;
        totalSessionsinTerm = numberofWeeks * targetedWeeklySessions;

        //get a cursor for the sessions
        c = databaseConnector.getStudySessions(courseID);
        c.moveToFirst();

        for (int i=0;i<c.getCount();i++)
        {
            int dateIndex = c.getColumnIndex("Date");

            try
            {
                dt1=format1.parse(c.getString(dateIndex));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if((dt1.after(semesterStartDate) || dt1.equals(semesterStartDate)) && (dt1.before(todaysDate) || dt1.equals(todaysDate)))
            {
                int completedSessionsIndex = c.getColumnIndex("CompletedSessions");
                int completedSessions =  c.getInt(completedSessionsIndex);
                termSessionsCompleted = termSessionsCompleted + completedSessions;
            }
            c.moveToNext();
        }

        double termSessionsPercentage = 0.0;
        if (totalSessionsinTerm != 0)
            termSessionsPercentage = (double)termSessionsCompleted/totalSessionsinTerm;


        return termSessionsPercentage;
    }



    private Date getStartDateFromWeekDay(String weekDay,Date curDate)
    {
        Date weekStartDate = new Date();
        //Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(curDate);

        switch(weekDay)
        {
            case "1":
                weekStartDate = curDate;
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "2":
                c1.add(Calendar.DAY_OF_YEAR, -1);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "3":
                c1.add(Calendar.DAY_OF_YEAR, -2);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "4":
                c1.add(Calendar.DAY_OF_YEAR, -3);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "5":
                c1.add(Calendar.DAY_OF_YEAR, -4);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "6":
                c1.add(Calendar.DAY_OF_YEAR, -5);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            case "7":
                c1.add(Calendar.DAY_OF_YEAR, -6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //Log.d("StartDate:",weekStartDate+"");
                break;
            default:
        }
        return  weekStartDate;
    }

    private Date getEndDateFromWeekDay(String weekDay,Date curDate)
    {
        Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(curDate);
        switch(weekDay)
        {
            case "1":
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "2":
                c1.add(Calendar.DAY_OF_YEAR, -1);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "3":
                c1.add(Calendar.DAY_OF_YEAR, -2);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "4":
                c1.add(Calendar.DAY_OF_YEAR, -3);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "5":
                c1.add(Calendar.DAY_OF_YEAR, -4);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "6":
                c1.add(Calendar.DAY_OF_YEAR, -5);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "7":
                c1.add(Calendar.DAY_OF_YEAR, -6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = curDate;
                //Log.d("EndDate:",weekEndDate+"");
                break;
            default:
        }
        return weekEndDate;
    }

    public int getWeeksBetweenDates(Date dateOne, Date dateTwo)
    {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        int rest = (int)delta % 365;
        int weeks = (int)Math.ceil(rest / 7d);
        Log.d("Weeks:",weeks+"");
        return weeks;
    }

    private void populateHashMap()
    {
        weekDays.put("1","Sunday");
        weekDays.put("2","Monday");
        weekDays.put("3","Tuesday");
        weekDays.put("4","Wednesday");
        weekDays.put("5","Thursday");
        weekDays.put("6","Friday");
        weekDays.put("7","Saturday");
    }

    //Obsolete method - Could be used to check whether study sessions are completed for the current week before saving in the database.
    private boolean isWeeklySessionsCompleted(String courseID, int targetedWeeklySessions)
    {
        Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);

        c1.setTime(currentDate);
        weekStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);

        c1.setTime(currentDate);
        weekEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);


        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity6.this);
        databaseConnector.open();

        int weeklySessionsCompleted = 0;

        //get a cursor for the sessions
        Cursor c = databaseConnector.getStudySessions(courseID);
        c.moveToFirst();

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();

        for (int i=0;i<c.getCount();i++)
        {
            int dateIndex = c.getColumnIndex("Date");

            try
            {
                dt1=format1.parse(c.getString(dateIndex));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if((dt1.after(weekStartDate) || dt1.equals(weekStartDate)) && (dt1.before(weekEndDate) || dt1.equals(weekEndDate)))
            {
                int completedSessionsIndex = c.getColumnIndex("CompletedSessions");
                int completedSessions =  c.getInt(completedSessionsIndex);
                weeklySessionsCompleted = weeklySessionsCompleted + completedSessions;
            }
            c.moveToNext();
        }
        if(weeklySessionsCompleted >= targetedWeeklySessions) {
            System.out.println("True Condition");
            return true;
        }
        else {
            System.out.println("False Condition");
            return false;
        }
    }
}
