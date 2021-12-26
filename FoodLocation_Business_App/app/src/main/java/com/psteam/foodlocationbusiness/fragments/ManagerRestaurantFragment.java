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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.LabelFormatter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerRestaurantBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutAddMenuNameDialogBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutStatisticBinding;
import com.psteam.foodlocationbusiness.ultilities.Constants;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerRestaurantFragment extends Fragment {

    private boolean isSelected = true;
    private AlertDialog dialog;
    private FragmentManagerRestaurantBinding binding;
    private List<getStatistic> statistics;
    private DataTokenAndUserId dataTokenAndUserId;
    private StatisticModel statisticModel;
    private List<getStatistic> temp, tempRemoved;

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
        statisticModel = new StatisticModel("Tuần này", 7);
        getResDetail();
        initData();
        temp = new ArrayList<>();
        tempRemoved = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date firstDayOfWeek = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(java.util.Calendar.DATE, -1);
        Date firstDayOfNextWeek = cal.getTime();

        getStaticResWithStartDateAndEndDate(new InputChart(new SimpleDateFormat("dd/MM/yyyy").format(firstDayOfNextWeek), dataTokenAndUserId.getRestaurantId(),
                new SimpleDateFormat("dd/MM/yyyy").format(firstDayOfWeek), dataTokenAndUserId.getUserId()));
    }


    private void initData() {

        ArrayList<StatisticModel> strings = new ArrayList<>();
        strings.add(new StatisticModel("Tuần này", 7));
        strings.add(new StatisticModel("Tuần trước", -7));
        strings.add(new StatisticModel("Tháng này", 30));
        strings.add(new StatisticModel("Tháng trước", -30));
        strings.add(new StatisticModel("Khác", -1));

        ArrayAdapter<StatisticModel> arrayAdapter = new ArrayAdapter<StatisticModel>(getContext(), android.R.layout.simple_list_item_1, strings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStatistic.setAdapter(arrayAdapter);
        binding.spinnerStatistic.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                statisticModel = (StatisticModel) item;

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                Date startDay;
                Date endDay;

                if (statisticModel.getValue() != -1) {
                    binding.layout6.setVisibility(View.GONE);
                }

                switch (statisticModel.getValue()) {
                    case 7:
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        startDay = cal.getTime();

                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        cal.add(java.util.Calendar.DATE, -1);
                        endDay = cal.getTime();
                        getStaticResWithStartDateAndEndDate(new InputChart(new SimpleDateFormat("dd/MM/yyyy").format(endDay), dataTokenAndUserId.getRestaurantId(),
                                new SimpleDateFormat("dd/MM/yyyy").format(startDay), dataTokenAndUserId.getUserId()));
                        break;
                    case -7:
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        cal.add(Calendar.WEEK_OF_YEAR, -1);
                        startDay = cal.getTime();

                        cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        cal.add(java.util.Calendar.DATE, -1);
                        endDay = cal.getTime();
                        getStaticResWithStartDateAndEndDate(new InputChart(new SimpleDateFormat("dd/MM/yyyy").format(endDay), dataTokenAndUserId.getRestaurantId(),
                                new SimpleDateFormat("dd/MM/yyyy").format(startDay), dataTokenAndUserId.getUserId()));
                        break;
                    case 30:
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        startDay = cal.getTime();

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDay = cal.getTime();
                        getStaticResWithStartDateAndEndDate(new InputChart(new SimpleDateFormat("dd/MM/yyyy").format(endDay), dataTokenAndUserId.getRestaurantId(),
                                new SimpleDateFormat("dd/MM/yyyy").format(startDay), dataTokenAndUserId.getUserId()));
                        break;
                    case -30:
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        cal.add(Calendar.MONTH, -1);
                        startDay = cal.getTime();

                        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        endDay = cal.getTime();
                        getStaticResWithStartDateAndEndDate(new InputChart(new SimpleDateFormat("dd/MM/yyyy").format(endDay), dataTokenAndUserId.getRestaurantId(),
                                new SimpleDateFormat("dd/MM/yyyy").format(startDay), dataTokenAndUserId.getUserId()));
                        break;
                    default:
                        binding.layout6.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void initBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMinimum(0f);
        binding.barChart.getAxisRight().setEnabled(false);

        String[] labels = new String[statistics.size()];
        for (int i = 0; i < statistics.size(); i++) {
            if (statisticModel.getValue() == 7 || statisticModel.getValue() == -7) {
                labels[i] = new SimpleDateFormat("EEEE").format(coverStringToDate(statistics.get(i).getTime()));
            } else {
                labels[i] = statistics.get(i).getTime();
            }
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

        if (labels.length <= 10) {
            binding.barChart.setScaleMinima(1.5f, 1f);
        } else {
            binding.barChart.setScaleMinima(5f, 1f);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setDrawIcons(false);
        barDataSet.setColors(getColors());
        barDataSet.setValueTextSize(6f);
        barDataSet.setStackLabels(new String[]{"Hoàn tất", "Bị huỷ"});

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new LargeValueFormatter());
        barData.setValueTextColor(Color.WHITE);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setFitBars(true);

        binding.barChart.animateY(1200);
        binding.barChart.setDrawValueAboveBar(false);
        binding.barChart.setHighlightFullBarEnabled(false);
        binding.barChart.setPinchZoom(false);

        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setMaxVisibleValueCount(40);
        binding.barChart.setDrawBarShadow(false);

        binding.barChart.setData(barData);
    }


    public List<getStatistic> removeNullItem(List<getStatistic> statistics) {
        List<getStatistic> temp = new ArrayList<>();
        for (getStatistic getStatistic : statistics) {
            if (!getStatistic.getAmountComplete().equals("0") || !getStatistic.getAmountExpired().equals("0")) {
                temp.add(getStatistic);
            }
        }
        return temp;
    }

    public static Date coverStringToDate(String strDate) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(strDate);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
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

                    if (response.body().getResDetail().getStatusCo() !=null) {
                        binding.checkboxStatusCO.setChecked(true);
                        binding.textViewStatusCO.setText(response.body().getResDetail().getStatusCo());
                    } else {
                        binding.checkboxStatusCO.setChecked(false);
                        binding.textViewStatusCO.setText("N/A");
                    }


                    if (!response.body().getResDetail().isStatus()) {
                        binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                        binding.buttonSwitch.playAnimation();
                        binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.textStatus.setText("Đóng cửa");
                                binding.checkboxStatusCO.setText("Ngày mở cửa trở lại");
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
                                binding.checkboxStatusCO.setText("Hẹn ngày đóng cửa");
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

    private int flag = 0;

    public void updateStatus(boolean status1) {
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.changeStatus(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), status1);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    binding.checkboxStatusCO.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    public void updateStatusCo(String statusCo) {
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.changeStatusCO(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), statusCo);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void setListener() {
        binding.inputFromDay.clearFocus();
        binding.inputFromDay.setOnClickListener((v) -> {
            LocalDate date;
            if (v != null && !((EditText) v).getText().toString().isEmpty()) {
                date = LocalDate.parse(((EditText) v).getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                date = LocalDate.now();
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String result = String.format("%s/%s/%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : String.valueOf(dayOfMonth),
                            (month + 1) < 10 ? String.format("0%d", (month + 1)) : (month + 1), year);
                    binding.inputFromDay.setText(result);
                    if (binding.inputToDay != null && !binding.inputToDay.getText().toString().isEmpty()) {
                        getStaticResWithStartDateAndEndDate(new InputChart(binding.inputToDay.getText().toString(), dataTokenAndUserId.getRestaurantId(),
                                result, dataTokenAndUserId.getUserId()));
                    }
                }
            }, date.getYear(), date.getMonthValue() - 1,
                    date.getDayOfMonth());
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.setCancelable(false);
            datePickerDialog.show();
        });

        binding.inputToDay.clearFocus();
        binding.inputToDay.setOnClickListener((v) -> {
            LocalDate date;
            if (v != null && !((EditText) v).getText().toString().isEmpty()) {
                date = LocalDate.parse(((EditText) v).getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                date = LocalDate.now();
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String result = String.format("%s/%s/%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : String.valueOf(dayOfMonth),
                            (month + 1) < 10 ? String.format("0%d", (month + 1)) : (month + 1), year);
                    binding.inputToDay.setText(result);
                    if (binding.inputToDay != null && !binding.inputToDay.getText().toString().isEmpty()) {
                        getStaticResWithStartDateAndEndDate(new InputChart(result, dataTokenAndUserId.getRestaurantId(),
                                binding.inputFromDay.getText().toString(), dataTokenAndUserId.getUserId()));
                    }
                }
            }, date.getYear(), date.getMonthValue() - 1,
                    date.getDayOfMonth());
            if (binding.inputFromDay != null && !binding.inputFromDay.getText().toString().isEmpty()) {
                try {
                    Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(binding.inputFromDay.getText().toString());
                    datePickerDialog.getDatePicker().setMinDate(dt.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            datePickerDialog.setCancelable(false);
            datePickerDialog.show();
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

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.textStatus.setText("Đóng cửa");
                                    binding.checkboxStatusCO.setText("Hẹn ngày mở cửa");

                                }
                            });
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


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.textStatus.setText("Mở cửa");
                                    binding.checkboxStatusCO.setText("Hẹn ngày đóng cửa");

                                }
                            });
                        }
                    });

                    isSelected = true;
                }

                updateStatus(isSelected);
            }
        });

        binding.checkboxStatusCO.setOnClickListener(v -> {
            if (binding.checkboxStatusCO.isChecked()) {
                LocalDate date;
                if (binding.textViewStatusCO != null && !binding.textViewStatusCO.getText().toString().isEmpty() && !binding.textViewStatusCO.getText().toString().equals("N/A")) {
                    date = LocalDate.parse(binding.textViewStatusCO.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    date = LocalDate.now();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = String.format("%s/%s/%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : String.valueOf(dayOfMonth),
                                (month + 1) < 10 ? String.format("0%d", (month + 1)) : (month + 1), year);
                        binding.textViewStatusCO.setText(result);
                        updateStatusCo(result);
                    }
                }, date.getYear(), date.getMonthValue() - 1,
                        date.getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate(new Date(new Date().getTime() + 86000000).getTime());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        binding.checkboxStatusCO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    binding.textViewStatusCO.setText("N/A");
                    updateStatusCo(null);
                }
            }
        });

        binding.textViewStatusCO.setOnClickListener(v -> {

            if (binding.checkboxStatusCO.isChecked()) {
                LocalDate date;
                if (binding.textViewStatusCO != null && binding.textViewStatusCO.getText().toString().isEmpty()  && !binding.textViewStatusCO.getText().toString().equals("N/A")) {
                    date = LocalDate.parse(binding.textViewStatusCO.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    date = LocalDate.now();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = String.format("%s/%s/%d", dayOfMonth < 10 ? String.format("0%d", dayOfMonth) : String.valueOf(dayOfMonth),
                                (month + 1) < 10 ? String.format("0%d", (month + 1)) : (month + 1), year);
                        binding.textViewStatusCO.setText(result);

                    }
                }, date.getYear(), date.getMonthValue() - 1,
                        date.getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate(new Date(new Date().getTime() + 86000000).getTime());
                datePickerDialog.show();
            }
        });


        binding.textViewOnOrOff.setOnClickListener(v -> {
            if (binding.textViewOnOrOff.getText().equals("ON")) {
                statistics.clear();
                statistics.addAll(tempRemoved);
                binding.textViewOnOrOff.setText("OFF");
            } else {
                statistics.clear();
                statistics.addAll(temp);
                binding.textViewOnOrOff.setText("ON");
            }
            initBarChart();
        });

    }

    private void getStaticResWithStartDateAndEndDate(InputChart input) {

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageStatistic> call = serviceAPI_lib.getStaticResWithStartDateAndEndDate(dataTokenAndUserId.getToken(), input);
        call.enqueue(new Callback<messageStatistic>() {
            @Override
            public void onResponse(Call<messageStatistic> call, Response<messageStatistic> response) {
                if (response.body()!=null && response.body().getStatus() == 1) {
                    statistics.clear();
                    statistics.addAll(response.body().getGetStatic());
                    tempRemoved = removeNullItem(statistics);
                    temp.addAll(statistics);
                    initBarChart();
                }
            }

            @Override
            public void onFailure(Call<messageStatistic> call, Throwable t) {

            }
        });
    }

    public class StatisticModel {
        private String name;
        private int value;

        public StatisticModel(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}