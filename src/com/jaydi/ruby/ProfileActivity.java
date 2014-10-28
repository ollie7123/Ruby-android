package com.jaydi.ruby;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.appspot.ruby_mine.rubymine.model.User;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.dialogs.PictureOptionsDialog;
import com.jaydi.ruby.dialogs.PictureOptionsDialog.OnPictureOptionClickListener;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ResourceUtils;

public class ProfileActivity extends BaseActivity implements OnPictureOptionClickListener {
	private static final int REQUEST_GET_IMAGE = 0;

	private String imagePath;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		setParam();
	}

	private void setParam() {
		User user = LocalUser.getUser();

		if (user.getImageKey() != null && !user.getImageKey().isEmpty()) {
			ImageView imageImage = (ImageView) findViewById(R.id.image_profile_image);
			NetworkInter.getImage(null, imageImage, ResourceUtils.getImageUrlFromKey(user.getImageKey()), 300, 300);
		}

		if (user.getName() != null && !user.getName().isEmpty()) {
			EditText editName = (EditText) findViewById(R.id.edit_profile_nickname);
			editName.setText(user.getName());
		}
	}

	public void getImage(View view) {
		PictureOptionsDialog dialog = new PictureOptionsDialog();
		dialog.setOnPictureOptionClickListener(this);
		dialog.show(getSupportFragmentManager(), "pictureOptions");
	}

	@Override
	public void onPictureOptionClick(int position) {
		Intent intent;

		if (position == 0) {
			intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		} else {
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		}

		PackageManager manager = getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(intent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe)
			startActivityForResult(intent, REQUEST_GET_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri image = data.getData();
			String[] projection = { Media.DATA };

			Cursor cursor = getContentResolver().query(image, projection, null, null, null);
			cursor.moveToFirst();

			int column = cursor.getColumnIndex(Media.DATA);
			imagePath = cursor.getString(column);

			cursor.close();

			setImage();
		}
	}

	private void setImage() {
		ImageView imageImage = (ImageView) findViewById(R.id.image_profile_image);
		NetworkInter.getImage(null, imageImage, imagePath, 300, 300);
	}

	public void confirmProfile(View view) {
		EditText editName = (EditText) findViewById(R.id.edit_profile_nickname);
		name = editName.getEditableText().toString();

		if (imagePath == null || imagePath.isEmpty())
			sendProfile();
		else
			sendProfileWithImage();
	}

	private void sendProfileWithImage() {
		if (imagePath.startsWith("/"))
			NetworkInter.uploadImage(new ResponseHandler<String>(DialogUtils.showWaitingDialog(this)) {

				@Override
				protected void onResponse(String imageKey) {
					imagePath = imageKey;
					sendProfile();
				}

			}, imagePath);
		else
			sendProfile();
	}

	private void sendProfile() {
		User user = LocalUser.getUser();
		user.setImageKey(imagePath);
		user.setName(name);

		// temporary
		if (user.getId() == null)
			NetworkInter.insertUser(new ResponseHandler<User>(DialogUtils.showWaitingDialog(this)) {

				@Override
				protected void onResponse(User res) {
					if (res != null)
						LocalUser.setUser(res);
					goToMain();
				}

			}, user);
		else
			NetworkInter.updateUser(new ResponseHandler<User>(DialogUtils.showWaitingDialog(this)) {

				@Override
				protected void onResponse(User res) {
					if (res != null)
						LocalUser.setUser(res);
					goToMain();
				}

			}, user);
	}

	private void goToMain() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
