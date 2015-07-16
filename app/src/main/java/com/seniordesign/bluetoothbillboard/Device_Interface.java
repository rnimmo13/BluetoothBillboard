package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;

/*
    Creates and handles all local database interactions.

  Created by Eric_Anderson on 7/15/15.
 */

@SuppressWarnings({"unchecked", "unused"})
public class Device_Interface extends SQLiteOpenHelper{

    static Context application_Context;

    static final String database_Name = "Device Database";

    static final String post_Table = "Posts";
    static final String post_ID = "ID";
    static final String phone = "Phone_Number";
    static final String end_Date = "End_Date";
    static final String host_Name = "Host_Name";
    static final String email_Address = "EMail_Address";
    static final String address = "Address";
    static final String post_Information = "Information";
    static final String post_Status = "Status";
    static final String post_Type = "Type";

    static final String board_Table = "Boards";
    static final String board_ID = "ID";
    static final String board_Name = "Name";
    static final String organization = "Organization";
    static final String group_ID = "Group_ID";
    static final String instructions = "Instructions";
    static final String moderator_ID = "Moderator_ID";

    public void onCreate(SQLiteDatabase database){
        //setup the device database
        //post table
        database.execSQL("CREATE TABLE " + post_Table + " (" + post_ID + " LONG PRIMARY KEY , " + phone + " LONG , "
            + end_Date + " LONG , " + host_Name + " TEXT , " + email_Address + " TEXT , " + address + " TEXT , "
            + post_Information + " TEXT , " + post_Status + " TEXT , " + post_Type + " TEXT)");
        //board table
        database.execSQL("CREATE TABLE " + board_Table + " (" + board_ID + " LONG PRIMARY KEY , " + board_Name + " TEXT , "
            + organization + " TEXT , " + group_ID + " LONG , " + instructions + " TEXT , " + moderator_ID + " LONG)");
    }

    public void onUpgrade(SQLiteDatabase database, int old_Version, int current_Version){
        //allow database upgrades
        database.execSQL("DROP TABLE IF EXISTS " + post_Table);
        database.execSQL("DROP TABLE IF EXISTS " + board_Table);
        onCreate(database);
    }

    public Device_Interface(Context context){
        super(context, database_Name, null, 1);
        //save current context
        application_Context = context;
    }

    public void save_Post(Post save_Me){
        //Save a post to the device's local database
        //get readable database to check if post has already been saved
        SQLiteDatabase read_Base = this.getReadableDatabase();
        Cursor recurse = read_Base.query(post_Table, new String[]{post_ID}, post_ID + " = " + save_Me.getPost_ID(),
                new String[]{}, null, null, null);  //query for the post in question
        if (recurse.getCount() < 1) {   //post was not found, okay to save
            //get writable database
            SQLiteDatabase write_Base = this.getWritableDatabase();
            //set values
            ContentValues values = new ContentValues();
            values.put(post_ID, save_Me.getPost_ID());
            values.put(phone, save_Me.getPhone());
            values.put(end_Date, save_Me.getEnd_Date());
            values.put(host_Name, save_Me.getHost());
            values.put(email_Address, save_Me.getEmail());
            values.put(address, save_Me.getAddress());
            values.put(post_Information, save_Me.getInformation());
            values.put(post_Status, save_Me.getPost_Status());
            values.put(post_Type, save_Me.getPost_Type());
            //insert record into database
            write_Base.insert(post_Table, post_ID, values);
            Log.i("Post Saved", "Post was saved.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Post Saved")
                    .setMessage("This post has been saved to your device.")
                    .setPositiveButton("OK", null)
                    .show();
            //close the writable database
            write_Base.close();
        }else{  //post was found, not okay to save again
            Log.i("Already Saved", "Post was already saved.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Already Saved")
                    .setMessage("You have already saved this post.")
                    .setPositiveButton("OK", null)
                    .show();
            //close to query cursor
            recurse.close();
        }
        //close the readable database
        read_Base.close();
    }

    public Vector<Post> get_Posts(){
        //get all posts saved to the device's local database
        Vector<Post> saved_Posts = new Vector<>();  //return vector
        //get readable database
        SQLiteDatabase read_Base = this.getReadableDatabase();
        //perform raw query of all posts in database
        Cursor curse = read_Base.rawQuery("SELECT " + post_ID + ", " + phone + ", " + end_Date + ", "
                + host_Name + ", " + email_Address + ", " + address + ", " + post_Information + ", "
                + post_Status + ", " + post_Type + " from " + post_Table, new String[]{});
        if (curse != null) {    //if cursor isn't empty
            if  (curse.moveToFirst()) {
                do {    //translate each result to Post data type
                    Post buffer_Post = new Post();
                    buffer_Post.setPost_ID(curse.getLong(curse.getColumnIndex(post_ID)));
                    buffer_Post.setPhone(curse.getLong(curse.getColumnIndex(phone)));
                    buffer_Post.setEnd_Date(curse.getLong(curse.getColumnIndex(end_Date)));
                    buffer_Post.setHost(curse.getString(curse.getColumnIndex(host_Name)));
                    buffer_Post.setEmail(curse.getString(curse.getColumnIndex(email_Address)));
                    buffer_Post.setAddress(curse.getString(curse.getColumnIndex(address)));
                    buffer_Post.setInformation(curse.getString(curse.getColumnIndex(post_Information)));
                    buffer_Post.setPost_Status(curse.getString(curse.getColumnIndex(post_Status)));
                    buffer_Post.setPost_Type(curse.getString(curse.getColumnIndex(post_Type)));
                    saved_Posts.add(buffer_Post);   //store each post in vector
                }while (curse.moveToNext());
            }
            //close the cursor
            curse.close();
        }
        //close the readable database
        read_Base.close();
        //return the populated vector
        return saved_Posts;
    }

