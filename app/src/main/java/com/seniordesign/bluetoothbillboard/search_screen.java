package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

@SuppressWarnings("unchecked")
public class search_screen extends AppCompatActivity {

    Vector<Board> all_boards;
    Vector<Board> filtered_boards;
    ArrayAdapter board_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        android.support.v7.app.ActionBar title_Bar = getSupportActionBar();
        assert getSupportActionBar() != null;
        title_Bar.setTitle("Board Search");

        filtered_boards = new Vector<>();
        all_boards = Dynamo_Interface.getAll_board_information();
        final ListView board_list = (ListView) findViewById(R.id.lstBoards);
        TextView search_bar = (TextView) findViewById(R.id.txtSearch);
        search_bar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                board_adapter.clear();
                for (int i = 0; i < all_boards.size(); i ++){
                    if(all_boards.get(i).getBoard_Name().toLowerCase().contains(s.toString().toLowerCase())){
                        filtered_boards.add(all_boards.get(i));
                    }
                }
                board_adapter.notifyDataSetChanged();
            }
        });
        board_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, filtered_boards) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(filtered_boards.get(position).getBoard_Name());
                text2.setText(filtered_boards.get(position).getInstructions());
                text2.setSingleLine();
                return view;
            }
        };
        board_list.setAdapter(board_adapter);
        board_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Board selected_board = (filtered_boards.get(position));
                Dynamo_Interface.setCurrent_board(Long.toString(selected_board.getBoard_ID()));
                startActivity(new Intent(search_screen.this, view_post_list_screen.class));
            }
        });
        board_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Board info_board = filtered_boards.get(position);
                new AlertDialog.Builder(search_screen.this)
                        .setTitle(info_board.getBoard_Name())
                        .setMessage("Organization\n" + info_board.getOrganization() + "\nInstructions\n" + info_board.getInstructions())
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Device_Interface device_link = new Device_Interface(search_screen.this);
                                device_link.save_Board(info_board);
                            }
                        })
                        .show();
                return true;
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_screen, menu);
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
}