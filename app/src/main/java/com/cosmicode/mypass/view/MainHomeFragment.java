package com.cosmicode.mypass.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.R;
import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.service.FolderService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainHomeFragment extends Fragment implements FolderService.FolderServiceListener {

    private OnFragmentInteractionListener mListener;
    private SectionedRecyclerViewAdapter sectionAdapter;

    @BindView(R.id.secrets_list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.create_fab) FloatingActionButton createFloatingActionButton;

    private FolderService folderService;

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
        folderService = new FolderService(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        ButterKnife.bind(this, view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    createFloatingActionButton.show();
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && createFloatingActionButton.isShown())
                    createFloatingActionButton.hide();
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
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
        showProgress(true);
        folderService.getUserFolders(true);
    }

    @Override
    public void OnGetFoldersSuccess(List<Folder> folders) {
        sectionAdapter = new SectionedRecyclerViewAdapter();

        for (Folder folder: folders) {
            sectionAdapter.addSection(new FolderSection(folder));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);
        showProgress(false);
    }

    @Override
    public void OnGetFoldersError(String error) {

    }

    public interface OnFragmentInteractionListener {
        BaseActivity getBaseActivity();
    }

    private void showProgress(boolean show) {
        Long shortAnimTime = (long) getResources().getInteger(android.R.integer.config_shortAnimTime);

        recyclerView.setVisibility(((show) ? View.GONE : View.VISIBLE));

        recyclerView.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 0 : 1))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recyclerView.setVisibility(((show) ? View.GONE : View.VISIBLE));
                    }
                });

        progressBar.setVisibility(((show) ? View.VISIBLE : View.GONE));
        progressBar.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 1 : 0))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(((show) ? View.VISIBLE : View.GONE));
                    }
                });
    }

    private class FolderSection extends StatelessSection {

        final Folder folder;

        FolderSection(Folder folder) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.secret_item)
                    .headerResourceId(R.layout.folder_item)
                    .build());

            this.folder = folder;
        }

        FolderSection(Folder folder, List<Secret> secrets) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.secret_item)
                    .headerResourceId(R.layout.folder_item)
                    .build());
            this.folder = folder;
            this.folder.setSecrets(secrets);
        }

        @Override
        public int getContentItemsTotal() {
            return this.folder.getSecrets().size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new SecretViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final SecretViewHolder itemHolder = (SecretViewHolder) holder;

            Secret secret = this.folder.getSecrets().get(position);

            itemHolder.secretUsernameTextView.setText(secret.getName());
            itemHolder.secretNameTextView.setText(secret.getUsername());

            itemHolder.rootView.setOnClickListener(v -> Toast.makeText(getContext(),
                    String.format("Clicked on position #%s of Section %s",
                            sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()),
                            folder),
                    Toast.LENGTH_SHORT).show());
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new FolderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            FolderViewHolder headerHolder = (FolderViewHolder) holder;

            headerHolder.folderNameTextView.setText(folder.getName());


            String count = String.valueOf(folder.getSecrets().size());
            if (count.equals("0")) count = getString(R.string.none);
            headerHolder.folderCountTextView.setText("(" + count +")");
        }
    }

    private class FolderViewHolder extends RecyclerView.ViewHolder {

        private final TextView folderNameTextView;
        private final TextView folderCountTextView;

        FolderViewHolder(View view) {
            super(view);
            folderNameTextView = view.findViewById(R.id.folder_name);
            folderCountTextView = view.findViewById(R.id.folder_count);
        }
    }

    private class SecretViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView secretNameTextView;
        private final TextView secretUsernameTextView;

        SecretViewHolder(View view) {
            super(view);
            rootView = view;
            secretNameTextView = view.findViewById(R.id.secret_name);
            secretUsernameTextView = view.findViewById(R.id.secret_username);
        }
    }
}
