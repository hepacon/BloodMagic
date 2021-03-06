package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ItemUpgradeTrainer extends Item implements IUpgradeTrainer, IVariantProvider
{
    public ItemUpgradeTrainer()
    {
        super();

        setCreativeTab(BloodMagic.tabUpgradeTome);
        setUnlocalizedName(Constants.Mod.MODID + ".upgradeTrainer");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        list.add(new ItemStack(this));
        for (Entry<String, Integer> entry : LivingArmourHandler.upgradeMaxLevelMap.entrySet())
        {
            String key = entry.getKey();
            ItemStack stack = new ItemStack(this);
            LivingUpgrades.setKey(stack, key);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
//        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.livingArmour"))));
        LivingArmourUpgrade upgrade = LivingUpgrades.getUpgrade(stack);
        if (upgrade != null)
        {
            tooltip.add(TextHelper.localize(upgrade.getUnlocalizedName()));
        }
    }

    @Override
    public List<String> getTrainedUpgrades(ItemStack stack)
    {
        List<String> keyList = new ArrayList<String>();
        String key = LivingUpgrades.getKey(stack);

        if (!key.isEmpty())
        {
            keyList.add(key);
        }

        return keyList;
    }

    @Override
    public boolean setTrainedUpgrades(ItemStack stack, List<String> keys)
    {
        if (keys.isEmpty())
        {
            return false;
        }

        LivingUpgrades.setKey(stack, keys.get(0));
        return true;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=upgradetrainer"));
        return ret;
    }
}
