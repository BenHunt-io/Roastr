package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.exetxstate.bullytoolkit.R;

import static java.security.AccessController.getContext;

/**
 * Created by The Dough Boys on 12/21/2016.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private String[] tabTitle = new String[]{"Generate", "Saved", "Donate", "Settings"};
    Context context;
    private int privatePageCount = 4;

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new fragment1();
        }
        if (position == 1) {
            fragment = new fragment2();

        }
        if (position == 2) {
            fragment = new fragment3();

        }
        if (position == 3) {
            fragment = new fragment4();

        }
        return fragment;

    }

    @Override
    public int getCount() {
        return privatePageCount;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitle[position];
    }
}



