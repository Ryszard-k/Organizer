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

/**
 * Klasa Adaptera odpowiadająca za oprogramowanie Recycler View
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RViewHolder> {

    /**
     * Referencje do Cursora, LayoutInflatera oraz interfejsu ItemClick
     */
    private Cursor cData;
    private LayoutInflater layoutInflater;
    private IItemClickListener mClickListener;

    /**
     * Konstruktor inicjalizujący Layout oraz Cursor
     * @param context Context bierzący
     * @param mData Cursor
     */
    public RVAdapter(Context context, Cursor mData) {
        this.layoutInflater = LayoutInflater.from(context);
        this.cData = mData;
    }

    /**
     * Metoda onCreate tworząca obecny widok za pomocą layoutInflatera
     * @param parent Widok na którym budowany jest obecny holder
     * @param viewType Typ budowanego widoku
     * @return Zwraca nam nowy obiekt holdera z zadanym widokiem
     */
    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return  new RViewHolder(view);
    }

    /**
     * Metoda która pobiera dane z Cursora i załadowuje do holdera
     * @param holder Nowy holder
     * @param position Pozycja Cursora
     */
    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {

        if (cData.moveToPosition(position)){
            holder.hour.setText(String.valueOf(cData.getString(cData.getColumnIndex("HOUR"))));
            holder.minute.setText(String.valueOf(cData.getString(cData.getColumnIndex("MINUTE"))));
            holder.name.setText(String.valueOf(cData.getString(cData.getColumnIndex("NAME"))));
            holder.description.setText(String.valueOf(cData.getString(cData.getColumnIndex("DESCRIPTION"))));
        }

    }

    /**
     * Metoda zliczająca zawartość Cursora
     * @return Zwraca obecną liczbę itemów w Cursorze
     */
    @Override
    public int getItemCount() {
        return cData.getCount();
    }

    /**
     * Zagnieżdżona klasa Holdera posiadająca kontrolki i własny layout
     */
    public class RViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView hour, minute, name, description;

        /**
         * Kontruktor podpinający kontrolki oraz interfejs kliknięcia na holder
         * @param itemView Obecny widok
         */
        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        /**
         * Metoda onClick na dany holder
         * @param v Obecny widok
         */
        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemCLick(v, getAdapterPosition());
        }
    }

    /**
     * Metoda HashMap pobierająca dane z bazy danych i przechowująca dane w obiekcie item
     * @param id Pozycja Cursora
     * @return Zwraca obiekt rodzaju Hashmap z pobranymi danymi
     */
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

    /**
     * Metoda podmiany Cursora podczas czytania danych z bazy danych
     * @param newCursor Nowy Cursor który podmienia stary
     */
    public void swapCursor(Cursor newCursor){
        if (cData!=null)
            cData.close();
        cData = newCursor;
        if (cData!=null)notifyDataSetChanged();
    }

    /**
     * Interfejs implementujący metode kliknięcia na dany Holder w adapterze
     */
    public interface IItemClickListener{
        void onItemCLick(View view, int position);
    }

    /**
     * Metoda pozwalająca na implementacje interfejsu
     * @param itemClickListener
     */
    public void setClickListener(IItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
}
