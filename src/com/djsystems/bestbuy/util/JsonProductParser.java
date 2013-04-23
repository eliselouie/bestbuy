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

package com.djsystems.bestbuy.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.util.Log;

import com.djsystems.bestbuy.Constants;
import com.djsystems.bestbuy.model.Item;
/**
 * Utility class to parse the JSON response
 * @author Elise Louie
 *
 */
public class JsonProductParser {

	/**
	 * Utility class to parse the json object.
	 * @param json JSON string 
	 * @return ArrayList of Items and Images of the items.
	 * @throws Exception
	 */
   public static ArrayList<HashMap<Item, Bitmap>> parseProduct(String json) throws Exception {
	   boolean foundListOfProducts = true;
	   JSONArray itemArray = null;
	   JSONObject bestBuy = new JSONObject(json);
	   ArrayList<HashMap<Item, Bitmap>> listOfItems= new ArrayList<HashMap<Item, Bitmap>>();
	   try{
		   itemArray = bestBuy.getJSONArray("products");
	   } catch (JSONException e){
		   foundListOfProducts = false;
	   }
	   if(foundListOfProducts == true){
		   for(int i=0; i<itemArray.length(); i++){
			   JSONObject itemJsonObj = itemArray.getJSONObject(i);
			   Item item = new Item();
			   HashMap<Item, Bitmap> hashMap = new HashMap<Item, Bitmap>();
			
			   Bitmap theImage = ServerUtilities.getBitmapFromUrl(itemJsonObj.getString("image"));
			   
			   item.setImageUrl(itemJsonObj.getString("image"));
			   item.setSku(itemJsonObj.getLong("sku"));
			   item.setName(itemJsonObj.getString("name").replaceAll("&#174", "¨"));
			   item.setInstoreAvail(itemJsonObj.getBoolean("inStoreAvailability")? 1:0);
			   try {
				   String longDesc = itemJsonObj.getString("longDescription").replaceAll("&#174", "¨");
				   longDesc = longDesc.replaceAll("&#8482", "ª");
				   item.setLongDescption(longDesc);
			   } catch (JSONException e){
				 //  Log.d("JsonProductParser", "No long description");
			   }
			   String shortDesc = itemJsonObj.getString("shortDescription").replaceAll("&#174", "¨");
			   shortDesc = shortDesc.replaceAll("&#8482", "ª");
			   item.setShortDescrption(shortDesc);
			   item.setOrderable(itemJsonObj.getString("orderable"));
			   item.setCurrPrice(itemJsonObj.getDouble("salePrice"));
			   item.setDate(DateTimeUtility.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
			   
			   hashMap.put(item, theImage);
			   listOfItems.add(hashMap);
		   }
	   } else {
		   // request was with a specific sku
		   if(bestBuy.getString("name") != null){
			   Item item = new Item();
			   HashMap<Item, Bitmap> hashMap = new HashMap<Item, Bitmap>();
			
			   Bitmap theImage = ServerUtilities.getBitmapFromUrl(bestBuy.getString("image"));
			   
			   item.setImageUrl(bestBuy.getString("image"));
			   item.setSku(bestBuy.getLong("sku"));
			   item.setName(bestBuy.getString("name").replaceAll("&#174", "¨"));
			   item.setInstoreAvail(bestBuy.getBoolean("inStoreAvailability")? 1:0);
			   try {
				   item.setLongDescption(bestBuy.getString("longDescription").replaceAll("&#174", "¨"));
			   } catch (JSONException e){
				 //  Log.d("JsonProductParser", "No long description");
			   }
			   item.setShortDescrption(bestBuy.getString("shortDescription").replaceAll("&#174", "¨"));
			   item.setOrderable(bestBuy.getString("orderable"));
			   item.setCurrPrice(bestBuy.getDouble("salePrice"));
			   
			   hashMap.put(item, theImage);
			   listOfItems.add(hashMap);
		   }	   
	   }
      return listOfItems;
   }
   
   /**
    * Request backend server via {@link ServerUtilities} and then parse
    * the json response get the current price for the item. 
    * @param item The item.
    * @return Current price.
    */
   public static double getItemCurrentPrice(Item item){
	   double currentPrice = 0D;
	   long theSku = item.getSku();
	   String url = Constants.bestBuyApiEndpoint + "/" + theSku + ".json?apiKey=" + Constants.apiKey;
	   String jsonResponse = ServerUtilities.getJSONResponse(url);
	   // Failed to request
	 //  if(jsonResponse == null || jsonResponse == "") return -1;
	   if(jsonResponse == null) return -1;
	   try {
		   JSONObject bestBuyJsonObj =new JSONObject(jsonResponse);
		   currentPrice = bestBuyJsonObj.getDouble("salePrice");
	   } catch (JSONException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		   return -1;
	   }
	   return currentPrice;
   }
}
