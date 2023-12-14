package com.emrekentli.adoptme.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.controller.AdsAdaptor;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsFragment extends Fragment {
    private GridView lastAds,dogAds,birdAds,otherAds;
    private AdsAdaptor adapter;
    private List<PostModel> lastList,dogsList,catsList,lastothers;
    LinearLayout loading;
    Button searchBt,showDogAdsBt,showCatAdsBt,showOtherAdsBt,showLastAdsBt,wpButton,allAdsButton;
    EditText searchTv;
    TokenManager tokenManager;
    LinearLayout ly1,ly2,ly3,ly4;
    private String[] iller={"Hepsi","Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "İçel (Mersin)", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    private Spinner spinnerIller;

    private ArrayAdapter<String> dataAdapterForIller;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim
        tokenManager = new TokenManager(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_ads, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setViews();
        setOnclicks();




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setCity();
        lastDogAds();
     //   loading.setVisibility(View.GONE);


    }




    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setViews() {
        spinnerIller = view.findViewById(R.id.spinner1);
        lastAds=(GridView)view.findViewById(R.id.adsView);
        dogAds=(GridView)view.findViewById(R.id.adsView2);
        birdAds=(GridView)view.findViewById(R.id.adsView3);
        otherAds=(GridView)view.findViewById(R.id.adsView4);
        searchBt = view.findViewById(R.id.buttonSearch);
        searchTv = view.findViewById(R.id.searchTv);
        showDogAdsBt = view.findViewById(R.id.showDogAdsBt);
        showCatAdsBt = view.findViewById(R.id.showCatsAdsBt);
        showOtherAdsBt = view.findViewById(R.id.showOthersAdsBt);
        showLastAdsBt = view.findViewById(R.id.showLastAdsBt);
        loading = getActivity().findViewById(R.id.loading);
        ly1 = view.findViewById(R.id.ly1);
        ly2 = view.findViewById(R.id.ly2);
        ly3 = view.findViewById(R.id.ly3);
        ly4 = view.findViewById(R.id.ly4);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout action;
        BottomNavigationView menu;
        action = getActivity().findViewById(R.id.action);
        menu = getActivity().findViewById(R.id.bottom_navigation);
        menu.setClickable(true);


        action.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);




    }

    public void setOnclicks() {



        showDogAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragmentsSearch("köpek",SearchFragment.class);

            }
        });
        showCatAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragmentsSearch("kedi",SearchFragment.class);

            }
        });
        showOtherAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragmentsSearch("others",SearchFragment.class);

            }
        });
        showLastAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                replaceFragmentsSearch("last",SearchFragment.class);

            }
        });


        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchTv.setFocusable(false);
                replaceFragmentsSearch(searchTv.getText().toString(),SearchFragment.class);

            }
        });


        lastAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // DO something
                PostModel repo = lastList.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());


            }
        });

        dogAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // DO something
                PostModel repo = dogsList.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());


            }
        });

        birdAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // DO something
                PostModel repo = catsList.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());


            }
        });

        otherAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // DO something
                PostModel repo = lastothers.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());


            }
        });


    }

    public void lastAds() {

    final Interface[] restInterface = new Interface[1];
    restInterface[0] = ApiClient.getClient().create(Interface.class);
    Call<List<PostModel>> call = restInterface[0].getAds("Bearer " +tokenManager.getToken());
    call.enqueue(new Callback<List<PostModel>>() {
        @Override
        public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
            lastList=response.body();


            if (getActivity()!=null){
                adapter=new AdsAdaptor(getContext(),R.layout.lastads_row,lastList);
                Log.i("Bilgi",response.toString());
                lastAds.setAdapter(adapter);
            }


        }

        @Override
        public void onFailure(Call<List<PostModel>> call, Throwable t) {
            Log.e("Hata",t.toString());
        }
    });

}

    public void lastDogAds() {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<DataResponse<PostModel>>> call = restInterface[0].getDogAds("Bearer " +tokenManager.getToken());
        call.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<PostModel>>> call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                dogsList=response.body().getData().getItems();
                if (getActivity()!=null){
                adapter=new AdsAdaptor(getContext(),R.layout.lastads_row,dogsList);
                Log.i("Bilgi",response.toString());
                dogAds.setAdapter(adapter); }
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.e("Hata",t.toString());
            }
        });

    }

    public void lastBirdAds() {
 /*
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<List<AdsModel>> call = restInterface[0].getBirdAds();
        call.enqueue(new Callback<List<AdsModel>>() {
            @Override
            public void onResponse(Call<List<AdsModel>> call, Response<List<AdsModel>> response) {
                listDataList=response.body();
                adapter=new AdsAdaptor(getContext(),R.layout.lastads_row,listDataList);
                Log.i("Bilgi",response.toString());
                birdAds.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<AdsModel>> call, Throwable t) {
                Log.e("Hata",t.toString());
            }
        }); */

    }

    public void lastCatAds() {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<List<PostModel>> call = restInterface[0].getCatAds("Bearer " +tokenManager.getToken());
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                catsList=response.body();
                if (getActivity()!=null){
                adapter=new AdsAdaptor(getContext(),R.layout.lastads_row,catsList);
                Log.i("Bilgi",response.toString());
                birdAds.setAdapter(adapter);}
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.e("Hata",t.toString());
            }
        });

    }


    public void lastOtherAds() {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<List<PostModel>> call = restInterface[0].getOtherAds("Bearer " +tokenManager.getToken());
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                lastothers=response.body();

                if (getActivity()!=null){
                adapter=new AdsAdaptor(getContext(),R.layout.lastads_row,lastothers);
                Log.i("Bilgi",response.toString());
                otherAds.setAdapter(adapter);
            } }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.e("Hata",t.toString());
            }
        });

    }

    public void replaceFragmentsSearch(String value,Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("searchValue",value);
        args.putString("city",spinnerIller.getSelectedItem().toString());
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
        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }


    public void replaceFragmentsAds(Class fragmentClass, String adid) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        Bundle args = new Bundle();
        args.putString("adid",adid);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragmentLayout, fragment)
                .commit();
    }




    public void setCity() {


       // spinnerIlceler = (Spinner) getActivity().findViewById(R.id.spinner2);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIller = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, iller);
        //dataAdapterForIlceler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,ilceler0);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIller.setAdapter(dataAdapterForIller);
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

    }



}