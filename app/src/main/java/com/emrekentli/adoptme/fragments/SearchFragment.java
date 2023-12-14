package com.emrekentli.adoptme.fragments;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.emrekentli.adoptme.R;
import com.emrekentli.adoptme.api.ApiClient;
import com.emrekentli.adoptme.api.Interface;
import com.emrekentli.adoptme.controller.SearchAdaptor;
import com.emrekentli.adoptme.database.TokenManager;
import com.emrekentli.adoptme.model.PostModel;
import com.emrekentli.adoptme.model.response.ApiResponse;
import com.emrekentli.adoptme.model.response.DataResponse;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private ListView founds;
    private SearchAdaptor adapter;
    TokenManager tokenManager;
    Integer profilMi=0;
    private List<PostModel> listDataList;

    private Interface apiInterface;
    final SearchAdaptor listViewAdapter[]=new SearchAdaptor[1];

    ArrayList<PostModel> foundData;
    ArrayList<PostModel> foundDataOwn;
    String city;
    ListView list;
    String searchValue=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bu fragment'in layout'unu hazır hale getirelim
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        tokenManager = new TokenManager(getContext());


        foundData = new ArrayList<>();
        founds = view.findViewById(R.id.myAdsListView);
        list = view.findViewById(R.id.myAdsListView);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            if (profilMi==1) {

                PostModel repo = listDataList.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());

            } else {

                PostModel repo = foundData.get(position);

                replaceFragmentsAds(AdDetailFragment.class,repo.getId());

            }


            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getExtras();



    }



    public void getExtras() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            searchValue = bundle.getString("searchValue");
            String userId = bundle.getString("userId");
            city = bundle.getString("city");
            switch (searchValue) {


                case "id":
                   getOwnAds(userId);
                   profilMi=1;
                   break;
                case "last":
                getLast();
                break;
                case "köpek":
                    getSearch("köpek");
                    break;
                case "kedi":
                    getSearch("kedi");
                    break;
                case "kuş":
                    getSearch("kuş");
                    break;
                case "balık":
                    getSearch("balık");
                    break;
                case "tavşan":
                    getSearch("tavşan");
                    break;
                case "hamster":
                    getSearch("hamster");
                    break;
                case "tavuk":
                    getSearch("tavuk");
                    break;
                case "horoz":
                    getSearch("horoz");
                    break;
                case "kaz":
                    getSearch("kaz");
                    break;
                case "ördek":
                    getSearch("ördek");
                    break;
                case "sürüngenler":
                    getSearch("sürüngen");
                    break;
                case "others":
                    getOthers();
                    break;

                    default:

                        if (city.equals("Hepsi")) {
                            filterWithTitle(searchValue);

                        } else  {
                            filterWithTitleAndCity(searchValue,city);
                        }


                    break;

            }


        } else {



        }
    }

    private void filterWithTitleAndCity(String searchValue, String city) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);

        Call<ApiResponse<DataResponse<PostModel>>> repos;
        repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),searchValue,null, city);

        repos.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(  Call<ApiResponse<DataResponse<PostModel>>>  call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                if (response.body() != null) {
                    foundData.addAll(response.body().getData().getItems());
                    //  Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı...", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

                Log.i("Bilgi",response.toString());
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });
    }

    private void filterWithTitle(String searchValue) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);

        Call<ApiResponse<DataResponse<PostModel>>> repos;
        repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),searchValue,null, null);

        repos.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(  Call<ApiResponse<DataResponse<PostModel>>>  call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                if (response.body() != null) {
                    foundData.addAll(response.body().getData().getItems());
                    //  Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı...", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

                Log.i("Bilgi",response.toString());
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });
    }


    public void getSearch(String searchValue) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);

        Call<ApiResponse<DataResponse<PostModel>>> repos;
        if (city==null) {
            repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),searchValue,null,null);
        } else if (city.equals("Hepsi"))  {
            repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),null,searchValue,null);
        } else {
            repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),null,searchValue, city);
        }

        repos.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(  Call<ApiResponse<DataResponse<PostModel>>>  call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                if (response.body() != null) {
                   foundData.addAll(response.body().getData().getItems());
                  //  Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı...", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

                Log.i("Bilgi",response.toString());
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });

    }

    public void getSearchCity(String searchValue,String city) {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        Call<ApiResponse<DataResponse<PostModel>>> repos = restInterface[0].getAdsSearchWithCity("Bearer " +tokenManager.getToken(),null,searchValue,city);
        repos.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(  Call<ApiResponse<DataResponse<PostModel>>>  call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                if (response.body() != null) {
                    foundData.addAll(response.body().getData().getItems());
                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı.", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

                Log.i("Bilgi",response.toString());

            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });

    }

    public void getLast() {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);


        Call<ApiResponse<DataResponse<PostModel>>>  repos = restInterface[0].getAllPosts("Bearer " +tokenManager.getToken());

        repos.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(  Call<ApiResponse<DataResponse<PostModel>>>  call, Response<ApiResponse<DataResponse<PostModel>>> response) {
                if (response.body() != null) {


                    foundData.addAll(response.body().getData().getItems());
                    Log.i("Bilgi",response.toString());
                   //   Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı.", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });

    }


    public void getOwnAds(String userId) {

        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);
        tokenManager = new TokenManager(getContext());
        String token = tokenManager.getToken();
        Call<ApiResponse<DataResponse<PostModel>>> call = restInterface[0].getOwnAds("Bearer " + token);
        call.enqueue(new Callback<ApiResponse<DataResponse<PostModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<DataResponse<PostModel>>> call, Response<ApiResponse<DataResponse<PostModel>>> response) {

                listDataList=response.body().getData().getItems();
                adapter=new SearchAdaptor(getContext(),R.layout.search_row,listDataList);
                Log.i("Bilgi",response.toString());
                founds.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ApiResponse<DataResponse<PostModel>>> call, Throwable t) {

                Toast.makeText(getContext(), "Hiç ilan paylaşmadınız.", Toast.LENGTH_SHORT).show();


            }
        });

    }

    public void getOthers() {
        final Interface[] restInterface = new Interface[1];
        restInterface[0] = ApiClient.getClient().create(Interface.class);

        Call<List<PostModel>>  repos = restInterface[0].getOtherAdsSearch();

        repos.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(  Call<List<PostModel>>  call, Response<List<PostModel>> response) {
                if (response.body() != null) {


                    foundData.addAll(response.body());

                    //   Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                }


                if (foundData.size() > 0) {
                    listViewAdapter[0] = new SearchAdaptor(getActivity(),R.layout.search_row, foundData);
                    founds.setAdapter(listViewAdapter[0]);
                } else {

                    Toast.makeText(getActivity(), "Herhangi bir ilan bulunamadı.", Toast.LENGTH_SHORT).show();
                    replaceFragments(AdsFragment.class);
                }

            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }

        });

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


}