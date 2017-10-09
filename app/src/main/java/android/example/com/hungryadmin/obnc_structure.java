package android.example.com.hungryadmin;

/**
 * Created by Aakash on 06-10-2017.
 */

public class obnc_structure {

    public String userdetails;


    public obnc_structure()
    {

    }

    public obnc_structure(String userdetails) {
        this.userdetails = userdetails;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }
}
