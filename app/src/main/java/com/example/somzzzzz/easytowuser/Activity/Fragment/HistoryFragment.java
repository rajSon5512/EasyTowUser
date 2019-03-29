package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.somzzzzz.easytowuser.Activity.Model.NormalUser;
import com.example.somzzzzz.easytowuser.Activity.Model.Tickets;
import com.example.somzzzzz.easytowuser.Activity.Model.Transactions;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class HistoryFragment extends Fragment {


    private List<Tickets> mTicket=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragmenthistory,container,false);

        init(view);

        mRecyclerView=view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "HistotyFragment ");


        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefresher);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("ONREFRESH", "onRefresh: It's run ");
                fetchHistoryEntries();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    private void init(View view) {

        mAdapter=new HistoryAdapter();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchHistoryEntries();

    }

    private void fetchHistoryEntries() {

        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection(NormalUser.COLLECTION_NAME)
                .document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                        DocumentSnapshot documentSnapshot=task.getResult();

                        String vehicleNumber=documentSnapshot.getString(NormalUser.VEHICLE_NUMBER);

                        FetchPaidEntries(vehicleNumber);

                }

            }
        });


    }

    private void FetchPaidEntries(String vehicleNumber) {

        clear();
        final List<Tickets> listTickets=new ArrayList<Tickets>();

        FirebaseFirestore.getInstance().collection(Tickets.COLLECTION_NAME)
                .whereEqualTo(Tickets.VEHICLE_ID,vehicleNumber)
                .whereEqualTo(Tickets.CURRENT_STATUS,"paid")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            QuerySnapshot querySnapshot=task.getResult();

                            List<DocumentSnapshot> documentSnapshot=querySnapshot.getDocuments();

                            Log.d(TAG, "onComplete: "+documentSnapshot.size());


                            for(DocumentSnapshot i:documentSnapshot){

                                Tickets ticket=new Tickets(i);

                                Log.d(TAG, "onComplete: "+ticket.getDocumentId());
                                Log.d(TAG, "onComplete: "+ticket.getDate());

                                listTickets.add(ticket);

                            }

                            addAll(listTickets);
                            mSwipeRefreshLayout.setRefreshing(false);

                        }

                    }
                });
    }



    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView srno,vehiclenumber,date,fine,status;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            srno=itemView.findViewById(R.id.sr_no);
            vehiclenumber=itemView.findViewById(R.id.vehicle_number_history);
            date=itemView.findViewById(R.id.date_history);
            status=itemView.findViewById(R.id.status_history);
            fine=itemView.findViewById(R.id.fine_history);
        }

        public void bind(Tickets ticket){

            Log.d(TAG, "bind: "+ticket.getDocumentId());
            srno.setText(String.valueOf(getAdapterPosition()+1));
            vehiclenumber.setText(ticket.getVehicleId());
            date.setText(ticket.getDate());
            status.setText(ticket.getCurrentStatus());
            fine.setText(ticket.getFine());
        }

    }


    private class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

        private LayoutInflater mLayoutInflater;

        public HistoryAdapter(){

            mLayoutInflater=LayoutInflater.from(getActivity());
        }


        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=mLayoutInflater.inflate(R.layout.history_items,viewGroup,false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {

            historyViewHolder.bind(mTicket.get(i));
        }

        @Override
        public int getItemCount() {
            return mTicket.size();
        }
    }


    public void clear() {
        mTicket.clear();
        mAdapter.notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tickets>  ticket) {
        mTicket.addAll(ticket);
        mAdapter.notifyDataSetChanged();
    }


}
