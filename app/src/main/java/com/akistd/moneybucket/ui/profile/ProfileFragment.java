package com.akistd.moneybucket.ui.profile;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.ui.transaction.ThuNhapJarsSettingsActivity;
import com.akistd.moneybucket.util.Constants;
import com.akistd.moneybucket.util.ImageLoadTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView profile_username,profile_userid;
    ImageView profile_image;
    ImageButton profile_logoutBtn;
    Constants util = new Constants();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button deleteInfoBtn, sampleButtonID, appInfoBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        addControls(v);
        addEvents();
        return v;
    }

    private void addControls(View v){
        profile_username = v.findViewById(R.id.profile_username);
        profile_userid = v.findViewById(R.id.profile_userid);
        profile_logoutBtn = v.findViewById(R.id.profile_logoutBtn);
        profile_image = v.findViewById(R.id.profile_image);
        deleteInfoBtn  = v.findViewById(R.id.deleteInfoBtn);
        sampleButtonID = v.findViewById(R.id.sampleButtonID);
        appInfoBtn = v.findViewById(R.id.appInfoBtn);

    }

    private void addEvents(){

        //from sampleButtonID go to ThuNhapJarsSettingsActivity activity
        sampleButtonID.setOnClickListener(v -> {
            Intent jarSettings = new Intent(getContext(), ThuNhapJarsSettingsActivity.class);
            startActivity(jarSettings);
        });

        //Lấy thông tin đăng nhập
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(),gso);
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getContext());
        acc.getPhotoUrl();

        //User profile info
        profile_username.setText(getArguments().getString("username"));
        profile_userid.setText(getArguments().getString("userId"));
        new ImageLoadTask(acc.getPhotoUrl().toString(),profile_image).execute();
        profile_logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        deleteInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserInfo();
            }
        });

        appInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("MoneyBucket");


                builder1.setMessage("Phiên bản 0.0.1\nNhóm thực hiện:Tuấn Tú, Thái Vỹ, Anh Tùng, Bá Huy, Quang An");


                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đóng",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton("Xoá dữ liệu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserInfo();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    private void deleteUserInfo(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        builder2.setTitle("Xác nhận xoá mọi dữ liệu");
        builder2.setMessage("Hành động này sẽ xoá toàn bộ dữ liệu hiện tại của bạn và đặt về trạng thái mặc định!\nBạn chắc chứ?");
        builder2.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder2.setPositiveButton("Xoá và khởi động lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Jars> allJars = MongoDB.getInstance().getAllJars();
                ArrayList<Transaction> allTrans = MongoDB.getInstance().getAllTransaction();

                for (Transaction tr: allTrans) {
                    MongoDB.getInstance().deleteTransaction(tr.getId());
                }
                for (Jars j:allJars) {
                    MongoDB.getInstance().deleteJar(j.getId());
                }
                SharedPreferences prefs = getContext().getSharedPreferences("com.akistd.moneyBucket", MODE_PRIVATE);
                prefs.edit().clear().apply();
                prefs.edit().putBoolean("firstrun", true).commit();
                signOut();
                doRestart(getContext());
            }
        });

        builder2.show();

    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                doRestart(getContext());
            }
        });
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }

}