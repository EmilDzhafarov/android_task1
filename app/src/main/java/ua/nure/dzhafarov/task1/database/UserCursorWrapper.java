package ua.nure.dzhafarov.task1.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import ua.nure.dzhafarov.task1.User;

import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.BIRTHDAY;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.NAME;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.SURNAME;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.UUID;

public class UserCursorWrapper extends CursorWrapper {
    
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uuidString = getString(getColumnIndex(UUID));
        String name = getString(getColumnIndex(NAME));
        String surname = getString(getColumnIndex(SURNAME));
        long birthday = getLong(getColumnIndex(BIRTHDAY));

        User user = new User();
        user.setId(java.util.UUID.fromString(uuidString));
        user.setName(name);
        user.setSurname(surname);
        user.setBirthday(new Date(birthday));
        
        return user;
    }
}
