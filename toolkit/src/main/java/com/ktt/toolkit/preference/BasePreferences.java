package com.ktt.toolkit.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author luke.kao
 */
public abstract class BasePreferences {
    private WeakReference<Context> mWeakContext;
    private final String NAME;

    protected BasePreferences(Context context, String name) {
        mWeakContext = new WeakReference<>(context);
        NAME = name;
    }

    public boolean loadAll() {
        SharedPreferences prefs = getPreferences();
        if (prefs == null) return false;

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(PreferenceProperty.class)) continue;

            PreferenceProperty property = field.getAnnotation(PreferenceProperty.class);

            getValue(prefs, field, property);
        }

        return true;
    }

    private void getValue(SharedPreferences prefs, Field field, PreferenceProperty property) {
        boolean isAccessible = field.isAccessible();
        if (!isAccessible) field.setAccessible(true);

        try {
            ClassType type = ClassType.getType(field.getType());

            switch (type) {
                case STRING: {
                    String value = prefs.getString(property.key(), property.stringValue());
                    field.set(this, value);
                }
                break;

                case INTEGER: {
                    int value = prefs.getInt(property.key(), property.intValue());
                    field.setInt(this, value);
                }
                break;

                case BOOLEAN: {
                    boolean value = prefs.getBoolean(property.key(), property.boolValue());
                    field.setBoolean(this, value);
                }
                break;

                case FLOAT: {
                    float value = prefs.getFloat(property.key(), property.floatValue());
                    field.setFloat(this, value);
                }
                break;

                case LONG: {
                    long value = prefs.getLong(property.key(), property.longValue());
                    field.setLong(this, value);
                }
                break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!isAccessible) field.setAccessible(false);
    }

    public boolean saveAll() {
        SharedPreferences prefs = getPreferences();
        if (prefs == null) return false;

        SharedPreferences.Editor editor = prefs.edit();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(PreferenceProperty.class)) continue;

            PreferenceProperty property = field.getAnnotation(PreferenceProperty.class);
            String key = property.key();

            putValue(editor, field, key);
        }

        editor.apply();

        return true;
    }

    public boolean save(String... keys) {
        HashSet<String> keySet = new HashSet<>(Arrays.asList(keys));
        if (keySet.isEmpty()) return false;

        SharedPreferences prefs = getPreferences();
        if (prefs == null) return false;

        SharedPreferences.Editor editor = prefs.edit();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(PreferenceProperty.class)) continue;

            PreferenceProperty property = field.getAnnotation(PreferenceProperty.class);
            String key = property.key();
            if (!keySet.contains(key)) continue;

            putValue(editor, field, key);
        }

        editor.apply();

        return true;
    }

    private void putValue(SharedPreferences.Editor editor, Field field, String key) {
        try {
            ClassType type = ClassType.getType(field.getType());

            switch (type) {
                case STRING:
                    editor.putString(key, (String) field.get(this));
                    break;

                case INTEGER:
                    editor.putInt(key, field.getInt(this));
                    break;

                case BOOLEAN:
                    editor.putBoolean(key, field.getBoolean(this));
                    break;

                case FLOAT:
                    editor.putFloat(key, field.getFloat(this));
                    break;

                case LONG:
                    editor.putLong(key, field.getLong(this));
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected SharedPreferences getPreferences() {
        Context context = getContext();

        SharedPreferences preferences = null;
        if (context != null) {
            preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }

        return preferences;
    }

    protected Context getContext() {
        return mWeakContext == null ? null : mWeakContext.get();
    }

    private enum ClassType {
        UNKNOWN,

        STRING,
        INTEGER,
        BOOLEAN,
        FLOAT,
        LONG,;

        public static ClassType getType(Class<?> type) {
            if (type == String.class) {
                return STRING;
            } else if (type == Integer.class || type == int.class) {
                return INTEGER;
            } else if (type == Boolean.class || type == boolean.class) {
                return BOOLEAN;
            } else if (type == Float.class || type == float.class) {
                return FLOAT;
            } else if (type == Long.class || type == long.class) {
                return LONG;
            } else {
                return UNKNOWN;
            }
        }
    }
}