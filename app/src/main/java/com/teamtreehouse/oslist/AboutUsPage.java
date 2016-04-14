package com.teamtreehouse.oslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class AboutUsPage extends ActionBarActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_page);
        Button contact = (Button) findViewById(R.id.contactButton);

        getSupportActionBar().setTitle("About");
         contact.setOnClickListener(AboutUsPage.this);


    }
public void sendEmail(String emailID){
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("plain/text");


    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailID });
    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
    intent.putExtra(Intent.EXTRA_TEXT, "mail body");
    startActivity(Intent.createChooser(intent, ""));
}


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.contactButton:
                sendEmail("info@BestPracticesToGraduate.org");
                break;

        }
    }




}
