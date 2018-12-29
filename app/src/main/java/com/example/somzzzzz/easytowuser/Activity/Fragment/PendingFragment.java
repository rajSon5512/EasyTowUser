package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.somzzzzz.easytowuser.Activity.Model.Tickets;
import com.example.somzzzzz.easytowuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class PendingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private CollectionReference mCollectionReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragmentpending,container,false);

       mRecyclerView=view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new PendingAdapter());

        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mCollectionReference=mFirebaseFirestore.collection(Tickets.COLLECTION_NAME);

        return view;
    }


    public class PendingAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder{

        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(){


        }

    }


}
