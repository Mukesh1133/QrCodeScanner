package com.example.ushopcodescanner;


import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken( String token) {
        super.onNewToken(token);
    }
}


