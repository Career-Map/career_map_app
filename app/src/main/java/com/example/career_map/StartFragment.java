package com.example.career_map;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.career_map.Utils.Constant;
import com.example.career_map.databinding.FragmentStartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class StartFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_REQUEST_CODE = 132;
    private FragmentStartBinding binding;
    private NavController navController;

    private Activity activity;

    Menu menu;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private View header;
    private BottomNavigationView bottomNavigationView;

    private Animation tvFadeOut, tvFadeIn;

    private DocumentReference documentReference;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private String userid;
    private DocumentSnapshot documentSnapshot;

    private Constant constant;
    private UserModel userModel;

    private AppUpdateManager appUpdateManager;

    private static final String TAG = "test";
    private static final String testTAG = "empty";

    private AlertDialog.Builder builder1;


    private SharedPreferences sharedPreferences;
    private String theme;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStartBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        builder1 = new AlertDialog.Builder(requireContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        theme = sharedPreferences.getString("THEME", "1");

        if (theme.equals("1")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme.equals("2")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("Event")
                .addOnCompleteListener(task -> {
                    String msg = "Success";
                    Log.d("subscribe success", "token");
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                        Log.d("subscribe failed", "token");
                    }
                });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("token failed", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    else
                    {
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("token success", token);
                        //sendfcm(token);

                        Map<String, Object> fcmtoken = new HashMap<>();
                        fcmtoken.put("token", token);

                        firestoreDB.collection("token").document(firebaseAuth.getUid())
                                .set(fcmtoken)
                                .addOnSuccessListener((OnSuccessListener<Void>) aVoid -> Log.d("token uploading", "DocumentSnapshot successfully written!"))
                                .addOnFailureListener((OnFailureListener) e -> Log.w("token uploading", "Error writing document", e));
                    }
                });


        setAnimations();
        setTitleText(1);

        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getUid();
        constant = new Constant();
        userModel = new UserModel();

        documentReference = firestoreDB.collection(constant.getUsers()).document(userid);
        status("Online");


        return view;
    }

    private void setAnimations() {
        tvFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.title_bar_text_animation_fade_up);
        tvFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.title_bar_text_animation_fade_in);
    }



    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        binding.getRoot(),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.white));
        snackbar.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawerLayout = binding.drawer;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        float radius = getResources().getDimension(R.dimen.radius_10);
        MaterialShapeDrawable materialShapeDrawable = (MaterialShapeDrawable) navigationView.getBackground();
        materialShapeDrawable.setShapeAppearanceModel(materialShapeDrawable.getShapeAppearanceModel()
                .toBuilder().setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                .build());

//        Bottom Navigation bar
        bottomNavigationView = binding.bottomNavBar;

        NavHostFragment nestedNavHostFragment =(NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (nestedNavHostFragment != null) {
            navController = nestedNavHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        } else {
            Toast.makeText(getContext(), "Error getting controller", Toast.LENGTH_SHORT).show();
        }



        binding.drawerToggleIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
//        Get Data when this activity starts
        getUserData();
        Log.d(testTAG, "onStart: ");
    }

    private void getUserData() {

//      get realtime data and store it in a class
        Log.d(testTAG, "getUserData: ");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (isAdded()){
                    if (error != null) {
                        Log.d(TAG, "onEvent: " + error.toString());
                        return;
                    }

                    if (value != null && value.exists()) {
                        documentSnapshot = value;
                        saveDataToClass();
                        Log.d(TAG, "onEvent: " + value.getData());
                    }
                }
            }
        });

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot alertSnapshot = task.getResult();
                    int temp = 0;
                    if (alertSnapshot.getString(constant.getUserPhotoField()) == null || alertSnapshot.getString(constant.getUserBioField()) == null ) {
                        Log.d(testTAG, "onCompleteAlert: Photo, bio or tags null: " + alertSnapshot.getString(constant.getUserPhotoField()) + ", " + alertSnapshot.getString(constant.getUserBioField()));

                        builder1.setTitle("Tell us a bit about yourself");
                        builder1.setMessage("Complete your profile to continue");
                        builder1.setIcon(R.drawable.ic_baseline_settings_24);
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Complete profile", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getContext(), EditProfileActivity.class).putExtra(constant.getUserModelObject(), userModel);
                                startActivity(intent);
                            }
                        }).show();

