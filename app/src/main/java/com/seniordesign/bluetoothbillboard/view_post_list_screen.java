package com.seniordesign.bluetoothbillboard;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Vector;

public class view_post_list_screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_list_screen);
        //Dynamo_Interface Tests  (all successful so far)
        /*Dynamo_Interface.setCurrent_board("213411");
        Vector<Board> boards = Dynamo_Interface.getAll_board_information();
        Board myboard = Dynamo_Interface.getCurrent_board_info();
        Board myposts = Dynamo_Interface.getFiltered_posts(Long.parseLong(Dynamo_Interface.getCurrent_board()), "Posted");
        Post mypost = Dynamo_Interface.getSingle_post(myposts.getPosts().firstElement().getPost_ID());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_post_list_screen, menu);
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
}
