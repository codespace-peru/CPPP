package pe.com.codespace.codigopenal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Carlos on 01/03/14.
 */
public class AddNoteActivity extends ActionBarActivity {
    SQLiteHelperCodPenal myDBHelper;
    String nota = "";
    int numArticulo = -1;
    int tipoNorma = 0;
    String nombreArticulo = "";
    EditText editText;
    boolean modify = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        try{
            Intent intent = getIntent();
            numArticulo = intent.getExtras().getInt("numeroArticulo");
            nombreArticulo = intent.getExtras().getString("nombreArticulo");
            tipoNorma = intent.getExtras().getInt("tipoNorma");
            TextView textView = (TextView) findViewById(R.id.tvAddNota);
            textView.setText("Nota para el " + nombreArticulo + ":");
            editText = (EditText) findViewById(R.id.edtAddNota);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                    }
            });

            myDBHelper = SQLiteHelperCodPenal.getInstance(this);
            if(myDBHelper.hay_nota(numArticulo)){
                TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                textView1.setText("MODIFICAR NOTA");
                nota = myDBHelper.getNota(numArticulo)[2];
                editText.setText(nota);
                int end = editText.getText().length();
                editText.setSelection(end); // Colocar el cursor al final
                modify=true;
            }
            else{
                TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                textView1.setText("AGREGAR NOTA");
            }





            //Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewAddNote);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }catch (Exception ex){
            Log.e("Debug", "MessageError: " + ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_addnotes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_saveNota:
                nota = editText.getText().toString();
                if(modify==true){
                    if(myDBHelper.UpdateNota(numArticulo,nota)){
                        Toast.makeText(AddNoteActivity.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(myDBHelper.AddNota(numArticulo,nota)) {
                        Toast.makeText(AddNoteActivity.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                    }
                }
                this.finish();
                break;
            case R.id.action_cancelarNota:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
