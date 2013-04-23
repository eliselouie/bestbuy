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

import java.util.Calendar;

import com.djsystems.bestbuy.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <code>BroadcastReceiver</code> that is notified when the device boots up.
 * It uses the {@link http://developer.android.com/reference/android/app/AlarmManager.html AlarmManager}
 * to schedule regular invocations of the 
 * {@link com.djsystems.bestbuy.services.QueryItemService QueryItemService}.
 * 
 */
public class QueryItemStartupReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, 
				i, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 2);
		mgr.setRepeating(AlarmManager.RTC_WAKEUP, 
				now.getTimeInMillis(), Constants.TWELVE_HOURS, sender);
	}
}