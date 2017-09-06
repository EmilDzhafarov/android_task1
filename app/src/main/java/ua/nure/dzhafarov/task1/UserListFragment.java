package ua.nure.dzhafarov.task1;

import android.app.Activity;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static ua.nure.dzhafarov.task1.UserFragment.ARG_USER_ID;

public class UserListFragment extends Fragment {
    
    public static final int REQUEST_USER_ID = 1;
    
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UUID lastUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        
        updateUI();

        return view;
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
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USER_ID && resultCode == Activity.RESULT_OK) {
            lastUserId = (UUID) data.getSerializableExtra(ARG_USER_ID);
        }
    }

    private void updateUI() {
        UserLab userLab = UserLab.getInstance(getActivity());
        List<User> users = userLab.getUsers();

        if (userAdapter == null) {
            userAdapter = new UserAdapter(users);
            recyclerView.setAdapter(userAdapter);
        } else {
            userAdapter.setUsers(users);
            int pos = userLab.indexOfById(lastUserId);
            userAdapter.notifyItemChanged(pos);
        }
    }
    
    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {
        
        private User user;
        
        private TextView fullName;
        private TextView age;
        
        UserHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
            fullName = (TextView) itemView.findViewById(R.id.user_list_item_fullname);
            age = (TextView) itemView.findViewById(R.id.user_list_item_age);
        }

        void bindUser(User u) {
            this.user = u;
            fullName.setText(String.format("%s %s", user.getName(), user.getSurname()));
            age.setText(String.format(
                    Locale.US,"%d y/o", getDiffYears(user.getBirthday(), new Date()))
            );
        }

        private int getDiffYears(Date first, Date last) {
            Calendar a = getCalendar(first);
            Calendar b = getCalendar(last);
            
            int diff = b.get(YEAR) - a.get(YEAR);
            if (a.get(MONTH) > b.get(MONTH) ||
                    (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
                diff--;
            }
            return diff;
        }

        private Calendar getCalendar(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        
        @Override
        public void onClick(View v) {
            Intent intent = UserActivity.newIntent(getActivity(), user.getId());
            startActivityForResult(intent, REQUEST_USER_ID);
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
                switch (item.getItemId()){
                    case R.id.edit_user:
                        Intent intent = UserActivity.newIntent(getActivity(), user.getId());
                        startActivityForResult(intent, REQUEST_USER_ID);
                        return true;
                    case R.id.delete_user:
                        UserLab.getInstance(getActivity()).deleteUser(user);
                        userAdapter = null;
                        updateUI();
                        return true;
                    default:
                        return false;       
                }
            }
        };
    }
    
    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        
        private List<User> users;

        UserAdapter(List<User> users) {
            this.users = users;
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

        void setUsers(List<User> users) {
            this.users = users;
        }
        
        List<User> getUsers() {
            return users;
        }
    }
}
