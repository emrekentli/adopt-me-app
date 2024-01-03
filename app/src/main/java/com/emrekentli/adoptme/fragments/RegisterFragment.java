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
import com.emrekentli.adoptme.model.request.RegisterRequest;
import com.emrekentli.adoptme.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterFragment extends Fragment {

    private EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    private Button registerButton;
    private TextView loginButton;

    public RegisterFragment() {
        // Gerekli boş kurucu metod
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        fullNameEditText = view.findViewById(R.id.fullNameEt);
        emailEditText = view.findViewById(R.id.emailEt);
        passwordEditText = view.findViewById(R.id.passET);
        confirmPasswordEditText = view.findViewById(R.id.confirmPassEt);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEt);
        loginButton = view.findViewById(R.id.alreadyHaveAccountTv);
        registerButton = view.findViewById(R.id.registrationButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(LoginFragment.class);
            }
        });

        return view;
    }

    private void performRegistration() {
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        checkInputs(fullName, email, password, confirmPassword, phoneNumber);
    }

    private void register(String fullName, String email, String password,  String phoneNumber) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<Void>> response = restInterface[0].registerUser(new RegisterRequest(fullName, email, password, phoneNumber));
        response.enqueue(new Callback<ApiResponse<Void>>() {

            @Override
            public void onResponse(Call<ApiResponse<Void>> call, retrofit2.Response<ApiResponse<Void>> response) {
                if (response.body().getMeta().getCode().equals("200")) {
                    Toast.makeText(getContext(), "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                    replaceFragments(WelcomeFragment.class);
                } else {
                    Toast.makeText(getContext(), "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
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
    private void checkInputs(String fullName, String email, String password, String confirmPassword, String phoneNumber) {
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Ad Soyad boş bırakılamaz!");
            fullNameEditText.requestFocus();
        } if (email.isEmpty()) {
            emailEditText.setError("Email boş bırakılamaz!");
            emailEditText.requestFocus();
        }if (password.isEmpty()) {
            passwordEditText.setError("Şifre boş bırakılamaz!");
            passwordEditText.requestFocus();
        } if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Şifre tekrarı boş bırakılamaz!");
            confirmPasswordEditText.requestFocus();
        }if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Telefon numarası boş bırakılamaz!");
            phoneNumberEditText.requestFocus();
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Şifreler eşleşmiyor!");
            confirmPasswordEditText.requestFocus();
        }
        if (!fullName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !phoneNumber.isEmpty() && password.equals(confirmPassword)) {
            register(fullName, email, password, phoneNumber);
        }

    }
}