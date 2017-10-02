package ua.nure.dzhafarov.task1.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import ua.nure.dzhafarov.task1.R;
import ua.nure.dzhafarov.task1.models.User;
import ua.nure.dzhafarov.task1.utils.UserOperationListener;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    
    class UserHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private User user;

        private TextView fullName;
        private TextView age;

        UserHolder(View itemView) {
            super(itemView);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            fullName = (TextView) itemView.findViewById(R.id.user_list_item_fullname);
            age = (TextView) itemView.findViewById(R.id.user_list_item_age);
        }

        void bindUser(User u) {
            this.user = u;
            fullName.setText(user.getFullName());
            age.setText(user.getAgeString());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onSuccess(user);
            return false;
        }
    }

    private List<User> users;
    private UserOperationListener<User> listener;

    public UserAdapter(List<User> users, UserOperationListener<User> listener) {
        this.users = users;
        this.listener = listener;
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