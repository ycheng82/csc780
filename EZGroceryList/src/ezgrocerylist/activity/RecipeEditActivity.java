package ezgrocerylist.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;
import ezgrocerylist.sql.Item;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to edit a recipe, i.e. add ingredient, add steps
 * 
 * @author Ye
 * 
 */
public class RecipeEditActivity extends Activity {
	private String recipeName;
	ImageView img_logo;
	// protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	private Intent pictureActionIntent = null;
	Bitmap bitmap;
	String selectedImagePath;
	String mCurrentPhotoPath;
	protected static final int REQUEST_TAKE_PHOTO = 0;
	File photoFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);

		// hide action and status bar
		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		// get list name from intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeName = extras.getString("recipename");
		}

		TextView tvRecipeName = (TextView) findViewById(R.id.lbRecipeName);

		tvRecipeName.setText(recipeName);

		// load recipe information from database if it exists
		DatabaseHandler db = new DatabaseHandler(this);
		if (db.getRecipe(recipeName)||db.getCoverImage(recipeName) != null) {
			// load cover image
			if (db.getCoverImage(recipeName) != null) {
				try {
					InputStream stream = getContentResolver().openInputStream(
							db.getCoverImage(recipeName));
					bitmap = BitmapFactory.decodeStream(stream);
					stream.close();
					drawCover(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// load ingredients
			if (db.getRecipe(recipeName)){
				ArrayList<Item> ingredients =db.getIngredients(recipeName);
				//add ingredients (name, quantity, unit) to view
				for (int i = 0; i<ingredients.size();i++){
					LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_ingredients);
					LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lparams.setMargins(50,0,0,0);
					Button btn = new Button(this);
					btn.setBackgroundColor(getResources().getColor(R.color.ingredient_btn));
					btn.setLayoutParams(lparams);
					btn.setText(ingredients.get(i).getItemName());
					llIngredients.addView(btn);
				}
				
			}
			// load steps

			// load images
			db.close();
		}

	}

	/**
	 * callback when click the add ingredients button
	 */
	public void addIngredients(View view) {
		Intent intent = new Intent(this,IngredientActivity.class);
		intent.putExtra("recipename",recipeName);
		startActivity(intent);
		/*LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_ingredients);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("ingredients");
		llIngredients.addView(tv);*/
	}

	/**
	 * callback when click the add steps button
	 */
	public void addSteps(View view) {
		LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_steps);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("ingredients");
		llIngredients.addView(tv);
	}

	/**
	 * callback when click the add images button
	 */
	public void addImages(View view) {
		LinearLayout llIngredients = (LinearLayout) findViewById(R.id.layout_images);
		LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		EditText tv = new EditText(this);
		tv.setLayoutParams(lparams);
		tv.setText("images");
		llIngredients.addView(tv);
	}

	/**
	 * callback when click the add cover for a recipe
	 */
	public void addCover(View view) {
		startDialog();
	}

	/**
	 * make alertdialog for user to choose an image from gallery or using a
	 * camera
	 */
	private void startDialog() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("Upload Pictures Option");
		myAlertDialog.setMessage("How do you want to set your picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						pictureActionIntent = new Intent(
								Intent.ACTION_OPEN_DOCUMENT);
						pictureActionIntent
								.addCategory(Intent.CATEGORY_OPENABLE);
						pictureActionIntent.setType("image/*");
						pictureActionIntent.putExtra("return-data", true);
						startActivityForResult(pictureActionIntent,
								GALLERY_PICTURE);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// pictureActionIntent = new
						// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						// startActivityForResult(pictureActionIntent,CAMERA_REQUEST);
						dispatchTakePictureIntent();

					}
				});
		myAlertDialog.show();
	}

	/**
	 * call back after a picture is choosen from gallery or taken from camera
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
			// We need to recyle unused bitmaps
			if (bitmap != null) {
				bitmap.recycle();
			}
			// save cover image uri to database
			DatabaseHandler db = new DatabaseHandler(this);
			//db.addCover(recipeName, data.getData().toString());
			db.addImage(1,recipeName, data.getData().toString());

			try {
				InputStream stream = getContentResolver().openInputStream(
						data.getData());
				bitmap = BitmapFactory.decodeStream(stream);
				stream.close();
				drawCover(bitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			super.onActivityResult(requestCode, resultCode, data);

		} else if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == RESULT_OK) {
				InputStream stream;
				try {
					stream = getContentResolver().openInputStream(
							Uri.fromFile(photoFile));
					bitmap = BitmapFactory.decodeStream(stream);
					// add image to gallery
					// MediaStore.Images.Media.insertImage(getContentResolver(),
					// bitmap, "" , "");
					addImageToGallery(this, photoFile.getAbsolutePath(), "", "");
					// save cover image uri to database
					DatabaseHandler db = new DatabaseHandler(this);
					//db.addCover(recipeName, Uri.fromFile(photoFile).toString());
					// draw cover
					db.addImage(1,recipeName, Uri.fromFile(photoFile).toString());
					drawCover(bitmap);
					// save uri to database
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * draw cover image
	 * @param bitmap bitmap image to draw
	 */
	public void drawCover(Bitmap bitmap) {
		ImageButton btn = (ImageButton) findViewById(R.id.ibCamera);
		int bwidth = bitmap.getWidth();
		int bheight = bitmap.getHeight();
		Resources res = getResources();
		int swidth = (int) res.getDimension(R.dimen.ibCamera_width);
		// int swidth = btn.getMeasuredWidth();
		Log.e("btn swidth", swidth + "");
		int new_width = swidth;
		int new_height = (int) Math.floor((double) bheight
				* ((double) new_width / (double) bwidth));

		Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, new_width,
				new_height, true);
		btn.setImageBitmap(newBitmap);

	}

	/**
	 * create an unique file to save image
	 * @return image file
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	/**
	 * start intent for taking photo using android camera
	 */
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go

			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	/**
	 * add image to gallery for user to choose later
	 * @param context
	 * @param filepath
	 * @param title
	 * @param description
	 * @return
	 */
	public static Uri addImageToGallery(Context context, String filepath,
			String title, String description) {
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, title);
		values.put(Media.DESCRIPTION, description);
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.MediaColumns.DATA, filepath);

		return context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				values);
	}
}
