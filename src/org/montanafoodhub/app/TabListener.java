package org.montanafoodhub.app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

public class TabListener implements ActionBar.TabListener {

    private Context _context;
    private Class _tabClass;
    private Fragment _tabFragment;


    public TabListener(Context context, Class tabClass) {

        _context = context;
        _tabClass = tabClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        if (_tabFragment != null) {
            fragmentTransaction.attach(_tabFragment);
        } else {
            String className = _tabClass.getName();
            _tabFragment = Fragment.instantiate(_context, className);
            fragmentTransaction.replace(android.R.id.content, _tabFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.detach(_tabFragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
