package android.example.com.hungryadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YesUsersActivity extends AppCompatActivity {

    private RecyclerView mYesUsersList;

    private DatabaseReference mDatabase;
    private DatabaseReference countDatabase;
    private DatabaseReference yesUserDatabase;
    private DatabaseReference adminDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_users);

        mYesUsersList = (RecyclerView) findViewById(R.id.yes_users_list);
        mYesUsersList.setHasFixedSize(true);
        mYesUsersList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        countDatabase = mDatabase.child("COUNT");
        yesUserDatabase = countDatabase.child("YesUsers");
        adminDatabase = mDatabase.child("ADMIN");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder> firebaseYesRecyclerAdapter =
                new FirebaseRecyclerAdapter<YesUserStructure, YesUsersViewHolder>(

                        YesUserStructure.class,
                        R.layout.yes_users_single_layout,
                        YesUsersViewHolder.class,
                        yesUserDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final YesUsersViewHolder viewHolder, final YesUserStructure yesUserStructure, final int position) {

//              viewHolder.setUsername(model.getName());
                        viewHolder.setUsername(yesUserStructure.getName());
                    }
                };

        mYesUsersList.setAdapter(firebaseYesRecyclerAdapter);
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
                Intent todayUsersintent = new Intent(YesUsersActivity.this, YesUsersActivity.class);
                startActivity(todayUsersintent);
                break;

            case R.id.allUsers:
                Intent allUsersintent = new Intent(YesUsersActivity.this, UsersActivity.class);
                startActivity(allUsersintent);
                break;
        }
        return true;
    }

}
