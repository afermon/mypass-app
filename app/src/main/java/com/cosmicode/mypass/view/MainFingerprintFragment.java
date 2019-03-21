package com.cosmicode.mypass.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmicode.mypass.R;
import com.facebook.CallbackManager;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class MainFingerprintFragment extends Fragment {

    private static final String KEY_NAME = "mypass";
    @BindView(R.id.fingerprint_feedback)
    TextView textView;
    private CallbackManager callbackManager;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private OnFingerprintListener mListener;

    public MainFingerprintFragment() {
        // Required empty public constructor
    }

    public static MainFingerprintFragment newInstance() {
        MainFingerprintFragment fragment = new MainFingerprintFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected())
                textView.setText("Your device doesn't support fingerprint authentication");

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                textView.setText("Please enable the fingerprint permission");

            if (!fingerprintManager.hasEnrolledFingerprints())
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");

            if (!keyguardManager.isKeyguardSecure()) {
                textView.setText("Please enable lockscreen security in your device's Settings");
            } else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }
                if (initCipher()) {
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler helper = new FingerprintHandler(getContext());
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fingerprint, container, false);
        ButterKnife.bind(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFingerprintListener) {
            mListener = (OnFingerprintListener) context;
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

    private void generateKey() throws FingerprintException {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public interface OnFingerprintListener {
        void onFingerprintSuccess(String string);

        void onFingerprintError(String error);
    }

    private class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
        // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

        private CancellationSignal cancellationSignal;
        private Context context;

        public FingerprintHandler(Context mContext) {
            context = mContext;
        }

        //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

        public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

            cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

        public void onAuthenticationError(int errMsgId, CharSequence errString) {

            //I’m going to display the results of fingerprint authentication as a series of toasts.
            //Here, I’m creating the message that’ll be displayed if an error occurs//

            Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

        public void onAuthenticationFailed() {
            Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
        //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
        public void onAuthenticationSucceeded(
                FingerprintManager.AuthenticationResult result) {
            if (mListener != null)
                mListener.onFingerprintSuccess("Success!");
        }
    }
}
