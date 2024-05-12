package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.doan_nhom_6.Model.ReportTotal;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.AdminAPI;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsSoldActivity extends AppCompatActivity {

    String datefrom, dateto;
    TextView title, dateformto;
    AnyChartView anyChartView;
    Button btnStartRevenue, btnChooseDateFrom, btnChooseDateTo;
    LinearLayout chartLayout;
    Button btnexportfile;
    List<ReportTotal> reportTotals = new ArrayList<>();

    Column column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_sold);
        setControl();
        setEvent();
    }

    private void setupChart() {
        if (dateto == null) {
            Calendar calendar = Calendar.getInstance();
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH);
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
            dateto = yearNow + "-" + (monthNow + 1) + "-" + dayNow;
            btnChooseDateTo.setText(dateto);
        }
        if (datefrom == null) {
            datefrom = "2000-01-01";
            btnChooseDateFrom.setText(datefrom);
        }
        AdminAPI.adminApi.QuantityStatistic(datefrom, dateto).enqueue(new Callback<List<ReportTotal>>() {
            @Override
            public void onResponse(Call<List<ReportTotal>> call, Response<List<ReportTotal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportTotals = response.body();
                    if (reportTotals.isEmpty()) {
                        // Hiển thị thông báo nếu không có dữ liệu
                        Toast.makeText(ProductsSoldActivity.this, "Không có dữ liệu", Toast.LENGTH_LONG).show();
                    } else {
                        chartLayout.setVisibility(View.VISIBLE);
                        dateformto.setText("Từ: " + datefrom + "\nĐến: " + dateto);
                        // Tạo danh sách dữ liệu để vẽ biểu đồ
                        List<DataEntry> data = new ArrayList<>();
                        double totalRevenue = 0;
                        for (ReportTotal reportTotal : reportTotals) {
                            data.add(new ValueDataEntry(reportTotal.getName(), reportTotal.getValue()));
                            totalRevenue += reportTotal.getValue();
                        }
                        Cartesian cartesian = AnyChart.column();
                        // Nếu column chưa được khởi tạo, thực hiện khởi tạo
                        if (column == null) {
                            // Tạo biểu đồ cột
                            column = cartesian.column(data);

                            // Cấu hình tooltip
                            column.tooltip()
                                    .titleFormat("{%X}")
                                    .position(Position.CENTER_BOTTOM)
                                    .anchor(Anchor.CENTER_BOTTOM)
                                    .offsetX(0d)
                                    .offsetY(5d)
                                    .format("{%Value} sản phẩm");
                            // Cấu hình biểu đồ
                            cartesian.animation(true);
                            cartesian.yScale().minimum(0d);
                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                            cartesian.interactivity().hoverMode(HoverMode.BY_X);
                            cartesian.xAxis(0).title("Mặt Hàng");
                            cartesian.yAxis(0).title("Số lượng");

                            // Hiển thị biểu đồ mới
                            anyChartView.setChart(cartesian);
                        } else {
                            // Nếu column đã được khởi tạo, chỉ cập nhật dữ liệu
                            column.data(data);
                        }

                    }
                } else {
                    // Xử lý khi response không thành công hoặc không có dữ liệu
                    Toast.makeText(ProductsSoldActivity.this, "Không thể lấy dữ liệu", Toast.LENGTH_LONG).show();
                    chartLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReportTotal>> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Toast.makeText(ProductsSoldActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setEvent() {
        startRevenueOnclick();
        exportFileOnclick();

    }

    private void exportFileOnclick() {
        btnexportfile.setOnClickListener(new View.OnClickListener() {
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
        headers.add("Tổng sản phẩm");

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
            fileOutput = new File(storageVolume.getDirectory().getPath() +"/Download/ThongKeSanPhamDaBan.xls");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            hssfWorkbook.close();
            Toast.makeText(this, "File Created Successfully: Download/ThongKeSanPhamDaBan.xls", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "File Creation Failed", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    private void startRevenueOnclick() {
        btnStartRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupChart();
            }
        });
    }

    private void setControl() {
        title = findViewById(R.id.title);
        chartLayout = findViewById(R.id.layoutchart);
        btnChooseDateFrom = findViewById(R.id.btnChooseDateFrom);
        btnChooseDateTo = findViewById(R.id.btnChooseDateTo);
        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        chartLayout.setVisibility(View.INVISIBLE);
        btnStartRevenue = findViewById(R.id.btnStartRevenue);
        dateformto = findViewById(R.id.datefromto);
        btnexportfile = findViewById(R.id.btnexportfile);
    }

    public void showDatePickerDialogFrom(View v) {
        // Lấy ngày tháng năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int yearfrom = calendar.get(Calendar.YEAR);
        int monthfrom = calendar.get(Calendar.MONTH);
        int dayfrom = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày tháng
                        String selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        btnChooseDateFrom.setText(selectedDate);
                        datefrom = selectedDate;
                    }
                }, yearfrom, monthfrom, dayfrom);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    public void showDatePickerDialogTo(View v) {
        // Lấy ngày tháng năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int yearto = calendar.get(Calendar.YEAR);
        int monthto = calendar.get(Calendar.MONTH);
        int dayto = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày tháng
                        String selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        btnChooseDateTo.setText(selectedDate);
                        dateto = selectedDate;
                    }

                }, yearto, monthto, dayto);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    public void onBackClicked(View view) {
        finish();
    }

}
