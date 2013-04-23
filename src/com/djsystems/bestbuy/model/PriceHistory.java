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
package com.djsystems.bestbuy.model;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Model representation of the row in PriceHistory table.
 * @author Elise Louie
 *
 */
public class PriceHistory extends ModelBase implements Parcelable{

   private double price;
   private long sku;
   private String date;
   
   public PriceHistory() {
   }

   public PriceHistory(double price, long sku, String date) {
	      this.sku = sku;
	      this.price = price;
	      this.date = date;
	   }

   public double getPrice() {
      return this.price;
   }

   public void setPrice(double price) {
      this.price = price;
   }
   
   public long getSku() {
	      return this.sku;
   }

   public void setSku(long sku) {
	      this.sku = sku;
   }
   
   public String getDate() {
	      return this.date;
}

public void setDate(String date) {
	      this.date = date;
}
	   
	   public PriceHistory(Parcel in){
		   readFromParcel(in);
	   }
	   
	   private void readFromParcel(Parcel in){
			this.price = in.readDouble();		
			this.sku = in.readLong();
			this.date = in.readString();
		}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeDouble(price);
		dest.writeLong(sku);	
		dest.writeString(date);
	}
	
public static final Parcelable.Creator<PriceHistory> CREATOR = new Parcelable.Creator<PriceHistory>() {  
	    
        public PriceHistory createFromParcel(Parcel in) {  
            return new PriceHistory(in);  
        }  
   
        public PriceHistory[] newArray(int size) {  
            return new PriceHistory[size];  
        }  
    };  
}
