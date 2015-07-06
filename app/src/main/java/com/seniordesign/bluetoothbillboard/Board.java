package com.seniordesign.bluetoothbillboard;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Vector;

/**
 * Created by Eric Anderson on 7/5/2015.
 */

@DynamoDBTable(tableName = "Board")
public class Board {

    private long Board_ID;       //identification number (unique)
    private String Board_Name;     //name of the board
    private String Organization;   //organization that owns the board
    private long Group_ID;       //group number
    private String Instructions;   //instructions for posting on the board
    private long Moderator_ID;   //moderator identification number
    private Vector<Post> Posts;    //posts attached to the board

    @DynamoDBIndexHashKey(attributeName = "Board_ID")
    public long getBoard_ID() {
        return Board_ID;
    }

    public void setBoard_ID(long identifier) {
        this.Board_ID = identifier;
    }

    @DynamoDBAttribute(attributeName = "Board_Name")
    public String getBoard_Name() {
        return Board_Name;
    }

    public void setBoard_Name(String name) {
        this.Board_Name = name;
    }

    @DynamoDBAttribute(attributeName = "Group_ID")
    public long getGroup_ID() {
        return Group_ID;
    }

    public void setGroup_ID(long group) {
        this.Group_ID = group;
    }

    @DynamoDBAttribute(attributeName = "Instructions")
    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String directions) {
        this.Instructions = directions;
    }

    @DynamoDBAttribute(attributeName = "Moderator_ID")
    public long getModerator_ID() {
        return Moderator_ID;
    }

    public void setModerator_ID(long moderator) {
        this.Moderator_ID = moderator;
    }

    @DynamoDBAttribute(attributeName = "Organization")
    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String company) {
        this.Organization = company;
    }

    public Vector<Post> getPosts() {
        return Posts;
    }

    public void setPosts(Vector<Post> postings) {
        this.Posts = postings;
    }
}
