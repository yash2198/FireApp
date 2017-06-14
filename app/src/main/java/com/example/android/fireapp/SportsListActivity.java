package com.example.android.fireapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SportsListActivity extends AppCompatActivity {

    private String tournamentId;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String userId;
    private boolean GOD_MODE=false;

    public static String EXTRA_MESSAGE = "Tournament ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_list);

        Intent intent = getIntent();
        tournamentId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d("tournieId(slActivity)",tournamentId);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        final FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_edit_tournament);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //there is a user logged in
                if(firebaseAuth.getCurrentUser()!=null){
                    userId = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference mRef = mDatabase.child("Users").child(userId);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation user = dataSnapshot.getValue(UserInformation.class);
                            if(!user.isGod()){
                                mFab.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //no user logged in
                else{
                    mFab.setVisibility(View.INVISIBLE);
                }
            }
        };

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditTournament = new Intent(SportsListActivity.this,EditTournamentActivity.class);
                intentToEditTournament.putExtra(EXTRA_MESSAGE, tournamentId);
                startActivity(intentToEditTournament);
            }
        });
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthStateListener);
        DatabaseReference tournamentRef = mDatabase.child("Tournaments").child(tournamentId);
        populateView(tournamentRef);

        super.onStart();
    }

    private void populateView (DatabaseReference dbRef){
        final ListView sportsListView = (ListView) findViewById(R.id.sports_list);
        final ArrayList<String> sportsList = new ArrayList<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TournamentInformation tournamentInformation = dataSnapshot.getValue(TournamentInformation.class);
                setTitle(tournamentInformation.getTournamentName());
                if(tournamentInformation.hasBadminton()){    sportsList.add("Badminton");    }
                if(tournamentInformation.hasBasketball()){    sportsList.add("Basketball");    }
                if(tournamentInformation.hasChess()){    sportsList.add("Chess");    }
                if(tournamentInformation.hasContractBridge()){    sportsList.add("Contract Bridge");    }
                if(tournamentInformation.hasDodgeball()){    sportsList.add("Dodgeball");    }
                if(tournamentInformation.hasDota2()){    sportsList.add("Dota 2");    }
                if(tournamentInformation.hasFloorball()){    sportsList.add("Floorball");    }
                if(tournamentInformation.hasHandball()){    sportsList.add("Handball");    }
                if(tournamentInformation.hasNetball()){    sportsList.add("Netball");    }
                if(tournamentInformation.hasReversi()){    sportsList.add("Reversi");    }
                if(tournamentInformation.hasRoadRelay()){    sportsList.add("Road Relay");    }
                if(tournamentInformation.hasSoccer()){    sportsList.add("Soccer");    }
                if(tournamentInformation.hasTableTennis()){    sportsList.add("Table Tennis");    }
                if(tournamentInformation.hasTchoukball()){    sportsList.add("Tchoukball");    }
                if(tournamentInformation.hasTennis()){    sportsList.add("Tennis");    }
                if(tournamentInformation.hasTouchFootball()){    sportsList.add("Touch Football");    }
                if(tournamentInformation.hasUltimate()){    sportsList.add("Ultimate Frisbee");    }
                if(tournamentInformation.hasVolleyball()){    sportsList.add("Volleyball");    }

                StringAdapter stringAdapter = new StringAdapter(SportsListActivity.this,sportsList,R.color.sports_list);
                sportsListView.setAdapter(stringAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
