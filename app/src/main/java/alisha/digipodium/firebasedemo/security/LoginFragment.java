package alisha.digipodium.firebasedemo.security;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import alisha.digipodium.firebasedemo.MainActivity;
import alisha.digipodium.firebasedemo.R;

public class LoginFragment extends Fragment {

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressBar pBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public  void updateUI(int status){
        if(status == 1){
            pBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }else if(status == 0){
            pBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
        else if (status ==2){
            pBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            if (mAuth.getCurrentUser().isEmailVerified()){
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);//your first fragment will be displayed from main activity
                getActivity().finish();//kill the auth activity
            }
            else{
                Snackbar.make(btnLogin, "Email Not verified", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("resend", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // here you can write code to resend verification mail as given in register fragment
                    }
                }).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        pBar = view.findViewById(R.id.progressBar2);
        TextView tvCreateAccount = view.findViewById(R.id.tvCreateAcc);

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateUI(1);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "Login Succussful", Toast.LENGTH_LONG).show();
                        updateUI(2);
                        //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(btnLogin, "ERROR" + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUI(0);
                    }
                });
            }
        });
    }
}