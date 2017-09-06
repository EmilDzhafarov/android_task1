package ua.nure.dzhafarov.task1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ua.nure.dzhafarov.task1.database.UserBaseHelper;
import ua.nure.dzhafarov.task1.database.UserCursorWrapper;

import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.BIRTHDAY;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.NAME;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.SURNAME;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.Columns.UUID;
import static ua.nure.dzhafarov.task1.database.UserDBSchema.UserTable.TABLE_NAME;

public class UserLab {
    
    private static UserLab instance;

    private Context context;
    private SQLiteDatabase database;
    
    public static synchronized UserLab getInstance(Context context) {
        if (instance == null) {
            instance = new UserLab(context);
        }
        
        return instance;
    }
    
    private UserLab(Context context) {
        this.context = context.getApplicationContext();
        database = new UserBaseHelper(context).getWritableDatabase();
        
    }
    
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        UserCursorWrapper cursor = queryUsers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                users.add(cursor.getUser());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return users;
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();

        values.put(UUID, user.getId().toString());
        values.put(NAME, user.getName());
        values.put(SURNAME, user.getSurname());
        values.put(BIRTHDAY, user.getBirthday().getTime());

        return values;
    }
    
    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new UserCursorWrapper(cursor);
    }

    public User getUserById(java.util.UUID id) {
        UserCursorWrapper cursor = queryUsers(
                UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }
    
    public void addUser(User user) {
        ContentValues values = getContentValues(user);
        
        database.insert(TABLE_NAME, null, values);
    }
    
    public void updateUser(User user) {
        ContentValues values = getContentValues(user);
        String uuid = user.getId().toString();

        database.update(TABLE_NAME, values, UUID + " = ?", new String[] {uuid});
    }
    
    public void deleteUser(User user) {
        String uuid = user.getId().toString();

        database.delete(TABLE_NAME, UUID + " = ?", new String[] {uuid});
    }
    
    public int indexOfById(java.util.UUID id) {
        int i = 0;
        for (User user : getUsers()) {
            if (user.getId().equals(id)) {
                return i;
            }
            
            i++;
        }
        
        return -1;
    }
}
