package ezgrocerylist.sql;

import java.util.ArrayList;

public class PantryList {
	private String listName;
    private ArrayList<Item> items;

    public PantryList(String listName) {
        this.setListName(listName);
    }

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
    

}
