package umn.ac.id.holoengsub;

public class YouTubeConfig {
    public YouTubeConfig() {
    } //just a default constructor

    private static final String API_KEY = "AIzaSyACcqW42wtztxUNFjtJPXbZCBdAyYnDsRo";

    public static String getApiKey() {
        return API_KEY;
    } //a getter method to get the API key because it's set to private
}
