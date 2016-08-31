package com.prasilabs.intercom.customs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreActivity;
import com.prasilabs.intercom.core.CoreFragment;

import java.util.List;


/**
 * Created by prasi on 7/2/16.
 * Helper for navigating fragment or replacing fragment
 */
public class FragmentNavigator
{
    public static void navigateToFragment(CoreActivity coreActivity, CoreFragment coreFragment, boolean addToBackStack)
    {
        navigateToFragment(coreActivity, coreFragment, addToBackStack, 0);
    }

    public static void navigateToFragment(CoreActivity coreActivity, CoreFragment coreFragment, boolean addToBackStack, int viewId) {
        navigateToFragment(coreActivity, coreFragment, addToBackStack, viewId, false);
    }

    public static void navigateToFragment(CoreActivity coreActivity, CoreFragment coreFragment, boolean addToBackStack, int viewId, boolean isAdd)
    {
        FragmentManager fragmentManager = coreActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

        if(viewId == 0)
        {
            viewId = R.id.container;
        }

        if (!coreFragment.isAdded())
        {
            if (isAdd)
            {
                fragmentTransaction.add(viewId, coreFragment, coreFragment.getClass().getSimpleName());
            }
            else
            {
                fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
            }
            if (addToBackStack)
            {
                fragmentTransaction.addToBackStack(null);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static void removeFragments(CoreActivity coreActivity)
    {
        FragmentManager fragmentManager = coreActivity.getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for(Fragment fragment : fragmentList)
        {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public static void placeFragment(Fragment parentFragment, CoreFragment coreFragment, int viewId)
    {
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!coreFragment.isAdded())
        {
            fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public static void placeFragment(CoreActivity coreActivity, CoreFragment coreFragment, int viewId)
    {
        FragmentManager fragmentManager = coreActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!coreFragment.isAdded())
        {
            fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

}
