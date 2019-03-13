 package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Activity.MainActivity;
import com.example.somzzzzz.easytowuser.Activity.Model.CheckSum;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGActivity;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.Checksum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.inflate;


public class PendingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore mFirebaseFirestore;
    private CollectionReference mCollectionReference;
    private FirebaseUser mFirebaseUser;
    private List<Tickets> mPendingEntries=new ArrayList<>();
    private CheckSum mCheckSum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);

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

        mCollectionReference.whereEqualTo(Tickets.VEHICLE_ID,"GJ5-ER-1550")
                .whereEqualTo(Tickets.CURRENT_STATUS,Tickets.DEFAULT_TICKET_STATUS)
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


    public class PendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView index,vehiclenumber,pickupdate,time,fine;
        private Button paybutton;

        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            index=itemView.findViewById(R.id.entry_no);
            vehiclenumber=itemView.findViewById(R.id.vehicle_number);
            pickupdate=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            fine=itemView.findViewById(R.id.fine);
            paybutton=itemView.findViewById(R.id.pay_button);
            paybutton.setOnClickListener(this);
        }

        public void bind(Tickets tickets){

            index.setText(String.valueOf(getAdapterPosition()+1));
            vehiclenumber.setText(tickets.getVehicleId());
            fine.setText("Fine: "+tickets.getFine());
            String date1=tickets.getDate();
            pickupdate.setText(date1);

        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.pay_button:
                    Toast.makeText(getContext(),"Pay",Toast.LENGTH_SHORT).show();

                    PaytmPGService service=PaytmPGService.getStagingService();

                    PaytmOrder order=paytmOrder();

                    serviceInitialization(service,order);

                    checkSumGeneration();

                    break;

            }

        }

    }

    private void checkSumGeneration() {

        final Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api=retrofit.create(Api.class);

        api.getCheckSum().enqueue(new Callback<CheckSum>() {
            @Override
            public void onResponse(Call<CheckSum> call, Response<CheckSum> response) {

                    mCheckSum=response.body();
                Log.d("CheckSum", "onResponse: "+mCheckSum.getCHECKSUMHASH());
            }

            @Override
            public void onFailure(Call<CheckSum> call, Throwable t) {

                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void serviceInitialization(PaytmPGService service,PaytmOrder order) {

        service.initialize(order,null);

        service.startPaymentTransaction(getContext(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {

                        Toast.makeText(getContext(),"Transaction Response:"+inResponse.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void networkNotAvailable() {

                        Toast.makeText(getContext(),"Network Connectivity error",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        Toast.makeText(getContext(),"Client Authentication Failed :"+inErrorMessage.toString(),Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        Toast.makeText(getContext(),"SomeUIerror occurred :"+inErrorMessage.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

                        Toast.makeText(getContext(),"Unable to load Webpage :"+inErrorMessage.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {

                        Toast.makeText(getContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

                        Toast.makeText(getContext(), "Transaction cancelled :"+inErrorMessage.toString() , Toast.LENGTH_LONG).show();
                    }
                });




    }

    private PaytmOrder paytmOrder() {

        HashMap<String,String> paramMap=new HashMap<String,String>();
        paramMap.put( "MID" , "dbMIND07876515785068");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , "order1");
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "username@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "100.12");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder paytmOrder=new PaytmOrder(paramMap);

        return paytmOrder;
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
