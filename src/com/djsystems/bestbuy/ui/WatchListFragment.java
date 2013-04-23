/*
 * Copyright 2013 Elise Louie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.djsystems.bestbuy.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.djsystems.bestbuy.R;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.provider.DataManagerImpl;
import com.djsystems.bestbuy.provider.ItemTable;
import com.djsystems.bestbuy.util.DownloadImage;

public class WatchListFragment extends ListFragment{
	private WatchListCursorAdapter wlca;
	private Cursor cursor;
	private DataManagerImpl dataManagerImpl;
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WatchListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_watch,
				container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		dataManagerImpl = new DataManagerImpl(getActivity());
		cursor = dataManagerImpl.getItemCursor();
		wlca = new WatchListCursorAdapter(getActivity(), cursor);
		setListAdapter(wlca);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(wlca != null){
			cursor = dataManagerImpl.getItemCursor();
			wlca.changeCursor(cursor);
		}
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id){
		super.onListItemClick(lv, v, position, id);		
		Cursor c = (Cursor)lv.getAdapter().getItem(position);
		Item itemClicked = new Item();
		itemClicked.setName(c.getString(1));
		itemClicked.setImageUrl(c.getString(2));
		itemClicked.setSku(c.getLong(3));
		itemClicked.setShortDescrption(c.getString(4));
		itemClicked.setLongDescption(c.getString(5));
		itemClicked.setOrderable(c.getString(6));
		itemClicked.setInstoreAvail(c.getInt(7));
		itemClicked.setCurrPrice(c.getDouble(8));
		 if (!cursor.isClosed()) {
	            cursor.close();
	         }
  
	    Intent intent = new Intent();
	    intent.setClass(getActivity(), ItemDetailActivity.class);
	    
	    intent.putExtra("item_clicked", itemClicked);
	    startActivity(intent);    
	}
	
	public class WatchListCursorAdapter extends CursorAdapter{

		@SuppressWarnings("deprecation")
		public WatchListCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView name = (TextView)view.findViewById(R.id.lblItemName);
			TextView price = (TextView)view.findViewById(R.id.lblItemPrice);
			ImageView imageView = (ImageView)view.findViewById(R.id.itemImage);
			String theName = cursor.getString(cursor.getColumnIndex(ItemTable.ItemColumns.NAME));
			double thePrice = cursor.getDouble(cursor.getColumnIndex(ItemTable.ItemColumns.CURRPRICE));
			String url = cursor.getString(cursor.getColumnIndex(ItemTable.ItemColumns.IMAGE_URL));
			new DownloadImage(imageView).execute(url);	
			if(theName != null) name.setText(theName);
			price.setText("$" + Double.toString(thePrice));
		}
	 
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.watch_list_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}				
	}	
}
