package cl.niclabs.adkintunmobile.views.activeconnections;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.volley.VolleySingleton;


public class ActiveConnectionMapBottomSheetDialogFragment extends BottomSheetDialogFragment implements OnMapReadyCallback{

    private final String TAG = "AdkM:ACMap";

    private final ActiveConnectionMapBottomSheetDialogFragment thisMap = this;
    private ActiveConnectionListElement activeConnectionListElement;
    private SupportMapFragment map;

    private ArrayList<LatLng> locations = new ArrayList<>();
    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<String> ipAddr = new ArrayList<>();
    private Bitmap icon;
    private int index;
    private TextView dialogTextView;
    private LinearLayout mapLayout;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        Log.d(TAG, "Setup Dialog");
        index = 0;

        View contentView = View.inflate(getContext(), R.layout.fragment_active_connection_map_bottom_sheet, null);
        dialog.setContentView(contentView);

        Button nextButton = (Button) contentView.findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locations.size()<=1)
                    return;
                index++;
                if (index >= locations.size())
                    index = 0;
                Log.d(TAG, "Current index: " + index+ " "+activeConnectionListElement.getIpAddr().size()+" "+ locations.size());
                map.getMapAsync(thisMap);
            }
        });

        dialogTextView = (TextView) contentView.findViewById(R.id.tv_ip_addr);
        mapLayout = (LinearLayout) contentView.findViewById(R.id.map_layout);

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

        map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        //map.getMapAsync(this);

        for (int i=0; i<activeConnectionListElement.getIpAddr().size(); i++) {

            final String ip = activeConnectionListElement.getIpAddr().get(i);
            final int port = activeConnectionListElement.getPortAddr().get(i);
            String url = "http://freegeoip.net/json/" + ip;

            final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                double lat = Double.parseDouble(response.getString("latitude"));
                                double lon = Double.parseDouble(response.getString("longitude"));
                                String country = response.getString("country_name");
                                countries.add(country);
                                locations.add(new LatLng(lat, lon));
                                ipAddr.add(ip + ":" + port);
                                Log.d(TAG, lat + " " + lon+ " "+ ip);
                                map.getMapAsync(thisMap);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            // Access the RequestQueue through your singleton class.
            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Mapa desplegado");
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        mapLayout.setVisibility(View.VISIBLE);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                locations.get(index), 0));
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon)).anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(locations.get(index))); //Iasi, Romania
        dialogTextView.setText(ipAddr.get(index) + " " +countries.get(index));
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

        Drawable d = activeConnectionListElement.getLogo();
        icon = ((BitmapDrawable)d).getBitmap();
    }
}
