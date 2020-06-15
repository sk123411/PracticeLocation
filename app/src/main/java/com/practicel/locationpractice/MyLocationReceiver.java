package com.practicel.locationpractice;//package com.example.androidlocationpractice.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.firebase.database.FirebaseDatabase;
import com.practicel.locationpractice.Common;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;

import io.paperdb.Paper;

public class MyLocationReceiver extends BroadcastReceiver   {

        DatabaseReference locationRef;
        String uid;
        public static final String ACTION="com.practicel.locationpractice.UPDATE_LOCATION";


        public MyLocationReceiver(){

            locationRef = FirebaseDatabase.getInstance().getReference(Common.LOCATION);

        }
    @Override
    public void onReceive(Context context, Intent intent) {

        Paper.init(context);

        uid = Paper.book().read(Common.USER_UID_SAVE_KEY);

        if (intent!=null){

            String action = intent.getAction();

            if (action.equals(ACTION)){


                LocationResult locationResult = LocationResult.extractResult(intent);

                if (locationResult!=null){

                    Location location = locationResult.getLastLocation();
                    if (Common.loggedUser!=null){  //App is foreground

                        locationRef.child(Common.loggedUser.getUid()).setValue(location);

                    }else //App is killed
                        locationRef.child(uid).setValue(location);





                }
            }
        }
    }
}
