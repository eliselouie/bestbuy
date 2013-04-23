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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
	private ImageView imageView;
	
	public DownloadImage(ImageView image){
		imageView = image;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		return ServerUtilities.getBitmapFromUrl(params[0]);
	}
	
	protected void onPostExecute(Bitmap result){
		imageView.setImageBitmap(result);
	}
}
