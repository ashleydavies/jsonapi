package com.alecgorge.minecraft.jsonapi.util;

import java.io.File;

//#ifdefined mcversion
//$import net.minecraft.server.v1_6_R3.*;
//$import org.bukkit.craftbukkit.v1_6_R3.*;
//#if mc17OrNewer=="yes"
//$import com.mojang.authlib.GameProfile;
//#endif
//#else
//$import net.minecraft.server.v1_6_R3.*;
//$import org.bukkit.craftbukkit.v1_6_R3.*;
//#endif

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class OfflinePlayerLoader {
	public static Player load(String exactPlayerName) {
		// big thanks to
		// https://github.com/lishd/OpenInv/blob/master/src/com/lishid/openinv/internal/craftbukkit/PlayerDataManager.java
		// Offline inv here...
		
		int index = 0;
		for (World w : Bukkit.getWorlds()) {
			try {
				// See if the player has data files

				// Find the player folder
				File playerfolder = new File(w.getWorldFolder(), "players");
				if (!playerfolder.exists()) {
					return null;
				}

				Player target = null;
				MinecraftServer server = null;
				try {
					server = ((CraftServer) Bukkit.getServer()).getServer();
				} catch (Exception e) {
					server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();
				}

				// Create an entity to load the player data
				//#if mc17OrNewer=="yes"
				//$EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(index), new GameProfile(null, exactPlayerName), new PlayerInteractManager(server.getWorldServer(index)));				
				//#else
				//$EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(index), exactPlayerName, new PlayerInteractManager(server.getWorldServer(index)));
				//#endif

				// Get the bukkit entity
				target = (entity == null) ? null : entity.getBukkitEntity();

				if (target != null) {
					// Load data
					target.loadData();
					// Return the entity
					return target;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			} finally {
				index++;
			}
		}

		return null;
	}
}
