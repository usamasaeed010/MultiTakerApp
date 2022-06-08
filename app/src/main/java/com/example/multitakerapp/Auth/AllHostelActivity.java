package com.example.multitakerapp.Auth;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.multitakerapp.Adopter.MyAllHostelAdapter;
import com.example.multitakerapp.Model.UserHostelDetail;
import com.example.multitakerapp.databinding.ActivityAllHostelBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllHostelActivity extends AppCompatActivity {

    ActivityAllHostelBinding binding;
    private DatabaseReference databaseReference;
    MyAllHostelAdapter hostelAdapter;
    ArrayList<UserHostelDetail> hostelDetailList;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllHostelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hostelDetailList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();

        //    databaseReference = FirebaseDatabase.getInstance().getReference().child("AddHostel");


        binding.allHostelRV.setHasFixedSize(true);
        binding.allHostelRV.setLayoutManager(new GridLayoutManager(this, 1));
       /* hostelAdapter = new MyAllHostelAdapter(AllHostelActivity.this,mdatabase);
        binding.allHostelRV.setAdapter(hostelAdapter);*/
    }

    @Override
    protected void onStart() {
        super.onStart();


        getUserData();
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hostelDetailList.clear();
                for (DataSnapshot visitorSnapshot : dataSnapshot.getChildren()) {
                    UserHostelDetail visitorEntry = visitorSnapshot.getValue(UserHostelDetail.class);
                    hostelDetailList.add(visitorEntry);
                }
                hostelAdapter = new MyAllHostelAdapter(AllHostelActivity.this, hostelDetailList);
                binding.allHostelRV.setAdapter(hostelAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AddHostel");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostelDetailList.clear();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String familyname = datas.child("hostelName").getValue().toString();
                    String hostelnumber = datas.child("hostelnumber").getValue().toString();
                    String hostelArea = datas.child("hostelArea").getValue().toString();
                    String hostelrent = datas.child("hostelrent").getValue().toString();
                    String hostelrooms = datas.child("hostelrooms").getValue().toString();

                    UserHostelDetail userHostelDetail = new UserHostelDetail(familyname, hostelnumber, hostelArea, hostelrent, hostelrooms);
                    hostelDetailList.add(userHostelDetail);


                }
                hostelAdapter = new MyAllHostelAdapter(AllHostelActivity.this, hostelDetailList);
                binding.allHostelRV.setAdapter(hostelAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}