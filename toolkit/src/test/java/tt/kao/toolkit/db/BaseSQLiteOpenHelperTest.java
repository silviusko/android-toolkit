package tt.kao.toolkit.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import tt.kao.toolkit.BuildConfig;

import static org.junit.Assert.*;

/**
 * @author luke_kao
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BaseSQLiteOpenHelperTest {
    private MockSQLiteOpenHelper mHelper;

    private static final String TABLE_NAME = "test";
    private static final String COLUMN1_KEY = "column1";
    private static final String COLUMN1_VALUE1 = "value1";
    private static final String COLUMN1_VALUE2 = "abc";

    private static final String COLUMN2_KEY = "column2";
    private static final int COLUMN2_VALUE1 = 123;
    private static final int COLUMN2_VALUE2 = 456;

    private static final String COLUMN3_KEY = "column3";
    private static final String COLUMN3_VALUE1 = "value3";
    private static final String COLUMN3_VALUE2 = "def";

    private static final String[] PROJECTION = {
        COLUMN1_KEY,
        COLUMN2_KEY,
        COLUMN3_KEY,
    };

    private static final String SELECTION = COLUMN1_KEY + " = ?";
    private static final String[] SELECTION_ARGS = {
        COLUMN1_VALUE1
    };

    @Before
    public void setUp() throws Exception {
        mHelper = new MockSQLiteOpenHelper();
    }

    @Test
    public void insert_nullValue() throws Exception {
        long id = mHelper.insert(null, null);
        assertEquals(-1, id);

        id = mHelper.insert(TABLE_NAME, null);
        assertEquals(-1, id);

        id = mHelper.insert(null, new ContentValues());
        assertEquals(-1, id);
    }

    @Test
    public void insert_emptyValue() throws Exception {
        ContentValues values = new ContentValues();

        long id = mHelper.insert(TABLE_NAME, values);
        assertEquals(-1, id);
    }

    @Test
    public void insert_noPrimaryKey() throws Exception {
        ContentValues values = new ContentValues();
        values.put(COLUMN2_KEY, "1234");

        long id = mHelper.insert(TABLE_NAME, values);
        assertTrue(id > 0);
    }

    @Test
    public void insert_normalValue() throws Exception {
        ContentValues values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);

        long id = mHelper.insert(TABLE_NAME, values);
        assertTrue(id > 0);
    }

    @Test
    public void insert_duplicatedValue() throws Exception {
        ContentValues values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);

        mHelper.insert(TABLE_NAME, values);

        long id = mHelper.insert(TABLE_NAME, values);
        assertEquals(-1, id);
    }

    @Test
    public void bulkInsert_emptyValue() throws Exception {
        List<ContentValues> valuesList = new ArrayList<>();
        valuesList.add(new ContentValues());
        valuesList.add(new ContentValues());

        List<Long> ids = mHelper.bulkInsert(TABLE_NAME, valuesList);
        assertEquals(2, ids.size());
        assertTrue(ids.get(0) == -1);
        assertTrue(ids.get(1) == -1);
    }

    @Test
    public void bulkInsert_normalValue() throws Exception {
        List<ContentValues> valuesList = new ArrayList<>();
        ContentValues values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);
        valuesList.add(values);

        values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE2);
        valuesList.add(values);

        List<Long> ids = mHelper.bulkInsert(TABLE_NAME, valuesList);
        assertEquals(2, ids.size());
        assertTrue(ids.get(0) > 0);
        assertTrue(ids.get(1) > 0);
    }

    @Test
    public void bulkInsert_duplicatedValue() throws Exception {
        List<ContentValues> valuesList = new ArrayList<>();
        ContentValues values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);
        valuesList.add(values);

        values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);
        valuesList.add(values);

        List<Long> ids = mHelper.bulkInsert(TABLE_NAME, valuesList);
        assertEquals(2, ids.size());
        assertTrue(ids.get(0) > 0);
        assertTrue(ids.get(1) == -1);
    }

    @Test
    public void query_nullValue() throws Exception {
        Cursor cursor = mHelper.query(null, null, null, null, null, null);
        assertNull(cursor);

        cursor = mHelper.query(null, PROJECTION, null, null, null, null);
        assertNull(cursor);

        cursor = mHelper.query(TABLE_NAME, null, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
        assertEquals(PROJECTION.length, cursor.getColumnCount());
    }

    @Test
    public void query_emptyTable() throws Exception {
        Cursor cursor = mHelper.query(TABLE_NAME, PROJECTION, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
        assertEquals(PROJECTION.length, cursor.getColumnCount());

        cursor.close();
    }

    @Test
    public void query_normalValue() throws Exception {
        insertTestData();

        Cursor cursor = mHelper.query(TABLE_NAME, PROJECTION, null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToFirst());

        assertEquals(2, cursor.getCount());
        assertEquals(PROJECTION.length, cursor.getColumnCount());
        assertEquals(COLUMN1_VALUE1, cursor.getString(cursor.getColumnIndex(COLUMN1_KEY)));

        cursor.close();
    }

    @Test
    public void update_nullValue() throws Exception {
        insertTestData();

        int num = mHelper.update(null, null, null, null);
        assertEquals(-1, num);

        num = mHelper.update(TABLE_NAME, null, null, null);
        assertEquals(-1, num);

        num = mHelper.update(null, new ContentValues(), null, null);
        assertEquals(-1, num);
    }

    @Test
    public void update_emptyValue() throws Exception {
        insertTestData();

        int num = mHelper.update(TABLE_NAME, new ContentValues(), SELECTION, SELECTION_ARGS);
        assertEquals(-1, num);
    }

    @Test
    public void update_wrongKey() throws Exception {
        insertTestData();

        ContentValues values = new ContentValues();
        values.put(COLUMN2_KEY, -1);
        values.put(COLUMN3_KEY, "(0.0)");

        String[] selectionArgs = {
            "Orz",
        };

        int num = mHelper.update(TABLE_NAME, values, SELECTION, selectionArgs);
        assertEquals(0, num);
    }

    @Test
    public void update_normalValue() throws Exception {
        insertTestData();

        ContentValues values = new ContentValues();
        values.put(COLUMN2_KEY, -1);
        values.put(COLUMN3_KEY, "(0.0)");

        int num = mHelper.update(TABLE_NAME, values, SELECTION, SELECTION_ARGS);
        assertEquals(1, num);

        Cursor cursor = mHelper.query(TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToFirst());
        assertEquals(-1, cursor.getInt(cursor.getColumnIndex(COLUMN2_KEY)));
        assertEquals("(0.0)", cursor.getString(cursor.getColumnIndex(COLUMN3_KEY)));
    }

    @Test
    public void delete_nullValue() throws Exception {
        insertTestData();

        int num = mHelper.delete(null, null, null);
        assertEquals(-1, num);
    }

    @Test
    public void delete_wrongKey() throws Exception {
        insertTestData();

        String[] selectionArgs = {
            "Orz",
        };

        int num = mHelper.delete(TABLE_NAME, SELECTION, selectionArgs);
        assertEquals(0, num);

        Cursor cursor = mHelper.query(TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
    }

    @Test
    public void delete_normalValue() throws Exception {
        insertTestData();

        int num = mHelper.delete(TABLE_NAME, SELECTION, SELECTION_ARGS);
        assertEquals(1, num);

        Cursor cursor = mHelper.query(TABLE_NAME, PROJECTION, SELECTION, SELECTION_ARGS, null, null);
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
    }

    private void insertTestData() {
        ContentValues values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE1);
        values.put(COLUMN2_KEY, COLUMN2_VALUE1);
        values.put(COLUMN3_KEY, COLUMN3_VALUE1);

        long id = mHelper.insert(TABLE_NAME, values);
        assertTrue(id > 0);

        values = new ContentValues();
        values.put(COLUMN1_KEY, COLUMN1_VALUE2);
        values.put(COLUMN2_KEY, COLUMN2_VALUE2);
        values.put(COLUMN3_KEY, COLUMN3_VALUE2);

        id = mHelper.insert(TABLE_NAME, values);
        assertTrue(id > 0);
    }


    private static class MockSQLiteOpenHelper extends BaseSQLiteOpenHelper {

        public MockSQLiteOpenHelper() {
            super(RuntimeEnvironment.application, "mock.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ("
                + COLUMN1_KEY + " TEXT PRIMARY KEY , "
                + COLUMN2_KEY + " INTEGER , "
                + COLUMN3_KEY + " TEXT )";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}