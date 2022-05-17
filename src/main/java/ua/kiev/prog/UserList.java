package ua.kiev.prog;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserList {
    private final static UserList userList = new UserList();

    private List<Account> currentUserList = new LinkedList<Account>();
    Gson gson;

    private UserList() {
        gson = new GsonBuilder().create();
    }

    public static UserList getInstance() {
        return userList;
    }

    //true - account has added, false - has not added
    public synchronized boolean addUser(Account account) {
        if (!isAccountExist(account)) {
            currentUserList.add(account);
            return true;
        }
        return false;
    }

    //true - current user is present; false - absent
    private boolean isAccountExist(Account account) {
        for (Account a : currentUserList) {
            if (account.getName().equals(a.getName()))
                return true;
        }
        return false;
    }

    public synchronized void exitAccount(Account account) {
        System.out.println("in exit account");
        for (int i = 0; i <= currentUserList.size(); i++) {
            if (currentUserList.get(i).getName().equals(account.getName())) {
                currentUserList.remove(i);
                System.out.println("exit account");
            }
        }
    }

    public synchronized void changeStatus(String accountName, String status) {
        for (Account a : currentUserList) {
            if (a.getName().equals(accountName)) {
                a.setStatus(status);
                break;
            }
        }
    }

    public synchronized String toJson() {
        return gson.toJson(new JsonUserList(currentUserList));
    }
}
