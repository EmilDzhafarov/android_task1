package ua.nure.dzhafarov.task1.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.nure.dzhafarov.task1.R;
import ua.nure.dzhafarov.task1.activities.UserActivity;
import ua.nure.dzhafarov.task1.fragments.UserListFragment;
import ua.nure.dzhafarov.task1.models.User;
import ua.nure.dzhafarov.task1.utils.UserManager;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    class UserHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private User user;

        private TextView fullName;
        private TextView age;

        UserHolder(View itemView) {
            super(itemView);
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
            fullName = (TextView) itemView.findViewById(R.id.user_list_item_fullname);
            age = (TextView) itemView.findViewById(R.id.user_list_item_age);
        }

        void bindUser(User u) {
            this.user = u;
            fullName.setText(user.getFullName());
            age.setText(user.getAgeString());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, R.id.edit_user, Menu.NONE, R.string.edit_user);
            MenuItem delete = menu.add(Menu.NONE, R.id.delete_user, Menu.NONE, R.string.delete_user);

            edit.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);
        }

        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_user:
                        Intent intent = UserActivity.newIntent(fragment.getActivity(), user);
                        fragment.startActivity(intent);
                        return true;
                    case R.id.delete_user:
                        UserManager.getInstance(fragment.getActivity()).deleteUser(user);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private List<User> users;
    private UserListFragment fragment;

    public UserAdapter(List<User> users, UserListFragment fragment) {
        this.users = users;
        this.fragment = fragment;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_list_item, parent, false);

        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.bindUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}