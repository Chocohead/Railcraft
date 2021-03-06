/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2017
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.blocks.aesthetics.post;

import mods.railcraft.api.core.IPostConnection;
import mods.railcraft.api.core.IVariantEnum;
import mods.railcraft.common.plugins.color.ColorPlugin;
import mods.railcraft.common.plugins.color.EnumColor;
import mods.railcraft.common.plugins.forestry.ForestryPlugin;
import mods.railcraft.common.plugins.forge.*;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class BlockPostMetalBase extends BlockPostBase implements ColorPlugin.IColoredBlock {

    public static final PropertyEnum<EnumColor> COLOR = PropertyEnum.create("color", EnumColor.class);

    public BlockPostMetalBase() {
        setSoundType(SoundType.METAL);
    }

    @Override
    public void initializeDefinition() {
        HarvestPlugin.setBlockHarvestLevel("pickaxe", 2, this);

        ForestryPlugin.addBackpackItem("forestry.builder", this);

        for (EnumColor color : EnumColor.VALUES) {
            ItemStack stack = getStack(1, color);
            if (stack != null)
                RailcraftRegistry.register(this, color, stack);
        }
    }

    @Override
    public void finalizeDefinition() {
        ColorPlugin.instance.register(this, this);
    }

    @Override
    public void defineRecipes() {
        ItemStack stackColored = getWildcard();
        ItemStack stackRaw = EnumPost.METAL_UNPAINTED.getStack();

        for (EnumColor color : EnumColor.values()) {
            ItemStack outputStack = getStack(8, color);
            CraftingPlugin.addRecipe(outputStack,
                    "III",
                    "IDI",
                    "III",
                    'I', stackRaw,
                    'D', color.getDyeOreDictTag());
            CraftingPlugin.addRecipe(outputStack,
                    "III",
                    "IDI",
                    "III",
                    'I', stackColored,
                    'D', color.getDyeOreDictTag());
        }
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public StateMapperBase getStateMapper() {
        return new StateMap.Builder().ignore(COLOR).build();
    }

    @Nullable
    @Override
    public Class<? extends IVariantEnum> getVariantEnum() {
        return EnumColor.class;
    }

    @Override
    public IBlockState getState(@Nullable IVariantEnum variant) {
        IBlockState state = getDefaultState();
        if (variant != null) {
            checkVariant(variant);
            state = state.withProperty(COLOR, (EnumColor) variant);
        }
        return state;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(NORTH, PostConnectionHelper.connect(worldIn, pos, state, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, PostConnectionHelper.connect(worldIn, pos, state, EnumFacing.SOUTH));
        state = state.withProperty(EAST, PostConnectionHelper.connect(worldIn, pos, state, EnumFacing.EAST));
        state = state.withProperty(WEST, PostConnectionHelper.connect(worldIn, pos, state, EnumFacing.WEST));
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public IBlockState getItemRenderState(@Nullable IVariantEnum variant) {
        IBlockState state = getDefaultState();
        state = state.withProperty(EAST, IPostConnection.ConnectStyle.TWO_THIN);
        state = state.withProperty(WEST, IPostConnection.ConnectStyle.TWO_THIN);
        return state;
    }

    public EnumColor getColor(IBlockState state) {
        return state.getValue(COLOR);
    }

    @Override
    public IBlockColor colorHandler() {
        return (state, worldIn, pos, tintIndex) -> getColor(state).getHexColor();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (EnumColor color : EnumColor.VALUES) {
            CreativePlugin.addToList(list, getStack(1, color));
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        List<ItemStack> list = new ArrayList<ItemStack>();
        // TODO: Drop rusty
//        if (isPlatform)
//            list.add(EnumPost.METAL_PLATFORM_UNPAINTED.getStack());
//        else
//            list.add(EnumPost.METAL_UNPAINTED.getStack());
        list.add(getStack(1, EnumColor.BLACK));
        return list;
    }

    @Override
    public boolean recolorBlock(World world, @Nonnull BlockPos pos, EnumFacing side, @Nonnull EnumDyeColor color) {
        IBlockState state = WorldPlugin.getBlockState(world, pos);
        if (getColor(state).getDye() != color) {
            world.setBlockState(pos, getDefaultState().withProperty(COLOR, EnumColor.fromDye(color)));
            return true;
        }
        return false;
    }



    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getColor(state).getMapColor();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(COLOR, EnumColor.fromOrdinal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return getColor(state).ordinal();
    }
}
