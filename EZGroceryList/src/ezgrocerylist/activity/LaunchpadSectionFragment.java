package ezgrocerylist.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ezgrocerylist.R;

import ezgrocerylist.sql.DatabaseHandler;

/**
 * A fragment that launches the cover and introduction information of recipe.
 * @author Ye
 *
 */
public class LaunchpadSectionFragment extends Fragment {
	private String recipeName;
	private Bitmap bitmap;
	private View rootView ;
    protected static final int GALLERY_PICTURE = 1;
    protected static final int REQUEST_TAKE_PHOTO = 0;
    private File photoFile = null;
    private Intent pictureActionIntent;
    private String selectedImagePath;
	private String mCurrentPhotoPath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        rootView.findViewById(R.id.ibCamera)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	startDialog();
            }
        });


		TextView tvRecipeName = (TextView) rootView.findViewById(R.id.lbRecipeName);
		tvRecipeName.setText(recipeName);
		// load recipe information from database if it exists
		DatabaseHandler db = new DatabaseHandler(this.getActivity());
		if (db.getRecipe(recipeName)||db.getCoverImage(recipeName) != null) {
			// load cover image
			if (db.getCoverImage(recipeName) != null) {
				try {
					InputStream stream = this.getActivity().getContentResolver().openInputStream(
							db.getCoverImage(recipeName));
					bitmap = BitmapFactory.decodeStream(stream);
					stream.close();
					drawCover(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		db.close();
        // Demonstration of a collection-browsing activity.
        /*rootView.findViewById(R.id.demo_collection_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CollectionActivity.class);
                        startActivity(intent);
                    }
                });

        // Demonstration of navigating to external activities.
        rootView.findViewById(R.id.demo_external_activity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create an intent that asks the user to pick a photo, but using
                        // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                        // the application from the device home screen does not return
                        // to the external activity.
                        Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                        externalActivityIntent.setType("image/*");
                        externalActivityIntent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(externalActivityIntent);
                    }
                });*/

        return rootView;
    }
    public void setRecipeName(String recipeName){
    	this.recipeName = recipeName;
    }
	/**
	 * draw cover image
	 * @param bitmap bitmap image to draw
	 */
	public void drawCover(Bitmap bitmap) {
		
		ImageButton btn = (ImageButton) rootView.findViewById(R.id.ibCamera);
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
	 * call back after a picture is choosen from gallery or taken from camera
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {
			// We need to recyle unused bitmaps
			if (bitmap != null) {
				bitmap.recycle();
			}
			// save cover image uri to database
			DatabaseHandler db = new DatabaseHandler(this.getActivity());
			//db.addCover(recipeName, data.getData().toString());
			db.addImage(1,recipeName, data.getData().toString());

			try {
				InputStream stream = this.getActivity().getContentResolver().openInputStream(
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
			if (resultCode == Activity.RESULT_OK) {
				InputStream stream;
				try {
					stream = this.getActivity().getContentResolver().openInputStream(
							Uri.fromFile(photoFile));
					bitmap = BitmapFactory.decodeStream(stream);
					// add image to gallery
					// MediaStore.Images.Media.insertImage(getContentResolver(),
					// bitmap, "" , "");
					addImageToGallery(this.getActivity(), photoFile.getAbsolutePath(), "", "");
					// save cover image uri to database
					DatabaseHandler db = new DatabaseHandler(this.getActivity());
					//db.addCover(recipeName, Uri.fromFile(photoFile).toString());
					// draw cover
					db.addImage(1,recipeName, Uri.fromFile(photoFile).toString());
					drawCover(bitmap);
					// save uri to database
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this.getActivity().getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	public Uri addImageToGallery(Context context, String filepath,
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
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
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
	 * start intent for taking photo using android camera
	 */
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
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
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
}
