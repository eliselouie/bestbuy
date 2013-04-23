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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.provider.ItemTable.ItemColumns;

import java.util.ArrayList;
import java.util.List;

public class ItemDao implements Dao<Item> {

   private static final String INSERT =
            "insert into " + ItemTable.TABLE_NAME + "(" + ItemColumns.NAME + ", " + ItemColumns.IMAGE_URL + ", "
                     + ItemColumns.SKU + ", " + ItemColumns.SHORT_DESCRIPTION + ", " + ItemColumns.LONG_DESCRIPTION + ", "
                     + ItemColumns.ORDERABLE + ", " + ItemColumns.INSTORE_AVAILABILITY + "," + ItemColumns.CURRPRICE + "," 
                     + ItemColumns.DATE_UPDATED +") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

   private SQLiteDatabase db;
   private SQLiteStatement insertStatement;

   public ItemDao(SQLiteDatabase db) {
      this.db = db;
      insertStatement = db.compileStatement(INSERT);
   }

   @Override
   public long save(Item entity) {
      insertStatement.clearBindings();
      insertStatement.bindString(1, entity.getName());
      insertStatement.bindString(2, entity.getImageUrl());
      insertStatement.bindLong(3, entity.getSku());
      if(entity.getShortDescrption() == null) insertStatement.bindNull(4);
      else insertStatement.bindString(4, entity.getShortDescrption());
      if(entity.getLongDescption() == null) insertStatement.bindNull(5);
      else insertStatement.bindString(5, entity.getLongDescption());
      insertStatement.bindString(6, entity.getOrderable());
      insertStatement.bindString(7, Integer.toString(entity.getInstoreAvail()));
      insertStatement.bindDouble(8, entity.getCurrPrice());
      insertStatement.bindString(9, entity.getDate());
     
      return insertStatement.executeInsert();
   }
   
   public Item find(long sku) {
      long itemId = 0L;
      String sql = "select _id from " + ItemTable.TABLE_NAME + " where " + ItemColumns.SKU + " = " + sku;
      Cursor c = db.rawQuery(sql, null);
      if (c.moveToFirst()) {
         itemId = c.getLong(0);
      }
      if (!c.isClosed()) {
         c.close();
      }
      // we make another query here, which is another trip, 
      // this is a trade off we accept with such a small amount of data
      return this.get(itemId);
   }

   public void update(Item entity) {
      final ContentValues values = new ContentValues();
      values.put(ItemColumns.NAME, entity.getName());
      values.put(ItemColumns.IMAGE_URL, entity.getImageUrl());
      values.put(ItemColumns.SKU, entity.getSku());
      values.put(ItemColumns.SHORT_DESCRIPTION, entity.getShortDescrption());
      values.put(ItemColumns.LONG_DESCRIPTION, entity.getLongDescption());
      values.put(ItemColumns.ORDERABLE, entity.getOrderable());
      values.put(ItemColumns.INSTORE_AVAILABILITY, entity.getInstoreAvail());
      values.put(ItemColumns.CURRPRICE, entity.getCurrPrice());
      values.put(ItemColumns.DATE_UPDATED, entity.getDate());
      db.update(ItemTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[] { String
               .valueOf(entity.getId()) });
   }

   @Override
   public void delete(Item entity) {
      if (entity.getSku() > 0) {
         db.delete(ItemTable.TABLE_NAME, ItemColumns.SKU + " = ?", new String[] { String.valueOf(entity.getSku()) });
      }
   }
   
   public Item get(long id) {
      Item item = null;
      // check
      Cursor c =
               db.query(ItemTable.TABLE_NAME, new String[] { BaseColumns._ID, ItemColumns.NAME,
                        ItemColumns.IMAGE_URL, ItemColumns.SKU, ItemColumns.SHORT_DESCRIPTION, ItemColumns.LONG_DESCRIPTION,
                        ItemColumns.ORDERABLE, ItemColumns.INSTORE_AVAILABILITY, ItemColumns.CURRPRICE, ItemColumns.DATE_UPDATED},
                        BaseColumns._ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
      if (c.moveToFirst()) {
         item = this.buildItemFromCursor(c);
      }
      if (!c.isClosed()) {
         c.close();
      }
      return item;
   }

   public List<Item> getAll() {
      List<Item> list = new ArrayList<Item>();
      //check
      Cursor c =
               db.query(ItemTable.TABLE_NAME, new String[] { BaseColumns._ID, ItemColumns.NAME,
            		   ItemColumns.IMAGE_URL, ItemColumns.SKU, ItemColumns.SHORT_DESCRIPTION, ItemColumns.LONG_DESCRIPTION, 
                        ItemColumns.ORDERABLE, ItemColumns.INSTORE_AVAILABILITY, ItemColumns.CURRPRICE, ItemColumns.DATE_UPDATED }, null,
                        null, null, null, ItemColumns.NAME, null);
      if (c.moveToFirst()) {
         do {
            Item item = this.buildItemFromCursor(c);
            if (item != null) {
               list.add(item);
            }
         } while (c.moveToNext());
      }
      if (!c.isClosed()) {
         c.close();
      }
      return list;
   }


   private Item buildItemFromCursor(Cursor c) {
      Item item = null;
      if (c != null) {
         item = new Item();
         item.setId(c.getLong(0));
         item.setName(c.getString(1));
         item.setImageUrl(c.getString(2));
         item.setSku(c.getLong(3));
         item.setShortDescrption(c.getString(4));
         item.setLongDescption(c.getString(5));
         item.setOrderable(c.getString(6));
         item.setInstoreAvail(c.getInt(7));
         item.setCurrPrice(c.getDouble(8));
         item.setDate(c.getString(9));
      }
      return item;
   }

}
