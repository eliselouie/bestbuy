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

import com.djsystems.bestbuy.model.Item;
import com.djsystems.bestbuy.model.PriceHistory;

import java.util.List;

/**
 * Android DataManager interface used to define data operations.
 * 
 * @author Elise Louie
 *
 */
public interface DataManager {  
   
   public Item getItem(long itemId);

   public List<Item> getAllItems();

   public boolean saveItem(Item item);
   
   public void deleteItem(Item item);
   
   public boolean updateItem(Item item);
   
   // optional -- used for CursorAdapter
   public Cursor getItemCursor();
   
   public List<PriceHistory> getItemHistoryPrices(Item item);
   
   public long saveIntoPriceHistory(PriceHistory price);

}