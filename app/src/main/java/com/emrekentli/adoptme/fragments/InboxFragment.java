package com.emrekentli.adoptme.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.controller.ProfileAdsAdaptor;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends Fragment {
    private List<PostModel> myList;
    ListView myAdsListView;
    private ProfileAdsAdaptor adapter;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim

        View view = inflater.inflate(R.layout.inbox_fragment, container, false);
        tokenManager = new TokenManager(getContext());

        myAdsListView = view.findViewById(R.id.myAdsListView);
        setClicks();
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        getOwnAds();
    }


    public void setClicks() {

        myAdsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                PostModel repo = myList.get(position);

                replaceFragmentsAds(AdDetailFragment.class, repo.getId());

                BottomNavigationView navigation = getActivity().findViewById(R.id.bottom_navigation);
                navigation.setClickable(true);


            }
        });


    }

    public void getOwnAds() {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<DataResponse<PostModel>>> call = restInterface[0].getOwnAds("Bearer " +tokenManager.getToken());
        call.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<PostModel>>> call, Response<ApiResponse<DataResponse<PostModel>>> response) {

                if (response.body() != null) {
                    myList = response.body().getData().getItems();

                    if (getActivity() != null) {
                        adapter = new ProfileAdsAdaptor(getContext(), R.layout.profileads_row, myList, getActivity());
                        Log.i("Bilgi", response.toString());
                        myAdsListView.setAdapter(adapter);
                    }


                    if (myList.toString().equals("[]")) {


                        replaceFragments(AddYourFirstAdFragment.class);


                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Toast.makeText(getContext(), "Hiç ilan paylaşmadınız.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }

    public void replaceFragmentsAds(Class fragmentClass, String adid) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("adid", adid);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }


}
