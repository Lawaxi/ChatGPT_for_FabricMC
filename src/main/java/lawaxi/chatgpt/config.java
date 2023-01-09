package lawaxi.chatgpt;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class config {

    public static final File configFolder = new File("Lawaxi");
    public static final File config = new File(configFolder, "chatgpt.json");

    private String apiKey;
    private String botName;

    public config(){
        try {
            if (!config.exists()) {
                config.createNewFile();
                FileWriter fw = new FileWriter(config);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("apiKey","");
                jsonObject.addProperty("botName","ChatGPT");
                fw.write(jsonObject.toString());
                fw.close();

                System.out.println("请在Lawaxi/chatgpt中填写apiKey后重启");

            }else {

                FileReader fr = new FileReader(config);
                JsonObject jsonObject = new GsonBuilder().create().fromJson(fr, JsonObject.class);
                apiKey = jsonObject.get("apiKey").getAsString();
                botName = jsonObject.get("botName").getAsString();
                fr.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBotName() {
        return botName;
    }
}
