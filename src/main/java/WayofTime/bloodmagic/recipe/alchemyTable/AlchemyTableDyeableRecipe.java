package WayofTime.bloodmagic.recipe.alchemyTable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;

public class AlchemyTableDyeableRecipe extends AlchemyTableRecipe
{
    private ItemStack inputItem;

    public AlchemyTableDyeableRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem)
    {
        super(inputItem, lpDrained, ticksRequired, tierRequired);

        ArrayList<ItemStack> validDyes = new ArrayList<ItemStack>();
        validDyes.add(new ItemStack(Items.NAME_TAG));
        validDyes.add(new ItemStack(Items.DYE, 1, OreDictionary.WILDCARD_VALUE));

        ArrayList<Object> recipe = new ArrayList<Object>();
        recipe.add(inputItem);
        recipe.add(validDyes);

        this.input = recipe;

        this.inputItem = inputItem;
    }

    @Override
    public ItemStack getRecipeOutput(List<ItemStack> inputList)
    {
        int nameTagOrDyeLocation = -1;
        int inputItemLocation = -1;
        for (int x = 0; x < inputList.size(); x++)
        {
            ItemStack slot = inputList.get(x);

            if (slot != null)
            {
                boolean match = OreDictionary.itemMatches(inputItem, slot, false);

                if (match)
                {
                    inputItemLocation = x;
                    continue;
                } else
                {
                    if (slot.getItem() == Items.NAME_TAG || slot.getItem() == Items.DYE)
                    {
                        nameTagOrDyeLocation = x;
                        continue;
                    }
                }
            }
        }

        if (nameTagOrDyeLocation != -1 && inputItemLocation != -1)
        {
            ItemStack tagOrDyeStack = inputList.get(nameTagOrDyeLocation);
            ItemStack inputStack = inputList.get(inputItemLocation);

            if (inputStack == null || tagOrDyeStack == null)
            {
                return output.copy();
            }

            ItemStack outputStack = inputStack.copy();

            if (tagOrDyeStack.getItem() == Items.NAME_TAG)
            {
                if (!outputStack.hasTagCompound())
                {
                    outputStack.setTagCompound(new NBTTagCompound());
                }

                outputStack.getTagCompound().setString(Constants.NBT.COLOR, tagOrDyeStack.getDisplayName());

                return outputStack;
            } else
            {
                EnumDyeColor dyeColor = ItemBanner.getBaseColor(tagOrDyeStack);
                if (!outputStack.hasTagCompound())
                {
                    outputStack.setTagCompound(new NBTTagCompound());
                }

                outputStack.getTagCompound().setString(Constants.NBT.COLOR, String.valueOf(dyeColor.getMapColor().colorValue));

                return outputStack;
            }
        }

        return output.copy();
    }

    @Override
    public boolean matches(List<ItemStack> checkedList, World world, BlockPos pos)
    {
        boolean hasNameTagOrDye = false;
        boolean hasInputItem = false;

        for (int x = 0; x < checkedList.size(); x++)
        {
            ItemStack slot = checkedList.get(x);

            if (slot != null)
            {
                boolean match = OreDictionary.itemMatches(inputItem, slot, false);

                if (match && hasInputItem)
                {
                    return false;
                } else if (match)
                {
                    hasInputItem = true;
                    continue;
                } else
                {
                    if (slot.getItem() == Items.NAME_TAG || slot.getItem() == Items.DYE)
                    {
                        if (hasNameTagOrDye)
                        {
                            return false;
                        } else
                        {
                            hasNameTagOrDye = true;
                        }
                    }
                }
            }
        }

        return hasNameTagOrDye && hasInputItem;
    }
}
