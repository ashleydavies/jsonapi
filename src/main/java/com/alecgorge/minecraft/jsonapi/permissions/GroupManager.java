package com.alecgorge.minecraft.jsonapi.permissions;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.alecgorge.minecraft.jsonapi.config.GroupsConfig;
import com.alecgorge.minecraft.jsonapi.config.UsersConfig;
import com.alecgorge.minecraft.jsonapi.event.JSONAPIAuthEvent;
import com.alecgorge.minecraft.jsonapi.JSONAPI;

public class GroupManager {
	JSONAPI	plugin;

	public GroupManager(JSONAPI plugin) {
		this.plugin = plugin;
	
		JSONAPI.dbug("registering for jsonapiauthevents");
		plugin.getServer().getPluginManager().registerEvents(new JSONAPIPermissionsListener(), plugin);
	}

	public void loadFromConfig() {
		try {
			UsersConfig.config().init();
			GroupsConfig.config().init();
		}
		catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	boolean effectivePermission(JSONAPIUser username, String method, boolean stream) {
		JSONAPI.dbug("Testing " + method + " (" + stream + ")" + " on " + username);
		if (stream) {
			return username.canUseStream(method);
		}
		else {
			return username.canUseMethod(method);
		}
	}

	public class JSONAPIPermissionsListener implements Listener {
		@EventHandler
		public void onJSONAPIAuthChallenge(JSONAPIAuthEvent e) {
			JSONAPI.dbug("Recieved authevent " + e);
			if (e.getUser().username.equals("mcserveradmin")) // Bypass all permission checks for mcserveradmin; it's handled by the ASP.NET engine
			{
				JSONAPI.instance.outLog.info("Allowed MCServerAdmin payload without permission verification");
				e.getAuthResponse().setAllowed(true);
			}
			else
			{
				JSONAPI.instance.outLog.info(e.getUser().username);
				e.getAuthResponse().setAllowed(effectivePermission(e.getUser(), e.getMethod(), e.isStream()));
			}
		}
	}
}
