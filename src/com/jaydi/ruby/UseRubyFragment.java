package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.appspot.ruby_mine.rubymine.model.Gem;
import com.appspot.ruby_mine.rubymine.model.GemCol;
import com.jaydi.ruby.adapters.GemAdapter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;

public class UseRubyFragment extends MainFragment {
	private View view;
	private List<Gem> gems;
	private GridView gridGems;
	private GemAdapter gemAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_use_ruby, container, false);

		gems = new ArrayList<Gem>();
		gridGems = (GridView) view.findViewById(R.id.grid_use_ruby_gems);
		gemAdapter = new GemAdapter(getActivity(), gems);
		gridGems.setAdapter(gemAdapter);

		return view;
	}

	@Override
	public void loadContents(long rubyzoneId) {
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
	}

	private void refresh() {
		gemAdapter.notifyDataSetChanged();
	}

	private void hideProgress() {
		view.findViewById(R.id.progressbar_use_ruby_loading).setVisibility(View.GONE);
	}

}
