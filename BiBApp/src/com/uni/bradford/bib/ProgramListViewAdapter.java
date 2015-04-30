package com.uni.bradford.bib;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ProgramListViewAdapter extends BaseAdapter
{
	private ArrayList<ProgramEntry> arrayList;
	
	public ProgramListViewAdapter(ArrayList<ProgramEntry> programList, ProfileActivity profileActivity)
	{
		this.arrayList = programList;
	}

	@Override
	public int getCount()
	{
		return arrayList.size();
	}

	@Override
	public ProgramEntry getItem(int position)
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
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_list_row,null);

		// Configure list entry
		CheckBox cbPartOf = (CheckBox)convertView.findViewById(R.id.cbPartOf);
		TextView tvName = (TextView)convertView.findViewById(R.id.tvProgramName);
		TextView tvDescription = (TextView)convertView.findViewById(R.id.tvProgramDescription);
		
		cbPartOf.setChecked(this.arrayList.get(position).isPartOf());
		tvName.setText(this.arrayList.get(position).getName());
		tvDescription.setText(this.arrayList.get(position).getDescription());
				
		return convertView;
	}
}
