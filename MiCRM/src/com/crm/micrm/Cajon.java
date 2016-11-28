package com.crm.micrm;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Cajon extends Fragment{
	
	public static interface NavigationDrawerCallbacks {

		void onNavigationDrawerItemSelected(int position);

	}
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_lernerd";
	private NavigationDrawerCallbacks mCallbacks;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	
	private int mCurrentSelectedPosition = -1;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	
	//constructor
	public Cajon(){
		
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
		
		if(savedInstanceState != null){
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}
		
		selectItem(mCurrentSelectedPosition);
		
	}
	
    @Override
    public void onStart() {
        super.onStart();
        
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(STATE_SELECTED_POSITION));
        } else if (mCurrentSelectedPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentSelectedPosition);
        }
        
    }
    
    public void updateArticleView(int position) {
        mCurrentSelectedPosition = position;
    }
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		
		if(mDrawerListView != null){
			mDrawerListView.setItemChecked(position, true);
		}
		
		if(mDrawerLayout != null){
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		
		
		if(mCallbacks != null){
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}
	
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if(mDrawerLayout != null && isDrawerOpen()){
			inflater.inflate(R.menu.global, menu);
			showGlobalContexActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void showGlobalContexActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if( mDrawerToggle.onOptionsItemSelected(item) ){
			return true;
		}
		if(item.getItemId() == R.id.seccion){
			Toast.makeText(getActivity(), "MiCRM", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mCallbacks = (NavigationDrawerCallbacks) activity;
			
		}catch(ClassCastException e){
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(R.layout.cajon, container, false);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
				
			}
		});
		mDrawerListView.setAdapter(new ArrayAdapter<String>(
				getActionBar().getThemedContext(), 
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				new String[]{
						getString(R.string.section1),
						getString(R.string.section2),
						getString(R.string.section3)
				}));
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		
		return mDrawerListView;
	}
	
	public boolean isDrawerOpen(){
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayoutL){
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayoutL;
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(
				getActivity(), 
				mDrawerLayout, 
				R.drawable.ic_drawer,
				R.string.abrir_cajon_navegacion, 
				R.string.cerrar_cajon_navegacion){

					@Override
					public void onDrawerClosed(View drawerView) {
						// TODO Auto-generated method stub
						super.onDrawerClosed(drawerView);
						if(!isAdded()){
							return;
						}
						getActivity().supportInvalidateOptionsMenu();
					}

					@Override
					public void onDrawerOpened(View drawerView) {
						// TODO Auto-generated method stub
						super.onDrawerOpened(drawerView);
						if(!isAdded()){
							return;
						}
						if(!mUserLearnedDrawer){
							mUserLearnedDrawer = true;
							SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
							sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true);
						}
						getActivity().supportInvalidateOptionsMenu();
					}
			
		};
		
		if(!mUserLearnedDrawer && !mFromSavedInstanceState){
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
		
		mDrawerLayout.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDrawerToggle.syncState();
			}
		});
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	private ActionBar getActionBar() {
		// TODO Auto-generated method stub
		return ( (ActionBarActivity) getActivity()).getSupportActionBar();
	}
	
	
	
}
