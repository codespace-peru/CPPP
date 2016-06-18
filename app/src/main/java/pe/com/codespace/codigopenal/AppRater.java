package pe.com.codespace.codigopenal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Creado por Carlos el 07/01/2016.
 */
public class AppRater {
    private final static String APP_PNAME = "pe.com.codespace.codigopenal";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 5;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 12;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String message="Si te gusta esta app, por favor tómate un minuto para puntuarla. Gracias por tu ayuda!!";
        builder.setTitle("Calificar App");
        builder.setMessage(message);
        builder.setPositiveButton("Puntuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME));
                    mContext.startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    openBrowser(mContext, "https://play.google.com/store/apps/details?id=" + mContext.getPackageName());
                }
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        builder.setNegativeButton("No gracias", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
            }
        });
        builder.create().show();

    }

    public static void openBrowser(Context mContext, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(mContext.getPackageManager())) {
            mContext.startActivity(intent);
        }
    }
}