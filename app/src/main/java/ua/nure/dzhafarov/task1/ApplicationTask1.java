package ua.nure.dzhafarov.task1;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class ApplicationTask1 extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
