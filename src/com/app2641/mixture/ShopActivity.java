package com.app2641.mixture;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ShopActivity extends FragmentActivity implements ActionBar.TabListener {
	
	ShopPagerAdapter shopPagerAdapter;
	ViewPager viewPager;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		
		shopPagerAdapter = new ShopPagerAdapter(getSupportFragmentManager());
		
		final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.shop_activity_pager);
        viewPager.setAdapter(shopPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < shopPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(shopPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shop, menu);
        return true;
    }

    

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    
    /**
     * adapter
     */
    public class ShopPagerAdapter extends FragmentPagerAdapter {

        public ShopPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	Fragment fragment = null;
        	
        	switch (i) {
        	case 0:
        		fragment = new FragmentOfflineShopList();
        		break;
        	
        	case 1:
        		fragment = new FragmentOnlineShopList();
        		break;
        	}
        	
//            Bundle args = new Bundle();
//            args.putInt(FragmentShopList.ARG_SECTION_NUMBER, i + 1);
//            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.offline).toUpperCase();
                case 1: return getString(R.string.online).toUpperCase();
            }
            return null;
        }
    }
    
    
    /**
     * offline shop fragment
     */
    public static class FragmentOfflineShopList extends Fragment {
    	
    	public FragmentOfflineShopList() {
    		
    	}
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		
    		Bundle args = getArguments();
    		return inflater.inflate(R.layout.fragment_shop_offline_list, container, false);
    	}
    }
    
    
	/**
	 * online shop fragment
	 */
    public static class FragmentOnlineShopList extends Fragment {
    	
    	public FragmentOnlineShopList () {
    		
    	}
    	
    	@Override
    	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		return inflater.inflate(R.layout.fragment_shop_online_list, container, false);
    	}
    }
}
