package lawaxi.chatgpt.mixin;

import lawaxi.chatgpt.ExampleMod;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ExampleMixin {
	@Shadow @Final private MinecraftServer server;

	@Inject(at = @At("HEAD"), method = "onChatMessage")
	private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {

		if(!ExampleMod.instance.hasApiKey())
			return;

		if(ExampleMod.server == null)
			ExampleMod.server = this.server;

		new Thread(new Runnable() {
				@Override
				public void run () {
					String question = packet.chatMessage().replaceAll("\"", "");
					ExampleMod.instance.sendRequest(question);
				}
			}
		).start();
	}
}
