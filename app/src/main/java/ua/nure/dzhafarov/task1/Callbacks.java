package ua.nure.dzhafarov.task1;

import java.util.List;

import ua.nure.dzhafarov.task1.models.User;

public interface Callbacks {
    
    void onUserAdded(User user);
    
    void onUserDeleted(User user);
    
    void onUserUpdated(User user);
    
    void onUsersLoaded(List<User> users);
}
