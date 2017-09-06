package ua.nure.dzhafarov.task1.activities;

import android.support.v4.app.Fragment;

import ua.nure.dzhafarov.task1.fragments.UserListFragment;

public class UserListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }
}
