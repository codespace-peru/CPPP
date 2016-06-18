package pe.com.codespace.codigopenal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActivitySecondMain extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SQLiteHelperCodPenal myDBHelper;
    private ExpandableListView myExpand;
    private List<Tools.RowTitulo> listHeader;
    private HashMap<Tools.RowTitulo, List<Tools.RowCapitulo>> listChild;
    private int numLibro;
    private int numSeccion;
    private int cantidadArticulosNorma;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setIcon(R.drawable.ic_launcher);
        }

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
            AdapterExpandableListSecond myAdapter = new AdapterExpandableListSecond(this, listHeader, listChild);
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
                    Intent intentText = new Intent(ActivitySecondMain.this, ActivityText.class);
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
                        case 2://Libro 2
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
                    Intent intentText = new Intent(ActivitySecondMain.this, ActivityText.class);
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

            //Analytics
            Tracker tracker = ((AnalyticsApplication)  getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            String nameActivity = getApplicationContext().getPackageName() + "." + this.getClass().getSimpleName();
            tracker.setScreenName(nameActivity);
            tracker.enableAdvertisingIdCollection(true);
            tracker.send(new HitBuilders.AppViewBuilder().build());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void prepararData(){
        listHeader = new ArrayList<>();
        listChild = new HashMap<>();
        Tools.RowCapitulo child;
        List<Tools.RowCapitulo> childList;

        String[][] titulos, capitulos;
        try{
            titulos = myDBHelper.getTitulos(numLibro, numSeccion);
            for (String[] titulo : titulos) {
                Tools.RowTitulo temp1 = new Tools.RowTitulo(Integer.parseInt(titulo[0]), Integer.parseInt(titulo[1]), Integer.parseInt(titulo[2]), titulo[3], titulo[4]);
                listHeader.add(temp1);
            }

            for(int i=0; i<listHeader.size();i++){
                capitulos = myDBHelper.getCapitulos(numLibro, numSeccion,i+1);
                childList = new ArrayList<>();
                for (String[] capitulo : capitulos) {
                    child = new Tools.RowCapitulo(Integer.parseInt(capitulo[0]), Integer.parseInt(capitulo[1]), Integer.parseInt(capitulo[2]), Integer.parseInt(capitulo[3]), capitulo[4], capitulo[5]);
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
        final MenuItem searchItem =menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                SpeechRecognitionHelper.run(this);
                break;
            case R.id.action_goto:
                Tools.GoTo(this, MyValues.CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this, MyValues.CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this, MyValues.CANTIDAD_ARTICULOS_NORMA);
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
        Tools.QuerySubmit(this, menuItem, MyValues.CANTIDAD_ARTICULOS_NORMA, s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
