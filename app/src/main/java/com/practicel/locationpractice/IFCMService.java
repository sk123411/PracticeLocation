package com.practicel.locationpractice;

import com.google.firebase.messaging.FirebaseMessagingService;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAARMkejKE:APA91bHgixX61xdNIFFWtdJKpHVk49sxWBg-PWrj40HJZV5Flk_Zy9YEtbx3gOsgfh6rqxFn2UlIaUpr5EsAWfnC_mTjC6G3sW1mDHJU0K72Z_-Lvr8pNys1PAGD7NMGnCPOWE-1BEhS"
    })
    @POST("fcm/send")
   Observable<MyResponse> sendFriendRequestToUser(@Body MyRequest body);

}
