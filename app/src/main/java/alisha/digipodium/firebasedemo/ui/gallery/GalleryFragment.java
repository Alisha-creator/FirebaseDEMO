package alisha.digipodium.firebasedemo.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import alisha.digipodium.firebasedemo.R;
import alisha.digipodium.firebasedemo.models.Animal;

public class GalleryFragment extends Fragment {

    private FloatingActionButton fabSave;
    private ProgressBar probar;
    private EditText editName;
    private EditText editDesc;
    private FirebaseFirestore db;
    public static final String ANIMALS = "animals";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabSave = view.findViewById(R.id.fabSave);
        probar = view.findViewById(R.id.proBar);
        editName = view.findViewById(R.id.editName);
        editDesc = view.findViewById(R.id.editDesc);
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null){
            GalleryFragmentArgs args = GalleryFragmentArgs.fromBundle(getArguments());
            String name = args.getName();
            String desc = args.getDescription();
            if (name.equals("Null")){
                editName.setText(name);
            }
            if (!desc.equals(null)){
                editDesc.setText(desc);
            }
        }

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(1);
                String desc = editDesc.getText().toString();
                String name = editName.getText().toString();

                db.collection(ANIMALS).add(new Animal(name, desc)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            updateUI(2);
                        } else {
                            String error = task.getException().getMessage();
                            Snackbar.make(fabSave, error, BaseTransientBottomBar.LENGTH_LONG).show();
                            updateUI(0);
                        }
                    }
                });
            }
        });
    }
    private void updateUI(int i) {
        if (i == 1) {
            fabSave.setEnabled(false);
            probar.setVisibility(View.VISIBLE);
        } else if (i == 2) {
            fabSave.setEnabled(true);
            probar.setVisibility(View.GONE);
            editDesc.setText("");
            editName.setText("");
        } else if (i == 0) {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }
}