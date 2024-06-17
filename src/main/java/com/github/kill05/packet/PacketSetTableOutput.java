package com.github.kill05.packet;

import com.github.kill05.blocks.architectstation.inventory.ArchitectStationContainer;
import com.github.kill05.items.IArchitectItem;
import com.github.kill05.utils.PlayerUtils;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSetTableOutput extends Packet {

	public int button;

	public PacketSetTableOutput(IArchitectItem part) {
		this.button = part != null ? part.ordinal() : -1;
	}

	public PacketSetTableOutput() {
	}


	@Override
	public void readPacketData(DataInputStream dis) throws IOException {
		button = dis.readByte();
	}

	@Override
	public void writePacketData(DataOutputStream dos) throws IOException {
		dos.writeByte(button);
	}

	@Override
	public void processPacket(NetHandler netHandler) {
		System.out.println("packet!");
		EntityPlayer player = PlayerUtils.getPlayer(netHandler);

		if (!(player.craftingInventory instanceof ArchitectStationContainer<?> container)) return;
		setSelected(container, player);
	}

	private <T extends IArchitectItem> void setSelected(ArchitectStationContainer<T> container, EntityPlayer player) {
		boolean inBounds = button >= 0 && button < container.getModeValues().size();
		container.setSelected(inBounds ? container.getModeValues().get(button) : null);

		// Server to clients (tell other clients that another client has changed the selected part/tool)
		// The check is required in case the client is receiving the packet from the server
		if(player instanceof EntityPlayerMP playerMP) {
			PacketSetTableOutput packet = new PacketSetTableOutput(container.getSelected());

			for (EntityPlayerMP other : MinecraftServer.getInstance().playerList.playerEntities) {
				if (other == playerMP || !(other.craftingInventory instanceof ArchitectStationContainer<?> otherContainer)) continue;
				if (otherContainer.getTile() != container.getTile()) continue;
				other.playerNetServerHandler.sendPacket(packet);
			}
		}
	}

	@Override
	public int getPacketSize() {
		return 1;
	}

}
