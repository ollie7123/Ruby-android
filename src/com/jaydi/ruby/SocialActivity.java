package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.appspot.ruby_mine.rubymine.model.User;
import com.appspot.ruby_mine.rubymine.model.UserCol;
import com.appspot.ruby_mine.rubymine.model.Userpair;
import com.appspot.ruby_mine.rubymine.model.UserpairCol;
import com.jaydi.ruby.adapters.UserAdapter;
import com.jaydi.ruby.adapters.UserAdapter.PairUserInter;
import com.jaydi.ruby.adapters.UserpairAdapter;
import com.jaydi.ruby.adapters.UserpairAdapter.DeletePairInter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.user.LocalUser;
import com.jaydi.ruby.utils.DialogUtils;
import com.jaydi.ruby.utils.ToastUtils;

public class SocialActivity extends SubCategoryActivity implements PairUserInter, DeletePairInter {
	private int navPosition;

	private List<User> users;
	private ListView listUsers;
	private UserAdapter userAdapter;

	private List<Userpair> userpairs;
	private ListView listUserpairs;
	private UserpairAdapter userpairAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);

		users = new ArrayList<User>();
		listUsers = (ListView) findViewById(R.id.list_social_users);
		userAdapter = new UserAdapter(this, users, this);
		listUsers.setAdapter(userAdapter);

		userpairs = new ArrayList<Userpair>();
		listUserpairs = (ListView) findViewById(R.id.list_social_pairs);
		userpairAdapter = new UserpairAdapter(this, userpairs, this);
		listUserpairs.setAdapter(userpairAdapter);

		// prepare navigation menu and content fragments
		navPosition = 0;
		updateNavMenu();
		changePage();

		getPairs();
	}

	public void changeNav(View view) {
		switch (view.getId()) {
		case R.id.button_social_nav_search:
			navPosition = 0;
			break;
		case R.id.button_social_nav_pairs:
			navPosition = 1;
			break;
		default:
			navPosition = 0;
			break;
		}

		updateNavMenu();
		changePage();

		hideSoftKeyboard();
	}

	private void updateNavMenu() {
		findViewById(R.id.button_social_nav_search).setSelected(false);
		findViewById(R.id.button_social_nav_pairs).setSelected(false);

		switch (navPosition) {
		case 0:
			findViewById(R.id.button_social_nav_search).setSelected(true);
			break;
		case 1:
			findViewById(R.id.button_social_nav_pairs).setSelected(true);
			break;
		default:
			findViewById(R.id.button_social_nav_search).setSelected(true);
			break;
		}
	}

	private void changePage() {
		findViewById(R.id.linear_social_search).setVisibility(View.GONE);
		findViewById(R.id.list_social_pairs).setVisibility(View.GONE);

		switch (navPosition) {
		case 0:
			findViewById(R.id.linear_social_search).setVisibility(View.VISIBLE);
			break;
		case 1:
			findViewById(R.id.list_social_pairs).setVisibility(View.VISIBLE);
			break;
		default:
			findViewById(R.id.linear_social_search).setVisibility(View.VISIBLE);
			break;
		}
	}

	public void searchUser(View view) {
		EditText editName = (EditText) findViewById(R.id.edit_social_name);
		String name = editName.getEditableText().toString();

		NetworkInter.searchUser(new ResponseHandler<UserCol>(DialogUtils.showWaitingDialog(this)) {

			@Override
			protected void onResponse(UserCol res) {
				users.clear();
				if (res != null && res.getUsers() != null)
					users.addAll(res.getUsers());
				userAdapter.notifyDataSetChanged();
			}

		}, LocalUser.getUser().getId(), name);

		hideSoftKeyboard();
	}

	@Override
	public void onPairUser(int position) {
		User user = users.get(position);
		user.setPaired(true);
		userAdapter.notifyDataSetChanged();

		Userpair userpair = new Userpair();
		userpair.setUserIdA(LocalUser.getUser().getId());
		userpair.setUserIdB(user.getId());
		userpair.setUserNameA(LocalUser.getUser().getName());
		userpair.setUserNameB(user.getName());
		userpair.setUserImageKeyA(LocalUser.getUser().getImageKey());
		userpair.setUserImageKeyB(user.getImageKey());
		NetworkInter.pairUsers(new ResponseHandler<Void>() {

			@Override
			protected void onResponse(Void res) {
				getPairs();
			}

		}, userpair);

		ToastUtils.show("Added");
	}

	private void getPairs() {
		NetworkInter.getUserpairs(new ResponseHandler<UserpairCol>() {

			@Override
			protected void onResponse(UserpairCol res) {
				userpairs.clear();
				if (res != null && res.getUserpairs() != null)
					userpairs.addAll(res.getUserpairs());
				userpairAdapter.notifyDataSetChanged();
			}

		}, LocalUser.getUser().getId());
	}

	@Override
	public void onDeletePair(final int position) {
		DialogUtils.showDeletePairDialog(this, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				Userpair pair = userpairs.get(position);
				userpairs.remove(position);
				userpairAdapter.notifyDataSetChanged();

				NetworkInter.depairUsers(null, pair.getId());

				ToastUtils.show("Removed");
			}

		});
	}

	public void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

}
