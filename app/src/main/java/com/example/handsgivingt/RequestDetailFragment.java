package com.example.handsgivingt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestDetailFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String requestType;
    private String description;
    private Double locationLatitude;
    private Double locationLongitude;
    private String locationDescription;
    private TextView reqDesc;
    private TextView reqType;
    private  TextView locDesc;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    LatLng ltLng;

    public RequestDetailFragment(String requestType, String description, Double locationLatitude, Double locationLongitude, String locationDescription) {
        this.requestType = requestType;
        this.description = description;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationDescription = locationDescription;
        this.ltLng = new LatLng(locationLatitude, locationLongitude);

    }


    public static RequestDetailFragment newInstance(String requestType, String description, Double locationLatitude, Double locationLongitude, String locationDescription) {
        RequestDetailFragment fragment = new RequestDetailFragment(requestType, description, locationLatitude, locationLongitude, locationDescription);
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reqType = view.findViewById(R.id.reqTypeTW);
        reqDesc = view.findViewById(R.id.reqExpTW);
        locDesc = view.findViewById(R.id.locExpTW);
        reqType.setText(requestType);
        reqDesc.setText(description);
        locDesc.setText(locationDescription);
        
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions()
                .position(ltLng)
                .title("İşaretli Konum"));
        mMap.setMinZoomPreference(12.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ltLng));
    }

}
