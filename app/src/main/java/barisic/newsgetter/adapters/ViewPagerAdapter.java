package barisic.newsgetter.adapters;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import barisic.newsgetter.db_classes.Source;
import barisic.newsgetter.fragments.MyNewsItemFragment;
import barisic.newsgetter.fragments.NewsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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

    public List<Fragment> getFragmentList(){
        return fragmentList;
    }

    public void resetFragments(List<Source> sources){
        removeAllFragments();
        notifyDataSetChanged();

        if(sources == null){
            return;
        }

        for (Source source : sources){
            addFragment(NewsFragment.newInstance(source.getDomain()), source.getName());
        }
        notifyDataSetChanged();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
    public void removeAllFragments(){
        fragmentList.clear();
        fragmentListTitles.clear();
    }
}
