package com.prasilabs.intercom.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.utils.ViewUtil;

import butterknife.ButterKnife;

/**
 * Created by prasi on 6/2/16.
 * All fragment should extend this class
 */
public abstract class CoreFragment<T extends CorePresenter> extends Fragment
{
    private static final String TAG = CoreFragment.class.getSimpleName();
    private View mFragmentView;
    private Context context;
    private CoreActivity coreActivity;
    private T corePresenter;
    private CoreDialogFragment coreDialogFragment;
    private Dialog dialog;
    private boolean isLoading;


    public View getFragmentView()
    {
        return mFragmentView;
    }

    public void setFragmentView(View fragmentView)
    {
        this.mFragmentView = fragmentView;
        ButterKnife.bind(this, fragmentView);
    }

    protected abstract T setCorePresenter();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        corePresenter = setCorePresenter();
        if(corePresenter != null)
        {
            corePresenter.onCreate();
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(corePresenter != null)
        {
            corePresenter.onDestroy();
        }
    }

    protected boolean onDialogBackPressed()
    {
        return true;
    }

    protected void setDialogFragment(CoreDialogFragment coreDialogFragment) {
        this.coreDialogFragment = coreDialogFragment;
    }

    public CoreDialogFragment getCoreDialogFragment() {
        return coreDialogFragment;
    }

    public CoreActivity getCoreActivity()
    {
        return coreActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.coreActivity = (CoreActivity) activity;
    }

    @Override
    public Context getContext()
    {
        return context;
    }

    protected T  getPresenter()
    {
        return corePresenter;
    }

    public void showLoader()
    {
        if(isAdded())
        {
            if (dialog == null) {
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.widget_progress_dialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(CoreApp.appDebug);
            }
            dialog.show();
        }
    }

    public void dismissLoader()
    {
        if(dialog != null && isAdded())
        {
            dialog.dismiss();
        }
    }

    public void showLoader(ViewGroup viewGroup, boolean isBig)
    {
        if(!isLoading)
        {
            isLoading = true;
            ViewUtil.showProgressView(getContext(), viewGroup, isBig);
        }
    }

    public void dismissLoader(ViewGroup viewGroup)
    {
        if(isLoading)
        {
            ViewUtil.hideProgressView(getContext(), viewGroup);
            isLoading = false;
        }
    }

    public boolean onBackPressed()
    {
        return true;
    }
}
