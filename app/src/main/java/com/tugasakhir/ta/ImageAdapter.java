package com.tugasakhir.ta;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;
    private boolean mProcesslike = false;

    private DatabaseReference mDataLike;
    private FirebaseAuth mAuth;


    public LinearLayout linear;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;

        mDataLike = FirebaseDatabase.getInstance().getReference().child("like");
        mDataLike.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        final Upload uploadCurrent = mUploads.get(position);
        final String selectedKey = uploadCurrent.getKey();
        holder.textViewDesc.setText(uploadCurrent.getDesc());
        holder.textViewNama.setText(uploadCurrent.getNama());
        holder.setLikeBtn(selectedKey);

        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .into(holder.imageView);
        Picasso.with(mContext)
                .load(uploadCurrent.getProfil())
                .placeholder(R.drawable.account)
                .into(holder.profil);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProcesslike = true;

                    mDataLike.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(mProcesslike){

                                if(dataSnapshot.child(selectedKey).hasChild(mAuth.getCurrentUser().getUid())){
                                    mDataLike.child(selectedKey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                    mProcesslike = false;

                                }else {
                                    mDataLike.child(selectedKey).child(mAuth.getCurrentUser().getUid()).setValue("random");
                                    mProcesslike = false;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }
        });

        holder.komen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postId", selectedKey);
                intent.putExtra("publisherId", selectedKey);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {
        public TextView textViewDesc;
        public TextView textViewNama;
        public ImageView profil;
        public ImageView imageView;
        public ImageView menu;
        public ImageButton like;
        public ImageButton komen;

        private DatabaseReference mDataLike;
        private FirebaseAuth mAuth;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewDesc = itemView.findViewById(R.id.text_view_desc);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewNama = itemView.findViewById(R.id.nama);
            profil = itemView.findViewById(R.id.gambar);
            menu = itemView.findViewById(R.id.menu);
            like = itemView.findViewById(R.id.like_btn);
            komen = itemView.findViewById(R.id.comment_btn);


            mDataLike = FirebaseDatabase.getInstance().getReference().child("like");
            mDataLike.keepSynced(true);

            mAuth = FirebaseAuth.getInstance();

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        public void setLikeBtn(final String selectedKey){
            mDataLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(selectedKey).hasChild(mAuth.getCurrentUser().getUid())){
                        like.setImageResource(R.drawable.like_blue);
                    }else{
                        like.setImageResource(R.drawable.like_black);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}