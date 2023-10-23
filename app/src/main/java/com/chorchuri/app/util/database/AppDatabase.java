package com.chorchuri.app.util.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.chorchuri.app.model.Banner;
import com.chorchuri.app.model.Category;
import com.chorchuri.app.model.OfflineVideo;
import com.chorchuri.app.model.OfflineVideoSections;
import com.chorchuri.app.model.Video;
import com.chorchuri.app.model.VideoSection;



@Database(entities = {Banner.class, OfflineVideoSections.class, OfflineVideo.class}, version = 4, exportSchema = false)
public  abstract class AppDatabase extends RoomDatabase {

    public abstract OnDataBaseAction dataBaseAction();
    private static volatile AppDatabase appDatabase;

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}

