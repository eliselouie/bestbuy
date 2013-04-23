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
 * Model representation of the product item.
 * @author Elise Louie
 *
 */
public class Item extends ModelBase implements Parcelable{
	private String imageUrl;
	private String name;
	private double currPrice;
	private long sku;
	private String shortDesc;
	private String longDesc;
	private String orderable;
	private int instoreAvail;
	private String date;
	
	public Item(){
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
			
	public String getImageUrl(){
		return imageUrl; 
	}
	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	
	public String getName(){
		return name; 
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getShortDescrption(){
		return shortDesc; 
	}
	public void setShortDescrption(String shortDesc){
		this.shortDesc = shortDesc;
	}
	
	public String getLongDescption(){
		return longDesc; 
	}
	public void setLongDescption(String longDesc){
		this.longDesc = longDesc;
	}
	
	public long getSku(){
		return sku; 
	}
	public void setSku(long sku){
		this.sku = sku;
	}
	
	public String getOrderable(){
		return orderable; 
	}
	public void setOrderable(String orderable){
		this.orderable = orderable;
	}
	
	public int getInstoreAvail(){
		return instoreAvail; 
	}
	public void setInstoreAvail(int instoreAvail){
		this.instoreAvail = instoreAvail;
	}
	
	public double getCurrPrice(){
		return currPrice; 
	}
	public void setCurrPrice(double price){
		this.currPrice = price;
	}
	
	public Item(Parcel in){
		readFromParcel(in);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeDouble(currPrice);
		arg0.writeString(imageUrl);
		arg0.writeInt(instoreAvail);
		arg0.writeString(longDesc);
		arg0.writeString(shortDesc);
		arg0.writeString(orderable);
		arg0.writeLong(sku);
		arg0.writeString(date);
		}
	
	private void readFromParcel(Parcel in){
		this.name = in.readString();
		this.currPrice = in.readDouble();
		this.imageUrl = in.readString();
		this.instoreAvail = in.readInt();
		this.longDesc = in.readString();
		this.shortDesc = in.readString();
		this.orderable = in.readString();	
		this.sku = in.readLong();
		this.date = in.readString();
	}
	
	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {  
	    
        public Item createFromParcel(Parcel in) {  
            return new Item(in);  
        }  
   
        public Item[] newArray(int size) {  
            return new Item[size];  
        }  
    };  
}
