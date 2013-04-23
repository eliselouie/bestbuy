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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
/**
 * A broadcast receiver to create wakelock 
 * and wake up device to invoke 
 * {@link com.djsystems.bestbuy.services.QueryItemService QueryItemService}.
 * @author Elise Louie
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	private static PowerManager.WakeLock wakeLock = null;
	private static final String LOCK_TAG = "com.djsystems.bestbuy";
	
	public static synchronized void acquireLock(Context ctx){
		if (wakeLock == null){
			PowerManager mgr = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
			wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
			wakeLock.setReferenceCounted(true);
		}
		wakeLock.acquire();
	}
	
	public static synchronized void releaseLock(){
		if (wakeLock != null){
			wakeLock.release();
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		acquireLock(context);
		Intent queryItemService = 
			new Intent(context, QueryItemService.class);
		context.startService(queryItemService);
	}
}
