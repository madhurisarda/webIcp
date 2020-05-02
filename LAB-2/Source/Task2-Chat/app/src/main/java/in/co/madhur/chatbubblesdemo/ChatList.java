package in.co.madhur.chatbubblesdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import in.co.madhur.chatbubblesdemo.model.ChatMessage;
import in.co.madhur.chatbubblesdemo.model.Profile;

import static in.co.madhur.chatbubblesdemo.MainActivity.MESSAGES_CHILD;

public class ChatList extends ListFragment {
    public static final int REQUEST_READ_CONTACTS = 79;
    ListView list;
    ArrayList mobileArray;
    final ArrayList<String> nameList = new ArrayList<>();
    String number;
    DatabaseReference messagesRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.contacts_list);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            //mobileArray = getAllContacts();
        } else {
            requestPermission();
        }
        //list = (ListView)getActivity().findViewById(R.id.);

        return inflater.inflate(R.layout.contacts_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        messagesRef = mFirebaseDatabaseReference.child("users");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Profile track = postSnapshot.getValue(Profile.class);
                    nameList.add(track.getemail().toString());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, nameList);
                setListAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ChatList txt = (ChatList)this;
        String phn=nameList.get(position).toString();
        number=phn;
        txt.makecall();

        //getListView().setSelector(android.R.color.holo_blue_dark);
    }
    public void makecall(){

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("id", number);
        startActivity(intent);


    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //mobileArray = getAllContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }

                return;
            }
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makecall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;
        }
    }
//    private ArrayList getAllContacts() {
//
//
//
//    }
}
