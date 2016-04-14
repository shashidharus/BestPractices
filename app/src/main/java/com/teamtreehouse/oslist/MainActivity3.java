package com.teamtreehouse.oslist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity3 extends MainActivity implements View.OnClickListener {

    private EditText className;
    private EditText courseID;
    private Spinner instructor;
    private Spinner teachingAssistant;
    private EditText startDate;
    private EditText endDate;
    private EditText noOfStudySessions;
    private EditText classLocation;
    private EditText description, startTime, endTime;
    private TextView classDay;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TimePicker timePicker1;
    private boolean timeEntered = true;
    private ImageButton addIns, addTa;
    private ImageButton editIns, editTa;
    ArrayList<EditText> myEditTextList = new ArrayList<EditText>();
    Bundle extras;
    String text;
    int activity;
    Button mondayButton;
    Button tuesdayButton;
    Button wednesdayButton;
    Button thursdayButton;
    Button fridayButton;
    Button saturdayButton;
    Button sundayButton;

    TextView day;
    EditText startTimeText;
    EditText endTimeText;
    static int editCount = 0, endCount = 0;
    TableLayout timeDisplayTableLayout;
    String startTimeValue = "";
    String endTimeValue = "";
    private static String oldCourseID;
    ArrayAdapter<String> instructorAdapter;
    ArrayAdapter<String> taAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);
        super.onCreateDrawer();
        getSupportActionBar().setTitle("Add Class");
        populateData();

    }

    public void populateData() {
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        className = (EditText) findViewById(R.id.classNameEdit);
        courseID = (EditText) findViewById(R.id.courseNumberEdit);
        instructor = (Spinner) findViewById(R.id.instructorSpinner);
        teachingAssistant = (Spinner) findViewById(R.id.teachingAssistantSpinner);
        startDate = (EditText) findViewById(R.id.semesterStartEdit);
        startDate.setInputType(InputType.TYPE_NULL);
        addIns = (ImageButton) findViewById(R.id.imageButtonAddIns);
        editIns = (ImageButton) findViewById(R.id.imageButtonEditIns);
        addTa = (ImageButton) findViewById(R.id.imageButtonAddTa);
        editTa = (ImageButton) findViewById(R.id.imageButtonEditTa);
        endDate = (EditText) findViewById(R.id.semesterEndEdit);
        endDate.setInputType(InputType.TYPE_NULL);
        noOfStudySessions = (EditText) findViewById(R.id.targetSessionsEdit);
        classLocation = (EditText) findViewById(R.id.classLocation);
        description = (EditText) findViewById(R.id.classDescription);
        instructorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllInstructors());
        taAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllTAs());
        instructorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructor.setAdapter(instructorAdapter);
        teachingAssistant.setAdapter(taAdapter);
        //Hiding the keyboard
        InputMethodManager imm1 = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(startDate.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
        InputMethodManager imm2 = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(endDate.getWindowToken(), InputMethodManager.RESULT_HIDDEN);

        addIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity3.this, InstructorEdit.class);
                startActivity(newIntent);
            }
        });
        addTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity3.this, InstructorEdit.class);
                startActivity(newIntent);
            }
        });
        editIns.setOnClickListener(editInstructor);
        editTa.setOnClickListener(editTeachingAssistant);
        Bundle bundle = new Bundle();
        System.out.println("Inside Activity 3");
        extras = getIntent().getExtras(); // get Bundle of extras
        int callingActivity = getIntent().getIntExtra("ActivityInt", 0);
        String courseIDValue = null;

        if (extras != null) {
            getSupportActionBar().setTitle("Edit Class");
            System.out.println("Text:  " + text);
            DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity3.this);

            System.out.println("Inside Activity Edit");
            databaseConnector.open();
            Cursor classCursor = null;//
            switch (callingActivity) {
                case ActivityConstants.ClassActivityEdit:
                    String editClassName = extras.getString("ClassNameString");
                    classCursor = databaseConnector.getOneClassByClassName(editClassName);
                    break;
                case ActivityConstants.MainActivity1:
                    text = extras.getString("CourseIDString");
                    classCursor = databaseConnector.getOneClass(text);
                    break;
                case ActivityConstants.MainActivity5:
                    text = extras.getString("CourseIDString");
                    classCursor = databaseConnector.getOneClass(text);
                    break;
                case ActivityConstants.MainActivity6:
                    text = extras.getString("CourseIDString");
                    classCursor = databaseConnector.getOneClass(text);
                    break;
            }
            classCursor.moveToFirst();

            int classNameIndex = classCursor.getColumnIndex("ClassName");
            int courseIdIndex = classCursor.getColumnIndex("CourseID");
            int instructorIndex = classCursor.getColumnIndex("Instructor");
            int teachingAssistantIndex = classCursor.getColumnIndex("TeachingAssistant");
            int startDateIndex = classCursor.getColumnIndex("StartDate");
            int endDateIndex = classCursor.getColumnIndex("EndDate");
            int noOfStudySessionsIndex = classCursor.getColumnIndex("NumberOfStudySessions");
            int classLocationIndex = classCursor.getColumnIndex("ClassLocation");
            int descriptionIndex = classCursor.getColumnIndex("Description");


            System.out.println("Index in Edit " + startDateIndex);
            System.out.println("String in Edit " + classCursor.getString(startDateIndex));
            courseIDValue = classCursor.getString(courseIdIndex);
            className.setText(classCursor.getString(classNameIndex));
            courseID.setText(courseIDValue);
            oldCourseID = courseIDValue;
            if (classCursor.getString(instructorIndex) != null) {
                int pos = instructorAdapter.getPosition(classCursor.getString(instructorIndex));
                instructor.setSelection(pos);
            }
            if (classCursor.getString(teachingAssistantIndex) != null) {
                int pos = taAdapter.getPosition(classCursor.getString(teachingAssistantIndex));
                teachingAssistant.setSelection(pos);
            }

            startDate.setText(classCursor.getString(startDateIndex));

            endDate.setText(classCursor.getString(endDateIndex));

            noOfStudySessions.setText(classCursor.getString(noOfStudySessionsIndex));
            classLocation.setText(classCursor.getString(classLocationIndex));
            description.setText(classCursor.getString(descriptionIndex));

            classCursor.close();
            databaseConnector.close();

        }

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        setDateTimeField();
        Button saveButton =
                (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(saveButtonClicked);


        mondayButton = (Button) findViewById(R.id.monday1);
        mondayButton.setOnClickListener(this);
        ///bundle.s
        tuesdayButton = (Button) findViewById(R.id.tuesday1);
        tuesdayButton.setOnClickListener(this);

        wednesdayButton = (Button) findViewById(R.id.wednesday1);
        wednesdayButton.setOnClickListener(this);

        thursdayButton = (Button) findViewById(R.id.thursday1);
        thursdayButton.setOnClickListener(this);

        fridayButton = (Button) findViewById(R.id.friday1);
        fridayButton.setOnClickListener(this);

        saturdayButton = (Button) findViewById(R.id.saturday1);
        saturdayButton.setOnClickListener(this);

        sundayButton = (Button) findViewById(R.id.sunday1);
        sundayButton.setOnClickListener(this);

        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity3.this);
        databaseConnector.open();
        if (courseIDValue != null) {
            System.out.println(courseIDValue);
            Cursor scheduleCursor = databaseConnector.getClassSchedules(courseIDValue);
            int index = scheduleCursor.getCount();
            scheduleCursor.moveToFirst();
            int classDayIndex = scheduleCursor.getColumnIndex("Day");
            int startTimeIndex = scheduleCursor.getColumnIndex("StartTime");
            int endTimeIndex = scheduleCursor.getColumnIndex("EndTime");
            for (int i = 0; i < index; i++) {
                makeClassTag(scheduleCursor.getString(classDayIndex), scheduleCursor.getString(startTimeIndex), scheduleCursor.getString(endTimeIndex));
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

        }
        Button newDeleteButton = (Button) findViewById(R.id.delete);
        newDeleteButton.setOnClickListener(deleteButtonListener);

        ImageButton callHelp = (ImageButton) findViewById(R.id.help);
        callHelp.setOnClickListener(callGuidelinesPage);

    }
    /*@Override
    protected void onResume(){
        super.onResume();

    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 || requestCode == 3 || requestCode == 4 || requestCode == 5) {
            //Intent refresh = new Intent(this, MainActivity3.class);
            MainActivity3.this.finish();
            //refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(getIntent());
        }
    }*/

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("ClassName", className.getText().toString());
        savedInstanceState.putString("CourseID", courseID.getText().toString());
        savedInstanceState.putString("Instructor", instructor.getSelectedItem().toString());
        savedInstanceState.putString("TA", teachingAssistant.getSelectedItem().toString());
        savedInstanceState.putString("StartDate", startDate.getText().toString());
        savedInstanceState.putString("EndDate", endDate.getText().toString());
        // etc.
    }*/

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }
    View.OnClickListener editInstructor = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("NameStringExtra", instructor.getSelectedItem().toString());
            bundle.putInt("ActivityInt", ActivityConstants.MainActivity3);
            bundle.putString("InstructorEdit", "ins");
            Intent editClassIntent = new Intent(MainActivity3.this, InstructorEdit.class);
            editClassIntent.putExtras(bundle);
            startActivity(editClassIntent);
        }
    };
    View.OnClickListener editTeachingAssistant = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("NameStringExtra", teachingAssistant.getSelectedItem().toString());
            bundle.putInt("ActivityInt", ActivityConstants.MainActivity3);
            bundle.putString("InstructorEdit", "ta");
            Intent editClassIntent = new Intent(MainActivity3.this, InstructorEdit.class);
            editClassIntent.putExtras(bundle);
            startActivity(editClassIntent);
        }
    };

    public List<String> getAllInstructors() {
        List<String> labels = new ArrayList<String>();
        DatabaseConnector db = new DatabaseConnector(MainActivity3.this);
        db.open();
        Cursor cursor = db.getInstructor();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            labels = Collections.emptyList();
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllTAs() {
        List<String> labels = new ArrayList<String>();
        DatabaseConnector db = new DatabaseConnector(MainActivity3.this);
        db.open();
        Cursor cursor = db.getTA();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            labels = Collections.emptyList();
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    private void setDateTimeField() {


        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    View.OnClickListener saveButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (className.getText().length() != 0 && courseID.getText().length() != 0 &&
                    startDate.getText().length() != 0 && endDate.getText().length() != 0 && noOfStudySessions.getText().length() != 0
                    ) {
                TableLayout layout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);
//if no days added, prompt to add class days--Priyanka
                if (layout.getChildCount() == 0) {
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity3.this);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle("Invalid Schedule");
                    builder.setMessage("Add a class schedule");
                    builder.setPositiveButton(R.string.errorButton, null);
                    builder.show(); // display the Dialog
                } else {

                    for (int i = 0; i < layout.getChildCount(); i++) {

                        View child = layout.getChildAt(i);

                        if (child instanceof TableRow) {
                            TableRow row = (TableRow) child;
                            classDay = (TextView) row.findViewById(R.id.newTagButton);
                            startTime = (EditText) row.findViewById(R.id.newStartTime);
                            endTime = (EditText) row.findViewById(R.id.newEndTime);


                            //if time is not selected, prompt to add start time and end time--Priyanka
                            if (startTime.getText().length() == 0 || endTime.getText().length() == 0) {

                                timeEntered = false;
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(MainActivity3.this);

                                // set dialog title & message, and provide Button to dismiss
                                builder.setTitle("Invalid Time");
                                builder.setMessage("Pick the Start time and End time of the class");
                                builder.setPositiveButton(R.string.errorButton, null);
                                builder.show(); // display the Dialog

                            } else {
                                timeEntered = true;

                            }
                        }
                    }

                    if (timeEntered) {

                        AsyncTask<Object, Object, Object> saveContactTask =
                                new AsyncTask<Object, Object, Object>() {
                                    @Override
                                    protected Object doInBackground(Object... params) {
                                        SaveClasses(); // save classes to the database
                                        return null;
                                    } // end method doInBackground

                                    @Override
                                    protected void onPostExecute(Object result) {
                                        Toast.makeText(MainActivity3.this, className.getText().toString() + " saved",
                                                Toast.LENGTH_SHORT).show();
                                        NavUtils.navigateUpFromSameTask(MainActivity3.this); // return to the previous Activity
                                    } // end method onPostExecute
                                }; // end AsyncTask

                        // save the contact to the database using a separate thread
                        saveContactTask.execute((Object[]) null);

                    }

                }
            } // end if
            else {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity3.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show(); // display the Dialog
            } // end else1
        } // end method onClick
    }; // end OnClickListener saveContactButtonClicked


    View.OnClickListener callGuidelinesPage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent callGuidelines =
                    new Intent(MainActivity3.this, StudySessionsGuideLines.class);
            startActivity(callGuidelines);

        } // end method onClick
    }; // end OnClickListener saveContactButtonClicked


    View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (className.getText().length() != 0) {
                AsyncTask<Object, Object, Object> saveContactTask =
                        new AsyncTask<Object, Object, Object>() {
                            @Override
                            protected Object doInBackground(Object... params) {
                                DeleteClasses(); // save classes to the database
                                return null;
                            } // end method doInBackground

                            @Override
                            protected void onPostExecute(Object result) {
                                finish(); // return to the previous Activity
                            } // end method onPostExecute
                        }; // end AsyncTask

                // save the contact to the database using a separate thread
                saveContactTask.execute((Object[]) null);
            } // end if
            else {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity3.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle(R.string.errorTitle);
                builder.setMessage(R.string.errorMessage);
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show(); // display the Dialog
            } // end else1
        } // end method onClick
    }; // end OnClickListener saveContactButtonClicked

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void SaveClasses() {

        System.out.println("SaveClasses");
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        if (getIntent().getExtras() == null) {
            // insert the class information into the database
            TableLayout layout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);

            databaseConnector.insertClassDetails(
                    className.getText().toString(),
                    courseID.getText().toString(),
                    (instructor.getSelectedItem() != null) ? instructor.getSelectedItem().toString():"",
                    (teachingAssistant.getSelectedItem() != null) ? teachingAssistant.getSelectedItem().toString():"",
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    Integer.parseInt(noOfStudySessions.getText().toString()),
                    classLocation.getText().toString(),
                    description.getText().toString());


            for (int i = 0; i < layout.getChildCount(); i++) {

                View child = layout.getChildAt(i);

                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    classDay = (TextView) row.findViewById(R.id.newTagButton);
                    startTime = (EditText) row.findViewById(R.id.newStartTime);
                    //  startTime.setInputType(InputType.TYPE_NULL);
                    endTime = (EditText) row.findViewById(R.id.newEndTime);
                    //  endTime.setInputType(InputType.TYPE_NULL);


                    if (startTime.getText().length() != 0 && endTime.getText().length() != 0) {
//changed insertClassSchedule to upsertClassSchedule. Change the code in Database Connector --Priyanka
                        databaseConnector.upsertClassSchedule(courseID.getText().toString(),
                                classDay.getText().toString(), startTime.getText().toString(), endTime.getText().toString());


                    }


                }
            }
            //}
        } // end if
        else {
            databaseConnector.updateClasses(
                    oldCourseID,
                    className.getText().toString(),
                    courseID.getText().toString(),
                    instructor.getSelectedItem().toString(),
                    teachingAssistant.getSelectedItem().toString(),
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    Integer.parseInt(noOfStudySessions.getText().toString()),
                    classLocation.getText().toString(),
                    description.getText().toString());

            // System.out.println(startTime.getText().toString()+" "+  endTime.getText().toString());


            //Change to delete unwanted records from attendance and studysessions table. -- Peeyush
            Map<String, Boolean> currentSchedule = new HashMap<String, Boolean>();
            currentSchedule.put("monday", Boolean.FALSE);
            currentSchedule.put("tuesday", Boolean.FALSE);
            currentSchedule.put("wednesday", Boolean.FALSE);
            currentSchedule.put("thursday", Boolean.FALSE);
            currentSchedule.put("friday", Boolean.FALSE);
            currentSchedule.put("saturday", Boolean.FALSE);
            currentSchedule.put("sunday", Boolean.FALSE);


            TableLayout layout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);

            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);

                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    classDay = (TextView) row.findViewById(R.id.newTagButton);
                    startTime = (EditText) row.findViewById(R.id.newStartTime);
                    endTime = (EditText) row.findViewById(R.id.newEndTime);

                    if (startTime.getText().toString() != null && endTime.getText().toString() != null) {

                        //databaseConnector.deleteOneSchedule(courseID.getText().toString());
//changed insertClassSchedule to upsertClassSchedule. Change the code in Database Connector --Priyanka
                        databaseConnector.upsertClassSchedule(courseID.getText().toString(),
                                classDay.getText().toString(), startTime.getText().toString(), endTime.getText().toString());
                        currentSchedule.put(classDay.getText().toString().toLowerCase(), true);
                    }
                }
            }
            //Check Attendance and StudySession Records -- Peeyush
            databaseConnector.open();
            Cursor attendanceCursor = databaseConnector.getAttendance(courseID.getText().toString());
            checkAttendanceRecords(courseID.getText().toString(), attendanceCursor, currentSchedule, databaseConnector);
            //Cursor studySessionsCursor = databaseConnector.getStudySessions(courseID.getText().toString());
            //checkStudySessionsRecords(courseID.getText().toString(),studySessionsCursor,currentSchedule,databaseConnector);

        } // end else

        //Intent addNewClass =
               // new Intent(MainActivity3.this, MainActivity1.class);
        //startActivity(addNewClass);
    } // end class saveClasses


    public void checkAttendanceRecords(String courseID, Cursor attendanceCursor, Map<String, Boolean> currentSchedule, DatabaseConnector databaseConnector) {
        System.out.println("In checkAttendanceRecords method");
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        Calendar c = Calendar.getInstance();
        LinkedHashMap<String, Integer> classDays = new LinkedHashMap<String, Integer>();
        classDays.put("sunday", 1);
        classDays.put("monday", 2);
        classDays.put("tuesday", 3);
        classDays.put("wednesday", 4);
        classDays.put("thursday", 5);
        classDays.put("friday", 6);
        classDays.put("saturday", 7);

        attendanceCursor.moveToFirst();
        for (int i = 0; i < attendanceCursor.getCount(); i++) {
            int dateIndex = attendanceCursor.getColumnIndex("Date");

            try {
                String stringDate = attendanceCursor.getString(dateIndex);
                dt1 = format1.parse(stringDate);
                c.setTime(dt1);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                for (Map.Entry<String, Integer> entryKey : classDays.entrySet()) {
                    if (entryKey.getValue().equals(dayOfWeek)) {
                        String key = entryKey.getKey();
                        if (!currentSchedule.get(key)) {
                            databaseConnector.deleteAttendance(courseID, stringDate);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            attendanceCursor.moveToNext();
        }
    }

    public void checkStudySessionsRecords(String courseID, Cursor studySessionsCursor, Map<String, Boolean> currentSchedule, DatabaseConnector databaseConnector) {
        System.out.println("In checkStudySessionsRecords method");
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Date dt1 = new Date();
        Calendar c = Calendar.getInstance();
        LinkedHashMap<String, Integer> classDays = new LinkedHashMap<String, Integer>();
        classDays.put("sunday", 1);
        classDays.put("monday", 2);
        classDays.put("tuesday", 3);
        classDays.put("wednesday", 4);
        classDays.put("thursday", 5);
        classDays.put("friday", 6);
        classDays.put("saturday", 7);


        studySessionsCursor.moveToFirst();
        for (int i = 0; i < studySessionsCursor.getCount(); i++) {
            int dateIndex = studySessionsCursor.getColumnIndex("Date");

            try {
                String stringDate = studySessionsCursor.getString(dateIndex);
                dt1 = format1.parse(stringDate);
                c.setTime(dt1);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                for (Map.Entry<String, Integer> entryKey : classDays.entrySet()) {
                    if (entryKey.getValue().equals(dayOfWeek)) {
                        String key = entryKey.getKey();
                        if (!currentSchedule.get(key)) {
                            databaseConnector.deleteStudySessions(courseID, stringDate);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            studySessionsCursor.moveToNext();
        }
    }

    private void DeleteClasses() {
        DatabaseConnector databaseConnector = new DatabaseConnector(this);


        // insert the class information into the database
        databaseConnector.deleteClasses(

                courseID.getText().toString());


        Intent addNewClass =
                new Intent(MainActivity3.this, MainActivity1.class);
        startActivity(addNewClass);
        System.out.println("Deleting ");

    } // end class deleteClasses


    public View.OnClickListener deleteButtonClicked = new View.OnClickListener() {
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity3.this);

        @Override
        public void onClick(View v) {

            timeDisplayTableLayout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);
            TableRow buttonTableRow = (TableRow) v.getParent();
            ((ViewManager) buttonTableRow.getParent()).removeView(buttonTableRow);

            TextView newTagButton = (TextView) buttonTableRow.findViewById(R.id.newTagButton);

            String tag = newTagButton.getText().toString();

            databaseConnector.open();
            databaseConnector.deleteSchedule(courseID.getText().toString(), tag);
            switch (tag) {
                case "Monday":
                    mondayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Tuesday":
                    tuesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Wednesday":
                    wednesdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Thursday":
                    thursdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Friday":
                    fridayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Saturday":
                    saturdayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;
                case "Sunday":
                    sundayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mybuttons1));
                    break;

            }
            if(endCount>0)
                endCount--;
            if(editCount>0)
                editCount--;  //Rest the number of edit time fields to make sure auto populate works fine
            System.out.println("Deleting" + courseID.getText().toString() + tag);

        } // end else
    }; // end method onClick


    public void makeClassTag(final String classDay, String startTime, String endTime) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        timeDisplayTableLayout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);
        // inflate new_tag_view.xml to create new tag and edit Buttons



        View newTagView = inflater.inflate(R.layout.time_layout_edit, null);
        //System.out.println("Row count: "+ timeDisplayTableLayout.getChildCount());
        timeDisplayTableLayout.addView(newTagView, timeDisplayTableLayout.getChildCount());
        for( int i = 0; i < ((ViewGroup)newTagView).getChildCount(); i++ ) {
            if (((ViewGroup)newTagView).getChildAt(i) instanceof EditText) {
                myEditTextList.add((EditText) ((ViewGroup)newTagView).getChildAt(i));
            }
        }
        day = (TextView) newTagView.findViewById(R.id.newTagButton);
        startTimeText = (EditText) newTagView.findViewById(R.id.newStartTime);
        if (!startTimeValue.isEmpty())
            startTimeText.setText(startTimeValue);
        if (startTime != null && !startTime.isEmpty())
            startTimeText.setText(startTime);
        startTimeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final EditText stText = (EditText) v;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(!myEditTextList.isEmpty() && editCount == 0){
                            for(EditText ed: myEditTextList){
                                if(ed.getId() == R.id.newStartTime){
                                    ed.setText(selectedHour + ":" + selectedMinute);
                                }
                            }
                            editCount++;
                        }
                        stText.setText(selectedHour + ":" + selectedMinute);
                        if (startTimeValue.isEmpty())
                            startTimeValue = stText.getText().toString();
                        else if (startTimeValue != stText.getText().toString())
                            startTimeValue = "";
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        endTimeText = (EditText) newTagView.findViewById(R.id.newEndTime);
        if (!endTimeValue.isEmpty())
            endTimeText.setText(endTimeValue);
        if (endTime != null && !endTime.isEmpty())
            endTimeText.setText(endTime);
        endTimeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final EditText etText = (EditText) v;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etText.setText(selectedHour + ":" + selectedMinute);
                        if(!myEditTextList.isEmpty() && endCount == 0){
                            for(EditText ed: myEditTextList){
                                if(ed.getId() == R.id.newEndTime){
                                    ed.setText(selectedHour + ":" + selectedMinute);
                                }
                            }
                            endCount++;
                        }
                        if (endTimeValue.isEmpty())
                            endTimeValue = etText.getText().toString();
                        else if (endTimeValue != etText.getText().toString())
                            endTimeValue = "";
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        day.setText(classDay);
        //startTimeText.setText(startTime);
        //endTimeText.setText(endTime);


        Button deleteButton = (Button) newTagView.findViewById(R.id.newDeleteButton);
        deleteButton.setOnClickListener(deleteButtonClicked);

        //Code to sort the weekdays of the class in the view
        List<TableRow> tableRowList = new ArrayList<TableRow>();
        LinkedHashMap<String, Integer> weekDaysHashMap = populateHashMap();
        for (int i = 0; i < timeDisplayTableLayout.getChildCount(); i++) {
            View view = timeDisplayTableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                tableRowList.add(row);
            }
        }
        List<TableRow> sortedList = new ArrayList<TableRow>();
        for (String key : weekDaysHashMap.keySet()) {
            for (int i = 0; i < tableRowList.size(); i++) {
                TableRow row = tableRowList.get(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    View view = row.getChildAt(j);
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
        timeDisplayTableLayout.removeAllViews();
        for (int i = 0; i < sortedList.size(); i++) {
            timeDisplayTableLayout.addView(sortedList.get(i), i);
        }
        //End of code to sort the weekdays of the class in the view

    }


    public void addClassTime(String classDay) {
        DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity3.this);
        databaseConnector.open();
        Cursor classCursor = databaseConnector.getOneSchedule(courseID.getText().toString(), classDay);

        classCursor.moveToFirst();

        System.out.println("OnClick Add Class");
        if (classCursor.getCount() == 0) {
            //added a check for course ID to be entered to add schedule for the classes -- Priyanka
            if (courseID.getText().length() != 0) {
                makeClassTag(classDay, "", "");
                databaseConnector.insertClassSchedule(courseID.getText().toString(), classDay, null, null);

                switch (classDay) {
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
            } else {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity3.this);

                // set dialog title & message, and provide Button to dismiss
                builder.setTitle("Enter Course ID");
                builder.setMessage("Enter a valid Course ID");
                builder.setPositiveButton(R.string.errorButton, null);
                builder.show(); // display the Dialog
            }
        }
        classCursor.close();
        databaseConnector.close();

    }

    //in rare scenarios ehn the user presses the back button and if the values are deleted from the start time and end time, delete the values from the database. -- Priyanka
    @Override
    public void onBackPressed() {
        TableLayout layout = (TableLayout) findViewById(R.id.timeDisplayTableLayout);
        for (int i = 0; i < layout.getChildCount(); i++) {

            View child = layout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                classDay = (TextView) row.findViewById(R.id.newTagButton);
                startTime = (EditText) row.findViewById(R.id.newStartTime);
                endTime = (EditText) row.findViewById(R.id.newEndTime);


                if (startTime.getText().length() == 0 || endTime.getText().length() == 0) {
                    DatabaseConnector databaseConnector = new DatabaseConnector(MainActivity3.this);
                    databaseConnector.open();
                    databaseConnector.deleteSchedule(courseID.getText().toString(), classDay.getText().toString());
                    databaseConnector.close();
                }


            }
        }
        NavUtils.navigateUpFromSameTask(MainActivity3.this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.monday1:
                addClassTime("Monday");
                //classesViewMethod();
                break;


            case R.id.tuesday1:
                addClassTime("Tuesday");
                break;

            case R.id.wednesday1:
                addClassTime("Wednesday");
                break;

            case R.id.thursday1:
                addClassTime("Thursday");
                break;

            case R.id.friday1:
                addClassTime("Friday");
                break;

            case R.id.saturday1:
                addClassTime("Saturday");
                break;

            case R.id.sunday1:
                addClassTime("Sunday");
                break;

            case R.id.semesterStartEdit:
                startDatePickerDialog.show();
                break;

            case R.id.semesterEndEdit:
                endDatePickerDialog.show();
                break;

        }
    }

    private LinkedHashMap<String, Integer> populateHashMap() {
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

