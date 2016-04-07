package com.lu.momeymanager.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lu.financemanager.R;
import com.lu.momeymanager.manager.InOutBeanManager;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by lenovo on 2016/4/5.
 */
public class ChartFragement extends BaseFragment{
    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private InOutBeanManager smsManager;
    private int type=0;
//    public static ChartFragement newInstance(int poistion){
//        ChartFragement chartFragement=new ChartFragement();
//        Bundle bundle=new Bundle();
//        bundle.putInt("position", poistion);
//        chartFragement.setArguments(bundle);
//
//        return chartFragement;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        smsManager=InOutBeanManager.getDefault();
        View rootView =inflater.inflate(R.layout.fragment_chart,container,false);
        chart=findViewById(rootView,R.id.chart);
        generateNegativeSubcolumnsData();
        return rootView;
    }
    private void generateNegativeSubcolumnsData() {

        int numSubcolumns = 2;
        int numColumns=0;
        if(type==0){
            numColumns = smsManager.getSimilarDateMoneyBeans().size();
        }else{
            numColumns = smsManager.getAllMoney().size();
        }

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                int sign = getSign();
                values.add(new SubcolumnValue((float) Math.random() * 50f * sign + 5 * sign, ChartUtils.pickColor
                        ()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);
    }
    private int getSign() {
        int[] sign = new int[]{-1, 1};
        return sign[Math.round((float) Math.random())];
    }
}
