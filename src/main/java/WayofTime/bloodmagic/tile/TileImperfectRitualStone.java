package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import lombok.NoArgsConstructor;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@NoArgsConstructor
public class TileImperfectRitualStone extends TileEntity implements IImperfectRitualStone
{
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    // IImperfectRitualStone

    @Override
    public boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player)
    {
        if (imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual))
        {
            if (!PlayerHelper.isFakePlayer(player) && !world.isRemote)
            {
                NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, imperfectRitual.getActivationCost());
                if (imperfectRitual.onActivate(this, player))
                {
                    if (imperfectRitual.isLightshow())
                        getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ(), true));
                    return true;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public World getRitualWorld()
    {
        return getWorld();
    }

    @Override
    public BlockPos getRitualPos()
    {
        return getPos();
    }
}
