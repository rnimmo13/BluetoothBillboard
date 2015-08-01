package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class bans_screen extends AppCompatActivity {

    int list_type;
    ArrayList<String> types;
    ArrayList<String> hosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bans_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Ban List");

        list_type = 0;
        Set<String> defaultSet = new HashSet<>();
        final SharedPreferences blocked_types = bans_screen.this.getSharedPreferences("blocked_types", Context.MODE_PRIVATE);
        types = new ArrayList<>(blocked_types.getStringSet("types", defaultSet));
        final SharedPreferences blocked_hosts = bans_screen.this.getSharedPreferences("blocked_hosts", Context.MODE_PRIVATE);
        hosts = new ArrayList<>(blocked_hosts.getStringSet("hosts", defaultSet));


        ArrayAdapter block_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, hosts);
        final ListView lstBlocked = (ListView) findViewById(R.id.lstBlocks);
        lstBlocked.setAdapter(block_adapter);
        lstBlocked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (list_type == 0){    //blocked hosts
                    new AlertDialog.Builder(bans_screen.this)
                            .setTitle("Un-Ban Host")
                            .setMessage("Would you like to remove the ban on host " + hosts.get(position) + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    hosts.remove(position);
                                    Set updated_List = new HashSet(hosts);
                                    SharedPreferences.Editor editor = blocked_hosts.edit();
                                    editor.putStringSet("hosts", updated_List);
                                    editor.commit();
                                    new AlertDialog.Builder(bans_screen.this)
                                            .setTitle("Un-Banned")
                                            .setMessage("The host has been un-banned.")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                    onClick_btnBlock(lstBlocked);
                    onClick_btnBlock(lstBlocked);
                }else if (list_type == 1){  //blocked types
                    new AlertDialog.Builder(bans_screen.this)
                            .setTitle("Un-Ban Type")
                            .setMessage("Would you like to remove the ban on type " + types.get(position) + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    types.remove(position);
                                    Set updated_List = new HashSet(hosts);
                                    SharedPreferences.Editor editor = blocked_types.edit();
                                    editor.putStringSet("types", updated_List);
                                    editor.commit();
                                    new AlertDialog.Builder(bans_screen.this)
                                            .setTitle("Un-Banned")
                                            .setMessage("The type has been un-banned.")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                    onClick_btnBlock(lstBlocked);
                    onClick_btnBlock(lstBlocked);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bans_screen, menu);
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

    public void onClick_btnBlock(View view){
        ListView lstBlocked = (ListView) findViewById(R.id.lstBlocks);
        ArrayAdapter type_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, types);
        ArrayAdapter host_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, hosts);
        Button blocker = (Button) findViewById(R.id.btnBlock);
        TextView header = (TextView) findViewById(R.id.lblBlock);
        if (list_type == 0){
            blocker.setText("Blocked Hosts");
            header.setText("Blocked Types");
            lstBlocked.setAdapter(type_adapter);
            list_type = 1;
        }else if (list_type == 1){
            blocker.setText("Blocked Types");
            header.setText("Blocked Hosts");
            lstBlocked.setAdapter(host_adapter);
            list_type = 0;
        }
    }
}

