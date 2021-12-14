package com.psteam.foodlocationbusiness.fragments;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import static java.lang.Float.parseFloat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerRestaurantBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Get.messageResDetail;
import com.psteam.lib.Models.Get.messageStatistic;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerRestaurantFragment extends Fragment implements OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {

    private boolean isSelected = true;
    private int status;
    private String[] xData = { "Chờ xét duyệt", "Đã duyệt", "Hoàn tất", "Quá hạn" };
    private ArrayList<String> options=new ArrayList<String>();
    private ArrayList<String> options1=new ArrayList<String>();
    private ArrayList<String> options2=new ArrayList<String>();
    float[] yData = {2.5F, 8.5F, 9.5F, 10F};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentManagerRestaurantBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerRestaurantBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();


    }

    private void setSpinnerDate(){
        String date = java.time.LocalDate.now()+"";

        String[] time = date.split("-");
        int year = Integer.parseInt(time[0]);
        int month = Integer.parseInt(time[1]);

        for(int i = year-100; i <= year; i ++){
            options.add(i+"");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,options);
        binding.spinnerYear.setAdapter(adapter);
        binding.spinnerYear.setSelection(options.indexOf(time[0]));

        for(int i = 0; i <= 12 ; i ++){
            options1.add(i+"");
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,options1);
        binding.spinnerMonth.setAdapter(adapter1);
        binding.spinnerMonth.setSelection(options1.indexOf(time[1]));

        int dayMax = 31;
        if(month == 4 || month == 6 || month == 9 || month == 11){
            dayMax = 3;
        }else if(month == 2){
            if(year % 4 == 0){
                dayMax = 29;
            }else {
                dayMax = 28;
            }
        }
        for(int i = 0; i <= dayMax ; i ++){
            options2.add(i+"");
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,options2);
        binding.spinnerDay.setAdapter(adapter2);
        binding.spinnerDay.setSelection(options2.indexOf(time[2]));

        //getStatisticRes(time[2], time[1], time[0]);
    }

    private void setChart(){
        binding.piechart.setRotationEnabled(true);
        binding.piechart.setDescription(new Description());
        binding.piechart.setHoleRadius(35f);
        binding.piechart.setTransparentCircleAlpha(0);
        binding.piechart.setCenterText("Thống kê");
        binding.piechart.setCenterTextSize(14);
//        binding.piechart.setDrawEntryLabels(true);

        addDataSet(binding.piechart);

        binding.piechart.setOnChartValueSelectedListener(this);
    }

    private void addDataSet(PieChart pieChart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }
        for (int i = 0; i < xData.length;i++){
            xEntrys.add(xData[i]);
        }
        PieDataSet pieDataSet=new PieDataSet(yEntrys,"Chờ xét duyệt/Đã duyệt/Hoàn tất/Quá hạn");
        pieDataSet.setSliceSpace(4);
        pieDataSet.setValueTextSize(15);
        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);
        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void init() {
        getResDetail();
        binding.spinnerYear.setOnItemSelectedListener(this);
        binding.spinnerMonth.setOnItemSelectedListener(this);
        binding.spinnerDay.setOnItemSelectedListener(this);
        setSpinnerDate();

//        status=1;
//        isSelected = status != 1 ? true : false;
//        if(isSelected) {
//            binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
//            binding.textStatus.setText("Mở cửa");
//        }else {
//            binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
//            binding.textStatus.setText("Đóng cửa");
//        }

        binding.buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                    binding.buttonSwitch.playAnimation();
                    binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.textStatus.setText("Đóng cửa");
                        }
                    });
                    isSelected = false;
                } else {
                    binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                    binding.buttonSwitch.playAnimation();
                    binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.textStatus.setText("Mở cửa");
                        }
                    });

                    isSelected = true;
                }

                updateStatus(isSelected);
            }
        });
    }

    public void getResDetail(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageResDetail> call = serviceAPI_lib.getResDetail(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageResDetail>() {
            @Override
            public void onResponse(Call<messageResDetail> call, Response<messageResDetail> response) {
                if(response.body().getStatus() == 1){
                    binding.amountDay.setText(response.body().getResDetail().getAmountDay()+"");
                    binding.amountWeek.setText(response.body().getResDetail().getAmountWeek()+"");

                    if (!response.body().getResDetail().isStatus()) {
                        binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                        binding.buttonSwitch.playAnimation();
                        binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.textStatus.setText("Đóng cửa");
                            }
                        });
                        isSelected = false;
                    } else {
                        binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                        binding.buttonSwitch.playAnimation();
                        binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.textStatus.setText("Mở cửa");
                            }
                        });

                        isSelected = true;
                    }
                }
                Toast.makeText(getActivity(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<messageResDetail> call, Throwable t) {
                Toast.makeText(getActivity(), "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(boolean status1){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.changeStatus(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), status1);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(getActivity(), xData[(int) h.getX()]+" có "+e.getY()+" phiếu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerYear){
            int month = Integer.parseInt(binding.spinnerMonth.getSelectedItem().toString());

            if(month == 2){
                int year = Integer.parseInt(binding.spinnerYear.getItemAtPosition(position).toString());
                int dayMax = 28;

                if(year % 4 == 0){
                    dayMax = 29;
                }

                options2.clear();
                for(int i = 0; i <= dayMax ; i ++){
                    options2.add(i+"");
                }
            }
        }else if(parent.getId() == R.id.spinnerMonth){
            int year = Integer.parseInt(binding.spinnerYear.getSelectedItem().toString());
            int month = Integer.parseInt(binding.spinnerMonth.getItemAtPosition(position).toString());

            options2.clear();

            int dayMax = 31;
            if(month == 4 || month == 6 || month == 9 || month == 11){
                dayMax = 3;
            }else if(month == 2){
                if(year % 4 == 0){
                    dayMax = 29;
                }else {
                    dayMax = 28;
                }
            }
            for(int i = 0; i <= dayMax ; i ++){
                options2.add(i+"");
            }
        }

        getStatisticRes(binding.spinnerDay.getSelectedItem()+"", binding.spinnerMonth.getSelectedItem()+"", binding.spinnerYear.getSelectedItem()+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getStatisticRes(String day, String month, String year){
        DataTokenAndUserId data = new DataTokenAndUserId(getContext());

        ServiceAPI_lib  serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageStatistic> call = serviceAPI_lib.getStaticRes(data.getToken(), data.getUserId(), data.getRestaurantId(), day, month, year);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {
                if(response.body().getStatus() == 1){
                    yData[0] = parseFloat(response.body().getGetStatic().getAmountWait());
                    yData[1] = parseFloat(response.body().getGetStatic().getAmountConfirm());
                    yData[2] = parseFloat(response.body().getGetStatic().getAmountComplete());
                    yData[3] = parseFloat(response.body().getGetStatic().getAmountExpired());

                    setChart();
                }
            }

            @Override
            public void onFailure(Call<messageStatistic> call, Throwable t) {

            }
        });
    }
}