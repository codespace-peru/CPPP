package pe.com.codespace.codigopenal;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

/**
 * Creado por Carlos on 7/01/14.
 */
public class SQLiteHelperCodPenal extends SQLiteOpenHelper {
    private final Context myContext;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "codigopenal.db";
    private static final String DATABASE_PATH = "databases/";
    private static File DATABASE_FILE = null;
    private boolean mInvalidDatabaseFile = false;
    private boolean mIsUpgraded  = false;
    private int mOpenConnections=0;
    private static SQLiteHelperCodPenal mInstance;

    public synchronized static SQLiteHelperCodPenal getInstance (Context context){
        if(mInstance == null){
            mInstance = new SQLiteHelperCodPenal(context.getApplicationContext());
        }
        return mInstance;
    }

    private SQLiteHelperCodPenal(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            DATABASE_FILE = context.getDatabasePath(DATABASE_NAME);
            if(mInvalidDatabaseFile){
                copyDatabase();
            }
            if(mIsUpgraded){
                doUpgrade();
            }
        }
        catch(SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mInvalidDatabaseFile = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mInvalidDatabaseFile = true;
        mIsUpgraded = true;
    }

    @Override
    public synchronized void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        mOpenConnections++;
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public synchronized void close(){
        mOpenConnections--;
        if(mOpenConnections == 0){
            super.close();
        }
    }

