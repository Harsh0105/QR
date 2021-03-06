package com.example.qr;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.firebase.database.DatabaseReference;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


import static android.widget.Toast.LENGTH_SHORT;


public class ScannerHistory extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private DbAdapter mAdapter;
    public String newlocation;
    public Button buttonAdd;
    private String profileRollno;
    DatabaseReference dbref;
    member Member;
    private TextView mTimeText;
    private FirebaseAuth firebaseAuth1;
    private FirebaseDatabase firebaseDatabase;
    public String rollno;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_history);

   //     Toast.makeText(ScannerHistory.this,"firebase connection success", Toast.LENGTH_SHORT).show();


        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DbAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

        mTimeText = findViewById(R.id.textview_time_item);

        Member = new member();
    // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbref = FirebaseDatabase.getInstance().getReference().child("member");
    //    dbref.keepSynced(true);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth1 = FirebaseAuth.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference(Objects.requireNonNull(firebaseAuth1.getUid()));
        databaseReference.keepSynced(true);
        buttonAdd = findViewById(R.id.button_add);
        final Activity activity = this;

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                rollno = userProfile.getUserRoll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(ScannerHistory.this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "scanning was cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                newlocation = String.valueOf(result.getContents());
                addItem();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    public void addItem() {
        String status1 = "Checked in";
        String status2 = "Checked out";
        String oldLocation;
        String oldStatus;
        String newLocation = newlocation;
        if(mAdapter.mCursor.getCount() == 0){
            oldLocation = "empty";
            oldStatus = "empty";
        }
        else{
            mAdapter.mCursor.moveToFirst();
            oldLocation = mAdapter.mCursor.getString(mAdapter.mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_NAME));

            // System.out.println(oldLocation);
            oldStatus = mAdapter.mCursor.getString(mAdapter.mCursor.getColumnIndex(MyCovidEntry.CovidEntry.COLUMN_CHECKED));
            //System.out.println(oldStatus);
        }
        if (oldStatus.equals(status2) || oldStatus.equals("empty")){
            localGet(status1, newLocation);
            serverUpdate(status1,newLocation);

        }
        else if(oldStatus.equals(status1)){
            if(newLocation.equals(oldLocation)){
                localGet(status2, oldLocation);
               serverUpdate(status2,oldLocation);

            }
            else if (newLocation.equals(oldLocation) == false){
                localGet(status1, newLocation);
                serverUpdate(status1,newLocation);
                localGet(status2, oldLocation);
                serverUpdate(status2,oldLocation);


            }
        }
    }


  //  DataSnapshot snapshot;



    public void serverUpdate(String status, String location){
        String name = location;
        String dateDay = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

   //     UserProfile userProfile = snapshot.getValue(UserProfile.class);

    //    String rollno = userProfile.getUserRoll();
        Member.setBuildingName(name);
        Member.setCheckStatus(status);
        Member.setDate(dateDay);
        Member.setPersonName(rollno);//userProfile.getUserRoll()); //change to name);
        dbref.push().setValue(Member);
       Toast.makeText(ScannerHistory.this, "data successfully added", LENGTH_SHORT).show();

    }
    public void localGet(String status, String location){
        String name = location;
        String date = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        ContentValues cv = new ContentValues();
        cv.put(MyCovidEntry.CovidEntry.COLUMN_CHECKED, status);
        cv.put(MyCovidEntry.CovidEntry.COLUMN_NAME, name);
        cv.put(MyCovidEntry.CovidEntry.COLUMN_MYTIME, date);
        mDatabase.insert(MyCovidEntry.CovidEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());
    }

    public Cursor getAllItems() {
        return mDatabase.query(
                MyCovidEntry.CovidEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MyCovidEntry.CovidEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}