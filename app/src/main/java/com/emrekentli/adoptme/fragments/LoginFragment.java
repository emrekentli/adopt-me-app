package com.emrekentli.adoptme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.request.LoginRequest;
import com.emrekentli.adoptme.model.response.AuthenticationResponse;
import com.emrekentli.adoptme.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView dontHaveAccountTv;
    private TokenManager tokenManager;

    public LoginFragment() {
        // Gerekli boş kurucu metod
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        emailEditText = view.findViewById(R.id.emailEt);
        passwordEditText = view.findViewById(R.id.passET);
        loginButton = view.findViewById(R.id.registrationButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        dontHaveAccountTv = view.findViewById(R.id.dontHaveAccountTV);
        dontHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(RegisterFragment.class);
            }
        });

        return view;
    }

    private void performRegistration() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        checkInputs(email, password);
        login(email, password);
    }

    private void login(String email, String password) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<AuthenticationResponse>> response = restInterface[0].loginUser(new LoginRequest(email, password));
        response.enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {

            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, retrofit2.Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.body() != null && response.body().getMeta().getCode().equals("200") && response.body().getData() != null) {
                    Toast.makeText(getContext(), "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                    String token = response.body().getData().getToken();
                    tokenManager = new TokenManager(getContext());
                    tokenManager.saveToken(token);
                    replaceFragments(AdsFragment.class);
                } else {
                    Toast.makeText(getContext(), "Giriş başarısız!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }
    private void checkInputs(String email, String password) {
      if (email.isEmpty()) {
            emailEditText.setError("Email boş bırakılamaz!");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Şifre boş bırakılamaz!");
            passwordEditText.requestFocus();
        }
    }
}