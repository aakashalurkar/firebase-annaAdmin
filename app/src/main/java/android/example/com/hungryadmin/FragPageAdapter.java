package android.example.com.hungryadmin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Aakash on 07-10-2017.
 */

public class FragPageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> pages=new ArrayList<>();

   public FragPageAdapter(FragmentManager fm){
       super(fm);
   }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public void addPage(Fragment f)
    {
        pages.add(f);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).toString();
    }
}
