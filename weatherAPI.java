import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

public class weatherAPI {
    private static final String API_KEY = "5QLCFB67VZTXEBC5KMSSN49YY";
    private static final String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

    public static String fetchWeatherData(String city) throws Exception {
        String urlString = BASE_URL + city + "?unitGroup=metric&key=" + API_KEY;
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);

            }

            in.close();
            return response.toString();

        } else {
            System.out.println("Error: HTTP respose code " + responseCode);

            return null;
        }
    }

    public static void parseAndDisplay(String response) {
        // System.out.println("API response: " + response);
        JSONObject jsonObject = new JSONObject(response);

        String address = jsonObject.getString("resolvedAddress");

        JSONObject today = jsonObject.getJSONArray("days").getJSONObject(0);
        double tempurature = today.getDouble("temp");
        int humidity = today.getInt("humidity");
        String description = jsonObject.getString("description");
        String condition = today.getString("conditions");

        System.out.println("weather in" + address + ":");
        System.out.println("description: " + description);
        System.out.println("Temperature: " + String.format("%.2f", tempurature) + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("condition: " + condition);
    }

    public static void main(String[] args) {
        String city = "London";
        try {
            String response = fetchWeatherData(city);
            if (response != null) {
                parseAndDisplay(response);
            }

        } catch (Exception e) {
            System.out.println("An error occured " + e.getMessage());
        }
    }

}