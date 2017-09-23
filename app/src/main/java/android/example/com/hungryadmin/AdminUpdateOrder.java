package android.example.com.hungryadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Aakash on 03-09-2017.
 */

public class AdminUpdateOrder extends AppCompatActivity {

    private Button updateOrderButton;
    private Button manageUsersButton;
    private EditText orderName;
    private TextView orderCount;

    private Toolbar mToolBar;

    private DatabaseReference mDatabase;
    private DatabaseReference countDatabase;
    private DatabaseReference orderDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminupdateorder);

        updateOrderButton = (Button) findViewById(R.id.updateOrderButton);

        orderName = (EditText) findViewById(R.id.orderName);
        orderCount = (TextView) findViewById(R.id.orderCount);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        countDatabase = mDatabase.child("COUNT").child("TotalOrderCount");
        orderDatabase = mDatabase.child("ORDER").child("CurrentOrder");

        orderCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminUpdateOrder.this,YesUsersActivity.class);
            }
        });

        final ValueEventListener valueEventListener = countDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String count = dataSnapshot.getValue().toString();
                orderCount.setText(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String order = orderName.getText().toString().trim();

                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("OrderName", order);

                orderDatabase.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminUpdateOrder.this, "Order Updated", Toast.LENGTH_SHORT).show();
                            orderName.setText("");
                        } else {
                            Toast.makeText(AdminUpdateOrder.this, "Order Not Updated", Toast.LENGTH_SHORT).show();
                            orderName.setText("");
                        }
                    }
                });

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
                Intent todayUsersintent = new Intent(AdminUpdateOrder.this, YesUsersActivity.class);
                startActivity(todayUsersintent);
                finish();
                break;

            case R.id.allUsers:
                Intent allUsersintent = new Intent(AdminUpdateOrder.this, UsersActivity.class);
                startActivity(allUsersintent);
                finish();
                break;
        }
        return true;
    }
}