package com.burchard36.nbt.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

/**
 * This class acts as the "Brain" of the NBTApi. It contains the main logger for
 * other classes,registers bStats and checks rather Maven shading was done
 * correctly.
 * 
 * @author tr7zw
 *
 */
@SuppressWarnings("javadoc")
public enum MinecraftVersion {
	UNKNOWN(Integer.MAX_VALUE), // Use the newest known mappings
	MC1_7_R4(174), MC1_8_R3(183), MC1_9_R1(191), MC1_9_R2(192), MC1_10_R1(1101), MC1_11_R1(1111), MC1_12_R1(1121),
	MC1_13_R1(1131), MC1_13_R2(1132), MC1_14_R1(1141), MC1_15_R1(1151), MC1_16_R1(1161), MC1_16_R2(1162), MC1_16_R3(1163), MC1_17_R1(1171), MC1_18_R1(1181, true), MC1_18_R2(1182, true);

	private static MinecraftVersion version;
	private static Boolean hasGsonSupport;
	/**
	 * Logger used by the api
	 */
	private static Logger logger = Logger.getLogger("NBTAPI");

	// NBT-API Version
	protected static final String VERSION = "2.9.2";

	private final int versionId;
	private final boolean mojangMapping;

   MinecraftVersion(int versionId) {
        this(versionId, false);
    }
	
	MinecraftVersion(int versionId, boolean mojangMapping) {
		this.versionId = versionId;
		this.mojangMapping = mojangMapping;
	}

	/**
	 * @return A simple comparable Integer, representing the version.
	 */
	public int getVersionId() {
		return versionId;
	}

	/**
	 * @return True if method names are in Mojang format and need to be remapped internally
	 */
	public boolean isMojangMapping() {
        return mojangMapping;
    }
	
	/**
	 * This method is required to hot-wire the plugin during mappings generation for newer mc versions thanks to md_5 not used mojmap.
	 * 
	 * @return
	 */
	public String getPackageName() {
	    if(this == UNKNOWN) {
	        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	    }
	    return this.name().replace("MC", "v");
	}

    /**
	 * Returns true if the current versions is at least the given Version
	 * 
	 * @param version The minimum version
	 * @return
	 */
	public static boolean isAtLeastVersion(MinecraftVersion version) {
		return getVersion().getVersionId() >= version.getVersionId();
	}
	
	/**
	 * Returns true if the current versions newer (not equal) than the given version
	 * 
	 * @param version The minimum version
	 * @return
	 */
	public static boolean isNewerThan(MinecraftVersion version) {
		return getVersion().getVersionId() > version.getVersionId();
	}

	/**
	 * Getter for this servers MinecraftVersion. Also init's bStats and checks the
	 * shading.
	 * 
	 * @return The enum for the MinecraftVersion this server is running
	 */
	public static MinecraftVersion getVersion() {
		if (version != null) {
			return version;
		}
		final String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		logger.info("[NBTAPI] Found Spigot: " + ver + "! Trying to find NMS support");
		try {
			version = MinecraftVersion.valueOf(ver.replace("v", "MC"));
		} catch (IllegalArgumentException ex) {
			version = MinecraftVersion.UNKNOWN;
		}
		if (version != UNKNOWN) {
			logger.info("[NBTAPI] NMS support '" + version.name() + "' loaded!");
		} else {
			logger.warning("[NBTAPI] This Server-Version(" + ver + ") is not supported by this NBT-API Version(" + VERSION + ") located at " + MinecraftVersion.class.getName() + ". The NBT-API will try to work as good as it can! Some functions may not work!");
		}
		init();
		return version;
	}

	private static void init() {
		// Maven's Relocate is clever and changes strings, too. So we have to use this
		// little "trick" ... :D (from bStats)
		final String defaultPackage = new String(new byte[] { 'd', 'e', '.', 't', 'r', '7', 'z', 'w', '.', 'c', 'h',
				'a', 'n', 'g', 'e', 'm', 'e', '.', 'n', 'b', 't', 'a', 'p', 'i', '.', 'u', 't', 'i', 'l', 's' });
	}

	/**
	 * @return True, if Gson is usable
	 */
	public static boolean hasGsonSupport() {
		if (hasGsonSupport != null) {
			return hasGsonSupport;
		}
		try {
			logger.info("[NBTAPI] Found Gson: " + Class.forName("com.google.gson.Gson"));
			hasGsonSupport = true;
		} catch (Exception ex) {
			logger.info("[NBTAPI] Gson not found! This will not allow the usage of some methods!");
			hasGsonSupport = false;
		}
		return hasGsonSupport;
	}

	/**
	 * @return Logger used by the NBT-API
	 */
	public static Logger getLogger() {
		return logger;
	}

}
