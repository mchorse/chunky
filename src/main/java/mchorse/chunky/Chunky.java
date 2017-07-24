package mchorse.chunky;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = "chunky", name = "Chunky", version = Chunky.VERSION, clientSideOnly = true)
public final class Chunky
{
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static Chunky instance;

    @EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    public static class EventListener
    {
        @SubscribeEvent
        public void keyInput(InputEvent.KeyInputEvent event)
        {
            if (Keyboard.getEventKeyState())
            {
                int key = Keyboard.getEventKey();

                if (key == Keyboard.KEY_G)
                {
                    ChunkyState.freeze = !ChunkyState.freeze;
                }
                else if (key == Keyboard.KEY_H)
                {
                    ChunkyState.stopChunkUpdate = !ChunkyState.stopChunkUpdate;
                }
            }
        }
    }
}