package com.example.city_matcher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.city_matcher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;

    // define UI elements
    private EditText emailEdit;
    private EditText passwordEdit;
    private String email;
    private String password;
    private Button loginButton;
    private Button newAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        // get UI elements
        emailEdit = v.findViewById(R.id.username);
        passwordEdit = v.findViewById(R.id.password);
        loginButton = v.findViewById(R.id.submitLogin);
        newAccountButton = v.findViewById(R.id.goToCreateAccount);

        // init auth reference
        mAuth = FirebaseAuth.getInstance();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        // new login listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get email to store in realtime database with city choice
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();

                if(inputValid(email, password)) {
                    // sign in user
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // go to landing page
                                        Intent mIntent = new Intent(getActivity(), LandingActivity.class);
                                        startActivity(mIntent);
                                        // finish activity so they can't go back without logging out
                                        getActivity().finish();

                                    } else {
                                        Toast.makeText(getActivity(),
                                                getActivity().getResources().getString(R.string.loginFailToast),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        // new account listener
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getActivity(), NewUserActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private static boolean inputValid(String email, String password) {
        return !(email.equals("") || password.equals(""));
    }
}
