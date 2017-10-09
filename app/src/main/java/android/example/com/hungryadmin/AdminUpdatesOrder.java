package android.example.com.hungryadmin;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.data;
import static android.R.attr.publicKey;
import static android.R.attr.state_selected;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


public class AdminUpdatesOrder extends AppCompatActivity {
    public String recordName;
    public String count;
    public String date;
    private TextView orderCount;
    public final String rec = null;
    private EditText orderName;

    private Button updateOrderButton;
    private Button updateOrderButton2;
    private Button emailbutton;
    private TextView OBNC;
    private TextView countTextView;
    private TextView YEStextView;
    private TextView adminEmail;
    private TextView emailOrderName;

    private DatabaseReference mDatabase;
    private DatabaseReference countDatabase;
    private DatabaseReference totalCount;
    private DatabaseReference orderDatabase;
    private DatabaseReference historyref;
    private DatabaseReference order2;
    private DatabaseReference order;
    private DatabaseReference obncRef;
    private DatabaseReference obncrefchild;

    String temp = null;
    String obnc="";
    String yes="";
    String aemail="";
    String emailorder="";
    String notupdated;

    private TextView menuprompt;
    private Button buttonEdit;
    private LinearLayout defaultPrompt;
    private LinearLayout orderSend;

    private DatabaseReference updateCount, updateYesRef;
    private DatabaseReference adminEmailRef;

    private TextView textView;
    private TextView todayOrder;
    private TextView notUpdated;

    private LinearLayout orderNameSHOW;
    private LinearLayout orderNameHIDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_updates_order);

        orderNameSHOW = (LinearLayout) findViewById(R.id.orderNameSHOW);
        orderNameHIDE = (LinearLayout) findViewById(R.id.orderNameHIDE);

        notUpdated = (TextView) findViewById(R.id.notUpdated);

        String notupdated = "Today's Order is Not Yet Updated";

        notUpdated.setText(notupdated);

        YEStextView = (TextView) findViewById(R.id.YEStextView);
        menuprompt = (TextView) findViewById(R.id.menuprompt);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);

        defaultPrompt = (LinearLayout) findViewById(R.id.defaultPrompt);
        orderSend = (LinearLayout) findViewById(R.id.orderSend);

        orderCount = (TextView) findViewById(R.id.orderCount);
//        final String count=orderCount.getText().toString().trim();
        orderName = (EditText) findViewById(R.id.orderName);
        updateOrderButton = (Button) findViewById(R.id.updateOrderButton);

        emailbutton = (Button) findViewById(R.id.emailbutton);

        OBNC = (TextView) findViewById(R.id.OBNCtextView);
        countTextView = (TextView) findViewById(R.id.countTextView);
        adminEmail = (TextView) findViewById(R.id.adminEmail);
        emailOrderName = (TextView) findViewById(R.id.emailOrderName);

        todayOrder = (TextView) findViewById(R.id.todayOrder);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        countDatabase = mDatabase.child("COUNT");
        totalCount = countDatabase.child("TotalOrdersCount");
        orderDatabase = mDatabase.child("ORDER").child("CurrentOrder");
        order2 = mDatabase.child("ORDER").child("CurrentOrder");
        obncRef = countDatabase.child("OrderedButNotConsumed");
        obncrefchild = obncRef;
        adminEmailRef = mDatabase.child("ADMIN");

        String key=obncRef.push().getKey();
        updateCount = countDatabase.child("TotalOrdersCount");
        updateYesRef = countDatabase.child("YesUsers");

        historyref = mDatabase.child("ORDER").child("OrderRecords");

        Calendar c = Calendar.getInstance();
        final String cmonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        final GregorianCalendar calendar1 = new GregorianCalendar();
        String day = String.valueOf(calendar1.get(Calendar.DATE));
        String year = String.valueOf(calendar1.get(Calendar.YEAR));

        final String date = day + " " + cmonth + " " + year;

        orderNameHIDE.setVisibility(View.GONE);
        orderNameSHOW.setVisibility(View.VISIBLE);

        updateOrderButton.setEnabled(true);

        //  Toast.makeText(AdminUpdatesOrder.this, "FIRST", Toast.LENGTH_SHORT).show();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultPrompt.setVisibility(View.GONE);
                orderSend.setVisibility(View.VISIBLE);
            }
        });

        final ValueEventListener valueEventListener = totalCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   Toast.makeText(AdminUpdatesOrder.this, "SECOND", Toast.LENGTH_SHORT).show();
                count = dataSnapshot.getValue().toString().trim();
                orderCount.setText(count);
                countTextView.setText(count);
//                Toast.makeText(AdminUpdatesOrder.this, "WE JUST SET COUNT TEXT " + countTextView.getText(), Toast.LENGTH_SHORT).show();

