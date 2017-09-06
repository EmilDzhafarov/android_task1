package ua.nure.dzhafarov.task1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

public class UserFragment extends Fragment {
    
    public static final String ARG_USER_ID = "user_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    
    private User user;
    
    private EditText nameEditText;

    private EditText surnameEditText;
    
    private Button dateButton;

    public static UserFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER_ID, id);

        UserFragment userFragment = new UserFragment();
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Override
    public void onCreate(Bundle saveBundleState) {
        super.onCreate(saveBundleState);
        UUID id = (UUID) getArguments().getSerializable(ARG_USER_ID);
        
        if (id == null) {
            user = new User();
        } else {
            user = UserLab.getInstance(getActivity()).getUserById(id);
        }
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater ,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        
        nameEditText = (EditText) v.findViewById(R.id.edit_text_user_name);
        nameEditText.setText(user.getName());
        
        surnameEditText = (EditText) v.findViewById(R.id.edit_text_user_surname);
        surnameEditText.setText(user.getSurname());
        
        dateButton = (Button) v.findViewById(R.id.button_birthday_date);
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
        
        return v;
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
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            user.setBirthday(date);
            updateDate();
        }
    }
    
    private void updateDate() {
        dateButton.setText(DateFormat.format("dd.MM.yyyy", user.getBirthday()));
    }
    
    private void saveUser() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        
        String errorMessage = null;
        
        if (name.isEmpty()) {
            errorMessage = getString(R.string.error_save_user_empty_name);
        } else if (surname.isEmpty()) {
            errorMessage = getString(R.string.error_save_user_empty_surname);
        } else if (user.getBirthday().getTime() >= new Date().getTime()) {
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
            
            saveUserInDb();
            sendUserId();
            getActivity().finish();
        }
    }

    private void sendUserId() {
        Intent intent = new Intent();
        intent.putExtra(ARG_USER_ID, user.getId());
        getActivity().setResult(Activity.RESULT_OK, intent);
    }
    
    private void saveUserInDb() {
        UserLab lab = UserLab.getInstance(getActivity());
        User u = lab.getUserById(user.getId());

        if (u == null) {
            lab.addUser(user);
        } else {
            lab.updateUser(user);
        }
    }
}
