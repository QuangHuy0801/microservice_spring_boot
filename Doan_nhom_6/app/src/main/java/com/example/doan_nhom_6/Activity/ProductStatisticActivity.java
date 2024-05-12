package com.example.doan_nhom_6.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.doan_nhom_6.Model.ReportTotal;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.AdminAPI;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductStatisticActivity extends AppCompatActivity {

    ImageView ivback;
    AnyChartView anyChartView;
    Button btnEPFProduct;
    List<ReportTotal> reportTotals = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_statistic);
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPieChart();
    }

    private void setEvent() {
        ivbackCLick();
        exportFileOnclick();
    }

    private void ivbackCLick() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setControl() {
        anyChartView =findViewById(R.id.ANCProduce);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnEPFProduct=findViewById(R.id.btnEPFProduct);
        ivback=findViewById(R.id.ivback);

    }
    private void setupPieChart(){
        AdminAPI.adminApi.ProductStatistic().enqueue(new Callback<List<ReportTotal>>() {
            @Override
            public void onResponse(Call<List<ReportTotal>> call, Response<List<ReportTotal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportTotals = response.body();
                    if (reportTotals.isEmpty()) {
                        Toast.makeText(ProductStatisticActivity.this, "Không có dữ liệu", Toast.LENGTH_LONG).show();
                    } else {
                        Pie pie = AnyChart.pie();

                        List<DataEntry> data = new ArrayList<>();
                        int totalProducts = 0;
                        for (int i = 0; i < reportTotals.size(); i++){
                            data.add(new ValueDataEntry(reportTotals.get(i).getName(), reportTotals.get(i).getValue()));
                            totalProducts += reportTotals.get(i).getValue();
                        }

                        pie.data(data);
                        pie.palette( new String[]{"#FFCCFF", "#FF3333", "#FFFF33", "#0066FF", "#66FF99","#CCCCCC"});
                        pie.title("Thống kê sản phẩm còn trong kho: "+totalProducts+" sản phẩm");
                        pie.labels().position("outside");

                        pie.legend()
                                .position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                        anyChartView.setChart(pie);
                    }
                } else {
                    Toast.makeText(ProductStatisticActivity.this, "Không thể lấy dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReportTotal>> call, Throwable t) {
                Toast.makeText(ProductStatisticActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void exportFileOnclick() {
        btnEPFProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra và yêu cầu quyền ghi vào bộ nhớ ngoại trước khi tạo tệp Excel
                createExcel();
            }
        });
    }

    public void createExcel() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();


        ArrayList<String> headers = new ArrayList<>();
        headers.add("Mặt hàng");
        headers.add("Số lượng");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(headers.get(i));
        }

        for (int i = 0; i < reportTotals.size(); i++) {
            HSSFRow hssfRow1 = hssfSheet.createRow(i + 1);

            HSSFCell hssfCell = hssfRow1.createCell(0);
            hssfCell.setCellValue(reportTotals.get(i).getName());

            HSSFCell hssfCell1 = hssfRow1.createCell(1);
            hssfCell1.setCellValue(reportTotals.get(i).getValue().toString());
        }
        saveExcel(hssfWorkbook);

    }

    private void saveExcel(HSSFWorkbook hssfWorkbook){
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0); // internal storage

        File fileOutput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            fileOutput = new File(storageVolume.getDirectory().getPath() +"/Download/ThongKeSanPham.xls");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            hssfWorkbook.close();
            Toast.makeText(this, "File Created Successfully: Download/ThongKeSanPham.xls", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "File Creation Failed", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }



}