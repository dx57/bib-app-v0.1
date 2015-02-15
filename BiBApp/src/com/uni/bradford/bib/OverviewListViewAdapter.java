package com.uni.bradford.bib;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OverviewListViewAdapter extends BaseAdapter
{
	private ArrayList<OverviewEntry> arrayList;
	
	public OverviewListViewAdapter(ArrayList<OverviewEntry> overviewList)
	{
		this.arrayList = overviewList;
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
		
		ImageView imageView_iamge = (ImageView)convertView.findViewById(R.id.entry_image);
		imageView_iamge.setImageResource(this.arrayList.get(position).getDrawableRef()); 
		
		TextView textView_name = (TextView)convertView.findViewById(R.id.entry_name);
		textView_name.setText(this.arrayList.get(position).getName());
		
		TextView textView_details = (TextView)convertView.findViewById(R.id.entry_details);
		textView_details.setText(this.arrayList.get(position).getInformation());
		
		return convertView;
	}
}
