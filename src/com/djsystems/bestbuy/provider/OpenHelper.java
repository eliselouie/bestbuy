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
package com.djsystems.bestbuy.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

   private static final int DATABASE_VERSION = 1;
   
   OpenHelper(final Context context) {
      super(context, DataConstants.DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onOpen(final SQLiteDatabase db) {
      super.onOpen(db);
      if (!db.isReadOnly()) {         
         db.execSQL("PRAGMA foreign_keys=ON;");        
      }
   }

   @Override
   public void onCreate(final SQLiteDatabase db) {
      PriceHistoryTable.onCreate(db); 
      ItemTable.onCreate(db);
      DbTriggers.onCreate(db);
   }

   @Override
   public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
      ItemTable.onUpgrade(db, oldVersion, newVersion);
      PriceHistoryTable.onUpgrade(db, oldVersion, newVersion);
   }
}
