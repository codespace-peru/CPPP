package pe.com.codespace.codigopenal;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
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

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class TextActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    AdapterListArticulos myListAdapter;
    ListView myList;
    SQLiteHelperCodPenal myDBHelper;
    String[][] LstArticulos;
    String nombreArticuloSeleccionado="";
    int numeroArticuloSeleccionado = -1;
    MenuItem menuItem;
    int cantidadArticulosNorma = 0;
    int libro, seccion, titulo, capitulo;
    boolean ir = false;
    int primerArticulo;
    int gotoArticulo;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Intent intent = getIntent();
        libro = intent.getExtras().getInt("numLibro");
        seccion = intent.getExtras().getInt("numSeccion");
        titulo = intent.getExtras().getInt("numTitulo");
        capitulo = intent.getExtras().getInt("numCapitulo");
        ir = intent.getExtras().getBoolean("ir");
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
            String[] sec1 = myDBHelper.getSeccion(libro,seccion);
            String[] tit1 = myDBHelper.getTitulo(libro, seccion, titulo);
            String[] cap1 = myDBHelper.getCapitulo(libro, seccion, titulo, capitulo);
            LstArticulos = myDBHelper.getListaArticulos(libro, seccion, titulo, capitulo);
            if(libro==0)
                myText1.setText(lib1[0]);
            else
                myText1.setText(lib1[0] + ": " + lib1[1]);
            if(sec1[0]!=null)
                myText2.setText(sec1[0] + ": " + sec1[1]);
            else
                myText2.setVisibility(View.GONE);
            if(tit1[0]!=null)
                myText3.setText(tit1[0] + ": " + tit1[1]);
            else
                myText3.setVisibility(View.GONE);
            if(cap1[0]!=null)
                myText4.setText(cap1[0] + ": " + cap1[1]);
            else
                myText4.setVisibility(View.GONE);

            myList = (ListView) findViewById(R.id.lvText);
            myListAdapter = new AdapterListArticulos(this,LstArticulos);
            myList.setAdapter(myListAdapter);
            if(ir){
                String tt = LstArticulos[0][0];
                primerArticulo = Integer.parseInt(tt);
                gotoArticulo = intent.getExtras().getInt("gotoArticulo");
                myList.setSelection(gotoArticulo-primerArticulo);
            }
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(), "Mantener presionado para ver opciones", Toast.LENGTH_LONG).show();
                }
            });

            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewText);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String temp = ((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString();
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        numeroArticuloSeleccionado = Integer.parseInt(temp);
        menu.setHeaderTitle(nombreArticuloSeleccionado);
        inflater.inflate(R.menu.menu_contextual_lista,menu);

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
        final MenuItem searchItem;
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
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
                SpeechRecognitionHelper speech = new SpeechRecognitionHelper();
                speech.run(this);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this, menuItem,cantidadArticulosNorma, s);
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
