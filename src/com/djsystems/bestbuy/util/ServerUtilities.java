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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.djsystems.bestbuy.Constants;
import com.djsystems.bestbuy.model.Item;

public final class ServerUtilities {
	private static String TAG = "ServerUtilities";
	public static ArrayList <Item> itemsReturned = new ArrayList<Item> ();
	
	public static String getJSONResponse(String url){
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200){
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while((line = reader.readLine()) != null){
					stringBuilder.append(line);
				} 
			}else {
					Log.e(TAG, "Failed to download file");
					return null;
				}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return stringBuilder.toString();
	}
	
	public static ArrayList<HashMap<Item, Bitmap>> searchProduct(String name, int pageNum){
		String url = null;
		// the bestbuy api does not handle white space correctly.
		name = name.trim();
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(name);
		boolean found = matcher.find(); 
		if(found){
			String[] parts = name.split("\\s+");
			url = Constants.bestBuyApiEndpoint + "(name=" +
					parts[0] + "*)?&page=" + pageNum + "&pageSize=20" +"&format=json&apiKey=" + Constants.apiKey;
		} else {
			if(name.matches("\\d+")) {
				url = Constants.bestBuyApiEndpoint + "/" + name + ".json?apiKey=" + Constants.apiKey;
				Log.d("ServerUtilities", url);
			}
			else url = Constants.bestBuyApiEndpoint + "(name=" +
					name + "*)?&page=" + pageNum + "&pageSize=20" +"&format=json&apiKey=" + Constants.apiKey;
		}
		
		ArrayList<HashMap<Item, Bitmap>> listOfItems = null;
		try {
			if(getJSONResponse(url)!=null)
			listOfItems = JsonProductParser.parseProduct(getJSONResponse(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfItems;
	}
	
	  public static Bitmap getBitmapFromUrl(String src){
		   Bitmap myBitmap = null;
		   try {
			   URL url = new URL(src);
			   HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			   connection.setDoInput(true);
	           connection.connect();
	           InputStream input = connection.getInputStream();
	           myBitmap = BitmapFactory.decodeStream(input);   
			} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("JsonProductParser", e.getLocalizedMessage());
			
			}
		   return myBitmap;
	   }
}
