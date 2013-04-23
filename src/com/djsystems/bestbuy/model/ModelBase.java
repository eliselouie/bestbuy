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

public class ModelBase {

   protected long id;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return "ModelBase [id=" + this.id + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (this.id ^ (this.id >>> 32));
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof ModelBase)) {
         return false;
      }
      ModelBase other = (ModelBase) obj;
      if (this.id != other.id) {
         return false;
      }
      return true;
   }
}
