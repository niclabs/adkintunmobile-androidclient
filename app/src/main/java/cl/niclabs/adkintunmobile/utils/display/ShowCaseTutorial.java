package cl.niclabs.adkintunmobile.utils.display;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;

import cl.niclabs.adkintunmobile.R;

public class ShowCaseTutorial {
    private final static int SINGLE_SHOT = 37;

    public static ShowcaseView createViewTutorial(Activity activity, Target firstTarget, String[] tutorialTitle, String[] tutorialBody, View.OnClickListener listener){
        final ShowcaseView showcaseView = createGenericTutorial(activity, firstTarget, tutorialTitle, tutorialBody, listener)
                .build();
        showcaseView.setButtonText(activity.getString(R.string.tutorial_next));
        showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    showcaseView.hide();
                }
            }
        });

        return showcaseView;
    }

    public static ShowcaseView createInitialTutorial(Activity activity, Target firstTarget, String[] tutorialTitle, String[] tutorialBody, View.OnClickListener listener){
        ShowcaseView showcaseView = createGenericTutorial(activity, firstTarget, tutorialTitle, tutorialBody, listener)
                .singleShot(ShowCaseTutorial.SINGLE_SHOT)
                .build();
        showcaseView.setButtonText(activity.getString(R.string.tutorial_next));
        return showcaseView;
    }


    public static ShowcaseView.Builder createGenericTutorial(Activity activity, Target firstTarget, String[] tutorialTitle, String[] tutorialBody, View.OnClickListener listener){
        ShowcaseView.Builder builder = new ShowcaseView.Builder(activity)
                .setTarget(firstTarget)
                .setContentTitle(tutorialTitle[0])
                .blockAllTouches()
                .setContentText(tutorialBody[0])
                .setOnClickListener(listener)
                .setStyle(R.style.CustomShowcaseTheme)
                .withNewStyleShowcase();
        return builder;
    }
}
