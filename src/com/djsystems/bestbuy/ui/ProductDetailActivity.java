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

import com.djsystems.bestbuy.R;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.services.AddToWatchListService;
import com.djsystems.bestbuy.util.DownloadImage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Handle the Product Detail screen
 */
public class ProductDetailActivity extends Activity {
	private Item theItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		Intent i = getIntent();
		theItem = i.getParcelableExtra("the_item");
		
		TextView productName = (TextView)findViewById(R.id.lblProductName);
		TextView productShortDes = (TextView)findViewById(R.id.lblProductShortDesc);
		TextView productLongDes = (TextView)findViewById(R.id.lblProductLongDesc);
		TextView productSku = (TextView)findViewById(R.id.lblProductSku);
		TextView productPrice = (TextView)findViewById(R.id.lblProductPrice);
		TextView productInStoreAvail = (TextView)findViewById(R.id.lblProductInstoreAvail);
		ImageView imageView = (ImageView)findViewById(R.id.productImage);
		
		new DownloadImage(imageView).execute(theItem.getImageUrl());
		productName.setText(theItem.getName());
		productShortDes.setText(theItem.getShortDescrption());
		productLongDes.setText(theItem.getLongDescption());
		productSku.setText("SKU " + theItem.getSku());
		productPrice.setText("Price: $" + theItem.getCurrPrice());
		productInStoreAvail.setText("Available in store: " + (theItem.getInstoreAvail()==1 ? "Yes": "No"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.add_option){
			Intent request = new Intent(this, AddToWatchListService.class);
			request.putExtra("item", theItem);
			startService(request);
			Toast.makeText(getBaseContext(), "Item is saved into the watch list", Toast.LENGTH_SHORT).show();
		}
		return true;		
	}	
}



