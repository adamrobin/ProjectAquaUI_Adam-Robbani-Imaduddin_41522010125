package info.androidhive.sqlite.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, nomorRegistrasi, email, nomorTelepon;

        public MyViewHolder(View view) {
            super(view);
            nama = view.findViewById(R.id.nama);
            nomorRegistrasi = view.findViewById(R.id.nomor_registrasi);
            email = view.findViewById(R.id.email);
            nomorTelepon = view.findViewById(R.id.nomor_telepon);
        }
    }

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nama.setText(user.getNama());
        holder.nomorRegistrasi.setText(user.getNomorRegistrasi());
        holder.email.setText(user.getEmail());
        holder.nomorTelepon.setText(user.getNomorTelepon());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
