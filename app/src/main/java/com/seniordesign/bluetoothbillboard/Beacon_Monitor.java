package com.seniordesign.bluetoothbillboard;

/*
    Handles beacon monitoring

 * Created by Eric_Anderson on 7/16/15.
 */

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "unused"})
public class Beacon_Monitor extends Application implements BootstrapNotifier {

    private static final String beacon_UUID = "B8FB1855-0644-4FFC-94E7-CBA10CFC4087";   //Beacon UUID
    private static final String TAG = "Beacon Monitor";     //Log Tag
    private RegionBootstrap regionBootstrap;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Application started.");
        //create new beacon region and start monitoring it
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region("Bluetooth Billboard", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    @Override
    public void didDetermineStateForRegion(int range, Region pulse) {
        //state for a region has been determined
        Log.i(TAG, "Received a didDetermineStateForRegion call.");
    }

    @Override
    public void didEnterRegion(Region found_Region) {
        //region state is entered
        Log.i(TAG, "Received a didEnterRegion call.");
        if (found_Region.getId1().toString().equals(beacon_UUID)) {     //check if UUID matches
            //get compiled board identifier
            String board_Number = found_Region.getId2().toString() + found_Region.getId3().toString();
            //get list of previously found beacons
            SharedPreferences already_Found = Dynamo_Interface.application_context.getSharedPreferences(
                    "bluetooth_Billboard_Found_Beacons", Context.MODE_PRIVATE);
            ArrayList<String> beacon_List = new ArrayList<>(already_Found.getStringSet("beacon", null));
            if (!beacon_List.contains(board_Number)) {      //check if beacon was previously found
                Log.i(TAG, "Ranged application beacon: " + board_Number);
                //add beacon to already found list
                beacon_List.add(board_Number);
                Set updated_List = new HashSet(beacon_List);
                //store updated list
                SharedPreferences.Editor editor = already_Found.edit();
                editor.putStringSet("beacon", updated_List);
                editor.apply();
                regionBootstrap.disable();
                //open board list view intent
                Intent intent = new Intent(this, view_board_list_screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            }else{      //beacon was already found
                Log.i(TAG, "Beacon already found.");
            }
        }
    }

    @Override
    public void didExitRegion(Region pulse) {
        //confirmed region has been exited
        Log.i(TAG, "Received a didExitRegion call.");
    }
}
