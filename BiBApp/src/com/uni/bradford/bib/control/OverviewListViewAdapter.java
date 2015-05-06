package com.uni.bradford.bib.control;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.uni.bradford.bib.R;

/**
 * Class to deal with navigation list within ActivityOverview
 * 
 * @author Martin
 */
public class OverviewListViewAdapter extends BaseAdapter
{
	private ArrayList<OverviewEntry> arrayList;
	private OverviewActivity overviewActivity;

	/**
	 * Initialise the the overview list
	 * 
	 * @param overviewList List of list entries
	 * @param overviewActivity Reference for OverViewActivity
	 */
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

	/**
	 * Class to deal with selection of list entries
	 */
	private class OnBtnEntryClickListener implements OnClickListener
	{
		private int position;

		/**
		 * Initialise the the overview list
		 * 
		 * @param position Position of the list entry within the list
		 */
		public OnBtnEntryClickListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onClick(View view)
		{
			// Start the correct Activity with the help of the referenced intent					
			overviewActivity.startActivity(arrayList.get(position).getIntent());
		}	
	}
}
