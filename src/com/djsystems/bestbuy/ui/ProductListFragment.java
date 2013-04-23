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

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.djsystems.bestbuy.R;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.util.ServerUtilities;

public class ProductListFragment extends ListFragment{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private ArrayList<HashMap<Item, Bitmap>> listItems = new ArrayList<HashMap<Item, Bitmap>>();
	
	private EditText editText;
	private Button btnSearch;
	private ProductAdapter adapter;
	private int cachedPageNum = 1;
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	public ProductListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product,
				container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		adapter = new ProductAdapter(getActivity());
		setListAdapter(adapter);
		final boolean loadMore = true;
		
		// set a listener to be invoked when the list reaches the end
				((LoadMoreListView) getListView())
						.setOnLoadMoreListener(new OnLoadMoreListener() {
							public void onLoadMore() {
								// Do the work to load more items at the end of list
								// here
								new DownloadTask(loadMore).execute(editText.getText().toString());
							}
						});
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id){
		Item itemClicked = null;
		super.onListItemClick(lv, v, position, id);
		@SuppressWarnings("unchecked")
		HashMap<Item, Bitmap> hashMap = (HashMap<Item, Bitmap>)lv.getItemAtPosition(position);
		
		for(Item key: hashMap.keySet()){
			itemClicked = key;
		}
		 
	    Intent intent = new Intent();
	    intent.setClass(getActivity(), ProductDetailActivity.class);
	    intent.putExtra("the_item", itemClicked);
	    startActivity(intent);    
	}

	@Override
	public void onStart(){
		super.onStart();
		btnSearch = (Button)getActivity().findViewById(R.id.btnSearch);	
		btnSearch.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				((LoadMoreListView) getListView()).onLoadMoreComplete();
				View parent = (View)v.getParent();
				if(parent != null){
					editText = (EditText) parent
						.findViewById(R.id.editTextSearch);			
				String text = editText.getText().toString();				
				new DownloadTask(false).execute(text);			
				}
			}
		});
	}
	
	static class ViewHolder {
	    public TextView textName;
	    public TextView textPrice;
	    public ImageView image;
	  }
	
	public class ProductAdapter extends BaseAdapter {
		
		private LayoutInflater layoutInflater = null;
		
		public ProductAdapter(Context context) {
		      this.layoutInflater = LayoutInflater.from(context);
		   }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.product_list_item, null);
				holder.image = (ImageView)convertView.findViewById(R.id.itemImage);
				holder.textName = (TextView)convertView.findViewById(R.id.lblItemName);
				holder.textPrice = (TextView)convertView.findViewById(R.id.lblItemPrice);
				convertView.setTag(holder); 
			}else {
				holder = (ViewHolder)convertView.getTag();
			}
			Item theItem = null;
			Bitmap theImage = null;
			HashMap<Item, Bitmap> theHashMap = listItems.get(position);
			
			for(Item key: theHashMap.keySet()){
				theItem = key;
				theImage = theHashMap.get(theItem);
			}
					
			holder.image.setImageBitmap(theImage);
			holder.textName.setText(theItem.getName());
			holder.textPrice.setText("$" + Double.toString(theItem.getCurrPrice()));
			return convertView;
		}	
	}
	
	private class DownloadTask extends AsyncTask<String, Void, ArrayList<HashMap<Item, Bitmap>>>{
		private boolean loadMore = false;
		
		DownloadTask(boolean isLoadMore){
			loadMore = isLoadMore;
		}
		
		@Override
		protected ArrayList<HashMap<Item, Bitmap>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			cachedPageNum ++;
			if(!loadMore){
				cachedPageNum = 1;
			//	Log.d("doInBackground", "loadMore cachedPageName = " + cachedPageNum);
			}			
			//Log.d("doInBackground", "newLoad cachedPageName = " + cachedPageNum);
			return ServerUtilities.searchProduct(params[0], cachedPageNum);
		}
		
		protected void onPostExecute(ArrayList<HashMap<Item, Bitmap>> result){
			// put the item ArrayList into HashMap ArrayList
			if(result != null){
				if(loadMore){
					listItems.addAll(result);
				} else {
					listItems = result;
				}		
				adapter.notifyDataSetChanged();
				((LoadMoreListView) getListView()).onLoadMoreComplete();
			}
		}
		
		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}
	}

}
