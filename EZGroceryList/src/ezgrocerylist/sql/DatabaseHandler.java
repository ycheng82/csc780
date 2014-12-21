package ezgrocerylist.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.Editable;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 26;

	private static final String DATABASE_NAME = "EZgrocery";

	private static final String TABLE_ITEMS = "items";
	private static final String TABLE_RECIPES = "recipes";
	private static final String TABLE_IMAGES = "images";
	private static final String TABLE_STEPS = "steps";
	private static final String TABLE_SHOPPING = "shopping";
	private static final String TABLE_RECIPE_DES = "recipe_description";
	private static final String TABLE_UNIT = "item_unit";

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
	private static final String KEY_STEP_ID = "stepId";
	private static final String KEY_STEP_DETAIL = "stepDetail";
	private static final String KEY_RECIPE_DES = "recipeDescription";

	private static final String KEY_SHOPPING_LIST_NAME = "shoppingListName";

	private static final String KEY_UNIT_ID = "unitId";
	private static final String KEY_UNIT_NAME = "unitName";

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
				+ " TEXT," + KEY_ITEM_QUANTITY + " INTEGER," + KEY_ITEM_UNIT
				+ " TEXT," + KEY_ITEM_NOTE + " TEXT," + KEY_ITEM_CATEGORY
				+ " TEXT" + ")";
		db.execSQL(CREATE_TAGS_TABLE_RECIPE);
		// create recipe step table (store recipe steps)
		String CREATE_TAGS_TABLE_STEPS = "CREATE TABLE " + TABLE_STEPS + "("
				+ KEY_RECIPE_NAME + " TEXT NOT NULL," + KEY_STEP_ID
				+ " INTEGER," + KEY_STEP_DETAIL + " TEXT NOT NULL" + ")";
		db.execSQL(CREATE_TAGS_TABLE_STEPS);
		// create recipe image table (store recipe images)
		String CREATE_TAGS_TABLE_IMAGES = "CREATE TABLE " + TABLE_IMAGES + "("
				+ KEY_RECIPE_NAME + " TEXT NOT NULL," + KEY_RECIPE_COVER
				+ " INTEGER," + KEY_RECIPE_IMAGE + " TEXT" + ")";
		db.execSQL(CREATE_TAGS_TABLE_IMAGES);
		String CREATE_TAGS_TABLE_SHOPPING = "CREATE TABLE " + TABLE_SHOPPING
				+ "(" + KEY_SHOPPING_LIST_NAME + " TEXT NOT NULL,"
				+ KEY_ITEM_NAME + " TEXT NOT NULL," + KEY_ITEM_QUANTITY
				+ " INTEGER," + KEY_ITEM_UNIT + " TEXT," + KEY_ITEM_PRICE
				+ " FLOAT," + KEY_ITEM_NOTE + " TEXT," + KEY_ITEM_CATEGORY
				+ " TEXT," + KEY_ITEM_BARCODE + " TEXT" + ")";
		db.execSQL(CREATE_TAGS_TABLE_SHOPPING);
		String CREATE_TAGS_TABLE_RECIPE_DES = "CREATE TABLE "
				+ TABLE_RECIPE_DES + "(" + KEY_RECIPE_NAME + " TEXT NOT NULL,"
				+ KEY_RECIPE_DES + " TEXT" + ")";
		db.execSQL(CREATE_TAGS_TABLE_RECIPE_DES);
		String CREATE_CATEGORIES_TABLE_UNIT = "CREATE TABLE " + TABLE_UNIT
				+ "(" + KEY_UNIT_ID + " INTEGER PRIMARY KEY," + KEY_UNIT_NAME
				+ " TEXT)";
		db.execSQL(CREATE_CATEGORIES_TABLE_UNIT);
	}

	public void addDefaultUnit() {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		String[] unit = { "ea", "lb", "kg", "g", "box", "bag", "new" };

		Cursor cursor = db.query(TABLE_UNIT, new String[] {KEY_UNIT_ID,KEY_UNIT_NAME}, 
				KEY_UNIT_NAME + "=?" ,
				new String[] { "ea"}, null, null,
				null, null);
		if (!cursor.moveToFirst()) {
			for (int i = 0; i < unit.length; i++) {
				values.put(KEY_UNIT_ID, i + 1);
				values.put(KEY_UNIT_NAME, unit[i]);
				db.insert(TABLE_UNIT, null, values);

			}
			db.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_DES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIT);
		onCreate(db);
	}

	/**
	 * Add a new item to the database.
	 */
	public int addItem(String listName, Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT,
				KEY_ITEM_PRICE, KEY_ITEM_NOTE, KEY_ITEM_CATEGORY,
				KEY_ITEM_BARCODE }, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?",
				new String[] { listName, item.getItemName() }, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_LIST_NAME, listName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_PRICE, item.getItemPrice());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
			values.put(KEY_ITEM_BARCODE, item.getItemBarcode());
			String[] args = new String[1];
			args[0] = listName;
			return db.update(TABLE_ITEMS, values, KEY_LIST_NAME + "=?"
					+ " AND " + KEY_ITEM_NAME + "=?", new String[] { listName,
					item.getItemName() });
		} else {
			// record not found
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
			return 1;
		}
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
	 * 
	 * @return
	 */
	public int addIngredient(String recipeName, Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY }, KEY_RECIPE_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?",
				new String[] { recipeName, item.getItemName() }, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			// record exists

			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_RECIPES, values, KEY_RECIPE_NAME + "=?"
					+ " AND " + KEY_ITEM_NAME + "=?", new String[] {
					recipeName, item.getItemName() });
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
			db.insert(TABLE_RECIPES, null, values);
			db.close();
			return 1;
		}
	}

	/**
	 * Add recipe step to the database.
	 * 
	 * @return
	 */
	public int addStep(String recipeName, Step step) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.query(TABLE_STEPS, new String[] { KEY_RECIPE_NAME,
				KEY_STEP_ID, KEY_STEP_DETAIL }, KEY_RECIPE_NAME + "=?"
				+ " AND " + KEY_STEP_ID + "=?", new String[] { recipeName,
				Integer.toString(step.getStepId()) }, null, null, null, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_STEP_ID, step.getStepId());
			values.put(KEY_STEP_DETAIL, step.getStepDetail());
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_STEPS, values, KEY_RECIPE_NAME + "=?"
					+ " AND " + KEY_STEP_ID + "=?", new String[] { recipeName,
					Integer.toString(step.getStepId()) });
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_STEP_ID, step.getStepId());
			values.put(KEY_STEP_DETAIL, step.getStepDetail());
			db.insert(TABLE_STEPS, null, values);
			db.close();
			return 1;
		}

	}

	/**
	 * get the names of all recipes
	 * 
	 * @return recipe names as an arraylist of string
	 */
	public ArrayList<String> getRecipeList() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> names = new ArrayList<String>();
		// check recipe item table
		Cursor cursor = db.query(TABLE_RECIPE_DES,
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
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
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
	 * 
	 * @param recipeName
	 * @return true if recipe exists, false if not
	 */
	public boolean getRecipe(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_RECIPE_DES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			return true;
		} else {
			return false;
		}
	}

	public Uri getCover(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_RECIPES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
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
	 * 
	 * @param isCover
	 *            0:not cover image, 1: is cover image
	 * @param recipeName
	 * @param imageUri
	 * @return
	 */
	public int addImage(int isCover, String recipeName, String imageUri) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String Query = "SELECT * FROM " + TABLE_IMAGES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_RECIPE_COVER, isCover);
			values.put(KEY_RECIPE_IMAGE, imageUri);
			String[] args = new String[1];
			args[0] = recipeName;
			return db
					.update(TABLE_IMAGES, values, KEY_RECIPE_NAME + "=?", args);
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

	public Uri getCoverImage(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT * FROM " + TABLE_IMAGES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			if (cursor.getInt(cursor.getColumnIndex(KEY_RECIPE_COVER)) == 1) {
				// record exists
				return Uri.parse(cursor.getString(cursor
						.getColumnIndex(KEY_RECIPE_IMAGE)));
			}

		} else {
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
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY }, KEY_RECIPE_NAME + "=?",
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
				item.setItemCategory(cursor.getString(cursor
						.getColumnIndex(KEY_ITEM_CATEGORY)));

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
	 * 
	 * @param recipeName
	 * @param itemName
	 * @return item
	 */
	public Item getIngredient(String recipeName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = new Item();
		Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY }, KEY_RECIPE_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?", new String[] { recipeName, itemName },
				null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			item.setItemName(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NAME)));
			item.setItemQuantity(cursor.getInt(cursor
					.getColumnIndex(KEY_ITEM_QUANTITY)));
			item.setItemUnit(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_UNIT)));
			item.setItemNote(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NOTE)));
			item.setItemCategory(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_CATEGORY)));
		}
		cursor.close();
		db.close();
		return item;
	}

	/**
	 * delete an ingredient from recipe database
	 * 
	 * @param recipeName
	 * @param itemName
	 * @return true if deleted, false if not deleted
	 */
	public boolean deleteIngredient(String recipeName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_RECIPES, KEY_RECIPE_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?", new String[] { recipeName, itemName }) > 0;
	}

	/**
	 * Searches the database for the recipe steps in a given recipe.
	 * 
	 */
	public ArrayList<Step> getSteps(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Step> steps = new ArrayList<Step>();
		Cursor cursor = db.query(TABLE_STEPS, new String[] { KEY_RECIPE_NAME,
				KEY_STEP_ID, KEY_STEP_DETAIL }, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				// Calllog is a class with list of fileds
				Step step = new Step();
				// item.setId(cursor.getLong(cursor.getColumnIndex(KEY_LIST_NAME)));
				step.setStepId(cursor.getInt(cursor.getColumnIndex(KEY_STEP_ID)));
				step.setStepDetail(cursor.getString(cursor
						.getColumnIndex(KEY_STEP_DETAIL)));
				steps.add(step);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return steps;
	}

	/**
	 * get a recipe step from database
	 * 
	 * @param recipeName
	 * @param stepId
	 * @return item
	 */
	public Step getStep(String recipeName, int stepId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Step step = new Step();
		Cursor cursor = db.query(TABLE_STEPS, new String[] { KEY_RECIPE_NAME,
				KEY_STEP_ID, KEY_STEP_DETAIL }, KEY_RECIPE_NAME + "=?"
				+ " AND " + KEY_STEP_ID + "=?", new String[] { recipeName,
				Integer.toString(stepId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			step.setStepId(cursor.getInt(cursor.getColumnIndex(KEY_STEP_ID)));
			step.setStepDetail(cursor.getString(cursor
					.getColumnIndex(KEY_STEP_DETAIL)));
		}
		cursor.close();
		db.close();
		return step;
	}

	/**
	 * delete a recipe step from recipe database
	 * 
	 * @param recipeName
	 * @param stepId
	 * @return true if deleted, false if not deleted
	 */
	public boolean deleteStep(String recipeName, int stepId) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_STEPS, KEY_RECIPE_NAME + "=?" + " AND "
				+ KEY_STEP_ID + "=?",
				new String[] { recipeName, Integer.toString(stepId) }) > 0;
	}

	/**
	 * get category in a pantry list
	 * 
	 * @param listName
	 * @return
	 */
	public String[] getListCategory(String listName) {
		// retrieve items from database
		ArrayList<Item> items = new ArrayList<Item>();
		items = getItems(listName);

		// split items into category
		ArrayList<String> cats = new ArrayList<String>();
		HashSet<String> uCats = null;
		if (items.size() != 0) {
			for (int i = 0; i < items.size(); i++) {
				cats.add(items.get(i).getItemCategory());
			}
			uCats = new HashSet<>(cats);
			// Log.d("showList","#category: " + uCats.size());
		}
		return uCats.toArray(new String[uCats.size()]);
	}

	/**
	 * get the contents in a pantry list
	 * 
	 * @param listName
	 * @return
	 */
	public String[][] getListContents(String listName) {
		String[] uCats = getListCategory(listName);
		String[][] listContents = new String[uCats.length][1];
		// retrieve items from database
		int k = 0;
		String listText = "";
		ArrayList<Item> items = new ArrayList<Item>();
		items = getItems(listName);

		// split items into category
		ArrayList<String> cats = new ArrayList<String>();
		if (items.size() != 0) {
			for (int i = 0; i < items.size(); i++) {
				cats.add(items.get(i).getItemCategory());
			}
		}

		// add view for these items if they are not null
		if (uCats != null) {

			for (String value : uCats) {
				listText = "";
				for (int j = 0; j < items.size(); j++) {

					if (items.get(j).getItemCategory().equals(value)) {
						// Log.d("category 1 ",items.get(j).getItemCategory());
						listText += items.get(j).getItemName() + "		"
								+ items.get(j).getItemQuantity() + "		"
								+ items.get(j).getItemUnit() + "\n";
					}
				}
				listContents[k][0] = listText;
				k++;
			}
		}
		// Log.d("list contents: ", listText);

		return listContents;
	}

	/**
	 * get an item from database
	 * 
	 * @param recipeName
	 * @param itemName
	 * @return item
	 */
	public Item getItem(String listName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = new Item();
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT,
				KEY_ITEM_PRICE, KEY_ITEM_NOTE, KEY_ITEM_CATEGORY,
				KEY_ITEM_BARCODE }, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?", new String[] { listName, itemName },
				null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			item.setItemName(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NAME)));
			item.setItemQuantity(cursor.getInt(cursor
					.getColumnIndex(KEY_ITEM_QUANTITY)));
			item.setItemUnit(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_UNIT)));
			item.setItemNote(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NOTE)));
			item.setItemPrice(cursor.getFloat(cursor
					.getColumnIndex(KEY_ITEM_PRICE)));
			item.setItemCategory(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_CATEGORY)));
			item.setItemBarcode(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_BARCODE)));
		}
		cursor.close();
		db.close();
		return item;
	}

	/**
	 * delete an item from pantry database
	 * 
	 * @param listName
	 * @param itemName
	 * @return true if deleted, false if not deleted
	 */
	public boolean deleteItem(String listName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_ITEMS, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_NAME + "=?", new String[] { listName, itemName }) > 0;
	}

	/**
	 * delete a recipe from recipe database
	 * 
	 * @param recipeName
	 * @return true if deleted, false if not deleted
	 */
	public boolean deleteRecipe(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		int n1 = db.delete(TABLE_STEPS, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName });
		int n2 = db.delete(TABLE_IMAGES, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName });
		int n3 = db.delete(TABLE_RECIPES, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName });
		int n4 = db.delete(TABLE_RECIPE_DES, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName });
		return (n1 + n2 + n4) > 0;
	}

	public ArrayList<Item> getCategoryContents(String listName,
			String categoryName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT,
				KEY_ITEM_PRICE, KEY_ITEM_NOTE, KEY_ITEM_CATEGORY,
				KEY_ITEM_BARCODE }, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_CATEGORY + "=?", new String[] { listName,
				categoryName }, null, null, null, null);
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

	public boolean addShoppingItems(String shoppingListName,
			ArrayList<Item> items) {
		int k = 0;
		for (int i = 0; i < items.size(); i++) {
			addShoppingItem(shoppingListName, items.get(i));
			k++;
		}
		if (k == items.size()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Add a new item to the database.
	 */
	public int addShoppingItem(String listName, Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.query(TABLE_SHOPPING, new String[] {
				KEY_SHOPPING_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
				KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY, KEY_ITEM_BARCODE }, KEY_SHOPPING_LIST_NAME
				+ "=?" + " AND " + KEY_ITEM_NAME + "=?", new String[] {
				listName, item.getItemName() }, null, null, null, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_SHOPPING_LIST_NAME, listName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_PRICE, item.getItemPrice());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
			values.put(KEY_ITEM_BARCODE, item.getItemBarcode());
			String[] args = new String[1];
			args[0] = listName;
			return db.update(TABLE_SHOPPING, values, KEY_SHOPPING_LIST_NAME
					+ "=?" + " AND " + KEY_ITEM_NAME + "=?", new String[] {
					listName, item.getItemName() });
		} else {
			// record not found
			values.put(KEY_SHOPPING_LIST_NAME, listName);
			values.put(KEY_ITEM_NAME, item.getItemName());
			values.put(KEY_ITEM_QUANTITY, item.getItemQuantity());
			values.put(KEY_ITEM_UNIT, item.getItemUnit());
			values.put(KEY_ITEM_PRICE, item.getItemPrice());
			values.put(KEY_ITEM_NOTE, item.getItemNote());
			values.put(KEY_ITEM_CATEGORY, item.getItemCategory());
			values.put(KEY_ITEM_BARCODE, item.getItemBarcode());
			db.insert(TABLE_SHOPPING, null, values);
			db.close();
			return 1;
		}
	}

	/**
	 * get all the shopping lists in the database
	 * 
	 * @return arraylist of names of shopping list
	 */
	public ArrayList<String> getShoppingLists() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> names = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_SHOPPING,
				new String[] { KEY_SHOPPING_LIST_NAME }, null, null, null,
				null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				String name = cursor.getString(cursor
						.getColumnIndex(KEY_SHOPPING_LIST_NAME));
				names.add(name);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return names;
	}

	/**
	 * get category for a shopping list
	 * 
	 * @param listName
	 * @return array of string of category name
	 */
	public String[] getShoppingListCategory(String listName) {
		// retrieve items from database
		ArrayList<Item> items = new ArrayList<Item>();
		items = getShoppingItems(listName);

		// split items into category
		ArrayList<String> cats = new ArrayList<String>();
		HashSet<String> uCats = null;
		if (items.size() != 0) {
			for (int i = 0; i < items.size(); i++) {
				cats.add(items.get(i).getItemCategory());
			}
			uCats = new HashSet<>(cats);
			// Log.d("showList","#category: " + uCats.size());
		}
		return uCats.toArray(new String[uCats.size()]);
	}

	/**
	 * Get items in a given shopping list
	 * 
	 * @param listName
	 * @return arraylist of items
	 */
	public ArrayList<Item> getShoppingItems(String listName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(TABLE_SHOPPING, new String[] {
				KEY_SHOPPING_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
				KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY, KEY_ITEM_BARCODE }, KEY_SHOPPING_LIST_NAME
				+ "=?", new String[] { listName }, null, null, null, null);
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

	public List<Item> getShoppingCategoryContents(String listName,
			String categoryName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(TABLE_SHOPPING, new String[] {
				KEY_SHOPPING_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
				KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY, KEY_ITEM_BARCODE }, KEY_SHOPPING_LIST_NAME
				+ "=?" + " AND " + KEY_ITEM_CATEGORY + "=?", new String[] {
				listName, categoryName }, null, null, null, null);
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
	 * get an item from shopping table
	 * 
	 * @param listName
	 *            shopping list name
	 * @param itemName
	 *            item name
	 * @return item
	 */
	public Item getShoppingItem(String listName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = new Item();
		Cursor cursor = db.query(TABLE_SHOPPING, new String[] {
				KEY_SHOPPING_LIST_NAME, KEY_ITEM_NAME, KEY_ITEM_QUANTITY,
				KEY_ITEM_UNIT, KEY_ITEM_PRICE, KEY_ITEM_NOTE,
				KEY_ITEM_CATEGORY, KEY_ITEM_BARCODE }, KEY_SHOPPING_LIST_NAME
				+ "=?" + " AND " + KEY_ITEM_NAME + "=?", new String[] {
				listName, itemName }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			item.setItemName(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NAME)));
			item.setItemQuantity(cursor.getInt(cursor
					.getColumnIndex(KEY_ITEM_QUANTITY)));
			item.setItemUnit(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_UNIT)));
			item.setItemNote(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_NOTE)));
			item.setItemPrice(cursor.getFloat(cursor
					.getColumnIndex(KEY_ITEM_PRICE)));
			item.setItemCategory(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_CATEGORY)));
			item.setItemBarcode(cursor.getString(cursor
					.getColumnIndex(KEY_ITEM_BARCODE)));
		}
		cursor.close();
		db.close();
		return item;
	}

	/**
	 * delete an item from shopping table
	 * 
	 * @param listName
	 * @param itemName
	 * @return true if item is deleted; false if item is not deleted
	 */
	public boolean deleteShoppingItem(String listName, String itemName) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_SHOPPING, KEY_SHOPPING_LIST_NAME + "=?"
				+ " AND " + KEY_ITEM_NAME + "=?", new String[] { listName,
				itemName }) > 0;

	}

	/**
	 * remove a given shopping list from shopping table
	 * 
	 * @param listName
	 * @return number of items deleted in this list
	 */
	public int removeShoppingList(String listName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args = new String[1];
		args[0] = listName;
		return db.delete(TABLE_SHOPPING, KEY_SHOPPING_LIST_NAME + "=?", args);
	}

	/**
	 * change the name of a given shopping list
	 * 
	 * @param listName
	 * @param newListName
	 * @return number of records changed in this list
	 */
	public int changeShoppingListName(String oldListName, String newListName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		// itemNewValues.put(ITEM_TYPE,menuItem.getType());
		values.put(KEY_SHOPPING_LIST_NAME, newListName);
		String[] args = new String[1];
		args[0] = oldListName;
		return db.update(TABLE_SHOPPING, values, KEY_SHOPPING_LIST_NAME + "=?",
				args);
	}

	/**
	 * add items from a category in a pantry list to a shopping list
	 * 
	 * @param pantryListName
	 * @param shoppingListName
	 * @param catName
	 * @return true if added, false if not added
	 */
	public boolean addCatShopping(String pantryListName,
			String shoppingListName, String catName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME,
				KEY_ITEM_NAME, KEY_ITEM_QUANTITY, KEY_ITEM_UNIT,
				KEY_ITEM_PRICE, KEY_ITEM_NOTE, KEY_ITEM_CATEGORY,
				KEY_ITEM_BARCODE }, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_CATEGORY + "=?", new String[] { pantryListName,
				catName }, null, null, null, null);
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
		return addShoppingItems(shoppingListName, items);
	}

	/**
	 * delete items from a category in a pantry list
	 * 
	 * @param pantryListName
	 * @param groupName
	 * @return
	 */
	public int delteItems(String pantryListName, String groupName) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_ITEMS, KEY_LIST_NAME + "=?" + " AND "
				+ KEY_ITEM_CATEGORY + "=?", new String[] { pantryListName,
				groupName });
	}

	/**
	 * delete items from a category in a shopping list
	 * 
	 * @param shoppingListName
	 * @param groupName
	 * @return
	 */
	public int delteShoppingItems(String shoppingListName, String groupName) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.delete(TABLE_SHOPPING, KEY_SHOPPING_LIST_NAME + "=?"
				+ " AND " + KEY_ITEM_CATEGORY + "=?", new String[] {
				shoppingListName, groupName });
	}

	/**
	 * get recipe description for a give recipe
	 * 
	 * @param recipeName
	 * @return
	 */
	public String getRecipeDes(String recipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String recipeDes = null;
		Cursor cursor = db.query(TABLE_RECIPE_DES, new String[] {
				KEY_RECIPE_NAME, KEY_RECIPE_DES }, KEY_RECIPE_NAME + "=?",
				new String[] { recipeName }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			recipeDes = cursor.getString(cursor.getColumnIndex(KEY_RECIPE_DES));
		}
		cursor.close();
		db.close();
		return recipeDes;
	}

	/**
	 * add recipe to the recipe description table, update the table if the given
	 * recipe name is already in the table
	 * 
	 * @param listName
	 * @return
	 */
	public int addRecipe(String recipeName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String Query = "SELECT * FROM " + TABLE_RECIPE_DES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.putNull(KEY_RECIPE_DES);
			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_RECIPE_DES, values, KEY_RECIPE_NAME + "=?",
					args);
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.putNull(KEY_RECIPE_DES);
			db.insert(TABLE_RECIPE_DES, null, values);
			db.close();
			return 1;

		}

	}

	/**
	 * add description to a recipe
	 * 
	 * @param recipeName
	 * @param text
	 */
	public int addRecipeDes(String recipeName, String recipeDes) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String Query = "SELECT * FROM " + TABLE_RECIPE_DES + " WHERE "
				+ KEY_RECIPE_NAME + " = '" + recipeName + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.moveToFirst()) {
			// record exists
			values.put(KEY_RECIPE_DES, recipeDes);

			String[] args = new String[1];
			args[0] = recipeName;
			return db.update(TABLE_RECIPE_DES, values, KEY_RECIPE_NAME + "=?",
					args);
		} else {
			// record not found
			values.put(KEY_RECIPE_NAME, recipeName);
			values.put(KEY_RECIPE_DES, recipeDes);
			db.insert(TABLE_RECIPE_DES, null, values);
			db.close();
			return 1;

		}

	}

	/**
	 * change the name for a recipe
	 * 
	 * @param oldRecipeName
	 *            old recipe name
	 * @param newRecipeName
	 *            new recipe name
	 * @return number of records changed
	 */
	public int changeRecipeName(String oldRecipeName, String newRecipeName) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_RECIPE_NAME, newRecipeName);
		String[] args = new String[1];
		args[0] = oldRecipeName;
		int n1 = db.update(TABLE_RECIPE_DES, values, KEY_RECIPE_NAME + "=?",
				args);
		int n2 = db.update(TABLE_STEPS, values, KEY_RECIPE_NAME + "=?", args);
		int n3 = db.update(TABLE_IMAGES, values, KEY_RECIPE_NAME + "=?", args);
		int n4 = db.update(TABLE_RECIPES, values, KEY_RECIPE_NAME + "=?", args);
		return n1 + n2 + n3 + n4;
	}

	/**
	 * get the names of all pantry list
	 * 
	 * @return pantry list names as an arraylist of string
	 */
	public ArrayList<String> getPantryList() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> names = new ArrayList<String>();
		// check recipe item table
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_LIST_NAME },
				null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				String name = cursor.getString(cursor
						.getColumnIndex(KEY_LIST_NAME));
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
	 * Inserting new unit into unit table
	 * */
	public void insertUnit(String unit) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_UNIT_NAME, unit);

		// Inserting Row
		db.insert(TABLE_UNIT, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting all unit returns list of unit
	 * */
	public List<String> getAllUnits() {
		List<String> units = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_UNIT;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				units.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		db.close();

		// returning lables
		return units;
	}
}