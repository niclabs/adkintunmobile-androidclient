package cl.niclabs.adkintunmobile.views.activeconnections;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.IpLocation;
import cl.niclabs.adkintunmobile.utils.display.DisplayManager;
import cl.niclabs.adkintunmobile.utils.information.SystemSockets;
import cz.msebera.android.httpclient.Header;


public class ActiveConnectionMapBottomSheetDialogFragment extends BottomSheetDialogFragment implements OnMapReadyCallback, View.OnClickListener {

    private final String TAG = "AdkM:ACMap";

    private RelativeLayout loadingPanel;

    private final ActiveConnectionMapBottomSheetDialogFragment thisMap = this;
    private ActiveConnectionListElement activeConnectionListElement;
    private SupportMapFragment map;

    private ArrayList<IpLocation> ipLocations = new ArrayList<>();
    private ArrayList<String> ports = new ArrayList<>();
    private ArrayList<SystemSockets.Type> types = new ArrayList<>();
    private int index;

    private ImageView appLogo;
    private TextView dialogTextView;
    private TextView tvTotalConnections;
    private TextView tvAllIp;
    private LinearLayout mapLayout;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        Log.d(TAG, "Setup Dialog");
        index = 0;
        View contentView = View.inflate(getContext(), R.layout.fragment_active_connection_map_bottom_sheet, null);
        dialog.setContentView(contentView);

        Button nextButton = (Button) contentView.findViewById(R.id.button_next);
        nextButton.setOnClickListener(this);

        setUpLayoutElements(contentView);

        appLogo.setImageDrawable(activeConnectionListElement.getLogo());
        tvTotalConnections.setText(activeConnectionListElement.getLabel());
        String ret = "de " + activeConnectionListElement.getTotalActiveConnections() + " conexiones (" +
                activeConnectionListElement.getIpConnections(SystemSockets.Type.TCP).size() + "," +
                activeConnectionListElement.getIpConnections(SystemSockets.Type.TCP6).size() + "," +
                activeConnectionListElement.getIpConnections(SystemSockets.Type.UDP).size() + "," +
                activeConnectionListElement.getIpConnections(SystemSockets.Type.UDP6).size() + ")";
        tvAllIp.setText(ret);

        DisplayManager.enableLoadingPanel(this.loadingPanel);
        for (final SystemSockets.Type type : SystemSockets.Type.values()) {
            for (String ipAddress : activeConnectionListElement.getIpConnections(type)) {
                final String ip = ipAddress.split(":")[0];
                final String port = ipAddress.split(":")[1];

                String url = "http://freegeoip.net/json/" + ip;

                if (!IpLocation.existIpLocationByIp(ip)) {

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                double lat = Double.parseDouble(response.getString("latitude"));
                                double lon = Double.parseDouble(response.getString("longitude"));
                                String country = response.getString("country_name");
                                (new IpLocation(ip, lat, lon, country)).save();
                                Log.d(TAG, lat + " " + lon + " " + ip + " " + type + " from API" );

                                updateMapList(ip, port, type);
                            } catch (JSONException e) {
                                Log.d(TAG, "API fail");
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    updateMapList(ip, port, type);
                }
            }
        }
    }

    private void updateMapList(String ip, String port, SystemSockets.Type type) {
        IpLocation mIpLocation = IpLocation.getIpLocationByIp(ip);
        ipLocations.add(mIpLocation);
        ports.add(port);
        types.add(type);
        map.getMapAsync(thisMap);
    }

    private void setUpLayoutElements(View contentView) {
        this.loadingPanel = (RelativeLayout) contentView.findViewById(R.id.loading_panel);
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) contentView.findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();

        this.dialogTextView = (TextView) contentView.findViewById(R.id.tv_ip_addr);
        this.tvAllIp = (TextView) contentView.findViewById(R.id.tv_all_ip);
        this.tvTotalConnections = (TextView) contentView.findViewById(R.id.tv_total_connections);
        this.mapLayout = (LinearLayout) contentView.findViewById(R.id.map_layout);
        this.appLogo = (ImageView) contentView.findViewById(R.id.iv_applogo);
        this.map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        final BottomSheetBehavior behavior = (BottomSheetBehavior) params.getBehavior();

        if( behavior != null ) {
            behavior.setBottomSheetCallback( new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    return;
                }});
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Mapa desplegado");
        DisplayManager.dismissLoadingPanel(loadingPanel, getContext());

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        mapLayout.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(ipLocations.get(index).getLatitude(), ipLocations.get(index).getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 0));

        String snippet = ipLocations.get(index).getIpAddress() + ":" + ports.get(index) + " " + types.get(index);
        googleMap.addMarker(new MarkerOptions().position(latLng).snippet(snippet).title(ipLocations.get(index).getCountry())).showInfoWindow();
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        dialogTextView.setText("Mostrando conexión #" + (index + 1));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    public void setActiveConnectionListElement(ActiveConnectionListElement activeConnectionListElement){
        this.activeConnectionListElement = activeConnectionListElement;
        Log.d(TAG, "Mostrando: " + activeConnectionListElement.getLabel());
    }

    @Override
    public void onClick(View v) {
        index = (index + 1)%ipLocations.size();
        Log.d(TAG, "Current index: " + index + "/" + ipLocations.size());
        map.getMapAsync(thisMap);
    }
}
