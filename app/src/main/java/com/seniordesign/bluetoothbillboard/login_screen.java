package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class login_screen extends AppCompatActivity {

    private int failed_attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Login");
        failed_attempts = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login_screen, menu);
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

    public void onClick_btnLogin(View view){

        TextView username = (TextView) findViewById(R.id.txtUsername);
        TextView password = (TextView) findViewById(R.id.txtPassword);
        TextView failure = (TextView) findViewById(R.id.lblFailure);
        CheckBox robot = (CheckBox) findViewById(R.id.ckbRobot);
        Button login_button = (Button) findViewById(R.id.btnLogin);

        ///////////      LOGIN BYPASS
        CheckBox bypass = (CheckBox) findViewById(R.id.ckbBypass);
        if (bypass.isChecked()){
            startActivity(new Intent(login_screen.this, view_queue_list_screen.class));
        }
        ///////////     END BYPASS

        if(!robot.isChecked()){
            new AlertDialog.Builder(login_screen.this)
                    .setTitle("Humans Only!")
                    .setMessage("Robots are not allowed to login!")
                    .setPositiveButton("OK",null)
                    .show();
            failed_attempts++;
        }else if (username.getText().equals("") || password.getText().equals("")){
            new AlertDialog.Builder(login_screen.this)
                    .setTitle("Need Input")
                    .setMessage("A username and password must be provided!")
                    .setPositiveButton("OK",null)
                    .show();
            failed_attempts++;
        }else{
            Moderator login_info = new Moderator();
            login_info.setModerator_ID(Long.toString(Dynamo_Interface.getCurrent_board_info().getModerator_ID()));
            login_info.setUsername(username.getText().toString());
            login_info.setPassword(password.getText().toString());
            if(Dynamo_Interface.verify_credentials(login_info)){
                Dynamo_Interface.remove_outdated();
                startActivity(new Intent(login_screen.this, view_queue_list_screen.class));
            }else{
                new AlertDialog.Builder(login_screen.this)
                        .setTitle("Invalid")
                        .setMessage("The username or password you have entered is incorrect.")
                        .setPositiveButton("OK",null)
                        .show();
                failed_attempts++;
            }
        }
        if (failed_attempts == 1){
            failure.setVisibility(View.VISIBLE);
            failure.setText("You have failed to login 1 time.");
        }else if (failed_attempts > 1 && failed_attempts < 5){
            failure.setVisibility(View.VISIBLE);
            failure.setText("You have failed to login " + failed_attempts + " times.");
        }else if (failed_attempts > 5){
            failure.setVisibility(View.VISIBLE);
            failure.setText("You have reached the login failure limit.");
            login_button.setVisibility(View.INVISIBLE);
        }
    }
}