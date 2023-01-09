package lawaxi.chatgpt;

import lawaxi.chatgpt.http.HttpRequestSender;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
public class ExampleMod implements ModInitializer {
	public static ExampleMod instance;
	public static MinecraftServer server;
	private HttpRequestSender requestSender;
	private String apiKey, botName;

	public HttpRequestSender getRequestSender() {
		return requestSender;
	}

	public boolean hasApiKey(){
		return apiKey != null;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		this.instance = this;
		this.requestSender = new HttpRequestSender();
		config a = new config();
		apiKey = a.getApiKey();
		botName = a.getBotName();
	}


	public void sendRequest(String question) {
		sendBotMessage(requestSender.createRequest(apiKey, question));
	}

	public void sendBotMessage(String text) {
		for (ServerPlayerEntity PLA : server.getPlayerManager().getPlayerList()) {
			PLA.sendMessage(Text.literal("<"+botName+"> "+text), false);
		}
	}
}
