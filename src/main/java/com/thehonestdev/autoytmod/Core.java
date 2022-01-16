package com.thehonestdev.autoytmod;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod(name = "AutoYTMod", version = "1.0", modid = "AutoYTMod1.0")
public class Core{

    private boolean isNicked = false;
    private boolean stayNicked = true;

    public Core(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if(checkForOBS()){
                   if(!isNicked){
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick rank default");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick skin random");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/stream open 50");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/status busy");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/friend toggle");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/party mute");
                       MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick");
                       Minecraft.getMinecraft().thePlayer.sendChatMessage("§aStreamer/Recording Mode enabled!");
                       isNicked = true;
                   }
               }else{
                   if(isNicked){
                    if(stayNicked){
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/party disband");
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/friend toggle");
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/status online");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("§aStreamer/Recording Mode is disabled, but you remain nicked.");

                    }else{
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick reset");
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/party disband");
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/friend toggle");
                        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/status online");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("§cStreamer/Recording Mode is disabled!");
                        isNicked = false;
                    }
                   }else{
                    //TODO -  literally nothing
                   }
               }
            }
        }, 300000L);
    }

    public boolean checkForOBS(){

        final AtomicBoolean bool = new AtomicBoolean();
        bool.set(false);
        new Thread(() -> {
            try{
                String line;
                String pidInfo ="";

                Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

                BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

                while ((line = input.readLine()) != null) {
                    pidInfo+=line;
                }

                input.close();

                if(pidInfo.contains("OBS.exe")) {
                    bool.set(true);
                }
            }catch(IOException exception){
                exception.printStackTrace();
            }

        }).start();
        return bool.get();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent event){
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick rank default");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick skin random");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/stream open 50");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/status busy");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/friend toggle");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/party mute");
        MinecraftServer.getServer().getCommandManager().executeCommand(Minecraft.getMinecraft().thePlayer, "/nick");
        Minecraft.getMinecraft().thePlayer.sendChatMessage("§aStreamer/Recording Mode enabled!");
    }

}
