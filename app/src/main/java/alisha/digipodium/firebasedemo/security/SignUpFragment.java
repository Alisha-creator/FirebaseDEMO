package alisha.digipodium.firebasedemo.security;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import alisha.digipodium.firebasedemo.MainActivity;
import alisha.digipodium.firebasedemo.R;

public class SignUpFragment extends Fragment {


    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
    public  void updateUI(int status){
        if(status == 1){
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        }else if(status == 0){
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
        else if (status ==2){
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            /*Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);//your first fragment will be displayed from main activity
            getActivity().finish();//kill the auth activity*/
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>(){
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Security")
                                .setMessage("We have send you the verification mail,Please Check email.")
                                .setNeutralButton("Ok",null)
                                .create()
                                .show();
                    }else {
                        String errorMsg = task.getException().getMessage();
                        editEmail.setError(errorMsg);
                        editEmail.requestFocus();
                    }
                }
            });
            mAuth.signOut();//Logout
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        progressBar = view.findViewById(R.id.progressBar);
        TextView textAccount = view.findViewById(R.id.textAccount);

        textAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                SignUpFragment.this.updateUI(1);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                //Register User Here
                mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(getActivity(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        updateUI(2);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(btnRegister, "Registration Failed! Please try again." + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUI(0);
                    }
                });
            }
        });
    }
}