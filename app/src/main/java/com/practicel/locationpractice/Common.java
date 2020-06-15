package com.practicel.locationpractice;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String FROM_NAME = "fromName";
    public static final String ACCEPLIST = "acceplist";
    public static final String FROM_UID = "from_uid";
    public static final String TO_UID ="to_uid" ;
    public static final String TO_NAME = "to_name" ;
    public static final String FRIEND_REQUEST ="friend_request" ;
    public static final String LOCATION = "location";
    public static User loggedUser;
    public static String USERS="users";
    public static String USER_UID_SAVE_KEY="saveuid";
    public static User trackingUser;


    public static IFCMService getFCMService(){

        return RetrofitClient.getClient("https://fcm.googleapis.com/").create(IFCMService.class);


    }
    public static Date convertTimestampToDate(long time){

        return new Date(new Timestamp(time).getTime());


    }

    public static String getDateFormatted(Date date){

        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
    }


}
