package com.cosmicode.mypass.view;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.R;
import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.service.FolderService;

import java.util.List;

public class MainHomeFragment extends Fragment implements FolderService.FolderServiceListener {

    private OnFragmentInteractionListener mListener;

    public MainHomeFragment() {
        // Required empty public constructor
    }

    public static MainHomeFragment newInstance() {
        MainHomeFragment fragment = new MainHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void OnGetFoldersSuccess(List<Folder> Folders) {

    }

    @Override
    public void OnGetFoldersError(String error) {

    }

    public interface OnFragmentInteractionListener {
        BaseActivity getBaseActivity();
    }
}
