package com.samb.trs.Utilities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Controllers.AssetController;
import com.samb.trs.Resources.Atlases;

import java.util.Iterator;

public class TextureRegionEnumBuilder {
    public static void printAllTextureRegions(AssetController controller){
        int k = 0;
        for(int i = 0; i < Atlases.values().length; i++){
            Atlases a = Atlases.values()[i];
            Iterator<TextureAtlas.AtlasRegion> it = new Array.ArrayIterator<>(controller.getAsset(a).getRegions());
            while (it.hasNext()){
                TextureAtlas.AtlasRegion region = it.next();
                System.out.print(region.name.toUpperCase().replace("-", "_")+"(\""+region.name+"\", Atlases."+a.name().toUpperCase()+")");
                if(it.hasNext()) System.out.print(", ");
                if(++k % 5 == 0) System.out.println();
            }
            if(i == Atlases.values().length-1) System.out.println(";");
        }
    }
}
