package com.emrekentli.adoptme.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.UserModel;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.UserDto;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    CircleImageView profile_image;
    Button logOutButton;
    TextView username, userid, usermail;
    TokenManager tokenManager;
    String photoUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        tokenManager = new TokenManager(getContext());
        getProfileSpecs();

        profile_image = view.findViewById(R.id.profile_image);
        logOutButton = view.findViewById(R.id.logOutButton);
        usermail = view.findViewById(R.id.usermail);

        username = view.findViewById(R.id.username);


        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        navigation.setClickable(true);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logOut();

            }
        });

        return view;

    }


    public void getAccount() {

    }

    public void getProfileSpecs() {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<UserDto>> call = restInterface[0].getUserSpecs("Bearer "+ tokenManager.getToken());
        call.enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call, Response<ApiResponse<UserDto>> response) {
              if(response.body() != null) {
                  UserDto userDto = response.body().getData();
                  username.setText(userDto.getFullName());
                  usermail.setText(userDto.getEmail());
                  photoUrl = userDto.getImage();
                  Picasso.get().load(photoUrl).into(profile_image);
              }

            }

            @Override
            public void onFailure(Call<ApiResponse<UserDto>> call, Throwable t) {


                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logOut() {
        tokenManager.deleteToken();
        replaceFragments(WelcomeFragment.class);
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
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment)
                .commit();
    }
}
