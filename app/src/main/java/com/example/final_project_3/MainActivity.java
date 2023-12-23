package com.example.final_project_3;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;


    FirebaseUser user;

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    DrawerLayout drawerLayout;

    BroadcastReceiver broadcastReceiver =null;

    TextView welcomeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
            // Checks if the user is not logged in and redirects to the login activity
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        welcomeTextView = headerView.findViewById(R.id.WelcomeEmail);


        broadcastReceiver = new InternetRecevier();
        Internetstatus();


        SharedPreferences sp = getApplication().getSharedPreferences("MY_NAMES", Context.MODE_PRIVATE);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_profile) {
                    // Navigate to ProfileSettingsActivity
                    Intent profileSettingsIntent = new Intent(MainActivity.this, Profile_Settings.class);
                    startActivity(profileSettingsIntent);
                } else if (item.getItemId() == R.id.nav_logout) {
                    auth.signOut();
                    Intent profileLogoutIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(profileLogoutIntent);
                }


                else if (item.getItemId() == R.id.nav_share) {

                    //IMPLICIT intent
                    Intent Share = new Intent();
                    Share.setAction(Intent.ACTION_SEND);
                    getIntent().putExtra(Intent.EXTRA_TEXT,"App_url");
                    Share.setType("text/plain");
                    if (Share.resolveActivity(getPackageManager())!=null){
                        startActivity(Share);

                    }

                }else if(item.getItemId() ==R.id.nav_Service){
                    Intent NewServiceIntent = new Intent(MainActivity.this, Service.class);
                    startActivity(NewServiceIntent);
                }

                // Close the drawer after item selection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        String Userid = user.getUid();
        String LoggedInUserName = sp.getString(Userid,"");

        Log.d("MainActivity", "User ID from main: " + Userid); //for testing
        Log.d("MainActivity", "Logged In User Name: " + LoggedInUserName); //for testing

        welcomeTextView.setText(LoggedInUserName);



        // Initialize btnLogout

        recyclerView = findViewById(R.id.news_recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);
        btn10 = findViewById(R.id.btn_10);



        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);

        setupRecyclerView();
        // Call the getNews method
        getNews("Hacking");






    }

    public void Internetstatus(){
        registerReceiver(broadcastReceiver ,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean show) {
        if (show) {
            progressIndicator.setVisibility(View.VISIBLE);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    void getNews(String query) {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("071f26439b3d4e8d9c80001d45f7511b");
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .language("en")
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        runOnUiThread(() -> {
                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT FAIL", throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String query = btn.getText().toString();
        getNews(query);
    }

}
