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

    ArrayList<String> types;
    ArrayList<String> hosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bans_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Ban List");

        Set<String> defaultSet = new HashSet<>();
        final SharedPreferences blocked_types = bans_screen.this.getSharedPreferences("blocked_types", Context.MODE_PRIVATE);
        types = new ArrayList<>(blocked_types.getStringSet("types", defaultSet));
        final SharedPreferences blocked_hosts = bans_screen.this.getSharedPreferences("blocked_hosts", Context.MODE_PRIVATE);
        hosts = new ArrayList<>(blocked_hosts.getStringSet("hosts", defaultSet));


        final ArrayAdapter host_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, hosts);
        final ArrayAdapter type_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, types);

        final ListView list_Hosts = (ListView) findViewById(R.id.lstHosts);
        final ListView list_Types = (ListView) findViewById(R.id.lstTypes);
        list_Hosts.setVisibility(View.VISIBLE);
        list_Types.setVisibility(View.INVISIBLE);
        list_Hosts.setAdapter(host_adapter);
        list_Types.setAdapter(type_adapter);
        list_Hosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
                                host_adapter.clear();
                                host_adapter.addAll(hosts);
                                host_adapter.notifyDataSetChanged();
                                list_Hosts.setAdapter(host_adapter);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        list_Types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(bans_screen.this)
                        .setTitle("Un-Ban Type")
                        .setMessage("Would you like to remove the ban on type " + types.get(position) + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                types.remove(position);
                                Set updated_List = new HashSet(types);
                                SharedPreferences.Editor editor = blocked_types.edit();
                                editor.putStringSet("types", updated_List);
                                editor.commit();
                                new AlertDialog.Builder(bans_screen.this)
                                        .setTitle("Un-Banned")
                                        .setMessage("The type has been un-banned.")
                                        .setPositiveButton("OK", null)
                                        .show();
                                type_adapter.clear();
                                type_adapter.addAll(types);
                                type_adapter.notifyDataSetChanged();
                                list_Types.setAdapter(type_adapter);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
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
        ListView list_Hosts = (ListView) findViewById(R.id.lstHosts);
        ListView list_Types = (ListView) findViewById(R.id.lstTypes);
        ArrayAdapter type_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, types);
        ArrayAdapter host_adapter = new ArrayAdapter(bans_screen.this, android.R.layout.simple_list_item_1, hosts);
        Button blocker = (Button) findViewById(R.id.btnBlock);
        TextView header = (TextView) findViewById(R.id.lblBlock);
        if (list_Hosts.getVisibility() == View.VISIBLE){
            blocker.setText("Blocked Hosts");
            header.setText("Blocked Types");
            list_Types.setAdapter(type_adapter);
            list_Hosts.setVisibility(View.INVISIBLE);
            list_Types.setVisibility(View.VISIBLE);
        }else if (list_Types.getVisibility() == View.VISIBLE){
            blocker.setText("Blocked Types");
            header.setText("Blocked Hosts");
            list_Hosts.setAdapter(host_adapter);
            list_Hosts.setVisibility(View.VISIBLE);
            list_Types.setVisibility(View.INVISIBLE);
        }
    }
}

