package com.teamtreehouse.oslist;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity{

    private ListView mDrawerList;
    protected DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Button buttonClass;
    Button buttonTrackOutcome;
    Button PDFViewer;
    static int backButtonCount = 0;
    AppCompatActivity appCompatActivity;

    protected void onCreateDrawer() {
        //setContentView(R.layout.activity_class_activities);
        /*buttonClass = (Button)findViewById(iewById(R.id.outcomes);
        buttonTrackOutcome.setOnClickLisR.id.classes);
        buttonClass.setOnClickListener(this);
        buttonTrackOutcome = (Button)findVtener(this);
        PDFViewer = (Button) findViewById(R.id.activities);
        PDFViewer.setOnClickListener(this);*/

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerActivity);
        mActivityTitle = "Key Outcomes Tracker";
        getSupportActionBar().setTitle(mActivityTitle);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
 }
    /*@Override
    public void setContentView(@LayoutRes int layoutResID){
        super.setContentView(layoutResID);
        addDrawerItems();
        setupDrawer();

    }*/

    protected void addDrawerItems() {
        /*final TypedArray typedArray = getResources().obtainTypedArray(R.array.sections_icons);
        mDrawerList.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.drawer_list)
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                int resourceId = typedArray.getResourceId(position, 0);
                Drawable drawable = getResources().getDrawable(resourceId);
                ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                return v;
            }
        });*/
        DrawerItem drawerItem[] = new DrawerItem[]{
                new DrawerItem(R.drawable.icc_instructor,"Instructor Profile"),
                new DrawerItem(R.drawable.icc_study,"Classes and Study Sessions"),
                new DrawerItem(R.drawable.icc_tasks,"Class Activities"),
                new DrawerItem(R.drawable.icc_calendar,"Track Key Outcomes"),
                new DrawerItem(R.drawable.icc_student,"Student Profile"),
                new DrawerItem(R.drawable.icc_pdf,"Succeed in College"),
                new DrawerItem(R.drawable.icc_info,"About Us")

        };
        DrawerAdapter drawerAdapter = new DrawerAdapter(this,R.layout.list_view_row,drawerItem);
        /*String[] osArray = { "Instructor Profile","Classes and Study Sessions","Class Activities", "Key Outcomes","Student Profile","Info", "About Us", "Succeed in College" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);*/
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                mDrawerLayout.closeDrawers();
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(getApplicationContext(),InstructorProfile.class);
                        //intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(),MainActivity1.class);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(),ClassActivity.class);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(),MainActivity4.class);
                        //intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(),StudentProfile.class);
                        //intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent4);
                        break;
                    case 5:
                        viewPDFMethod();
                        break;
                    case 6:
                        Intent intent5 = new Intent(getApplicationContext(),InfoPage.class);
                        //intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent5);
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

    /*@Override
    public void onBackPressed()
    {

        if(backButtonCount >= 1)
        {
            MainActivity.this.finish();
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }*/


    protected void setupDrawer() {
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

    /*@Override
    protected void onResume(){
        super.onResume();
        mDrawerLayout.closeDrawer(mDrawerList);
    }*/
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
