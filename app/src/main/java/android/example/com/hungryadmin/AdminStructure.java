package android.example.com.hungryadmin;

/**
 * Created by Aakash on 11-09-2017.
 */

public class AdminStructure {

    private String userdetails;
    private String email;

   public AdminStructure(){

   }

    public AdminStructure(String userdetails, String email) {
        this.userdetails = userdetails;
        this.email = email;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
