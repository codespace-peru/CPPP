package pe.com.codespace.codigopenal;

import android.app.Activity;
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


public class SecondMainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    SQLiteHelperCodPenal myDBHelper;
    ExpandableListView myExpand;
    AdapterExpandableListSecond myAdapter;
    private List<Tools.RowTitulo> listHeader;
    private HashMap<Tools.RowTitulo, List<Tools.RowCapitulo>> listChild;
    int numLibro, numSeccion;
    int cantidadArticulosNorma;
    SearchView searchView;
    MenuItem menuItem;
    private static int CANTIDAD_ARTICULOS_NORMA = 566;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        try{
            TextView textView1 = (TextView) findViewById(R.id.tvTitleText);
            TextView textView2 = (TextView) findViewById(R.id.tvSubtitleText);
            myDBHelper = SQLiteHelperCodPenal.getInstance(this);
            numLibro = getIntent().getExtras().getInt("numLibro");
            numSeccion = getIntent().getExtras().getInt("numSeccion");
            cantidadArticulosNorma = getIntent().getExtras().getInt("cantidadArticulosNorma");
            String temp[] = myDBHelper.getLibro(numLibro);
            textView1.setText(temp[0] + ": " + temp[1]);
            temp = myDBHelper.getSeccion(numLibro, numSeccion);
            textView2.setText(temp[0] + ": " + temp[1]);
            prepararData();
            myAdapter = new AdapterExpandableListSecond(this, listHeader, listChild);
            myExpand = (ExpandableListView) findViewById(R.id.explvSecond);
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
                    int titulo = Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberTitulo1)).getText().toString());
                    Intent intentText = new Intent(SecondMainActivity.this, TextActivity.class);
                    intentText.putExtra("numLibro", numLibro);
                    intentText.putExtra("numSeccion", numSeccion);
                    intentText.putExtra("numTitulo", titulo);
                    intentText.putExtra("cantidadArticulosNorma",cantidadArticulosNorma);
                    switch (numLibro){
                        case 1://Libro 1
                            switch (numSeccion){
                                case 3:
                                    switch (titulo){
                                        case 1:case 3:
                                            startActivity(intentText);
                                            break;
                                    }
                                    break;
                                case 4:
                                    switch (titulo){
                                        case 3:case 5:
                                        startActivity(intentText);
                                        break;
                                    }
                                    break;
                            }
                            break;
                        case 2:
                            switch (numSeccion){
                                case 1://Seccion 1
                                    switch (titulo){
                                        case 2:case 3://Titulos 2,3
                                            startActivity(intentText);
                                            break;
                                    }
                                    break;
                                case 2://Seccion 2
                                    switch (titulo){
                                        case 1:case 4:case 5://Titulos 1,4,5
                                            startActivity(intentText);
                                            break;
                                    }
                                     break;
                                case 3://Seccion 3
                                    switch (titulo){
                                        case 1:case 2:case 4:case 5:case 6:
                                        case 7:case 8:case 9:case 10://Titulos 1,2,4,5,6,7,8,9,10
                                            startActivity(intentText);
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case 3://Libro 3
                            switch (numSeccion){
                                case 1://Seccion 1
                                    switch (titulo){
                                        case 1:case 3:case 4:case 5:
                                            startActivity(intentText);
                                            break;
                                    }
                                    break;
                                case 2:case 3://Seccion 2,3
                                    startActivity(intentText);
                                    break;
                            }
                            break;
                        case 4: //Libro 4
                            switch (numSeccion){
                                case 4://Seccion 4
                                    startActivity(intentText);
                                    break;
                            }
                            break;
                        case 5://Libro 5
                            switch (numSeccion){
                                case 2://Seccion 2
                                    startActivity(intentText);
                                    break;
                            }
                            break;
                        case 7://Libro 7:
                            switch (numSeccion){
                                case 2:case 5:case 7: // Secciones 2,5,7
                                    startActivity(intentText);
                                    break;

                            }
                            break;
                    }
                    return false;
                }
            });

            myExpand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                    int titulo = Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberTitulo2)).getText().toString());
                    int capitulo = Integer.parseInt(((TextView)view.findViewById(R.id.tvNumberCapitulo1)).getText().toString());
                    Intent intentText = new Intent(SecondMainActivity.this, TextActivity.class);
                    intentText.putExtra("numLibro", numLibro);
                    intentText.putExtra("numSeccion", numSeccion);
                    intentText.putExtra("numTitulo", titulo);
                    intentText.putExtra("numCapitulo", capitulo);
                    intentText.putExtra("cantidadArticulosNorma",cantidadArticulosNorma);
                    startActivity(intentText);
                    return false;
                }
            });
            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewSecondMain);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void prepararData(){
        listHeader = new ArrayList<Tools.RowTitulo>();
        listChild = new HashMap<Tools.RowTitulo, List<Tools.RowCapitulo>>();
        Tools.RowCapitulo child;
        List<Tools.RowCapitulo> childList;

        String[][] titulos, capitulos;
        try{
            titulos = myDBHelper.getTitulos(numLibro, numSeccion);
            for(int i=0; i<titulos.length;i++){
                Tools.RowTitulo temp1 = new Tools.RowTitulo(Integer.parseInt(titulos[i][0]), Integer.parseInt(titulos[i][1]), Integer.parseInt(titulos[i][2]),titulos[i][3], titulos[i][4]);
                listHeader.add(temp1);
            }

            for(int i=0; i<listHeader.size();i++){
                capitulos = myDBHelper.getCapitulos(numLibro, numSeccion,i+1);
                childList = new ArrayList<Tools.RowCapitulo>();
                for(int j=0;j<capitulos.length;j++){
                    child = new Tools.RowCapitulo(Integer.parseInt(capitulos[j][0]),Integer.parseInt(capitulos[j][1]),Integer.parseInt(capitulos[j][2]),Integer.parseInt(capitulos[j][3]),capitulos[j][4],capitulos[j][5]);
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
        return false;
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
