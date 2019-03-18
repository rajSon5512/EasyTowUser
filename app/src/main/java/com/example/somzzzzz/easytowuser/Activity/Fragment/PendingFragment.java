 package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Model.CheckSum;
import com.example.somzzzzz.easytowuser.Activity.Model.NormalUser;
import com.example.somzzzzz.easytowuser.Activity.Model.Tickets;
import com.example.somzzzzz.easytowuser.Activity.Model.Transactions;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;
import static com.paytm.pgsdk.PaytmConstants.CHECKSUM;


 public class PendingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore mFirebaseFirestore;
    private CollectionReference mCollectionReference;
    private FirebaseUser mFirebaseUser;
    private List<Tickets> mPendingEntries=new ArrayList<>();
    private CheckSum mCheckSum;
     private static final String USERID="USERID";

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

        String userId=FirebaseAuth.getInstance().getUid();

        Log.d(TAG, "Uid: "+userId);

        final String[] vehicleI = new String[1];

        FirebaseFirestore.getInstance().collection(NormalUser.COLLECTION_NAME)
                .document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            DocumentSnapshot vehiclenumber=task.getResult();

                            Log.d(TAG, "onComplete: "+vehiclenumber.getString(NormalUser.VEHICLE_NUMBER));

                            vehicleI[0] =vehiclenumber.getString(NormalUser.VEHICLE_NUMBER);

                            addVehicleEntries(vehicleI[0]);


                        }else{
                            Log.d(TAG, "onComplete: "+task.getException());

                        }

                    }
                });


    }

     private void addVehicleEntries(String s) {

         Log.d(TAG, "addVehicleEntries: "+s);

         mCollectionReference.whereEqualTo(Tickets.VEHICLE_ID,s)
                 .whereEqualTo(Tickets.CURRENT_STATUS,Tickets.DEFAULT_TICKET_STATUS)
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         List<DocumentSnapshot> documentSnapshots=new ArrayList<DocumentSnapshot>();

                         // documentSnapshots=queryDocumentSnapshots.getDocuments();
                         documentSnapshots=queryDocumentSnapshots.getDocuments();

                         Log.d("SIZE", "onSuccess: "+documentSnapshots.size());

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


     public class PendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        public void bind(final Tickets tickets){

            index.setText(String.valueOf(getAdapterPosition()+1));
            vehiclenumber.setText(tickets.getVehicleId());
            fine.setText("Fine: "+tickets.getFine());
            String date1=tickets.getDate();
            pickupdate.setText(date1);

            /*paybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkFunction(tickets.getVehicleId());
                }
            });*/
        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.pay_button:

                    Toast.makeText(getContext(),"Pay",Toast.LENGTH_SHORT).show();

                    int position=getAdapterPosition();

                    Log.d(TAG, "onClick: "+position);

                    Tickets tickets=mPendingEntries.get(position);

                    FirebaseFirestore.getInstance().collection("transactions")
                            .whereEqualTo("vehiclenumber", tickets.getVehicleId())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if(task.isSuccessful()){

                                        List<DocumentSnapshot> documents = task.getResult().getDocuments();

                                        for(int i=0;i<documents.size();i++){
                                            String documentId = documents.get(i).getId();
                                            String fine=documents.get(i).getString("taxamount");
                                            String date=documents.get(i).getString("date");
                                            String status=documents.get(i).getString("status");
                                            createCheckSumGeneration(documentId,fine,date,status);
                                        }

                                    }

                                }
                            });
                    break;

            }

        }

    }

    /* private void checkFunction(String vehicleId) {

         Toast.makeText(getContext(),""+vehicleId,Toast.LENGTH_SHORT).show();

         String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

         Log.d(TAG, "checkFunction: "+uid);



         createCheckSumGeneration();

     }
*/
     private void createCheckSumGeneration(String orderid,String date,String fine,String status) {

        Log.d(CHECKSUM, "createCheckSumGeneration: "+orderid+"date :"+date+"fine :"+fine+"Status: "+status);

        Transactions transactions=new Transactions();

        transactions.setORDERID(orderid);
       /* transactions.setDate(date);
       */ transactions.setFINE(fine);
       transactions.setStatus(status);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api=retrofit.create(Api.class);

        api.getCheckSum(transactions).enqueue(new Callback<CheckSum>() {
            @Override
            public void onResponse(Call<CheckSum> call, Response<CheckSum> response) {

                     mCheckSum=response.body();
                    Log.d(CHECKSUM, "onResponse: "+mCheckSum.getCHECKSUMHASH());
            }

            @Override
            public void onFailure(Call<CheckSum> call, Throwable t) {

                Log.d(CHECKSUM, "onFailure: "+t.getMessage());
            }
        });

        api.getResponse(mCheckSum).enqueue(new Callback<com.example.somzzzzz.easytowuser.Activity.Model.Response>() {
            @Override
            public void onResponse(Call<com.example.somzzzzz.easytowuser.Activity.Model.Response> call, Response<com.example.somzzzzz.easytowuser.Activity.Model.Response> response) {

                if(call.isExecuted()){

                    int rescode=response.code();

                    if(rescode==200){

                        Log.d(CHECKSUM, "SUCCESS");

                        PaytmPGService service=PaytmPGService.getStagingService();

                        PaytmOrder order=paytmOrder(mCheckSum);

                        serviceInitialization(service,order);

                    }
                }

            }

            @Override
            public void onFailure(Call<com.example.somzzzzz.easytowuser.Activity.Model.Response> call, Throwable t) {

                Log.d(CHECKSUM, "onFailure: "+t.getMessage());

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

    private PaytmOrder paytmOrder(CheckSum checkSum) {

        HashMap<String,String> paramMap=new HashMap<String,String>();
        paramMap.put( "MID" , checkSum.getMID());

        Log.d(CHECKSUM, "paytmOrder: "+checkSum.getMID());

        // Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , checkSum.getORDER_ID());
        paramMap.put( "CUST_ID" , checkSum.getCUST_ID());
        paramMap.put( "MOBILE_NO" ,checkSum.getMOBILE_NO());
        paramMap.put( "EMAIL" , checkSum.getEMAIL());
        paramMap.put( "CHANNEL_ID" , checkSum.getCHANNEL_ID());
        paramMap.put( "TXN_AMOUNT" , checkSum.getTXN_AMOUNT());
        paramMap.put( "WEBSITE" , checkSum.getWEBSITE());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , checkSum.getINDUSTRY_TYPE_ID());
        // This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", checkSum.getCALLBACK_URL());
        paramMap.put( "CHECKSUMHASH" , checkSum.getCHECKSUMHASH());
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
