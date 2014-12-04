/** Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package ezgrocerylist.sql;

/**
 * POJO that encapsulates the details of an item: name, quantity,
 * unit, price, note .
 */
public class Item {

    private String itemName;
    private int itemQuantity;
    private String itemUnit;
    private float itemPrice;
    private String itemNote;
    private String itemCategory;
    private String itemBarcode;
    
    public Item(){
    	
    }

    public Item(String itemName, int itemQuantity, String itemUnit, float itemPrice, String itemNote,String itemCategory,String itemBarcode) {
        this.setItemName(itemName);
        this.setItemQuantity(itemQuantity);
        this.setItemUnit(itemUnit);
        this.setItemPrice(itemPrice);
        this.setItemNote(itemNote);
        this.setItemCategory(itemCategory);
        this.setItemBarcode(itemBarcode);
    }
    public Item(String itemName, int itemQuantity, String itemUnit, String itemNote,String itemCategory) {
        this.setItemName(itemName);
        this.setItemQuantity(itemQuantity);
        this.setItemUnit(itemUnit);
        this.setItemNote(itemNote);
        this.setItemCategory(itemCategory);
    }
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public float getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(float itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemNote() {
		return itemNote;
	}

	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemBarcode() {
		return itemBarcode;
	}

	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
}
