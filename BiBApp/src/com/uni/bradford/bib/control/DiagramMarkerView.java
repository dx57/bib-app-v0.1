package com.uni.bradford.bib.control;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;
import com.uni.bradford.bib.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class DiagramMarkerView extends MarkerView 
{
    private TextView tvContent;

    public DiagramMarkerView(Context context, int layoutResource) 
    {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content
    @Override
    public void refreshContent(Entry e, int dataSetIndex) 
    {
    	tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
    }

    @Override
    public float getXOffset() 
    {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public float getYOffset() 
    {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
