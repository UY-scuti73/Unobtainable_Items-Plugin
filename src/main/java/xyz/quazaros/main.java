package xyz.quazaros;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public final class main extends JavaPlugin implements Listener {
    ArrayList<item> itemList;

    String setting_string;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initialize_items();
        initialize_settingString();
        getServer().getPluginManager().registerEvents(this, this);
        System.out.println("Unobtainable Items Plugin Has Started");
    }

    @Override
    public void onDisable() {
        System.out.println("Unobtainable Items Plugin Has Stopped");
    }

    private void initialize_items() {
        getConfig();
        itemList = new ArrayList<>();

        itemList.add(new item("Dirt Variants", Arrays.asList("dirt_path", "farmland"), getConfig().getString("dirt_variants.tool"), getConfig().getString("dirt_variants.enable")));
        itemList.add(new item("Spawners", Arrays.asList("spawner", "trial_spawner", "vault"), getConfig().getString("spawners.tool"), getConfig().getString("spawners.enable")));
        itemList.add(new item("Infested Stones", Arrays.asList("infested_stone", "infested_cobblestone", "infested_stone_bricks", "infested_cracked_stone_bricks", "infested_mossy_stone_bricks", "infested_chiseled_stone_bricks", "infested_deepslate"), getConfig().getString("infested_stones.tool"), getConfig().getString("infested_stones.enable")));
        itemList.add(new item("Suspicious Blocks", Arrays.asList("suspicious_sand", "suspicious_gravel"), getConfig().getString("suspicious_blocks.tool"), getConfig().getString("suspicious_blocks.enable")));
        itemList.add(new item("Reinforced Deepslate", Arrays.asList("reinforced_deepslate"), getConfig().getString("reinforced_deepslate.tool"), getConfig().getString("reinforced_deepslate.enable")));
        itemList.add(new item("Amethyst", Arrays.asList("budding_amethyst"), getConfig().getString("amethyst.tool"), getConfig().getString("amethyst.enable")));
        itemList.add(new item("Chorus Plant", Arrays.asList("chorus_plant"), getConfig().getString("chorus_plant.tool"), getConfig().getString("chorus_plant.enable")));
        itemList.add(new item("Frog Eggs", Arrays.asList("frogspawn"), getConfig().getString("frog_eggs.tool"), getConfig().getString("frog_eggs.enable")));
    }

    private void initialize_settingString() {
        ChatColor CC1 = ChatColor.LIGHT_PURPLE;
        ChatColor CC2 = ChatColor.AQUA;

        setting_string = "";

        String temp;
        for (item i : itemList) {
            if (i.enabled) {
                temp = CC1 + i.name + ": " + CC2 + i.enabled_string + " - " + i.min_tool_string + "\n";
            } else {
                temp = CC1 + i.name + ": " + CC2 + i.enabled_string + "\n";
            }
            setting_string = setting_string + temp;
        }
    }

    private item in_material_list(Material material) {
        for (item i : itemList) {
            for (Material m : i.materials) {
                if (m == material) {return i;}
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.isOp() && command.getName().equalsIgnoreCase("itemsettings")) {
            sender.sendMessage(setting_string);
        }
        return true;
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        item Item = in_material_list(block.getType());
        if (Item != null) {
            Player player = event.getPlayer();
            ItemStack weapon = player.getItemInHand();

            //Will run the command if the set of items in the plugin is enabled, the item is of the correct type or is "any", and the item has silk touch
            if (Item.enabled && (Item.min_tool.contains(weapon.getType()) || Item.any) && weapon.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
            }
        }
    }
}
