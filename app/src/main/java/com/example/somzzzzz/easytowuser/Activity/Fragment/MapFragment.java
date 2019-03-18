package com.example.somzzzzz.easytowuser.Activity.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.somzzzzz.easytowuser.Activity.Model.MarkerData;
import com.example.somzzzzz.easytowuser.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 4;
    private static final String TAG = "Defualt";
    private static final int M_MAX_ENTRIES = 1;
    private GoogleMap mMap;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation = new LatLng(0, 0);
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses, mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private ArrayList<MarkerData> mMarkerDataArrayList = new ArrayList<MarkerData>();


    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Parcelable mCurrentLocation, mCameraPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragmentmap,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* SupportMapFragment mapFragment = (SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
*/
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        //updateLocationUI();
        getLocationPermission();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

         mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        mMarkerDataArrayList.add(new MarkerData(21.199437, 72.824668, "SMC Multilevel Parking"));
        mMarkerDataArrayList.add(new MarkerData(21.198877, 72.832521, "SMC Parking slot"));
        mMarkerDataArrayList.add(new MarkerData(21.198556, 72.833122, "SMC Miltilevel parking zampabazar"));
        mMarkerDataArrayList.add(new MarkerData(21.198223, 72.833156, "Multi National Parking"));
        mMarkerDataArrayList.add(new MarkerData(21.182371, 72.822649, "Pay And Park By SMC., Under, Ring Rd Flyover, Sahara Darwaja, New Textile Market, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182412, 72.822676, "SMC Pay & Park, Zampa Bazaar, Begampura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182385, 72.822716, "Kavi Shree Jayant Pathak Garden SMC, Sagrampura Talavdi Road, Garden Colony, Rudrapura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182379, 72.822685, "SMC Pay & Parking, Adajan Flyover Bridge, Opp. Mahan Terrace, Guru Ram Pavan Bhumi, Adajan Gam, Adajan, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182385, 72.822691, "S.M.C Multilevel Parking-Chauta Bazzar, Lal Gate, Shahpore, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182385, 72.822691, "SMC Parking Lot, 4/771, Station Rd, Zampa Bazaar, Begampura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182382, 72.822688, "SMC Multilevel Parking, B/S Gandhi Smruti Bhavan,, Timiliyawad Rd, Nanpura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182387, 72.822696, "S.M.C Pay & Park, Falsawadi Main Rd, Falsawadi, Begampura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182361, 72.822703, "S.M.C Multi Level Parking Kadarshani Nal Road, Kadarshani Nal Rd, Amina Wadi, Kailash Nagar, Nanpura, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182361, 72.822703, "S.M.C Multi Level Parking, Chowk Bazar, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182376, 72.822717, "SMC Pay & Parking, B-9, Canal Rd, Panaas, Athwa, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182378, 72.822703, "SMC Pay & Parking, B-9, Canal Rd, Panaas, Athwa, Surat"));
        mMarkerDataArrayList.add(new MarkerData(21.182378, 72.822703, "SMC Parking Lot, 4/771, Station Rd, Zampa Bazaar, Begampura, Surat"));


        setAllMarkers();

        updateLocationUI();

        getDevicelocation();
    }

    private void setAllMarkers() {

        for (MarkerData marker : mMarkerDataArrayList) {

            LatLng latLng = new LatLng(marker.getLag(), marker.getLog());

            mMap.addMarker(new MarkerOptions().position(latLng).title(marker.getNameofplace()).zIndex(1.0f));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

    private void getDevicelocation() {

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(),new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void getLocationPermission() {
         /** Request location permission, so that we can get the location of the
         * device. The result of the Manifest.permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mylocation) {
            showCurrentPlace();
            getDevicelocation();
            showCurrentPlace();
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
*/

    private void showCurrentPlace() {
        if (mMap == null) {

            Toast.makeText(getActivity(), "mMap null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;

                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }
                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private void openPlacesDialog() {

        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.

               mLikelyPlaceLatLngs[0]=new LatLng(23,25);
                mLikelyPlaceLatLngs[1]=new LatLng(21,24);

                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();

    }


     public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

}

