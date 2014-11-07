package pe.com.codespace.codigopenal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    SearchView searchView;
    SQLiteHelperCodPenal myDBHelper;
    ExpandableListView myExpand;
    AdapterExpandableListMain myAdapter;
    private List<Tools.RowLibro> listHeader;
    private HashMap<Tools.RowLibro, List<Tools.RowSeccion>> listChild;
    MenuItem menuItem;
    private static int CANTIDAD_ARTICULOS_NORMA = 566;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            myDBHelper = SQLiteHelperCodPenal.getInstance(this);
            prepararData();
            myAdapter = new AdapterExpandableListMain(this, listHeader, listChild);
            myExpand = (ExpandableListView) findViewById(R.id.explvMain);
            myExpand.setGroupIndicator(null);
            myExpand.setAdapter(myAdapter);

            myExpand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int lastExpandedPosition = -1;
                @Override
                public void onGroupExpand(int pos) {
                    if(lastExpandedPosition != -1 && pos != lastExpandedPosition)
                        myExpand.collapseGroup(lastExpandedPosition);
                    lastExpandedPosition = pos;
                }
            });

            myExpand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            Intent intentText = new Intent(MainActivity.this, TextActivity.class);
                            intentText.putExtra("numLibro", Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberLibro1)).getText().toString()));
                            intentText.putExtra("cantidadArticulosNorma",CANTIDAD_ARTICULOS_NORMA);
                            startActivity(intentText);
                    }
                    return false;
                }
            });

            myExpand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                    Intent intentSecond = new Intent(MainActivity.this, SecondMainActivity.class);
                    Intent intentText = new Intent(MainActivity.this, TextActivity.class);
                    int libro = Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberLibro2)).getText().toString());
                    int seccion = Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberSeccion1)).getText().toString());
                    intentSecond.putExtra("numLibro", libro);
                    intentSecond.putExtra("numSeccion", seccion);
                    intentSecond.putExtra("cantidadArticulosNorma",CANTIDAD_ARTICULOS_NORMA);
                    intentText.putExtra("numLibro", libro);
                    intentText.putExtra("numSeccion", seccion);
                    intentText.putExtra("numTitulo",0);
                    intentText.putExtra("numCapitulo",0);
                    intentText.putExtra("cantidadArticulosNorma",CANTIDAD_ARTICULOS_NORMA);
                    switch (libro){
                        case 1://Libro Primero
                            switch (seccion){
                                case 1:case 2: //Seccion 1 y 2
                                    startActivity(intentText);
                                    break;
                                case 3:case 4://Seccion 3 y 4
                                    startActivity(intentSecond);
                                    break;
                            }
                            break;
                        case 2:case 3://Libro 2,3
                            startActivity(intentSecond);
                            break;
                        case 4://Libro 4
                            switch (seccion){
                                case 1:case 2:case 3:case 5:case 6:case 7: //Secciones 1,2,3,5,6,7
                                    startActivity(intentText);
                                    break;
                                case 4://Seccion 4
                                    startActivity(intentSecond);
                                    break;
                            }
                            break;
                        case 5: //Libro 5
                            switch (seccion){
                                case 1:case 3:case 4:case 5:case 6:case 7:
                                    startActivity(intentText);
                                    break;
                                case 2:
                                    startActivity(intentSecond);
                                    break;
                            }
                            break;
                        case 6: //Libro 6
                            startActivity(intentText);
                            break;
                        case 7: //Libro 7
                            switch (seccion){
                                case 1:case 3:case 4:case 6:
                                    startActivity(intentText);
                                    break;
                                case 2:case 5:case 7:
                                    startActivity(intentSecond);
                                    break;
                            }
                            break;
                    }
                    return false;
                }
            });

            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewMain);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void prepararData(){
        listHeader = new ArrayList<Tools.RowLibro>();
        listChild = new HashMap<Tools.RowLibro, List<Tools.RowSeccion>>();
        Tools.RowSeccion child;
        List<Tools.RowSeccion> childList;
        String[][] libros, secciones;
        try{
            libros = myDBHelper.getLibros();
            for(int i=0; i<libros.length;i++){
                Tools.RowLibro temp1 = new Tools.RowLibro(Integer.parseInt(libros[i][0]),libros[i][1], libros[i][2]);
                listHeader.add(temp1);
            }
            for(int i=0; i<listHeader.size();i++){
                secciones = myDBHelper.getSecciones(i);
                childList = new ArrayList<Tools.RowSeccion>();
                for(int j=0;j<secciones.length;j++){
                    child = new Tools.RowSeccion(Integer.parseInt(secciones[j][0]), Integer.parseInt(secciones[j][1]),secciones[j][2], secciones[j][3]);
                    childList.add(child);
                }
                listChild.put(listHeader.get(i),childList);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0){
                Intent intent = new Intent(this,SearchResultsActivity.class);
                intent.putExtra("searchText",matches.get(0).toString());
                this.startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem =menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchItem.collapseActionView();
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
                SpeechRecognitionHelper speech = new SpeechRecognitionHelper();
                speech.run(this);
                break;
            case R.id.action_goto:
                Tools.GoTo(this, CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this, CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this, CANTIDAD_ARTICULOS_NORMA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this, menuItem, CANTIDAD_ARTICULOS_NORMA, s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
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
