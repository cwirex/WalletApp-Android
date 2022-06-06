package com.example.walletapp.summary;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walletapp.DBS;
import com.example.walletapp.R;
import com.example.walletapp.UserData;
import com.example.walletapp.expense.Expense;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {
    ArrayList<PieEntry> pieEntries;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6D00")));

        pieChart = findViewById(R.id.pieChart);
        pieEntries = new ArrayList<>();
        loadPieChart();

        Button btn_back = findViewById(R.id.btn_summaryBack);
        btn_back.setOnClickListener(l -> finish());
    }

    private void loadPieChart() {
        HashMap<String, Float> hashMap = new HashMap<>();
        hashMap.put(DBS.CATEGORIES.bill, 0f);
        hashMap.put(DBS.CATEGORIES.food, 0f);
        hashMap.put(DBS.CATEGORIES.gas, 0f);
        hashMap.put(DBS.CATEGORIES.holidays, 0f);
        float other = 0f;
        for (Expense e : UserData.expenses) {
            try {
                float temp = hashMap.get(e.category);
                temp += Float.parseFloat(e.cost);
                hashMap.replace(e.category, temp);
            } catch (Exception ex) {
                other += Float.parseFloat(e.cost);
            }
        }

        for(String key : hashMap.keySet()){
            if(hashMap.get(key) > 0){
                pieEntries.add(new PieEntry(hashMap.get(key), key));
            }
        }
        if (other > 0) {
            pieEntries.add(new PieEntry(other, "Other"));
        }
        float sum = hashMap.get("Bill") + hashMap.get("Gas") + hashMap.get("Food") + hashMap.get("Holidays") + other;
        displayPieChart(sum);
    }

    private void displayPieChart(float total) {
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(String.format(Locale.US, "Total\n%.2f", total));
        pieChart.setCenterTextSize(19f);
        pieChart.animateXY(1400, 1400, Easing.EaseInOutQuad);
        pieChart.animate();
    }

//    private void displayBarChart() {
//        BarDataSet barDataSet = new BarDataSet(barEntries, "expenses");
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);
//        BarData bardata = new BarData(barDataSet);
//
//        barChart.setFitBars(true);
//        barChart.setData(bardata);
//        barChart.getDescription().setText("some text");
//    }
//
//    private void loadBarChart() {
//        int P = 30;
//        LocalDateTime now = LocalDateTime.now();
//        Float[] history = new Float[P];
//        for(int i = 0; i < P; i++) history[i] = 0f;
//        for(Expense e : UserData.expenses){
//            long days_between = Duration.between(e.dateTime, now).toDays();
//            if(days_between < P){
//                history[(int) days_between] += Float.parseFloat(e.cost);
//            }
//        }
//        for(int i = 0; i < P; i++){
//            if(history[i] > 0)
//                barEntries.add(new BarEntry(i, history[i]));
//        }
//        displayBarChart();
//    }
//

}