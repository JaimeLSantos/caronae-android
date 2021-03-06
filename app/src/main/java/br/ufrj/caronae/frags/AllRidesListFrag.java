package br.ufrj.caronae.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.Callback;

import br.ufrj.caronae.App;
import br.ufrj.caronae.customizedviews.EndlessRecyclerViewScrollListener;
import br.ufrj.caronae.R;
import br.ufrj.caronae.data.SharedPref;
import br.ufrj.caronae.Util;
import br.ufrj.caronae.acts.MainAct;
import br.ufrj.caronae.adapters.AllRidesFragmentPagerAdapter;
import br.ufrj.caronae.adapters.RidesAdapter;
import br.ufrj.caronae.httpapis.CaronaeAPI;
import br.ufrj.caronae.models.RideRequestSent;
import br.ufrj.caronae.models.modelsforjson.MyRidesForJson;
import br.ufrj.caronae.models.modelsforjson.RideForJson;
import br.ufrj.caronae.models.modelsforjson.RideForJsonDeserializer;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class AllRidesListFrag extends Fragment implements Callback {

    @BindView(R.id.rvRides)
    RecyclerView rvRides;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.norides_tv)
    TextView noRides;

    private final int FIRST_PAGE_TO_LOAD = 0;

    RidesAdapter adapter;

    int pageCounter = FIRST_PAGE_TO_LOAD;
    boolean isLoadingPage = false;

    private EndlessRecyclerViewScrollListener scrollListener;

    LinearLayoutManager mLayoutManager;

    int pageIdentifier;

    String neighborhoods = null;
    String zone = null;
    String hub = null;
    String campus = null;


    ArrayList<RideForJson> goingRides = new ArrayList<>();
    ArrayList<RideForJson> notGoingRides = new ArrayList<>();



    public AllRidesListFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_rides_list, container, false);
        ButterKnife.bind(this, view);

        Context ctx;
        ctx = getContext();
        refreshLayout.setProgressViewOffset(false, getResources().getDimensionPixelSize(R.dimen.refresher_offset), getResources().getDimensionPixelSize(R.dimen.refresher_offset_end));
        Bundle bundle = getArguments();
        ArrayList<RideForJson> rideOffers = bundle.getParcelableArrayList("rides");
        pageIdentifier = bundle.getInt("ID");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageCounter = FIRST_PAGE_TO_LOAD;
                SharedPref.lastAllRidesUpdate = 0;
                for (int counter = FIRST_PAGE_TO_LOAD; counter <= pageCounter; counter++) {
                    refreshRideList(counter);
                }
            }
        });

        adapter = new RidesAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        rvRides.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadOneMorePage();
            }
        };
        rvRides.addOnScrollListener(scrollListener);

        rvRides.setAdapter(adapter);

        boolean isFiltering = SharedPref.checkExistence(SharedPref.RIDE_FILTER_PREF_KEY) ? SharedPref.getFiltersPref() : false;

        if(SharedPref.OPEN_ALL_RIDES && !isFiltering)
        {
            noRides.setVisibility(View.GONE);
            if (pageIdentifier == AllRidesFragmentPagerAdapter.PAGE_GOING) {
                if (SharedPref.ALL_RIDES_GOING != null && !SharedPref.ALL_RIDES_GOING.isEmpty()) {
                    noRides.setVisibility(View.GONE);
                    adapter.makeList(SharedPref.ALL_RIDES_GOING);
                    scrollListener.resetState();
                }
            } else {
                if (SharedPref.ALL_RIDES_LEAVING != null && !SharedPref.ALL_RIDES_LEAVING.isEmpty()) {
                    noRides.setVisibility(View.GONE);
                    adapter.makeList(SharedPref.ALL_RIDES_LEAVING);
                    scrollListener.resetState();
                }
            }
        }
        else if(!Util.isNetworkAvailable(ctx))
        {
            noRides.setText(R.string.allrides_norides);
            noRides.setVisibility(View.VISIBLE);
        }
        else
        {
            noRides.setText(R.string.charging);
            noRides.setVisibility(View.VISIBLE);
        }

        if (!(rideOffers == null || rideOffers.isEmpty())) {
            adapter.makeList(rideOffers);
        }

        App.getBus().register(this);

        // After setting layout manager, adapter, etc...
        float offsetBottonPx = getResources().getDimension(R.dimen.recycler_view_botton_offset);
        float offsetTopPx = getResources().getDimension(R.dimen.recycler_view_top_offset);
        Util.OffsetDecoration OffsetDecoration = new Util.OffsetDecoration((int) offsetBottonPx, (int) offsetTopPx);
        rvRides.addItemDecoration(OffsetDecoration);

        animateListFadeIn();

        reloadRidesIfNecessary();

        return view;
    }

    @Subscribe
    public void updateAfterResquest(RideRequestSent ride) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getBus().unregister(this);
    }

    @Override
    public void onStart() {
        try {
            if(!SharedPref.getFiltersPref()) {
                MainAct act = (MainAct) getActivity();
                act.hideFilterCard(getContext());
            }
        }catch (Exception e){}
        super.onStart();
    }

    void refreshRideList(final int pageNumber) {
        if(App.getUser() == null)
        isLoadingPage = true;
        String going;
        final Context ctx = getContext();
        if (pageIdentifier == AllRidesFragmentPagerAdapter.PAGE_GOING)
            going = "1";
        else
            going = "0";
        neighborhoods = null;
        zone = null;
        hub = null;
        campus = null;
        boolean isFiltering = SharedPref.getFiltersPref();
        if (isFiltering){
            if(Util.isZone(SharedPref.getLocationFilter()))
            {
                zone = SharedPref.getLocationFilter();
            }
            else
            {
                if(!SharedPref.getLocationFilter().equals("Todos os Bairros")) {
                    neighborhoods = SharedPref.getLocationFilter();
                }
            }
            if(Util.isCampus(SharedPref.getCenterFilter()))
            {
                campus = SharedPref.getCenterFilter();
            }
            else
            {
                if(!SharedPref.getCenterFilter().equals("Todos os Campi")) {
                    hub = SharedPref.getCenterFilter();
                }
            }
        }

        CaronaeAPI.service(ctx).getMyRides(Integer.toString(App.getUser().getDbId()))
            .enqueue(new retrofit2.Callback<MyRidesForJson>() {
                @Override
                public void onResponse(Call<MyRidesForJson> call, Response<MyRidesForJson> response) {
                    if (response.isSuccessful()) {
                        MyRidesForJson data = response.body();
                        SharedPref.MY_RIDES_ACTIVE = data.getActiveRides();
                        SharedPref.MY_RIDES_OFFERED = data.getOfferedRides();
                        SharedPref.MY_RIDES_PENDING = data.getPendingRides();
                        Util.setMyPARidesId();
                        CaronaeAPI.service(ctx).listAllRides(pageNumber + "", going, neighborhoods, zone, hub,  "", campus, "", "")
                                .enqueue(new retrofit2.Callback<RideForJsonDeserializer>() {
                                    @Override
                                    public void onResponse(Call<RideForJsonDeserializer> call, Response<RideForJsonDeserializer> response) {
                                        if (response.isSuccessful()) {

                                            if (pageCounter == FIRST_PAGE_TO_LOAD) {
                                                goingRides = new ArrayList<>();
                                                notGoingRides = new ArrayList<>();
                                            }

                                            RideForJsonDeserializer data = response.body();
                                            List<RideForJson> rideOffers = data.getData();
                                            if(rideOffers.size() != 0) {
                                                noRides.setVisibility(View.GONE);
                                                if (isFiltering){
                                                    setRides(rideOffers, isFiltering);
                                                }
                                                else
                                                {
                                                    SharedPref.OPEN_ALL_RIDES = true;
                                                    setRides(rideOffers, isFiltering);
                                                }
                                            }
                                            else
                                            {
                                                noRides.setText(R.string.frag_rideSearch_noRideFound);
                                                isLoadingPage = false;
                                            }
                                        } else {
                                            Util.treatResponseFromServer(response);
                                            noRides.setText(R.string.allrides_norides);
                                            refreshLayout.setRefreshing(false);
                                            Log.e("listAllRides", response.message());
                                            isLoadingPage = false;
                                        }
                                        scrollListener.resetState();
                                    }

                                    @Override
                                    public void onFailure(Call<RideForJsonDeserializer> call, Throwable t) {
                                        refreshLayout.setRefreshing(false);
                                        Log.e("listAllRides", t.getMessage());
                                        if(!SharedPref.OPEN_ALL_RIDES) {
                                            noRides.setText(R.string.allrides_norides);
                                        }
                                        scrollListener.resetState();
                                        isLoadingPage = false;
                                    }
                                });

                    } else {
                        Util.treatResponseFromServer(response);
                        noRides.setText(R.string.allrides_norides);
                        refreshLayout.setRefreshing(false);
                        Log.e("listAllRides", response.message());
                        isLoadingPage = false;
                    }
                    scrollListener.resetState();
                }
                @Override
                public void onFailure(Call<MyRidesForJson> call, Throwable t) {
                    refreshLayout.setRefreshing(false);
                    Log.e("listAllRides", t.getMessage());
                    if(!SharedPref.OPEN_ALL_RIDES) {
                        noRides.setText(R.string.allrides_norides);
                    }
                    scrollListener.resetState();
                    isLoadingPage = false;
                }
            });
    }

    private void setRides(List<RideForJson> rideOffers, boolean isFiltering)
    {
        if (rideOffers != null && !rideOffers.isEmpty()) {
            Iterator<RideForJson> it = rideOffers.iterator();
            while (it.hasNext()) {
                RideForJson rideOffer = it.next();
                rideOffer.fromWhere = "AllRides";
                rideOffer.setDbId(rideOffer.getId().intValue());
                if (rideOffer.isGoing()) {
                    goingRides.add(rideOffer);

                } else {
                    notGoingRides.add(rideOffer);
                }
            }
        }

        if (pageIdentifier == AllRidesFragmentPagerAdapter.PAGE_GOING) {
            if (goingRides != null && !goingRides.isEmpty()) {
                if (!isFiltering) {
                    SharedPref.ALL_RIDES_GOING = goingRides;
                }
                scrollListener.resetState();
                adapter.makeList(goingRides);
            }
        } else {
            if (notGoingRides != null && !notGoingRides.isEmpty()) {
                if (!isFiltering) {
                    SharedPref.ALL_RIDES_LEAVING = notGoingRides;
                }
                scrollListener.resetState();
                adapter.makeList(notGoingRides);
            }
        }
        SharedPref.lastAllRidesUpdate = 0;
        refreshLayout.setRefreshing(false);
        rvRides.setVisibility(View.VISIBLE);
        isLoadingPage = false;
    }

    private void reloadRidesIfNecessary()
    {
        //Verifies every half second if a reload is necessary
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                if(SharedPref.lastAllRidesUpdate >= 300)
                {
                    pageCounter = FIRST_PAGE_TO_LOAD;
                    for (int counter = FIRST_PAGE_TO_LOAD; counter <= pageCounter; counter++) {
                        refreshRideList(counter);
                    }
                }
            }
        };
        timer.schedule (hourlyTask, 0, 500);
    }

    private void loadOneMorePage() {
        if(!isLoadingPage && !refreshLayout.isRefreshing()) {
            pageCounter++;
            refreshRideList(pageCounter);
        }
    }

    private void animateListFadeIn() {
        Animation anim = new AlphaAnimation(0, 1);
        anim.setDuration(300);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        rvRides.startAnimation(anim);
    }
}