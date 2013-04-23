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

public final class ItemTable {

   public static final String TABLE_NAME = "tblItem";

   public static class ItemColumns implements BaseColumns {
      public static final String NAME = "item_name";
      public static final String SKU = "sku";
      public static final String SHORT_DESCRIPTION = "short_desc";
      public static final String LONG_DESCRIPTION = "long_desc";
      public static final String IMAGE_URL = "image_url";
      public static final String ORDERABLE = "orderable";
      public static final String INSTORE_AVAILABILITY = "instore_avail";
      public static final String CURRPRICE = "curr_price";
      public static final String DATE_UPDATED = "date_updated";
   }

   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // item table
      sb.append("CREATE TABLE " + ItemTable.TABLE_NAME + " (");
      sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(ItemColumns.NAME + " TEXT UNIQUE NOT NULL, "); 
      sb.append(ItemColumns.IMAGE_URL + " TEXT, ");
      sb.append(ItemColumns.SKU + " LONG, ");
      sb.append(ItemColumns.SHORT_DESCRIPTION + " TEXT, ");
      sb.append(ItemColumns.LONG_DESCRIPTION + " TEXT, ");    
      sb.append(ItemColumns.ORDERABLE + " TEXT, ");
      sb.append(ItemColumns.INSTORE_AVAILABILITY + " INTEGER, ");
      sb.append(ItemColumns.CURRPRICE + " DECIMAL(8,2), ");
      sb.append(ItemColumns.DATE_UPDATED + " TEXT");
      sb.append(");");
      db.execSQL(sb.toString());
      
   	}

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME);
      ItemTable.onCreate(db);
   }
}
