package com.example.borna2.trlababalan;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.LinkedList;

import static com.example.borna2.trlababalan.MyGLSurfaceView.graphDataPitchKalman;
import static com.example.borna2.trlababalan.MyGLSurfaceView.graphDataRollKalman;
import static com.example.borna2.trlababalan.MyGLSurfaceView.graphDataYawKalman;
import static com.example.borna2.trlababalan.MyGLSurfaceView2.graphDataPitch;
import static com.example.borna2.trlababalan.MyGLSurfaceView2.graphDataRoll;


/**
 * Created by Borna2 on 07-Apr-17.
 */

public class Method4 extends Fragment {

    public static void setCtx(Context ctx) {
        Method4.ctx = ctx;
    }

    public static Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView =  inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);


        DataPoint[] graphRoll;
        DataPoint[] graphPitch;
        DataPoint[] graphRollKalman;
        DataPoint[] graphPitchKalman;
        DataPoint[] graphYawKalman;

        if(graphDataRoll != null) {
            graphRoll = new DataPoint[graphDataRoll.size()];
            FillGraphData(graphDataRoll, graphRoll);
        }
        else{
            graphRoll = new DataPoint[0];
        }

        if(graphDataPitch != null) {
            graphPitch = new DataPoint[graphDataPitch.size()];
            FillGraphData(graphDataPitch, graphPitch);
        }
        else{
            graphPitch = new DataPoint[0];
        }

        if(graphDataRollKalman != null) {
            graphRollKalman = new DataPoint[graphDataRollKalman.size()];
            FillGraphData(graphDataRollKalman, graphRollKalman);
        }
        else{
            graphRollKalman = new DataPoint[0];
        }
        if(graphDataPitchKalman != null) {
            graphPitchKalman = new DataPoint[graphDataPitchKalman.size()];
            FillGraphData(graphDataPitchKalman, graphPitchKalman);
        }
        else{
            graphPitchKalman = new DataPoint[0];
        }
        if(graphDataYawKalman != null) {
            graphYawKalman = new DataPoint[graphDataYawKalman.size()];
            FillGraphData(graphDataYawKalman, graphYawKalman);
        }
        else{
            graphYawKalman = new DataPoint[0];
        }


        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(graphRoll);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(graphPitch);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(graphRollKalman);
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(graphPitchKalman);
        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(graphYawKalman);

        series1.setTitle("Roll");
        series1.setColor(Color.GREEN);

        series2.setTitle("Pitch");
        series2.setColor(Color.RED);

        series3.setTitle("RollKalman");
        series3.setColor(Color.GREEN);

        series4.setTitle("PitchKalman");
        series4.setColor(Color.RED);

        series5.setTitle("YawKalman");
        series5.setColor(Color.YELLOW);


        graph.addSeries(series1);
        graph.addSeries(series2);

        graph2.addSeries(series3);
        graph2.addSeries(series4);
        graph2.addSeries(series5);

        graph.setTitle("Akcelerometar");
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph2.setTitle("Fuzija senzora");
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setScrollableY(true);
        graph2.getViewport().setScalable(true);
        graph2.getViewport().setScalableY(true);

        return rootView;

        }

    private static void FillGraphData(LinkedList<Double> list, DataPoint[] data) {
        double time = 0;
        for(int i=0;i<list.size();i++){
            DataPoint point =  new DataPoint(time,list.get(i));
            time+=0.001;
            data[i] = point;
        }
        list.clear();

    }
}
