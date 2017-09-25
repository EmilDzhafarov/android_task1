package ua.nure.dzhafarov.task1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.nure.dzhafarov.task1.utils.Callbacks;
import ua.nure.dzhafarov.task1.R;
import ua.nure.dzhafarov.task1.adapters.UserAdapter;
import ua.nure.dzhafarov.task1.models.User;
import ua.nure.dzhafarov.task1.activities.UserActivity;
import ua.nure.dzhafarov.task1.utils.UserManager;

public class UserListFragment extends Fragment implements Callbacks {

    private RecyclerView recyclerView;
    private TextView textViewForNonUsers;
    private UserAdapter userAdapter;
    private UserManager userManager;

    private List<User> users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewForNonUsers = (TextView) view.findViewById(R.id.text_view_for_non_users);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        users = new ArrayList<>();

        userAdapter = new UserAdapter(users, new UserAdapter.UserClickListener() {
            @Override
            public void onUserClicked(User user) {
                int pos = users.indexOf(user);

                recyclerView
                        .findViewHolderForAdapterPosition(pos)
                        .itemView
                        .setOnCreateContextMenuListener(new ContextMenuListenerImpl(user));
            }
        });
        
        recyclerView.setAdapter(userAdapter);

        userManager = UserManager.getInstance(getActivity());
        userManager.registerCallbacks(this);

        userManager.loadUsers();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_users, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_user:
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onUserAdded(final User user) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userManager.loadUsers();
            }
        });
    }

    @Override
    public void onUserUpdated(final User user) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userManager.loadUsers();
            }
        });
    }

    @Override
    public void onUserDeleted(final User user) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pos = users.indexOf(user);
                users.remove(pos);
                userAdapter.notifyItemRemoved(pos);
                checkForEmptyUsers(users);
            }
        });
    }

    @Override
    public void onUsersLoaded(final List<User> us) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                users.clear();
                users.addAll(us);

                userAdapter.notifyDataSetChanged();
                checkForEmptyUsers(users);
            }
        });
    }

    private void checkForEmptyUsers(List<User> users) {
        if (users.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewForNonUsers.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewForNonUsers.setVisibility(View.GONE);
        }
    }

    private class ContextMenuListenerImpl implements View.OnCreateContextMenuListener {

        private User user;

        ContextMenuListenerImpl(User user) {
            this.user = user;
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
                        Intent intent = UserActivity.newIntent(getActivity(), user);
                        startActivity(intent);
                        return true;
                    case R.id.delete_user:
                        UserManager.getInstance(getActivity()).deleteUser(user);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }
}
