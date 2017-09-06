package ua.nure.dzhafarov.task1.database;


public class UserDBSchema {
    public static class UserTable {
        public static final String TABLE_NAME = "users";
        
        public static class Columns {
            public static final String UUID = "id";
            public static final String NAME = "name";
            public static final String SURNAME = "surname";
            public static final String BIRTHDAY = "birthday";
        }
    }
}
