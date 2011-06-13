package com.matejdro.bukkit.jail;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JailCellCreation {
public static HashMap<String,CreationPlayer> players = new HashMap<String,CreationPlayer>();

	
	public static void selectstart(Player player, String name)
	{
		if (players.containsKey(player.getName()))
		{
			players.remove(player.getName());
		}
		if (!player.getInventory().contains(Settings.SelectionTool))
			player.getInventory().addItem(new ItemStack(Settings.SelectionTool,1));
		
		Jail.Message("�cJail Cell Creation:", player);
		Jail.Message("First, you must select teleport point for the cell! Move to the teleport and then click anywhere with wooden sword, to set it", player);
		players.put(player.getName(), new CreationPlayer(name));
	}
	
	public static void select(Player player, Block block)
	{
		switch (players.get(player.getName()).state)
		{
			case 1:
				telepoint(player,block);
				break;
			case 2:
				sign(player,block);
				break;
			case 3:
				chest(player,block);
				break;
			
			
		}
	}
	
	private static void telepoint(Player player, Block block)
	{
		Jail.Message("Teleport point selected. Now select sign, associated with this cell. If there is no such sign, click on any non-sign block.", player);
		CreationPlayer cr = players.get(player.getName());
		cr.cell.setTeleportLocation(player.getLocation());
		cr.state++;
		
	}

	private static void sign(Player player, Block block)
	{
		Jail.Message("Sign selected. Now select chest, associated with this cell. If there is no such chest, click on any non-chest block.", player);
		CreationPlayer cr = players.get(player.getName());
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
			cr.cell.setSign(block.getLocation());
		cr.state++;
		
	}
	
	
	private static void chest(Player player, Block block)
	{
		CreationPlayer cr = players.get(player.getName());
		if (block.getType() == Material.CHEST)
		{
			cr.cell.setChest(block.getLocation());
			if (block.getRelative(BlockFace.NORTH).getType() == Material.CHEST) cr.cell.setSecondChest(block.getRelative(BlockFace.NORTH).getLocation());
			else if (block.getRelative(BlockFace.SOUTH).getType() == Material.CHEST) cr.cell.setSecondChest(block.getRelative(BlockFace.SOUTH).getLocation());
			else if (block.getRelative(BlockFace.EAST).getType() == Material.CHEST) cr.cell.setSecondChest(block.getRelative(BlockFace.EAST).getLocation());
			else if (block.getRelative(BlockFace.WEST).getType() == Material.CHEST) cr.cell.setSecondChest(block.getRelative(BlockFace.WEST).getLocation());

		}
			
		
		cr.cell.getJail().getCellList().add(cr.cell);
		InputOutput.InsertCell(cr.cell);
		players.remove(player.getName());
		players.put(player.getName(), new CreationPlayer(cr.cell.getJail().getName()));
		Jail.Message("Chest selected and cell created. Now select teleport point of next cell. To stop creating, type /jailstop", player);

		
	}
	

	
	private static class CreationPlayer
	{
		public int state;
		
		private JailCell cell;		
		public CreationPlayer(String name)
		{
			state = 1;
			cell = new JailCell(name, "");
		}
}

}
