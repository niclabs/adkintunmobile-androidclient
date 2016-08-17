package cl.niclabs.adkintunmobile.utils.files;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;

import cl.niclabs.adkintunmobile.R;

public class FileManager {

    static public int deleteStoredReports(final Context context){
        File outputDir = context.getFilesDir();
        File[] reportFiles = outputDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(context.getString(R.string.synchronization_report_filename)) &&
                        filename.endsWith(context.getString(R.string.synchronization_report_file_extension));
            }
        });

        int deletedFiles;
        for (deletedFiles = 0; deletedFiles<reportFiles.length; deletedFiles++){
            File reportFile = reportFiles[deletedFiles];
            reportFile.delete();
        }

        return deletedFiles;
    }
}
