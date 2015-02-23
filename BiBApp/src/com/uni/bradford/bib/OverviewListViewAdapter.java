package com.uni.bradford.bib;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
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
			// TODO: Add behaviour
			System.out.println("Button Entry clicked");	

			switch (position)
			{
				case 0: 
				{
					// Change to HeightVisual activity 
					Intent changeToHeightVisual = new Intent(overviewActivity, HeightVisualActivity.class);
					overviewActivity.startActivity(changeToHeightVisual);
	
					break;
				}
				case 1: 
				{
					// Change to diagram height activity 
					Intent changeToDiagramHeight = new Intent(overviewActivity, HeightDiagramActivity.class);
					overviewActivity.startActivity(changeToDiagramHeight);
	
					break;
				}
				case 2: 
				{
					// Change to Survey activity 
					Intent changeToSurvey = new Intent(overviewActivity, SurveyActivity.class);
					overviewActivity.startActivity(changeToSurvey);
	
					break; 
				}
				case 3: 
				{
					// Change to About activity 
					Intent changeToAbout = new Intent(overviewActivity, AboutActivity.class);
					overviewActivity.startActivity(changeToAbout);
	
					break; 
				}
				case 4: 
				{
	
					break;
				}
				default:
				{
					System.out.println("Default: No behaviour specified");
					break;
				}
			}

		}	
	}
}
