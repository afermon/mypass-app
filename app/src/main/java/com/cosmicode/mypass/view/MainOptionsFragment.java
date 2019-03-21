package com.cosmicode.mypass.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.R;
import com.cosmicode.mypass.domain.MyPassUser;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainOptionsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MainOptionsFragment() {
        // Required empty public constructor
    }

    public static MainOptionsFragment newInstance() {
        MainOptionsFragment fragment = new MainOptionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_options, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getJhiUsers().getLogedUser(user -> updateUserInfo(user));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFingerprintListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.logout_button)
    public void logout(View view) {
        if (mListener != null) {
            mListener.performLogout();
        }
    }

    public void updateUserInfo(MyPassUser mypassUser) {
        TextView nameTextView = getView().findViewById(R.id.profile_name);
        TextView emailTextView = getView().findViewById(R.id.profile_email);
        nameTextView.setText(mypassUser.getFullName());
        emailTextView.setText(mypassUser.getEmail());
    }

    public interface OnFragmentInteractionListener {
        void performLogout();

    }
}
