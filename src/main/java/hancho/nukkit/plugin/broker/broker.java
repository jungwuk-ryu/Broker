package hancho.nukkit.plugin.broker;

import java.net.InetSocketAddress;

import com.nukkitx.protocol.bedrock.BedrockClient;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.v389.Bedrock_v389;
import com.nukkitx.protocol.bedrock.v390.Bedrock_v390;

public class broker extends PluginBase {
	// static File file;
	// static FileReader filereader;
	// char singleCh;

	Server server;

	@Override
	public void onEnable() {
		// file = new File("/PMEBroker");
		this.server = getServer();
		InetSocketAddress bindAddress = new InetSocketAddress("0.0.0.0", 54231);
		BedrockClient client = new BedrockClient(bindAddress);

		// Bind the port
		client.bind().join();
		this.getServer().getScheduler().scheduleRepeatingTask(this, new AsyncTask() {

			@Override
			public void onRun() {
				if(Server.getInstance().getOnlinePlayers().size() < 1) return;
				InetSocketAddress addressToPing = new InetSocketAddress("localhost", 19132);
				client.connect(addressToPing).whenComplete((session, throwable) -> {
				    if (throwable != null) {
				    	server.getOnlinePlayers().forEach((id, pl) -> {
							pl.sendTitle("§a재부팅 대기중", "§f아직 서버가 오프라인 입니다.", 10, 10, 10);
						});
				        return;
				    }
				    transform();
					session.disconnect();
				    // Pong received.
				}).join();

				/*
				 * try { filereader = new FileReader(file); singleCh = 'f'; singleCh = (char)
				 * filereader.read(); if ((char) singleCh == 'r') { // 사용되지 않음
				 * server.broadcastMessage("§e곧 서버 재부팅이 완료됩니다.");
				 * server.broadcastMessage("§e곧 서버 재부팅이 완료됩니다.");
				 * server.broadcastMessage("§e곧 서버 재부팅이 완료됩니다."); } else if (singleCh == 'o') {
				 * transform(); } else { server.getOnlinePlayers().forEach((id, pl) -> {
				 * pl.sendTitle("§a재부팅 대기중", "§f아직 서버가 오프라인 입니다.", 10, 10, 10); }); }
				 * filereader.close(); } catch (FileNotFoundException e) { e.printStackTrace();
				 * } catch (IOException e) { e.printStackTrace(); }
				 */
			}
			
			

			public void transform() {
				server.getOnlinePlayers().forEach((id, pl) -> {
					server.broadcastMessage("§e" + pl.getName() + "§f님이 초코서버로 이동합니다.");
					pl.transfer(new InetSocketAddress("ccc1.kro.kr", 19132));
				});
			}
		}, 20 * 11, true);
	}
}
