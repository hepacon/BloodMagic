package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpell;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpellRegistry;

public class TESpellTable extends TileEntity
{
    public boolean canCastSpell()
    {
        return true;
    }
    
    public int getCostForSpell()
    {
    	HomSpell spell = getSpell();

        if (spell != null)
        {
            switch (getModifiedParadigm())
            {
                case 0:
                    return spell.getOffensiveRangedEnergy();

                case 1:
                    return spell.getOffensiveMeleeEnergy();
                    
                case 2:
                    return spell.getDefensiveEnergy();

                case 3:
                    return spell.getEnvironmentalEnergy();
            }
        }

        return 0;
    }

    public int castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        HomSpell spell = getSpell();

        if (spell != null)
        {
            switch (getModifiedParadigm())
            {
                case 0:
                    spell.onOffensiveRangedRightClick(par1ItemStack, par2World, par3EntityPlayer);
                    return spell.getOffensiveRangedEnergy();

                case 1:
                    spell.onOffensiveMeleeRightClick(par1ItemStack, par2World, par3EntityPlayer);
                    return spell.getOffensiveMeleeEnergy();
                    
                case 2:
                    spell.onDefensiveRightClick(par1ItemStack, par2World, par3EntityPlayer);
                    return spell.getDefensiveEnergy();

                case 3:
                    spell.onEnvironmentalRightClick(par1ItemStack, par2World, par3EntityPlayer);
                    return spell.getEnvironmentalEnergy();
            }
        }

        return 0;
    }

    public HomSpell getSpell()
    {
    	for(EnumFacing face : EnumFacing.HORIZONTALS)
    	{
    		TileEntity tileEntity = worldObj.getTileEntity(pos.offset(face));

            if (tileEntity instanceof TEAltar)
            {
                ItemStack itemStack = ((TEAltar) tileEntity).getStackInSlot(0);

                if (itemStack != null)
                {
                    HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

                    if (spell != null)
                    {
                        return spell;
                    }
                }
            }
    	}

        return null;
    }

    public int getModifiedParadigm()
    {
        //TODO change so that it works with a Tile Entity for a custom head or whatnot

        TileEntity tileEntity = worldObj.getTileEntity(pos.offsetUp());

        if (tileEntity instanceof TileEntitySkull)
        {
            int skullType = ((TileEntitySkull) tileEntity).getSkullType();

            switch (skullType)
            {
                case 0:
                    return 0;

                case 1:
                    return 1;

                case 2:
                    return 2;

                case 4:
                    return 3;
            }
        }

        return -1;
    }
}