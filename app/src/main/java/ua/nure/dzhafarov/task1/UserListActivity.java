package ua.nure.dzhafarov.task1;

import android.support.v4.app.Fragment;

public class UserListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }
}
