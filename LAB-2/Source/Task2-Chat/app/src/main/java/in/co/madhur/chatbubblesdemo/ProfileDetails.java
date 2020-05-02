package in.co.madhur.chatbubblesdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import in.co.madhur.chatbubblesdemo.R;
import in.co.madhur.chatbubblesdemo.model.ChatMessage;
import in.co.madhur.chatbubblesdemo.model.Profile;

public class ProfileDetails extends Fragment{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference userdetails;

    EditText firstname,lastname,email;
    ImageView photo;

    public ProfileDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_edit, container, false);
        firstname=view.findViewById(R.id.pFirstName);
        lastname=view.findViewById(R.id.pLastName);
        email=view.findViewById(R.id.pEmail);
        photo=view.findViewById(R.id.pUserImage);

        setData();
        firstname.setText("Madhuri");
        lastname.setText("Sarda");
        email.setText("madhurisarda32@gmail.com");
        // Inflate the layout for this fragment
        return view;
    }
    public void setData(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String email=mFirebaseUser.getEmail();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        userdetails = mFirebaseDatabaseReference.child("users");

    }

    @Override
    public void onStart() {
        super.onStart();

        userdetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Profile track = postSnapshot.getValue(Profile.class);
                    Log.d("profile",track.getemail());
                    if(track.getemail()=="madhurisarda32@gmail.com") {
                        firstname.setText(track.getfirstname());
                        lastname.setText(track.getlastname());
                        email.setText(track.getemail());
                        //Fragment currentFragment = getActivity().getFragmentManager().findFragmentById(R.id.);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
