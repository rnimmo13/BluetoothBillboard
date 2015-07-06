package com.seniordesign.bluetoothbillboard;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by Eric Anderson on 7/5/2015.
 */
@DynamoDBTable(tableName = "Board")
public class Post {

    private long Post_ID;        //identification number (unique to board)
    private long Phone;          //phone number (optional)
    private long End_Date;       //end date (default = 90 days from today)
    private String Host;           //host name
    private String Email;          //e-mail address (optional)
    private String Address;        //address (optional)
    private String Information;    //post information, actual message
    private String Post_Type;      //type of post (predefined set)
    private String Post_Status;    //status of post (posted, queued, denied)

    @DynamoDBIndexHashKey(attributeName = "Post_ID")
    public long getPost_ID() {
        return Post_ID;
    }

    public void setPost_ID(long identifier) {
        this.Post_ID = identifier;
    }

    @DynamoDBAttribute(attributeName = "Phone")
    public long getPhone() {
        return Phone;
    }

    public void setPhone(long phone_number) {
        this.Phone = phone_number;
    }

    @DynamoDBAttribute(attributeName = "End_Date")
    public long getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(long end) {
        this.End_Date = end;
    }

    @DynamoDBAttribute(attributeName = "Host")
    public String getHost() {
        return Host;
    }

    public void setHost(String poster) {
        this.Host = poster;
    }

    @DynamoDBAttribute(attributeName = "Email")
    public String getEmail() {
        return Email;
    }

    public void setEmail(String mail) {
        this.Email = mail;
    }

    @DynamoDBAttribute(attributeName = "Address")
    public String getAddress() {
        return Address;
    }

    public void setAddress(String location) {
        this.Address = location;
    }

    @DynamoDBAttribute(attributeName = "Information")
    public String getInformation() {
        return Information;
    }

    public void setInformation(String details) {
        this.Information = details;
    }

    @DynamoDBAttribute(attributeName = "Post_Type")
    public String getPost_Type() {
        return Post_Type;
    }

    public void setPost_Type(String type) {
        this.Post_Type = type;
    }

    @DynamoDBAttribute(attributeName = "Post_Status")
    public String getPost_Status() {
        return Post_Status;
    }

    public void setPost_Status(String status) {
        this.Post_Status = status;
    }
}
