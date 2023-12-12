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
import com.emrekentli.adoptme.model.AdsModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditYourAdsFragment extends Fragment {

    private Uri filePath,filePath2;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageButton image1,image2;
    Integer placed1,placed2=null;
    TextView closePhoto1,closePhoto2;
    Integer selectedPlace = null;
    Boolean finishUpload= false;
    Boolean clickedPhoto=false;
    private List<AdsModel> addNewList;
    Integer adId;
    private String[] spinnerCityList={"Şehir Seçiniz.","Tüm Türkiye","Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
            "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    private String[] spinnerGenderList={"Cinsiyet seçiniz","Erkek","Dişi","Karışık"};
    private String[] spinnerCategoryList={"Kategori Seçiniz","Köpek","Kedi","Kuş","Balık","Tavşan","Hamster","Tavuk","Horoz","Kaz","Ördek","Sürüngenler"};
    Button pushButton;
    private Spinner spinnerIller,spinnerGender,spinnerCategory;

    private ArrayAdapter<String> dataAdapterForIller,dataAdapterForGender,dataAdapterForCategory;

    TextView genusTv,titleTv,detailTv,ageTv;
    GoogleSignInClient mGoogleSignInClient;
    String  userId;
    String userName;
    StorageReference ref;
    String photo1Url;
    String photo2Url;
    EditText telephoneTv;
    String iconPathFirebase=null;
    AdsModel repo;
    View view;
    String providerId,name,email,uid;
    Uri photoUrl;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // bu fragment'in layout'unu hazır hale getirelim
         view = inflater.inflate(R.layout.newadd_fragment, container, false);


        getExtras();
        setView();
        setOnClick();
        setCity();
        getAccount();





        return view;


    }

    public void getDetails(Integer id) {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<AdsModel> call = restInterface[0].getAdDetails(id);
        call.enqueue(new Callback<AdsModel>() {
            @Override
            public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {

                repo= response.body();




                pushButton.setText("YAYINA GÖNDER");



                selectValue(spinnerIller,repo.getCountry());
                selectValue(spinnerCategory,repo.getAdCategory());
                selectValue(spinnerGender,repo.getAdSex());


                genusTv.setText(repo.getAdAltcategory());
                titleTv.setText(repo.getAdName());
                detailTv.setText(repo.getAdDetail());
                ageTv.setText(repo.getAdAge());
                telephoneTv.setText(repo.getAdOwnertelephone());


                photo1Url=repo.getAdImage();
                Picasso
                        .get()
                        .load(repo.getAdImage())
                        .fit()
                        .into(image1);

                placed1=1;
                closePhoto1.setVisibility(View.VISIBLE);


                if (repo.getAdImage2()==null) {

                  //  adimagex2.setVisibility(View.GONE);

                    placed2=0;
                } else {

                    closePhoto2.setVisibility(View.VISIBLE);
                    Picasso
                            .get()
                            .load(repo.getAdImage2())
                            .fit()
                            .into(image2);

                    placed2=1;
                    photo2Url=repo.getAdImage2();

                }





            }

            @Override
            public void onFailure(Call<AdsModel> call, Throwable t) {
                Log.e("Hata",t.toString());
            }
        });

    }

    public void getExtras() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            adId = bundle.getInt("adid");
            getDetails(adId);

        }
    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    public void setOnClick() {
        closePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placed1=0;
                image1.setImageDrawable(null);
                image1.setBackgroundResource(R.drawable.upload);
                // Toast.makeText(getContext(), placed1.toString(), Toast.LENGTH_SHORT).show();
                closePhoto1.setVisibility(View.INVISIBLE);
                photo1Url=null;
            }
        });

        closePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image2.setImageDrawable(null);
                image2.setBackgroundResource(R.drawable.upload);
                placed2=0;
                // Toast.makeText(getContext(), placed2.toString(), Toast.LENGTH_SHORT).show();

                closePhoto2.setVisibility(View.INVISIBLE);

                photo2Url=null;
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPlace=1;

                placed1=1;
                photo1Url=null;
                SelectImage();
                closePhoto1.setVisibility(View.VISIBLE);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPlace=2;
                placed2=1;
                photo2Url=null;
                SelectImage();
                closePhoto2.setVisibility(View.VISIBLE);
            }
        });









        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (formControl()) {

                    if (clickedPhoto==true) {

                        if ((placed1 == 0) & (placed2 == 1)) {


                            if (photo2Url != null) {

                                pushEdit(spinnerIller.getSelectedItem().toString(),
                                        spinnerCategory.getSelectedItem().toString(),
                                        spinnerGender.getSelectedItem().toString(),
                                        genusTv.getText().toString(),
                                        ageTv.getText().toString(),
                                        titleTv.getText().toString(),
                                        detailTv.getText().toString(),
                                        photo2Url,
                                        null);


                            } else {

                                uploadImage(1, filePath2, false);


                            }


                        } else if ((placed1 == 1) & (placed2 == 0)) {

                            if (photo1Url == null) {

                                uploadImage(1, filePath, false);

                            } else {

                                uploadImage(1, filePath2, false);

                            }


                        } else if ((placed1 == 1) & (placed2 == 1)) {

                            if ((photo1Url == null) & (photo2Url == null)) {


                                uploadImage(2, filePath, false);
                                uploadImage(2, filePath, true);


                            } else if ((photo1Url != null) & (photo2Url == null)) {


                                uploadImage(2, filePath2, true);


                            } else if ((photo1Url == null) & (photo2Url != null)) {

                                photo2Url = repo.getAdImage2().toString();
                                uploadImage(1, filePath, true);


                            }


                        } else if ((placed1 == 0) & (placed2 == 0)) {

                            Toast.makeText(getContext(), "Lütfen fotoğraf yükleyiniz.", Toast.LENGTH_SHORT).show();


                        }

                    } else if (clickedPhoto==false) {

                        pushEdit(spinnerIller.getSelectedItem().toString(),
                                spinnerCategory.getSelectedItem().toString(),
                                spinnerGender.getSelectedItem().toString(),
                                genusTv.getText().toString(),
                                ageTv.getText().toString(),
                                titleTv.getText().toString(),
                                detailTv.getText().toString(),
                                repo.getAdImage(),
                                repo.getAdImage2());

                    }
                } else {



                }

            }
        });


    }
    public void setView() {

        spinnerIller = view.findViewById(R.id.spinnerCity);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        pushButton = view.findViewById(R.id.pushButton);
        genusTv = view.findViewById(R.id.ad_altcategory);
        detailTv =view.findViewById(R.id.ad_detail);
        titleTv = view.findViewById(R.id.ad_title);
        ageTv = view.findViewById(R.id.ad_age);
        placed1=0;
        placed2=0;
        closePhoto2 = view.findViewById(R.id.closePhoto2);
        closePhoto1= view.findViewById(R.id.closePhoto1);
        telephoneTv= view.findViewById(R.id.telephone);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        image1=view.findViewById(R.id.photo1);
        image2=view.findViewById(R.id.photo2);








        closePhoto1.setVisibility(View.INVISIBLE);
        closePhoto2.setVisibility(View.INVISIBLE);




    }


    public void getAccount() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();


                userId = profile.getUid();
                userName = profile.getDisplayName();
                Log.i("Bilgi",uid.toString());
            }
        }
    }


    private void SelectImage()
    {
         clickedPhoto=true;

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

            if (selectedPlace == 1) {

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


            } else {


                filePath2 = data.getData();
                try {

                    // Setting image on image view using Bitmap
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getActivity().getContentResolver(),
                                    filePath2);
                    image2.setImageDrawable(null);
                    image2.setImageBitmap(bitmap);

                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }



            }





        }
    }

    public void pushEdit(String city, String category,String gender,String altCategory,String age,String tittle,String detail,String photo1,String photo2) {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<AdsModel> call = restInterface[0].editAds(String.valueOf(repo.getId()),repo.getAdOwnerid(),repo.getAdOwnername(),telephoneTv.getText().toString(),"Sahiplendirme",tittle,detail,category,altCategory,age,1,city,"",photo1,photo2,"","03.07.2021",gender,repo.getAdViews());
        call.enqueue(new Callback<AdsModel>() {
            @Override
            public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {
                if (response.isSuccessful()) {

                    replaceFragments(InboxFragment.class);

                    Toast.makeText(getActivity(), "İlan incelemeye gönderildi.", Toast.LENGTH_LONG).show();

                } else {



                }

            }

            @Override
            public void onFailure(Call<AdsModel> call, Throwable t) {
                Log.e("Hata",t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // UploadImage method
    private void uploadImage(Integer Mode,Uri filePathValue,Boolean finishUploadValue) {


        if (Mode == 1) {
            if (filePathValue != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(getContext());
                progressDialog.setTitle("Resim yükleniyor...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "photos/"
                                        + UUID.randomUUID().toString());

                UploadTask uploadTask = ref.putFile(filePathValue);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Uri> task) {
                                                 if (task.isSuccessful()) {
                                                     Uri downloadUri = task.getResult();
                                                     iconPathFirebase = downloadUri.toString();




                                                     pushEdit(spinnerIller.getSelectedItem().toString(),
                                                             spinnerCategory.getSelectedItem().toString(),
                                                             spinnerGender.getSelectedItem().toString(),
                                                             genusTv.getText().toString(),
                                                             ageTv.getText().toString(),
                                                             titleTv.getText().toString(),
                                                             detailTv.getText().toString(),
                                                             iconPathFirebase,
                                                             photo2Url);

                                                 } else {
                                                     // Handle failures
                                                     // ...
                                                 }


                                                 // Image uploaded successfully
                                                 // Dismiss dialog
                                                 progressDialog.dismiss();
                                                 Toast
                                                         .makeText(getContext(),
                                                                 "Resim yüklendi.",
                                                                 Toast.LENGTH_SHORT)
                                                         .show();


                                             }
                                         }
                );


            }


        } else if (Mode == 2) {

            ref
                    = storageReference
                    .child(
                            "photos/"
                                    + UUID.randomUUID().toString());

            UploadTask uploadTask = ref.putFile(filePathValue);


            if (filePathValue != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog
                        = new ProgressDialog(getContext());
                progressDialog.setTitle("Resim yükleniyor...");
                progressDialog.show();

                // Defining the child of storageReference

                if (finishUploadValue == false) {


                    StorageReference ref
                            = storageReference
                            .child(
                                    "photos/"
                                            + UUID.randomUUID().toString());

                    uploadTask = ref.putFile(filePathValue);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                photo1Url = downloadUri.toString();

                                progressDialog.dismiss();
                                Toast
                                        .makeText(getContext(),
                                                "Resim yüklendi.",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });


                } else if (finishUploadValue) {


                    ref = storageReference.child("photos/" + UUID.randomUUID().toString());


                    // adding listeners on upload


                    uploadTask = ref.putFile(filePathValue);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                iconPathFirebase = downloadUri.toString();

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(getContext(),
                                                "Resim yüklendi.",
                                                Toast.LENGTH_SHORT)
                                        .show();


                                pushEdit(spinnerIller.getSelectedItem().toString(),
                                        spinnerCategory.getSelectedItem().toString(),
                                        spinnerGender.getSelectedItem().toString(),
                                        genusTv.getText().toString(),
                                        ageTv.getText().toString(),
                                        titleTv.getText().toString(),
                                        detailTv.getText().toString(),
                                        photo1Url,
                                        iconPathFirebase);

                                                        }



                        }

                    });





                }


            }


        }


    };






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
                //Hangi il seçilmişse onun ilçeleri adapter'e ekleniyor.
                /* if(parent.getSelectedItem().toString().equals(iller[0]))
                    dataAdapterForIlceler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,ilceler0);
                else if(parent.getSelectedItem().toString().equals(iller[1]))
                    dataAdapterForIlceler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,ilceler1);

                dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIlceler.setAdapter(dataAdapterForIlceler); */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*        spinnerIlceler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Seçilen il ve ilçeyi ekranda gösteriyoruz.
            //    sehirAdi = spinnerIller.getSelectedItem().toString();
              //  ilceAdi = parent.getSelectedItem().toString();

                //eczaneVericek(sehirAdi,ilceAdi);
                //Toast.makeText(getActivity(), sehirAdi + ilceAdi, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); */

    }

    private void getUrlAsync (String parametre){
        // Points to the root reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("photos/");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                //do something with downloadurl
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
