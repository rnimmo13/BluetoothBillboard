package com.seniordesign.bluetoothbillboard;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class view_queued_post_screen extends AppCompatActivity {

    Post viewing_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_queued_post_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Queued Post");

        viewing_post = Dynamo_Interface.getSelected_post();
        CheckBox check_details = (CheckBox) findViewById(R.id.ckbDetails);
        final ScrollView detail_view = (ScrollView) findViewById(R.id.scvDetails);
        check_details.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    detail_view.setVisibility(View.VISIBLE);
                }
                else {
                    detail_view.setVisibility(View.INVISIBLE);
                }
            }
        });
        TextView host_name = (TextView) findViewById(R.id.lblHost);
        TextView post_details = (TextView) findViewById(R.id.txvDetails);
        TextView email = (TextView) findViewById(R.id.txtEmail);
        TextView phone = (TextView) findViewById(R.id.txtPhone);
        TextView address = (TextView) findViewById(R.id.txtAddress);
        host_name.setText(viewing_post.getHost());
        post_details.setText(viewing_post.getInformation());
        email.setText(viewing_post.getEmail());
        phone.setText(Long.toString(viewing_post.getPhone()));
        address.setText(viewing_post.getAddress());
        //handle html
        WebView web_viewer = (WebView) findViewById(R.id.wbvWeb);
        String stripped = viewing_post.getInformation().replaceAll("<(.|\n)*?>", "");
        if (stripped.equals(viewing_post.getInformation())) {
            post_details.setText(viewing_post.getInformation());
            post_details.setMovementMethod(new ScrollingMovementMethod());
            ((ViewGroup) web_viewer.getParent()).removeView(web_viewer);
        }else{
            web_viewer.loadData(viewing_post.getInformation(), "text/html", null);
            ((ViewGroup) post_details.getParent()).removeView(post_details);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_queued_post_screen, menu);
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

    public void onClick_btnDeny(View view){
        viewing_post.setPost_Status("Denied");
        Device_Interface device_link = new Device_Interface(view_queued_post_screen.this);
        device_link.save_Post(viewing_post);

        if (!viewing_post.getEmail().equals(" ")) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, viewing_post.getEmail());
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your post has been denied");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your post:\n" + viewing_post.getInformation() + "\n Has been denied.");
            try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(view_queued_post_screen.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(view_queued_post_screen.this, view_queue_list_screen.class));
        }
    }

    public void onClick_btnAccept(View view){
        viewing_post.setPost_Status("Posted");
        Device_Interface device_link = new Device_Interface(view_queued_post_screen.this);
        device_link.save_Post(viewing_post);

        if (!viewing_post.getEmail().equals(" ")) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, viewing_post.getEmail());
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your post has been approved");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your post:\n" + viewing_post.getInformation() + "\n Has been approved.");
            try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(view_queued_post_screen.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(view_queued_post_screen.this, view_queue_list_screen.class));
        }
    }
}
