/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2017
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.util.crafting;

import mods.railcraft.api.core.items.IPrototypedItem;
import mods.railcraft.common.util.inventory.InvTools;
import mods.railcraft.common.util.inventory.filters.StackFilters;
import mods.railcraft.common.util.inventory.wrappers.InventoryComposite;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static mods.railcraft.common.util.inventory.InvTools.setSize;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class PrototypeRecipe extends BaseRecipe {
    private static Predicate<ItemStack> PROTOTYPE_CONTAINER = StackFilters.of(IPrototypedItem.class);

    public PrototypeRecipe() {
        super("prototype");
    }

    @Override
    public boolean matches(InventoryCrafting grid, @Nullable World world) {
        InventoryComposite inv = InventoryComposite.of(grid);
        int containerStacks = InvTools.countStacks(inv, PROTOTYPE_CONTAINER);
        if (containerStacks != 1)
            return false;
        ItemStack container = InvTools.findMatchingItem(inv, PROTOTYPE_CONTAINER);
        return !InvTools.isEmpty(container) && InvTools.countStacks(inv, validPrototype(container)) == 1;
    }

    private Predicate<ItemStack> validPrototype(ItemStack container) {
        return s -> !InvTools.isItemEqual(s, container, false, false) && ((IPrototypedItem) container.getItem()).isValidPrototype(s);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting grid) {
        if (!matches(grid, null))
            return InvTools.emptyStack();
        InventoryComposite inv = InventoryComposite.of(grid);
        ItemStack container = InvTools.findMatchingItem(inv, PROTOTYPE_CONTAINER);
        if (InvTools.isEmpty(container))
            return InvTools.emptyStack();
        ItemStack prototype = InvTools.findMatchingItem(inv, validPrototype(container));
        if (!InvTools.isEmpty(prototype))
            return ((IPrototypedItem) container.getItem()).setPrototype(container, prototype);
        return InvTools.emptyStack();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return InvTools.emptyStack();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        ItemStack[] grid = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < grid.length; ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!InvTools.isEmpty(stack) && !PROTOTYPE_CONTAINER.test(stack)) {
                stack = stack.copy();
                setSize(stack, 1);
                grid[i] = stack;
            }
        }

        return NonNullList.from(ItemStack.EMPTY, grid);
    }
}
