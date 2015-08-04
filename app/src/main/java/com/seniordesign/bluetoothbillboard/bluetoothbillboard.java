package com.seniordesign.bluetoothbillboard;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "unused"})
public class bluetoothbillboard extends Application implements BeaconConsumer {

    private static final String beacon_UUID = "B8FB1855-0644-4FFC-94E7-CBA10CFC4087";   //Beacon UUID
    private static final String unique_name = "com.seniordesign.bluetoothbillboard";    //Application Name
    protected static final String TAG = "Beacon";       //Log Tag
    private BeaconManager beaconManager;                //Beacon Manager

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize beacon manager
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundMode(true);
        //add altbeacon and ibeacon support
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "Region " + region.toString() + " found for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "Left region " + region.toString());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                if (state == 1) {
                    Log.i(TAG, "Beacons are in range");
                }else if (state == 0){
                    Log.i(TAG, "Beacons are not in range");
                }
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region(unique_name,
                    Identifier.parse(beacon_UUID), null, null));
            Log.i(TAG, "Monitoring started.");
        } catch (RemoteException e) {
            Log.e(TAG, "Error: " + e.toString());
        }

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region found_Region) {
                Log.i(TAG, "Ranged " + beacons.size() + " beacons.");
                if (beacons.size() > 0) {
                    if (found_Region.getId1().toString().equalsIgnoreCase(beacon_UUID)) {     //check if UUID matches
                        //get compiled board identifier
                        ArrayList<Beacon> beacon_list = new ArrayList(beacons);
                        Log.i(TAG, "Beacon Identification: " + beacon_list.get(0).toString());
                        String board_Number = beacon_list.get(beacon_list.size() - 1).getId2().toString() +
                                beacon_list.get(beacon_list.size() - 1).getId3().toString();
                        //get list of previously found beacons
                        Set<String> defaultSet = new HashSet<>();
                        SharedPreferences already_Found = Dynamo_Interface.application_context.getSharedPreferences(
                                "found_beacons", Context.MODE_PRIVATE);
                        ArrayList<String> beacon_List = new ArrayList<>(already_Found.getStringSet("boards", defaultSet));
                        if (!beacon_List.contains(board_Number)) {      //check if beacon was previously found
                            Log.i(TAG, "Ranged application beacon: " + board_Number);
                            //add beacon to already found list
                            beacon_List.add(board_Number);
                            Set updated_List = new HashSet(beacon_List);
                            //store updated list
                            SharedPreferences.Editor editor = already_Found.edit();
                            editor.putStringSet("boards", updated_List);
                            editor.commit();
                            //create a notification
                            Board found_board = Dynamo_Interface.getSingle_board_information(Long.parseLong(board_Number));
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(bluetoothbillboard.this)
                                    .setSmallIcon(R.drawable.icon_notifier)
                                    .setContentTitle("New Board")
                                    .setContentText("You have found the " + found_board.getBoard_Name() + " board!");
                            //assign an activity to start when notification is selected
                            Intent resultIntent = new Intent(bluetoothbillboard.this, view_board_list_screen.class);
                            PendingIntent resultPendingIntent = PendingIntent.getActivity(bluetoothbillboard.this,
                                    0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);
                            mBuilder.setAutoCancel(true);
                            NotificationManager manage_notifications = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manage_notifications.notify(999, mBuilder.build());
                        } else {      //beacon was already found
                            Log.i(TAG, "Beacon already found.");
                        }
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region(unique_name,
                    Identifier.parse(beacon_UUID), null, null));
            Log.i(TAG, "Ranging started.");
        } catch (RemoteException e) {
            Log.e(TAG, "Error: " + e.toString());
        }
    }
}