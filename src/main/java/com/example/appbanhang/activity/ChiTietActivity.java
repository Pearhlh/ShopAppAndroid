package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

public class ChiTietActivity extends AppCompatActivity {
    TextView txtTen,txtGia,txtMota;
    ImageView imgChiTiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        initView();

        Intent intent = getIntent();
        // Kiểm tra nếu intent không null và có chứa dữ liệu
        if (intent != null && intent.hasExtra("sanPhamMoi")) {
            SanPhamMoi sanPhamMoi = (SanPhamMoi) intent.getSerializableExtra("sanPhamMoi");

            // Kiểm tra nếu sanPhamMoi không null
            if (sanPhamMoi != null) {
                Log.d("ChiTietActivity", "onCreate: " + sanPhamMoi.getTensp() + " " + sanPhamMoi.getGia() + " " + sanPhamMoi.getHinhanh());
                setView(sanPhamMoi);
            } else {
                Log.e("ChiTietActivity", "SanPhamMoi object is null");
                // Xử lý khi sanPhamMoi là null (hiển thị thông báo lỗi hoặc giao diện lỗi)
                showError("Không thể tải thông tin sản phẩm.");
            }
        } else {
            Log.e("ChiTietActivity", "Intent is null or doesn't have 'sanPhamMoi' extra");
            // Xử lý khi intent không chứa dữ liệu cần thiết
            showError("Dữ liệu sản phẩm không có sẵn.");
        }

    }


    // Hàm hiển thị thông báo lỗi
    private void showError(String message) {
        // Bạn có thể sử dụng Toast, Snackbar hoặc bất kỳ cách nào khác để thông báo lỗi cho người dùng
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setView(SanPhamMoi sanPhamMoi) {
        txtTen.setText(sanPhamMoi.getTensp());
        txtGia.setText(sanPhamMoi.getGia());
        txtMota.setText("Mô tả:\n" +sanPhamMoi.getMota());
        Glide.with(this)
                .load(sanPhamMoi.getHinhanh()) // URL hình ảnh từ sản phẩm
                .placeholder(R.drawable.ic_media_24) // Ảnh thay thế khi đang tải
                .error(R.drawable.ic_media_24) // Ảnh khi có lỗi
                .into(imgChiTiet); // Set hình ảnh vào ImageView
    }
    private void initView() {
        txtTen = findViewById(R.id.itemsp_ten);
        txtGia = findViewById(R.id.itemsp_gia);
        txtMota = findViewById(R.id.itemsp_mota);
        imgChiTiet = findViewById(R.id.itemsp_image);
    }
}
