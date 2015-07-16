package com.seniordesign.bluetoothbillboard;

import android.app.AlertDialog;
import android.util.Log;
import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.net.InetAddress;
import java.util.Vector;

/*
    Handles all communication with the remote database.

     Created by Eric Anderson on 7/5/2015.
 */

@SuppressWarnings({"unchecked", "unused"})
public class Dynamo_Interface {

    static boolean connection = false;        //state of the device's internet connection
    static String table_name;            //name of the current table being queried
    static String hash_key;              //name of the hash-key attribute for the table being queried
    static String current_board;         //name of the current board
    static Board full_board;               //the current board
    static Context application_context;     //context of the application

    public static void setApplication_context(Context appContext){
        //set application context
        application_context = appContext;
    }

    public static void setTable_name(String name){
        //set table name
        table_name = name;
    }

    public static void setHash_key(String key){
        //set hash key
        hash_key = key;
    }

    public static String dynamoDBTableName(){
        //return table name
        return table_name;
    }

    public static String hashKeyAttribute(){
        //return hash key for a query/scan
        return hash_key;
    }

    public static void setCurrent_board(String newBoard){
        //sets current board and gets info for it
        current_board = newBoard;
        Log.i("Board set", "Current board set to " + current_board);
        full_board = Dynamo_Interface.getSingle_board_information(Long.parseLong(current_board));
    }

    public static Board getCurrent_board_info(){
        //return current board info
        return full_board;
    }

    public static String getCurrent_board(){
        //return current board name
        return current_board;
    }

    public static boolean getConnection(){
        //return connection status
        return isConnected();
    }

    public static boolean isConnected() {
        try {
            InetAddress IPAddress = InetAddress.getByName("www.google.com");
            if (!IPAddress.toString().equals("")) {
                Log.i("Connected","Device is connected to the internet");
                return true;
            } else {
                Log.i("Disconnected","Device is not connected to the internet");
                new AlertDialog.Builder(application_context)
                        .setTitle("Not Connected")
                        .setMessage("Could not connect to the database.  Please verify your internet connection or try again later.")
                        .setPositiveButton("OK", null)
                        .show();
                return false;
            }
        } catch (Exception e) {
            Log.e("Connection Error", "Error: " + e.toString());
            return false;
        }
    }

    public static Vector<Board> getAll_board_information(){
        //returns an array of boards, with fully populated information
        Vector<Board> board_list = new Vector<>();
        //aws credentials
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        //aws scan expression
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        if(getConnection()) {
            PaginatedScanList<Board> result = mapper.scan(Board.class, scanExpression);
            Log.i("Scan Complete", "Database scan successful!");
            board_list = new Vector<>(result);
        }
        return board_list;
    }

    public static Board getSingle_board_information(long identifier){
        //returns a populated board
        Board identify_me = new Board();
        identify_me.setBoard_ID(identifier);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(identify_me);
        if(getConnection()) {
            PaginatedQueryList<Board> result = mapper.query(Board.class, queryExpression);
            Log.i("Scan Complete", "Database scan successful!");
            identify_me = result.get(0);
        }
        return identify_me;
    }

    public static Post getSingle_post(long identifier){
        //returns a single populated post
        Post check_post = new Post();
        check_post.setPost_ID(identifier);
        String full_table_name = "Board" + getCurrent_board();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(check_post);
        if(getConnection()) {
            PaginatedQueryList<Post> result = mapper.query(Post.class, queryExpression,
                    new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(full_table_name)));
            Log.i("Scan Complete", "Database scan successful!");
            check_post = result.get(0);
        }
        return check_post;
    }

    public static Board getFiltered_posts(long identifier, String filter){
        //return all posts on a board using the filter
        Board filled_board = getSingle_board_information(Long.parseLong(current_board));
        String full_table_name = "Board" + getCurrent_board();
        //aws credentials
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        //aws scan expression
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        if (!filter.equals("")){
            //set the scan expression's filter if there is one
            Condition scanCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ.toString())
                            .withAttributeValueList(
            new AttributeValue().withS(filter));
            scanExpression.addFilterCondition("Post_Status", scanCondition);
        }
        if(getConnection()) {
            PaginatedScanList<Post> result = mapper.scan(Post.class, scanExpression,
                    new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(full_table_name)));
            Log.i("Scan Complete", "Database scan successful!");
            filled_board.setPosts(new Vector<>(result));
        }
        return filled_board;
    }

    public static void save_post(Post save_me){

        String full_table_name = "Board" + getCurrent_board();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        mapper.save(save_me, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(full_table_name)));
        Log.i("Post Saved","Post has been saved.");
    }

    public static void delete_post(Post delete_me){
        //remove a post from the database
        String full_table_name = "Board" + getCurrent_board();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

        mapper.delete(delete_me, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(full_table_name)));
        Log.i("Post Deleted", "Post has been deleted.");
    }

    public static void remove_outdated(){
        //remove outdated posts from the current board
        //will fill out when I learn how to manipulate date
            //objects in java
    }

    public static boolean verify_credentials(Moderator entered_credentials){
        //verify moderator credentials
        Moderator database_credentials = new Moderator();
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                application_context, // Context
                "us-east-1:ed50d9e9-fd87-4188-b4e2-24a974ee68e9", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(full_board.getModerator_ID());
        if(getConnection()) {
            PaginatedQueryList<Moderator> result = mapper.query(Moderator.class, queryExpression);
            Log.i("Query Complete", "Database query successful!");
            database_credentials = result.get(0);
        }
        if(entered_credentials.getUsername().equals(database_credentials.getUsername())
                && entered_credentials.getPassword().equals(database_credentials.getPassword())){
            Log.i("Valid Credentials","Proper moderator credentials entered.");
            return true;
        }else {
            Log.i("Invalid Credentials", "Invalid moderator credentials entered.");
            return false;
        }
    }
}
