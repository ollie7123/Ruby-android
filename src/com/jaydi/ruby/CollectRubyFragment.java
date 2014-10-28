package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.appspot.ruby_mine.rubymine.model.RubymineCol;
import com.jaydi.ruby.adapters.MinePagerAdapter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;

public class CollectRubyFragment extends MainFragment implements OnPageChangeListener {
	private View view;
	private List<Rubymine> rubymines;
	private ViewPager minePager;
	private MinePagerAdapter minePagerAdapter;

	private boolean loaded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main_collect_ruby, container, false);

		rubymines = new ArrayList<Rubymine>();
		minePager = (ViewPager) view.findViewById(R.id.pager_collect_ruby_mines);
		minePagerAdapter = new MinePagerAdapter(getFragmentManager(), rubymines);
		minePager.setAdapter(minePagerAdapter);
		minePager.setOnPageChangeListener(this);

		loaded = false;

		return view;
	}

	@Override
	public void loadContents(long rubyzoneId) {
		if (loaded)
			return;

		NetworkInter.getRubymines(new ResponseHandler<RubymineCol>() {

			@Override
			protected void onResponse(RubymineCol res) {
				hideProgress();
				if (res == null || res.getRubymines() == null)
					return;

				rubymines.clear();
				rubymines.addAll(res.getRubymines());
				refreshContents();
			}

		}, rubyzoneId);

		loaded = true;
	}

	private void hideProgress() {
		view.findViewById(R.id.progressbar_collect_ruby_loading).setVisibility(View.GONE);
	}

	private void refreshContents() {
		minePagerAdapter.notifyDataSetChanged();
		minePager.setCurrentItem(0);
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

}
