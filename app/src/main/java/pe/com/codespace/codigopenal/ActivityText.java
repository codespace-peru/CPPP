package pe.com.codespace.codigopenal;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class ActivityText extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SQLiteHelperCodPenal myDBHelper;
    private String nombreArticuloSeleccionado="";
    private double numeroArticuloSeleccionado = -1;
    private MenuItem menuItem;
    private int cantidadArticulosNorma = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setIcon(R.drawable.ic_launcher);
        }

        Intent intent = getIntent();
        int libro = intent.getExtras().getInt("numLibro");
        int seccion = intent.getExtras().getInt("numSeccion");
        int titulo = intent.getExtras().getInt("numTitulo");
        int capitulo = intent.getExtras().getInt("numCapitulo");
        boolean ir = intent.getExtras().getBoolean("ir");
        cantidadArticulosNorma = intent.getExtras().getInt("cantidadArticulosNorma");
        TextView myText1;
        TextView myText2;
        TextView myText3;
        TextView myText4;

        myText1 = (TextView) findViewById(R.id.tvRootTitle);
        myText2 = (TextView) findViewById(R.id.tvTitle);
        myText3 = (TextView) findViewById(R.id.tvSubTitle);
        myText4 = (TextView) findViewById(R.id.tvSubsubTitle);

        try {
            myDBHelper = SQLiteHelperCodPenal.getInstance(this);
            String[] lib1 = myDBHelper.getLibro(libro);
            String[] sec1 = myDBHelper.getSeccion(libro, seccion);
            String[] tit1 = myDBHelper.getTitulo(libro, seccion, titulo);
            String[] cap1 = myDBHelper.getCapitulo(libro, seccion, titulo, capitulo);
            String[][] lstArticulos = myDBHelper.getListaArticulos(libro, seccion, titulo, capitulo);
            if(libro ==0)
                myText1.setText(lib1[0]);
            else
                myText1.setText(lib1[0] + ": " + lib1[1]);

            if(sec1[0]!=null){
                myText2.setText(sec1[0] + ": " + sec1[1]);
                myText2.setVisibility(View.VISIBLE);
            }
            else
                myText2.setVisibility(View.GONE);

            if(tit1[0]!=null){
                myText3.setText(tit1[0] + ": " + tit1[1]);
                myText3.setVisibility(View.VISIBLE);
            }
            else
                myText3.setVisibility(View.GONE);

            if(cap1[0]==null || cap1[0].equals("Sin Capitulo")){
                myText4.setVisibility(View.GONE);
            }
            else{
                myText4.setText(cap1[0] + ": " + cap1[1]);
                myText4.setVisibility(View.VISIBLE);
            }


            ListView myList = (ListView) findViewById(R.id.lvText);
            AdapterListArticulos myListAdapter = new AdapterListArticulos(this, lstArticulos);
            myList.setAdapter(myListAdapter);
            if(ir){
                String tt = lstArticulos[0][0];
                int primerArticulo = Integer.parseInt(tt);
                int gotoArticulo = intent.getExtras().getInt("gotoArticulo");
                myList.setSelection(gotoArticulo - primerArticulo);
            }
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(), "Mantener presionado para ver opciones", Toast.LENGTH_LONG).show();
                }
            });

            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewText);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            //Analytics
            Tracker tracker = ((AnalyticsApplication)  getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            String nameActivity = getApplicationContext().getPackageName() + "." + this.getClass().getSimpleName();
            tracker.setScreenName(nameActivity);
            tracker.enableAdvertisingIdCollection(true);
            tracker.send(new HitBuilders.AppViewBuilder().build());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String temp = ((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString();
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        numeroArticuloSeleccionado = Double.parseDouble(temp);
        menu.setHeaderTitle(nombreArticuloSeleccionado);
        inflater.inflate(R.menu.menu_contextual_lista, menu);

        try{
            if(myDBHelper.hay_nota(numeroArticuloSeleccionado)){
                MenuItem itemHide1 = menu.findItem(R.id.CtxAddNote);
                itemHide1.setVisible(false);
            }
            else{
                MenuItem itemHide1 = menu.findItem(R.id.CtxEditNote);
                itemHide1.setVisible(false);
                MenuItem itemHide2 = menu.findItem(R.id.CtxShowNote);
                itemHide2.setVisible(false);
            }

            if(myDBHelper.es_favorito(numeroArticuloSeleccionado)){
                MenuItem itemHide3 = menu.findItem(R.id.CtxAddFavorito);
                itemHide3.setVisible(false);
            }
            else{
                MenuItem itemHide3 = menu.findItem(R.id.CtxDelFavorito);
                itemHide3.setVisible(false);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try{
            switch (item.getItemId()) {
                case R.id.CtxAddFavorito:
                    Tools.AgregarFavorito(this, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                    return true;
                case R.id.CtxDelFavorito:
                    Tools.EliminarFavorito(this, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                    return true;
                case R.id.CtxShowNote:
                    Tools.ShowNota(this, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                    return true;
                case R.id.CtxAddNote: case R.id.CtxEditNote:
                    Tools.AgregarNota(this, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                    return true;
                case R.id.CtxCopyArticulo:
                    Tools.CopyArticuloToClipboard(this, numeroArticuloSeleccionado);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyValues.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0){
                Intent intent = new Intent(this,ActivitySearchResults.class);
                intent.putExtra("searchText",matches.get(0).toString());
                this.startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem;
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                MenuItemCompat.collapseActionView(searchItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItem = item;
        switch (item.getItemId()){
            case R.id.action_search:
                break;
            case R.id.action_voice:
                SpeechRecognitionHelper.run(this);
                break;
            case R.id.action_goto:
                Tools.GoTo(this, cantidadArticulosNorma);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this, cantidadArticulosNorma);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this, cantidadArticulosNorma);
                break;
            case R.id.action_share:
                Tools.ShareApp(this);
                break;
            case R.id.action_rate:
                Tools.RateApp(this,getPackageName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this, menuItem, cantidadArticulosNorma, s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
