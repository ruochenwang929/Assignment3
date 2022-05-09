package com.example.assignment3.report;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.assignment3.DrawerActivity;
import com.example.assignment3.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


public class ShareActivity extends DrawerActivity {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private boolean canPresentShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }
}
