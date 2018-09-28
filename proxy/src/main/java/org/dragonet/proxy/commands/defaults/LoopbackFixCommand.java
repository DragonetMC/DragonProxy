package org.dragonet.proxy.commands.defaults;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.commands.Command;
import org.dragonet.proxy.configuration.Lang;

import com.whirvis.jraknet.windows.UniversalWindowsProgram;

public class LoopbackFixCommand extends Command {

	public LoopbackFixCommand(String name) {
		super(name,
				"Adds Minecraft to the Windows 10 loopback exemption list, allowing connections from the same machine that the server is running on");
	}

	@Override
	public void execute(DragonProxy proxy, String[] args) {
		if (UniversalWindowsProgram.MINECRAFT.isLoopbackExempt()) {
			proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_MCW10_LOOPBACK_ALREADY_EXEMPT));
			return; // Minecraft is already loopback exempt
		}
		if (!UniversalWindowsProgram.MINECRAFT.addLoopbackExempt()) {
			proxy.getLogger().info(proxy.getLang().get(Lang.ERROR_MCW10_LOOPBACK));
		} else {
			proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_MCW10_LOOPBACK_SUCCESS));
		}
	}

}
