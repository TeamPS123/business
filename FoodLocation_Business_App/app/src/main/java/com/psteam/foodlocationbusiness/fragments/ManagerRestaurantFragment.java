package com.psteam.foodlocationbusiness.fragments;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import static java.lang.Float.parseFloat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.slider.LabelFormatter;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerRestaurantBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutAddMenuNameDialogBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutStatisticBinding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.Para;
import com.psteam.lib.Models.Get.getStatistic;
import com.psteam.lib.Models.Get.messageResDetail;
import com.psteam.lib.Models.Get.messageStatistic;
import com.psteam.lib.Models.Input.inputStatisticWithMonthAndYear;
import com.psteam.lib.Models.Input.inputStatisticWithYear;
import com.psteam.lib.Models.Insert.insertMenu;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerRestaurantFragment extends Fragment implements OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {

    private boolean isSelected = true;
    private AlertDialog dialog;
    private List<getStatistic> statistics = new ArrayList<>();

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

    private void setBarChar(){
        ArrayList NoOfEmp = new ArrayList();

        float start = 0f;

        for (int i = 0; i < statistics.size(); i++) {
            NoOfEmp.add(new BarEntry(start, Integer.parseInt(statistics.get(i).getAmountComplete())));
            NoOfEmp.add(new BarEntry(start+1f, Integer.parseInt(statistics.get(i).getAmountExpired())));

            start = start + 3f ;
        }

        String[] labels = new String[statistics.size()];
        for(int i = 0; i < statistics.size(); i ++){
            labels[i] = statistics.get(i).getTime();
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Hoàn tất/Quá hạn");
        binding.piechart.animateY(1000);
        XAxis xAxis = binding.piechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        //binding.piechart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
        BarData data = new BarData(bardataset);
        int[] a = {Color.MAGENTA, Color.RED};
        bardataset.setColors(ColorTemplate.createColors(a));
        binding.piechart.setData(data);
    }

    private void setChart(){

        binding.piechart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.piechart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        binding.piechart.setPinchZoom(false);

        binding.piechart.setDrawGridBackground(false);
        binding.piechart.setDrawBarShadow(false);

        binding.piechart.setDrawValueAboveBar(false);
        binding.piechart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = binding.piechart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        binding.piechart.getAxisRight().setEnabled(false);

        String[] labels = new String[statistics.size()];
        for(int i = 0; i < statistics.size(); i ++){
            labels[i] = statistics.get(i).getTime();
        }

        XAxis xAxis = binding.piechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        Legend l = binding.piechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < statistics.size(); i++) {
            float val1 = Float.parseFloat(statistics.get(i).getAmountComplete());
            float val2 = Float.parseFloat(statistics.get(i).getAmountExpired());

            yVals1.add(new BarEntry(i,
                    new float[]{val1, val2},
                    getResources().getDrawable(R.drawable.ic_arrow_down)));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"Hoàn tất", "Quá hạn"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextColor(Color.WHITE);

        binding.piechart.setData(data);

//        if (binding.piechart.getData() != null &&
//                binding.piechart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) binding.piechart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            binding.piechart.getData().notifyDataChanged();
//            binding.piechart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, "");
//            set1.setDrawIcons(false);
//            set1.setColors(getColors());
//            set1.setStackLabels(new String[]{"Hoàn tất", "Quá hạn"});
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new LargeValueFormatter());
//            data.setValueTextColor(Color.WHITE);
//
//            binding.piechart.setData(data);
//        }
//
//        binding.piechart.setFitBars(true);
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[2];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 2);

        return colors;
    }

    private void init() {
        getResDetail();
//        binding.spinnerYear.setOnItemSelectedListener(this);
//        binding.spinnerMonth.setOnItemSelectedListener(this);
//        binding.spinnerDay.setOnItemSelectedListener(this);
//        setSpinnerDate();

        setListener();

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
        Toast.makeText(getActivity(), " phiếu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(parent.getId() == R.id.spinnerYear){
//            int month = Integer.parseInt(binding.spinnerMonth.getSelectedItem().toString());
//
//            if(month == 2){
//                int year = Integer.parseInt(binding.spinnerYear.getItemAtPosition(position).toString());
//                int dayMax = 28;
//
//                if(year % 4 == 0){
//                    dayMax = 29;
//                }
//
//                options2.clear();
//                for(int i = 0; i <= dayMax ; i ++){
//                    options2.add(i+"");
//                }
//            }
//        }else if(parent.getId() == R.id.spinnerMonth){
//            int year = Integer.parseInt(binding.spinnerYear.getSelectedItem().toString());
//            int month = Integer.parseInt(binding.spinnerMonth.getItemAtPosition(position).toString());
//
//            options2.clear();
//
//            int dayMax = 31;
//            if(month == 4 || month == 6 || month == 9 || month == 11){
//                dayMax = 3;
//            }else if(month == 2){
//                if(year % 4 == 0){
//                    dayMax = 29;
//                }else {
//                    dayMax = 28;
//                }
//            }
//            for(int i = 0; i <= dayMax ; i ++){
//                options2.add(i+"");
//            }
//        }
//
//        getStatisticRes(binding.spinnerDay.getSelectedItem()+"", binding.spinnerMonth.getSelectedItem()+"", binding.spinnerYear.getSelectedItem()+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setListener(){
        binding.btnMonth.setOnClickListener((v) -> {
            openDialog(1);
        });

        binding.btnYear.setOnClickListener((v) -> {
            openDialog(2);
        });
    }

    private void getStatisticRes(String month1, String year1, String month2, String year2){
        DataTokenAndUserId data = new DataTokenAndUserId(getContext());

        inputStatisticWithMonthAndYear input = new inputStatisticWithMonthAndYear();
        input.setUserId(data.getUserId());
        input.setRestaurantId(data.getRestaurantId());
        input.setYear1(year1);
        input.setYear2(year2);
        input.setMonth2(month2);
        input.setMonth1(month1);

        ServiceAPI_lib  serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageStatistic> call = serviceAPI_lib.getStaticResWithMonthAndYear(data.getToken(), input);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {
                if(response.body().getStatus() == 1){
                    statistics = response.body().getGetStatic();

                    setChart();
                }
            }

            @Override
            public void onFailure(Call<messageStatistic> call, Throwable t) {

            }
        });
    }

    private void getStatisticResWithYear(String year1, String year2){
        DataTokenAndUserId data = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);

        inputStatisticWithYear input = new inputStatisticWithYear();
        input.setUserId(data.getUserId());
        input.setRestaurantId(data.getRestaurantId());
        input.setYear1(year1);
        input.setYear2(year2);

        Call<messageStatistic> call = serviceAPI_lib.getStaticResWithYear(data.getToken(), input);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {

            }

            @Override
            public void onFailure(Call<messageStatistic> call, Throwable t) {

            }
        });
    }

    private void openDialog(int code) {
        final String[] dateStart = new String[2];
        final String[] dateEnd = new String[2];

        final LayoutStatisticBinding layoutStatisticBinding
                = LayoutStatisticBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutStatisticBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        // Use the calendar for create ranges
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(2010, 0, 1); // Set minimum date to show in dialog
        long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        calendar.clear();
        calendar.set(2021, 12, 31); // Set maximum date to show in dialog
        long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        layoutStatisticBinding.txtDateStart.setOnClickListener((v) -> {
            if(code == 1) {
                // Create instance with date ranges values
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(11, 2020, minDate, maxDate);

                dialogFragment.show(getActivity().getSupportFragmentManager(), null);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        dateStart[0] = (monthOfYear + 1)+"";
                        dateStart[1] = year+"";

                        layoutStatisticBinding.txtDateStart.setText(dateStart[0]+"/"+dateStart[1]);
                    }
                });
            }
        });

        layoutStatisticBinding.txtDateEnd.setOnClickListener((v) -> {
            // Create instance with date ranges values
            MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                    .getInstance(11, 2020, minDate, maxDate);

            dialogFragment.show(getActivity().getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int year, int monthOfYear) {
                    dateEnd[0] = (monthOfYear+1)+"";
                    dateEnd[1] = year+"";

                    layoutStatisticBinding.txtDateEnd.setText(dateEnd[0]+"/"+dateEnd[1]);
                }
            });
        });

        layoutStatisticBinding.btnStatistic.setOnClickListener((v) -> {
            binding.txtStatistic.setText("Thống kê từ " + dateStart[0]+"/"+dateStart[1] +" đến "+dateEnd[0]+"/"+dateEnd[1]);

            getStatisticRes(dateStart[0], dateStart[1], dateEnd[0], dateEnd[1]);

            dialog.dismiss();
        });


        dialog.show();
    }
}