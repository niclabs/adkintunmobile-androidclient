package cl.niclabs.adkintunmobile.workers;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class AdkintunWorker extends Worker {

    private static final String TAG = AdkintunWorker.class.getSimpleName();

    public AdkintunWorker(Context context, WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        return Result.success();
    }



}