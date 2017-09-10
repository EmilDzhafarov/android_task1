package ua.nure.dzhafarov.task1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.nure.dzhafarov.task1.R;
import ua.nure.dzhafarov.task1.adapters.UserAdapter;
import ua.nure.dzhafarov.task1.models.User;
import ua.nure.dzhafarov.task1.utils.UserLab;
import ua.nure.dzhafarov.task1.activities.UserActivity;

public class UserListFragment extends Fragment {
    
    public static final int REQUEST_UPDATE_UI = 1;

    private RecyclerView recyclerView;
    private TextView textViewForNonUsers;
    private UserAdapter userAdapter;
    
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
        userAdapter = new UserAdapter(users, this);
        recyclerView.setAdapter(userAdapter);
        updateUI();
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
                startActivityForResult(intent, REQUEST_UPDATE_UI);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE_UI && resultCode == Activity.RESULT_OK) {
            updateUI();
        }
    }

    public void deleteUserAndUpdateUI(User user) {
        int pos = users.indexOf(user);

        users.remove(user);
        userAdapter.notifyItemRemoved(pos);
        UserLab.getInstance(getActivity()).deleteUser(user);
        
        checkForEmptyUsers(users);
    }

    private void updateUI() {
        UserLab userLab = UserLab.getInstance(getActivity());
        
        users.clear();
        users.addAll(userLab.getUsers());
        userAdapter.notifyDataSetChanged();

        checkForEmptyUsers(users);
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
}
