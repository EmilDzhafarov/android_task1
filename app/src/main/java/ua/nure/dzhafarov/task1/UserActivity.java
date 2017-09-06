package ua.nure.dzhafarov.task1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import java.util.UUID;

public class UserActivity extends SingleFragmentActivity {
    
    public static final String EXTRA_USER_ID = "ua.nure.dzhafarov.task1.user_id";
    
    public Fragment createFragment() {
        UUID userId = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        return UserFragment.newInstance(userId);   
    }

    public static Intent newIntent(Context packageContext, UUID userId) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }
}
