package com.tara.tara;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tara.tara.models.Comment;
import com.tara.tara.models.FoodItem;
import com.tara.tara.models.FoodTable;
import com.tara.tara.models.Hotel;
import com.tara.tara.models.HotelOrder;
import com.tara.tara.models.User;
import com.tara.tara.models.UserOrder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    private FirebaseDatabase fDatabase;

    private int users=0;
    private int hotels=0;
    private int foods=0;
    private int orders=0;

    private String userId,hotelId,tableId;
    private List<String> foodIds;
    private String orderId;

    private Button adduserBtn,addHotel,addOrder,addComment,addCatImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fDatabase = FirebaseDatabase.getInstance();
        adduserBtn = (Button)findViewById(R.id.addUser);
        adduserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        addHotel = (Button)findViewById(R.id.addHotel);
        addHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHotel();
            }
        });

        addOrder = (Button)findViewById(R.id.addOrder);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
            }
        });

        addComment = (Button)findViewById(R.id.addComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        addCatImage = (Button)findViewById(R.id.addCatImage);
        addCatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToCategory(hotelId);
            }
        });

    }

    private void addUser(){
        users++;
        DatabaseReference fDBReference=fDatabase.getReference("users");
        String userKey=fDBReference.push().getKey();
        User user = new User("User"+users,"a"+users+"@b.com","fid"+users);
        fDBReference.child(userKey).setValue(user);
        System.out.println("Added user"+users);

        userId = userKey;
    }

    private void addHotel(){
        hotels++;
        DatabaseReference fDBReference=fDatabase.getReference("hotels");
        String hotelKey=fDBReference.push().getKey();

        Map offers=new HashMap<String,String>();
        offers.put("offer1","offers1.jpg");
        offers.put("offer2","offers2.jpg");

        Hotel hotel=new Hotel("hotel"+hotels,hotels,hotels,offers);
        fDBReference.child(hotelKey).setValue(hotel);
        System.out.println("added Hotel"+hotels);
        addMenu(hotelKey);
        addTables(hotelKey);

        hotelId=hotelKey;
    }

    private void addMenu(String hotelKey){

        DatabaseReference fDBReference=fDatabase.getReference("menus");

        foodIds=new ArrayList<String>();
        for(int cat=1;cat<=2;cat++) {
            for (int food = 1; food <= cat; food++) {
                String foodItemKey=fDBReference.child(hotelKey).push().getKey();
                FoodItem foodItem = new FoodItem("foodName("+cat+","+food+")",
                                            "shortDesc",
                                            "desc", cat*food,
                                            "category("+cat+")",
                                            "imageUrl");
                fDBReference.child(hotelKey).child(foodItemKey).setValue(foodItem);
                System.out.println("food:"+foodItemKey);

                foodIds.add(foodItemKey);
            }
        }
    }

    private void addTables(String hotelKey){
        DatabaseReference fDBReference=fDatabase.getReference("tables");
        for(int i=1;i<=3;i++){
            String tableKey=fDBReference.child(hotelKey).push().getKey();
            FoodTable table=new FoodTable("table"+i,i);
            fDBReference.child(hotelKey).child(tableKey).setValue(table);
            System.out.println("TableKey:"+tableKey);

            tableId = tableKey;
        }

    }

    public void addOrder(){
        // hotelOrderChangeListener();
        addHotelOrder();
        addUserOrder();
        userOrderChangeListener();

    }

    public void addHotelOrder(){
        //addspinner
        DatabaseReference fDBReference=fDatabase.getReference("hotelOrders").child(hotelId);
        String orderKey = fDBReference.push().getKey();
        Map<String,Integer> orderedItems = new HashMap<String,Integer>();
        for(String order:foodIds) {
            orderedItems.put(order,1);
        }
        HotelOrder hotelOrder=new HotelOrder(userId,hotelId,tableId,orderKey,orderedItems);

        fDBReference.child(orderKey).setValue(hotelOrder);
        System.out.println("Add Hotel Order");

        orderId=orderKey;
    }
    public void addUserOrder(){
        DatabaseReference fDBReference=fDatabase.getReference("userOrders").child(userId);
        UserOrder userOrder = new UserOrder(hotelId, tableId,userId,orderId);
        fDBReference.child(orderId).setValue(userOrder);
        System.out.println("Add User Order");
    }

    public void addComment(){
        DatabaseReference fDBReference=fDatabase.getReference("foodComments").child(foodIds.get(0));
        String commentKey = fDBReference.push().getKey();
        Comment comment = new Comment("userName", "comment", userId);
        fDBReference.child(commentKey).setValue(comment);

        addRating(userId,hotelId,foodIds.get(0),3);

        System.out.println("Add Comment");
    }

    public void addRating(final String user, String hotel, String food, final int rating){
        DatabaseReference foodRef = fDatabase.getReference("menus").child(hotel).child(food);
        foodRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                FoodItem f = mutableData.getValue(FoodItem.class);
                if (f == null) {
                    return Transaction.success(mutableData);
                }
                if (f.stars.containsKey(user)) {
                    //nothing to do for now
                    if(rating == 0) {
                        f.starCount = f.starCount - 1;
                        f.stars.remove(user);
                    }else{
                        f.stars.put(user,rating);
                    }
                } else {
                    // Star the post and add self to stars
                    f.starCount = f.starCount + 1;
                    f.stars.put(user,rating);
                }
                int starsSum=0;
                for(int i:f.stars.values())
                    starsSum+=i;
                if(f.starCount>0)
                    f.avgStars = (starsSum/(f.starCount));
                else
                    f.avgStars=0;
                // Set value and report transaction success
                mutableData.setValue(f);

                System.out.println("Food Star Transaction Done");
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                System.out.println("FoodStarTransaction:onComplete:" + databaseError);
            }
        });
    }

    public void hotelOrderChangeListener(){
        DatabaseReference fDBReference=fDatabase.getReference("hotelOrders").child(hotelId);
        fDBReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HotelOrder hotelOrder = dataSnapshot.getValue(HotelOrder.class);
                Log.d("ADDED:","Hotel order by:"+hotelOrder.user+"; orderID:"+dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HotelOrder hotelOrder = dataSnapshot.getValue(HotelOrder.class);
                Log.d("CHANGED:","Hotel order by:"+hotelOrder.user+"; orderID:"+dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVED:","Hotel Order Removed from Hotel");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("MOVED:","Hotel Order Moved from Hotel");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CANCEL:","Hotel Order Canceled from Hotel");
            }
        });
    }

    public void userOrderChangeListener(){
        DatabaseReference fDBReference=fDatabase.getReference("userOrders").child(userId);
        fDBReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserOrder  userOrder= dataSnapshot.getValue(UserOrder.class);
                Log.d("ADDED:","User order by:"+userOrder.hotel+"; orderID:"+dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserOrder  userOrder= dataSnapshot.getValue(UserOrder.class);
                Log.d("CHANGED:","User order by:"+userOrder.hotel+"; orderID:"+dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVED:","User Order Removed from Hotel");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("MOVED:","User Order Moved from Hotel");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CANCEL:","User Order Canceled from Hotel");
            }
        });
    }

    public void addImageToCategory(String hotel){
        try {
            String imageUrl = "https://www.wagamama.com/-/media/WagamamaMainsite/hero-pod-images/salads.jpg";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference(hotel);
           // InputStream stream =   getResources().openRawResource(R.drawable.salads);

            Drawable drawable=getApplicationContext().getResources().getDrawable(R.drawable.salads);
            BitmapDrawable bitDw = ((BitmapDrawable) drawable);
            Bitmap bitmap = bitDw.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            //new FileInputStream(new File("path/to/images/rivers.jpg"));
            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            UploadTask uploadTask = storageRef.child("category/salads.jpg").putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("File Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    System.out.println("File upload OnSUCCESS:" + downloadUrl);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("File Upload is paused");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            //httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }
}

