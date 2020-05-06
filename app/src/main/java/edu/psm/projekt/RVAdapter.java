package edu.psm.projekt;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RViewHolder> {

    private Cursor cData;
    private LayoutInflater layoutInflater;
    private IItemClickListener mClickListener;

    public RVAdapter(Context context, Cursor mData) {
        this.layoutInflater = LayoutInflater.from(context);
        this.cData = mData;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return  new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {

        if (cData.moveToPosition(position)){
            holder.hour.setText(String.valueOf(cData.getString(cData.getColumnIndex("HOUR"))));
            holder.minute.setText(String.valueOf(cData.getString(cData.getColumnIndex("MINUTE"))));
            holder.name.setText(String.valueOf(cData.getString(cData.getColumnIndex("NAME"))));
            holder.description.setText(String.valueOf(cData.getString(cData.getColumnIndex("DESCRIPTION"))));
        }

    }

    @Override
    public int getItemCount() {
        return cData.getCount();
    }

    public class RViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView hour, minute, name, description;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemCLick(v, getAdapterPosition());
        }
    }

    public HashMap<String, String> getItem(int id) {
        HashMap<String, String> item = null;
        if (cData.moveToPosition(id)){
            item = new HashMap<String, String>();
            item.put("ID", String.valueOf(cData.getInt(cData.getColumnIndex("ID"))));
            item.put("DATE", String.valueOf(cData.getString(cData.getColumnIndex("DATE"))));
            item.put("HOUR", String.valueOf(cData.getString(cData.getColumnIndex("HOUR"))));
            item.put("MINUTE", String.valueOf(cData.getString(cData.getColumnIndex("MINUTE"))));
            item.put("NAME", String.valueOf(cData.getString(cData.getColumnIndex("NAME"))));
            item.put("DESCRIPTION", String.valueOf(cData.getString(cData.getColumnIndex("DESCRIPTION"))));

        }
        return item;
    }

    public void swapCursor(Cursor newCursor){
        if (cData!=null)
            cData.close();
        cData = newCursor;
        if (cData!=null)notifyDataSetChanged();
    }

    public interface IItemClickListener{
        void onItemCLick(View view, int position);
    }

    public void setClickListener(IItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
}
