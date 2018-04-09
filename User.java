package connnect_try;

import java.io.Serializable;


public class User implements Serializable {
    public int userType;
    public String ID;

    public User(String id, int usertype){
        ID = id;
        userType = usertype;
    }
    public int getUserType(){
        return userType;
    }
    public String getID(){
        return ID;
    }
    //TODO: using string information to initialize
    //TODO: get information into strings
}
