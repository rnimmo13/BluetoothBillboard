package com.seniordesign.bluetoothbillboard;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/*
 * Created by Eric Anderson on 7/5/2015.
 */

@SuppressWarnings("unused")
@DynamoDBTable(tableName = "Moderator")
public class Moderator {

    private Long Moderator_ID;       //identification number (unique)
    private String Username;           //user name
    private String Password;           //password

    @DynamoDBIndexHashKey(attributeName = "Moderator_ID")
    public Long getModerator_ID() {
        return Moderator_ID;
    }

    public void setModerator_ID(Long identifier) {
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
