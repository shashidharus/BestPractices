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

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "BestPractices";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper; // database helper

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } // end DatabaseConnector constructor

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } // end method open

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } // end method close

   // inserts a new contact in the database
   public void insertClassDetails(String className ,String courseID,String instructor,
                                  String teachingAssistant,String StartDate, String EndDate,
                                  int NoOfStudySessions, String classLocation,String description)
   {
       open(); // open the database
       Cursor c = database.rawQuery("select CourseID from classes where CourseID = ?",new String[]{courseID});
       if (c.getCount() == 0)
       {
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


    public void upsertClassSchedule(String courseID,String day,
                                    String startTime,String endTime)
    {
        ContentValues newClass = new ContentValues();
        newClass.put("CourseID", courseID);
        newClass.put("Day", day);
        newClass.put("StartTime", startTime);
        newClass.put("EndTime", endTime);


        open(); // open the database
        long val = database.insertWithOnConflict("schedule", null, newClass, SQLiteDatabase.CONFLICT_REPLACE);
        System.out.println("Inside Insert Schedule");
        System.out.println("CourseID:"+courseID + " DAY: "+day + " Returned Value: "+val);
        close(); // close the database
    } // end method insertContact
    //Insert Schedule for the course

    public void insertClassSchedule(String courseID,String day,
                                   String startTime,String endTime)
    {
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
   public void updateClasses(String className ,String courseID,String instructor,
                             String teachingAssistant,String StartDate, String EndDate,
                             int NoOfStudySessions, String classLocation,String description)
   {
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
      database.update("classes", editClasses, "CourseID=" + "\'"+courseID+"\'", null);
      close(); // close the database
   } // end method updateContact


    // Updates class Schedule in the database
    public void updateClassSchedule(String courseID,String day,
                              String startTime,String endTime)
    {
        ContentValues editClasses = new ContentValues();

        editClasses.put("CourseID", courseID);
        editClasses.put("Day", day);
        editClasses.put("StartTime", startTime);
        editClasses.put("EndTime", endTime);

        open(); // open the database
        database.update("schedule", editClasses, "CourseID=" + "\'"+courseID+"\' AND Day="+ "\'"+day+"'", null);
        close(); // close the database
    } // end method updateContact



    public void manageStudySessions(String courseID, String date,int completedSessions)
    {
        Cursor c = database.rawQuery("select CourseID from StudySessions where Date = ? and CourseID = ?",new String[]{date,courseID});
        if (c.getCount() > 0)
        {
            ContentValues editStudySessions = new ContentValues();
            editStudySessions.put("CourseID", courseID);
            editStudySessions.put("Date", date);
            editStudySessions.put("CompletedSessions", completedSessions);
            open(); // open the database

            database.update("StudySessions", editStudySessions, "CourseID = ? and Date = ?", new String[]{courseID,date});
            System.out.println("Inside Attendance Update");
            close();// close the database
        }
        else
        {
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
    public void manageClassAttendance(String courseID, String date,Boolean isAttended, Boolean isCancelled)
    {
        System.out.println("Updated: "+ courseID+" "+ date+ " "+ isAttended+ " "+ isCancelled);
        Cursor c = database.rawQuery("select CourseID from attendances where Date = ? and CourseID = ?",new String[]{date,courseID});
        if (c.getCount() > 0)
        {
            ContentValues editClassAttendance = new ContentValues();
            editClassAttendance.put("CourseID", courseID);
            editClassAttendance.put("Date", date);
            editClassAttendance.put("IsAttended", isAttended);
            editClassAttendance.put("IsCancelled", isCancelled);
            open(); // open the database
            //database.update("attendances", editClassAttendance, "CourseID=" + "\'"+courseID+"\'", null);

            database.update("attendances", editClassAttendance, "CourseID = ? and Date = ?", new String[]{courseID,date});
            System.out.println("Inside Attendance Update");
            close(); // close the database
        }
        else
        {
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

    public Cursor getStudySessions(String courseID)
    {
        open();
        return database.rawQuery("select CourseID,Date,CompletedSessions from StudySessions where CourseID = ?",new String[]{courseID});
    } // end method getAllContacts



   // return a Cursor with all contact information in the database
   public Cursor getAllClasses()
   {
      return database.query("classes", null,
         null, null, null, null, "ClassName");
   } // end method getAllContacts

   // get a Cursor containing all information about the contact specified
   // by the given id
   public Cursor getOneClass(String courseID)
   {
      return database.query(
         "classes", null, "CourseID=" + "\'"+courseID+"\'", null, null, null, null, null);
   } // end method getOnContact

    public Cursor getOneSchedule(String courseID, String day)
    {
        return database.query(
                "schedule", null, "CourseID=" + "\'"+courseID+"\' AND Day="+ "\'"+day+"'", null, null, null, null, null);
    } // end method getOnContact

    public Cursor getClassSchedules(String courseID)//Priyanka
    {
        return database.query(
                "schedule", null, "CourseID=" + "\'"+courseID+"\'", null, null, null, null, null);
    } // end method getOnContact


    public Cursor getClassesforSelectedDate(String selectedDay)
    {
        return database.rawQuery("select ClassName, c.CourseID, Instructor, TeachingAssistant, StartDate, EndDate, NumberOfStudySessions, ClassLocation, Description, StartTime,EndTime from classes c join schedule s on c.CourseID = s.CourseID where s.Day = ?",new String[]{selectedDay});
    }
    public Cursor getAttendance(String courseID)
    {
        return database.rawQuery("select CourseID, Date, IsAttended , IsCancelled from attendances where CourseID = ?",new String[]{courseID});
    }
    public Cursor getAttendanceByDate(String courseID,String date)
    {
        return database.rawQuery("select CourseID, Date, IsAttended, IsCancelled from attendances where CourseID = ? and Date = ?", new String[]{courseID, date});
    }



    /* //Peeyush
    public Cursor getAllSchedules(String courseID)
    {
        return database.rawQuery("select CourseID, Day, StartTime, EndTime from schedule where CourseID = ?",new String[]{courseID});
    }

    */
   // delete the contact specified by the given String name
   public void deleteClasses(String courseID)
   {
      open(); // open the database
      database.delete("classes", "CourseID=" + "\'" + courseID + "'", null);
      database.delete("schedule","CourseID=" + "\'" + courseID + "'", null);
      database.delete("attendances","CourseID=" + "\'" + courseID + "'", null);
      database.delete("StudySessions","CourseID=" + "\'" + courseID + "'", null);
       close(); // close the database
   } // end method deleteContact



    public void deleteOneSchedule(String courseID)
    {
        open(); // open the database
        database.delete("schedule","CourseID=" + "\'"+courseID, null);
        close(); // close the database
    }
    // delete the contact specified by the given String name
    public void deleteSchedule(String courseID, String day)
    {
        open(); // open the database
        database.delete("schedule","CourseID=" + "\'"+courseID+"\' AND Day="+ "\'"+day+"'" , null);
        close(); // close the database
    } // end method deleteContact

    public void deleteAttendance(String courseID, String date)
    {
        open(); // open the database
        database.delete("attendances","CourseID=" + "\'"+courseID+"\' AND Date="+ "\'"+date+"'" , null);
        close(); // close the database
    }

    public void deleteStudySessions(String courseID, String date)
    {
        open(); // open the database
        database.delete("StudySessions","CourseID=" + "\'"+courseID+"\' AND Date="+ "\'"+date+"'" , null);
        close(); // close the database
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } // end DatabaseOpenHelper constructor

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
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



          String createStudySessionsQuery = "CREATE TABLE StudySessions" +
                  "(CourseID TEXT , Date TEXT, CompletedSessions INT);" +
                  "FOREIGN KEY(CourseID) REFERENCES classes(CourseID));";

                  
         db.execSQL(createQuery); // execute the query
         db.execSQL(createSchedule);

          db.execSQL(createAttendanceQuery);

          db.execSQL(createStudySessionsQuery);


      } // end method onCreate



      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
          db.execSQL("DROP TABLE IF EXISTS classes");
          db.execSQL("DROP TABLE IF EXISTS schedule");
          db.execSQL("DROP TABLE IF EXISTS lookUpWeekDays");
          db.execSQL("DROP TABLE IF EXISTS attendances");
          db.execSQL("DROP TABLE IF EXISTS lookUpWeekDays");
          db.execSQL("DROP TABLE IF EXISTS StudySessions");
          onCreate(db);

      } // end method onUpgrade
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
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
