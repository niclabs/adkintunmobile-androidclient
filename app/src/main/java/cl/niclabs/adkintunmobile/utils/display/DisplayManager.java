package cl.niclabs.adkintunmobile.utils.display;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import cl.niclabs.adkintunmobile.R;

public class DisplayManager {

    static public void enableLoadingPanel(RelativeLayout loadingPanel) {
        loadingPanel.setVisibility(View.VISIBLE);
    }

    static public void dismissLoadingPanel(RelativeLayout loadingPanel, Context context) {
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        loadingPanel.startAnimation(fadeOut);
        loadingPanel.setVisibility(View.GONE);
    }
}
