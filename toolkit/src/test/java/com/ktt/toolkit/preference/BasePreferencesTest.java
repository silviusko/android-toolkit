package com.ktt.toolkit.preference;

import android.content.Context;

import com.ktt.toolkit.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author luke_kao
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BasePreferencesTest {
    private MockPreferences mPreferences;

    @Before
    public void setUp() throws Exception {
        mPreferences = new MockPreferences(RuntimeEnvironment.application);
    }

    @Test
    public void loadAll_defaultValue() {
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void saveAll_normalValue() {
        mPreferences.stringValue = "abc";
        mPreferences.intValue = 4321;
        mPreferences.boolValue = false;
        mPreferences.floatValue = 65.4321f;
        mPreferences.longValue = 9876543210L;

        mPreferences.saveAll();
        mPreferences.loadAll();

        assertEquals("abc", mPreferences.stringValue);
        assertEquals(4321, mPreferences.intValue);
        assertEquals(false, mPreferences.boolValue);
        assertEquals(65.4321f, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(9876543210L, mPreferences.longValue);
    }

    @Test(expected = NullPointerException.class)
    public void save_nullKeys() {
        String[] nullKeys = null;

        mPreferences.save(nullKeys);
    }

    @Test
    public void save_nullKey() {
        mPreferences.stringValue = "abc";
        mPreferences.intValue = 4321;
        mPreferences.boolValue = false;
        mPreferences.floatValue = 65.4321f;
        mPreferences.longValue = 9876543210L;

        String nullKey = null;

        mPreferences.save(nullKey);
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_emptyKeys() {
        mPreferences.stringValue = "abc";
        mPreferences.intValue = 4321;
        mPreferences.boolValue = false;
        mPreferences.floatValue = 65.4321f;
        mPreferences.longValue = 9876543210L;

        assertFalse(mPreferences.save());
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_stringValue() {
        mPreferences.stringValue = "abc";

        mPreferences.save(MockPreferences.KEY_STRING_VALUE);
        mPreferences.loadAll();

        assertEquals("abc", mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_intValue() {
        mPreferences.intValue = 4321;

        mPreferences.save(MockPreferences.KEY_INT_VALUE);
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(4321, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_booleanValue() {
        mPreferences.boolValue = false;

        mPreferences.save(MockPreferences.KEY_BOOLEAN_VALUE);
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(false, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_floatValue() {
        mPreferences.floatValue = 65.4321f;

        mPreferences.save(MockPreferences.KEY_FLOAT_VALUE);
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(65.4321f, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(MockPreferences.DEFAULT_LONG_VALUE, mPreferences.longValue);
    }

    @Test
    public void save_longValue() {
        mPreferences.longValue = 9876543210L;

        mPreferences.save(MockPreferences.KEY_LONG_VALUE);
        mPreferences.loadAll();

        assertEquals(MockPreferences.DEFAULT_STRING_VALUE, mPreferences.stringValue);
        assertEquals(MockPreferences.DEFAULT_INT_VALUE, mPreferences.intValue);
        assertEquals(MockPreferences.DEFAULT_BOOLEAN_VALUE, mPreferences.boolValue);
        assertEquals(MockPreferences.DEFAULT_FLOAT_VALUE, mPreferences.floatValue, MockPreferences.DELTA_FLOAT_VALUE);
        assertEquals(9876543210L, mPreferences.longValue);
    }

    private static class MockPreferences extends BasePreferences {
        public static final String KEY_STRING_VALUE = "STRING";
        public static final String KEY_INT_VALUE = "INT";
        public static final String KEY_BOOLEAN_VALUE = "BOOLEAN";
        public static final String KEY_FLOAT_VALUE = "FLOAT";
        public static final String KEY_LONG_VALUE = "LONG";

        public static final String DEFAULT_STRING_VALUE = "STRING";
        public static final int DEFAULT_INT_VALUE = 123;
        public static final boolean DEFAULT_BOOLEAN_VALUE = true;
        public static final float DEFAULT_FLOAT_VALUE = 12.345f;
        public static final long DEFAULT_LONG_VALUE = 1234567890L;

        public static final float DELTA_FLOAT_VALUE = 0.0001f;

        @PreferenceProperty(key = KEY_STRING_VALUE, stringValue = DEFAULT_STRING_VALUE)
        public String stringValue;
        @PreferenceProperty(key = KEY_INT_VALUE, intValue = DEFAULT_INT_VALUE)
        public int intValue;
        @PreferenceProperty(key = KEY_BOOLEAN_VALUE, boolValue = DEFAULT_BOOLEAN_VALUE)
        public boolean boolValue;
        @PreferenceProperty(key = KEY_FLOAT_VALUE, floatValue = DEFAULT_FLOAT_VALUE)
        public float floatValue;
        @PreferenceProperty(key = KEY_LONG_VALUE, longValue = DEFAULT_LONG_VALUE)
        public long longValue;

        protected MockPreferences(Context context) {
            super(context, "MockPreference");
        }
    }
}