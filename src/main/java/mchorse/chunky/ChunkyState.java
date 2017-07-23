package mchorse.chunky;

import java.util.List;

import net.minecraft.client.renderer.chunk.RenderChunk;

public class ChunkyState
{
    public static boolean freeze = false;

    public static boolean stopChunkUpdate = false;

    public static void clearRenderContainer(List list)
    {
        if (!freeze)
        {
            list.clear();
        }
    }

    public static void addRenderChunk(List<RenderChunk> list, RenderChunk renderChunk)
    {
        if (!freeze)
        {
            list.add(renderChunk);
        }
    }
}