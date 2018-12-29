package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.somzzzzz.easytowuser.Activity.Model.Tickets;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.inflate;

public class PendingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore mFirebaseFirestore;
    private CollectionReference mCollectionReference;
    private FirebaseUser mFirebaseUser;
    private List<Tickets> mPendingEntries=new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragmentpending,container,false);

        init(view);

       mRecyclerView=view.findViewById(R.id.recycler_view);


       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mAdapter);

        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseFirestore=FirebaseFirestore.getInstance();
        mCollectionReference=mFirebaseFirestore.collection(Tickets.COLLECTION_NAME);

        Log.d(TAG, "onCreateView: "+Tickets.COLLECTION_NAME);

        fetchPendingEntries();


        return view;
    }

    private void init(View view) {

        mAdapter=new PendingAdapter();
     }

    private void fetchPendingEntries() {

        mCollectionReference.whereEqualTo(Tickets.CURRENT_STATUS,Tickets.DEFAULT_TICKET_STATUS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> documentSnapshots=new ArrayList<DocumentSnapshot>();

                        documentSnapshots=queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot document:documentSnapshots) {

                            Tickets tickets=new Tickets(document);

                            mPendingEntries.add(tickets);
                            mAdapter.notifyItemInserted(mPendingEntries.indexOf(tickets));

                        }

                        Log.d(TAG, "onSuccess: "+mPendingEntries.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });


    }


    public class PendingViewHolder extends RecyclerView.ViewHolder{

        private TextView index,vehiclenumber,pickupdate,time,fine;

        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            index=itemView.findViewById(R.id.entry_no);
            vehiclenumber=itemView.findViewById(R.id.vehicle_number);
            pickupdate=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            fine=itemView.findViewById(R.id.fine);
        }

        public void bind(Tickets tickets){

            index.setText(String.valueOf(getAdapterPosition()+1));
            vehiclenumber.setText(tickets.getVehicleId());
            fine.setText("Fine: "+tickets.getFine());
            String date1=tickets.getDate();
            pickupdate.setText(date1);
        }

    }

    public class PendingAdapter extends RecyclerView.Adapter<PendingViewHolder>{

        private LayoutInflater mLayoutInflater;

        public PendingAdapter(){
            mLayoutInflater=LayoutInflater.from(getActivity());
        }

        @NonNull
        @Override
        public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=mLayoutInflater.inflate(R.layout.pending_item,viewGroup,false);
            return new PendingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PendingViewHolder viewHolder, int i) {
            viewHolder.bind(mPendingEntries.get(i));
        }

        @Override
        public int getItemCount() {
            return mPendingEntries.size();
        }
    }


}
