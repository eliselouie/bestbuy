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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.model.PriceHistory;
import com.djsystems.bestbuy.provider.ItemTable.ItemColumns;

/**
 * Implementation of the data layer interface.
 */
public class DataManagerImpl implements DataManager {

   private Context context;

   private SQLiteDatabase db;

   private PriceHistoryDao priceHistoryDao;
   private ItemDao itemDao;

   public DataManagerImpl(Context context) {

      this.context = context;

      SQLiteOpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
    //  Log.i(Constants.LOG_TAG, "DataManagerImpl created, db open status: " + db.isOpen());

      priceHistoryDao = new PriceHistoryDao(db);
      itemDao = new ItemDao(db);     
   }

   public SQLiteDatabase getDb() {
      return db;
   }

   @Override
   public Item getItem(long itemId) {
      Item item = itemDao.get(itemId);
      return item;
   }

   @Override
   public List<Item> getAllItems() {
      return itemDao.getAll();
   }

   
   @Override
   public boolean saveItem(Item item) { 
      // Insert only when item does not exist
      if(itemDao.find(item.getSku()) == null){
    	  itemDao.save(item);
    	  return true;
      } 
      return false;
   }

   @Override
   public void deleteItem(Item item) {
    itemDao.delete(item);
   }
   
   public boolean updateItem(Item item){
	   boolean result = false;
	   if (item != null) {	           
		   itemDao.update(item);
	       result = true;
	   }
	   return result;
   }

   @Override
   public Cursor getItemCursor() {
      return db.rawQuery("select " + ItemColumns._ID + ", " + ItemColumns.NAME + ", " + 
      ItemColumns.IMAGE_URL + ", " + ItemColumns.SKU + ", " + ItemColumns.SHORT_DESCRIPTION
      + ", " + ItemColumns.LONG_DESCRIPTION + ", " + ItemColumns.ORDERABLE + ", " +
      ItemColumns.INSTORE_AVAILABILITY + ", " + ItemColumns.CURRPRICE
      + " from " + ItemTable.TABLE_NAME, null);
   }

   @Override
   public List<PriceHistory> getItemHistoryPrices(Item item) {
      return priceHistoryDao.getAllHistoryPrices(item);
   }

   @Override
   public long saveIntoPriceHistory(PriceHistory price) {
      return priceHistoryDao.save(price);
   }
}