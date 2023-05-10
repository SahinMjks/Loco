package com.example.loco_v1;



import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    //Post Details Activity Part variable
    String hisuid, ptime, myuid, myname, myemail, mydp, plike, hisdp, hisname;

    //U_image is to show the actual posted image
    ImageView picture, image;
    TextView name, time, title, description, like, tcomment;
    Button likebtn, share;
    ImageButton more;
    LinearLayout profile;
    EditText comment;
    ImageButton sendb;
    RecyclerView post_details_recyclerView;
    List<ModelComment> commentList;
    AdapterComment adapterComment;
    ImageView imagep;
    boolean mlike = false;
    ActionBar actionBar;
    ProgressDialog progressDialog;

    //post Liked by

    Context context;
    RecyclerView recyclerView;
    String postId;
    List<ModelUsers> list;
    AdapterUsers adapterUsers;
    FirebaseAuth firebaseAuth;

    String pid;
    private DatabaseReference liekeref, postref;
    boolean mprocesslike = false;



    public AdapterPosts(Context context,List<ModelPost> modelPosts) {
        this.context = context;
        this.modelPosts = modelPosts;
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    List<ModelPost> modelPosts;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        String uid = modelPosts.get(position).getUid();

        //Another Problem is name is showing wrong
        String nameh = modelPosts.get(position).getUname();
        final String titlee = modelPosts.get(position).getTitle();
        final String descri = modelPosts.get(position).getDescription();
        final String ptime = modelPosts.get(position).getPtime();
        //This is where the Problem is, user's dp is not showing
        String dp = modelPosts.get(position).getUdp();
        String plike = modelPosts.get(position).getPlike();

        //Post image
        final String u_image = modelPosts.get(position).getUimage();

        Toast.makeText(context,"Inside OnBind: "+u_image,Toast.LENGTH_SHORT).show();

        String comm = modelPosts.get(position).getPcomments();
        pid = ptime;
        postId=ptime;

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(ptime));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.name.setText(nameh);
        holder.title.setText(titlee);
        holder.description.setText(descri);
        holder.time.setText(timedate);
        holder.like.setText(plike + " Likes");
        holder.comments.setText(comm + " Comments");
        setLikes(holder, ptime);
        try {
            Glide.with(context).load(dp).into(holder.picture);
        } catch (Exception e) {

        }
        holder.image.setVisibility(View.VISIBLE);
        try {
            Glide.with(context).load(u_image).into(holder.image);
        } catch (Exception e) {

        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_like_Dialog();
            }
        });
        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int plike = Integer.parseInt(modelPosts.get(position).getPlike());
                mprocesslike = true;
                final String postid = modelPosts.get(position).getPtime();
                liekeref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (mprocesslike) {
                            if (dataSnapshot.child(postid).hasChild(myuid)) {
                                postref.child(postid).child("plike").setValue("" + (plike - 1));
                                liekeref.child(postid).child(myuid).removeValue();

                                mprocesslike = false;
                            } else {
                                postref.child(postid).child("plike").setValue("" + (plike + 1));
                                liekeref.child(postid).child(myuid).setValue("Liked");
                                mprocesslike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.more,uid,myuid, ptime,u_image);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    show_details_dialog(u_image);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    show_details_dialog(u_image);
            }
        });
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,OthersProfile.class);
                intent.putExtra("uid",uid);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private void show_details_dialog(String u_image){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_post_details);



        post_details_recyclerView = dialog.findViewById(R.id.recyclecomment);
        //picture is for dp
        picture = dialog.findViewById(R.id.pictureco);
        //this is the image i've posted
        image = dialog.findViewById(R.id.pimagetvco);
        name = dialog.findViewById(R.id.unameco);
        time = dialog.findViewById(R.id.utimeco);
        more = dialog.findViewById(R.id.morebtn);
        title = dialog.findViewById(R.id.ptitleco);

        myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        description = dialog.findViewById(R.id.descriptco);
        tcomment = dialog.findViewById(R.id.pcommenttv);
        like = dialog.findViewById(R.id.plikebco);
        likebtn = dialog.findViewById(R.id.like);
        comment = dialog.findViewById(R.id.typecommet);
        sendb = dialog.findViewById(R.id.sendcomment);
        imagep = dialog.findViewById(R.id.commentimge);
        share = dialog.findViewById(R.id.share);

        //Need to check this
        profile = dialog.findViewById(R.id.profilelayout);
        progressDialog = new ProgressDialog(context);
        loadPostInfo(u_image);

        loadUserInfo();
        setLikes();
        //actionBar.setSubtitle("SignedInAs:" + myemail);
        loadComments();
        sendb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likepost();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_like_Dialog();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getScreenHeight(dialog) * 0.9));
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setGravity(Gravity.BOTTOM);
        }
    }
    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        post_details_recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelComment modelComment = dataSnapshot1.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComment = new AdapterComment(context, commentList, myuid, postId);
                    post_details_recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setLikes() {
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postId).hasChild(myuid)) {
                    likebtn.setText("Liked");
                } else {
                    likebtn.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void likepost() {

        mlike = true;
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (mlike) {
                    if (dataSnapshot.child(postId).hasChild(myuid)) {
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) - 1));
                        liekeref.child(postId).child(myuid).removeValue();
                        mlike = false;

                    } else {
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) + 1));
                        liekeref.child(postId).child(myuid).setValue("Liked");
                        mlike = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void postComment() {
        progressDialog.setMessage("Adding Comment");

        final String commentss = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentss)) {
            Toast.makeText(context, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference datarf = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timestamp);
        hashMap.put("comment", commentss);
        hashMap.put("ptime", timestamp);
        hashMap.put("uid", myuid);
        hashMap.put("uemail", myemail);
        hashMap.put("udp", mydp);
        hashMap.put("uname", myname);
        datarf.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(context, "Added", Toast.LENGTH_LONG).show();
                comment.setText("");
                updatecommetcount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean count = false;

    private void updatecommetcount() {
        count = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (count) {
                    String comments = (String) dataSnapshot.child("pcomments").getValue();
                    int newcomment = Integer.parseInt(comments) + 1;
                    reference.child("pcomments").setValue("" + newcomment);
                    count = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uid").equalTo(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    myname = dataSnapshot1.child("name").getValue().toString();
                    mydp = dataSnapshot1.child("image").getValue().toString();
                    try {
                        Glide.with(context).load(mydp).into(imagep);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo(String u_image) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = databaseReference.orderByChild("ptime").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ModelPost item=dataSnapshot1.getValue(ModelPost.class);
                    String ptitle = dataSnapshot1.child("title").getValue().toString();
                    String descriptions = dataSnapshot1.child("description").getValue().toString();
                    //If there is no image we need to check it
                    //String post_image =item.getUimage();
                    hisdp = dataSnapshot1.child("udp").getValue().toString();
                    hisuid = dataSnapshot1.child("uid").getValue().toString();
                    String uemail = dataSnapshot1.child("uemail").getValue().toString();
                    hisname = dataSnapshot1.child("uname").getValue().toString();
                    ptime = dataSnapshot1.child("ptime").getValue().toString();
                    plike = dataSnapshot1.child("plike").getValue().toString();
                    String commentcount = dataSnapshot1.child("pcomments").getValue().toString();
                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(ptime));
                    String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                    name.setText(hisname);
                    title.setText(ptitle);
                    description.setText(descriptions);
                    like.setText(plike + " Likes");
                    time.setText(timedate);
                    tcomment.setText(commentcount + " Comments");

                    Toast.makeText(context,"Image is"+ u_image,Toast.LENGTH_SHORT).show();
                    if(u_image!=null){
                        if (u_image.equals("noImage")) {
                            image.setVisibility(View.GONE);
                        } else {
                            image.setVisibility(View.VISIBLE);
                            try {
                                Glide.with(context).load(u_image).into(image);
                            } catch (Exception e) {

                            }
                        }
                    }

                    try {
                        Glide.with(context).load(hisdp).into(picture);
                    } catch (Exception e) {

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void show_like_Dialog(){

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_post_liked_by);



        firebaseAuth = FirebaseAuth.getInstance();
        //actionBar.setSubtitle(firebaseAuth.getCurrentUser().getEmail());
        recyclerView = dialog.findViewById(R.id.likerecycle);

        list = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes");
        reference.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String hisUid = "" + dataSnapshot1.getRef().getKey();
                    getUsers(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getScreenHeight(dialog) * 0.5));
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setGravity(Gravity.BOTTOM);
        }


    }
    private int getScreenHeight(Dialog dialog) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void getUsers(String hisUid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUsers model = ds.getValue(ModelUsers.class);
                    list.add(model);
                }
                adapterUsers = new AdapterUsers(context, list);
                recyclerView.setAdapter(adapterUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMoreOptions(ImageButton more,String uid,String myuid, final String pid, final String image) {
        if (context == null) {
            // Handle the error here
            Log.e(TAG, "showMoreOptions(): context is null");
            return;
        }

        Toast.makeText(context,"myUID"+myuid+"PID:"+pid,Toast.LENGTH_SHORT).show();
        PopupMenu popupMenu = new PopupMenu(context, more, Gravity.END);
        if (uid != null && uid.equals(myuid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "DELETE");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    deltewithImage(pid, image);
                }

                return false;
            }
        });
        popupMenu.show();

    }




    private void deltewithImage(final String pid, String image) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting");
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("ptime").equalTo(pid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                        }

                        pd.dismiss();
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setLikes(final MyHolder holder, final String pid) {
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(pid).hasChild(myuid)) {
                    holder.likebtn.setBackgroundResource(R.drawable.ic_happy);
                } else {
                    holder.likebtn.setBackgroundResource(R.drawable.ic_sad);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelPosts.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView  image;
        CircleImageView picture;
        TextView name, time, title, description, like, comments;
        ImageButton more;
        ImageButton likebtn, comment;
        LinearLayout profile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picturetv);
            image = itemView.findViewById(R.id.pimagetv);
            name = itemView.findViewById(R.id.unametv);
            time = itemView.findViewById(R.id.utimetv);
            more = itemView.findViewById(R.id.morebtn);
            title = itemView.findViewById(R.id.ptitletv);
            description = itemView.findViewById(R.id.descript);
            like = itemView.findViewById(R.id.plikeb);
            comments = itemView.findViewById(R.id.pcommentco);
            likebtn = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            profile = itemView.findViewById(R.id.profilelayout);
        }
    }
}