    public void delete_Post(Post end_Me){
        //delete a post from the device's local database
        //get readable database to check if the post exists in the database
        SQLiteDatabase read_Base = this.getReadableDatabase();
        Cursor recurse = read_Base.query(post_Table, new String[]{post_ID}, post_ID + " = " + end_Me.getPost_ID(),
                new String[]{}, null, null, null);  //query for the post in question
        if (recurse.getCount() < 1) {   //post was not found, cannot delete
            Log.i("File Corruption", "Could not locate post, possible data corruption.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("File Corruption")
                    .setMessage("Could not locate post, possible data corruption.")
                    .setPositiveButton("OK", null)
                    .show();
        }else{  //post was found, okay to delete
            //get writable database
            SQLiteDatabase write_Base = this.getWritableDatabase();
            write_Base.delete(post_Table, post_ID + " = " + end_Me.getPost_ID(), new String [] {});
            Log.i("Post Deleted", "Post has been deleted.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Post Deleted")
                    .setMessage("The post has been removed from your device.")
                    .setPositiveButton("OK", null)
                    .show();
            //close the writable database
            write_Base.close();
        }
        //close to query cursor
        recurse.close();
        //close the readable database
        read_Base.close();
    }

    public void save_Board(Board save_Me){
        //Save a board to the device's local database
        //get readable database to check if the board has already been saved
        SQLiteDatabase read_Base = this.getReadableDatabase();
        Cursor recurse = read_Base.query(board_Table, new String[]{board_ID}, board_ID + " = " + save_Me.getBoard_ID(),
                new String[]{}, null, null, null);  //query for the board in question
        if (recurse.getCount() < 1) {   //board was not found, okay to save
            //get writable database
            SQLiteDatabase write_Base = this.getWritableDatabase();
            //set values
            ContentValues values = new ContentValues();
            values.put(board_ID, save_Me.getBoard_ID());
            values.put(board_Name, save_Me.getBoard_Name());
            values.put(organization, save_Me.getOrganization());
            values.put(group_ID, save_Me.getGroup_ID());
            values.put(instructions, save_Me.getInstructions());
            values.put(moderator_ID, save_Me.getModerator_ID());
            //insert record into database
            write_Base.insert(board_Table, board_ID, values);
            Log.i("Board Saved", "Board was saved.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Board Saved")
                    .setMessage("This board has been saved to your device.")
                    .setPositiveButton("OK", null)
                    .show();
            //close the writable database
            write_Base.close();
        }else{  //post was found, not okay to save again
            Log.i("Already Saved", "Board was already saved.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Already Saved")
                    .setMessage("You have already saved this board.")
                    .setPositiveButton("OK", null)
                    .show();
            //close to query cursor
            recurse.close();
        }
        //close the readable database
        read_Base.close();
    }

    public Vector<Board> get_Boards(){
        //get all boards saved to the device's local database
        Vector<Board> saved_Boards = new Vector<>();  //return vector
        //get readable database
        SQLiteDatabase read_Base = this.getReadableDatabase();
        //perform raw query of all boards in database
        Cursor curse = read_Base.rawQuery("SELECT " + board_ID + ", " + board_Name + ", " + organization + ", "
                + group_ID + ", " + instructions + ", " + moderator_ID + " from " + board_Table, new String[]{});
        if (curse != null) {    //if cursor isn't empty
            if  (curse.moveToFirst()) {
                do {    //translate each result to Board data type
                    Board buffer_Board = new Board();
                    buffer_Board.setBoard_ID(curse.getLong(curse.getColumnIndex(board_ID)));
                    buffer_Board.setBoard_Name(curse.getString(curse.getColumnIndex(board_Name)));
                    buffer_Board.setOrganization(curse.getString(curse.getColumnIndex(organization)));
                    buffer_Board.setGroup_ID(curse.getLong(curse.getColumnIndex(group_ID)));
                    buffer_Board.setInstructions(curse.getString(curse.getColumnIndex(instructions)));
                    buffer_Board.setModerator_ID(curse.getLong(curse.getColumnIndex(moderator_ID)));
                    saved_Boards.add(buffer_Board);   //store each board in vector
                }while (curse.moveToNext());
            }
            //close the cursor
            curse.close();
        }
        //close the readable database
        read_Base.close();
        //return the populated vector
        return saved_Boards;
    }

    public void delete_Board(Board end_Me){
        //delete a board from the device's local database
        //get readable database to check if the board exists in the database
        SQLiteDatabase read_Base = this.getReadableDatabase();
        Cursor recurse = read_Base.query(board_Table, new String[]{board_ID}, board_ID + " = " + end_Me.getBoard_ID(),
                new String[]{}, null, null, null);  //query for the board in question
        if (recurse.getCount() < 1) {   //board was not found, cannot delete
            Log.i("File Corruption", "Could not locate board, possible data corruption.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("File Corruption")
                    .setMessage("Could not locate board, possible data corruption.")
                    .setPositiveButton("OK", null)
                    .show();
        }else{  //board was found, okay to delete
            //get writable database
            SQLiteDatabase write_Base = this.getWritableDatabase();
            write_Base.delete(board_Table, board_ID + " = " + end_Me.getBoard_ID(), new String [] {});
            Log.i("Board Deleted", "Board has been deleted.");
            //inform user of result
            new AlertDialog.Builder(application_Context)
                    .setTitle("Board Deleted")
                    .setMessage("The board has been removed from your device.")
                    .setPositiveButton("OK", null)
                    .show();
            //close the writable database
            write_Base.close();
        }
        //close to query cursor
        recurse.close();
        //close the readable database
        read_Base.close();
    }
}
