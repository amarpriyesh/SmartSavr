package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ChildProfile_parentlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile_parentlogin);
        Child child = savedInstanceState.getParcelable("child");

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(child.getProfilePicture());


    }


}