package org.dragonet.proxy.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginException;
import org.pf4j.util.FileUtils;
import org.yaml.snakeyaml.Yaml;


public class YmlPluginDescriptorFinder implements PluginDescriptorFinder {

    protected String propertiesFileName = "plugin.yml";

    public YmlPluginDescriptorFinder() {
    }

    @Override
    public boolean isApplicable(Path pluginPath) {
        return Files.exists(pluginPath) && (Files.isDirectory(pluginPath) || FileUtils.isJarFile(pluginPath));
    }

    @Override
    public PluginDescriptor find(Path pluginPath) throws PluginException {
        return readYml(pluginPath);
    }

    protected DefaultPluginDescriptor readYml(Path pluginPath) throws PluginException {
       JarFile jarFile = null;
        try{
            jarFile = new JarFile(pluginPath.toString());
        }catch(Exception ex){
            throw new PluginException("Cannot find the plugin.yml");
        }
        ZipEntry entry = jarFile.getEntry(propertiesFileName);
        if (entry == null) {
            try{
                jarFile.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            throw new PluginException("Cannot find the plugin.yml");
        }

        try{
            ConfigContent content = new Yaml().loadAs(jarFile.getInputStream(entry), ConfigContent.class);
            jarFile.close();
            return new DefaultPluginDescriptor(content.name, content.description, content.main, content.version, content.requires, content.author, null);
        }catch(IOException e){
            e.printStackTrace();
            throw new PluginException(e);
        }

    }

    private static class ConfigContent{
        public String name;
        public String description = "";
        public String main;
        public String version = "1.0";
        public String requires = "";
        public String author = "";
    }

}