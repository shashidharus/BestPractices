package com.teamtreehouse.oslist;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity implements  OnClickListener{

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Button buttonClass;
    Button buttonTrackOutcome;
    Button PDFViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonClass = (Button)findViewById(R.id.classes);
        buttonClass.setOnClickListener(this);
        buttonTrackOutcome = (Button)findViewById(R.id.outcomes);
        buttonTrackOutcome.setOnClickListener(this);
        PDFViewer = (Button) findViewById(R.id.activities);
        PDFViewer.setOnClickListener(this);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "Key Outcomes Tracker";
        getSupportActionBar().setTitle(mActivityTitle);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
 }

    private void addDrawerItems() {
        String[] osArray = { "Info", "About Us" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                switch (position){
                    case 0:
                        startActivity(new Intent("com.teamtreehouse.oslist.InfoPage"));
                        break;
                    case 1:
                        startActivity(new Intent("com.teamtreehouse.oslist.AboutUsPage"));
                        break;

                }

            }
        });
    }
    @Override
    public void onDestroy()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

   /* @Override
    public void onBackPressed()
    {
        int backButtonCount = 0;
        if(backButtonCount >= 1)
        {
            //Intent intent = new Intent(Intent.ACTION_MAIN);
            //intent.addCategory(Intent.CATEGORY_HOME);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.classes:
                classesViewMethod();
                break;


            case R.id.outcomes:
                trackOutcomesViewMethod();
                break;

            case R.id.activities:

                viewPDFMethod();
                break;
        }
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {

        System.out.println("Copying");
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private  void classesViewMethod(){
        startActivity(new Intent("com.teamtreehouse.oslist.MainActivity1"));
    }

    private  void trackOutcomesViewMethod(){
        startActivity(new Intent("com.teamtreehouse.oslist.MainActivity4"));
    }

    private void viewPDFMethod(){


            System.out.println("Inside Try");

            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;
            File file = new File(getFilesDir(), "NoteFromStudents.pdf");
            try
            {
                in = assetManager.open("NoteFromStudents.pdf");
                out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e)
            {
                Log.e("tag", e.getMessage());
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.parse("file://" + getFilesDir() + "/NoteFromStudents.pdf"),
                    "application/pdf");

            startActivity(intent);



           /* copyFile(this.getAssets().open("NoteFromStudents.pdf"),
                    new FileOutputStream(new File(getFilesDir(), "android.resource://main/assets/NoteFromStudents.pdf")));
            File pdfFile = new File(getFilesDir(), "yourPath/NoteFromStudents.pdf");
            Uri path = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(path, "application/pdf");

            startActivity(intent);


*/

    }
}
