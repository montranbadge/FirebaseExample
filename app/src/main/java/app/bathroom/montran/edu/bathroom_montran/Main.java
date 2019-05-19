package app.bathroom.montran.edu.bathroom_montran;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * @author jsaavedra
 *
 * Simple Firebase (Live Database) implementation
 */

public class Main extends AppCompatActivity implements View.OnClickListener {

    //views declaration
    private Button button_1_7;
    private Button button_2_7;
    private Button button_3_7;
    private Button button_4_7;
    private Button button_1_10;
    private Button button_2_10;
    private Button button_3_10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views association
        button_1_7 = findViewById(R.id.button_1_7);
        button_2_7 = findViewById(R.id.button_2_7);
        button_3_7 = findViewById(R.id.button_3_7);
        button_4_7 = findViewById(R.id.button_4_7);
        button_1_10 = findViewById(R.id.button_1_10);
        button_2_10 = findViewById(R.id.button_2_10);
        button_3_10 = findViewById(R.id.button_3_10);

        //setting an onclick listener to the buttons
        //we send "this" because we already implemented View.OnClickListener
        button_1_7.setOnClickListener(this);
        button_2_7.setOnClickListener(this);
        button_3_7.setOnClickListener(this);
        button_4_7.setOnClickListener(this);
        button_1_10.setOnClickListener(this);
        button_2_10.setOnClickListener(this);
        button_3_10.setOnClickListener(this);

        //on the creation of this activity, start listening to the Database
        listenAllButtons();

    }

    /**
     * This function writes a message in a given Node, and then stops the listener (to prevent
     * infinite loops of changes in database in this reference
     * @param node
     * @param message
     * @param listener
     */
    public void writeMessage(String node, String message, ValueEventListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getResources().getString(R.string.node_name)+node);
        // Write a message to the database
        myRef.setValue(message);
        myRef.removeEventListener(listener);
    }

    public void listenAllButtons(){
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(button_1_7);
        buttonList.add(button_2_7);
        buttonList.add(button_3_7);
        buttonList.add(button_4_7);
        buttonList.add(button_1_10);
        buttonList.add(button_2_10);
        buttonList.add(button_3_10);

        for(Button button: buttonList){
            setBathroomColor(button);
        }
    }

    public void setBathroomStatus(final View v){
        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getResources().getString(R.string.node_name)+v.getId());
        // Read from the database
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value.equals(getResources().getString(R.string.busy_bathroom_status))){
                    writeMessage(""+v.getId(), getResources().getString(R.string.free_bathroom_status), this);
                } else{
                    writeMessage(""+v.getId(), getResources().getString(R.string.busy_bathroom_status), this);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Main.this, "Failed to Read Value", Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addValueEventListener(listener);

    }

    public void setBathroomColor(final View v){
        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getResources().getString(R.string.node_name)+v.getId());
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value.equals("1")){
                    ((Button) v).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else{
                    ((Button) v).setBackgroundResource(android.R.drawable.btn_default);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Main.this, "Failed to Read Value", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        setBathroomStatus(v);
    }
}
