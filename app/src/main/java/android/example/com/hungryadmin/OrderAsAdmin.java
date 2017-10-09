package android.example.com.hungryadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OrderAsAdmin extends AppCompatActivity {

    private TextView NameOfOrder;
    private TextView orderChoice;

    private DatabaseReference mainref;
    private DatabaseReference ordernameref;
    private DatabaseReference countref;
    private DatabaseReference yesref;
    private DatabaseReference yesbufferRef;
    private DatabaseReference noRef;

    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();

    private RadioGroup rg;
    private RadioButton radioYes;
    private RadioButton radioNo;

    private TextView nameOfOrder;
    private TextView notUpdatedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_as_admin);


        nameOfOrder = (TextView) findViewById(R.id.NameOfOrder);
        notUpdatedOrder = (TextView) findViewById(R.id.notUpdatedOrder);

        radioYes = (RadioButton) findViewById(R.id.radioYES);
        radioNo = (RadioButton) findViewById(R.id.radioNO);

        NameOfOrder = (TextView) findViewById(R.id.NameOfOrder);
        mainref = FirebaseDatabase.getInstance().getReference();
        ordernameref = mainref.child("ORDER").child("CurrentOrder");
        countref = mainref.child("COUNT").child("TotalOrdersCount");
        yesref = mainref.child("COUNT").child("YesUsers");
        noRef = mainref.child("COUNT").child("YesUsers");


        final String userFireid = fireUser.getUid();

        yesbufferRef = mainref.child("ADMIN").child(userFireid).child("userdetails");

        orderChoice = (TextView) findViewById(R.id.orderChoice);

        final String yes = "YES";
        final String no = "NO";


        ordernameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentmenu = dataSnapshot.getValue().toString();
                NameOfOrder.setText(currentmenu);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        noorderButton.setEnabled(false);
//        orderChoice.setText(no);

//        radioYes.setChecked(false);
//        radioNo.setChecked(false);

//        radioNo.setEnabled(false);
        radioYes.setEnabled(true);

        radioYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                orderChoice.setText(yes);

                radioNo.setChecked(false);
                radioYes.setChecked(false);

                radioNo.setEnabled(true);
                radioYes.setEnabled(false);

//                radioNo.setChecked(false);
//                radioYes.setChecked(true);
//                radioYes.setEnabled(false);
//                radioNo.setEnabled(true);


                yesbufferRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userdetails = dataSnapshot.getValue().toString().trim();
                        saveYesChoiceUser(userdetails);

                    }


                    private void saveYesChoiceUser(String uname) {

                        YesUserStructure yesuser = new YesUserStructure(uname);
                        yesref.child(userFireid).setValue(yesuser);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                countref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer currentValue = mutableData.getValue(Integer.class);
                        if (currentValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentValue + 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }


                });
            }
        });


        radioNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                orderChoice.setText(no);


//                radioNo.setEnabled(false);
//                radioYes.setEnabled(true);
                radioYes.setChecked(false);
                radioNo.setChecked(false);
//                radioYes.setChecked(false);
//                radioNo.setChecked(true);
                radioNo.setEnabled(false);
                radioYes.setEnabled(true);
                noRef.child(userFireid).removeValue();


                countref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer currentValue = mutableData.getValue(Integer.class);
                        if (currentValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentValue - 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }


                });
            }
        });

//        radioNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                noorderButton.setEnabled(false);
////                yesorderButton.setEnabled(true);
//                orderChoice.setText(no);
//                radioYes.setChecked(false);
//                radioNo.setEnabled(false);
//                radioYes.setEnabled(true);
//
//               noRef.child(userFireid).removeValue();
//
//
//                countref.runTransaction(new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        Integer currentValue = mutableData.getValue(Integer.class);
//                        if (currentValue == null) {
//                            mutableData.setValue(1);
//                        } else if (currentValue <0){
//                            mutableData.setValue(0);
//                        }else {
//                            mutableData.setValue(currentValue - 1);
//                        }
//
//                        return Transaction.success(mutableData);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//                    }
//
//
//                });
//            }
//        });

        GregorianCalendar calendar = new GregorianCalendar();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (time < 10 || time > 12 ) {

            Toast.makeText(this, "You cannot update your order now", Toast.LENGTH_SHORT).show();
            radioYes.setEnabled(false);
            radioNo.setEnabled(false);

            nameOfOrder.setVisibility(View.GONE);
            notUpdatedOrder.setVisibility(View.VISIBLE);
//            yesorderButton.setEnabled(false);
//            noorderButton.setEnabled(false);
        }


    }
}
