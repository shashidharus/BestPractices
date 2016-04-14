package com.teamtreehouse.oslist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity1 extends MainActivity {

    private TableLayout queryTableLayout; // shows the search buttons
    private SharedPreferences savedPreferences;
    TextView classNameView;
    TextView courseIDView;
    TextView instructorView;
    TextView teachingAssistantView;
    TextView studySessionsView;
    TextView termAttendanceRateView;
    TextView studySessionRateView;
    TextView termStudySessionRateView;
    Button deleteClass;
    TextView startDateView;
    TextView endDateView;

    TextView attendanceRateView;
    Button mondayButton;
    Button tuesdayButton;
    Button wednesdayButton;
    Button thursdayButton;
    Button fridayButton;
    Button saturdayButton;
    Button sundayButton;
    String curDate = null;
    Date currentDate;
    HashMap<String, String> weekDays = new HashMap<String, String>();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity1);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("All Classes");
        populateHashMap();
        editMode();
    }

    public void editMode() {
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity1.this);

        // databaseConnector.insertWeekDays();
        databaseConnector.open();
        // get a cursor containing call contacts
        Cursor c = databaseConnector.getAllClasses();
        int index = c.getCount();

        deleteClass = (Button) findViewById(R.id.deleteClasses);
        c.moveToFirst();    // move to the first item
        int classNameIndex = c.getColumnIndex("ClassName");
        int courseIdIndex = c.getColumnIndex("CourseID");
        int instructorIndex = c.getColumnIndex("Instructor");
        int teachingAssistantIndex = c.getColumnIndex("TeachingAssistant");
        int weeklyStudySessionsIndex = c.getColumnIndex("NumberOfStudySessions");
        int startDateIndex = c.getColumnIndex("StartDate");
        int endDateIndex = c.getColumnIndex("EndDate");
        int activeClassIndex = 0;
        int completedClassIndex = 0;
        boolean flag = false;
        boolean activeFlag = false;
        int completedCount = 0;
        int activeCount = 0;

        // int cityIndex = result.getColumnIndex("city");
        queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
        if (curDate == null || curDate.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            curDate = month + 1 + "/" + day + "/" + year;
            Log.d("Current Date", curDate);
        }
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        try {
            dt1 = format1.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c1.setTime(dt1);
        currentDate = dt1;


        int activeClassCount = 0;
        for (int i = 0; i < index; i++) {

            try {
                if (!activeFlag) {
                    activeFlag = !activeFlag;

                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    View newTagView = inflater.inflate(R.layout.active_classes_layout, null);
                    //queryTableLayout.addView(newTagView, activeClassIndex++);
                }
                if (((currentDate.after(format1.parse(c.getString(startDateIndex))) ||
                        currentDate.equals(format1.parse(c.getString(startDateIndex)))) &&
                        (currentDate.before(format1.parse(c.getString(endDateIndex))) ||
                                currentDate.equals(format1.parse(c.getString(endDateIndex))))) || currentDate.before(format1.parse(c.getString(startDateIndex)))) {

                    System.out.println("If Condition satisfied");
                    System.out.println(activeClassIndex);
                    System.out.println(currentDate.after(format1.parse(c.getString(startDateIndex))) ||
                            currentDate.equals(format1.parse(c.getString(startDateIndex))));
                    System.out.println(currentDate.before(format1.parse(c.getString(endDateIndex))) ||
                            currentDate.equals(format1.parse(c.getString(endDateIndex))));
                    System.out.println(currentDate);
                    System.out.println(format1.parse(c.getString(startDateIndex)));
                    System.out.println(format1.parse(c.getString(endDateIndex)));

                    // cityTextView.setText(result.getString(cityIndex));
                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    String courseIDValue = c.getString(courseIdIndex);
                    // inflate new_tag_view.xml to create new tag and edit Buttons

                    View newTagView = inflater.inflate(R.layout.activity_main_activity2, null);
                    queryTableLayout.addView(newTagView, activeClassIndex++);

                    if (activeClassCount == 0) {
                        TextView activeClasses = (TextView) newTagView.findViewById(R.id.activeClasses);
                        activeClasses.setVisibility(View.VISIBLE);
                        activeClassCount++;
                    }
                    //completedClassIndex = activeClassIndex;
                    classNameView = (TextView) newTagView.findViewById(R.id.classNameView);
                    courseIDView = (TextView) newTagView.findViewById(R.id.courseNumberView);
                    instructorView = (TextView) newTagView.findViewById(R.id.instructorView);
                    teachingAssistantView = (TextView) newTagView.findViewById(R.id.teachingAssistantView);
                    studySessionsView = (TextView) newTagView.findViewById(R.id.targetSessionsView);

                    attendanceRateView = (TextView) newTagView.findViewById(R.id.attendanceRateView);
                    termAttendanceRateView = (TextView) newTagView.findViewById(R.id.termAttendanceRateView);
                    studySessionRateView = (TextView) newTagView.findViewById(R.id.targetWeekSessionsView);
                    termStudySessionRateView = (TextView) newTagView.findViewById(R.id.targetTermSessionsView);


                    startDateView = (TextView) newTagView.findViewById(R.id.startDateView);
                    endDateView = (TextView) newTagView.findViewById(R.id.endDateView);

                    mondayButton = (Button) newTagView.findViewById(R.id.monday);
                    tuesdayButton = (Button) newTagView.findViewById(R.id.tuesday);
                    wednesdayButton = (Button) newTagView.findViewById(R.id.wednesday);
                    thursdayButton = (Button) newTagView.findViewById(R.id.thursday);
                    fridayButton = (Button) newTagView.findViewById(R.id.friday);
                    saturdayButton = (Button) newTagView.findViewById(R.id.saturday);
                    sundayButton = (Button) newTagView.findViewById(R.id.sunday);


                    classNameView.setText(c.getString(classNameIndex));
                    courseIDView.setText(courseIDValue);

                    instructorView.setText(c.getString(instructorIndex));
                    teachingAssistantView.setText(c.getString(teachingAssistantIndex));
                    DecimalFormat f = new DecimalFormat("00.00");
                    attendanceRateView.setText(f.format(calculateWeeklyAttendance(c.getString(courseIdIndex)) * 100) + " %");
                    termAttendanceRateView.setText(f.format(calculateTermAttendance(c.getString(courseIdIndex)) * 100) + " %");

                    int weeklyStudySessions = Integer.parseInt(c.getString(weeklyStudySessionsIndex));
                    studySessionsView.setText(weeklyStudySessions + "");
                    studySessionRateView.setText(f.format(calculateWeeklySessionsCompleted(c.getString(courseIdIndex), weeklyStudySessions) * 100) + " %");
                    termStudySessionRateView.setText(f.format(calculateTermSessionsCompleted(c.getString(courseIdIndex), weeklyStudySessions) * 100) + " %");


                    startDateView.setText(c.getString(startDateIndex));
                    endDateView.setText(c.getString(endDateIndex));


                    if (courseIDValue != null) {
                        System.out.println(courseIDValue);
                        Cursor scheduleCursor = databaseConnector.getClassSchedules(courseIDValue);
                        int index1 = scheduleCursor.getCount();

                        int classDayIndex = scheduleCursor.getColumnIndex("Day");
                        int startTimeIndex = scheduleCursor.getColumnIndex("StartTime");
                        int endTimeIndex = scheduleCursor.getColumnIndex("EndTime");

                        scheduleCursor.moveToFirst();

                        LayoutInflater inflater1 = (LayoutInflater) getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        TableLayout scheduleDisplayTableLayout = (TableLayout) newTagView.findViewById(R.id.schedulesTableLayout);
                        for (int j = 0; j < index1; j++) {


                            View newTagView1 = inflater.inflate(R.layout.time_layout_view, null);
                            scheduleDisplayTableLayout.addView(newTagView1, j);
                            TextView day = (TextView) newTagView1.findViewById(R.id.newDayTag);
                            TextView newStartTimeView = (TextView) newTagView1.findViewById(R.id.newStartTimeView);
                            TextView newEndTimeView = (TextView) newTagView1.findViewById(R.id.newEndTimeView);
                            day.setText(scheduleCursor.getString(classDayIndex));
                            newStartTimeView.setText(scheduleCursor.getString(startTimeIndex));
                            newStartTimeView.setTypeface(null, Typeface.BOLD);
                            newEndTimeView.setText(scheduleCursor.getString(endTimeIndex));
                            newEndTimeView.setTypeface(null, Typeface.BOLD);

                            switch (scheduleCursor.getString(classDayIndex)) {
                                case "Monday":
                                    mondayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Tuesday":
                                    tuesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Wednesday":
                                    wednesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Thursday":
                                    thursdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Friday":
                                    fridayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Saturday":
                                    saturdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Sunday":
                                    sundayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;


                            }

                            scheduleCursor.moveToNext();
                        }
                        //Code to sort the weekdays of the class in the view
                        List<TableRow> tableRowList = new ArrayList<TableRow>();
                        LinkedHashMap<String, Integer> weekDaysHashMap = populateOrderedHashMap();
                        for (int j = 0; j < scheduleDisplayTableLayout.getChildCount(); j++) {
                            View view = scheduleDisplayTableLayout.getChildAt(j);
                            if (view instanceof TableRow) {
                                TableRow row = (TableRow) view;
                                tableRowList.add(row);
                            }
                        }
                        List<TableRow> sortedList = new ArrayList<TableRow>();
                        for (String key : weekDaysHashMap.keySet()) {
                            for (int j = 0; j < tableRowList.size(); j++) {
                                TableRow row = tableRowList.get(j);
                                for (int k = 0; k < row.getChildCount(); k++) {
                                    View view = row.getChildAt(k);
                                    if (view instanceof TextView) {
                                        String weekDayString = ((TextView) view).getText().toString().toLowerCase();
                                        if (key.equals(weekDayString)) {
                                            sortedList.add(row);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        scheduleDisplayTableLayout.removeAllViews();
                        for (int j = 0; j < sortedList.size(); j++) {
                            scheduleDisplayTableLayout.addView(sortedList.get(j), j);
                        }
                        //End of code to sort the weekdays of the class in the view
                    }

                    Button newEditButton = (Button) newTagView.findViewById(R.id.edit);
                    newEditButton.setOnClickListener(editButtonListener);
                } else {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c.moveToNext();

        }
        c.moveToFirst();
        int historyCount = 0;
        for (int i = 0; i < index; i++) {
            try {
/*        if (!((currentDate.after(format1.parse(c.getString(startDateIndex))) ||
                currentDate.equals(format1.parse(c.getString(startDateIndex)))) &&
                (currentDate.before(format1.parse(c.getString(endDateIndex))) ||
                        currentDate.equals(format1.parse(c.getString(endDateIndex))))))*/
                if (currentDate.after(format1.parse(c.getString(endDateIndex)))) {
                    if (!flag) {
                        flag = !flag;
                        LayoutInflater inflater = (LayoutInflater) getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        View newTagView = inflater.inflate(R.layout.history_classes_layout, null);
                        //queryTableLayout.addView(newTagView, completedClassIndex++);
                    }
                    System.out.println("else Condition satisfied");
                    System.out.println("Completed classes index: " + completedClassIndex);
                    System.out.println(currentDate.after(format1.parse(c.getString(startDateIndex))) ||
                            currentDate.equals(format1.parse(c.getString(startDateIndex))));
                    System.out.println(currentDate.before(format1.parse(c.getString(endDateIndex))) ||
                            currentDate.equals(format1.parse(c.getString(endDateIndex))));
                    System.out.println(currentDate);
                    System.out.println(format1.parse(c.getString(startDateIndex)));
                    System.out.println(format1.parse(c.getString(endDateIndex)));

                    // cityTextView.setText(result.getString(cityIndex));
                    LayoutInflater inflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    String courseIDValue = c.getString(courseIdIndex);
                    // inflate new_tag_view.xml to create new tag and edit Buttons

                    View newTagView = inflater.inflate(R.layout.activity_main_activity2, null);
                    queryTableLayout.addView(newTagView, activeClassIndex + completedClassIndex++);
                    // completedClassIndex++;
                    if (historyCount == 0) {
                        TextView histClasses = (TextView) newTagView.findViewById(R.id.historyClasses);
                        histClasses.setVisibility(View.VISIBLE);
                        historyCount++;
                    }

                    classNameView = (TextView) newTagView.findViewById(R.id.classNameView);
                    courseIDView = (TextView) newTagView.findViewById(R.id.courseNumberView);
                    instructorView = (TextView) newTagView.findViewById(R.id.instructorView);
                    teachingAssistantView = (TextView) newTagView.findViewById(R.id.teachingAssistantView);
                    studySessionsView = (TextView) newTagView.findViewById(R.id.targetSessionsView);
                    //studySessionsView = (TextView) newTagView.findViewById(R.id.targetSessionsView);

                    attendanceRateView = (TextView) newTagView.findViewById(R.id.attendanceRateView);
                    termAttendanceRateView = (TextView) newTagView.findViewById(R.id.termAttendanceRateView);
                    studySessionRateView = (TextView) newTagView.findViewById(R.id.targetWeekSessionsView);
                    termStudySessionRateView = (TextView) newTagView.findViewById(R.id.targetTermSessionsView);


                    startDateView = (TextView) newTagView.findViewById(R.id.startDateView);
                    endDateView = (TextView) newTagView.findViewById(R.id.endDateView);

                    mondayButton = (Button) newTagView.findViewById(R.id.monday);
                    tuesdayButton = (Button) newTagView.findViewById(R.id.tuesday);
                    wednesdayButton = (Button) newTagView.findViewById(R.id.wednesday);
                    thursdayButton = (Button) newTagView.findViewById(R.id.thursday);
                    fridayButton = (Button) newTagView.findViewById(R.id.friday);
                    saturdayButton = (Button) newTagView.findViewById(R.id.saturday);
                    sundayButton = (Button) newTagView.findViewById(R.id.sunday);


                    classNameView.setText(c.getString(classNameIndex));
                    courseIDView.setText(courseIDValue);

                    instructorView.setText(c.getString(instructorIndex));
                    teachingAssistantView.setText(c.getString(teachingAssistantIndex));
                    DecimalFormat f = new DecimalFormat("00.00");
                    attendanceRateView.setText(f.format(calculateWeeklyAttendance(c.getString(courseIdIndex)) * 100) + " %");
                    termAttendanceRateView.setText(f.format(calculateTermAttendance(c.getString(courseIdIndex)) * 100) + " %");

                    int weeklyStudySessions = Integer.parseInt(c.getString(weeklyStudySessionsIndex));
                    studySessionsView.setText(weeklyStudySessions + "");
                    studySessionRateView.setText(f.format(calculateWeeklySessionsCompleted(c.getString(courseIdIndex), weeklyStudySessions) * 100) + " %");
                    termStudySessionRateView.setText(f.format(calculateTermSessionsCompleted(c.getString(courseIdIndex), weeklyStudySessions) * 100) + " %");


                    startDateView.setText(c.getString(startDateIndex));
                    endDateView.setText(c.getString(endDateIndex));


                    if (courseIDValue != null) {
                        System.out.println(courseIDValue);
                        Cursor scheduleCursor = databaseConnector.getClassSchedules(courseIDValue);
                        int index1 = scheduleCursor.getCount();

                        int classDayIndex = scheduleCursor.getColumnIndex("Day");
                        int startTimeIndex = scheduleCursor.getColumnIndex("StartTime");
                        int endTimeIndex = scheduleCursor.getColumnIndex("EndTime");

                        scheduleCursor.moveToFirst();

                        LayoutInflater inflater1 = (LayoutInflater) getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                        TableLayout scheduleDisplayTableLayout = (TableLayout) newTagView.findViewById(R.id.schedulesTableLayout);
                        for (int j = 0; j < index1; j++) {


                            View newTagView1 = inflater.inflate(R.layout.time_layout_view, null);
                            scheduleDisplayTableLayout.addView(newTagView1, j);
                            TextView day = (TextView) newTagView1.findViewById(R.id.newDayTag);
                            TextView newStartTimeView = (TextView) newTagView1.findViewById(R.id.newStartTimeView);
                            TextView newEndTimeView = (TextView) newTagView1.findViewById(R.id.newEndTimeView);
                            day.setText(scheduleCursor.getString(classDayIndex));
                            newStartTimeView.setText(scheduleCursor.getString(startTimeIndex));
                            newStartTimeView.setTypeface(null, Typeface.BOLD);
                            newEndTimeView.setText(scheduleCursor.getString(endTimeIndex));
                            newEndTimeView.setTypeface(null, Typeface.BOLD);

                            switch (scheduleCursor.getString(classDayIndex)) {
                                case "Monday":
                                    mondayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Tuesday":
                                    tuesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Wednesday":
                                    wednesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Thursday":
                                    thursdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Friday":
                                    fridayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Saturday":
                                    saturdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;
                                case "Sunday":
                                    sundayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons));
                                    break;


                            }

                            scheduleCursor.moveToNext();
                        }
                        scheduleCursor.close();
                        //Code to sort the weekdays of the class in the view
                        List<TableRow> tableRowList = new ArrayList<TableRow>();
                        LinkedHashMap<String, Integer> weekDaysHashMap = populateOrderedHashMap();
                        for (int j = 0; j < scheduleDisplayTableLayout.getChildCount(); j++) {
                            View view = scheduleDisplayTableLayout.getChildAt(j);
                            if (view instanceof TableRow) {
                                TableRow row = (TableRow) view;
                                tableRowList.add(row);
                            }
                        }
                        List<TableRow> sortedList = new ArrayList<TableRow>();
                        for (String key : weekDaysHashMap.keySet()) {
                            for (int j = 0; j < tableRowList.size(); j++) {
                                TableRow row = tableRowList.get(j);
                                for (int k = 0; k < row.getChildCount(); k++) {
                                    View view = row.getChildAt(k);
                                    if (view instanceof TextView) {
                                        String weekDayString = ((TextView) view).getText().toString().toLowerCase();
                                        if (key.equals(weekDayString)) {
                                            sortedList.add(row);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        scheduleDisplayTableLayout.removeAllViews();
                        for (int j = 0; j < sortedList.size(); j++) {
                            scheduleDisplayTableLayout.addView(sortedList.get(j), j);
                        }
                        //End of code to sort the weekdays of the class in the view
                    }

                    Button newEditButton = (Button) newTagView.findViewById(R.id.edit);
                    newEditButton.setOnClickListener(editButtonListener);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.moveToNext();
        }
        if (index < 1) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View newTagView = inflater.inflate(R.layout.activity_no_class, null);
            queryTableLayout.addView(newTagView, 0);

        }
        c.close();
    }

    /*@Override
    public void onResume()
    {
        super.onResume();
        mDrawerLayout.closeDrawers();
    }*/
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            //Intent intent = new Intent(Intent.ACTION_MAIN);
            //intent.addCategory(Intent.CATEGORY_HOME);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity1.this.finish();
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
    private void DeleteClass(View v) {
        DatabaseConnector databaseConnector = new DatabaseConnector(this);
        databaseConnector.open();
        RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
        TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumberView);
        String courseID = getCourseID.getText().toString();
        databaseConnector.deleteClasses(courseID);
        databaseConnector.close();
    }

    public void deleteClasses(final View v) {
        AlertDialog.Builder dialBuilder =
                new AlertDialog.Builder(MainActivity1.this);

        // set dialog title & message, and provide Button to dismiss
        dialBuilder.setTitle(R.string.deleteConfirm);
        dialBuilder.setMessage(R.string.deleteMessage);
        dialBuilder.setPositiveButton(R.string.deleteYesButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTask<Object, Object, Object> saveContactTask =
                        new AsyncTask<Object, Object, Object>() {
                            @Override
                            protected Object doInBackground(Object... params) {
                                DeleteClass(v);
                                // save classes to the database
                                //Toast.makeText(ClassActivity.this, activityName+" deleted!", Toast.LENGTH_SHORT).show();
                                return null;
                            } // end method doInBackground

                            @Override
                            protected void onPostExecute(Object result) {
                                queryTableLayout.removeAllViews();
                                editMode();
                            }
                        }; // end AsyncTask

                // save the contact to the database using a separate thread
                saveContactTask.execute((Object[]) null);
            }
        });
        dialBuilder.setNegativeButton(R.string.deleteNoButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialBuilder.show();
        // end else1// end method onClick
    }

    /*@Override
    protected void onStop() {


        super.onStop();
    } */// end method onStop
    // performs database query outside GUI thread


    public OnClickListener editButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent addNewClass =
                    new Intent(MainActivity1.this, MainActivity3.class);
            RelativeLayout buttonTableRow = (RelativeLayout) v.getParent();
            TextView getCourseID = (TextView) buttonTableRow.findViewById(R.id.courseNumberView);
            String courseIDString = getCourseID.getText().toString();
            //System.out.println(getCourseID.getText().toString());


            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("CourseIDString", courseIDString);
            bundle.putInt("ActivityInt", ActivityConstants.MainActivity1);
            addNewClass.putExtras(bundle);
            startActivity(addNewClass); // start the AddEditContact Activity


            // set EditTexts to match the chosen tag and query
            //tagEditText.setText(tag);
            //queryEditText.setText(savedSearches.getString(tag, ""));
        } // end method onClick
    }; // end OnClickListener anonymous inner class

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent addNewClass =
                    new Intent(MainActivity1.this, MainActivity3.class);
            startActivity(addNewClass); // start the AddEditContact Activity
        }
        return super.onOptionsItemSelected(item); // call super's method
    }
    /*@Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        startActivity(new Intent("com.teamtreehouse.oslist.MainActivity1"));
        queryTableLayout.removeAllViews();
        editMode();
    }*/



  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {

            if (resultCode == RESULT_OK) {
                queryTableLayout.removeAllViews();
                editMode();
            }else{
                queryTableLayout.removeAllViews();
                editMode();
            }
        }
    }//onActivityResult*/

    private double calculateWeeklyAttendance(String courseID) {
        Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Date tempNewDate;
        Date currentDate = getCurrentDate();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        //Log.d("BeforeSwitch",currentDate+"");

        c1.setTime(currentDate);
        weekStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), currentDate);
        startCal.setTime(weekStartDate);

        c1.setTime(currentDate);
        weekEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), currentDate);
        endCal.setTime(weekEndDate);

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();

        //Create and open database object
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity1.this);
        databaseConnector.open();

        Cursor semDateCursor = databaseConnector.getOneClass(courseID);
        semDateCursor.moveToFirst();
        try {
            //to check if semester end date is not before the end date of the week
            dt1 = format1.parse(semDateCursor.getString(semDateCursor.getColumnIndex("EndDate")));
            if (dt1.before(weekEndDate))
                endCal.setTime(dt1);
            //to check if semester start date is not after the start date of the week
            dt1 = format1.parse(semDateCursor.getString(semDateCursor.getColumnIndex("StartDate")));
            if (dt1.after(weekStartDate))
                startCal.setTime(dt1);
        } catch (ParseException ex) {
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
        for (int i = 0; i < c.getCount(); i++) {
            int dayIndex = c.getColumnIndex("Day");
            daysList.add(c.getString(dayIndex).toLowerCase());
            c.moveToNext();
        }
        do {
            String calWeekVal = startCal.get(Calendar.DAY_OF_WEEK) + "";
            if (daysList.contains(weekDays.get(calWeekVal).toLowerCase())) {
                totalWeeklyClass++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
        Log.d("After do-while", totalWeeklyClass + "");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        c = databaseConnector.getAttendance(courseID);
        int attendanceCountDB = c.getCount();
        c.moveToFirst();


        Log.d("Attnd", classAttendedCount + "");
        for (int i = 0; i < attendanceCountDB; i++) {
            int dateIndex = c.getColumnIndex("Date");

            try {
                dt1 = format1.parse(c.getString(dateIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((dt1.after(weekStartDate) || dt1.equals(weekStartDate)) && (dt1.before(weekEndDate) || dt1.equals(weekEndDate))) {
                counter = counter + 1;
                int isAttendedIndex = c.getColumnIndex("IsAttended");
                int isCancelledIndex = c.getColumnIndex("IsCancelled");
                boolean isAttended = c.getInt(isAttendedIndex) > 0;
                boolean isCancelled = c.getInt(isCancelledIndex) > 0;

                if (isAttended)
                    classAttendedCount = classAttendedCount + 1;
                if (isCancelled)
                    totalWeeklyClass = totalWeeklyClass - 1;
            }
            c.moveToNext();
        }

        double weeklyPercentage = 0.0;
        if (totalWeeklyClass != 0)
            weeklyPercentage = (double) classAttendedCount / totalWeeklyClass;
            //date condition to make sure that weekly percentage of InActive classes is not 100%
        else if (totalWeeklyClass == 0 && classAttendedCount == 0 && (dt1.after(weekStartDate) || dt1.equals(weekStartDate)) && (dt1.before(weekEndDate) || dt1.equals(weekEndDate)))//when there is only one class in a week and that is either cancelled or holiday
            weeklyPercentage = 1.0;

        return weeklyPercentage;
    }

    private double calculateTermAttendance(String courseID) {
        Date semesterStartDate = new Date();
        Date semesterEndDate = new Date();
        Date todaysDate = new Date();
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity1.this);
        databaseConnector.open();

        //get a cursor for the classes
        Cursor c = databaseConnector.getOneClass(courseID);
        int index = c.getCount();
        c.moveToFirst();
        int startDateIndex = c.getColumnIndex("StartDate");
        int endDateIndex = c.getColumnIndex("EndDate");
        if (curDate == null || curDate.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            curDate = month + 1 + "/" + day + "/" + year;
            //Log.d("Current Date",curDate);
        }

        /////////////////////////////////////////////////////////////////////
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        try {
            dt1 = format1.parse(c.getString(startDateIndex));
            c1.setTime(dt1);
            startCal.setTime(dt1);
            semesterStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

            dt1 = format1.parse(c.getString(endDateIndex));
            c1.setTime(dt1);
            semesterEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

            dt1 = format1.parse(curDate);
            c1.setTime(dt1);
            endCal.setTime(dt1);
            //todaysDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)),dt1);
            todaysDate = dt1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //int numberofWeeks = getWeeksBetweenDates(semesterStartDate,semesterEndDate);

        c = databaseConnector.getClassSchedules(courseID);
        int numberofWeeklyClasses = c.getCount();


        //int numberofClassesPerTerm = numberofWeeks * numberofWeeklyClasses;
        int totalClassestillToday = getWeeksBetweenDates(semesterStartDate, todaysDate) * numberofWeeklyClasses;
        totalClassestillToday = 0;
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        List<String> daysList = new ArrayList<String>();
        c.moveToFirst();
        for (int i = 0; i < numberofWeeklyClasses; i++) {
            int dayIndex = c.getColumnIndex("Day");
            daysList.add(c.getString(dayIndex).toLowerCase());
            c.moveToNext();
        }
        do {
            String calWeekVal = startCal.get(Calendar.DAY_OF_WEEK) + "";
            if (daysList.contains(weekDays.get(calWeekVal).toLowerCase())) {
                totalClassestillToday++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        int counter = 0;
        int classAttendedCount = 0;
        //classAttendedCount = totalClassestillToday;

        //int pendingClasses = numberofClassesPerTerm - totalClassestillToday;
        c = databaseConnector.getAttendance(courseID);
        int attendanceCountDB = c.getCount();
        c.moveToFirst();

        for (int i = 0; i < attendanceCountDB; i++) {
            int dateIndex = c.getColumnIndex("Date");

            try {
                dt1 = format1.parse(c.getString(dateIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((dt1.after(semesterStartDate) || dt1.equals(semesterStartDate)) && (dt1.before(todaysDate) || dt1.equals(todaysDate))) {
                counter = counter + 1;
                int isAttendedIndex = c.getColumnIndex("IsAttended");
                int isCancelledIndex = c.getColumnIndex("IsCancelled");
                boolean isAttended = c.getInt(isAttendedIndex) > 0;
                boolean isCancelled = c.getInt(isCancelledIndex) > 0;
                /*if(!isAttended && !isCancelled)
                    classAttendedCount = classAttendedCount - 1;*/
                if (isAttended)
                    classAttendedCount = classAttendedCount + 1;
                if (isCancelled)
                    totalClassestillToday = totalClassestillToday - 1;
                //numberofClassesPerTerm = numberofClassesPerTerm - 1;
            }
            c.moveToNext();
        }
        /*if(counter < numberofClassesPerTerm)
        {
            classAttendedCount = classAttendedCount - (numberofClassesPerTerm - pendingClasses - counter);
        }*/
        double termPercentage = 0.0;
        if (totalClassestillToday != 0)
            //termPercentage = (double)classAttendedCount/numberofClassesPerTerm;
            termPercentage = (double) classAttendedCount / totalClassestillToday;

        return termPercentage;

        //////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    public double calculateWeeklySessionsCompleted(String courseID, int targetedWeeklySessions) {
        Log.d("WeeklySessionPercentage", "Calculating Sessions Percentage");
        Date weekStartDate = new Date();
        Date weekEndDate = new Date();
        Date semesterStartDate = new Date();
        Date semesterEndDate = new Date();
        Date currentDate = getCurrentDate();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity1.this);
        databaseConnector.open();

        //get a cursor for the classes
        Cursor c2 = databaseConnector.getOneClass(courseID);

        c2.moveToFirst();
        int startDateIndex = c2.getColumnIndex("StartDate");
        int endDateIndex = c2.getColumnIndex("EndDate");

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        try {
            dt1 = format1.parse(c2.getString(startDateIndex));
            c1.setTime(dt1);
            semesterStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

            dt1 = format1.parse(c2.getString(endDateIndex));
            c1.setTime(dt1);
            semesterEndDate = dt1;//getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c1.setTime(currentDate);
        dt1 = currentDate;
        /*if(semesterStartDate.after(dt1))
            dt1 = semesterStartDate;*/
        weekStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

        c1.setTime(currentDate);
        dt1 = currentDate;
        if (semesterEndDate.before(dt1)) {
            dt1 = semesterEndDate;
            c1.setTime(dt1);
        }
        weekEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

        int weeklySessionsCompleted = 0;

        //get a cursor for the sessions
        Cursor c = databaseConnector.getStudySessions(courseID);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            int dateIndex = c.getColumnIndex("Date");

            try {
                dt1 = format1.parse(c.getString(dateIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((dt1.after(weekStartDate) || dt1.equals(weekStartDate)) && (dt1.before(weekEndDate) || dt1.equals(weekEndDate))) {
                int completedSessionsIndex = c.getColumnIndex("CompletedSessions");
                int completedSessions = c.getInt(completedSessionsIndex);
                weeklySessionsCompleted = weeklySessionsCompleted + completedSessions;
            }
            c.moveToNext();
        }
        double weeklySessionsPercentage = 0.0;
        if (targetedWeeklySessions != 0)
            weeklySessionsPercentage = (double) weeklySessionsCompleted / targetedWeeklySessions;

        return weeklySessionsPercentage;
    }

    public double calculateTermSessionsCompleted(String courseID, int targetedWeeklySessions) {
        Log.d("TermSessionsPercentage", "Calculating Sessions Term Percentage");
        Date semesterStartDate = new Date();
        Date semesterEndDate = new Date();
        Date todaysDate = new Date();
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity1.this);
        databaseConnector.open();

        //get a cursor for the classes
        Cursor c = databaseConnector.getOneClass(courseID);

        c.moveToFirst();
        int startDateIndex = c.getColumnIndex("StartDate");
        int endDateIndex = c.getColumnIndex("EndDate");

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        try {
            dt1 = format1.parse(c.getString(startDateIndex));
            c1.setTime(dt1);
            semesterStartDate = getStartDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

            dt1 = format1.parse(c.getString(endDateIndex));
            c1.setTime(dt1);
            semesterEndDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

            dt1 = format1.parse(curDate);
            if (semesterEndDate.before(dt1))
                dt1 = semesterEndDate;
            c1.setTime(dt1);
            todaysDate = getEndDateFromWeekDay(Integer.toString(c1.get(Calendar.DAY_OF_WEEK)), dt1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        int numberofWeeks = getWeeksBetweenDates(semesterStartDate, todaysDate);//getWeeksBetweenDates(semesterStartDate,semesterEndDate);
        int totalSessionsinTerm = 0;
        int termSessionsCompleted = 0;
        totalSessionsinTerm = numberofWeeks * targetedWeeklySessions;

        //get a cursor for the sessions
        c = databaseConnector.getStudySessions(courseID);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            int dateIndex = c.getColumnIndex("Date");

            try {
                dt1 = format1.parse(c.getString(dateIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((dt1.after(semesterStartDate) || dt1.equals(semesterStartDate)) && (dt1.before(todaysDate) || dt1.equals(todaysDate))) {
                int completedSessionsIndex = c.getColumnIndex("CompletedSessions");
                int completedSessions = c.getInt(completedSessionsIndex);
                termSessionsCompleted = termSessionsCompleted + completedSessions;
            }
            c.moveToNext();
        }

        double termSessionsPercentage = 0.0;
        if (totalSessionsinTerm != 0)
            termSessionsPercentage = (double) termSessionsCompleted / totalSessionsinTerm;


        return termSessionsPercentage;
    }


    public int getWeeksBetweenDates(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;


        int rest = (int) delta % 365;
        int weeks = (int) Math.ceil(rest / 7d);
        return weeks;
    }

    private Date getStartDateFromWeekDay(String weekDay, Date curDate) {
        Date weekStartDate = new Date();
        //Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(curDate);

        switch (weekDay) {
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
        return weekStartDate;
    }

    private Date getEndDateFromWeekDay(String weekDay, Date curDate) {
        Date weekEndDate = new Date();
        Date tempNewDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(curDate);
        switch (weekDay) {
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


    private Date getCurrentDate() {
        String curDate = null;
        Date currentDate = new Date();
        Calendar c1 = Calendar.getInstance();

        int day = c1.get(Calendar.DAY_OF_MONTH);
        int month = c1.get(Calendar.MONTH);
        int year = c1.get(Calendar.YEAR);
        curDate = month + 1 + "/" + day + "/" + year;
        //Log.d("Current Date",curDate);


        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        try {
            dt1 = format1.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentDate = dt1;
        return currentDate;
    }

    private void populateHashMap() {
        weekDays.put("1", "Sunday");
        weekDays.put("2", "Monday");
        weekDays.put("3", "Tuesday");
        weekDays.put("4", "Wednesday");
        weekDays.put("5", "Thursday");
        weekDays.put("6", "Friday");
        weekDays.put("7", "Saturday");
    }

    private LinkedHashMap<String, Integer> populateOrderedHashMap() {
        LinkedHashMap<String, Integer> weekDays = new LinkedHashMap<String, Integer>();
        weekDays.put("monday", 1);
        weekDays.put("tuesday", 2);
        weekDays.put("wednesday", 3);
        weekDays.put("thursday", 4);
        weekDays.put("friday", 5);
        weekDays.put("saturday", 6);
        weekDays.put("sunday", 7);

        return weekDays;
    }
}