package com.emrekentli.adoptme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.emrekentli.adoptme.R;

public class RegisterFragment extends Fragment {

    private EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    private Button registerButton;

    public RegisterFragment() {
        // Gerekli boş kurucu metod
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        registerButton = view.findViewById(R.id.registrationButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
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
        register(fullName, email, password, confirmPassword, phoneNumber);
    }

    private void register(String fullName, String email, String password, String confirmPassword, String phoneNumber) {

    }

    private void checkInputs(String fullName, String email, String password, String confirmPassword, String phoneNumber) {
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Ad Soyad boş bırakılamaz!");
            fullNameEditText.requestFocus();
        } else if (email.isEmpty()) {
            emailEditText.setError("Email boş bırakılamaz!");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Şifre boş bırakılamaz!");
            passwordEditText.requestFocus();
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Şifre tekrarı boş bırakılamaz!");
            confirmPasswordEditText.requestFocus();
        } else if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Telefon numarası boş bırakılamaz!");
            phoneNumberEditText.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Şifreler eşleşmiyor!");
            confirmPasswordEditText.requestFocus();
        }
    }
}