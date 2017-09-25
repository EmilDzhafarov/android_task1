package ua.nure.dzhafarov.task1.utils;

import android.content.Context;
import java.util.List;
import ua.nure.dzhafarov.task1.models.User;

public class UserManager {
    
    private static UserManager instance;
    private UserLab userLab;
    private Callbacks callbacks;
    
    private UserManager(Context context) {
        userLab = UserLab.getInstance(context);
    }
    
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        
        return instance;
    }
    
    public void registerCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    
    public void loadUsers() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        List<User> users = userLab.getUsers();
                        callbacks.onUsersLoaded(users);
                    }
                }
        ).start();
    }
    
    public void addUser(final User user) {
        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        userLab.addUser(user);
                        callbacks.onUserAdded(user);
                    }
                }
        ).start();
    }
    
    public void deleteUser(final User user) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.deleteUser(user);
                        callbacks.onUserDeleted(user);
                    }
                }
        ).start();
    }
    
    public void updateUser(final User user) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.updateUser(user);
                        callbacks.onUserUpdated(user);
                    }
                }
        ).start();
    }
}
