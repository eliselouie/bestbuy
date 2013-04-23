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
package com.djsystems.bestbuy.services;

import java.util.ArrayList;

import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.provider.DataManager;
import com.djsystems.bestbuy.ui.NotificationDetails;
import com.djsystems.bestbuy.util.DateTimeUtility;
import com.djsystems.bestbuy.util.JsonProductParser;
import com.djsystems.bestbuy.BestbuyApp;
import com.djsystems.bestbuy.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
/**
 * A service to query the item current price. Called periodically
 * by {@link com.djsystems.bestbuy.services.QueryItemStartupReceiver QueryItemStartupReceiver}.
 * 
 * @author Elise Louie
 *
 */
public class QueryItemService extends IntentService{
	private BestbuyApp app = null;
	public QueryItemService() {
		super("QueryItemService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		checkForAlertAndUpdateDb();
	}
	
	/**
	 * Internal helper method to check whether the notification should be sent
	 * and update the database for items on the watch list when it's necessary.
	 */
	private void checkForAlertAndUpdateDb(){
		app = (BestbuyApp) getApplication();
		DataManager dataManager = app.getDataManager();
		ArrayList<Item> allItemsInDb = (ArrayList<Item>) dataManager.getAllItems();
		for(Item item : allItemsInDb){
			double currPrice = JsonProductParser.getItemCurrentPrice(item);
			if(currPrice == -1) {
				return; // ignore if cannot get the response
			}
			if(currPrice < item.getCurrPrice()) createNotification(item, currPrice);
			if(currPrice != item.getCurrPrice()) {
				item.setCurrPrice(currPrice);
				item.setDate(DateTimeUtility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				dataManager.updateItem(item);
			}
			try {
				// bestbuy api has a limit on request per second
				Thread.sleep(30*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Internal method to create a notification.
	 * @param item The item the notification is target for.
	 * @param currPrice The current price(which is lower than the previous price) of the item. 
	 */
	private void createNotification(Item item, double currPrice){
		NotificationManager mgr = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
			int dollarBill = R.drawable.dollar_icon;
			String shortMsg = "Price dropped alert: " + item.getName();
			long time = System.currentTimeMillis();
			Notification n = new Notification(dollarBill, shortMsg, time);
			
			String title = item.getName();
			String msg = "Current price $" + currPrice + 
				" is lower than before";
			Intent i = new Intent(this, NotificationDetails.class);
			i.putExtra("item", item);
			i.putExtra("curr_price", currPrice);
			PendingIntent pi = PendingIntent.getActivity(this, (int) item.getSku(), i, 0);
		
			n.setLatestEventInfo(this, title, msg, pi);
			n.defaults |= Notification.DEFAULT_SOUND;
			long[] steps = {0, 500, 100, 200, 100, 200};
			n.vibrate = steps;
			n.ledARGB = 0x80009500;
			n.ledOnMS = 250;
			n.ledOffMS = 500;
			n.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
			mgr.notify((int) item.getSku(), n);
	}
}
