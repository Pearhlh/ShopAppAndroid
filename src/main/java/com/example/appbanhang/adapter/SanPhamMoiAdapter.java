package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    private List<SanPhamMoi> array;
    private Context context;

    public SanPhamMoiAdapter(Context context,List<SanPhamMoi> array) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public SanPhamMoiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMoiAdapter.MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtTen.setText(sanPhamMoi.getTensp());
        holder.txtGia.setText("Giá: "+sanPhamMoi.getGia()+"đ");
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imgsanphammoi);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen,txtGia;
        ImageView imgsanphammoi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.itemsp_ten);
            txtGia = itemView.findViewById(R.id.itemsp_gia);
            imgsanphammoi = itemView.findViewById(R.id.itemsp_image);
        }
    }
}
