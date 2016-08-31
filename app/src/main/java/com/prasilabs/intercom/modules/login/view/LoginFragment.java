package com.prasilabs.intercom.modules.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.prasilabs.intercom.activities.HomeActivity;
import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreFragment;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.managers.UserManager;
import com.prasilabs.intercom.modules.login.presenter.LoginPresenter;
import com.prasilabs.intercom.utils.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 31/8/16.
 */
public class LoginFragment extends CoreFragment<LoginPresenter> implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = LoginFragment.class.getSimpleName();

    private static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = -1;

    @BindView(R.id.sign_in_button)
    protected SignInButton signInButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static final LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_login, container, false));

            if(TextUtils.isEmpty(UserManager.getName(getContext())))
            {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .enableAutoManage(getCoreActivity(), this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                signInButton.setSize(SignInButton.SIZE_STANDARD);
                signInButton.setScopes(gso.getScopeArray());
            }
            else
            {
                ConsoleLog.i(TAG, "opening home activity");
                HomeActivity.openHomeActivity(getContext());
            }
        }

        return getFragmentView();
    }

    @Override
    protected LoginPresenter setCorePresenter()
    {
        return new LoginPresenter();
    }

    @OnClick(R.id.sign_in_button)
    protected void onSignInClicked()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ConsoleLog.i(TAG, "activity result");

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        ConsoleLog.i(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String name = acct.getDisplayName();
            String pictureUrl = acct.getPhotoUrl().toString();

            UserManager.saveUserData(getContext(), name, pictureUrl);

            HomeActivity.openHomeActivity(getContext());
        }
        else
        {
            ConsoleLog.i(TAG, "login failed");
            ViewUtil.t(getContext(), "Login failed");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        ConsoleLog.i(TAG, "connection failed");

        ConsoleLog.i(TAG, "error code is : " + connectionResult.getErrorCode());
        ConsoleLog.i(TAG, "error message is : " + connectionResult.getErrorMessage());
    }
}
