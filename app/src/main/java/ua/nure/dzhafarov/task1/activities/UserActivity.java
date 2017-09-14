package ua.nure.dzhafarov.task1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import ua.nure.dzhafarov.task1.fragments.UserFragment;
import ua.nure.dzhafarov.task1.models.User;

public class UserActivity extends SingleFragmentActivity {
    
    public static final String EXTRA_USER = "ua.nure.dzhafarov.task1.UserActivity.user";
    
    public Fragment createFragment() {
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        return UserFragment.newInstance(user);   
    }

    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }
}
