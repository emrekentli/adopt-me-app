package com.emrekentli.adoptme.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.dto.CityDto;
import com.emrekentli.adoptme.model.dto.Gender;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAdd_Fragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 22;
    ImageButton image1;
    TokenManager tokenManager;
    TextView closePhoto1;
    Boolean finishUpload= false;
    private List<PostModel> addNewList;
    private List<CityDto> cities;
    private String[] spinnerCityList={"Şehir Seçiniz.","Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
            "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    private String[] spinnerGenderList={"Cinsiyet seçiniz","Erkek","Dişi"};
    private String[] spinnerCategoryList={"Kategori Seçiniz","Köpek","Kedi","Kuş","Balık","Tavşan","Hamster","Tavuk","Horoz","Kaz","Ördek","Sürüngenler"};
    Button pushButton;

    Uri filePath;
    private Spinner spinnerIller,spinnerGender,spinnerCategory;

    private ArrayAdapter<String> dataAdapterForIller,dataAdapterForGender,dataAdapterForCategory;
    TextView genusTv,titleTv,detailTv,ageTv;
    String photo1Url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // bu fragment'in layout'unu hazır hale getirelim
        View view = inflater.inflate(R.layout.newadd_fragment, container, false);
        spinnerIller = view.findViewById(R.id.spinnerCity);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        pushButton = view.findViewById(R.id.pushButton);
        genusTv = view.findViewById(R.id.ad_altcategory);
        detailTv =view.findViewById(R.id.ad_detail);
        titleTv = view.findViewById(R.id.ad_title);
        ageTv = view.findViewById(R.id.ad_age);
        closePhoto1= view.findViewById(R.id.closePhoto1);

        tokenManager = new TokenManager(getContext());
        image1=view.findViewById(R.id.photo1);

        closePhoto1.setVisibility(View.INVISIBLE);

        getCities();
        closePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image1.setImageDrawable(null);
                image1.setBackgroundResource(R.drawable.upload);
               // Toast.makeText(getContext(), placed1.toString(), Toast.LENGTH_SHORT).show();
                closePhoto1.setVisibility(View.INVISIBLE);
            }
        });


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
                closePhoto1.setVisibility(View.VISIBLE);
            }
        });

        setCity();
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (formControl()) {

                } else {




                }

            }
        });



        return view;

    }
    public void getCities() {

        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<DataResponse<CityDto>>> call = restInterface.getCities("Bearer " + tokenManager.getToken());
        call.enqueue(new Callback<ApiResponse<DataResponse<CityDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<CityDto>>> call, Response<ApiResponse<DataResponse<CityDto>>> response) {
                if (response.isSuccessful()) {
                        cities = response.body().getData().getItems();

                }
            }
            @Override
            public void onFailure(Call<ApiResponse<DataResponse<CityDto>>> call, Throwable t) {
                Log.e("Hata",t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void addNewAd( String city, String animalType,String gender,String breed,String age,String title,String description,String photo1) {
//
//        final Interface[] restInterface = new Interface[1];
//        restInterface[0] = ApiClient.getClient().create(Interface.class);
//        Call<PostModel> call = restInterface[0].addNewAdd(title,description,animalType,breed,age,null,city,null,photo1,breed, Gender.valueOf(gender));
//        call.enqueue(new Callback<PostModel>() {
//            @Override
//            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
//                if (response.isSuccessful()) {
//
//                    replaceFragments(InboxFragment.class);
//
//                    Toast.makeText(getActivity(), "İlan incelemeye gönderildi.", Toast.LENGTH_LONG).show();
//
//                } else {
//
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<PostModel> call, Throwable t) {
//                Log.e("Hata",t.toString());
//                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data


                filePath = data.getData();
                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getActivity().getContentResolver(),
                                    filePath);

                    image1.setImageDrawable(null);
                    image1.setImageBitmap(bitmap);

                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
        }
    }

    // UploadImage method
    private void uploadImage(Integer Mode,Uri filePathValue,Boolean finishUploadValue) {




    }






    public void setCity() {

        // spinnerIlceler = (Spinner) getActivity().findViewById(R.id.spinner2);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIller = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerCityList);
        dataAdapterForCategory = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerCategoryList);
        dataAdapterForGender = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerGenderList);
        //dataAdapterForIlceler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,ilceler0);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIller.setAdapter(dataAdapterForIller);
        spinnerGender.setAdapter(dataAdapterForGender);
        spinnerCategory.setAdapter(dataAdapterForCategory);
        //spinnerIlceler.setAdapter(dataAdapterForIlceler);

        // Listelerden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinnerIller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public boolean formControl() {

        boolean control= true;

        if(TextUtils.isEmpty(titleTv.getText())) {
            titleTv.setError("İlan Başlığı boş bırakılamaz.");
             control= false;
        }

        if(TextUtils.isEmpty(detailTv.getText())) {
            detailTv.setError("Açıklama kısmı boş bırakılamaz.");
             control= false;
        }

        if(TextUtils.isEmpty(genusTv.getText())) {
            genusTv.setError("Cins kısmı boş bırakılamaz.");
             control= false;
        }

        if(TextUtils.isEmpty(ageTv.getText())) {
            genusTv.setError("Yaş kısmı boş bırakılamaz.");
            control= false;
        }

        if (spinnerCategory.getSelectedItem().toString().equals("Kategori Seçiniz")) {
            TextView errorText = (TextView)spinnerCategory.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen kategori seçiniz.");
            control= false;
        }

        if (spinnerIller.getSelectedItem().toString().equals("Şehir Seçiniz.")) {
            TextView errorText = (TextView)spinnerIller.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen Şehir seçiniz.");
            control= false;
        }

        if (spinnerGender.getSelectedItem().toString().equals("Cinsiyet seçiniz")) {
            TextView errorText = (TextView)spinnerGender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen Şehir seçiniz.");
            control= false;
        }



        return control;

    }


    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }

}
