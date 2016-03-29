package tt.kao.toolkit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luke_kao
 */
public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
    public BaseSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String orderBy, String limit) {
        Cursor cursor = null;

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            cursor = db.query(table, projection, selection, selectionArgs, null, null, orderBy, limit);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public long insert(String table, ContentValues values) {
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            id = db.insert(table, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return id;
    }

    public List<Long> bulkInsert(String table, List<ContentValues> valuesList) {
        List<Long> ids = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues values : valuesList) {
                long id = db.insert(table, null, values);
                ids.add(id);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            ids.clear();
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return ids;
    }

    public int delete(String table, String selection, String[] selectionArgs) {
        int num = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            num = db.delete(table, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            num = -1;
        } finally {
            db.endTransaction();
        }

        return num;
    }

    public int update(String table, ContentValues values, String selection, String[] selectionArgs) {
        int num = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            num = db.update(table, values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            num = -1;
        } finally {
            db.endTransaction();
        }

        return num;
    }
}
