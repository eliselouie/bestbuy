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
package com.djsystems.bestbuy;

import com.djsystems.bestbuy.provider.DataManager;
import com.djsystems.bestbuy.provider.DataManagerImpl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;

public class BestbuyApp extends Application {

   private ConnectivityManager cMgr;
   private DataManager dataManager;
   private SharedPreferences prefs;

   public SharedPreferences getPrefs() {
      return this.prefs;
   }

   public DataManager getDataManager() {
      return this.dataManager;
   }

   @Override
   public void onCreate() {
      super.onCreate();
      cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      dataManager = new DataManagerImpl(this);
   }
   
   @Override
   public void onTerminate() {
      // not guaranteed to be called
      super.onTerminate();
   }

   public boolean connectionPresent() {
      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
      if ((netInfo != null) && (netInfo.getState() != null)) {
         return netInfo.getState().equals(State.CONNECTED);
      }
      return false;
   }
}