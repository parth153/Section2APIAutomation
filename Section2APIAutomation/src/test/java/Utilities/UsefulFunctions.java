package Utilities;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class UsefulFunctions {
	
	//function to get total number of child elements
	public static int getNumberofJSONObjectsinResponse(String responseAsJSON)
	{
		JSONArray jsonArr = new JSONArray(responseAsJSON);
				
		return jsonArr.length();
	}
	
	//function to get JSON object with an index
	public static JSONObject getJSONObject(String responseAsJSON, int objectNumber)
	{
		JSONArray jsonArr = new JSONArray(responseAsJSON);
				
		return jsonArr.getJSONObject(objectNumber);
	}
	
	//function to format price in 4 decimals
	public static Double formatPriceAsPerResponse(Double price)
	{
		DecimalFormat df = new DecimalFormat("#.####");
		
		return Double.parseDouble(df.format(price));
	}

}
