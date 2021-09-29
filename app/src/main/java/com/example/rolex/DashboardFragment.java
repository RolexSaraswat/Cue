package com.example.rolex;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;


import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import java.util.concurrent.Executor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.*;

import androidx.fragment.app.Fragment;
import com.example.rolex.period_days.PeriodDaysManager;
import org.jetbrains.annotations.NotNull;
import com.example.rolex.util.AppPreferences;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardFragment extends Fragment {


TextView name;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    String  userId;

TextView welcome,age;
    ImageView flag;
    CircleImageView photo;
Button skip , update;
String mParam1;
FloatingActionButton whatsappa,twitter,facebook,insta;
    private long pressedTime;
    private PeriodDaysManager manager;
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

            ViewGroup root =(ViewGroup) inflater.inflate(R.layout.dashboard_fragment , container, false);
        skip =root.findViewById(R.id.prefrences);
        update=root.findViewById(R.id.updatee);
        name=root.findViewById(R.id.quot);
        fAuth = FirebaseAuth.getInstance();
    photo= root.findViewById(R.id.imageView8);
        age=root.findViewById(R.id.age);
        whatsappa=root.findViewById(R.id.whatsapp);
        facebook=root.findViewById(R.id.facebook);  twitter=root.findViewById(R.id.twitter);  insta=root.findViewById(R.id.insta);
        flag= root.findViewById(R.id.flag);
        fStore = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString("params");
        }
        if(fAuth.getCurrentUser() != null){
        userId = fAuth.getCurrentUser().getUid();}
welcome=root.findViewById(R.id.welcome);
        if(fAuth.getCurrentUser() != null){
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    // work with the snapshot here
                String a = value.getString("country");
                if (a == "India") {
                    flag.setImageResource(R.drawable.india);

                } else if (a == "USA") {
                    flag.setImageResource(R.drawable.usa);
                } else if (a == "China") {
                    flag.setImageResource(R.drawable.china);
                } else if (a == "Japan") {
                    flag.setImageResource(R.drawable.japan);
                }

                age.setText("Age: " + value.getLong("age") + "   " + value.getString("country"));
                welcome.setText(value.getString("fName"));

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference photoReference = storageReference.child("images/" + userId);

                final long ONE_MEGABYTE = 1024 * 1024;
                photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        photo.setImageBitmap(bmp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                       photo.setImageResource(R.drawable.dp);
                    }
                });


            }else {
                    Log.e("TAG", "Error", error);
                }
            }

        });}






        String[] quootes= root.getResources().getStringArray(R.array.names);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CalendarActivity.class);
                Random random = new Random();
                int i= random.nextInt(quootes.length);

                name.setText(quootes[i]);
                startActivity(intent);

            }



        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),signupform.class);
                Random random = new Random();
                int i= random.nextInt(quootes.length);

                name.setText(quootes[i]);

                startActivity(intent);

            }



        });
        whatsappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent whatsappshare=new Intent(Intent.ACTION_SEND);
                    whatsappshare.setType("text/plane");
                    whatsappshare.setPackage("com.whatsapp");
                    whatsappshare.putExtra(Intent.EXTRA_TEXT,"hello");
                    getActivity().startActivity(whatsappshare);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appName = "org.telegram.messenger";;


                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    myIntent.setPackage(appName);
                    myIntent.putExtra(Intent.EXTRA_TEXT,"hello");//
                    getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));


            }



        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri file = Uri.parse("android.resource://com.example.rolex/"+R.drawable.butter);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM,file);
                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                shareIntent.setPackage("com.instagram.android");
                getActivity().startActivity(shareIntent);
            }



        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "hello";
                Uri uri = Uri
                        .parse("android.resource://com.example.rolex/drawable/butter");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/jpeg");
                intent.setPackage("com.twitter.android");
                startActivity(intent);
            }



        });



        return root;
    }




}
