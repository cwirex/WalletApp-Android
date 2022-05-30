package com.example.walletapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {
    ArrayList<PieEntry> pieEntries;
    ArrayList<BarEntry> barEntries;
    private PieChart pieChart;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        pieChart = findViewById(R.id.pieChart);
        pieEntries = new ArrayList<>();
        loadPieChart();

        barChart = findViewById(R.id.barChart);
        barEntries = new ArrayList<>();
        loadBarChart();
    }

    private void loadBarChart() {
        LocalDateTime now = LocalDateTime.now();
        for(Expense e : User.expenses){
            //todo
        }
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
        pieChart.animateXY(1000, 1000);
        pieChart.animate();
    }
}