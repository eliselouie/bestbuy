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

import com.djsystems.bestbuy.BestbuyApp;
import com.djsystems.bestbuy.R;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.model.PriceHistory;
import com.djsystems.bestbuy.provider.DataManager;
import com.djsystems.bestbuy.services.DeleteFromWatchListService;
import com.djsystems.bestbuy.util.DownloadImage;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
/**
 * Display item detail information
 */
public class ItemDetailActivity extends Activity {
	private Item theItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		Intent i = getIntent();
		theItem = i.getParcelableExtra("item_clicked");
		TextView productName = (TextView)findViewById(R.id.lblItemName);
		TextView productShortDes = (TextView)findViewById(R.id.lblItemShortDesc);
		TextView productLongDes = (TextView)findViewById(R.id.lblItemLongDesc);
		TextView productSku = (TextView)findViewById(R.id.lblItemSku);
		TextView productPrice = (TextView)findViewById(R.id.lblItemPrice);
		TextView productInStoreAvail = (TextView)findViewById(R.id.lblItemInstoreAvail);
		ImageView imageView = (ImageView)findViewById(R.id.itemImage);
		
		new DownloadImage(imageView).execute(theItem.getImageUrl());
		productName.setText(theItem.getName());
		productShortDes.setText(theItem.getShortDescrption());
		productLongDes.setText(theItem.getLongDescption());
		productSku.setText("SKU " + theItem.getSku());
		productPrice.setText("Price: $" + theItem.getCurrPrice());
		productInStoreAvail.setText("Available in store: " + (theItem.getInstoreAvail()==1 ? "Yes": "No"));
		
		// Table view 
		TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
		
	//	TableLayout table = new TableLayout(this);
		TableRow tr = new TableRow(this);
		tr.setBackgroundColor(Color.BLACK);
		tr.setPadding(0, 0, 0, 2); //Border between rows

		TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 0, 2, 0);//2px right-margin

		//New Cell
		LinearLayout headerCell1 = new LinearLayout(this);
		headerCell1.setBackgroundColor(Color.GREEN);
		headerCell1.setLayoutParams(llp);//2px border on the right for the cell


		TextView priceHeader = new TextView(this);
		priceHeader.setText("Price");
		priceHeader.setPadding(0, 0, 4, 3);

		headerCell1.addView(priceHeader);
		tr.addView(headerCell1);
		
		//New Cell
		LinearLayout headerCell2 = new LinearLayout(this);
		headerCell2.setBackgroundColor(Color.GREEN);
		headerCell2.setLayoutParams(llp);//2px border on the right for the cell
		
		TextView dateUpdatedHeader = new TextView(this);
		dateUpdatedHeader.setText("Date updated");
		dateUpdatedHeader.setPadding(0, 0, 4, 3);

		headerCell2.addView(dateUpdatedHeader);
		tr.addView(headerCell2);
		table.addView(tr);
		// Get data from PriceHistory and add all of them to the table
		BestbuyApp app = (BestbuyApp)getApplication();
		DataManager dm = app.getDataManager();
		ArrayList<PriceHistory> itemHistoryPrices = (ArrayList<PriceHistory>) dm.getItemHistoryPrices(theItem);
		for(PriceHistory priceHistory : itemHistoryPrices){
			TableRow trData = new TableRow(this);
			trData.setBackgroundColor(Color.BLACK);
			trData.setPadding(0, 0, 0, 2); //Border between rows

			TableRow.LayoutParams trlp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			trlp.setMargins(0, 0, 2, 0);//2px right-margin

			//New Cell
			LinearLayout dataCell1 = new LinearLayout(this);
			dataCell1.setBackgroundColor(Color.WHITE);
			dataCell1.setLayoutParams(llp);//2px border on the right for the cell


			TextView price = new TextView(this);
			price.setText("$" + priceHistory.getPrice());
			price.setPadding(0, 0, 4, 3);

			dataCell1.addView(price);
			trData.addView(dataCell1);
			
			//New Cell
			LinearLayout dataCell2 = new LinearLayout(this);
			dataCell2.setBackgroundColor(Color.WHITE);
			dataCell2.setLayoutParams(llp);//2px border on the right for the cell

			TextView dateUpdated = new TextView(this);
			dateUpdated.setText(priceHistory.getDate()+"");
			dateUpdated.setPadding(0, 0, 4, 3);

			dataCell2.addView(dateUpdated);
			trData.addView(dataCell2);
			table.addView(trData);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.delete_option){	
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
			        	Intent request = new Intent(ItemDetailActivity.this, DeleteFromWatchListService.class);
						request.putExtra("item", theItem);
						startService(request);
			        	finish();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Delete the item from the watch list?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
		}
		return true;		
	}
}