    private void copyDatabase()  {
        AssetManager assetManager = myContext.getResources().getAssets();
        InputStream myInput = null;
        OutputStream myOutput = null;
        try{
            myInput = assetManager.open(DATABASE_PATH +DATABASE_NAME);
            myOutput = new FileOutputStream(DATABASE_FILE);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = myInput.read(buffer)) != -1) {
                myOutput.write(buffer, 0, read);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if(myInput != null){
                try{ myInput.close(); }
                catch(IOException ex){ex.printStackTrace(); }
            }
            if(myOutput!=null){
                try{ myOutput.close(); }
                catch (IOException ex){ex.printStackTrace(); }
            }
            setDataBaseVersion();
            mInvalidDatabaseFile = false;
        }
    }

    private void setDataBaseVersion(){
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("PRAGMA user_version=" + DATABASE_VERSION);
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    private void doUpgrade(){
        try{
            myContext.deleteDatabase(DATABASE_NAME);
            copyDatabase();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String[][] getLibros(){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numLibro, nombreLibro, descripLibro FROM libros ORDER BY numLibro",null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),3 });
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getSecciones(int lib){
        SQLiteDatabase db = null;
        String[] array = new String[1];
        array[0] = String.valueOf(lib);
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numLibro, numSeccion, nombreSeccion, descripSeccion FROM secciones WHERE numLibro = ? ORDER BY numSeccion",array);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),4 });
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getTitulos(int lib, int sec){
        SQLiteDatabase db = null;
        String[] array = new String[2];
        array[0] = String.valueOf(lib);
        array[1] = String.valueOf(sec);
        try{
            db = getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT numLibro, numSeccion, numTitulo, nombreTitulo, descripTitulo from titulos WHERE numLibro=? AND numSeccion=? ORDER BY numTitulo", array);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),5 });
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    arrayOfString[i][4] = cursor.getString(4);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getCapitulos(int lib, int sec, int tit) {
        SQLiteDatabase db = null;
        String[] array = new String[3];
        array[0] = String.valueOf(lib);
        array[1] = String.valueOf(sec);
        array[2] = String.valueOf(tit);
        try{
            db = getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT numLibro, numSeccion, numTitulo, numCapitulo, nombreCapitulo, descripCapitulo FROM capitulos WHERE numLibro=? AND numSeccion=? AND numTitulo = ? ORDER BY numCapitulo", array);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),6 });
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    arrayOfString[i][4] = cursor.getString(4);
                    arrayOfString[i][5] = cursor.getString(5);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }


    public String[] getLibro(int libro){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[1];
            array[0] = String.valueOf(libro);
            Cursor cursor = db.rawQuery("select nombreLibro, descripLibro from libros WHERE numLibro = ?", array);
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{2});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
           throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[] getSeccion(int libro, int seccion){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[2];
            array[0] = String.valueOf(libro);
            array[1] = String.valueOf(seccion);
            Cursor cursor = db.rawQuery("select nombreSeccion, descripSeccion from secciones WHERE numLibro = ? AND numSeccion = ?", array);
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{2});
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

   public String[] getTitulo(int libro, int seccion, int titulo){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[3];
            array[0] = String.valueOf(libro);
            array[1] = String.valueOf(seccion);
            array[2] = String.valueOf(titulo);
            Cursor cursor = db.rawQuery("SELECT nombreTitulo, descripTitulo FROM titulos WHERE numLibro=? AND numSeccion=? AND numTitulo = ?", array);
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{2});
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
           throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[] getCapitulo(int lib, int sec, int tit, int cap){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[4];
            array[0] = String.valueOf(lib);
            array[1] = String.valueOf(sec);
            array[2] = String.valueOf(tit);
            array[3] = String.valueOf(cap);
            Cursor cursor = db.rawQuery("SELECT nombreCapitulo, descripCapitulo FROM capitulos WHERE numLibro=? AND numSeccion=? AND numTitulo = ? and numCapitulo = ?", array);
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{2});
            int i=0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }


    public String[] getArticulo(double art){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numLibro, numSeccion, numTitulo, numCapitulo, nombreArticulo, descripArticulo, textArticulo FROM articulos WHERE numArticulo = ?", new String[] {String.valueOf(art)});
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{7});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    arrayOfString[2] = cursor.getString(2);
                    arrayOfString[3] = cursor.getString(3);
                    arrayOfString[4] = cursor.getString(4);
                    arrayOfString[5] = cursor.getString(5);
                    arrayOfString[6] = cursor.getString(6);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getListaArticulos(int libro, int seccion, int titulo, int capitulo) {
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[4];
            array[0] = String.valueOf(libro);
            array[1] = String.valueOf(seccion);
            array[2] = String.valueOf(titulo);
            array[3] = String.valueOf(capitulo);
            Cursor cursor = db.rawQuery("SELECT numArticulo, nombreArticulo, descripArticulo, textArticulo FROM articulos WHERE numLibro=? AND numSeccion=? AND numTitulo=? AND numCapitulo=? ORDER BY numArticulo", array);
            String[][] arrayOfString = (String[][]) Array.newInstance(String.class, new int[]{cursor.getCount(),4});

            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean es_favorito(double art) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("SELECT numArticulo FROM favoritos WHERE numArticulo = ? ", new String[]{String.valueOf(art)});
            return cursor.moveToFirst() && cursor.getDouble(0) == art;
        }catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(cursor!=null)
                cursor.close();
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getFavoritos(){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numArticulo, nombreArticulo, descripArticulo, textArticulo FROM FAVORITOS ORDER BY numArticulo",null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] {cursor.getCount(),4});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }catch(SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

   public boolean setFavorito(double art){
        SQLiteDatabase db=null;
        try{
            boolean flag = false;
            db = getReadableDatabase();
            String[] myArt =  getArticulo(art);
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombreArticulo",myArt[4]);
            values.put("numArticulo",art);
            values.put("descripArticulo",myArt[5]);
            values.put("textArticulo",myArt[6]);
            long x = db.insert("FAVORITOS",null,values);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
   }

   public boolean eliminarFavorito(double art){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            db = getWritableDatabase();
            String[] whereArgs={String.valueOf(art)};
            long x = db.delete("FAVORITOS","numArticulo = ? ",whereArgs);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
   }

    public String[][] getNotes(){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numArticulo, nombreArticulo, descripArticulo, nota FROM notas ORDER BY numArticulo", null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] {cursor.getCount(),4});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }catch(SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean hay_nota(double art) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("select numArticulo from NOTAS where numArticulo = ? ", new String[]{String.valueOf(art)});
            return cursor.moveToFirst() && cursor.getDouble(0) == art;
        }catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(cursor != null)
                cursor.close();
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[] getNota(double art){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT nombreArticulo, descripArticulo, nota FROM NOTAS WHERE numArticulo = ?", new String[] {String.valueOf(art)});
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{3});
            int i=0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    arrayOfString[2] = cursor.getString(2);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean AddNota(double art, String nota){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            String[] articulo = getArticulo(art);
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("numArticulo",art);
            values.put("nombreArticulo",articulo[4]);
            values.put("descripArticulo",articulo[5]);
            values.put("nota",nota);
            long x = db.insert("notas",null,values);
            if (x > 0){
                flag = true;
            }
            return flag;
        } catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean UpdateNota(double art, String nota){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            String[] whereArgs={String.valueOf(art)};
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nota",nota);
            long x = db.update("notas",values,"numArticulo = ?",whereArgs);
            if (x > 0){
                flag = true;
            }
            return flag;
        } catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean EliminarNota(double art){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            db = getWritableDatabase();
            String[] whereArgs={String.valueOf(art)};
            long x = db.delete("NOTAS","numArticulo = ? ",whereArgs);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] searchArticulo(String cadena) {
        SQLiteDatabase db = null;
        String[] cad = cadena.split(" ");
        String sqlLike = "SELECT numArticulo, nombreArticulo, descripArticulo, textArticulo FROM ARTICULOS where ";
        for(int i=0;i<cad.length;i++){
            if(i<cad.length-1){
                sqlLike = sqlLike + "textArticulo LIKE " + "\'%" + cad[i]  + "%\' AND ";
            }
            else{
                sqlLike = sqlLike + "textArticulo LIKE " + "\'%" + cad[i]  + "%\' COLLATE NOCASE";
            }
        }
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sqlLike, null);
            int j = 0;
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),4 });
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    arrayOfString[j][0] = cursor.getString(0);
                    arrayOfString[j][1] = cursor.getString(1);
                    arrayOfString[j][2] = cursor.getString(2);
                    arrayOfString[j][3] = cursor.getString(3);
                    j++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }
}
