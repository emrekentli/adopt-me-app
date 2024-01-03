package com.emrekentli.adoptme.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.dto.Gender;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.UserDto;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdDetailFragment extends Fragment {
    PhotoView mainImage;
    TextView adName, adDetail, adCountry, dateValue;
    String adId;
    TextView ageValue, sexValue, CategoryValue, cinsValue, memberName;
    LinearLayout wpButton;
    Button allAdsButton, buttonTelephone;
    TokenManager tokenManager;
    LinearLayout callButton;
    View view;
    String ownerId;
    String photo1url, photo2url;
    String userId;
    PostModel repo;
    CircleImageView profileImage;
    Dialog dialog;
    String name, email, id;
    String photoUrl;
    String telephone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim
        view = inflater.inflate(R.layout.ads_detail, container, false);
        setView();
        getExtras();
        setClick();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void getAccountDetails(UserDto user) {
        id = user.getId();
        name = user.getFullName();
        email = user.getEmail();
        photoUrl = user.getImage();


        userId = id;

        Log.i("Bilgi", id.toString());
    }


    public void setView() {
        mainImage = view.findViewById(R.id.adimagex);
        adName = view.findViewById(R.id.ad_name);
        adDetail = view.findViewById(R.id.ad_detail);
        adCountry = view.findViewById(R.id.country);
        dateValue = view.findViewById(R.id.date);
        memberName = view.findViewById(R.id.profileName);
        ageValue = view.findViewById(R.id.yasvalue);
        sexValue = view.findViewById(R.id.adsexvalue);
        CategoryValue = view.findViewById(R.id.adcategoryvalue);
        cinsValue = view.findViewById(R.id.cinsValue);
        allAdsButton = view.findViewById(R.id.allAdsBt);
        wpButton = view.findViewById(R.id.wpButton);
        buttonTelephone = view.findViewById(R.id.buttonTelephone);
        profileImage = view.findViewById(R.id.profileImage);
        callButton = view.findViewById(R.id.callButton);
    }

    public void getExtras() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            adId = bundle.getString("adid");
            getById(adId);

        }
    }


    public void setClick() {

        wpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String url = "https://api.whatsapp.com/send?phone=9" + telephone;
                sendIntent.setData(Uri.parse(url));
                if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(sendIntent);
                }

            }
        });


        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("1");
            }
        });

        allAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragmentsSearch(ownerId, SearchFragment.class);

            }
        });


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onCallBtnClick();

            }
        });


    }


    public void replaceFragmentsSearch(String id, Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("searchValue", "id");
        args.putString("userId", id);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment)
                .commit();
    }

    public void getById(String id) {
        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        tokenManager =  new TokenManager(getContext());
        String token = tokenManager.getToken();
        Call<ApiResponse<PostModel>> call = restInterface.getById("Bearer "+ token,id);

        call.enqueue(new Callback<ApiResponse<PostModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostModel>> call, Response<ApiResponse<PostModel>> response) {

                if (response.body() != null) {
                    repo = response.body().getData();
                    ownerId = repo.getOwner().getId();
                    Picasso.get().load(repo.getOwner().getImage()).fit().into(profileImage);


                    adCountry.setText(repo.getCity().getName());
                    adName.setText("\n" + repo.getName() + "\n");
                    getAccountDetails(repo.getOwner());
                    dateValue.setText(formatDate(repo.getCreated()));

                    telephone = repo.getOwner().getPhoneNumber();
                    String base64Image = repo.getMainImage();
                    byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                    Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    mainImage.setImageBitmap(originalBitmap);
                    photo1url = repo.getMainImage();
                    adDetail.setText("\n" + repo.getDescription() + "\n");
                    ageValue.setText(repo.getAge().toString());
                    sexValue.setText(repo.getGender().equals(Gender.MALE) ? "Erkek" : "Dişi");
                    CategoryValue.setText(repo.getAnimalType().getName());
                    cinsValue.setText(repo.getBreed().getName());
                    memberName.setText(repo.getOwner().getFullName());
                    buttonTelephone.setText("Mesaj Gönder +" + repo.getOwner().getPhoneNumber());
                    buttonTelephone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.setAction(Intent.ACTION_VIEW);
                            sendIntent.setPackage("com.whatsapp");
                            String url = "https://api.whatsapp.com/send?phone=" + telephone;
                            sendIntent.setData(Uri.parse(url));
                            if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                                startActivity(sendIntent);
                            } else {
                                Toast.makeText(getContext(), "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "İlan bulunamadı.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostModel>> call, Throwable t) {
                Log.e("Hata", t.toString());
            }
        });

    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(date);
    }

    private void onCallBtnClick() {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        } else {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                phoneCall();
                break;
        }
        if (permissionGranted) {
            phoneCall();

        } else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + telephone));
            getActivity().startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDialog(String url) {
        // custom dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);
        Button buy = (Button) dialog.findViewById(R.id.btnBuy);
        ImageView photo = (ImageView) dialog.findViewById(R.id.photo);

        if (url.equals("1")) {
            byte[] imageAsBytes = Base64.decode(photo1url.getBytes(), Base64.DEFAULT);
            Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            photo.setImageBitmap(originalBitmap);
        } else {
            byte[] imageAsBytes = Base64.decode(photo2url.getBytes(), Base64.DEFAULT);
            Bitmap originalBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            photo.setImageBitmap(originalBitmap);
        }


        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });

        // Buy Button
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Buy button action
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }
}