package ua.nure.dzhafarov.task1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import ua.nure.dzhafarov.task1.R;
import ua.nure.dzhafarov.task1.models.User;
import ua.nure.dzhafarov.task1.utils.UserManager;
import ua.nure.dzhafarov.task1.utils.UserOperationListener;

import static ua.nure.dzhafarov.task1.fragments.UserListFragment.REQUEST_USER_ADD;
import static ua.nure.dzhafarov.task1.fragments.UserListFragment.REQUEST_USER_UPDATE;
import static ua.nure.dzhafarov.task1.fragments.UserListFragment.USER_CHANGED;

public class UserFragment extends Fragment {
    
    public static final String USER = "user";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String USER_DATA = "user_data";
    private static final int REQUEST_DATE = 0;
    
    private User user;
    
    private EditText nameEditText;
    private EditText surnameEditText;
    private Button dateButton;

    public static UserFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);

        UserFragment userFragment = new UserFragment();
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Override
    public void onCreate(Bundle saveBundleState) {
        super.onCreate(saveBundleState);

        if (saveBundleState != null) {
            user = (User) saveBundleState.getSerializable(USER_DATA);
        } else {
            User user = (User) getArguments().getSerializable(USER);
            
            if (user == null) {
                this.user = new User();
            } else {
                this.user = user;
            }   
        }
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater ,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = (EditText) view.findViewById(R.id.edit_text_user_name);
        nameEditText.setText(user.getName());

        surnameEditText = (EditText) view.findViewById(R.id.edit_text_user_surname);
        surnameEditText.setText(user.getSurname());

        dateButton = (Button) view.findViewById(R.id.button_birthday_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(user.getBirthday());
                dialog.setTargetFragment(UserFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        updateDate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_user) {
            saveUser();
            return true;
        } else {
            return super.onOptionsItemSelected(item);   
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            LocalDate date = (LocalDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            user.setBirthday(date);
            updateDate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER_DATA, user);
    }

    private void updateDate() {
        dateButton.setText(
                DateTimeFormatter.ofPattern("dd MMM yyyy").format(user.getBirthday())
        );
    }
    
    private void saveUser() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        
        String errorMessage = null;
        
        if (name.isEmpty()) {
            errorMessage = getString(R.string.error_save_user_empty_name);
        } else if (surname.isEmpty()) {
            errorMessage = getString(R.string.error_save_user_empty_surname);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
           errorMessage = getString(R.string.error_save_user_invalid_birthday_date);  
        } 
        
        if (errorMessage != null) {
            Toast.makeText(
                    getActivity(),
                    errorMessage,
                    Toast.LENGTH_LONG
            ).show();
        } else {
            user.setName(name);
            user.setSurname(surname);
            
            passUserToParentActivity(user);
        }
    }
    
    private void passUserToParentActivity(User user) {
        Intent intent = new Intent();
        intent.putExtra(USER_CHANGED, user);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
