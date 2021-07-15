package de.tr7zw.nbtinjector;

import dev.tr7zw.nbtapi.NBTCompound;

/**
 * This interface gets placed on Tiles and Entities
 * 
 * @author tr7zw
 *
 */
public interface INBTWrapper {

	/**
	 * @return The custom NBT compound inside this Object
	 */
	NBTCompound getNbtData();

}