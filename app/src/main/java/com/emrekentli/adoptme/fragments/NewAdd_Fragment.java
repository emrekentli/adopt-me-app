package com.emrekentli.adoptme.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.dto.AnimalTypeDto;
import com.emrekentli.adoptme.model.dto.BreedDto;
import com.emrekentli.adoptme.model.dto.CityDto;
import com.emrekentli.adoptme.model.dto.DistrictDto;
import com.emrekentli.adoptme.model.dto.Gender;
import com.emrekentli.adoptme.model.request.PostRequest;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.DataResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAdd_Fragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 22;
    ImageButton image1;
    TokenManager tokenManager;
    TextView closePhoto1;
    Boolean finishUpload = false;
    private List<PostModel> addNewList;
    private List<CityDto> cities;
    private List<DistrictDto> districts;
    private List<AnimalTypeDto> animalTypes;
    private List<BreedDto> breeds;
    private List<String> spinnerCityList = new ArrayList<>(Arrays.asList("Şehir Seçiniz."));
    private List<String> spinnerDistrictList = new ArrayList<>(Arrays.asList("İlçe Seçiniz."));
    private List<String> spinnerGenderList = Arrays.asList("Cinsiyet seçiniz", "Erkek", "Dişi");
    private List<String> spinnerCategoryList = new ArrayList<>(Arrays.asList("Kategori Seçiniz"));
    private List<String> spinnerBreedList = new ArrayList<>(Arrays.asList("Cins Seçiniz"));
    Button pushButton;

    Uri filePath;
    private Spinner spinnerIller, spinnerGender, spinnerCategory, spinnerBreed, spinnerDistrict;

    private ArrayAdapter<String> dataAdapterForIller, dataAdapterForGender, dataAdapterForCategory, dataAdapterForBreed, dataAdapterForDistricts;
    TextView titleTv, detailTv, ageTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // bu fragment'in layout'unu hazır hale getirelim
        View view = inflater.inflate(R.layout.newadd_fragment, container, false);
        spinnerIller = view.findViewById(R.id.spinnerCity);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerBreed = view.findViewById(R.id.spinnerBreed);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);

        pushButton = view.findViewById(R.id.pushButton);
        detailTv = view.findViewById(R.id.ad_detail);
        titleTv = view.findViewById(R.id.ad_title);
        ageTv = view.findViewById(R.id.ad_age);
        closePhoto1 = view.findViewById(R.id.closePhoto1);

        tokenManager = new TokenManager(getContext());
        image1 = view.findViewById(R.id.photo1);

        closePhoto1.setVisibility(View.INVISIBLE);

        getCities();
        getAnimalTypes();

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
                    addPost(spinnerIller.getSelectedItem().toString(), spinnerDistrict.getSelectedItem().toString(),spinnerCategory.getSelectedItem().toString(), spinnerGender.getSelectedItem().toString(), spinnerBreed.getSelectedItem().toString(), ageTv.getText().toString(), titleTv.getText().toString(), detailTv.getText().toString());
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
                    for (CityDto city : cities) {
                        spinnerCityList.add(city.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<CityDto>>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDistricts(String cityName) {

        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        String id = "";
        for (CityDto city : cities) {
            if (city.getName().equals(cityName)) {
                id = city.getId();
                break;
            }
        }
        Call<ApiResponse<DataResponse<DistrictDto>>> call = restInterface.getDistricts("Bearer " + tokenManager.getToken(), id);
        call.enqueue(new Callback<ApiResponse<DataResponse<DistrictDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<DistrictDto>>> call, Response<ApiResponse<DataResponse<DistrictDto>>> response) {
                if (response.isSuccessful()) {
                    districts = response.body().getData().getItems();
                    for (DistrictDto district : districts) {
                        spinnerDistrictList.add(district.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<DistrictDto>>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAnimalTypes() {
        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<DataResponse<AnimalTypeDto>>> call = restInterface.getAnimalTypes("Bearer " + tokenManager.getToken());
        call.enqueue(new Callback<ApiResponse<DataResponse<AnimalTypeDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<AnimalTypeDto>>> call, Response<ApiResponse<DataResponse<AnimalTypeDto>>> response) {
                if (response.isSuccessful()) {
                    animalTypes = response.body().getData().getItems();
                    for (AnimalTypeDto animalType : animalTypes) {
                        spinnerCategoryList.add(animalType.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<AnimalTypeDto>>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getBreeds(String animalType) {
        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        String id = "";
        for (AnimalTypeDto animal : animalTypes) {
            if (animal.getName().equals(animalType)) {
                id = animal.getId();
                break;
            }
        }
        Call<ApiResponse<DataResponse<BreedDto>>> call = restInterface.getBreeds("Bearer " + tokenManager.getToken(), id);
        call.enqueue(new Callback<ApiResponse<DataResponse<BreedDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<BreedDto>>> call, Response<ApiResponse<DataResponse<BreedDto>>> response) {
                if (response.isSuccessful()) {
                    breeds = response.body().getData().getItems();
                    for (BreedDto breed : breeds) {
                        spinnerBreedList.add(breed.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<BreedDto>>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void addPost(String city, String district, String animalType, String gender, String breed, String age, String title, String description) {
        String cityId = "";
        String breedId = "";
        String animalTypeId = "";
        String districtId = "";
        Gender genderValue = Gender.getFromValue(gender);
        for (CityDto cityDto : cities) {
            if (cityDto.getName().equals(city)) {
                cityId = cityDto.getId();
                break;
            }
        }
        for (DistrictDto districtDto : districts) {
            if (districtDto.getName().equals(district)) {
                districtId = districtDto.getId();
                break;
            }
        }
      for (BreedDto breedDto : breeds) {
            if (breedDto.getName().equals(breed)) {
                breedId = breedDto.getId();
                break;
            }
        }
        for (AnimalTypeDto animalTypeDto : animalTypes) {
            if (animalTypeDto.getName().equals(animalType)) {
                animalTypeId = animalTypeDto.getId();
                break;
            }
        }
        final Interface restInterface = ApiClient.getClient().create(Interface.class);
        PostRequest request = new PostRequest();
        request.setAge(Integer.parseInt(age));
        request.setAnimalBreedId(breedId);
        request.setAnimalTypeId(animalTypeId);
        request.setCityId(cityId);
        request.setDescription(description);
        request.setDistrictId(districtId);
        request.setGender(genderValue);
        request.setTitle(title);
        request.setName(title);

        Call<ApiResponse<PostModel>> call = restInterface.addPost("Bearer " + tokenManager.getToken(), request);
        call.enqueue(new Callback<ApiResponse<PostModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<PostModel>> call, Response<ApiResponse<PostModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "İlan başarıyla eklendi.", Toast.LENGTH_SHORT).show();
                    replaceFragments(InboxFragment.class);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PostModel>> call, Throwable t) {
                Log.e("Hata", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void SelectImage() {

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
    private void uploadImage(Integer Mode, Uri filePathValue, Boolean finishUploadValue) {


    }


    public void setCity() {

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIller = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerCityList);
        dataAdapterForCategory = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerCategoryList);
        dataAdapterForGender = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerGenderList);
        dataAdapterForBreed = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerBreedList);
        dataAdapterForDistricts = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerDistrictList);
        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForDistricts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIller.setAdapter(dataAdapterForIller);
        spinnerGender.setAdapter(dataAdapterForGender);
        spinnerCategory.setAdapter(dataAdapterForCategory);
        spinnerBreed.setAdapter(dataAdapterForBreed);
        spinnerDistrict.setAdapter(dataAdapterForDistricts);

        // Listelerden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinnerIller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position != 0) {
                    getDistricts(spinnerIller.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position != 0) {
                    getBreeds(spinnerCategory.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public boolean formControl() {

        boolean control = true;

        if (TextUtils.isEmpty(titleTv.getText())) {
            titleTv.setError("İlan Başlığı boş bırakılamaz.");
            control = false;
        }

        if (TextUtils.isEmpty(detailTv.getText())) {
            detailTv.setError("Açıklama kısmı boş bırakılamaz.");
            control = false;
        }

        if (spinnerBreed.getSelectedItem().toString().equals("Cins Seçiniz")) {
            TextView errorText = (TextView) spinnerBreed.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen kategori seçiniz.");
            control = false;
        }

        if (TextUtils.isEmpty(ageTv.getText())) {
            ageTv.setError("Yaş kısmı boş bırakılamaz.");
            control = false;
        }

        if (spinnerCategory.getSelectedItem().toString().equals("Kategori Seçiniz")) {
            TextView errorText = (TextView) spinnerCategory.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen kategori seçiniz.");
            control = false;
        }

        if (spinnerIller.getSelectedItem().toString().equals("Şehir Seçiniz.")) {
            TextView errorText = (TextView) spinnerIller.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen Şehir seçiniz.");
            control = false;
        }

        if (spinnerGender.getSelectedItem().toString().equals("Cinsiyet seçiniz")) {
            TextView errorText = (TextView) spinnerGender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Lütfen Şehir seçiniz.");
            control = false;
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
