package project.dang.daniel.acpm_coverflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CoverFlowAdapter extends BaseAdapter {
    ///////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    private ArrayList<Programme> programmesList = new ArrayList<>(0);
    private Context mContext;

    ///////////////////////////////////////////////////////////////////////////////
    //2: Constructor
    public CoverFlowAdapter(Context context, ArrayList<Programme> programmesList) {
        this.mContext = context;
        this.programmesList = programmesList;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //3: Add a ViewHolder Class that link to "programme_view.xml" layout
    private static class ViewHolder {
        private TextView programme_title;
        private ImageView programme_image;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //4: Get converted view: Make the link to programme_view.xml layout
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.programme_view, null, false);
            //Assign values to "programme_title" and "programme_image" of viewHolder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.programme_title = (TextView) rowView.findViewById(R.id.programme_title);
            viewHolder.programme_image = (ImageView) rowView.findViewById(R.id.programme_image);
            //
            rowView.setTag(viewHolder);
        }

        //Assign values to "programme_title" and "programme_image" of holder
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.programme_image.setImageResource(programmesList.get(position).getProgramme_image());
        holder.programme_title.setText(programmesList.get(position).getProgramme_title());

        //Return value
        return rowView;
    }

    ///////////////////////////////////////////////////////////////////////////////
    //5: Override getCount() method: return the size of programmesList
    @Override
    public int getCount() {
        return programmesList.size();
    }

    ///////////////////////////////////////////////////////////////////////////////
    //6: Override getItem() method: return which game item has been selected
    @Override
    public Programme getItem(int position) {
        return programmesList.get(position);
    }

    ///////////////////////////////////////////////////////////////////////////////
    //7: Override getItemId() method: return the position of selected item
    @Override
    public long getItemId(int position) {
        return position;
    }
}
