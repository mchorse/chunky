package mchorse.chunky;

import net.minecraftforge.fml.common.DummyModContainer;

public class ChunkyCoreModContainer extends DummyModContainer
{
    @Override
    public String getName()
    {
        return "Chunky Core";
    }

    @Override
    public String getModId()
    {
        return "chunky_core";
    }

    @Override
    public Object getMod()
    {
        return Chunky.instance;
    }

    @Override
    public String getVersion()
    {
        return Chunky.VERSION;
    }
}