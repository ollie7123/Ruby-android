package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.appspot.ruby_mine.rubymine.model.RubymineCol;
import com.jaydi.ruby.adapters.MinePagerAdapter;
import com.jaydi.ruby.connection.ResponseHandler;
import com.jaydi.ruby.connection.network.NetworkInter;

public class RecommendActivity extends BaseActivity {
	public static final String EXTRA_CURRENT_RUBYMINE_ID = "com.jaydi.ruby.extras.CURRENT_RUBYMINE_ID";

	private long currentRubymineId;
	private List<Rubymine> rubymines;
	private ViewPager minePager;
	private MinePagerAdapter minePagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);

		if (savedInstanceState == null)
			currentRubymineId = getIntent().getLongExtra(EXTRA_CURRENT_RUBYMINE_ID, 0);

		rubymines = new ArrayList<Rubymine>();
		minePager = (ViewPager) findViewById(R.id.pager_recommend_mines);
		minePagerAdapter = new MinePagerAdapter(getSupportFragmentManager(), rubymines);
		minePager.setAdapter(minePagerAdapter);

		loadMines();
	}

	private void loadMines() {
		NetworkInter.recommendRubymines(new ResponseHandler<RubymineCol>() {

			@Override
			protected void onResponse(RubymineCol res) {
				if (res == null || res.getRubymines() == null)
					return;

				rubymines.clear();
				rubymines.addAll(res.getRubymines());
				refresh();
			}

		}, currentRubymineId);
	}

	private void refresh() {
		minePagerAdapter.notifyDataSetChanged();
	}

}
