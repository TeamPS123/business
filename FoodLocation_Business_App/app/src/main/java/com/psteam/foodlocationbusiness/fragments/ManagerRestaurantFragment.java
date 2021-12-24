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
import com.github.mikephil.charting.charts.BarChart;
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
import com.psteam.lib.Models.Input.InputChart;
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

public class ManagerRestaurantFragment extends Fragment {

    private boolean isSelected = true;
    private AlertDialog dialog;
    private FragmentManagerRestaurantBinding binding;
    private List<getStatistic> statistics;
    private DataTokenAndUserId dataTokenAndUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerRestaurantBinding.inflate(inflater, container, false);
        dataTokenAndUserId = new DataTokenAndUserId(getContext());
        init();
        setListener();
        return binding.getRoot();
    }

    private void init() {
        statistics = new ArrayList<>();
        getResDetail();
        //getStatisticRes("12", "2020", "12", "2021");
        getStaticResWithStartDateAndEndDate(new InputChart("27/12/2021",dataTokenAndUserId.getRestaurantId(),"20/12/2021",dataTokenAndUserId.getUserId()));
    }

    private void initBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setMaxVisibleValueCount(40);
        binding.barChart.setPinchZoom(false);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setGridBackgroundColor(Color.WHITE);
        binding.barChart.setDrawBarShadow(false);
        binding.barChart.setDrawValueAboveBar(false);
        binding.barChart.setScaleMinima(2f, 1f);

        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMinimum(0f);
        binding.barChart.getAxisRight().setEnabled(false);

        String[] labels = new String[statistics.size()];
        for (int i = 0; i < statistics.size(); i++) {
            labels[i] = statistics.get(i).getTime();
        }

        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        Legend l = binding.barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        for (int i = 0; i < statistics.size(); i++) {
            float val1 = Integer.parseInt(statistics.get(i).getAmountComplete());
            float val2 = Integer.parseInt(statistics.get(i).getAmountExpired());

            barEntries.add(new BarEntry(i, new float[]{val1, val2}));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setDrawIcons(false);
        barDataSet.setColors(getColors());
        barDataSet.setValueTextSize(6f);
        barDataSet.setStackLabels(new String[]{"Hoàn tất", "Quá hạn"});

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new LargeValueFormatter());
        barData.setValueTextColor(Color.WHITE);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setFitBars(true);
        binding.barChart.setData(barData);
        binding.barChart.animateY(2000);
        binding.barChart.setDrawValueAboveBar(false);
        binding.barChart.setHighlightFullBarEnabled(false);
        binding.barChart.setPinchZoom(false);

    }

    private int[] getColors() {

        int[] colors = new int[2];

        System.arraycopy(ColorTemplate.COLORFUL_COLORS, 0, colors, 0, 2);

        return colors;
    }

    public void getResDetail() {

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageResDetail> call = serviceAPI_lib.getResDetail(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageResDetail>() {
            @Override
            public void onResponse(Call<messageResDetail> call, Response<messageResDetail> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    binding.amountDay.setText(response.body().getResDetail().getAmountDay() + "");
                    binding.amountWeek.setText(response.body().getResDetail().getAmountWeek() + "");

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
            }

            @Override
            public void onFailure(Call<messageResDetail> call, Throwable t) {
                Toast.makeText(getActivity(), "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(boolean status1) {
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

    private void setListener() {
        binding.btnMonth.setOnClickListener((v) -> {
            openDialog(1);
        });

        binding.btnYear.setOnClickListener((v) -> {
            openDialog(2);
        });

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

    private void getStatisticRes(String month1, String year1, String month2, String year2) {
        DataTokenAndUserId data = new DataTokenAndUserId(getContext());

        inputStatisticWithMonthAndYear input = new inputStatisticWithMonthAndYear();
        input.setUserId(data.getUserId());
        input.setRestaurantId(data.getRestaurantId());
        input.setYear1(year1);
        input.setYear2(year2);
        input.setMonth2(month2);
        input.setMonth1(month1);

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageStatistic> call = serviceAPI_lib.getStaticResWithMonthAndYear(data.getToken(), input);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {
                if (response.body().getStatus() == 1) {
                    statistics.clear();
                    statistics.addAll(response.body().getGetStatic());
                    initBarChart();
                }
            }

            @Override
            public void onFailure(Call<messageStatistic> call, Throwable t) {

            }
        });
    }

    private void getStaticResWithStartDateAndEndDate(InputChart input) {

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageStatistic> call = serviceAPI_lib.getStaticResWithStartDateAndEndDate(dataTokenAndUserId.getToken(), input);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {
                if (response.body().getStatus() == 1) {
                    statistics.clear();
                    statistics.addAll(response.body().getGetStatic());
                    initBarChart();
                }
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
        builder.setCancelable(true);
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
            if (code == 1) {
                // Create instance with date ranges values
                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(11, 2020, minDate, maxDate);

                dialogFragment.show(getActivity().getSupportFragmentManager(), null);


                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        dateStart[0] = (monthOfYear + 1) + "";
                        dateStart[1] = year + "";
                        layoutStatisticBinding.txtDateStart.setText(dateStart[0] + "/" + dateStart[1]);
                    }
                });
            }
        });

        layoutStatisticBinding.txtDateEnd.setOnClickListener((v) -> {
            // Create instance with date ranges values
            MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                    .getInstance(11, 2020, minDate, maxDate);

            dialogFragment.show(getActivity().getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int year, int monthOfYear) {
                    dateEnd[0] = (monthOfYear + 1) + "";
                    dateEnd[1] = year + "";

                    layoutStatisticBinding.txtDateEnd.setText(dateEnd[0] + "/" + dateEnd[1]);
                }
            });
        });

        layoutStatisticBinding.btnStatistic.setOnClickListener((v) -> {
            binding.txtStatistic.setText("Thống kê từ " + dateStart[0] + "/" + dateStart[1] + " đến " + dateEnd[0] + "/" + dateEnd[1]);

            getStatisticRes(dateStart[0], dateStart[1], dateEnd[0], dateEnd[1]);

            dialog.dismiss();
        });

        dialog.show();
    }
}