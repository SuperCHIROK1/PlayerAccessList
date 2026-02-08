package me.superchirok1.playeraccesslist.color;

import me.superchirok1.playeraccesslist.color.impl.LegacySerializer;
import me.superchirok1.playeraccesslist.color.impl.LegacyUpdatedSerializer;
import me.superchirok1.playeraccesslist.color.impl.MiniMessageSerializer;

public class Colorizer {

    public static TextColorizer get;
    public void init(Serializer serializer) {
        get = switch (serializer) {
            case LEGACY -> new LegacySerializer();
            case LEGACY_UPDATED -> new LegacyUpdatedSerializer();
            case MINIMESSAGE -> new MiniMessageSerializer();
        };
    }

}
