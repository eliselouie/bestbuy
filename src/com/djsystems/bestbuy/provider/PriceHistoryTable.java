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

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class PriceHistoryTable {

   public static final String TABLE_NAME = "tblPrice";

   public static class PriceHistoryColumns implements BaseColumns {
      public static final String PRICE = "price";
      public static final String SKU = "sku";
      public static final String DATE = "date_updated";
   }

   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      sb.append("CREATE TABLE " + PriceHistoryTable.TABLE_NAME + " (");
      sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(PriceHistoryColumns.PRICE + " DOUBLE, ");
      sb.append(PriceHistoryColumns.SKU + " LONG, ");
      sb.append(PriceHistoryColumns.DATE + " TEXT");
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + PriceHistoryTable.TABLE_NAME);
      PriceHistoryTable.onCreate(db);
   }

}
