package com.seniordesign.bluetoothbillboard;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/*
 * Created by Eric Anderson on 7/5/2015.
 */

@SuppressWarnings("unused")
@DynamoDBTable(tableName = "Moderator")
public class Moderator {

    private String Moderator_ID;       //identification number (unique)
    private String Username;           //user name
    private String Password;           //password

    @DynamoDBHashKey(attributeName = "Moderator_ID")
    public String getModerator_ID() {
        return Moderator_ID;
    }

    public void setModerator_ID(String identifier) {
        this.Moderator_ID = identifier;
    }

    @DynamoDBAttribute(attributeName = "Username")
    public String getUsername() {
        return Username;
    }

    public void setUsername(String user) {
        this.Username = user;
    }

    @DynamoDBAttribute(attributeName = "Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String pass) {
        this.Password = pass;
    }
}
