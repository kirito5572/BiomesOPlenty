/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.block;

import biomesoplenty.api.block.BOPBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class BarnaclesBlock extends MultifaceBlock implements SimpleWaterloggedBlock
{
    public static final MapCodec<BarnaclesBlock> CODEC = simpleCodec(BarnaclesBlock::new);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BarnaclesBlock(Properties p_153282_)
    {
        super(p_153282_);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    public MapCodec<BarnaclesBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153309_)
    {
        super.createBlockStateDefinition(p_153309_);
        p_153309_.add(WATERLOGGED);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random)
    {
        if (state.getValue(WATERLOGGED))
        {
            tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, tickAccess, pos, facing, facingPos, facingState, random);
    }

    @Override
    public boolean canBeReplaced(BlockState p_153299_, BlockPlaceContext p_153300_)
    {
        return !p_153300_.getItemInHand().is(BOPBlocks.BARNACLES.asItem()) || super.canBeReplaced(p_153299_, p_153300_);
    }

    @Override
    public MultifaceSpreader getSpreader()
    {
        return new MultifaceSpreader(new MultifaceSpreader.DefaultSpreaderConfig(null)
        {
            @Nullable
            public BlockState getStateForPlacement(BlockState p_221694_, BlockGetter p_221695_, BlockPos p_221696_, Direction p_221697_)
            {
                return null;
            }

            @Override
            protected boolean stateCanBeReplaced(BlockGetter p_221688_, BlockPos p_221689_, BlockPos p_221690_, Direction p_221691_, BlockState p_221692_)
            {
                return false;
            }

            @Override
            public boolean canSpreadInto(BlockGetter p_221685_, BlockPos p_221686_, MultifaceSpreader.SpreadPos p_221687_)
            {
                return false;
            }
        });
    }

    @Override
    public FluidState getFluidState(BlockState p_153311_)
    {
        return p_153311_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153311_);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_181225_)
    {
        return p_181225_.getFluidState().isEmpty();
    }
}