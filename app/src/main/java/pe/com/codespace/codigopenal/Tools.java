package pe.com.codespace.codigopenal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Carlos on 01/03/14.
 */
public class Tools {

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }

    public static class RowLibro {
        int numLibro;
        String nombreLibro;
        String descripLibro;
        RowLibro(int num, String title1, String title2){
           this.numLibro = num;
            this.nombreLibro = title1;
            this.descripLibro = title2;
        }
    }

    public static class RowSeccion {
        int numLibro;
        int numSeccion;
        String nombreSeccion;
        String descripSeccion;
        RowSeccion(int num1, int num2, String title1, String title2){
            this.numLibro = num1;
            this.numSeccion = num2;
            this.nombreSeccion = title1;
            this.descripSeccion = title2;
        }
    }

    public static class RowTitulo {
        int numLibro;
        int numSeccion;
        int numTitulo;
        String nombreTitulo;
        String descripTitulo;
        RowTitulo(int num1, int num2, int num3, String nomb, String desc){
            this.numLibro = num1;
            this.numSeccion = num2;
            this.numTitulo = num3;
            this.nombreTitulo = nomb;
            this.descripTitulo = desc;
        }
    }

    public static class RowCapitulo {
        int numLibro;
        int numSeccion;
        int numTitulo;
        int numCapitulo;
        String nombreCapitulo;
        String descripCapitulo;
        RowCapitulo(int num1, int num2, int num3, int num4, String nomb, String desc){
            this.numLibro = num1;
            this.numSeccion = num2;
            this.numTitulo = num3;
            this.numCapitulo = num4;
            this.nombreCapitulo = nomb;
            this.descripCapitulo = desc;
        }
    }

    public static class TextHolderLibro {
        TextView myNumLibro;
        TextView myNombreLibro;
        TextView myDescripcionLibro;
        TextHolderLibro(View v)
        {
            myNumLibro = (TextView) v.findViewById(R.id.tvNumberLibro1);
            myNombreLibro = (TextView) v.findViewById(R.id.tvTitleLibro);
            myDescripcionLibro = (TextView) v.findViewById(R.id.tvDescriptionLibro);
        }
    }

    public static class TextHolderSeccion {
        TextView myNumLibro;
        TextView myNumSeccion;
        TextView myNombreSeccion;
        TextView myDescripcionSeccion;
        TextHolderSeccion(View v)
        {
            myNumLibro = (TextView) v.findViewById(R.id.tvNumberLibro2);
            myNumSeccion = (TextView) v.findViewById(R.id.tvNumberSeccion1);
            myNombreSeccion = (TextView) v.findViewById(R.id.tvTitleSeccion);
            myDescripcionSeccion = (TextView) v.findViewById(R.id.tvDescriptionSeccion);
        }
    }

    public static class TextHolderTitulo {
        TextView myNumLibro;
        TextView myNumSeccion;
        TextView myNumTitulo;
        TextView myNombreTitulo;
        TextView myDescripcionTitulo;
        TextHolderTitulo(View v)
        {
            myNumLibro = (TextView) v.findViewById(R.id.tvNumberLibro3);
            myNumSeccion = (TextView) v.findViewById(R.id.tvNumberSeccion2);
            myNumTitulo = (TextView) v.findViewById(R.id.tvNumberTitulo1);
            myNombreTitulo = (TextView) v.findViewById(R.id.tvTitleTitulo);
            myDescripcionTitulo = (TextView) v.findViewById(R.id.tvDescriptionTitulo);
        }
    }

    public static class TextHolderCapitulo {
        TextView myNumLibro;
        TextView myNumSeccion;
        TextView myNumTitulo;
        TextView myNumCapitulo;
        TextView myNombreCapitulo;
        TextView myDescripcionCapitulo;
        TextHolderCapitulo(View v)
        {
            myNumLibro = (TextView) v.findViewById(R.id.tvNumberLibro4);
            myNumSeccion = (TextView) v.findViewById(R.id.tvNumberSeccion3);
            myNumTitulo = (TextView) v.findViewById(R.id.tvNumberTitulo2);
            myNumCapitulo = (TextView) v.findViewById(R.id.tvNumberCapitulo1);
            myNombreCapitulo = (TextView) v.findViewById(R.id.tvTitleCapitulo);
            myDescripcionCapitulo = (TextView) v.findViewById(R.id.tvDescriptionCapitulo);
        }
    }


    public static void MostrarFavoritos(Context context, int cantidadArticulos){
        Intent intent = new Intent(context, FavoritosActivity.class);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        context.startActivity(intent);
    }

    public static void MostrarNotas(Context context, int cantidadArticulos){
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        context.startActivity(intent);
    }

    public static void GoTo(final Context ctx, final int cantidadArticulos){
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle("Ir al Artículo");
        final EditText input = new EditText(ctx);
        //Para que acepte maximo 3 caracteres
        input.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(3),
        });
        //Para que use el softkeyborad con solo numeros
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if(Tools.isNumeric(value)){
                    String[] articulo = null;
                    SQLiteHelperCodPenal myDBHelper;
                    myDBHelper = SQLiteHelperCodPenal.getInstance(ctx);
                    articulo = myDBHelper.getArticulo(Integer.parseInt(value));
                    int art = Integer.parseInt(value);
                    if(art>0 && art<=cantidadArticulos){
                        Intent intent = new Intent(ctx,TextActivity.class);
                        intent.putExtra("cantidadArticulosNorma", cantidadArticulos);
                        intent.putExtra("numLibro", Integer.parseInt(articulo[0]));
                        intent.putExtra("numSeccion", Integer.parseInt(articulo[1]));
                        intent.putExtra("numTitulo", Integer.parseInt(articulo[2]));
                        intent.putExtra("numCapitulo", Integer.parseInt(articulo[3]));
                        intent.putExtra("gotoArticulo",Integer.parseInt(value));
                        intent.putExtra("ir",true);
                        if(ctx instanceof TextActivity) {
                            ((Activity) ctx).finish();
                        }
                        ctx.startActivity(intent);
                    }
                    else
                        Toast.makeText(ctx, "Número de artículo no válido", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ctx,"Número de artículo no válido", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { }
        });
        alert.show();
    }

    public static void CopyArticuloToClipboard(Context context, int art){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE) ;
        SQLiteHelperCodPenal myDBHelper;
        myDBHelper = SQLiteHelperCodPenal.getInstance(context);
        String[] articulo = myDBHelper.getArticulo(art);
        ClipData clip = ClipData.newPlainText("text",articulo[4] + ": " + articulo[5] + "\n\n" + articulo[6]);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "El " + articulo[4] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
    }

    public static void CopyNotaToClipboard(Context context, int art){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE) ;
        SQLiteHelperCodPenal myDBHelper;
        myDBHelper = SQLiteHelperCodPenal.getInstance(context);
        String[] nota = myDBHelper.getNota(art);
        ClipData clip = ClipData.newPlainText("text","Nota del " + nota[0] + ": " + nota[1] + "\n\n" + nota[2]);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "El " + nota[0] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
    }

    public static final void AgregarFavorito(Context context, int numArticulo, String nombreArticulo){
        boolean flag = false;
        SQLiteHelperCodPenal myDBHelper1;
        myDBHelper1 = SQLiteHelperCodPenal.getInstance(context);
        if(myDBHelper1.setFavorito(numArticulo))
            flag = true;

        if(flag == true)
            Toast.makeText(context,"Se agregó " + nombreArticulo.toLowerCase() + " a Favoritos",Toast.LENGTH_LONG).show();
    }

    public static final void EliminarFavorito(final Context context, final int numArticulo, final String nombreArticulo, final int cantidadArticulos){
        final Intent intent = new Intent(context,FavoritosActivity.class);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar de Favoritos");
        confirmar.setMessage("¿Está seguro que desea quitar el " + nombreArticulo + " de Mis Favoritos?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
                SQLiteHelperCodPenal myDBHelper1;
                myDBHelper1 = SQLiteHelperCodPenal.getInstance(context);
                if (myDBHelper1.eliminarFavorito(numArticulo)) {
                    Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                    if(context instanceof FavoritosActivity){
                        ((Activity) context).finish();
                        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
                        context.startActivity(intent);
                    }
                }
            }
        });
        confirmar.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface confirmar, int i){ }
        });
        confirmar.show();
    }

    public static final void AgregarNota(Context context, int numArticulo, String nombreArticulo, int cantidadArticulos){
        Intent intent1 = new Intent(context,AddNoteActivity.class);
        intent1.putExtra("numeroArticulo",numArticulo);
        intent1.putExtra("nombreArticulo",nombreArticulo);
        intent1.putExtra("cantidadArticulosNorma", cantidadArticulos);
        context.startActivity(intent1);
    }

    public static final void EliminarNota(final Context context, final int numArticulo, final String nombreArticulo, int cantidadArticulos){
        final Intent intent = new Intent(context,NotesActivity.class);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar Anotación");
        confirmar.setMessage("¿Está seguro que desea eliminar la nota del " + nombreArticulo + "?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
                SQLiteHelperCodPenal myDBHelper1;
                myDBHelper1 = SQLiteHelperCodPenal.getInstance(context);
                if (myDBHelper1.EliminarNota(numArticulo)) {
                    Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                    context.startActivity(intent);
                }
            }
        });
        confirmar.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface confirmar, int i){ }
        });
        confirmar.show();
    }

    public static final void ShowNota(Context context, int numArticulo, String nombreArticulo){
        String nota = "";
        SQLiteHelperCodPenal myDBHelper1;
        myDBHelper1 = SQLiteHelperCodPenal.getInstance(context);
        nota = myDBHelper1.getNota(numArticulo)[2];
        AlertDialog.Builder dialogoNota = new AlertDialog.Builder(context);
        dialogoNota.setTitle("Notas del " + nombreArticulo);
        dialogoNota.setMessage(nota);
        dialogoNota.setCancelable(true);
        dialogoNota.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int i) {
                dialog1.cancel();
            }
        });
        dialogoNota.show();
    }

    /*public static final void CheckInternetAccessToWebview(Context context, String url){
        final ConnectivityManager network = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = network.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){
            Intent intent = new Intent(context, WebviewActivity.class);
            intent.putExtra("url",url);
            context.startActivity(intent);
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Error de Conexión");
            dialog.setMessage("No está conectado a internet");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog1, int i) {
                    dialog1.cancel();
                }
            });
            dialog.show();
        }
    }*/

    public static final void QuerySubmit(Context context, MenuItem menuItem, int cantidadArticulos, String query){
        Intent intent = new Intent(context,SearchResultsActivity.class);
        intent.putExtra("searchText", query);
        intent.putExtra("cantidadArticulosNorma", cantidadArticulos);
        context.startActivity(intent);
        MenuItemCompat.collapseActionView(menuItem);
    }

}