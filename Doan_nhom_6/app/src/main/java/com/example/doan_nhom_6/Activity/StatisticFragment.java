package com.example.doan_nhom_6.Activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.doan_nhom_6.Adapter.StatisticAdapter;
import com.example.doan_nhom_6.Model.Statistic;
import com.example.doan_nhom_6.R;
import java.util.ArrayList;

public class StatisticFragment extends Fragment {
    ListView listView;
    View view;
    ArrayList<Statistic> data = new ArrayList<>();
    StatisticAdapter statisticAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        setControl();
        setEvent();
        return view;
    }

    private void setControl() {
        listView = view.findViewById(R.id.lvStatistic);
//        data.add(new Statistic(1, "Doanh thu", "Xem thông tin về doanh thu của cửa hàng"));
//        data.add(new Statistic(2, "Sản phẩm bán ra", "Xem thống kê về tổng số lượng sản phẩm được bán ra"));
        data.add(new Statistic(3, "Sản phẩm", "Xem thống kê về tổng số lượng sản phẩm và phân loại của chúng trong kho"));
        data.add(new Statistic(4, "Đơn vị sản phẩm", "Xem thống kê về tổng số lượng đơn vị sản phẩm và phân loại của chúng trong kho"));
        statisticAdapter = new StatisticAdapter(getContext(), R.layout.activity_statistic_row, data);
        listView.setAdapter(statisticAdapter);
    }

    private void setEvent() {
    }
}
