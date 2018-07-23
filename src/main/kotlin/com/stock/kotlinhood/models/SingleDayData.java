package com.stock.kotlinhood.models;

import java.util.List;



public class SingleDayData {
    private String company;
    // in minutes
    private int interval;

    List<DataUnit> timeSeriesData;
}
