package me.kennydude.awesomeprefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class ImagePreference extends WidgetPreference<String> {

	public ImagePreference(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	boolean saveToDisk = false; // Save image to disk
	
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(AWESOME_NAMESPACE)){
			if(key.equals("save")){
				saveToDisk = value.equals("true");
				return;
			}
		}
		super.setXMLAttribute(namespace, key, value, resId);
	}
	
	public static String generateImageFileName(Context context) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "IMG_" + timeStamp + ".jpg";
		File storageDir = context.getExternalCacheDir();
		storageDir.mkdirs();
		File fi = new File(storageDir, imageFileName);
		return fi.getAbsolutePath();
	}
	
	File getOutputImage(){
		try{
			File fi = new File(generateImageFileName(getContext()));
			fi.createNewFile();
			return fi;
		} catch(Exception e){ return null; }
	}
	
	int getFileSize(File in) {
		try {
			FileInputStream fis = new FileInputStream(in);
			int r = fis.available();
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getFragment().getActivity().managedQuery(uri, projection, null, null, null);
	    getFragment().getActivity().startManagingCursor(cursor);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	@Override
	public void onActivityResult(int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			if (getFileSize(tempFile) == 0) {
				File source = new File(getPath(data.getData()));
				if(saveToDisk){
					try{
						if (source.exists()) {
				            FileChannel src = new FileInputStream(source).getChannel();
				            FileChannel dst = new FileOutputStream(tempFile).getChannel();
				            dst.transferFrom(src, 0, src.size());
				            src.close();
				            dst.close();
				        }
					} catch(Exception e){ e.printStackTrace(); }
				} else{
					tempFile = source;
				}
			}
			setValue(tempFile.getAbsolutePath());
		}
	}
	
	File tempFile;
	Bitmap bm;
	
	@Override
	public void onClick(View v){
		Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null)
			.setType("image/*");
		if(saveToDisk){
			try{
				tempFile = getOutputImage();
				galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.parse(tempFile.getAbsolutePath()))
						.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
			} catch(Exception e){}
		}
		getFragment().startActivityForResult(this, galleryIntent);
	}

	@Override
	public View getWidget() {
		bm = BitmapFactory.decodeFile(Value);
		
		ImageView iv = new ImageView(getContext());
		iv.setMinimumHeight(90);
		iv.setMinimumWidth(90);
		iv.setImageBitmap(bm);
		return iv;
	}

	@Override
	protected java.lang.Class<?> getType() {
		return String.class;
	}

}
