package ezgrocerylist.sql;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 15;

	private static final String DATABASE_NAME = "EZgrocery";

	private static final String TABLE_ITEMS = "items";
	private static final String TABLE_RECIPES = "recipes";
	private static final String TABLE_IMAGES = "images";
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
	private static final String KEY_RECIPE_COVER = "recipeCover";
	private static final String KEY_RECIPE_IMAGE = "recipeImage";
	private static final String KEY_STEP_ID = "recipeId";
	private static final String KEY_STEP_DETAIL = "recipeStep";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create item table (store pantry item)
		String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
				+ KEY_LIST_NAME + " TEXT NOT NULL," + KEY_ITEM_NAME
				+ " TEXT NOT NULL," + KEY_ITEM_QUANTITY + " INTEGER,"
				+ KEY_ITEM_UNIT + " TEXT," + KEY_ITEM_PRICE + " FLOAT,"
				+ KEY_ITEM_NOTE + " TEXT," + KEY_ITEM_CATEGORY + " TEXT,"
				+ KEY_ITEM_BARCODE + " TEXT" + ")";
		db.execSQL(CREATE_TAGS_TABLE);
		// create recipe table (store recipe ingredients)
		String CREATE_TAGS_TABLE_RECIPE = "CREATE TABLE " + TABLE_RECIPES + "("
				+ KEY_RECIPE_NAME + " TEXT NOT NULL," + KEY_ITEM_NAME
				+ " TEXT," + KEY_ITEM_QUANTITY + " INTEGER,"
				+ KEY_ITEM_UNIT + " TEXT," + KEY_ITEM_NOTE + " TEXT"+")";
		db.execSQL(CREATE_TAGS_TABLE_RECIPE);
		// create recipe step table (store recipe steps)
		String CREATE_TAGS_TABLE_STEPS = "CREATE TABLE " + TABLE_STEPS + "("
				+ KEY_RECIPE_NAME + " TEXT NOT NULL," + KEY_STEP_ID
				+ " INTEGER," + KEY_STEP_DETAIL + " TEXT NOT NULL" + ")";
		db.execSQL(CREATE_TAGS_TABLE_STEPS);
		// create recipe image table (store recipe images)
		String CREATE_TAGS_TABLE_IMAGES = "CREATE TABLE " + TABLE_IMAGES + "("
				+ KEY_RECIPE_NAME + " TEXT NOT NULL," + KEY_RECIPE_COVER + " INTEGER,"  
				+ KEY_RECIPE_IMAGE + " TEXT"+ ")";
		db.execSQL(CREATE_TAGS_TABLE_IMAGES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
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
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT,
				KEY_ITEM_PRICE, KEY_ITEM_NOTE, KEY_ITEM_CATEGORY,
				KEY_ITEM_BARCODE }, KEY_LIST_NAME + "=?",
				new String[] { listName }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				// Calllog is a class with list of fileds
				Item item = new Item();
				// item.setId(cursor.getLong(cursor.getColumnIndex(KEY_LIST_NAME)));
				item.setItemName(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NAME)));
				item.setItemQuantity(cursor.getInt(cursor
						.getColumnIndex(KEY_ITEM_QUANTITY)));
				item.setItemUnit(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_UNIT)));
				item.setItemPrice(cursor.getFloat(cursor
						.getColumnIndex(KEY_ITEM_PRICE)));
				item.setItemCategory(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_CATEGORY)));
				item.setItemNote(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NOTE)));
				item.setItemBarcode(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_BARCODE)));

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
	 * 
	 * @return arraylist of list names
	 */
	public ArrayList<String> getAllLists() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> names = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME },
				null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				String name = cursor.getString(cursor
						.getColumnIndex(KEY_LIST_NAME));
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
	 * 
	 * @param listName
	 * @return the number of rows affected
	 */
	public int changeListName(String oldListName, String newListName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		// itemNewValues.put(ITEM_TYPE,menuItem.getType());
		values.put(KEY_LIST_NAME, newListName);
		String[] args = new String[1];
		args[0] = oldListName;
		return db.update(TABLE_ITEMS, values, KEY_LIST_NAME + "=?", args);
	}

	/**
	 * remove all items in the database with the given list name
	 * 
	 * @param listName
	 * @return
	 */
	public int removeList(String listName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args = new String[1];
		args[0] = listName;
		return db.delete(TABLE_ITEMS, KEY_LIST_NAME + "=?", args);
	}

	/**
	 * Add a new recipe ingredient to the database.
	 * @return 
	 */
	public int addIngredient(String recipeName, Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY ,KEY_ITEM_UNIT,KEY_ITEM_NOTE}, 
				KEY_RECIPE_NAME + "=?"+ " AND " + KEY_ITEM_NAME + "=?",
				new String[] { recipeName ,item.getItemName()}, null, null, null, null);
		if (cursor.moveToFirst()) {
			// record exists

			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_RECIPES, values, KEY_RECIPE_NAME + "=?"+ " AND " + KEY_ITEM_NAME + "=?",
					new String[] { recipeName ,item.getItemName()});
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			db.insert(TABLE_RECIPES, null, values);
			db.close();
			return 1;
		}
	}

	/**
	 * Add recipe steps to the database.
	 */
	public void addSteps(String recipeName, ArrayList<RecipeStep> step) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0; i < step.size(); i++) {
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_STEP_ID, step.get(i).getStepId());
			values.put(KEY_STEP_DETAIL, step.get(i).getStepDetail());
		}
		db.insert(TABLE_STEPS, null, values);
		db.close();
	}

	/**
	 * get the names of all recipes
	 * 
	 * @return recipe names as an arraylist of string
	 */
	public ArrayList<String> getRecipeList() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> names = new ArrayList<String>();
		//check recipe item table
		Cursor cursor = db.query(TABLE_RECIPES,
				new String[] { KEY_RECIPE_NAME }, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				String name = cursor.getString(cursor
						.getColumnIndex(KEY_RECIPE_NAME));
				if (!names.contains(name)) {
					names.add(name);
				}
				cursor.moveToNext();
			}
		}
		//check recipe image table
		cursor = db.query(TABLE_IMAGES,
				new String[] { KEY_RECIPE_NAME }, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				String name = cursor.getString(cursor
						.getColumnIndex(KEY_RECIPE_NAME));
				if (!names.contains(name)) {
					names.add(name);
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return names;
	}

	/**
	 * Add recipe cover image to the database.Check if a recipe exists, if yes,
	 * update cover else, add recipe and cover to database
	 */
	public int addCover(String recipeName, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String Query = "SELECT * FROM " + TABLE_RECIPES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName+"'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists

			values.put(KEY_RECIPE_COVER, imageUri);
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_RECIPES, values, KEY_RECIPE_NAME + "=?",
					args);
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_RECIPE_COVER, imageUri);
			db.insert(TABLE_RECIPES, null, values);
			db.close();
			return 1;

		}
	}
	
	/**
	 * get recipes from database
	 * @param recipeName
	 * @return true if recipe exists, false if not
	 */
	public boolean getRecipe(String recipeName){
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_RECIPES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName+"'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			return true;
		} else {
			return false;
		}
	}
	
	public Uri getCover(String recipeName){
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_RECIPES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName+"'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			return Uri.parse(cursor.getString(cursor
					.getColumnIndex(KEY_RECIPE_COVER)));
		} else {
			return null;
		}
	}
	
	/**
	 * Add recipe cover image to the database.Check if a recipe exists, if yes,
	 * update cover else, add recipe and cover to database
	 * @param isCover 0:not cover image, 1: is cover image
	 * @param recipeName
	 * @param imageUri
	 * @return
	 */
	public int addImage(int isCover, String recipeName, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String Query = "SELECT * FROM " + TABLE_IMAGES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName+"'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_RECIPE_COVER, isCover);
			values.put(KEY_RECIPE_IMAGE, imageUri);
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_IMAGES, values, KEY_RECIPE_NAME + "=?",
					args);
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_RECIPE_COVER, isCover);
			values.put(KEY_RECIPE_IMAGE, imageUri);
			db.insert(TABLE_IMAGES, null, values);
			db.close();
			return 1;

		}
	}
	public Uri getCoverImage(String recipeName){
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_IMAGES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName+"'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			if(cursor.getInt(cursor.getColumnIndex(KEY_RECIPE_COVER))==1){
				// record exists
				return Uri.parse(cursor.getString(cursor
						.getColumnIndex(KEY_RECIPE_IMAGE)));
			}
			
		} 
		else {
			return null;
		}
		return null;
	}
	
	/**
	 * Searches the database for the items in a given pantry list.
	 * 
	 */
	public ArrayList<Item> getIngredients(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY ,KEY_ITEM_UNIT,KEY_ITEM_NOTE}, 
				KEY_RECIPE_NAME + "=?",
				new String[] { recipeName }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				// Calllog is a class with list of fileds
				Item item = new Item();
				// item.setId(cursor.getLong(cursor.getColumnIndex(KEY_LIST_NAME)));
				item.setItemName(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NAME)));
				item.setItemQuantity(cursor.getInt(cursor
						.getColumnIndex(KEY_ITEM_QUANTITY)));
				item.setItemUnit(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_UNIT)));
				item.setItemNote(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NOTE)));

				items.add(item);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return items;
	}
	
	/**
	 * get an ingredient from database
	 * @param recipeName
	 * @param itemName
	 * @return item
	 */
	public Item getIngredient(String recipeName,String itemName){
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = new Item();
		Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY ,KEY_ITEM_UNIT,KEY_ITEM_NOTE}, 
				KEY_RECIPE_NAME + "=?"+ " AND " + KEY_ITEM_NAME + "=?",
				new String[] { recipeName ,itemName}, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
				item.setItemName(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NAME)));
				item.setItemQuantity(cursor.getInt(cursor
						.getColumnIndex(KEY_ITEM_QUANTITY)));
				item.setItemUnit(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_UNIT)));
				item.setItemNote(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_NOTE)));
		}
		cursor.close();
		db.close();
		return item;
	}
	public boolean deleteIngredient(String recipeName,String itemName){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_RECIPES, KEY_RECIPE_NAME + "=?"+ " AND " + KEY_ITEM_NAME + "=?",
				new String[] { recipeName ,itemName}) > 0;
	}
}