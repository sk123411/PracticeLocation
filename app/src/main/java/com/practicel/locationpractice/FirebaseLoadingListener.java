package com.practicel.locationpractice;

import java.util.List;

public interface FirebaseLoadingListener {

    void onFirebaseUserSearchDone(List<String> users);
    void onFirebaseLoadFailed(String message);
}
