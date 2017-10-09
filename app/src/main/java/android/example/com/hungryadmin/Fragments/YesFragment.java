package android.example.com.hungryadmin.Fragments;

import android.app.Fragment;
import android.example.com.hungryadmin.R;
import android.example.com.hungryadmin.YesUserStructure;
import android.example.com.hungryadmin.YesUsersActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Aakash on 07-10-2017.
 */

public class YesFragment extends android.support.v4.app.Fragment {
    private DatabaseReference mDatabase;
    private DatabaseReference countDatabase;
    private DatabaseReference yesUserDatabase;
    private DatabaseReference adminDatabase;
    private DatabaseReference updateYesRef;
    private DatabaseReference updateCount;
    private DatabaseReference copyRef;
    private DatabaseReference orderbutnotTaken;
    private DatabaseReference notConsumeRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        orderbutnotTaken = mDatabase.child("COUNT").child("OrderedButNotConsumed");
        notConsumeRef = orderbutnotTaken.child("COUNT").child("OrderedButNotConsumed");
        countDatabase = mDatabase.child("COUNT");
        updateCount = countDatabase.child("TotalOrdersCount");
        updateYesRef = countDatabase.child("YesUsers");
        yesUserDatabase = countDatabase.child("YesUsers");
        adminDatabase = mDatabase.child("ADMIN");
        View yesv= inflater.inflate(R.layout.activity_yes_users,null);
       RecyclerView ryesv=(RecyclerView) yesv.findViewById(R.id.yes_users_list);

        ryesv.setHasFixedSize(true);
        ryesv.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        Query query = yesUserDatabase.orderByChild("userdetails");
        FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder> firebaseYesRecyclerAdapter =
                new FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder>(

                        YesUserStructure.class,
                        R.layout.yes_users_single_layout,
                        YesUsersViewHolder.class,
                        query
                ) {
                    @Override
                    protected void populateViewHolder(final YesUsersViewHolder viewHolder, final YesUserStructure yesUserStructure, final int position) {

//              viewHolder.setUsername(model.getName());
                        viewHolder.setUsername(yesUserStructure.getUserdetails());
                        final String key = getRef(position).getKey();
                        viewHolder.mYesView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                showConfirmDialog(key);
                                return false;
                            }

                            private void showConfirmDialog(final String key) {
                                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                final View dialogview = inflater.inflate(R.layout.confirm_dialog, null);
                                dialogbuilder.setView(dialogview);
                                final Button confirmbutton = (Button) dialogview.findViewById(R.id.confirmButton);
                                dialogbuilder.setTitle("NOT CONSUMED");
                                final AlertDialog alert = dialogbuilder.create();
                                alert.show();
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                confirmbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        copyRef = yesUserDatabase.child(key);
                                        moveFirebaseRecord(copyRef, orderbutnotTaken);
                                        copyRef.removeValue();
//                                        copyRef.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                String Name=dataSnapshot.child("name").getValue().toString();
//                                                orderbutnotTaken.push().setValue(Name);
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });


                                        alert.dismiss();
                                    }

                                    private void moveFirebaseRecord(final DatabaseReference copyRef, final DatabaseReference pasteRef) {

                                        copyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                pasteRef.push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Not Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                            }
                        });

                    }


                };

        ryesv.setAdapter(firebaseYesRecyclerAdapter);
        return yesv;
    }


    //title of fragment


    @Override
    public String toString() {
        return "YES USERS";
    }


    public static class YesUsersViewHolder extends RecyclerView.ViewHolder {

        View mYesView;

        public YesUsersViewHolder(View itemView) {
            super(itemView);

            mYesView = itemView;
        }

        public void setUsername(String username) {

            TextView yesUserNameView = (TextView) mYesView.findViewById(R.id.yes_user_single_name);
            yesUserNameView.setText(username);
        }

    }
}
