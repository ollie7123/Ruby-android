package com.jaydi.ruby;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.appspot.ruby_mine.rubymine.model.Ruby;
import com.appspot.ruby_mine.rubymine.model.Rubymine;
import com.jaydi.ruby.adapters.MinePagerAdapter;
import com.jaydi.ruby.models.RubyParcel;
import com.jaydi.ruby.models.RubymineParcel;

public class RecommendActivity extends BaseActivity {
	public static final String EXTRA_RUBIES = "com.jaydi.ruby.extras.RUBIES";
	public static final String EXTRA_PLANTER = "com.jaydi.ruby.extras.PLANTER";
	public static final String EXTRA_GIVERS = "com.jaydi.ruby.extras.GIVERS";

	private List<Ruby> rubies;
	@SuppressWarnings("unused")
	private Rubymine planter;
	private List<Rubymine> givers;
	private ViewPager minePager;
	private MinePagerAdapter minePagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);

		if (savedInstanceState == null) {
			RubyParcel[] rubyParcels = (RubyParcel[]) getIntent().getParcelableArrayExtra(EXTRA_RUBIES);
			rubies = new ArrayList<Ruby>();
			for (RubyParcel rubyParcel : rubyParcels)
				rubies.add(rubyParcel.toRuby());

			RubymineParcel planterParcel = getIntent().getParcelableExtra(EXTRA_PLANTER);
			planter = planterParcel.toRubymine();

			RubymineParcel[] giverParcels = (RubymineParcel[]) getIntent().getParcelableArrayExtra(EXTRA_GIVERS);
			givers = new ArrayList<Rubymine>();
			for (RubymineParcel giverParcel : giverParcels)
				givers.add(giverParcel.toRubymine());
		}

		minePager = (ViewPager) findViewById(R.id.pager_recommend_mines);
		minePagerAdapter = new MinePagerAdapter(getSupportFragmentManager(), givers);
		minePager.setAdapter(minePagerAdapter);
	}

}
