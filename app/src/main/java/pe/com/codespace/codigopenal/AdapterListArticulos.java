package pe.com.codespace.codigopenal;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Creado por Carlos on 17/02/14.
 */
public class AdapterListArticulos extends ArrayAdapter {

    private final Context context;
    private final String[][] values;
    private final List<String[]> misArticulos = new ArrayList<String[]>();
    private String searchText = "";
    private boolean search = false;


    public AdapterListArticulos(Context pContext, String[][] pValues) {
        super(pContext, R.layout.single_item_twoline, pValues);
        this.context = pContext;
        this.values = pValues;
        String[] temp;// = null;// = new String[]{"1","2","3","4"}; // = {values[i][0], values[i][1],values[i][2],values[i][3]};
        for(int i=0; i<values.length;i++){
            temp = new String[] {values[i][0],values[i][1],values[i][2],values[i][3]};
            misArticulos.add(temp);
        }
    }

    public AdapterListArticulos(Context pContext, String[][] pValues, boolean flag, String sSearch) {
        super(pContext, R.layout.single_item_twoline, pValues);
        this.context = pContext;
        this.values = pValues;
        this.searchText = sSearch;
        this.search = flag;
        String[] temp;
        for(int i=0; i<values.length;i++){
            temp = new String[] {values[i][0],values[i][1],values[i][2],values[i][3]};
            misArticulos.add(temp);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        ViewHolder holder;

        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_item_twoline,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
            String[] arts = misArticulos.get(position);
            holder.myNumber.setText(arts[0]);
            holder.myTitle.setText(arts[1] + ": " + arts[2]);
            CharSequence cc;
            if(search){
                cc = searchResaltado(searchText,arts[3]);
                holder.myText.setText(cc);
            }
            else{
                holder.myText.setText(arts[3]);
            }
        return view;
    }

    static class ViewHolder{
        TextView myNumber;
        TextView myTitle;
        TextView myText;
        ViewHolder(View v){
            myNumber = (TextView) v.findViewById(R.id.tvNumberItem);
            myTitle = (TextView) v.findViewById(R.id.tvTitleItem);
            myText = (TextView) v.findViewById(R.id.tvTextItem);
        }
    }

    private static CharSequence searchResaltado(String search, String originalText) {
        String normalizedText = originalText.toLowerCase(); //Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        String[] searchWord = search.toLowerCase().split(" ");
        Spannable highlighted = new SpannableString(originalText);
        for(int i=0;i<searchWord.length;i++){
            int start = normalizedText.indexOf(searchWord[i]);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + searchWord[i].length(), originalText.length());
                highlighted.setSpan(new BackgroundColorSpan(Color.YELLOW), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = normalizedText.indexOf(searchWord[i], spanEnd);
            }
        }
        return highlighted;
    }
}
