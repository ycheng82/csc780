package ezgrocerylist.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "EZgrocery";

    private static final String TABLE_PANTRY = "pantryList";

    private static final String KEY_LIST_NAME = "listName";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_QUANTITY = "itemQuantity";
    private static final String KEY_ITEM_UNIT = "itemUnit";
    private static final String KEY_ITEM_PRICE = "itemPrice";
    private static final String KEY_ITEM_NOTE = "itemNote";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_PANTRY + "(" + KEY_LIST_NAME
                + " TEXT NOT NULL," + KEY_ITEM_NAME  + " TEXT NOT NULL," + KEY_ITEM_QUANTITY 
                + " INTEGER," + KEY_ITEM_UNIT + " TEXT," + KEY_ITEM_PRICE + " INTEGER," + 
                KEY_ITEM_NOTE + "TEXT" + ")";
        db.execSQL(CREATE_TAGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    /**
     * Add a new item to the database.
     */
    public void addItem(PantryList list, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIST_NAME, list.getListName());
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
        values.put(KEY_ITEM_UNIT, item.getItemUnit());
        values.put(KEY_ITEM_PRICE, item.getItemPrice());
        values.put(KEY_ITEM_NOTE, item.getItemNote());
        db.insert(TABLE_PANTRY, null, values);
        db.close();
    }

    /**
     * Searches the database for the item with a certain name.
     * 
     */
    public PantryList getListByName(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PANTRY, new String[] { KEY_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
        		KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE }, KEY_LIST_NAME + "=?", new String[] { listName }, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return null;
        }
        /**
         * Return the list. The indices of the cursor correspond to the
         * selection when querying the database.
         */
        PantryList groceryList = new PantryList(cursor.getString(0));
        cursor.close();
        db.close();
        return groceryList;
    }

    /**
     * Count the number of employees currently stored in the database via the
     * SQL statement:
     * 
     * <pre>
     * SELECT * FROM employee
     * </pre>
     */
    /*public int getEmployeeCount() {
        String countQuery = "SELECT * FROM " + TABLE_EMPLOYEE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }*/
}