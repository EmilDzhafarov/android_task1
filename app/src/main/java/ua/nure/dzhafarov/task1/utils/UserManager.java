package ua.nure.dzhafarov.task1.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ua.nure.dzhafarov.task1.Callbacks;
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
            instance = new UserManager(context);
        }
        
        return instance;
    }
    
    public void registerCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }
    
    public void loadUsers() {
        final List<User> users = new ArrayList<>();
        
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                     users.addAll(userLab.getUsers());   
                    }
                }
        );
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            callbacks.onUsersLoaded(users);
        }
    }
    
    public void addUser(final User user) {
        Thread t = new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        userLab.addUser(user);
                    }
                }
        );
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            callbacks.onUserAdded(user);
        }
    }
    
    public void deleteUser(final User user) {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.deleteUser(user);
                    }
                }
        );
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            callbacks.onUserDeleted(user);
        }
    }
    
    public void updateUser(final User user) {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        userLab.updateUser(user);
                    }
                }
        );
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            callbacks.onUserUpdated(user);
        }
    }
    
    public User getUserById(final UUID id) {
        final User[] users = new User[1];
        
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                       users[0] = userLab.getUserById(id);
                    }
                }
        );
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return users[0];
    }
}
