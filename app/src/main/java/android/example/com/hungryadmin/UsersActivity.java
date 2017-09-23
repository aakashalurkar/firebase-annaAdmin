package android.example.com.hungryadmin;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.key;
import static android.example.com.hungryadmin.R.id.orderCount;

public class UsersActivity extends AppCompatActivity {

    private TextView deletePrompt;

    private RecyclerView mUsersList;
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;
    private DatabaseReference deleteRef;
    private DatabaseReference username;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("USERS");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<UserStructure, UsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UserStructure, UsersViewHolder>(

                        UserStructure.class,
                        R.layout.user_single_layout,
                        UsersViewHolder.class,
                        userDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final UsersViewHolder usersViewHolder, final UserStructure userStructure, int position) {

                        usersViewHolder.setName(userStructure.getName());
                        usersViewHolder.setMobile(userStructure.getMobile());

                        final String key = getRef(position).getKey();
                        usersViewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                showDeleteDialog(key);
                                return false;
                            }
                        });
                    }
                };

        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setMobile(String mobile) {

            TextView userMobileView = (TextView) mView.findViewById(R.id.user_single_mobile);
            userMobileView.setText(mobile);
        }

    }

    private void showDeleteDialog(final String key) {
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(UsersActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogview = inflater.inflate(R.layout.delete_dialog, null);
        dialogbuilder.setView(dialogview);
        final Button deletebutton = (Button) dialogview.findViewById(R.id.deletebutton);
        dialogbuilder.setTitle("DELETING USER");
        final AlertDialog alert = dialogbuilder.create();
        alert.show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRef = userDatabase.child(key);
                deleteRef.removeValue();
                alert.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.yesUsers:
                Intent todayUsersintent = new Intent(UsersActivity.this, YesUsersActivity.class);
                startActivity(todayUsersintent);
                finish();
                break;

            case R.id.allUsers:
                Intent allUsersintent = new Intent(UsersActivity.this, UsersActivity.class);
                startActivity(allUsersintent);
                finish();
                break;
        }
        return true;
    }
}

