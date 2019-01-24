package runner;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.jayway.jsonpath.JsonPath;

import Utilities.UsefulFunctions;

public class FunctionalTest {

	static Double originalPrice8205 = 101.2455;
	static int totalNumRecords = 28;
	
	@BeforeClass
	public static void Setup()
	{
		String apiEndpoint = System.getProperty("apiendpoint");
		
		if(apiEndpoint == null)
			apiEndpoint = "https://www.vanguardinvestments.com.au/retail/mvc/getNavPriceList.jsonp";
		
		RestAssured.baseURI = apiEndpoint;
		
		originalPrice8205 = 101.2455;	
		
	}
	
	//to get the JSON response of the request
	//could have kept in Setup but it is better to make a new request for each test
	public String GetResponse() {
		
		Response resp = RestAssured.get(RestAssured.baseURI);
		String json = resp.getBody().asString();
		
		return json;
	}
	
	@Test
	public void makeSureStatusCodeIsCorrect()
	{
		given().when().get(RestAssured.baseURI).then().statusCode(200);
	}
	
	@Test
	public void MakeSureResponseTimeisunderSLA()
	{
		//assumed that SLA is 2000 milliseconds... response time is around 1750
		given().when().get(RestAssured.baseURI).then().time(lessThan(2000L));
	}

	@Test
    public void verifyResponseContainsnavPriceArray() {
        given().when().get(RestAssured.baseURI).then()
            .body(containsString("navPriceArray"));
    }
	
	@Test
	public void verifyFirstElementPortIdNavPrice() {
		
		String responseAsJSON = GetResponse();
		
		//verify if the port ID is matching
		Object dataObject = JsonPath.read(responseAsJSON, "$.[0].portId");
		String dataString = dataObject.toString();
		
		//asserting that the port ID is of the first product
		Assert.assertTrue(dataString.equalsIgnoreCase("8205"));
		
		//read price value and percentage change from the response 
		dataObject = JsonPath.read(responseAsJSON, "$.[0].navPriceArray.[0].price");
		String price = dataObject.toString();
		
		Double priceValue = Double.parseDouble(price);
		
		dataObject = JsonPath.read(responseAsJSON, "$.[0].navPriceArray.[0].percentChange");
		String percentageChange = dataObject.toString();
		
		Double percentChange = Double.parseDouble(percentageChange);
		
		//calculate the price based on assumption of price
		//original price calculated based on the percentChange and current value
		Double calculatedPriceValue = originalPrice8205 + originalPrice8205*percentChange;
		
		//asserting that the port ID is of the first product
		Assert.assertTrue(priceValue.equals(UsefulFunctions.formatPriceAsPerResponse(calculatedPriceValue)));
		
	}
	
	@Test
	public void verifyTotalNumberofChildElements() {
		
		String responseAsJSON = GetResponse();
		
		Assert.assertTrue(UsefulFunctions.getNumberofJSONObjectsinResponse(responseAsJSON) == totalNumRecords);
		
	}
	
}