//                historyref.push().setValue(count);
//                historyref.child("Date").setValue(date);
                int time = calendar1.get(Calendar.HOUR_OF_DAY);
                int min = calendar1.get(Calendar.MINUTE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ValueEventListener valueEventListener0 = updateYesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    getYes getyes = postsnapshot.getValue(getYes.class);
                    yes += getyes.getUserdetails() + "\n";

                }

                YEStextView.setText(yes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ValueEventListener valueEventListener1 = obncRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                   getObnc getobnc = postsnapshot.getValue(getObnc.class);
                   obnc += getobnc.getUserdetails() + "\n";
               }
                OBNC.setText(obnc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ValueEventListener valueEventListener2 = adminEmailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    android.example.com.hungryadmin.adminEmail adminemail = postsnapshot.getValue(android.example.com.hungryadmin.adminEmail.class);
                    aemail += adminemail.getEmail() + ",";
                }

                adminEmail.setText(aemail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ValueEventListener valueEventListener3 = orderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                emailorder = dataSnapshot.getValue().toString().trim();
                emailOrderName.setText(emailorder);

                String totalorder = "Today's Order : " + emailorder;
                todayOrder.setText(totalorder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        totalCount.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                int hour = calendar1.get(Calendar.HOUR_OF_DAY);
//                int min = calendar1.get(Calendar.MINUTE);
//
//
//                if (hour==20 && min >40 && min <42) {
//                    Toast.makeText(AdminUpdatesOrder.this, "inside time" + hour, Toast.LENGTH_SHORT).show();
//                    HashMap<String, String> h = new HashMap<String, String>();
//                    h.put("date", date);
//                    h.put("count", count);
//                     historyref.push().setValue(h);
//////                    RecordStructure recordStructure = new RecordStructure(count, date);
////                    historyref.push().setValue(recordStructure);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        updateOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                defaultPrompt.setVisibility(View.VISIBLE);
                orderSend.setVisibility(View.GONE);

                final String order = orderName.getText().toString().trim();
                if (!order.trim().equals("")) {
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("CurrentOrder", order);

                    emailorder = order;
                    emailOrderName.setText(emailorder);

                    temp = recordName;


                    orderDatabase.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminUpdatesOrder.this, "Order Updated", Toast.LENGTH_SHORT);
                                orderName.setText("");
                            } else {
                                Toast.makeText(AdminUpdatesOrder.this, "Order Not Updated", Toast.LENGTH_SHORT);
                                orderName.setText("");
                            }
                        }
                    });
                }
            }
        });

        GregorianCalendar calendar = new GregorianCalendar();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time < 8 || time > 12) {
            Toast.makeText(this, "You can no longer update your order now!", Toast.LENGTH_SHORT).show();
            updateOrderButton.setEnabled(false);
        }


        int mins = calendar.get(Calendar.MINUTE);

        if (time == 23) {
            updateYesRef.removeValue();
            obncRef.removeValue();
            orderNameHIDE.setVisibility(View.VISIBLE);
            orderNameSHOW.setVisibility(View.GONE);
//            updateCount.setValue("0");

            updateCount.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Integer currentValue = mutableData.getValue(Integer.class);
                    currentValue = 0;
                    mutableData.setValue(currentValue);


                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                }


            });

        }

        if (time < 8 || time > 10) {

            updateOrderButton.setEnabled(false);
        }

        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalCount = countTextView.getText().toString();
                String obncfinal=OBNC.getText().toString();
                String htmlFormat = " <html><body>\n" +
                        " <strong>This is bold</strong>\n" +
                        "This is not bold\n" +
                        "</body>\n" +
                        " </html>";
                String yesfinal = YEStextView.getText().toString();
                String aemailfinal = adminEmail.getText().toString();
                String currentorder = emailOrderName.getText().toString();

                Intent email=new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/html");
                String[] to = {aemailfinal};
//                email.putExtra(Intent.EXTRA_EMAIL,"aakash.alurkar95@gmail.com");
                email.putExtra(Intent.EXTRA_EMAIL, to);
                email.putExtra(Intent.EXTRA_SUBJECT,"REPORT OF " + date);

//                email.putExtra(Intent.EXTRA_TEXT,"YOLO CHANGE");

                email.putExtra(Intent.EXTRA_TEXT,
                        Html.fromHtml(new StringBuilder()
                        .append("<head><b>This is bold text     </b></head>")
                        .append("<head><i>This is SIDDHESH text</i></head>")
                        .toString())
                        );

                ////THIS IS FINAL PUT EXTRA BELOW
//                email.putExtra(Intent.EXTRA_TEXT,
//                        "THE COUNT IS " +
//                                finalCount +
//                                "\n\n" +
//                                "THE ORDER NAME IS " +
//                                "\n" +
//                                currentorder +
//                                "\n\n" +
//                                "ORDERED BUT NOT EATEN: " +
//                                "\n\n" +
//                                obncfinal +
//                                "\n" +
//                                "USERS WHO ORDERED: " +
//                                "\n\n" +
//                                yesfinal +
//                                "\n");


                if(email.resolveActivity(getPackageManager())!=null)
                    startActivity(email);
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
                Intent yes = new Intent(AdminUpdatesOrder.this, YesUsersActivity.class);
                startActivity(yes);
                finish();
                break;

            case R.id.allUsers:
                Intent all = new Intent(AdminUpdatesOrder.this, UsersActivity.class);
                startActivity(all);
                finish();
                break;

            case R.id.orderAsAdmin:
                Intent order = new Intent(AdminUpdatesOrder.this, OrderAsAdmin.class);
                startActivity(order);
                finish();
                break;
        }
        return true;
    }

}
