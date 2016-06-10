package cl.niclabs.adkintunmobile.views.activeconnections;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.niclabs.adkintunmobile.R;


public class ActiveConnectionMapBottomSheetDialogFragment extends BottomSheetDialogFragment implements OnMapReadyCallback{
    private ActiveConnectionListElement activeConnectionListElement;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_active_connection_map_bottom_sheet, null);
        dialog.setContentView(contentView);

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
        SupportMapFragment map =(SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(47.17, 27.5699), 0));
        //map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable) activeConnectionListElement.getLogo()).getBitmap())).anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
        //        .position(new LatLng(47.17, 27.5699))); //Iasi, Romania
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
    }

}
