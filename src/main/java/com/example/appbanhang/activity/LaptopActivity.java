package com.example.appbanhang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.DienThoaiAdapter;
import com.example.appbanhang.adapter.LaptopAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LaptopActivity extends AppCompatActivity {

    Toolbar toolbarLt;
    RecyclerView recyclerView;
    LaptopAdapter laptopAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    List<SanPhamMoi> sanPhamMoiList;

    int page = 1;
    int loai;
    private int loadingItemPosition = -1; // Thêm biến lưu chỉ số phần tử loading
    //
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
//
        Anhxa();
        ActionBar();
//
        GetData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
//                add null
                sanPhamMoiList.add(null);
                loadingItemPosition = sanPhamMoiList.size()-1;
                laptopAdapter.notifyItemInserted(loadingItemPosition);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                remove NULL
                if (loadingItemPosition >= 0) {
                    sanPhamMoiList.remove(loadingItemPosition);
                    laptopAdapter.notifyItemRemoved(loadingItemPosition);
                }
                page = page + 1;
                GetData(page);
                laptopAdapter.notifyDataSetChanged();
                isLoading = false;

            }
        },1000);
    }

    private void GetData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                Log.d("DienThoaiActivity", "API call success, received data");
                                if(laptopAdapter == null){
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    laptopAdapter = new LaptopAdapter(getApplicationContext(), sanPhamMoiList);
                                    recyclerView.setAdapter(laptopAdapter);
                                }else{
                                    int vitri = sanPhamMoiList.size();
                                    List<SanPhamMoi> newProducts = sanPhamMoiModel.getResult();
                                    sanPhamMoiList.addAll(newProducts); // Thay vì dùng vòng lặp
                                    laptopAdapter.notifyItemRangeInserted(vitri, newProducts.size());
                                }
                            }else {
                                isLoading = true;
                                Log.w("DienThoaiActivity", "API call success but no data");
                            }
                        },
                        throwable -> {
                            Log.e("DienThoaiActivity", "API call failed", throwable);
                            Toast.makeText(getApplicationContext(),"Khong ket noi server",Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void ActionBar() {
        setSupportActionBar(toolbarLt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLt.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void Anhxa() {
        toolbarLt = findViewById(R.id.toolbar_lt);
        recyclerView = findViewById(R.id.recyleview_lt);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
