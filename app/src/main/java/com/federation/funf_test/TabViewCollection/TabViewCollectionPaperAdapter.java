package com.federation.funf_test.TabViewCollection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabViewCollectionPaperAdapter extends FragmentStatePagerAdapter {
    public TabViewCollectionPaperAdapter(FragmentManager fm){
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
