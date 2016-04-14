// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.teamtreehouse.oslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnector {
    // database name
    private static final String DATABASE_NAME = "BestPractices";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper

    // public constructor for DatabaseConnector
    public DatabaseConnector(Context context) {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } // end method open

    // close the database connection
    public void close() {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    // inserts a new contact in the database
    public void insertClassDetails(String className, String courseID, String instructor,
                                   String teachingAssistant, String StartDate, String EndDate,
                                   int NoOfStudySessions, String classLocation, String description) {
        open(); // open the database
        Cursor c = database.rawQuery("select CourseID from classes where CourseID = ?", new String[]{courseID});
        if (c.getCount() == 0) {
            ContentValues newClass = new ContentValues();
            newClass.put("ClassName", className);
            newClass.put("CourseID", courseID);
            newClass.put("Instructor", instructor);
            newClass.put("TeachingAssistant", teachingAssistant);
            newClass.put("StartDate", StartDate);
            newClass.put("EndDate", EndDate);
            newClass.put("NumberOfStudySessions", NoOfStudySessions);
            newClass.put("ClassLocation", classLocation);
            newClass.put("Description", description);

            database.insert("classes", null, newClass);
            System.out.println("Inside Insert");
            close(); // close the database
        }

    } // end method insertContact

    public void insertClassActivity(String courseID, String className, String activityType, String activityName, String dueDate, String desc, boolean isCompleted,
                                    String maxGrade, String grade) {
        open(); // open the database
        Cursor c = database.rawQuery("select ActivityID from class_activities where CourseID = ? and ClassName = ? and DueDate = ? and ActivityType = ? " +
                "and ActivityName = ?", new String[]{courseID,className,dueDate,activityType,activityName});
        if (c.getCount() == 0) {
            ContentValues newClass = new ContentValues();
            newClass.put("CourseID", courseID);
            newClass.put("ClassName", className);
            newClass.put("ActivityType", activityType);
            newClass.put("ActivityName", activityName);
            newClass.put("DueDate", dueDate);
            newClass.put("Description", desc);
            newClass.put("CompletedFlag", isCompleted);
            newClass.put("MaximumGrade", maxGrade);
            newClass.put("Grade", grade);
            database.insert("class_activities", null, newClass);
            close();
        }
    }

    public void insertInstructor(String insName, String location, String email, String phone, int isIns){
        open(); // open the database
        Cursor c = database.rawQuery("select Instructor_ID from instructor_profile where Name = ? and Office_Location = ? and Email_Address = ? and Phone_Number = ? ",
                 new String[]{insName,location,email,phone});
        if (c.getCount() == 0) {
            ContentValues newClass = new ContentValues();
            newClass.put("Name", insName);
            newClass.put("Office_Location", location);
            newClass.put("Email_Address", email);
            newClass.put("Phone_Number", phone);
            newClass.put("IsInstructor", isIns);
            database.insert("instructor_profile", null, newClass);
            close();
        }
    }

    public void insertStudent(String firstName, String lastName, String studentID, String schoolName, String description, String other) {
        open();
        ContentValues newStudent = new ContentValues();
        newStudent.put("First_Name", firstName);
        newStudent.put("Last_Name", lastName);
        newStudent.put("Student_ID", studentID);
        newStudent.put("School_Name", schoolName);
        newStudent.put("Description", description);
        newStudent.put("Other", other);
        database.insert("student_profile", null, newStudent);
        close();
    }

    public void updateClassActivity(String oldCourseID, String oldClassName, String oldDueDate, String oldActivityName, String courseID, String className, String activityType, String activityName, String dueDate, String desc, boolean isCompleted,
                                    String maxGrade, String grade) {
        ContentValues editClassActivity = new ContentValues();
        editClassActivity.put("CourseID", courseID);
        editClassActivity.put("ClassName", className);
        editClassActivity.put("ActivityType", activityType);
        editClassActivity.put("ActivityName", activityName);
        editClassActivity.put("DueDate", dueDate);
        editClassActivity.put("Description", desc);
        editClassActivity.put("CompletedFlag", isCompleted);
        editClassActivity.put("MaximumGrade", maxGrade);
        editClassActivity.put("Grade", grade);
        open();
        database.update("class_activities", editClassActivity, "CourseID=" + "\'" + oldCourseID + "\' AND DueDate=" + "\'" + oldDueDate + "' AND ActivityName=" + "\'" + oldActivityName + "' AND ClassName=" + "\'" + oldClassName + "'", null);
        close();
    }

    public void updateInstructor(String insName, String location, String email, String phone, int isIns){
        open(); // open the database
        ContentValues newClass = new ContentValues();
            newClass.put("Name", insName);
            newClass.put("Office_Location", location);
            newClass.put("Email_Address", email);
            newClass.put("Phone_Number", phone);
            newClass.put("IsInstructor", isIns);
            database.update("instructor_profile", newClass, "Name=" + "\'" + insName + "\' AND Office_Location=" + "\'" + location + "' AND Email_Address=" + "\'" + email + "' AND Phone_Number=" + "\'" + phone + "' AND IsInstructor = "+ isIns,null);
            close();
    }

    public void upsertClassSchedule(String courseID, String day,
                                    String startTime, String endTime) {
        ContentValues newClass = new ContentValues();
        newClass.put("CourseID", courseID);
        newClass.put("Day", day);
        newClass.put("StartTime", startTime);
        newClass.put("EndTime", endTime);


        open(); // open the database
        long val = database.insertWithOnConflict("schedule", null, newClass, SQLiteDatabase.CONFLICT_REPLACE);
        System.out.println("Inside Insert Schedule");
        System.out.println("CourseID:" + courseID + " DAY: " + day + " Returned Value: " + val);
        close(); // close the database
    } // end method insertContact
    //Insert Schedule for the course

    public void insertClassSchedule(String courseID, String day,
                                    String startTime, String endTime) {
        ContentValues newClass = new ContentValues();
        newClass.put("CourseID", courseID);
        newClass.put("Day", day);
        newClass.put("StartTime", startTime);
        newClass.put("EndTime", endTime);


        open(); // open the database
        database.insert("schedule", null, newClass);
        System.out.println("Inside Insert Schedule");
        close(); // close the database
    } // end method insertContact


    //Insert Schedule for the course

    // inserts a new contact in the database
    public void updateClasses(String oldCourseID, String className, String courseID, String instructor,
                              String teachingAssistant, String StartDate, String EndDate,
                              int NoOfStudySessions, String classLocation, String description) {
        ContentValues editClasses = new ContentValues();
        editClasses.put("ClassName", className);
        editClasses.put("CourseID", courseID);
        editClasses.put("Instructor", instructor);
        editClasses.put("TeachingAssistant", teachingAssistant);
        editClasses.put("StartDate", StartDate);
        editClasses.put("EndDate", EndDate);
        editClasses.put("NumberOfStudySessions", NoOfStudySessions);
        editClasses.put("ClassLocation", classLocation);
        editClasses.put("Description", description);
        open(); // open the database
        database.update("classes", editClasses, "CourseID=" + "\'" + oldCourseID + "\'", null);
        close(); // close the database
    } // end method updateContact


    // Updates class Schedule in the database
    public void updateClassSchedule(String courseID, String day,
                                    String startTime, String endTime) {
        ContentValues editClasses = new ContentValues();

        editClasses.put("CourseID", courseID);
        editClasses.put("Day", day);
        editClasses.put("StartTime", startTime);
        editClasses.put("EndTime", endTime);

        open(); // open the database
        database.update("schedule", editClasses, "CourseID=" + "\'" + courseID + "\' AND Day=" + "\'" + day + "'", null);
        close(); // close the database
    } // end method updateContact


    public void manageStudySessions(String courseID, String date, int completedSessions) {
        Cursor c = database.rawQuery("select CourseID from StudySessions where Date = ? and CourseID = ?", new String[]{date, courseID});
        if (c.getCount() > 0) {
            ContentValues editStudySessions = new ContentValues();
            editStudySessions.put("CourseID", courseID);
            editStudySessions.put("Date", date);
            editStudySessions.put("CompletedSessions", completedSessions);
            open(); // open the database

            database.update("StudySessions", editStudySessions, "CourseID = ? and Date = ?", new String[]{courseID, date});
            System.out.println("Inside Attendance Update");
            close();// close the database
        } else {
            ContentValues newClass = new ContentValues();
            newClass.put("CourseID", courseID);
            newClass.put("Date", date);
            newClass.put("CompletedSessions", completedSessions);

            open(); // open the database
            database.insert("StudySessions", null, newClass);
            System.out.println("Inside Insert Study Sessions Completed");
            close(); // close the database
        }
    }

    public void manageClassAttendance(String courseID, String date, Boolean isAttended, Boolean isCancelled) {
        System.out.println("Updated: " + courseID + " " + date + " " + isAttended + " " + isCancelled);
        Cursor c = database.rawQuery("select CourseID from attendances where Date = ? and CourseID = ?", new String[]{date, courseID});
        if (c.getCount() > 0) {
            ContentValues editClassAttendance = new ContentValues();
            editClassAttendance.put("CourseID", courseID);
            editClassAttendance.put("Date", date);
            editClassAttendance.put("IsAttended", isAttended);
            editClassAttendance.put("IsCancelled", isCancelled);
            open(); // open the database
            //database.update("attendances", editClassAttendance, "CourseID=" + "\'"+courseID+"\'", null);

            database.update("attendances", editClassAttendance, "CourseID = ? and Date = ?", new String[]{courseID, date});
            System.out.println("Inside Attendance Update");
            close(); // close the database
        } else {
            ContentValues newClass = new ContentValues();
            newClass.put("CourseID", courseID);
            newClass.put("Date", date);
            newClass.put("IsAttended", isAttended);
            newClass.put("IsCancelled", isCancelled);

            open(); // open the database
            database.insert("attendances", null, newClass);
            System.out.println("Inside Attendance Insert");
            close(); // close the database
        }
    }

    public void manageClassActivities(String courseID, String dueDate, String className, String activityName, boolean ischecked) {
        Cursor c = database.rawQuery("select ActivityID from class_activities where CourseID = ? and DueDate = ? and ClassName = ?", new String[]{courseID, dueDate, className});
        if (c.getCount() > 0) {
            ContentValues editClassActivity = new ContentValues();
            editClassActivity.put("CourseID", courseID);
            editClassActivity.put("DueDate", dueDate);
            editClassActivity.put("CompletedFlag", ischecked);
            editClassActivity.put("ClassName", className);
            editClassActivity.put("ActivityName", activityName);
            open(); // open the database
            //database.update("attendances", editClassAttendance, "CourseID=" + "\'"+courseID+"\'", null);

            database.update("class_activities", editClassActivity, "CourseID = ? and DueDate = ? and ClassName = ? and ActivityName = ?", new String[]{courseID, dueDate, className, activityName});
            close(); // close the database
        } else {
            ContentValues newClass = new ContentValues();
            newClass.put("CourseID", courseID);
            newClass.put("DueDate", dueDate);
            newClass.put("CompletedFlag", ischecked);
            newClass.put("ClassName", className);
            newClass.put("ActivityName", activityName);

            open(); // open the database
            database.insert("class_activities", null, newClass);
        }
    }

    public Cursor getStudySessions(String courseID) {
        open();
        return database.rawQuery("select CourseID,Date,CompletedSessions from StudySessions where CourseID = ?", new String[]{courseID});
    } // end method getAllContacts


    // return a Cursor with all contact information in the database
    public Cursor getAllClasses() {
        return database.query("classes", null,
                null, null, null, null, "ClassName");
    } // end method getAllContacts

    public Cursor getStudent() {
        return database.rawQuery("select * from student_profile", null);
    }

    public Cursor getAllClassActivities() {
        return database.query("class_activities", null,
                null, null, null, null, "ClassName");
    }
    public Cursor getAllInstructors() {
        return database.query("instructor_profile", null,
                "IsInstructor = 1", null, null, null, "Name");
    }
    public Cursor getIfInstructor(String name, String location, String email, String PhoneNumber){
        return database.rawQuery("Select IsInstructor from instructor_profile where Name= ? AND Office_Location= ? AND Email_Address= ? AND Phone_Number= ?",new String[]{name,location,email,PhoneNumber});
    }
    public Cursor getAllTA() {
        return database.query("instructor_profile", null,
                "IsInstructor = 0", null, null, null, "Name");
    }
    // get a Cursor containing all information about the contact specified
    // by the given id
    public Cursor getOneClass(String courseID) {
        return database.query(
                "classes", null, "CourseID=" + "\'" + courseID + "\'", null, null, null, null, null);
    } // end method getOnContact

    public Cursor getOneClassByClassName(String className) {
        return database.query(
                "classes", null, "ClassName=" + "\'" + className + "\'", null, null, null, null, null);
    }
    public Cursor getOneClassActivity(String courseID, String dueDate, String className) {
        return database.query(
                "class_activities", null, "CourseID=" + "\'" + courseID + "\' AND DueDate=" + "\'" + dueDate + "' AND ClassName=" + "\'" + className + "'", null, null, null, null, null);
    } // end method getOnContact

    public Cursor getOneInstructor(String name, String location, String email, String PhoneNumber) {
        return database.query(
                "instructor_profile", null, "Name=" + "\'" + name + "\' AND Office_Location=" + "\'" + location + "' AND Email_Address=" + "\'" + email + "' AND Phone_Number=" + "\'" + PhoneNumber + "'", null, null, null, null, null);
    }
    public Cursor getOneInstructorByName(String name) {
        return database.query(
                "instructor_profile", null, "Name=" + "\'" + name +"' AND IsInstructor = 1", null, null, null, null, null);
    }
    public Cursor getOneTAByName(String name) {
        return database.query(
                "instructor_profile", null, "Name=" + "\'" + name +"' AND IsInstructor = 0", null, null, null, null, null);
    }
    public Cursor getOneSchedule(String courseID, String day) {
        return database.query(
                "schedule", null, "CourseID=" + "\'" + courseID + "\' AND Day=" + "\'" + day + "'", null, null, null, null, null);
    } // end method getOnContact

    public Cursor getCourseIDByClassName(String className) {
        return database.rawQuery("select CourseID from classes where ClassName = ?", new String[]{className});
    }
    public Cursor getClassNameByCourseID(String courseID) {
        return database.rawQuery("select ClassName from classes where CourseID = ?", new String[]{courseID});
    }
    public Cursor getClassSchedules(String courseID)//Priyanka
    {
        return database.query(
                "schedule", null, "CourseID=" + "\'" + courseID + "\'", null, null, null, null, null);
    } // end method getOnContact

    public Cursor getClassesforSelectedDate(String selectedDay) {
        return database.rawQuery("select ClassName, c.CourseID, Instructor, TeachingAssistant, StartDate, EndDate, NumberOfStudySessions, ClassLocation, Description, StartTime,EndTime from classes c join schedule s on c.CourseID = s.CourseID where s.Day = ?", new String[]{selectedDay});
    }

    public Cursor getClassActivitiesforSelectedDate(String selectedDay) {
        String q = "select * from class_activities where DueDate = " + "\'" + selectedDay + "\'";
        return database.rawQuery(q, null);
    }

    public Cursor getAttendance(String courseID) {
        return database.rawQuery("select CourseID, Date, IsAttended , IsCancelled from attendances where CourseID = ?", new String[]{courseID});
    }
    public Cursor getCourseID(){
        return database.rawQuery("select CourseID from classes",null);
    }
    public Cursor getInstructor(){
        return database.rawQuery("select Name from instructor_profile where IsInstructor = 1",null);
    }
    public Cursor getTA(){
        return database.rawQuery("select Name from instructor_profile where IsInstructor = 0",null);
    }
    public Cursor getClassNames(){
        return database.rawQuery("select ClassName from classes",null);
    }

    public Cursor getAttendanceByDate(String courseID, String date) {
        return database.rawQuery("select CourseID, Date, IsAttended, IsCancelled from attendances where CourseID = ? and Date = ?", new String[]{courseID, date});
    }

    public Cursor getActivityIDByCourseID(String courseID) {
        String q = "Select ActivityID from class_activities where CourseID = '" + courseID + "'";
        return database.rawQuery(q, null);
    }

    public Cursor getActivityNameByActivityID(int activityID) {
        String q = "Select ActivityName from class_activities where ActivityID = " + activityID;
        return database.rawQuery(q, null);
    }

    /* //Peeyush
    public Cursor getAllSchedules(String courseID)
    {
        return database.rawQuery("select CourseID, Day, StartTime, EndTime from schedule where CourseID = ?",new String[]{courseID});
    }

    */
    // delete the contact specified by the given String name
    public void deleteClasses(String courseID) {
        database.delete("classes", "CourseID=" + "\'" + courseID + "'", null);
        database.delete("schedule", "CourseID=" + "\'" + courseID + "'", null);
        database.delete("attendances", "CourseID=" + "\'" + courseID + "'", null);
        database.delete("StudySessions", "CourseID=" + "\'" + courseID + "'", null);
    } // end method deleteContact

    public void deleteClassActivity(String courseID, String dueDate, String className) { // open the database
        database.delete("class_activities", "CourseID=" + "\'" + courseID + "\' AND DueDate=" + "\'" + dueDate + "' AND ClassName=" + "\'" + className + "'", null);
        // close the database
    }
    public void deleteInstructor(String name, String location, String email, String phone) { // open the database
        database.delete("instructor_profile", "Name=" + "\'" + name + "\' AND Office_Location = " + "\'" + location + "' AND Email_Address = " + "\'" + email + "' AND Phone_Number = "+ "\'"  + phone + "'", null);
        // close the database
    }
    public void deleteOneSchedule(String courseID) {
        open(); // open the database
        database.delete("schedule", "CourseID=" + "\'" + courseID, null);
        close(); // close the database
    }

    // delete the contact specified by the given String name
    public void deleteSchedule(String courseID, String day) {
        open(); // open the database
        database.delete("schedule", "CourseID=" + "\'" + courseID + "\' AND Day=" + "\'" + day + "'", null);
        close(); // close the database
    } // end method deleteContact

    public void deleteAttendance(String courseID, String date) {
        open(); // open the database
        database.delete("attendances", "CourseID=" + "\'" + courseID + "\' AND Date=" + "\'" + date + "'", null);
        close(); // close the database
    }

    public void deleteStudySessions(String courseID, String date) {
        open(); // open the database
        database.delete("StudySessions", "CourseID=" + "\'" + courseID + "\' AND Date=" + "\'" + date + "'", null);
        close(); // close the database
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  CursorFactory factory, int version) {
            super(context, name, factory, version);
        } // end DatabaseOpenHelper constructor

        // creates the contacts table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {
            // query to create a new table named contacts
            String createQuery = "CREATE TABLE classes" +
                    "(ClassName TEXT, CourseID TEXT primary key, Instructor TEXT," +
                    "TeachingAssistant TEXT, StartDate TEXT, EndDate TEXT," +
                    "NumberOfStudySessions INTEGER, ClassLocation TEXT,Description TEXT);";


            String createSchedule = "CREATE TABLE schedule" +
                    "(CourseID TEXT, Day TEXT, StartTime TEXT, EndTime TEXT, PRIMARY KEY(CourseID,Day)," +
                    " FOREIGN KEY(CourseID) REFERENCES classes(CourseID));";

            String createAttendanceQuery = "CREATE TABLE attendances" +
                    "(CourseID TEXT, Date TEXT, IsAttended BOOLEAN, IsCancelled BOOLEAN, PRIMARY KEY(CourseID,Date )" +
                    "FOREIGN KEY(CourseID) REFERENCES classes(CourseID));";

            String classActivitiesQuery = "CREATE TABLE `class_activities` (\n" +
                    "\t`ActivityID`\tINTEGER NOT NULL,\n" +
                    "\t`CourseID`\tTEXT,\n" +
                    "\t`ClassName`\tTEXT,\n" +
                    "\t`ActivityType`\tTEXT,\n" +
                    "\t`ActivityName`\tBLOB,\n" +
                    "\t`DueDate`\tTEXT NOT NULL,\n" +
                    "\t`Description`\tTEXT,\n" +
                    "\t`MaximumGrade`\tBLOB,\n" +
                    "\t`CompletedFlag`\tBOOLEAN,\n" +
                    "\t`Grade`\tTEXT,\n" +
                    "\tPRIMARY KEY(ActivityID),\n" +
                    "\tFOREIGN KEY(`CourseID`) REFERENCES `classes`(`CourseID`)\n" +
                    ");";

            String instructorQuery = "CREATE TABLE `instructor_profile` (\n" +
                    "\t`Instructor_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`Name`\tTEXT NOT NULL,\n" +
                    "\t`Office_Location`\tTEXT,\n" +
                    "\t`Email_Address`\tTEXT,\n" +
                    "\t`Phone_Number`\tTEXT,\n" +
                    "\t`IsInstructor`\tINTEGER\n" +
                    ");";
            String createStudySessionsQuery = "CREATE TABLE StudySessions" +
                    "(CourseID TEXT , Date TEXT, CompletedSessions INT);" +
                    "FOREIGN KEY(CourseID) REFERENCES classes(CourseID));";

            String createStudentQuery = "CREATE TABLE `student_profile` (\n" +
                    "\t`Student_ID`\tTEXT NOT NULL,\n" +
                    "\t`First_Name`\tTEXT NOT NULL,\n" +
                    "\t`Last_Name`\tTEXT,\n" +
                    "\t`School_Name`\tTEXT,\n" +
                    "\t`Description`\tTEXT,\n" +
                    "\t`Other`\tTEXT,\n" +
                    "\tPRIMARY KEY(Student_ID)\n" +
                    ");";
            db.execSQL(createQuery); // execute the query
            db.execSQL(createSchedule);
            db.execSQL(instructorQuery);
            db.execSQL(classActivitiesQuery);
            db.execSQL(createAttendanceQuery);
            db.execSQL(createStudentQuery);
            db.execSQL(createStudySessionsQuery);


        } // end method onCreate


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS classes");
            db.execSQL("DROP TABLE IF EXISTS schedule");
            db.execSQL("DROP TABLE IF EXISTS lookUpWeekDays");
            db.execSQL("DROP TABLE IF EXISTS attendances");
            db.execSQL("DROP TABLE IF EXISTS instructor_profile");
            db.execSQL("DROP TABLE IF EXISTS lookUpWeekDays");
            db.execSQL("DROP TABLE IF EXISTS StudySessions");
            db.execSQL("DROP TABLE IF EXISTS class_activities");
            db.execSQL("DROP TABLE IF EXISTS student_profile");
            onCreate(db);

        } // end method onUpgrade
    } // end class DatabaseOpenHelper
} // end class DatabaseConnector

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 * *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/
