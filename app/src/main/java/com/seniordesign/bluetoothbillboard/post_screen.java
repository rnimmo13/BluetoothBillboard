package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class post_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Submit Post");

        //spinner initialization
        String types[] = {"Event", "Announcement", "Employment", "Coupon", "Sales", "Services", "Other"};
        Spinner spinType = (Spinner) findViewById(R.id.spnType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapter);

        //date picker initialization
        final Calendar myCalendar = Calendar.getInstance();
        final TextView dateText = (TextView) findViewById(R.id.txtDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, dateText);
            }
        };
        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean focus){
                if (focus){
                    new DatePickerDialog(post_screen.this, date, myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(post_screen.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.view_post_action:
                startActivity(new Intent(this, view_post_list_screen.class));
                return true;
            case R.id.submit_post_action:
                startActivity(new Intent(this, post_screen.class));
                return true;
            case R.id.saved_post_action:
                startActivity(new Intent(this, saved_posts_screen.class));
                return true;
            case R.id.saved_board_action:
                startActivity(new Intent(this, saved_boards_screen.class));
                return true;
            case R.id.board_list_action:
                startActivity(new Intent(this, view_board_list_screen.class));
                return true;
            case R.id.login_action:
                startActivity(new Intent(this, login_screen.class));
                return true;
            case R.id.search_action:
                startActivity(new Intent(this, search_screen.class));
                return true;
            case R.id.ban_list_action:
                startActivity(new Intent(this, bans_screen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateLabel(Calendar myCalendar, TextView dateText) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean validateFields(){

        TextView host_text = (TextView) findViewById(R.id.txtHost);
        TextView detail_text = (TextView) findViewById(R.id.lblHost);
        CheckBox robot = (CheckBox) findViewById(R.id.ckbRobot);
        boolean acceptable;
        if (!robot.isChecked()){
            new AlertDialog.Builder(post_screen.this)
                    .setTitle("Humans Only!")
                    .setMessage("Robots are not allowed to submit posts!")
                    .setPositiveButton("OK",null)
                    .show();
            acceptable = false;
        }else if (host_text.getText().equals("")){
            new AlertDialog.Builder(post_screen.this)
                    .setTitle("Host Name")
                    .setMessage("Posts are filtered by host name, this field must be filled out.")
                    .setPositiveButton("OK",null)
                    .show();
            acceptable = false;
        }else if (detail_text.getText().equals("")){
            new AlertDialog.Builder(post_screen.this)
                    .setTitle("Post Details")
                    .setMessage("Posts must have information, otherwise why post them?")
                    .setPositiveButton("OK",null)
                    .show();
            acceptable = false;
        }else{
            acceptable = true;
        }
        return acceptable;
    }

    public void onClick_btnPreview(View view){

        if (validateFields()){
            Post submit_me = new Post();
            TextView host_text = (TextView) findViewById(R.id.txtHost);
            TextView detail_text = (TextView) findViewById(R.id.lblHost);
            TextView date_text = (TextView) findViewById(R.id.txtDate);
            TextView address_text = (TextView) findViewById(R.id.txtAddress);
            TextView phone_text = (TextView) findViewById(R.id.txtPhone);
            TextView email_text = (TextView) findViewById(R.id.txtEmail);
            Spinner type_spinner = (Spinner) findViewById(R.id.spnType);

            submit_me.setPost_Status("Queued");
            submit_me.setHost(host_text.getText().toString());
            submit_me.setInformation(detail_text.getText().toString());
            submit_me.setPost_Type(type_spinner.getSelectedItem().toString());
            if (address_text.getText().equals("")){
                submit_me.setAddress(" ");
            }else{
                submit_me.setAddress(address_text.getText().toString());
            }
            if (phone_text.getText().toString().length() == 10){
                submit_me.setPhone(Long.parseLong(phone_text.getText().toString()));
            }else{
                submit_me.setPhone(0);
            }
            if (email_text.getText().equals("")){
                submit_me.setEmail(" ");
            }else{
                submit_me.setEmail(email_text.getText().toString());
            }
            if (date_text.getText().equals("")){
                //set date to 90 days from today
                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                cal.add(Calendar.DATE, 90);
                Date futureDate = cal.getTime();
                //format date
                String myFormat = "MMddyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                submit_me.setEnd_Date(Long.parseLong(sdf.format(futureDate)));
            }else{
                String parsed_date = date_text.getText().toString();
                while (parsed_date.contains("/")){
                    parsed_date = parsed_date.replace("/","");
                }
                submit_me.setEnd_Date(Long.parseLong(parsed_date));
            }
            Random random_generator = new Random();
            int post_id = random_generator.nextInt(99999999 - 10000000) + 10000000;
            while(Dynamo_Interface.getSingle_post(post_id) != null){
                post_id = random_generator.nextInt(99999999 - 10000000) + 10000000;
            }
            submit_me.setPost_ID(post_id);
            Dynamo_Interface.setSelected_post(submit_me);
            startActivity(new Intent(post_screen.this, preview_post_screen.class));
        }
    }

    public void onClick_btnSubmit(View view){

        if (validateFields()){
            Post submit_me = new Post();
            TextView host_text = (TextView) findViewById(R.id.txtHost);
            TextView detail_text = (TextView) findViewById(R.id.lblHost);
            TextView date_text = (TextView) findViewById(R.id.txtDate);
            TextView address_text = (TextView) findViewById(R.id.txtAddress);
            TextView phone_text = (TextView) findViewById(R.id.txtPhone);
            TextView email_text = (TextView) findViewById(R.id.txtEmail);
            Spinner type_spinner = (Spinner) findViewById(R.id.spnType);

            submit_me.setPost_Status("Queued");
            submit_me.setHost(host_text.getText().toString());
            submit_me.setInformation(detail_text.getText().toString());
            submit_me.setPost_Type(type_spinner.getSelectedItem().toString());
            if (address_text.getText().equals("")){
                submit_me.setAddress(" ");
            }else{
                submit_me.setAddress(address_text.getText().toString());
            }
            if (phone_text.getText().toString().length() == 10){
                submit_me.setPhone(Long.parseLong(phone_text.getText().toString()));
            }else{
                submit_me.setPhone(0);
            }
            if (email_text.getText().equals("")){
                submit_me.setEmail(" ");
            }else{
                submit_me.setEmail(email_text.getText().toString());
            }
            if (date_text.getText().equals("")){
                //set date to 90 days from today
                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                cal.add(Calendar.DATE, 90);
                Date futureDate = cal.getTime();
                //format date
                String myFormat = "MMddyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                submit_me.setEnd_Date(Long.parseLong(sdf.format(futureDate)));
            }else{
                String parsed_date = date_text.getText().toString();
                while (parsed_date.contains("/")){
                    parsed_date = parsed_date.replace("/","");
                }
                submit_me.setEnd_Date(Long.parseLong(parsed_date));
            }
            Random random_generator = new Random();
            int post_id = random_generator.nextInt(99999999 - 10000000) + 10000000;
            while(Dynamo_Interface.getSingle_post(post_id) != null){
                post_id = random_generator.nextInt(99999999 - 10000000) + 10000000;
            }
            submit_me.setPost_ID(post_id);
            Dynamo_Interface.save_post(submit_me);
            startActivity(new Intent(post_screen.this, view_post_list_screen.class));
        }
    }
}