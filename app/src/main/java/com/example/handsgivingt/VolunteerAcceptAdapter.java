package com.example.handsgivingt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//we need to extend the ArrayAdapter class as we are building an adapter
public class VolunteerAcceptAdapter extends ArrayAdapter<JSONObject> {

    //the list values in the List of type hero
    List<JSONObject> heroList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;
    private FirebaseFunctions mFunctions;


    //constructor initializing the values
    public VolunteerAcceptAdapter(Context context, int resource, List<JSONObject> heroList) {
        super(context, resource, heroList);
        this.context = context;
        this.resource = resource;
        this.heroList = heroList;
    }

    //this will return the ListView Item as a View

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mFunctions = FirebaseFunctions.getInstance();
        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        final TextView textViewRequestype = view.findViewById(R.id.rowPrevRequestType);
        final TextView textViewVolunteerName = view.findViewById(R.id.rowPrevVolunteerName);


        //getting the hero of the specified position
        final JSONObject hero = heroList.get(position);

        try {
                textViewRequestype.setText(hero.getString("RequestType"));
                textViewVolunteerName.setText(hero.getString("LocationDesc"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String[] userName = {""};
        String userMail = null;
        try {
            userMail = hero.getString("userEmail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //finally returning the view
        Map<String, Object> data = new HashMap<>();
        data.put("email", userMail);
        final String finalEmailo = userMail;
        mFunctions.getHttpsCallable("getCurrentUserInfo")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        try{
                            Gson g = new Gson();
                            String json = g.toJson(httpsCallableResult.getData());
                            JSONObject jsonObject = new JSONObject(json);
                            userName[0] = jsonObject.getJSONObject(finalEmailo).getString("Name") + " "+ jsonObject.getJSONObject(finalEmailo).getString("Surname");
                        } catch (Exception e){
                            Log.d("Error",e.toString());
                        }
                    }
                });

        String reqType = "";
        String reqDesc = "";
        String locDesc = "";
        Double lati = 0.0;
        Double longi = 0.0;
        final String requestStatus = "Gönüllü Kabul Edildi";
        try {
            reqType = hero.getString("RequestType");
            reqDesc = hero.getString("Description");
            locDesc = hero.getString("LocationDesc");
            lati = hero.getJSONObject("Location").getDouble("_latitude");
            longi = hero.getJSONObject("Location").getDouble("_longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String finalReqType = reqType;
        final String finalReqDesc = reqDesc;
        final Double finalLati = lati;
        final Double finalLongi = longi;
        final String finalLocDesc = locDesc;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new RequestDetailFragment(finalReqType, finalReqDesc, finalLati, finalLongi, finalLocDesc, userName[0], requestStatus);
                loadFragment(fragment);
            }
        });
        return view;
    }
    private boolean loadFragment(Fragment fragment)
    {

        if (fragment != null) {
            ((AppCompatActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}