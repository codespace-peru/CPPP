package pe.com.codespace.codigopenal;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.widget.Toast;

public class SpeechRecognitionHelper {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	public static void run(Activity ownerActivity) {		
		if (isSpeechRecognitionActivityPresented(ownerActivity) == true) {			
			startRecognitionActivity(ownerActivity);
		} else {			
			installGoogleVoiceSearch(ownerActivity);
		}			
	}
	
	private static boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
		try {			
			PackageManager pm = ownerActivity.getPackageManager();			
			List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
			
			if (activities.size() != 0) {
				return true;			
			}
		} catch (Exception e) {
			
		}
		return false;
	}
	
	private static void startRecognitionActivity(Activity ownerActivity) {		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);		
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Puede hablar ahora...");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);	
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        ownerActivity.startActivityForResult(intent,VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	private static void installGoogleVoiceSearch(final Activity ownerActivity) {
		Dialog dialog = new AlertDialog.Builder(ownerActivity)
			.setMessage("Para usar el reconocimiento de voz es necesario instalar \"Google Voice Search\"")
			.setTitle("¿Desea Instalar Voice Search desde Google Play?")
			.setPositiveButton("Instalar", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {	
					try {
                        //Toast.makeText(ownerActivity, "paso 1",Toast.LENGTH_LONG).show();
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"));						
						intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
						ownerActivity.startActivity(intent);
                        //Toast.makeText(ownerActivity, "paso 2",Toast.LENGTH_LONG).show();
					 } catch (Exception ex) {
                        //Toast.makeText(ownerActivity, "Error: "+ ex.toString(),Toast.LENGTH_LONG).show();
					 }					
				}})
				
			.setNegativeButton("Cancelar", null)
			.create();
		
		dialog.show();		 
	}	
}
