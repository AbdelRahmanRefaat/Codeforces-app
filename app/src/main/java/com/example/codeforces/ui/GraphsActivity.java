package com.example.codeforces.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codeforces.AppUtils;
import com.example.codeforces.Models.UserSubmissionModel;
import com.example.codeforces.R;
import com.example.codeforces.Models.UserRatingModel;
import com.example.codeforces.interfaces.iUserRatingAPI;
import com.example.codeforces.interfaces.iUserSubmissionAPI;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraphsActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    // TODO: to be used later :v

    public static final String EXTRA_HANDLE =
            "com.example.codeforces.EXTRA_HANDLE";
    public static final String EXTRA_RANK =
            "com.example.codeforces.EXTRA_RANK";
    public static final String EXTRA_MAX_RANK =
            "com.example.codeforces.EXTRA_MAX_RANK";
    public static final String EXTRA_NAME =
            "com.example.codeforces.EXTRA_NAME";
    public static final String EXTRA_CURRENT_RATING =
            "com.example.codeforces.CURRENT_RATING";
    public static final String EXTRA_MAX_RATING =
            "com.example.codeforces.EXTRA_MAX_RATING";
    public static final String EXTRA_IMAGE =
            "com.example.codeforces.EXTRA_IMAGE";

    private static final String TAG = "GraphsActivity";
    public static final String BASE_URL = "https://codeforces.com/api/";
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private int BASE_WIDTH;
    private int BASE_HEIGHT;

    private static final int EXTRA_SLICE_SIZE = 20;
    private static final int FRAME_MARGIN = 10;
    private LinearLayout.LayoutParams frameParams;


    // info widgets
    ImageView image;
    TextView handleTV;
    TextView rankTV;
    TextView maxRankTV;
    TextView ratingTV;
    TextView maxRatingTV;


    // zoom control buttons
    private Button zoomControl_1;
    private Button zoomControl_2;
    private Button zoomControl_3;

    // frame layouts
    private FrameLayout frame_UserSolvedTags;
    private FrameLayout frame_UserVerdicts;
    private FrameLayout frame_UserInfro;
    private FrameLayout frame_UserCFGraph;

    // line chart
    private LineChart mChart;
    private List<Entry> entries;
    private LineDataSet mLineDataSet;
    private LineData mLineData;

    private List<Integer> colors;

    // pie charts
    PieChart verdictPieChart;
    PieChart tagsPieChart;


    // data sets
    UserRatingModel userRatingModelData;
    UserSubmissionModel userSubmissionModelData;

    private Retrofit retrofit;
    private iUserRatingAPI mUserRatingApi;
    private iUserSubmissionAPI mUserSubmissionApi;

    private Toast toast;

    boolean isZoomed_1 = false;
    boolean isZoomed_2 = false;
    boolean isZoomed_3 = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: Creating some graphs...");
        setContentView(R.layout.activity_graphs);
        setTitle("Charts & Graphs");
        init_Widgets();
        init_ScreenHW();
        init_ZoomControlButtons();
        init_Colors();
        setupRetrofit(); // setting up retrofit


        mChart = (LineChart) findViewById(R.id.line_chart_graph_cf);
        mChart.setDoubleTapToZoomEnabled(true);

        verdictPieChart = (PieChart) findViewById(R.id.pie_chart_user_verdicts);

        tagsPieChart = (PieChart) findViewById(R.id.pie_chart_user_tags);


        verdictPieChart.setOnChartValueSelectedListener(this);
        tagsPieChart.setOnChartValueSelectedListener(this);



        Intent data = getIntent();
        String handle = data.getStringExtra(EXTRA_HANDLE);
        int rating = data.getIntExtra(EXTRA_CURRENT_RATING, 0);
        int maxRating = data.getIntExtra(EXTRA_MAX_RATING, 0);
        String rank = data.getStringExtra(EXTRA_RANK);
        String maxRank = data.getStringExtra(EXTRA_MAX_RANK);
        byte[] img = data.getByteArrayExtra(EXTRA_IMAGE);

        setTitle(handle);
        if(image != null) {

            image.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

        }else{

            image.setImageDrawable(getDrawable(R.drawable.ic_wael_ghonem));

        }

        handleTV.append(handle);
        rankTV.append(rank);
        maxRankTV.append(maxRank);

        ratingTV.append(String.valueOf(rating));
        maxRatingTV.append(String.valueOf(maxRating));


        zoomControl_3.callOnClick(); // this is for making the tags pie chart full screen at first
        drawCFGraph(handle);
        drawPieCharts(handle);


    }

    private void init_Colors() {
        colors = AppUtils.createColors();
    }

    public void init_ScreenHW() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels - 200;
        BASE_WIDTH = frame_UserVerdicts.getLayoutParams().width;
        BASE_HEIGHT = frame_UserVerdicts.getLayoutParams().height;

    }

    private void init_Widgets() {

        image = findViewById(R.id.graph_info_image);
        handleTV = findViewById(R.id.graph_info_handle);
        rankTV = findViewById(R.id.graph_info_rank);
        maxRankTV = findViewById(R.id.graph_info_max_rank);
        ratingTV = findViewById(R.id.graph_info_rating);
        maxRatingTV = findViewById(R.id.graph_info_max_rating);


        frame_UserInfro = (FrameLayout) findViewById(R.id.frame_user_info);
        frame_UserCFGraph = (FrameLayout) findViewById(R.id.frame_user_cfgraph);
        frame_UserVerdicts = (FrameLayout) findViewById(R.id.frame_user_verdicts_pie);
        frame_UserSolvedTags = (FrameLayout) findViewById(R.id.frame_user_solved_tags_pie);

        zoomControl_1 = (Button) findViewById(R.id.zoom_control_1);
        zoomControl_2 = (Button) findViewById(R.id.zoom_control_2);
        zoomControl_3 = (Button) findViewById(R.id.zoom_control_3);


    }



    private void setupRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private void drawCFGraph(String handle) {
        mUserRatingApi = retrofit.create(iUserRatingAPI.class);
        Call<UserRatingModel> call = mUserRatingApi.getUserRatingChanges(handle);
        call.enqueue(new Callback<UserRatingModel>() {
            @Override
            public void onResponse(Call<UserRatingModel> call, Response<UserRatingModel> response) {
                Log.d(TAG, "onResponse: User Rating OK...");
                userRatingModelData = response.body();
                drawLineChartGraph(userRatingModelData);

            }

            @Override
            public void onFailure(Call<UserRatingModel> call, Throwable t) {
                Log.d(TAG, "onFailure: FAILED...");
            }
        });
    }

    private void drawPieCharts(String handle) {
        mUserSubmissionApi = retrofit.create(iUserSubmissionAPI.class);
        Call<UserSubmissionModel> call = mUserSubmissionApi.getUserSubmissions(handle);
        call.enqueue(new Callback<UserSubmissionModel>() {
            @Override
            public void onResponse(Call<UserSubmissionModel> call, Response<UserSubmissionModel> response) {
                Log.d(TAG, "onResponse: User Submission OK...");
                userSubmissionModelData = response.body();
                drawVerdictPieChartGraph(userSubmissionModelData);
                drawTagsPieChartGraph(userSubmissionModelData);

            }

            @Override
            public void onFailure(Call<UserSubmissionModel> call, Throwable t) {

            }
        });
    }


    private void drawLineChartGraph(UserRatingModel result) {
        if(result == null)
                return ;

        entries = new ArrayList<Entry>();
        Long x;
        Integer y;
        for (UserRatingModel.Result i : result.getResult()) {
            x = i.getRatingUpdateTimeSeconds();
            y = i.getNewRating();
            entries.add(new Entry(x, y));
        }

        mLineDataSet = new LineDataSet(entries, "CF-Graph");
        mLineDataSet.setColor(Color.BLUE);

        mLineData = new LineData(mLineDataSet);
        mLineData.setDrawValues(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new DateXAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(30f);


        YAxis yAxis = mChart.getAxisLeft();

        mChart.getAxisRight().setEnabled(false);
        mChart.setVisibleXRangeMaximum(3);
        mChart.setVisibleXRangeMinimum(2);
        mChart.setData(mLineData);
        mChart.invalidate();
    }

    private void drawVerdictPieChartGraph(UserSubmissionModel data) {

        if(data == null)
                return ;
        verdictPieChart.getDescription().setEnabled(false);
        verdictPieChart.setExtraOffsets(10, 10, 10, 10);
        verdictPieChart.setUsePercentValues(false);
        verdictPieChart.setDragDecelerationFrictionCoef(0.95f);

        verdictPieChart.setDrawHoleEnabled(true);
        verdictPieChart.setHoleColor(Color.WHITE);
        verdictPieChart.setTransparentCircleRadius(50f);

        verdictPieChart.setDrawEntryLabels(true);
        verdictPieChart.setEntryLabelColor(Color.BLACK);
        verdictPieChart.animateXY(3000, 3000);


        ArrayList<PieEntry> entries = getVerdictsPieEntries(data);


        Legend legend = verdictPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.setYOffset(10f);


        PieDataSet dataSet = new PieDataSet(entries, "");
        // dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(true);
        dataSet.setColors(colors);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueTextColors(colors);
        dataSet.setUsingSliceColorAsValueLineColor(true);
        dataSet.setValueTextSize(12f);
        dataSet.setValueLinePart2Length(0.2f);
        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueFormatter(new DefaultAxisValueFormatter(0));


        PieData pieData = new PieData(dataSet);
        // pieData.setValueTextColor(Color.BLUE);

        verdictPieChart.setData(pieData);
        verdictPieChart.invalidate();
    }

    private void drawTagsPieChartGraph(UserSubmissionModel data) {

        Log.d(TAG, "drawTagsPieChartGraph: drwaing tags pie chart graph...");
        tagsPieChart.getDescription().setEnabled(false);
        tagsPieChart.setExtraOffsets(10, 10, 10, 10);
        tagsPieChart.setUsePercentValues(false);
        tagsPieChart.setDragDecelerationFrictionCoef(0.95f);

        tagsPieChart.setDrawHoleEnabled(false);
        tagsPieChart.setHoleColor(Color.WHITE);
        tagsPieChart.setTransparentCircleRadius(50f);

        tagsPieChart.setDrawEntryLabels(false);
        tagsPieChart.setEntryLabelColor(Color.BLACK);
        tagsPieChart.animateXY(5000, 5000);


        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        ArrayList<PieEntry> entries = getTagsPieEntries(data, legendEntries);

        Legend legend = tagsPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.setYOffset(10f);
        legend.setXOffset(10f);
        legend.setCustom(legendEntries);
        Log.d(TAG, "drawTagsPieChartGraph: Legend Entries size = " + legendEntries.size());


        PieDataSet dataSet = new PieDataSet(entries, "");
        // dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(true);
        dataSet.setColors(colors);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueTextColors(colors);
        dataSet.setUsingSliceColorAsValueLineColor(true);
        dataSet.setValueTextSize(12f);
        dataSet.setValueLinePart2Length(0.6f);
        dataSet.setValueLinePart1Length(0.7f);
        dataSet.setValueLineVariableLength(true);
        dataSet.setValueLinePart1OffsetPercentage(15f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueFormatter(new DefaultAxisValueFormatter(0));


        PieData pieData = new PieData(dataSet);
        // pieData.setValueTextColor(Color.BLUE);

        tagsPieChart.setData(pieData);
        tagsPieChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        if (toast != null)
            toast.cancel();
        PieEntry p = (PieEntry) e;
        toast = Toast.makeText(getApplicationContext(), p.getLabel() + String.format(" %d", (int) e.getY()), Toast.LENGTH_SHORT);
        toast.show();
        Log.d("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }


    ArrayList<PieEntry> getVerdictsPieEntries(UserSubmissionModel data) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        HashMap<String, Integer> freq = new HashMap<>();

        // empty data
        freq.put("OK", 0);
        freq.put("WRONG_ANSWER", 0);
        freq.put("TIME_LIMIT_EXCEEDED", 0);
        freq.put("RUNTIME_ERROR", 0);
        freq.put("MEMORY_LIMIT_EXCEEDED", 0);
        freq.put("SKIPPED", 0);
        freq.put("CHALLENGED", 0);
        freq.put("IDLENESS_LIMIT_EXCEEDED", 0);
        freq.put("FAILED", 0);
        freq.put("PARTIAL", 0);
        freq.put("COMPILATION_ERROR", 0);
        freq.put("PRESENTATION_ERROR", 0);
        freq.put("SECURITY_VIOLATED", 0);
        freq.put("CRASHED", 0);
        freq.put("INPUT_PREPARATION_CRASHED", 0);
        freq.put("TESTING", 0);
        freq.put("REJECTED", 0);

        String verdict;

        Log.d(TAG, "drawVerdictPieChartGraph: SIZEEEEEEE" + data.getResult().size());

        for (int i = 0; i < data.getResult().size(); ++i) {

            verdict = data.getResult().get(i).getVerdict();

            if (verdict.equals("TESTING"))
                continue;

            if (freq.containsKey(verdict)) {

                freq.put(verdict, freq.get(verdict) + 1);

            } else {

                freq.put(verdict, 1);
            }
        }
        for (Map.Entry entry : freq.entrySet()) {
            Log.d(TAG, "drawVerdictPieChartGraph: " + entry.getKey() + " " + entry.getValue());
        }
        String accepted = "AC";
        Integer AC = freq.get("OK");
        String wrong_answer = "WA";
        Integer WA = freq.get("WRONG_ANSWER");
        String time_limit = "TLE";
        Integer TLE = freq.get("TIME_LIMIT_EXCEEDED");
        String runtime_error = "RTE";
        Integer RTE = freq.get("RUNTIME_ERROR");
        String memory_limit_exceed = "MLE";
        Integer MLE = freq.get("MEMORY_LIMIT_EXCEEDED");
        String others = "Others";
        Integer others_cnt = freq.get("SKIPPED") + freq.get("CHALLENGED") + freq.get("IDLENESS_LIMIT_EXCEEDED");

        entries.add(new PieEntry(WA, wrong_answer));
        entries.add(new PieEntry(MLE, memory_limit_exceed));
        entries.add(new PieEntry(TLE, time_limit));
        entries.add(new PieEntry(others_cnt, others));
        entries.add(new PieEntry(AC, accepted));
        entries.add(new PieEntry(RTE, runtime_error));


        return entries;
    }

    ArrayList<PieEntry> getTagsPieEntries(UserSubmissionModel data, ArrayList<LegendEntry> legendEntries) {


        ArrayList<PieEntry> entries = new ArrayList<>();

        HashMap<String, Integer> freq = new HashMap<>();

        HashMap<String, Boolean> visited = new HashMap<>();

        ArrayList<String> tags;

        String problem;

        Log.d(TAG, "drawVerdictPieChartGraph: pie data size = " + data.getResult().size());

        for (int i = 0; i < data.getResult().size(); ++i) {

            if (!data.getResult().get(i).getVerdict().equals("OK"))
                continue; // not solved yet

            problem = data.getResult().get(i).getProblem().getName();


            // already solved this problem
            if (visited.containsKey(problem) && visited.get(problem) == true)
                continue;

            visited.put(problem, true);

            tags = (ArrayList<String>) data.getResult().get(i).getProblem().getTags();

            for (int j = 0; j < tags.size(); ++j) {

                if (freq.containsKey(tags.get(j))) {

                    freq.put(tags.get(j), freq.get(tags.get(j)) + 1);

                } else {

                    freq.put(tags.get(j), 1);
                }
            }
        }
        int pos = 0;
        for (Map.Entry entry : freq.entrySet()) {

            Log.d(TAG, "drawVerdictPieChartGraph: " + entry.getKey() + " " + entry.getValue());

            entries.add(new PieEntry((int) entry.getValue(), entry.getKey().toString()));

            legendEntries.add(new LegendEntry(
                    entry.getKey() + ": " + entry.getValue(),
                    Legend.LegendForm.CIRCLE,
                    10f,
                    2f,
                    null,
                    colors.get(pos % colors.size())
            ));
            pos++;
        }
        return entries;
    }
    private void init_ZoomControlButtons() {
        zoomControl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZoomed_1) {
                    zoomControl_1.setBackgroundResource(R.drawable.ic_fullscreen_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, BASE_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserCFGraph.setLayoutParams(frameParams);
                } else {
                    zoomControl_1.setBackgroundResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, SCREEN_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserCFGraph.setLayoutParams(frameParams);
                }
                isZoomed_1 = !isZoomed_1;
            }
        });

        zoomControl_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZoomed_2) {
                    zoomControl_2.setBackgroundResource(R.drawable.ic_fullscreen_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, BASE_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserVerdicts.setLayoutParams(frameParams);
                } else {
                    zoomControl_2.setBackgroundResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, SCREEN_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserVerdicts.setLayoutParams(frameParams);
                }
                isZoomed_2 = !isZoomed_2;
            }
        });

        zoomControl_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZoomed_3) {
                    zoomControl_3.setBackgroundResource(R.drawable.ic_fullscreen_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, BASE_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserSolvedTags.setLayoutParams(frameParams);
                } else {
                    zoomControl_3.setBackgroundResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    frameParams = new LinearLayout.LayoutParams(BASE_WIDTH, SCREEN_HEIGHT);
                    frameParams.setMargins(FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN, FRAME_MARGIN);
                    frame_UserSolvedTags.setLayoutParams(frameParams);
                }
                isZoomed_3 = !isZoomed_3;
            }
        });


    }

}
