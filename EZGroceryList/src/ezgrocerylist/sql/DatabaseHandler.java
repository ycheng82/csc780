package ezgrocerylist.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "EZgrocery";

    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_STEPS = "steps";

    private static final String KEY_LIST_NAME = "listName";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_QUANTITY = "itemQuantity";
    private static final String KEY_ITEM_UNIT = "itemUnit";
    private static final String KEY_ITEM_PRICE = "itemPrice";
    private static final String KEY_ITEM_NOTE = "itemNote";
    private static final String KEY_ITEM_CATEGORY = "itemCategory";
    private static final String KEY_ITEM_BARCODE = "itemBarcode";
    
    private static final String KEY_RECIPE_NAME = "recipeName";
    private static final String KEY_STEP_ID = "recipeId";
    private static final String KEY_STEP = "recipeStep";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	//create item table
        String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + KEY_LIST_NAME
                + " TEXT NOT NULL," + KEY_ITEM_NAME  + " TEXT NOT NULL," + KEY_ITEM_QUANTITY 
                + " INTEGER," + KEY_ITEM_UNIT + " TEXT," + KEY_ITEM_PRICE + " FLOAT," + 
                KEY_ITEM_NOTE + " TEXT," + KEY_ITEM_CATEGORY + " TEXT," + KEY_ITEM_BARCODE +
                " TEXT"+")";
        db.execSQL(CREATE_TAGS_TABLE);
        String CREATE_TAGS_TABLE_RECIPE = "CREATE TABLE " + TABLE_RECIPES + "(" + KEY_RECIPE_NAME
                + " TEXT NOT NULL," + KEY_ITEM_NAME  + " TEXT NOT NULL," + KEY_ITEM_QUANTITY 
                + " INTEGER," + KEY_ITEM_UNIT + " TEXT," + 
                KEY_ITEM_NOTE + " TEXT" +")";
        db.execSQL(CREATE_TAGS_TABLE_RECIPE);
        String CREATE_TAGS_TABLE_STEPS = "CREATE TABLE " + TABLE_STEPS + "("+
        		KEY_RECIPE_NAME + " TEXT NOT NULL,"+ KEY_STEP_ID
                + " INTEGER," + KEY_STEP  + " TEXT NOT NULL"+")";
        db.execSQL(CREATE_TAGS_TABLE_STEPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    /**
     * Add a new item to the database.
     */
    public void addItem(String listName, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIST_NAME, listName);
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
        values.put(KEY_ITEM_UNIT, item.getItemUnit());
        values.put(KEY_ITEM_PRICE, item.getItemPrice());
        values.put(KEY_ITEM_NOTE, item.getItemNote());
        values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
        values.put(KEY_ITEM_BARCODE, item.getItemBarcode());
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    /**
     * Searches the database for the items in a given pantry list.
     * 
     */
    public ArrayList<Item> getItems(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Item> items = new ArrayList<Item>();
        Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
        		KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE,  KEY_ITEM_CATEGORY, KEY_ITEM_BARCODE}, KEY_LIST_NAME + "=?", 
        		new String[] { listName }, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
	        while (cursor.isAfterLast() == false) {
	            //Calllog is a class with list of fileds 
	            Item item = new Item();
	            //item.setId(cursor.getLong(cursor.getColumnIndex(KEY_LIST_NAME)));
	            item.setItemName(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)));
	            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_QUANTITY)));
	            item.setItemUnit(cursor.getString(cursor.getColumnIndex(KEY_ITEM_UNIT)));
	            item.setItemPrice(cursor.getFloat(cursor.getColumnIndex(KEY_ITEM_PRICE)));
	            item.setItemCategory(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CATEGORY)));
	            item.setItemNote(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NOTE)));
	            item.setItemBarcode(cursor.getString(cursor.getColumnIndex(KEY_ITEM_BARCODE)));

	            items.add(item);
	            cursor.moveToNext();
	        }
        }
        cursor.close();
        db.close();
        return items;
    }
    
    /**
     * get the name of all the pantry lists in the database
     * @return arraylist of list names
     */
    public ArrayList<String> getAllLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> names = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME}, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
	        while (cursor.isAfterLast() == false) {
	            String name = cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME));
	            names.add(name);
	            cursor.moveToNext();
	        }
        }
        cursor.close();
        db.close();
        return names;
    }
    
    /**
     * change the list name in database (for all items included in that list)
     * @param listName
     * @return the number of rows affected 
     */
    public int changeListName(String oldListName, String newListName) {
    	SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        //itemNewValues.put(ITEM_TYPE,menuItem.getType());
        values.put(KEY_LIST_NAME,newListName);
        String[] args = new String[1];
        args[0]= oldListName;
        return db.update(TABLE_ITEMS, values,KEY_LIST_NAME+"=?", args);
    }
    
    /**
     * remove all items in the database with the given list name
     * @param listName
     * @return
     */
    public int removeList(String listName) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] args = new String[1];
    	args[0]= listName;
        return db.delete(TABLE_ITEMS, KEY_LIST_NAME+"=?", args);
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