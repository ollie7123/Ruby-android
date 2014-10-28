package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.appspot.ruby_mine.rubymine.model.Gem;
import com.appspot.ruby_mine.rubymine.model.GemCol;
import com.jaydi.ruby.adapters.GemAdapter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;
import com.jaydi.ruby.models.GemParcel;

public class UseRubyFragment extends MainFragment {
	private View view;
	private List<Gem> gems;
	private GridView gridGems;
	private GemAdapter gemAdapter;

	private boolean loaded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_use_ruby, container, false);

		gems = new ArrayList<Gem>();
		gridGems = (GridView) view.findViewById(R.id.grid_use_ruby_gems);
		gemAdapter = new GemAdapter(getActivity(), gems);
		gridGems.setAdapter(gemAdapter);
		gridGems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goToGem(position);
			}

		});

		loaded = false;

		return view;
	}

	@Override
	public void loadContents(long rubyzoneId) {
		if (loaded)
			return;

		NetworkInter.getZoneGems(new ResponseHandler<GemCol>() {

			@Override
			protected void onResponse(GemCol res) {
				hideProgress();
				if (res == null || res.getGems() == null)
					return;

				gems.clear();
				gems.addAll(res.getGems());
				refresh();
			}

		}, rubyzoneId);

		loaded = true;
	}

	private void refresh() {
		gemAdapter.notifyDataSetChanged();
	}

	private void hideProgress() {
		view.findViewById(R.id.progressbar_use_ruby_loading).setVisibility(View.GONE);
	}

	private void goToGem(int position) {
		Gem gem = (Gem) gemAdapter.getItem(position);
		Intent intent = new Intent(getActivity(), GemActivity.class);
		intent.putExtra(GemActivity.EXTRA_GEM, new GemParcel(gem));
		getActivity().startActivity(intent);
	}

}
