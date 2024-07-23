package info.androidhive.sqlite.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.MataKuliah;

public class MataKuliahAdapter extends RecyclerView.Adapter<MataKuliahAdapter.MyViewHolder> {

    private Context context;
    private List<MataKuliah> mataKuliahList;
    private MataKuliahAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaMataKuliah;
        public TextView kodeMataKuliah;
        public TextView sksMataKuliah;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public MyViewHolder(View view) {
            super(view);
            namaMataKuliah = view.findViewById(R.id.nama_mata_kuliah);
            kodeMataKuliah = view.findViewById(R.id.kode_mata_kuliah);
            sksMataKuliah = view.findViewById(R.id.sks_mata_kuliah);
            editButton = view.findViewById(R.id.edit_mk_button);
            deleteButton = view.findViewById(R.id.delete_mk_button);
        }
    }

    public MataKuliahAdapter(Context context, List<MataKuliah> mataKuliahList, MataKuliahAdapterListener listener) {
        this.context = context;
        this.mataKuliahList = mataKuliahList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mata_kuliah_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MataKuliah mataKuliah = mataKuliahList.get(position);
        holder.namaMataKuliah.setText(mataKuliah.getNamaMataKuliah());
        holder.kodeMataKuliah.setText(mataKuliah.getKodeMataKuliah());
        holder.sksMataKuliah.setText(String.valueOf(mataKuliah.getSksMataKuliah()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditButtonClicked(mataKuliah, position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteButtonClicked(mataKuliah, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mataKuliahList.size();
    }

    public interface MataKuliahAdapterListener {
        void onEditButtonClicked(MataKuliah mataKuliah, int position);
        void onDeleteButtonClicked(MataKuliah mataKuliah, int position);
    }
}
