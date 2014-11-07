package pe.com.codespace.codigopenal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Carlos on 23/11/13.
 */
public class AdapterExpandableListMain extends BaseExpandableListAdapter {
    private Context context;
    private List<Tools.RowLibro> _listHeader;
    private HashMap<Tools.RowLibro, List<Tools.RowSeccion>> _listChild;

    public AdapterExpandableListMain(Context context, List<Tools.RowLibro> listHeader, HashMap<Tools.RowLibro, List<Tools.RowSeccion>> listChild){
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
        Tools.TextHolderLibro holder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_libro, viewGroup, false);
            holder = new Tools.TextHolderLibro(row);
            row.setTag(holder);
        }
        else{
            holder = (Tools.TextHolderLibro) row.getTag();
        }

        Tools.RowLibro temp = (Tools.RowLibro) getGroup(groupPosition);
        holder.myNumLibro.setText(String.valueOf(temp.numLibro));
        holder.myNombreLibro.setText(temp.nombreLibro);
        holder.myDescripcionLibro.setText(temp.descripLibro);
        //row.setPadding(10,0,0,0);
        //row.setBackgroundColor(Color.BLUE);
        return row;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        View row = view;
        Tools.TextHolderSeccion holder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_seccion, viewGroup, false);
            holder = new Tools.TextHolderSeccion(row);
            row.setTag(holder);
        }
        else {
            holder = (Tools.TextHolderSeccion) row.getTag();
        }

        Tools.RowSeccion temp = (Tools.RowSeccion) getChild(groupPosition, childPosition);
        if(temp != null){
            holder.myNumLibro.setText(String.valueOf(temp.numLibro));
            holder.myNumSeccion.setText(String.valueOf(temp.numSeccion));
            holder.myNombreSeccion.setText(temp.nombreSeccion);
            holder.myDescripcionSeccion.setText(temp.descripSeccion);
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




