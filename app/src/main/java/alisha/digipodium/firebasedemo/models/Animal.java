package alisha.digipodium.firebasedemo.models;


//@IgnoreExtraProperties
public class Animal {

    public String name,description;

    public Animal(){
        //required for firebase firestore model
    }

    public Animal(String name, String description){
        this.name = name;
        this.description = description;
        //this.timestamp =  timestamp;
    }

}