package com.teamtreehouse.oslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity5 extends ActionBarActivity implements View.OnClickListener{

    Bundle extras;
    CalendarView calendar;
    private int dayOfWeek;
    private TableLayout queryTableLayout;
    TextView classNameView;
    TextView courseIDView;
    TextView classTimeView;
    TextView instructorView;
    TextView teachingAssistantView;
    TextView classLocationView;
    TextView wtdAttendanceView;
    TextView ttdAttendanceView;
    TextView descriptionView;
    CheckBox isAttendedCheckBox;
    CheckBox isCancelledCheckBox;

    String selectedWeekDay;
    String curDate;
    Date currentDate;
    HashMap<String,String> weekDays = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5);

        final Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //System.out.println("Calendar" + " This " + dayOfWeek);

        populateHashMap();
        initializeCalendar();//initializes the calendarview


    }


    @Override
    protected void onResume()
    {
        super.onResume();
        populateSelectedDateData();

    }


    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendarView1);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Sunday as the first day of the Calendar
        calendar.setFirstDayOfWeek(1);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.grey));

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
              //  Toast.makeText(getApplicationContext(), month+1 + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
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


    public void onClick(View v) {
    }

    public View.OnClickListener onClassAttendedChanged = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Boolean isAttended = false;
            Boolean isCancelled = false;

           // RelativeLayout buttonTableRow = (RelativeLayout) v.getParent().getParent().getParent().getParent().getParent().getParent();

            RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
            System.out.println();

            TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumber);

            String courseIDString = getCourseID.getText().toString();



            CheckBox attendedCheckBox = (CheckBox)buttonTableRow.findViewById(R.id.attendedCheckBox);
            isAttended = attendedCheckBox.isChecked();
            CheckBox cancelledCheckBox = (CheckBox)buttonTableRow.findViewById(R.id.cancelledCheckBox);
            isCancelled = cancelledCheckBox.isChecked();

            if(isAttended) {
                cancelledCheckBox.setChecked(false);
                isCancelled = false;
            }


            DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity5.this);
            databaseConnector.open();

            if(curDate == null || curDate.isEmpty())
            {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                curDate =  month+1 + "/" + day + "/" + year;
                //Log.d("Current Date",curDate);
            }

            //System.out.println("after if attended");
            DecimalFormat f = new DecimalFormat("00.00");
            databaseConnector.manageClassAttendance(courseIDString,curDate,isAttended,isCancelled);
            wtdAttendanceView = (TextView) buttonTableRow.findViewById(R.id.wtdText);
            wtdAttendanceView.setText(f.format(calculateWeeklyAttendance(courseIDString)*100) + " %");
            ttdAttendanceView = (TextView) buttonTableRow.findViewById(R.id.ttdText);
            ttdAttendanceView.setText(f.format(calculateTermAttendance(courseIDString)*100) + " %");
        }
    };

    public View.OnClickListener onClassCancelledChanged = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

            System.out.println("Inside cancelled");
            Boolean isAttended = false;
            Boolean isCancelled = false;
            //RelativeLayout buttonTableRow = (RelativeLayout) v.getParent().getParent().getParent().getParent().getParent().getParent();

            RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
            TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumber);
            String courseIDString = getCourseID.getText().toString();


            CheckBox attendedCheckBox = (CheckBox)buttonTableRow.findViewById(R.id.attendedCheckBox);
            isAttended = attendedCheckBox.isChecked();
            CheckBox cancelledCheckBox = (CheckBox)buttonTableRow.findViewById(R.id.cancelledCheckBox);
            isCancelled = cancelledCheckBox.isChecked();

            if(isCancelled) {
                attendedCheckBox.setChecked(false);
                isAttended = false;
            }

            DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity5.this);
            databaseConnector.open();

            if(curDate == null || curDate.isEmpty())
            {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                curDate =  month+1 + "/" + day + "/" + year;
                //Log.d("Current Date",curDate);
            }
            databaseConnector.manageClassAttendance(courseIDString,curDate,isAttended,isCancelled);
            wtdAttendanceView = (TextView) buttonTableRow.findViewById(R.id.wtdText);
            DecimalFormat f = new DecimalFormat("00.00");
            wtdAttendanceView.setText(f.format(calculateWeeklyAttendance(courseIDString)*100) + " %");
            ttdAttendanceView = (TextView) buttonTableRow.findViewById(R.id.ttdText);
            ttdAttendanceView.setText(f.format(calculateTermAttendance(courseIDString)*100) + " %");
        }
    };

    private void populateSelectedDateData()
    {
        System.out.println("populate date method");
        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity5.this);
        databaseConnector.open();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        int classesCount = 0;

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
        //get a cursor for the classes

        Cursor c = databaseConnector.getClassesforSelectedDate(weekDays.get(selectedWeekDay));

        int index = c.getCount();
        c.moveToFirst();
        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
        queryTableLayout.removeAllViews();
        for (int i=0;i<index;i++)
        {

            System.out.println("populate date inside loop");
            int semesterStartDateIndex = c.getColumnIndex("StartDate");
            int semesterEndDateIndex = c.getColumnIndex("EndDate");

            try
            {
                if ((currentDate.after(format1.parse(c.getString(semesterStartDateIndex))) || currentDate.equals(format1.parse(c.getString(semesterStartDateIndex)))) && (currentDate.before(format1.parse(c.getString(semesterEndDateIndex))) || currentDate.equals(format1.parse(c.getString(semesterEndDateIndex)))))
                {
                    //System.out.println("populate date inside if ");
                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

                    // inflate new_tag_view.xml to create new tag and edit Buttons

                    View newTagView = inflater.inflate(R.layout.activity_attendance_sub_layout, null);
                    queryTableLayout.addView(newTagView, classesCount++);
                    classNameView = (TextView) newTagView.findViewById(R.id.className);
                    courseIDView = (TextView) newTagView.findViewById(R.id.courseNumber);
                    classTimeView = (TextView) newTagView.findViewById(R.id.time);
                    instructorView = (TextView) newTagView.findViewById(R.id.instructor);
                    teachingAssistantView = (TextView) newTagView.findViewById(R.id.ta);
                    classLocationView = (TextView) newTagView.findViewById(R.id.location);
                    wtdAttendanceView = (TextView) newTagView.findViewById(R.id.wtdText);
                    ttdAttendanceView = (TextView) newTagView.findViewById(R.id.ttdText);
                    isAttendedCheckBox = (CheckBox) newTagView.findViewById(R.id.attendedCheckBox);
                    isCancelledCheckBox = (CheckBox) newTagView.findViewById(R.id.cancelledCheckBox);

                    RelativeLayout relativeLayoutInstructor = (RelativeLayout)newTagView.findViewById(R.id.relativelayout4);
                    RelativeLayout relativeLayoutTA = (RelativeLayout)newTagView.findViewById(R.id.relativelayout5);
                    relativeLayoutInstructor.setVisibility(View.GONE);
                    relativeLayoutTA.setVisibility(View.GONE);

                    // get the column index for each data item
                    int classNameIndex = c.getColumnIndex("ClassName");
                    int courseIdIndex = c.getColumnIndex("CourseID");
                    int startTimeIndex = c.getColumnIndex("StartTime");
                    int endTimeIndex = c.getColumnIndex("EndTime");
                    int instructorIndex = c.getColumnIndex("Instructor");
                    int teachingAssistantIndex = c.getColumnIndex("TeachingAssistant");
                    int classLocationIndex = c.getColumnIndex("ClassLocation");


                    // fill TextViews with the retrieved data
                    classNameView.setText(c.getString(classNameIndex));
                    courseIDView.setText(c.getString(courseIdIndex));
                    classTimeView.setText(c.getString(startTimeIndex) + " - " + c.getString(endTimeIndex));
                    if(!(c.getString(instructorIndex) == null || c.getString(instructorIndex).isEmpty())) {
                        instructorView.setText(c.getString(instructorIndex));
                        relativeLayoutInstructor.setVisibility(View.VISIBLE);
                    }
                    if(!(c.getString(teachingAssistantIndex) == null || c.getString(teachingAssistantIndex).isEmpty())) {
                        teachingAssistantView.setText(c.getString(teachingAssistantIndex));
                        relativeLayoutTA.setVisibility(View.VISIBLE);
                    }
                    classLocationView.setText(c.getString(classLocationIndex));

                    Cursor c1 = databaseConnector.getAttendanceByDate(c.getString(courseIdIndex),curDate);
                    c1.moveToFirst();
                    if(c1.getCount()==1)
                    {
                        isAttendedCheckBox.setChecked(c1.getInt(c1.getColumnIndex("IsAttended")) > 0);
                        isCancelledCheckBox.setChecked(c1.getInt(c1.getColumnIndex("IsCancelled")) > 0);
                    }

                    DecimalFormat f = new DecimalFormat("00.00");

                    wtdAttendanceView.setText(f.format(calculateWeeklyAttendance(c.getString(courseIdIndex))*100) + " %");
                    ttdAttendanceView.setText(f.format(calculateTermAttendance(c.getString(courseIdIndex))*100) + " %");



                    CheckBox attendedCheckBox = (CheckBox)newTagView.findViewById(R.id.attendedCheckBox);
                    attendedCheckBox.setOnClickListener(onClassAttendedChanged);

                    CheckBox cancelledCheckBox = (CheckBox)newTagView.findViewById(R.id.cancelledCheckBox);
                    cancelledCheckBox.setOnClickListener(onClassCancelledChanged);

                    Button newEditButton =  (Button) newTagView.findViewById(R.id.edit1);
                    newEditButton.setOnClickListener(editButtonListener);
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
         //   System.out.print(v.getId());
            Intent addNewClass = new Intent(MainActivity5.this, MainActivity3.class);
            RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
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

    private double calculateWeeklyAttendance(String courseID)
    {
        Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        //Log.d("BeforeSwitch",currentDate+"");

        c1.setTime(currentDate);
        weekStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);
        startCal.setTime(weekStartDate);

        c1.setTime(currentDate);
        weekEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),currentDate);
        endCal.setTime(weekEndDate);


        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();


        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity5.this);
        databaseConnector.open();

        Cursor semDateCursor = databaseConnector.getOneClass(courseID);
        semDateCursor.moveToFirst();
        try
        {
            //to check if semester end date is not before the end date of the week
            dt1 = format1.parse(semDateCursor.getString(semDateCursor.getColumnIndex("EndDate")));
            if (dt1.before(weekEndDate))
                endCal.setTime(dt1);
            //to check if semester start date is not after the start date of the week
            dt1 = format1.parse(semDateCursor.getString(semDateCursor.getColumnIndex("StartDate")));
            if(dt1.after(weekStartDate))
                startCal.setTime(dt1);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        //String courseID = "";
        int totalWeeklyClass = 0;
        int classAttendedCount = 0;
        int counter = 0;

        //get a cursor for the classes
        Cursor c = databaseConnector.getClassSchedules(courseID);
        totalWeeklyClass = c.getCount();
        //classAttendedCount = totalWeeklyClass;
        c.moveToFirst();
        totalWeeklyClass = 0;
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        List<String> daysList = new ArrayList<String>();
        for (int i=0;i<c.getCount();i++)
        {
            int dayIndex = c.getColumnIndex("Day");
            daysList.add(c.getString(dayIndex).toLowerCase());
            c.moveToNext();
        }
        do
        {
            String calWeekVal = startCal.get(Calendar.DAY_OF_WEEK) + "";
            if(daysList.contains(weekDays.get(calWeekVal).toLowerCase()))
            {
                totalWeeklyClass++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }while(startCal.getTimeInMillis() <= endCal.getTimeInMillis());
        //Log.d("After do-while",totalWeeklyClass+"");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c = databaseConnector.getAttendance(courseID);
        int attendanceCountDB = c.getCount();
        c.moveToFirst();

        //Log.d("Attnd",classAttendedCount+"");
        for (int i=0;i<attendanceCountDB;i++)
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
                counter = counter + 1;
                int isAttendedIndex = c.getColumnIndex("IsAttended");
                int isCancelledIndex = c.getColumnIndex("IsCancelled");
                boolean isAttended = c.getInt(isAttendedIndex)>0;
                boolean isCancelled = c.getInt(isCancelledIndex)>0;
                /*if(!isAttended || isCancelled)
                    classAttendedCount = classAttendedCount - 1;
                if(isCancelled) {
                    totalWeeklyClass = totalWeeklyClass - 1;
                }*/
                if(isAttended)
                    classAttendedCount = classAttendedCount + 1;
                if(isCancelled)
                    totalWeeklyClass = totalWeeklyClass - 1;
                //Log.d("Attnd2",classAttendedCount+"");
                //Log.d("Weekly1",totalWeeklyClass+"");
            }
            c.moveToNext();
        }

        //Log.d("Attnd3",classAttendedCount+"");
        //Log.d("Weekly2",totalWeeklyClass+"");
        double weeklyPercentage = 0.0;
        if(totalWeeklyClass != 0)
            weeklyPercentage = (double)classAttendedCount/totalWeeklyClass;
        else if(totalWeeklyClass == 0 && classAttendedCount == 0)//when there is only one class in a week and that is either cancelled or holiday
            weeklyPercentage = 1.0;

        return weeklyPercentage;
    }
    private double calculateTermAttendance(String courseID)
    {
        Date semesterStartDate = new Date();
        Date semesterEndDate = new Date();
        Date todaysDate = new Date();
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity5.this);
        databaseConnector.open();

        //get a cursor for the classes
        Cursor c = databaseConnector.getOneClass(courseID);
        int index = c.getCount();
        c.moveToFirst();
        int startDateIndex = c.getColumnIndex("StartDate");
        int endDateIndex = c.getColumnIndex("EndDate");
        if(curDate == null || curDate.isEmpty())
        {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            curDate =  month+1 + "/" + day + "/" + year;
            //Log.d("Current Date",curDate);
        }


        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        try
        {
            dt1=format1.parse(c.getString(startDateIndex));
            c1.setTime(dt1);
            startCal.setTime(dt1);
            //Log.d("StartCal",dt1+"");
            semesterStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);

            /*dt1=format1.parse(c.getString(endDateIndex));
            c1.setTime(dt1);
            semesterEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);*/

            dt1=format1.parse(curDate);
            c1.setTime(dt1);
            endCal.setTime(dt1);
            //Log.d("EndCal",dt1+"");
            //todaysDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);
            todaysDate = dt1;

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        c = databaseConnector.getClassSchedules(courseID);
        int numberofWeeklyClasses = c.getCount();


        //int numberofClassesPerTerm = numberofWeeks * numberofWeeklyClasses;
        int totalClassestillToday = getWeeksBetweenDates(semesterStartDate,todaysDate) * numberofWeeklyClasses;
        totalClassestillToday = 0;
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        List<String> daysList = new ArrayList<String>();
        c.moveToFirst();
        for (int i=0;i<numberofWeeklyClasses;i++)
        {
            int dayIndex = c.getColumnIndex("Day");
            daysList.add(c.getString(dayIndex).toLowerCase());
            c.moveToNext();
        }
        do
        {
            String calWeekVal = startCal.get(Calendar.DAY_OF_WEEK) + "";
            if(daysList.contains(weekDays.get(calWeekVal).toLowerCase()))
            {
                totalClassestillToday++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }while(startCal.getTimeInMillis() <= endCal.getTimeInMillis());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        int counter = 0;
        int classAttendedCount = 0;
        //classAttendedCount = totalClassestillToday;

        //int pendingClasses = numberofClassesPerTerm - totalClassestillToday;
        c = databaseConnector.getAttendance(courseID);
        int attendanceCountDB = c.getCount();
        c.moveToFirst();

        for (int i=0;i<attendanceCountDB;i++)
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
                counter = counter + 1;
                int isAttendedIndex = c.getColumnIndex("IsAttended");
                int isCancelledIndex = c.getColumnIndex("IsCancelled");
                boolean isAttended = c.getInt(isAttendedIndex)>0;
                boolean isCancelled = c.getInt(isCancelledIndex)>0;
                /*if(!isAttended && !isCancelled)
                    classAttendedCount = classAttendedCount - 1;*/
                if(isAttended)
                    classAttendedCount = classAttendedCount + 1;
                if(isCancelled)
                    totalClassestillToday = totalClassestillToday - 1;
                //numberofClassesPerTerm = numberofClassesPerTerm - 1;
                //Log.d("Attnd2",classAttendedCount+"");
                //Log.d("Term1",totalClassestillToday+"");
            }
            c.moveToNext();
        }

        double termPercentage = 0.0;
        if(totalClassestillToday != 0)
            termPercentage = (double)classAttendedCount/totalClassestillToday;

        return termPercentage;
    }


    public int getWeeksBetweenDates(Date dateOne, Date dateTwo)
    {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;


        int rest = (int)delta % 365;
        int weeks = (int)Math.ceil(rest / 7d);

        return weeks;
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
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "2":
                c1.add(Calendar.DAY_OF_YEAR, -1);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //c1.add(Calendar.DAY_OF_YEAR, 6);
                //tempNewDate = c1.getTime();
                //c1.setTime(tempNewDate);
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "3":
                c1.add(Calendar.DAY_OF_YEAR, -2);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //c1.add(Calendar.DAY_OF_YEAR, 6);
                //tempNewDate = c1.getTime();
                //c1.setTime(tempNewDate);
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "4":
                c1.add(Calendar.DAY_OF_YEAR, -3);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //c1.add(Calendar.DAY_OF_YEAR, 6);
                //tempNewDate = c1.getTime();
                //c1.setTime(tempNewDate);
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "5":
                c1.add(Calendar.DAY_OF_YEAR, -4);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //c1.add(Calendar.DAY_OF_YEAR, 6);
                //tempNewDate = c1.getTime();
                //c1.setTime(tempNewDate);
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "6":
                c1.add(Calendar.DAY_OF_YEAR, -5);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //c1.add(Calendar.DAY_OF_YEAR, 6);
                //tempNewDate = c1.getTime();
                //c1.setTime(tempNewDate);
                //weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            case "7":
                c1.add(Calendar.DAY_OF_YEAR, -6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //weekEndDate = currentDate;
                //weekEndDate = curDate;
                    //Log.d("StartDate:",weekStartDate+"");
                //Log.d("EndDate:",weekEndDate+"");
                break;
            default:
        }
        return  weekStartDate;
    }

    private Date getEndDateFromWeekDay(String weekDay,Date curDate)
    {
        //Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(curDate);
        switch(weekDay)
        {
            case "1":
                //weekStartDate = currentDate;
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
                    //Log.d("EndDate:",weekEndDate+"");
                break;
            case "2":
                c1.add(Calendar.DAY_OF_YEAR, -1);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
                    //Log.d("EndDate:",weekEndDate+"");
                break;
            case "3":
                c1.add(Calendar.DAY_OF_YEAR, -2);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
                    //Log.d("EndDate:",weekEndDate+"");
                break;
            case "4":
                c1.add(Calendar.DAY_OF_YEAR, -3);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
                    //Log.d("EndDate:",weekEndDate+"");
                break;
            case "5":
                c1.add(Calendar.DAY_OF_YEAR, -4);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
                    //Log.d("EndDate:",weekEndDate+"");
                break;
            case "6":
                c1.add(Calendar.DAY_OF_YEAR, -5);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                //weekStartDate = tempNewDate;
                //weekStartDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 6);
                tempNewDate = c1.getTime();
                c1.setTime(tempNewDate);
                weekEndDate = tempNewDate;
                //weekEndDate = c1.get(Calendar.MONTH)+1 + "/" + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.YEAR);
                //Log.d("StartDate:",weekStartDate+"");
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
}
