package com.example.walletapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {
    ArrayList<PieEntry> pieEntries;
    private PieChart pieChart;
//    ArrayList<BarEntry> barEntries;
//    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        pieChart = findViewById(R.id.pieChart);
        pieEntries = new ArrayList<>();
        loadPieChart();

//        barChart = findViewById(R.id.barChart);
//        barEntries = new ArrayList<>();
//        loadBarChart();

        Button btn_back = findViewById(R.id.btn_summaryBack);
        btn_back.setOnClickListener(l -> finish());
    }

    private void loadPieChart() {
        HashMap<String, Float> hashMap = new HashMap<>();
        hashMap.put("Bill", 0f);
        hashMap.put("Food", 0f);
        hashMap.put("Gas", 0f);
        hashMap.put("Holidays", 0f);
        float other=0f;
        for (Expense e : User.expenses) {
            try {
                float temp = hashMap.get(e.category);
                temp += Float.parseFloat(e.cost);
                hashMap.replace(e.category, temp);
            } catch (Exception ex){
                other += Float.parseFloat(e.cost);
            }
        }
        try {
            pieEntries.add(new PieEntry(hashMap.get("Bill"), "Bill"));
            pieEntries.add(new PieEntry(hashMap.get("Food"), "Food"));
            pieEntries.add(new PieEntry(hashMap.get("Gas"), "Gas"));
            pieEntries.add(new PieEntry(hashMap.get("Holidays"), "Holidays"));
        } catch (Exception ignored){

        }
        if(other > 0){
            pieEntries.add(new PieEntry(other, "Other"));
        }
        float sum = hashMap.get("Bill")+ hashMap.get("Gas")+ hashMap.get("Food")+ hashMap.get("Holidays")+other;
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
        pieChart.animateXY(1200, 1200);
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
//        for(Expense e : User.expenses){
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