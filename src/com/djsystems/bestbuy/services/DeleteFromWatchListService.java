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

import com.djsystems.bestbuy.BestbuyApp;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.provider.DataManager;

import android.app.IntentService;
import android.content.Intent;

public class DeleteFromWatchListService extends IntentService{
	public static final String ITEM_EXTRA = "item";
	public DeleteFromWatchListService(){
		super("DeleteFromWatchListService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Item item = intent.getParcelableExtra(ITEM_EXTRA);
		BestbuyApp app = (BestbuyApp)getApplication();
		DataManager dm = app.getDataManager();
		dm.deleteItem(item);
	}
}
