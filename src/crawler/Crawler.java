package crawler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static Database db = new Database();
	
	
	public static void main(String[] args) {
		Crawler cw = new Crawler();
		Elements countryData = null;
		try {
			countryData = cw.fetchCountries();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String base = "http://www.11v11.com";
		ArrayList<String> links = new ArrayList<String>();			
		for (Element country : countryData) {
			links.add(base + country.attr("href"));
			try {
				cw.addCountryToDatabase(country.text());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(links);
	}
	
		
	public Elements  fetchCountries() throws IOException {
		final List<String> confederations = new ArrayList<String>(
				Arrays.asList(new String[]{"caf","afc","concacaf","conmebol","ofc","uefa"})); 
		Elements countries = new Elements ();
				
		
		for (String c : confederations) {
			countries.addAll(fetchCountries(c));
		}
		
		return countries;		
	}

	
	/**
	 * Returns links to all countries statistics in a given confederation
	 * 
	 * @param confederation
	 * @return List<String>
	 * @throws IOException
	 */
	public Elements fetchCountries (String confederation) throws IOException {
		String base = "http://www.11v11.com";
		Document doc = Jsoup.connect(base + "/" + confederation).get();
		Elements data = doc.select("a[href^=/teams/]");
		
		return data;
	}	
	
	
	public void addCountryToDatabase (String country) throws SQLException {
		String query = "INSERT INTO `football`.`countries` " + "(`country`) VALUES " + 
						"(?) ON DUPLICATE KEY UPDATE country=country;";
		PreparedStatement stmt = db.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, country);
		stmt.execute();		
	}
	
}


