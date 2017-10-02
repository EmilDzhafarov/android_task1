package ua.nure.dzhafarov.task1.utils;

import android.content.Context;
import java.util.List;
import ua.nure.dzhafarov.task1.models.User;

public class UserManager {
    
    private static UserManager instance;
    private UserLab userLab;
    
    private UserManager(Context context) {
        userLab = UserLab.getInstance(context);
    }
    
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        
        return instance;
    }
    
    public void loadUsers(final UserOperationListener<List<User>> listener) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        List<User> users = userLab.getUsers();
                        listener.onSuccess(users);
                    }
                }
        ).start();
    }
    
    public void addUser(final User user, final UserOperationListener<User> listener) {
        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        userLab.addUser(user);
                        listener.onSuccess(user);
                    }
                }
        ).start();
    }
    
    public void deleteUser(final User user, final UserOperationListener<User> listener) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.deleteUser(user);
                        listener.onSuccess(user);
                    }
                }
        ).start();
    }
    
    public void updateUser(final User user, final UserOperationListener<User> listener) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.updateUser(user);
                        listener.onSuccess(user);
                    }
                }
        ).start();
    }
}
