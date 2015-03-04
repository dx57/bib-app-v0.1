package com.uni.bradford.bib;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class OverviewListViewAdapter extends BaseAdapter
{
	private ArrayList<OverviewEntry> arrayList;
	private OverviewActivity overviewActivity;
	
	public static final String SURVEY_URL = "survey-url";

	public OverviewListViewAdapter(ArrayList<OverviewEntry> overviewList, OverviewActivity overviewActivity)
	{
		this.arrayList = overviewList;
		this.overviewActivity = overviewActivity;
	}

	@Override
	public int getCount()
	{
		return arrayList.size();
	}

	@Override
	public OverviewEntry getItem(int position)
	{
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return (long)position;
	} 

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Set entry
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_list_row,null);

		// Configure list entry
		Button btnEntry = (Button)convertView.findViewById(R.id.btnEntry);
		btnEntry.setText(this.arrayList.get(position).getName());
		btnEntry.setEnabled(true);
		btnEntry.setCompoundDrawablesWithIntrinsicBounds(this.arrayList.get(position).getDrawableRef(), 0, 0, 0);
		btnEntry.setOnClickListener(new OnBtnEntryClickListener(position));
		
		return convertView;
	}

	private class OnBtnEntryClickListener implements OnClickListener
	{
		private int position;

		public OnBtnEntryClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View view)
		{
			System.out.println("Button Entry clicked");	
					
			overviewActivity.startActivityForResult(arrayList.get(position).getIntent(), arrayList.get(position).getRequestCode());
		}	
	}
}