//                        temp = 1;
                    }

                }
            }
        });
    }


    private void saveDataToClass() {
        getActivity().getIntent().putExtra(constant.getUserModelObject(), userModel);

        userModel.setUserName(documentSnapshot.getString(constant.getUserNameField()));
        userModel.setUserEmail(documentSnapshot.getString(constant.getUserEmailField()));
        userModel.setUserPhoto(documentSnapshot.getString(constant.getUserPhotoField()));
        userModel.setUserBio(documentSnapshot.getString(constant.getUserBioField()));
        userModel.setUserLinkedin(documentSnapshot.getString(constant.getUserLinkedinField()));
        userModel.setUserPhone(documentSnapshot.getString(constant.getUserPhoneField()));
        userModel.setUserID(documentSnapshot.getString(constant.getUserIdField()));


        updateHeader();
    }

    protected void setTitleText(int position) {
        switch (position) {
            case 1:
                binding.tvTitleBar.startAnimation(tvFadeOut);
                binding.tvTitleBar.setText("Feed");
                binding.tvTitleBar.startAnimation(tvFadeIn);
                break;
            case 2:
                binding.tvTitleBar.startAnimation(tvFadeOut);
                binding.tvTitleBar.setText("Course");
                binding.tvTitleBar.startAnimation(tvFadeIn);
                break;
            case 3:
                binding.tvTitleBar.startAnimation(tvFadeOut);
                binding.tvTitleBar.setText("API Test");
                binding.tvTitleBar.startAnimation(tvFadeIn);
                break;
            case 4:
                binding.tvTitleBar.startAnimation(tvFadeOut);
                binding.tvTitleBar.setText("MCC");
                binding.tvTitleBar.startAnimation(tvFadeIn);
                break;
            case 5:
                binding.tvTitleBar.startAnimation(tvFadeOut);
                binding.tvTitleBar.setText("MSM");
                binding.tvTitleBar.startAnimation(tvFadeIn);
                break;
            default:
                Toast.makeText(getContext(), "Wrong fragment", Toast.LENGTH_SHORT).show();

        }
    }

    protected void loadFragment(int position) {
        //switching fragment
        /*if (fragment != null) {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
        }*/
        switch (position) {
            case 1:
                setTitleText(1);
                bottomNavigationView.setSelectedItemId(R.id.feedFragmentMenu);
                navController.navigate(R.id.feedFragmentMenu);
                break;
            case 2:
                setTitleText(2);
                bottomNavigationView.setSelectedItemId(R.id.courseFragmentMenu);
                navController.navigate(R.id.courseFragmentMenu);
                break;
            case 3:
                setTitleText(3);
                bottomNavigationView.setSelectedItemId(R.id.ApiFragmentMenu);
                navController.navigate(R.id.ApiFragmentMenu);
                break;
            case 4:
                setTitleText(4);
                bottomNavigationView.setSelectedItemId(R.id.mccFragmentMenu);
                navController.navigate(R.id.mccFragmentMenu);
                break;
            case 5:
                setTitleText(5);
                bottomNavigationView.setSelectedItemId(R.id.msmFragmentMenu);
                navController.navigate(R.id.msmFragmentMenu);
                break;
            default:
                Toast.makeText(getContext(), "Wrong fragment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logOut) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("weather");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    logout();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }


        else if (id == R.id.menu_faq)
            startActivity(new Intent(getContext(), FAQActivity.class));

        else if (id == R.id.menu_about_us)
            startActivity(new Intent(getContext(), AboutUsActivity.class));

        return true;
    }

    private void logout() {
        onPause();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Event");
        FirebaseMessaging.getInstance().deleteToken();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }



    private void updateHeader() {
        TextView tvname = header.findViewById(R.id.tvHeaderName);
        ImageView ivprofile = header.findViewById(R.id.header_image);
        ImageView ivprofilebackground = header.findViewById(R.id.headerBackgroundImage);

        tvname.setText(userModel.getUserName());


        String imguri = userModel.getUserPhoto();
        if (imguri != null) {
//            Clear image
            loadUsingGlide(imguri, ivprofile, 1, 1);
//            Blurred Background
            loadUsingGlide(imguri, ivprofilebackground, 25, 5);
        }
    }


    private void loadUsingGlide(String imgurl, ImageView imageView, int radius, int sampling) {
        Glide.with(getContext()).
                load(imgurl).
                apply(RequestOptions.bitmapTransform(new BlurTransformation(radius, sampling)))
                .into(imageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        status("Offline");
        Log.d("status", "onPause: Offline");
    }

    @Override
    public void onResume() {
        super.onResume();
        status("Online");
        Log.d("status", "onResume: Online");

    }

    private void status(String status) {
        documentReference.update("status", status);
    }

}