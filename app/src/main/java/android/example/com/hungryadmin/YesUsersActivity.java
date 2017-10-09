package android.example.com.hungryadmin;

import android.content.Intent;
import android.example.com.hungryadmin.Fragments.YesFragment;
//import android.example.com.hungryadmin.Fragments.oFrag;
import android.example.com.hungryadmin.Fragments.yFrag2;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.R.attr.y;

public class YesUsersActivity extends AppCompatActivity {

//    private RecyclerView mYesUsersList;
//
//    private DatabaseReference mDatabase;
//    private DatabaseReference countDatabase;
//    private DatabaseReference yesUserDatabase;
//    private DatabaseReference adminDatabase;
//    private DatabaseReference updateYesRef;
//    private DatabaseReference updateCount;
//    private DatabaseReference copyRef;
//    private DatabaseReference orderbutnotTaken;
//    private DatabaseReference notConsumeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_users);

//        mYesUsersList = (RecyclerView) findViewById(R.id.yes_users_list);
//        mYesUsersList.setHasFixedSize(true);
//        mYesUsersList.setLayoutManager(new LinearLayoutManager(this));
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        orderbutnotTaken = mDatabase.child("COUNT").child("OrderedButNotConsumed");
//        notConsumeRef = orderbutnotTaken.child("COUNT").child("OrderedButNotConsumed");
//        countDatabase = mDatabase.child("COUNT");
//        updateCount = countDatabase.child("TotalOrdersCount");
//        updateYesRef = countDatabase.child("YesUsers");
//        yesUserDatabase = countDatabase.child("YesUsers");
//        adminDatabase = mDatabase.child("ADMIN");
        ViewPager vp= (ViewPager) findViewById(R.id.viewPagerID);
        this.addPages(vp);

        TabLayout tabLayout= (TabLayout) findViewById(R.id.Tab_id);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(listener(vp));

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Query query = yesUserDatabase.orderByChild("name");
//        FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder> firebaseYesRecyclerAdapter =
//                new FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder>(
//
//                        YesUserStructure.class,
//                        R.layout.yes_users_single_layout,
//                        YesUsersViewHolder.class,
//                        query
//                ) {
//                    @Override
//                    protected void populateViewHolder(final YesUsersViewHolder viewHolder, final YesUserStructure yesUserStructure, final int position) {
//
////              viewHolder.setUsername(model.getName());
//                        viewHolder.setUsername(yesUserStructure.getUserdetails());
//                        final String key = getRef(position).getKey();
//                        viewHolder.mYesView.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View v) {
//
//                                showConfirmDialog(key);
//                                return false;
//                            }
//
//                            private void showConfirmDialog(final String key) {
//                                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(YesUsersActivity.this);
//                                LayoutInflater inflater = getLayoutInflater();
//                                final View dialogview = inflater.inflate(R.layout.confirm_dialog, null);
//                                dialogbuilder.setView(dialogview);
//                                final Button confirmbutton = (Button) dialogview.findViewById(R.id.confirmButton);
//                                dialogbuilder.setTitle("NOT CONSUMED");
//                                final AlertDialog alert = dialogbuilder.create();
//                                alert.show();
//                                mDatabase = FirebaseDatabase.getInstance().getReference();
//                                confirmbutton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Toast.makeText(YesUsersActivity.this, "Inside onClick", Toast.LENGTH_SHORT).show();
//
//                                        copyRef = yesUserDatabase.child(key);
//                                        moveFirebaseRecord(copyRef, orderbutnotTaken);
//                                        copyRef.removeValue();
////                                        copyRef.addValueEventListener(new ValueEventListener() {
////                                            @Override
////                                            public void onDataChange(DataSnapshot dataSnapshot) {
////                                                String Name=dataSnapshot.child("name").getValue().toString();
////                                                orderbutnotTaken.push().setValue(Name);
////
////                                            }
////
////                                            @Override
////                                            public void onCancelled(DatabaseError databaseError) {
////
////                                            }
////                                        });
//
//
//                                        alert.dismiss();
//                                    }
//
//                                    private void moveFirebaseRecord(final DatabaseReference copyRef, final DatabaseReference pasteRef) {
//
//                                        copyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                pasteRef.push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            Toast.makeText(YesUsersActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                                        } else {
//                                                            Toast.makeText(YesUsersActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                });
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                });
//
//
//                            }
//                        });
//
//                    }
//                };
//
//        mYesUsersList.setAdapter(firebaseYesRecyclerAdapter);




    }

//    public static class YesUsersViewHolder extends RecyclerView.ViewHolder {
//
//        View mYesView;
//
//        public YesUsersViewHolder(View itemView) {
//            super(itemView);
//
//            mYesView = itemView;
//        }
//
//        public void setUsername(String username) {
//
//            TextView yesUserNameView = (TextView) mYesView.findViewById(R.id.yes_user_single_name);
//            yesUserNameView.setText(username);
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.yesusers, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.yesUsers:
                Intent yes = new Intent(YesUsersActivity.this, UsersActivity.class);
                startActivity(yes);
                finish();
                break;

            case R.id.allUsers:
                Intent all = new Intent(YesUsersActivity.this, AdminUpdatesOrder.class);
                startActivity(all);
                finish();
                break;


        }
        return true;
    }


    private void addPages(ViewPager pager)
    {
        FragPageAdapter adapter=new FragPageAdapter(getSupportFragmentManager());
adapter.addPage(new YesFragment());
    adapter.addPage(new oFrag());
        pager.setAdapter(adapter);

    }

    private TabLayout.OnTabSelectedListener listener(final ViewPager pager)
    {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}
