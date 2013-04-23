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

public final class DbTriggers {
	public static void onCreate(SQLiteDatabase db) {
		// create triggers
		String insertTriggerString = "CREATE TRIGGER insert_into_price_history_on_insert AFTER INSERT ON tblItem" +
	    		  				" BEGIN " +
	    		  				"INSERT INTO tblPrice(price, sku, date_updated) VALUES (new.curr_price, new.sku, new.date_updated" + 
	    						"); END;";
	    db.execSQL(insertTriggerString);
	    String updateTriggerString = "CREATE TRIGGER insert_into_price_history_on_update UPDATE ON tblItem" +
					" BEGIN " +
					"INSERT INTO tblPrice(price, sku, date_updated) VALUES (new.curr_price, new.sku, new.date_updated" + 
					"); END;";
	    db.execSQL(updateTriggerString);
	    String deleteTriggerString = "CREATE TRIGGER delete_from_price_history_on_delete DELETE ON tblItem" +
				" BEGIN " +
				"DELETE FROM tblPrice WHERE sku=old.sku; END;";
	    db.execSQL(deleteTriggerString);
	}
}
