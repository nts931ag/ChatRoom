package account;

import java.util.ArrayList;

/**
 * PACKAGE_NAME
 * Created by Thai Son
 * Date 16/01/2022 - 11:51 CH
 * Description: ...
 */
public class Account implements Comparable<Account>{
    private String username;
    private String password;

    public Account(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        if (this.username != null)
            return this.username;
        return null;
    }

    public String getPassword(){
        return this.password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public int compareTo(Account o) {
        if(this.username.equalsIgnoreCase(o.getUsername())&&this.password.equalsIgnoreCase(o.getPassword())){
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object v){
        boolean retVal = false;
        if (v instanceof Account){
            Account a = (Account) v;
            if(a.getUsername().equalsIgnoreCase(username)&&a.getPassword().equalsIgnoreCase(password))
                retVal = true;

        }
        return  retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    public static void main(String[] args){
        Account a = new Account("thaison","1234");
        Account b = new Account("thaison", "1234");

        ArrayList<Account> lst = new ArrayList<>();
        lst.add(a);

        System.out.println(lst.contains(b));
    }
}
