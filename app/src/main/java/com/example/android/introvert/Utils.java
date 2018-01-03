package com.example.android.introvert;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import static com.example.android.introvert.IntrovertDbHelper.NOTE_TYPES_LAST_ID_COLUMN;
import static com.example.android.introvert.IntrovertDbHelper.NOTE_TYPES_NAME_COLUMN;
import static com.example.android.introvert.IntrovertDbHelper.NOTE_TYPES_TABLE_NAME;
import static com.example.android.introvert.IntrovertDbHelper.NOTE_TYPES_TYPE_COLUMN;

/**
 * Created by takeoff on 001 01 Jan 18.
 */

public class Utils {

    static private String TAG = "INTROWERT_UTILS:";

    public static void dumpTableExternal(SQLiteDatabase db, String tableName) {
        new DumpTable(db, tableName).execute();
    }

    private static void dumpTableInternal(SQLiteDatabase db, String tableName) {

        //Table Heading
        Log.i(TAG, "Starting " + tableName + " dump...");
        Log.d(TAG, "|=======================================================|");
        Log.d(TAG, "|TABLE NAME: " + tableName);
        Log.d(TAG, "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");


        //Columns info
        String pragmaCommand = "PRAGMA table_info(" + tableName + ")";
        Cursor cursorColumns = db.rawQuery(pragmaCommand, null);
        int nameIdx = cursorColumns.getColumnIndexOrThrow("name");
        int typeIdx = cursorColumns.getColumnIndexOrThrow("type");
        int notNullIdx = cursorColumns.getColumnIndexOrThrow("notnull");
        int dfltValueIdx = cursorColumns.getColumnIndexOrThrow("dflt_value");

        int columnsNumber = cursorColumns.getCount();
        Log.d(TAG, "|NUMBER OF COLUMNS: " + columnsNumber);

        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> columnTypes = new ArrayList<>();
        int i = 1;

        while (cursorColumns.moveToNext()) {
            Log.i(TAG, "|-------------------------------------------------------|");
            String name = cursorColumns.getString(nameIdx);
            String type = cursorColumns.getString(typeIdx);
            String notNull = cursorColumns.getString(notNullIdx);
            String dfltValue = cursorColumns.getString(dfltValueIdx);
            columnNames.add(name);
            columnTypes.add(type);

            Log.d(TAG, "|Column: " + i);
            Log.d(TAG, "|Name: " + name);
            Log.d(TAG, "|Type: " + type);
            Log.d(TAG, "|Not Null: " + notNull);
            Log.d(TAG, "|Default: " + dfltValue);

            i++;
        }

        cursorColumns.close();


        //Rows info
        Cursor cursorRows = db.query(tableName, null, null,
                null, null, null, null);

        int rowsNumber = cursorRows.getCount();
        Log.d(TAG, "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|");
        Log.d(TAG, "|NUMBER OF ROWS: " + rowsNumber);

        if (rowsNumber > 0) {

            //Going through each row
            while (cursorRows.moveToNext()) {
                Log.d(TAG, "|-------------------------------------------------------|");

                //Going through each column
                for (int b = 0; b < columnsNumber; b++) {
                    String columnName = columnNames.get(b);
                    String columnValue = null;
                    if (columnTypes.get(b).equals("INTEGER")) {
                        columnValue = Integer.toString(cursorRows.getInt(
                                cursorRows.getColumnIndex(columnName)));
                    } else if (columnTypes.get(b).equals("TEXT")) {
                        columnValue = cursorRows.getString(
                                cursorRows.getColumnIndex(columnName));
                    } else {
                        Log.e(TAG, "UNKNOWN TYPE OF ROW ENTRY!");
                    }

                    Log.i(TAG, "|" + columnName + ": " + columnValue);

                }
            }
        }
        Log.d(TAG, "|=======================================================|");

        cursorRows.close();
    }


    public static class DumpTable extends AsyncTask<String, Void, Boolean> {
        SQLiteDatabase db;
        String tableName;

        DumpTable(SQLiteDatabase db, String tableName) {
            this.db = db;
            this.tableName = tableName;
        }

        protected Boolean doInBackground(String... params) {
            dumpTableInternal(db, tableName);
            return true;
        }

        protected void onPostExecute() {
        }
    }


 /*   private static int getLastNoteId(SQLiteDatabase db, String noteType) {
        int lastNote = -1;
        // read from NOTE_TYPES table number of notes column

        Cursor cursorLastNoteId = db.query(NOTE_TYPES_TABLE_NAME,
                new String[]{NOTE_TYPES_LAST_ID_COLUMN},
                NOTE_TYPES_TYPE_COLUMN + "=?", new String[]{noteType},
                null, null, null);

        if (cursorLastNoteId.getCount() == 1) {
            cursorLastNoteId.moveToFirst();
            lastNote = cursorLastNoteId.getInt(cursorLastNoteId
                    .getColumnIndex(NOTE_TYPES_LAST_ID_COLUMN));
        } else {
            Log.e(TAG, "Wrong number of rows received when querying number of notes: "
                    + cursorLastNoteId.getCount() + "; Should be: 1");
        }
        cursorLastNoteId.close();

        // TODO: 003 03 Jan 18 prevent querying name column duplicates

        // TODO: 003 03 Jan 18 make querying async

        return lastNote;
    }*/


    public static String getNameForNewNote(SQLiteDatabase db, String noteType) {

        String noteTypeName = null;
        int lastNote = -1;
        // read from NOTE_TYPES table number of notes column

        Cursor cursor = db.query(NOTE_TYPES_TABLE_NAME,
                new String[]{NOTE_TYPES_NAME_COLUMN, NOTE_TYPES_LAST_ID_COLUMN},
                NOTE_TYPES_TYPE_COLUMN + "=?", new String[]{noteType},
                null, null, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            noteTypeName = cursor.getString(cursor
                    .getColumnIndex(NOTE_TYPES_NAME_COLUMN));
            lastNote = cursor.getInt(cursor
                    .getColumnIndex(NOTE_TYPES_LAST_ID_COLUMN));
        } else {
            Log.e(TAG, "Wrong number of rows received when querying name of note: "
                    + cursor.getCount() + "; Should be: 1");
        }
        cursor.close();

        // TODO: 003 03 Jan 18 prevent querying name column duplicates

        // TODO: 003 03 Jan 18 make querying async

        return noteTypeName + " " + (lastNote + 1);
    }
}
