package com.example.kangai.Firebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Map;


public class FirebaseData {
    private DatabaseReference databaseRef;

    public FirebaseData() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public interface FirebaseDataCallback {
        void onDataReceived(DataSnapshot dataSnapshot);
    }

    // REAL-TIME DATABASE

    public void retrieveData(Context context, String childPath, final FirebaseDataCallback callback) {
        DatabaseReference childRef = databaseRef.child(childPath);
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Pass the retrieved data to the callback method
                callback.onDataReceived(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error fetching data. Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addValue(String childPath, Object value) {
        DatabaseReference childRef = databaseRef.child(childPath);
        childRef.setValue(value);
    }

    public void updateValue(String childPath, Object value) {
        DatabaseReference childRef = databaseRef.child(childPath);
        childRef.setValue(value);
    }

    public void addValues(String childPath, Map<String, Object> value){
        DatabaseReference childRef = databaseRef.child(childPath);
        childRef.updateChildren(value);
    }

    public void updateValues(String childPath, Map<String, Object> value){
        DatabaseReference childRef = databaseRef.child(childPath);
        childRef.updateChildren(value);
    }

    public void removeData(String childpath){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nodeRef = databaseRef.child(childpath);
        nodeRef.removeValue();
    }

    // IMAGE

    public enum imageType{
        Device, User, Slots
    }

    private String getImageChild(imageType type){
        switch (type){
            case Device: return "Devices/Device/";
            case User: return "Users/";
            case Slots: return "Devices/Slots/";
            default: return "/";
        }
    }


    public void uploadImage(Drawable drawable, String imageName, imageType type){
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(getImageChild(type)+imageName+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {}
        });

    }

    private void ShowImage(String imageName, ImageView view, imageType type){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(getImageChild(type)).child(imageName+".jpg");
        if (imageRef == null){
            imageRef = storage.getReference().child("products").child(imageName+".png");
        }
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(view);
            }
        });
    }
}
