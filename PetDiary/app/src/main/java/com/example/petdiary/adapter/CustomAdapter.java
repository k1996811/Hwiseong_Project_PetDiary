package com.example.petdiary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.petdiary.info.BlockFriendInfo;
import com.example.petdiary.info.BookmarkInfo;
import com.example.petdiary.Comment;
import com.example.petdiary.Data;
import com.example.petdiary.info.FriendInfo;
import com.example.petdiary.info.PostLikeInfo;
import com.example.petdiary.R;
import com.example.petdiary.activity.ContentEditActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Data> arrayList;
    private Context context;
    private Button Comment_btn;
    private Button onPopupButton;

    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    WormDotsIndicator wormDotsIndicator;

    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.
    public CustomAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void clear() {
        arrayList.clear();
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {

        if (arrayList.get(position).getImageUrl1().equals("https://firebasestorage.googleapis.com/v0/b/petdiary-794c6.appspot.com/o/images%2Fempty.png?alt=media&token=eb832feb-bb39-48a0-9f46-81ffea724871")) {
            viewPager = (ViewPager) holder.itemView.findViewById(R.id.main_image);
            viewPageAdapter = new ViewPageAdapter(arrayList.get(position), arrayList.get(position).getImageUrl1(), arrayList.get(position).getImageUrl2(),
                    arrayList.get(position).getImageUrl3(), arrayList.get(position).getImageUrl4(), arrayList.get(position).getImageUrl5(), context);
            viewPager.setAdapter(viewPageAdapter);
            viewPager.setVisibility(View.GONE);
            wormDotsIndicator = (WormDotsIndicator) holder.itemView.findViewById(R.id.worm_dots_indicator);
            wormDotsIndicator.setViewPager(viewPager);
            wormDotsIndicator.setVisibility(View.INVISIBLE);


            View first_border = (View) holder.itemView.findViewById(R.id.first_Square);
            View second_border = (View) holder.itemView.findViewById(R.id.second_Square);
            View hidden_border = (View) holder.itemView.findViewById(R.id.hidden_Square);

            first_border.setVisibility(View.GONE);
            second_border.setVisibility(View.GONE);
            hidden_border.setVisibility(View.VISIBLE);

            //  RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams
            //     (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            // buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            //  buttonLayoutParams.addRule(RelativeLayout.ABOVE, view3.getId());
            // view1.setLayoutParams(buttonLayoutParams);
        }
        else {

            viewPager = (ViewPager) holder.itemView.findViewById(R.id.main_image);
            viewPageAdapter = new ViewPageAdapter(arrayList.get(position), arrayList.get(position).getImageUrl1(), arrayList.get(position).getImageUrl2(),
                    arrayList.get(position).getImageUrl3(), arrayList.get(position).getImageUrl4(), arrayList.get(position).getImageUrl5(), context);
            viewPager.setAdapter(viewPageAdapter);

        }
        holder.content.setText(arrayList.get(position).getContent());
        holder.nickName.setText(arrayList.get(position).getNickName());

        if(arrayList.get(position).getBookmark()){
            holder.bookmark_button.setChecked(true);
        } else {
            holder.bookmark_button.setChecked(false);
        }
        if(arrayList.get(position).getLike()){
            holder.Like_button.setChecked(true);
        } else {
            holder.Like_button.setChecked(false);
        }

        Comment_btn = (Button) holder.itemView.findViewById(R.id.Comment_btn);
        Comment_btn.findViewById(R.id.Comment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(context, Comment.class);

                intent.putExtra("postID", arrayList.get(position).getPostID());
                intent.putExtra("nickName", arrayList.get(position).getNickName());
                intent.putExtra("uid", arrayList.get(position).getUid());
                intent.putExtra("content", arrayList.get(position).getContent());

                context.startActivity(intent);
            }
        });

        onPopupButton = (Button) holder.itemView.findViewById(R.id.onPopupButton);
        onPopupButton.setOnClickListener(new View.OnClickListener() {

            String uids = uid;
            //내 uid
            String uids2 = arrayList.get(position).getUid();

            //게시물 정보 uid
            @Override
            public void onClick(final View view) {
                if (uids.equals(uids2)) {
                    Log.d("@@@@", "onBindViewHolder: 클릭되었냐?" + uid + "////" + arrayList.get(position).getUid());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    CharSequence info[] = new CharSequence[]{"Edit", "Delete", "Share"};
                    builder.setTitle("");
                    builder.setItems(info, new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    // 내정보
                                    Intent intent = new Intent(context, ContentEditActivity.class);

                                    //intent.putExtra("nickName", arrayList.get(position).getNickName());
                                    // intent.putExtra("uid", arrayList.get(position).getUid());
                                    // intent.putExtra("content", arrayList.get(position).getContent());
                                    intent.putExtra("imageUrl1", arrayList.get(position).getImageUrl1());
                                    intent.putExtra("imageUrl2", arrayList.get(position).getImageUrl2());
                                    intent.putExtra("imageUrl3", arrayList.get(position).getImageUrl3());
                                    intent.putExtra("imageUrl4", arrayList.get(position).getImageUrl4());
                                    intent.putExtra("imageUrl5", arrayList.get(position).getImageUrl5());
                                    intent.putExtra("category", arrayList.get(position).getCategory());
                                    intent.putExtra("content", arrayList.get(position).getContent());
                                    intent.putExtra("postID", arrayList.get(position).getPostID());

                                    context.startActivity(intent);
                                    Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    // 로그아웃
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("post").document(arrayList.get(position).getPostID())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("@@@", "DocumentSnapshot successfully deleted!");

                                                    arrayList.remove(position);
                                                    notifyItemRemoved(position);
                                                    //this line below gives you the animation and also updates the
                                                    //list items after the deleted item
                                                    notifyItemRangeChanged(position, getItemCount());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("@@@", "Error deleting document", e);
                                                }
                                            });
                                    Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Intent msg = new Intent(Intent.ACTION_SEND);
                                    msg.addCategory(Intent.CATEGORY_DEFAULT);
                                    msg.putExtra(Intent.EXTRA_SUBJECT, "주제");
                                    msg.putExtra(Intent.EXTRA_TEXT, "내용");
                                    msg.putExtra(Intent.EXTRA_TITLE, "제목");
                                    msg.setType("text/plain");
                                    view.getContext().startActivity(Intent.createChooser(msg, "공유"));

                                    break;
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    CharSequence info[] = new CharSequence[]{"친구삭제", "신고하기", "사용자 차단", "게시물 숨기기"};
                    builder.setTitle("");
                    builder.setItems(info, new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(view.getContext(), "deleteFriend", Toast.LENGTH_SHORT).show();
                                    DatabaseReference friend = firebaseDatabase.getReference("friend").child(user.getUid() + "/" + uid);
                                    FriendInfo friendInfo = new FriendInfo();
                                    friend.setValue(friendInfo);
                                    Toast.makeText(context, "친구를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(view.getContext(), "신고하기", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    BlockFriendInfo blockFriendInfo = new BlockFriendInfo();
                                    blockFriendInfo.setFriendUid(arrayList.get(position).getUid());
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("blockFriends/"+user.getUid()+"/friends").document(uid).set(blockFriendInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                    Toast.makeText(view.getContext(), "사용자 차단", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:

                                    Toast.makeText(view.getContext(), "게시물 숨기기", Toast.LENGTH_SHORT).show();

                                    break;
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                }
            }

        });

        final String[] profileImg = new String[1];
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(arrayList.get(position).getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            profileImg[0] = document.getData().get("profileImg").toString();
                            if (profileImg[0].length() > 0) {
                                ImageView profileImage = (ImageView) holder.itemView.findViewById(R.id.Profile_image);
                                Glide.with(context).load(profileImg[0]).centerCrop().override(500).into(profileImage);
                            }
                        } else {
                            //Log.d("###", "No such document");
                        }
                    }
                } else {
                    //Log.d("###", "get failed with ", task.getException());
                }
            }
        });

        wormDotsIndicator = (WormDotsIndicator) holder.itemView.findViewById(R.id.worm_dots_indicator);
        wormDotsIndicator.setViewPager(viewPager);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView nickName;
        ImageView profileImage;
        CheckBox bookmark_button;
        CheckBox Like_button;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.main_textView);
            this.nickName = itemView.findViewById(R.id.Profile_Name);
            this.profileImage = itemView.findViewById(R.id.Profile_image);
            this.bookmark_button = itemView.findViewById(R.id.bookmark_button);
            this.Like_button = itemView.findViewById(R.id.Like_button);

            itemView.findViewById(R.id.bookmark_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    BookmarkInfo bookmarkInfo = new BookmarkInfo();
                    if(((CheckBox)view).isChecked()){
                        bookmarkInfo.setPostID(arrayList.get(pos).getPostID());
                        db.collection("user-checked/"+user.getUid()+"/bookmark").document(arrayList.get(pos).getPostID()).set(bookmarkInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    } else {
                        db.collection("user-checked/"+user.getUid()+"/bookmark").document(arrayList.get(pos).getPostID())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("CustomAdapter", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("CustomAdapter", "Error deleting document", e);
                                    }
                                });
                    }
                }
            });

            itemView.findViewById(R.id.Like_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    PostLikeInfo postLikeInfo = new PostLikeInfo();
                    if(((CheckBox)view).isChecked()){
                        postLikeInfo.setPostID(arrayList.get(pos).getPostID());
                        db.collection("user-checked/"+user.getUid()+"/like").document(arrayList.get(pos).getPostID()).set(postLikeInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    } else {
                        db.collection("user-checked/"+user.getUid()+"/like").document(arrayList.get(pos).getPostID())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("CustomAdapter", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("CustomAdapter", "Error deleting document", e);
                                    }
                                });
                    }
                }
            });
        }
    }

    
}