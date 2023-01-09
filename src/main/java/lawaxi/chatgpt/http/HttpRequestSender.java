package lawaxi.chatgpt.http;

import com.google.gson.GsonBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author Nikolas Rummel
 * @since 21.12.22
 */
public class HttpRequestSender {

    private final HttpClient httpClient;

    public HttpRequestSender() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    /**
     * Send an HTTP-Request to open.ai's api
     *
     * @param apiKey   the api key
     * @param question the question
     * @return the answer made by the ai
     */
    private String makeDefaultRequest(String apiKey, String question) {
        // Create request
        // https://beta.openai.com/docs/api-reference/making-requests
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(BodyPublishers.ofString("{\n"
                + "  \"model\": \"text-davinci-003\",\n"
                + "  \"prompt\": \"" + question + "\",\n"
                + "  \"temperature\": 0.12,\n"
                + "  \"max_tokens\": 256,\n"
                + "  \"top_p\": 0.63,\n"
                + "  \"frequency_penalty\": 0,\n"
                + "  \"presence_penalty\": 0\n"
                + "}"))
            .build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            JsonObject json = new GsonBuilder().create().fromJson(response.body(), JsonObject.class);

            // Get the value of the "choices" field
            JsonArray choices = json.get("choices").getAsJsonArray();

            // Get the first element of the "choices" array
            JsonObject firstChoice = choices.get(0).getAsJsonObject();

            // Get the value of the "text" field from the first element of the "choices" array
            return firstChoice.get("text").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "§cError!";
    }

    /**
     * Creates a request and broadcasts the answer in the Minecraft chat.
     *
     * @param apiKey   the api key
     * @param question the question
     */
    public String createRequest(String apiKey, String question) {
        return makeDefaultRequest(apiKey, question).substring(2);
    }
}