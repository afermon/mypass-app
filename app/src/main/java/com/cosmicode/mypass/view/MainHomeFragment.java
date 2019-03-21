package com.cosmicode.mypass.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.cosmicode.mypass.BaseActivity;
import com.cosmicode.mypass.R;
import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.service.FolderService;
import com.cosmicode.mypass.service.SecretService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class MainHomeFragment extends Fragment implements FolderService.FolderServiceListener, SecretService.SecretServiceListener {

    private final static String TAG = "MainHomeFragment";
    @BindView(R.id.secrets_list)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.create_fab)
    FloatingActionButton createFloatingActionButton;
    @BindView(R.id.no_resources)
    ConstraintLayout noResourcesMessage;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private FolderService folderService;
    private SecretService secretService;
    private List<Folder> folderList;
    private Folder[] folderArray;
    private String[] folderNames;


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
        secretService = new SecretService(getContext(), this);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        showProgress(true);
        folderService.getUserFolders(true);

        createFloatingActionButton.setOnClickListener(v -> {
            String[] listItems = new String[]{getString(R.string.folder), getString(R.string.secret)};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle(R.string.create_prompt);
            mBuilder.setIcon(R.drawable.icon);
            mBuilder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
                //If is 0 is a Folder, if is 1 is a password
                if (which == 0) createFolder();
                else createSecret();

                dialog.dismiss();
            });

            mBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
    }

    private void refreshFolderSecretList() {
        showProgress(true);
        folderService.getUserFolders(true);
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

    private void createSecret() {
        AlertDialog.Builder selectFolderDialogBuilder = new AlertDialog.Builder(getContext());
        selectFolderDialogBuilder.setTitle(R.string.select_folder);
        selectFolderDialogBuilder.setIcon(R.drawable.icon);
        folderArray = new Folder[folderList.size()];
        folderArray = folderList.toArray(folderArray);
        extractFolderName();

        selectFolderDialogBuilder.setSingleChoiceItems(folderNames, -1, (dialog14, which14) -> {
            dialog14.dismiss();
            //get chosen folder
            Folder selectFolder = folderList.get(which14);
            AlertDialog.Builder createSecretDialogBuilder = new AlertDialog.Builder(getContext());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View secretFormLayout = inflater.inflate(R.layout.secret_form, null);

            createSecretDialogBuilder.setView(secretFormLayout)
                    .setTitle(getString(R.string.secret_create_title))
                    .setNegativeButton(R.string.cancel, (dialog15, which15) -> dialog15.dismiss());

            EditText editTextName = secretFormLayout.findViewById(R.id.edit_name);
            EditText editTextUrl = secretFormLayout.findViewById(R.id.edit_url);
            EditText editTextDescription = secretFormLayout.findViewById(R.id.edit_description);
            EditText editTextUsername = secretFormLayout.findViewById(R.id.edit_username);
            TextView folderNameTextView = secretFormLayout.findViewById(R.id.folder_name);
            folderNameTextView.setText(selectFolder.getName());
            EditText editTextPassword = secretFormLayout.findViewById(R.id.edit_password);

            AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
            mAwesomeValidation.addValidation(editTextName, "[a-zA-Z\\s]+", getString(R.string.error_name));
            mAwesomeValidation.addValidation(editTextUrl, Patterns.WEB_URL, getString(R.string.error_uri));
            mAwesomeValidation.addValidation(editTextUsername, RegexTemplate.NOT_EMPTY, getString(R.string.error_username));
            mAwesomeValidation.addValidation(editTextPassword, RegexTemplate.NOT_EMPTY, getString(R.string.error_pass));


            createSecretDialogBuilder.setPositiveButton(R.string.create, (dialog, which) -> { });

            AlertDialog createSecretDialog = createSecretDialogBuilder.create();
            createSecretDialog.show();
            createSecretDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if(mAwesomeValidation.validate()) {
                    Secret secret = new Secret();
                    secret.setFolderId(selectFolder.getId());
                    secret.setName(editTextName.getText().toString());
                    secret.setNotes(editTextDescription.getText().toString());
                    secret.setUrl(editTextUrl.getText().toString());
                    secret.setUsername(editTextUsername.getText().toString());
                    secret.setNewPassword( editTextPassword.getText().toString());
                    secretService.createSecret(secret, selectFolder.getKey());
                    createSecretDialog.dismiss();
                }
            });

        });
        selectFolderDialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private void createFolder() {
        AlertDialog.Builder createFolderDialogBuilder = new AlertDialog.Builder(getContext());
        final EditText folderNameTexView = new EditText(getContext());
        folderNameTexView.setSingleLine();
        folderNameTexView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_folder_gray,0,0,0);
        folderNameTexView.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dialog_margin));

        FrameLayout dialogContainer = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        folderNameTexView.setLayoutParams(params);
        dialogContainer.addView(folderNameTexView);

        AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(folderNameTexView, "[a-zA-Z\\s]+", getString(R.string.error_name));

        createFolderDialogBuilder.setTitle(R.string.create_folder_title)
        .setMessage(getString(R.string.create_folder_message))
        .setView(dialogContainer)
        .setPositiveButton(R.string.create, (dialog, which) -> { })
        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog createFolderDialog = createFolderDialogBuilder.create();

        createFolderDialog.show();

        createFolderDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(mAwesomeValidation.validate()) {
                String folderName = folderNameTexView.getText().toString();
                Folder newFolder = new Folder(null, null, null, folderName, null, null, null, null);
                folderService.createFolder(newFolder);
                showProgress(true);
                createFolderDialog.dismiss();
            }
        });
    }

    private void editFolder(Folder folder) {
        AlertDialog.Builder editFolderDialogBuilder = new AlertDialog.Builder(getContext());
        final EditText folderNameTexView = new EditText(getContext());
        folderNameTexView.setSingleLine();
        folderNameTexView.setText(folder.getName());
        folderNameTexView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_folder_gray,0,0,0);
        folderNameTexView.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dialog_margin));

        AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(folderNameTexView, "[a-zA-Z\\s]+", getString(R.string.error_name));

        FrameLayout dialogContainer = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        folderNameTexView.setLayoutParams(params);
        dialogContainer.addView(folderNameTexView);

        editFolderDialogBuilder.setTitle(R.string.update_folder_title)
                .setMessage(getString(R.string.create_folder_message))
                .setView(dialogContainer)
                .setPositiveButton(R.string.update, (dialog, which) -> { })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog editFolderDialog = editFolderDialogBuilder.create();
        editFolderDialog.show();

        editFolderDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(mAwesomeValidation.validate()) {
                String folderName = folderNameTexView.getText().toString();
                Folder newFolder = new Folder(null, null, null, folderName, null, null, null, null);
                folderService.createFolder(newFolder);
                showProgress(true);
                editFolderDialog.dismiss();
            }
        });
    }

    private void editSecret(Secret secret, Folder folder) {
        AlertDialog.Builder editSecretBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View secretFormView = inflater.inflate(R.layout.secret_form, null);
        editSecretBuilder.setView(secretFormView)
                .setTitle(getString(R.string.password_edit_title))
                .setNegativeButton(R.string.cancel, (dialog15, which15) -> dialog15.dismiss());

        EditText editTextName = secretFormView.findViewById(R.id.edit_name);
        EditText editTextUrl = secretFormView.findViewById(R.id.edit_url);
        EditText editTextDescription = secretFormView.findViewById(R.id.edit_description);
        EditText editTextPassword = secretFormView.findViewById(R.id.edit_password);
        EditText editTextUsername = secretFormView.findViewById(R.id.edit_username);
        TextView folderNameTextView = secretFormView.findViewById(R.id.folder_name);

        AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(editTextName, "[a-zA-Z\\s]+", getString(R.string.error_name));
        mAwesomeValidation.addValidation(editTextUrl, Patterns.WEB_URL, getString(R.string.error_uri));
        mAwesomeValidation.addValidation(editTextUsername, RegexTemplate.NOT_EMPTY, getString(R.string.error_username));

        editTextName.setText(secret.getName());
        editTextUrl.setText(secret.getUrl());
        editTextDescription.setText(secret.getNotes());
        editTextUsername.setText(secret.getUsername());
        folderNameTextView.setText(folder.getName());

        editSecretBuilder.setPositiveButton(getString(R.string.update), (dialog, which) -> { });

        AlertDialog editSecretDialog = editSecretBuilder.create();
        editSecretDialog.show();

        editSecretDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(mAwesomeValidation.validate()) {
                secret.setName(editTextName.getText().toString());
                secret.setNotes(editTextDescription.getText().toString());
                secret.setUrl(editTextUrl.getText().toString());
                secret.setUsername(editTextUsername.getText().toString());
                String password = editTextPassword.getText().toString();
                if (!password.equals("")) secret.setNewPassword(password);
                secretService.updateSecret(folder.getKey(), secret);
                editSecretDialog.dismiss();
            }
        });
    }

    private void shareFolder(Folder folder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.share_title);
        builder.setMessage(R.string.share_desc);
        final EditText emailTexView = new EditText(getContext());

        emailTexView.setSingleLine();
        emailTexView.setText(folder.getName());
        emailTexView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_mail_black,0,0,0);
        emailTexView.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dialog_margin));
        emailTexView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(emailTexView);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> folderService.shareFolder(folder, emailTexView.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void extractFolderName() {
        folderNames = new String[folderList.size()];
        for (int i = 0; i < folderList.size(); i++) {
            folderNames[i] = folderArray[i].getName();
        }
    }

    @Override
    public void OnGetFoldersSuccess(List<Folder> folders) {
        folderList = folders;
        Log.d(TAG, folders.toString());

        sectionAdapter = new SectionedRecyclerViewAdapter();

        if (folderList.size() > 0) {
            noResourcesMessage.setVisibility(View.INVISIBLE);
            for (Folder folder : folderList) {
                sectionAdapter.addSection(new FolderSection(folder));
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(sectionAdapter);

        } else
            noResourcesMessage.setVisibility(View.VISIBLE);

        showProgress(false);
    }

    @Override
    public void OnCreateFolderSuccess(Folder folder) {
        Toast.makeText(getContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnUpdateFolderSuccess(Folder folder) {
        Toast.makeText(getContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnDeleteFolderSuccess(Long id) {
        Toast.makeText(getContext(), getString(R.string.deleted_msg), Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnShareFolderSuccess(Folder folder) {
        Toast.makeText(getContext(), R.string.share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnFolderActionError(String error) {
        Toast.makeText(getContext(), getString(R.string.something_wrong) + " " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnGetSecretsSuccess(List<Secret> secrets) {

    }

    @Override
    public void OnCreateSecretSuccess(Secret secret) {
        Toast.makeText(getContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnUpdateSecretSuccess(Secret secret) {
        Toast.makeText(getContext(), getString(R.string.updated) + " " + secret.getName(), Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnDeleteSecretSuccess(Long id) {
        Toast.makeText(getContext(), getString(R.string.deleted_msg), Toast.LENGTH_SHORT).show();
        refreshFolderSecretList();
    }

    @Override
    public void OnSecretActionError(String error) {
        Toast.makeText(getContext(), getString(R.string.something_wrong) + " " + error, Toast.LENGTH_LONG).show();
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

        @Override
        public int getContentItemsTotal() {
            return this.folder.getSecrets().size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new SecretViewHolder(view);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final SecretViewHolder itemHolder = (SecretViewHolder) holder;

            Secret secret = this.folder.getSecrets().get(position);

            itemHolder.secretNameTextView.setText(secret.getName());
            itemHolder.secretUsernameTextView.setText(secret.getUsername());

            itemHolder.rootView.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.secret_options, popup.getMenu());
                if (!folder.getOwnerLogin().equals(((BaseActivity) getActivity()).getJhiUsers().getLogedUserLogin())) {
                    //If user not owner then remove edit and delete options from menu.
                    Menu menu = popup.getMenu();
                    menu.removeItem(R.id.edit);
                    menu.removeItem(R.id.delete);
                }
                popup.setOnMenuItemClickListener(item -> {

                    Object clipboardService = v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;
                    ClipData clipData;

                    switch (item.getItemId()) {
                        case R.id.copy_username:
                            clipData = ClipData.newPlainText(getString(R.string.app_name), secret.getUsername());
                            clipboardManager.setPrimaryClip(clipData);
                            Snackbar.make(v, R.string.copied_toast, Snackbar.LENGTH_LONG).show();
                            return true;
                        case R.id.copy_password:
                            clipData = ClipData.newPlainText(getString(R.string.app_name), secret.getPasswordDecrypted(folder.getKey()));
                            clipboardManager.setPrimaryClip(clipData);
                            Snackbar.make(v, R.string.copied_toast, Snackbar.LENGTH_LONG).show();
                            return true;
                        case R.id.show_password:
                            Snackbar.make(v, secret.getPasswordDecrypted(folder.getKey()), Snackbar.LENGTH_LONG).show();
                            return true;
                        case R.id.open_site:
                            String url =  secret.getUrl();
                            if (!url.equals("")){
                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                    url = "http://" + url;
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            } else {
                                Snackbar.make(v, getString(R.string.uril_empty), Snackbar.LENGTH_LONG).show();
                            }
                            return true;
                        case R.id.edit:
                            editSecret(secret,folder);
                            return true;
                        case R.id.delete:
                            new AlertDialog.Builder(getContext())
                                    .setMessage("Are you sure you want to delete the secret " + secret.getName() + "?")
                                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> secretService.deleteSecret(secret))
                                    .setNegativeButton(android.R.string.no, null).show();
                            return true;
                    }

                    return true;
                });

                MenuPopupHelper menuHelper = new MenuPopupHelper(v.getContext(), (MenuBuilder) popup.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.setGravity(Gravity.END);
                menuHelper.show();

            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new FolderViewHolder(view);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            FolderViewHolder headerHolder = (FolderViewHolder) holder;

            headerHolder.folderNameTextView.setText(folder.getName());

            if (!folder.getOwnerLogin().equals(((BaseActivity) getActivity()).getJhiUsers().getLogedUserLogin())) {
                headerHolder.folderCountTextView.setText("(" + folder.getOwnerLogin() + ")");
                headerHolder.folderSettings.setVisibility(View.INVISIBLE);
            } else {
                String count = String.valueOf(folder.getSecrets().size());
                if (count.equals("0")) count = getString(R.string.none);
                headerHolder.folderCountTextView.setText("(" + count + ")");
            }


            headerHolder.folderSettings.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.folder_options, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.share:
                            shareFolder(folder);
                            return true;
                        case R.id.edit:
                            editFolder(folder);
                            return true;
                        case R.id.delete:
                            new AlertDialog.Builder(getContext())
                                    .setMessage("Are you sure you want to delete the folder " + folder.getName() + "?")
                                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> folderService.deleteFolder(folder))
                                    .setNegativeButton(android.R.string.no, null).show();
                            return true;
                    }

                    return true;
                });

                MenuPopupHelper menuHelper = new MenuPopupHelper(v.getContext(), (MenuBuilder) popup.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.setGravity(Gravity.END);
                menuHelper.show();
            });
        }
    }

    private class FolderViewHolder extends RecyclerView.ViewHolder {

        private final TextView folderNameTextView;
        private final TextView folderCountTextView;
        private final ImageView folderSettings;

        FolderViewHolder(View view) {
            super(view);
            folderNameTextView = view.findViewById(R.id.folder_name);
            folderCountTextView = view.findViewById(R.id.folder_count);
            folderSettings = view.findViewById(R.id.folder_settings);
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

    public enum PasswordStrength
    {

        WEAK(0, Color.RED), MEDIUM(1, Color.argb(255, 220, 185, 0)), STRONG(2, Color.GREEN), VERY_STRONG(3, Color.BLUE);

        //--------REQUIREMENTS--------
        static int REQUIRED_LENGTH = 6;
        static int MAXIMUM_LENGTH = 6;
        static boolean REQUIRE_SPECIAL_CHARACTERS = true;
        static boolean REQUIRE_DIGITS = true;
        static boolean REQUIRE_LOWER_CASE = true;
        static boolean REQUIRE_UPPER_CASE = true;

        int resId;
        int color;

        PasswordStrength(int resId, int color)
        {
            this.resId = resId;
            this.color = color;
        }

        public int getValue()
        {
            return resId;
        }

        public int getColor()
        {
            return color;
        }

        public static PasswordStrength calculateStrength(String password)
        {
            int currentScore = 0;
            boolean sawUpper = false;
            boolean sawLower = false;
            boolean sawDigit = false;
            boolean sawSpecial = false;

            for (int i = 0; i < password.length(); i++)
            {
                char c = password.charAt(i);

                if (!sawSpecial && !Character.isLetterOrDigit(c))
                {
                    currentScore += 1;
                    sawSpecial = true;
                }
                else
                {
                    if (!sawDigit && Character.isDigit(c))
                    {
                        currentScore += 1;
                        sawDigit = true;
                    }
                    else
                    {
                        if (!sawUpper || !sawLower)
                        {
                            if (Character.isUpperCase(c))
                                sawUpper = true;
                            else
                                sawLower = true;
                            if (sawUpper && sawLower)
                                currentScore += 1;
                        }
                    }
                }
            }

            if (password.length() > REQUIRED_LENGTH)
            {
                if ((REQUIRE_SPECIAL_CHARACTERS && !sawSpecial) || (REQUIRE_UPPER_CASE && !sawUpper) || (REQUIRE_LOWER_CASE && !sawLower) || (REQUIRE_DIGITS && !sawDigit))
                {
                    currentScore = 1;
                }
                else
                {
                    currentScore = 2;
                    if (password.length() > MAXIMUM_LENGTH)
                    {
                        currentScore = 3;
                    }
                }
            }
            else
            {
                currentScore = 0;
            }

            switch (currentScore)
            {
                case 0:
                    return WEAK;
                case 1:
                    return MEDIUM;
                case 2:
                    return STRONG;
                case 3:
                    return VERY_STRONG;
                default:
            }

            return VERY_STRONG;
        }

    }
}
