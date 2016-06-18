package pe.com.codespace.codigopenal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Creado por Carlos el 23/11/13.
 */
public class AdapterExpandableListSecond extends BaseExpandableListAdapter {
    private Context context;
    private List<Tools.RowTitulo> _listHeader;
    private HashMap<Tools.RowTitulo, List<Tools.RowCapitulo>> _listChild;

    public AdapterExpandableListSecond(Context context, List<Tools.RowTitulo> listHeader, HashMap<Tools.RowTitulo, List<Tools.RowCapitulo>> listChild){
        this.context = context;
        this._listHeader = listHeader;
        this._listChild = listChild;
    }


    @Override
    public int getGroupCount() {
        return this._listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        View row = view;
        Tools.TextHolderTitulo holder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_titulo, viewGroup, false);
            holder = new Tools.TextHolderTitulo(row);
            row.setTag(holder);
        }
        else{
            holder = (Tools.TextHolderTitulo) row.getTag();
        }

        Tools.RowTitulo temp = (Tools.RowTitulo) getGroup(groupPosition);
        holder.myNumLibro.setText(String.valueOf(temp.numLibro));
        holder.myNumSeccion.setText(String.valueOf(temp.numSeccion));
        holder.myNumTitulo.setText(String.valueOf(temp.numTitulo));
        holder.myNombreTitulo.setText(temp.nombreTitulo);
        holder.myDescripcionTitulo.setText(temp.descripTitulo);
        //row.setPadding(10,0,0,0);
        //row.setBackgroundColor(Color.BLUE);
        return row;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        View row = view;
        Tools.TextHolderCapitulo holder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_capitulo, viewGroup, false);
            holder = new Tools.TextHolderCapitulo(row);
            row.setTag(holder);
        }
        else {
            holder = (Tools.TextHolderCapitulo) row.getTag();
        }

        Tools.RowCapitulo temp = (Tools.RowCapitulo) getChild(groupPosition, childPosition);
        if(temp != null){
            holder.myNumLibro.setText(String.valueOf(temp.numLibro));
            holder.myNumSeccion.setText(String.valueOf(temp.numSeccion));
            holder.myNumTitulo.setText(String.valueOf(temp.numTitulo));
            holder.myNumCapitulo.setText(String.valueOf(temp.numCapitulo));
            holder.myNombreCapitulo.setText(temp.nombreCapitulo);
            holder.myDescripcionCapitulo.setText(temp.descripCapitulo);
        }
        row.setPadding(20,0,0,0);
        //row.setBackgroundColor(Color.RED);
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}




