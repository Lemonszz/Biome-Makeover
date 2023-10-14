package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.item.Item;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatModel;
import party.lemons.biomemakeover.entity.render.feature.HatModel;
import party.lemons.biomemakeover.entity.render.feature.WitchHatModel;
import party.lemons.biomemakeover.init.BMItems;

import java.util.HashMap;
import java.util.Map;

public class HatModels
{
    private static final Map<Item, HatModel> models = new HashMap<>();

    public static Model getHatModel(Item item, ModelPart baseHead)
    {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        HatModel model = null;

        if(models.containsKey(item)) {
            model = models.get(item);
        }
        else if(item == BMItems.COWBOY_HAT.get())    //TODO Move to lib, make easier to register these
        {
            model = new CowboyHatModel<>(modelSet.bakeLayer(CowboyHatModel.LAYER_LOCATION));
            models.put(item, model);
        }
        else if(item == BMItems.WITCH_HAT.get())
        {
            model = new WitchHatModel<>(modelSet.bakeLayer(WitchHatModel.LAYER_LOCATION));
            models.put(item, model);
        }

        model.copyHead(baseHead);

        return (Model) model;
    }
}
