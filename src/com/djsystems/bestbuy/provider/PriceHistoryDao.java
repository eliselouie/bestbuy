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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.model.PriceHistory;
import com.djsystems.bestbuy.provider.PriceHistoryTable.PriceHistoryColumns;

import java.util.ArrayList;
import java.util.List;

public class PriceHistoryDao implements Dao<PriceHistory> {

   private static final String INSERT =
            "insert into " + PriceHistoryTable.TABLE_NAME + "(" + PriceHistoryColumns.PRICE + ", " + PriceHistoryColumns.SKU + 
            ", " + PriceHistoryColumns.DATE + ") values (?, ?, ?)";

   private SQLiteDatabase db;
   private SQLiteStatement insertStatement;

   public PriceHistoryDao(SQLiteDatabase db) {
      this.db = db;
      insertStatement = db.compileStatement(PriceHistoryDao.INSERT);
   }

   @Override
   public long save(PriceHistory entity) {
      insertStatement.clearBindings();
      insertStatement.bindDouble(1, entity.getPrice());
      insertStatement.bindLong(2, entity.getSku());
      insertStatement.bindString(3, entity.getDate());
      return insertStatement.executeInsert();
   }
   
// delete all the history prices
   @Override
   public void delete(PriceHistory entity) {
      if (entity.getSku() > 0) {
         db.delete(PriceHistoryTable.TABLE_NAME, PriceHistoryColumns.SKU + " = ?", new String[] { String.valueOf(entity.getSku()) });
      }
   }

   /**
    * Get all the price history for the specified item.
    * @param item The item that is interested.
    * @return A list of PriceHistory objects in regard to the item
    */
   public List<PriceHistory> getAllHistoryPrices(Item item) {
      List<PriceHistory> list = new ArrayList<PriceHistory>();
      String whereClause = PriceHistoryColumns.SKU + " = ?";
      String[] whereArgs = new String[] {item.getSku()+""};
      
      Cursor c =
               db.query(PriceHistoryTable.TABLE_NAME, new String[] { BaseColumns._ID, PriceHistoryColumns.PRICE, PriceHistoryColumns.SKU, PriceHistoryColumns.DATE}, 
            		   whereClause, whereArgs,
                        null, null, BaseColumns._ID, null);
      if (c.moveToFirst()) {
         do {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setId(c.getLong(0));
            priceHistory.setPrice(c.getDouble(1));
            priceHistory.setSku(c.getLong(2));
            priceHistory.setDate(c.getString(3));
            list.add(priceHistory);
         } while (c.moveToNext());
      }
      if (!c.isClosed()) {
         c.close();
      }
      return list;
   }

}
