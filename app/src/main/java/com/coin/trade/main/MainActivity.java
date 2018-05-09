package com.coin.trade.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.coin.trade.fragment.HomeFragment;
import com.coin.trade.fragment.QuotationFragment;
import com.coin.trade.fragment.TradeFragment;
import com.coin.trade.fragment.AccountFragment;
import com.coin.trade.helper.BottomNavigationViewHelper;
import com.coin.trade.R;


public class MainActivity extends AppCompatActivity {

    private Fragment[] fragments;
    private int currentFragment = 2;
    
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

       @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    showFragment(0);
                    return true;

                case R.id.nav_quotation:
                    showFragment(1);
                    return true;

                case R.id.nav_trade:
                    showFragment(2);
                    return true;
					
				case R.id.nav_account:
                    showFragment(3);
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

		//fragments
        HomeFragment homeFragment = new HomeFragment();
       
        QuotationFragment quotationFragment = new QuotationFragment();
   
        TradeFragment tradeFragment = new TradeFragment();
		
		AccountFragment accountFragment = new AccountFragment();
       
        fragments = new Fragment[] { homeFragment, quotationFragment, tradeFragment, accountFragment };

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .add(R.id.fragment_container, quotationFragment)
				.add(R.id.fragment_container, tradeFragment)
                .add(R.id.fragment_container, accountFragment)
                .hide(quotationFragment)
				.hide(tradeFragment)
                .hide(accountFragment)
                .show(homeFragment).commit();

		//navigation
        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
		navigation.setItemIconTintList(null);
    
        BottomNavigationViewHelper.removeShiftMode(navigation);
    }

    protected void showFragment(int index)
    {
        if (index != currentFragment)
        {
            currentFragment = index;
        }
    
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    
        for (int i = 0; i < fragments.length; i++)
        {
            fragmentTransaction.hide(fragments[i]);
        }
    
        if (!fragments[index].isAdded())
        {
            fragmentTransaction.add(R.id.fragment_container, fragments[index]);
        }
    
        fragmentTransaction.show(fragments[index]).commit();
    }
}
