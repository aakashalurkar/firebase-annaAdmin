package android.example.com.hungryadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class obncActivity extends AppCompatActivity {

    private RecyclerView obncList;
    private DatabaseReference mDatabase;
    private DatabaseReference countDatabase;
    private DatabaseReference obncRef;
    private DatabaseReference yesRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obnc);

        obncList=(RecyclerView) findViewById(R.id.obncList);
        obncList.setHasFixedSize(true);
        obncList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase= FirebaseDatabase.getInstance().getReference();
        countDatabase=mDatabase.child("COUNT");
        obncRef=countDatabase.child("OrderedButNotConsumed");
        yesRef=countDatabase.child("YesUsers");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<obnc_structure, obncViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<obnc_structure, obncViewHolder>(

                obnc_structure.class,
                R.layout.obnc_single_layout,
                obncViewHolder.class,
                obncRef


        ) {
            @Override
            protected void populateViewHolder(obncViewHolder viewHolder, obnc_structure model, int position) {


                viewHolder.setUsername(model.getUserdetails());
                final String key = getRef(position).getKey();
                viewHolder.obncView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showConfirmDialog2(key);
                        return false;

                    }

                    private void showConfirmDialog2(final String key) {

                        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(obncActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogview = inflater.inflate(R.layout.confirm2, null);
                        dialogbuilder.setView(dialogview);
                        final Button confirmbutton2 = (Button) dialogview.findViewById(R.id.confirmButton2);
                        dialogbuilder.setTitle("USER HAS CONSUMED");
                        final AlertDialog alert = dialogbuilder.create();
                        alert.show();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        confirmbutton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference copyRef = obncRef.child(key);
                                moveFirebaseRecord(copyRef, yesRef);
                                copyRef.removeValue();
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
                                                    Toast.makeText(obncActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(obncActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
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

        obncList.setAdapter(firebaseRecyclerAdapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("obnc", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }

    public static class obncViewHolder extends RecyclerView.ViewHolder{

        View obncView;

        public obncViewHolder(View itemView) {
            super(itemView);

            obncView=itemView;
        }

        public void setUsername(String username) {

            TextView obncNameView = (TextView) obncView.findViewById(R.id.obnc_single_name);
            obncNameView.setText(username);
        }
    }
}
