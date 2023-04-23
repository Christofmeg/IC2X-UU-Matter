package com.christofmeg.ic2xuumatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = IC2XUUMatter.MODID, name = IC2XUUMatter.NAME, version = IC2XUUMatter.VERSION, acceptedMinecraftVersions = "[1.12, 1.13)", dependencies = "required-after:ic2@[2.8.70,);required-after:jei@[4.12,);", clientSideOnly = true)

public class IC2XUUMatter {
    public static final String MODID = "ic2xuumatter";
    public static final String NAME = "IC2X UU-Matter";
    public static final String VERSION = "1.1.1";

    public static final Logger log = LogManager.getLogger(MODID);

}
