package org.dragonet.proxy.utilities;

import java.nio.file.Path;
import org.dragonet.api.plugin.IPluginManager;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginDescriptorFinder;

public class PluginManager extends DefaultPluginManager{

    public PluginManager(Path pluginFolder){
        super(pluginFolder);
    }

    @Override
    protected PluginDescriptorFinder createPluginDescriptorFinder() {
        return new YmlPluginDescriptorFinder();
    }

}
