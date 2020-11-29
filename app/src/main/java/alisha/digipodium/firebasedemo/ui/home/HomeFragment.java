package alisha.digipodium.firebasedemo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import alisha.digipodium.firebasedemo.R;
import alisha.digipodium.firebasedemo.models.Animal;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView rvAnimals;
    public  static final String ANIMALS = "animals";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        rvAnimals = view.findViewById(R.id.rvAnimals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAnimalList();
    }

    public void setAnimalList(){
        final List<Animal> animalList = new ArrayList<>();
        db.collection(ANIMALS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document:documents){
                        animalList.add(document.toObject(Animal.class));
                    }
                    AnimalAdaptor adaptor = new AnimalAdaptor(getActivity(),R.layout.card_animal_layout,animalList);
                    rvAnimals.setAdapter(adaptor);
                }else {
                    String error = task.getException().getMessage();
                    animalList.add(new Animal("No Data Found","Insert Some Data"));
                    AnimalAdaptor adaptor = new AnimalAdaptor(getActivity(),R.layout.card_animal_layout,animalList);
                    rvAnimals.setAdapter(adaptor);
                }
            }
        });
    }
    class AnimalAdaptor extends RecyclerView.Adapter<AnimalAdaptor.Holder>{

        Context c;
        int layout;
        List<Animal> animals;
        LayoutInflater inflater;

        public AnimalAdaptor(Context c, int layout, List<Animal> animals) {
            this.c = c;
            this.layout = layout;
            this.animals = animals;
            inflater =LayoutInflater.from(c);
        }

        @NonNull
        @Override
        public AnimalAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = inflater.inflate(layout,parent,false);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AnimalAdaptor.Holder holder, int position) {
            Animal animal = animals.get(position);
            holder.tvName.setText(animal.name);
            holder.imgEdit.setTag(animal);
            holder.imgDelete.setTag(animal);
        }

        @Override
        public int getItemCount() {
            return animals.size();
        }

        public class Holder extends RecyclerView.ViewHolder{

            TextView tvName;
            ImageView imgEdit,imgDelete;

            public Holder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                imgDelete =itemView.findViewById(R.id.imgDelete);
                imgEdit = itemView.findViewById(R.id.imgEdit);

                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animal animal = (Animal) view.getTag();
                        db.collection(ANIMALS).whereEqualTo("name", animal.name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Toast.makeText(c, "data received", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        Animal animal = (Animal) view2.getTag();
                        HomeFragmentDirections.ActionNavHomeToNavGallery action = HomeFragmentDirections.actionNavHomeToNavGallery();
                        action.setDescription(animal.description);
                        action.setName(animal.name);
                        Navigation.findNavController(view2).navigate(action);
                    }
                });
            }
        }
    }
}