package barisic.newsgetter.adapters;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import barisic.newsgetter.db_classes.Source;
import barisic.newsgetter.fragments.NewsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentListTitles = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context c) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = c;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public String getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    public void resetFragments(List<Source> sources){
        removeAllFragments();
        notifyDataSetChanged();

        if(sources == null){
            return;
        }

        for (Source source : sources){
            addFragment(new NewsFragment(source.getDomain(), context), source.getName());
        }
        notifyDataSetChanged();
    }

    private void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
    private void removeAllFragments(){
        fragmentList.clear();
        fragmentListTitles.clear();
    }
}
