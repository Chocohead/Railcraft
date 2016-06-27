package mods.railcraft.common.plugins.buildcraft;

import buildcraft.api.statements.StatementManager;
import mods.railcraft.common.plugins.buildcraft.actions.ActionProvider;
import mods.railcraft.common.plugins.buildcraft.triggers.TriggerProvider;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameData;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class BuildcraftPlugin {

    public static void init() {
        try {
            StatementManager.registerTriggerProvider(new TriggerProvider());
            StatementManager.registerActionProvider(new ActionProvider());
        } catch (Throwable error) {
            Game.logErrorAPI("Buildcraft", error, StatementManager.class);
        }
    }

    public static void addFacade(Block block, int meta) {
        if (block == null) return;
        FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", String.format("%s@%d", GameData.getBlockRegistry().getNameForObject(block), meta));
    }
}
