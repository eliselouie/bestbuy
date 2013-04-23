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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * An Activity that is started when a user opens a Notification.
 */
public class NotificationDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.notification_details);
		Intent intent = getIntent();
		
		Item item = (Item) intent.getParcelableExtra("item");
		TextView nameLabel = (TextView) findViewById(R.id.name);
		if (item == null){
			nameLabel.setText("No item passed in to display");
			return;
		}
		
		nameLabel.setText(item.getName() + "(" + item.getName() + ")");
		
		double current = intent.getDoubleExtra("curr_price", 0.0);
		TextView currentLabel = (TextView) findViewById(R.id.current);
		currentLabel.setText("Current Price: $" + current);
		
		TextView minLabel = (TextView) findViewById(R.id.min);
		if (current < item.getCurrPrice()){
			minLabel.setText("Current price is less than last time $" + 
					item.getCurrPrice());
			minLabel.setTextColor(0xFFFF0000);
		} 
	}

}
