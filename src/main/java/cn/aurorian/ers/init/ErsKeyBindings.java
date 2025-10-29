package cn.aurorian.ers.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class ErsKeyBindings {
    public static final String CATEGORY = "key.categories.ers";
    public static final KeyMapping DIVE_KEY = new KeyMapping(
        "key.ers.dive",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_Z,
        CATEGORY
    );
    
	public static final KeyMapping ATTACK_KEY = new KeyMapping(
		"key.ers.attack",
		KeyConflictContext.IN_GAME,
		InputConstants.Type.KEYSYM,
		InputConstants.KEY_G, 
		CATEGORY
	);
	public static final KeyMapping ATTACK2_KEY = new KeyMapping(
        "key.ers.attack2",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_J,
        CATEGORY
    );
    public static final KeyMapping ATTACK3_KEY = new KeyMapping(
        "key.ers.attack3",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_K,
        CATEGORY
    );
	public static final KeyMapping ATTACK4_KEY = new KeyMapping(
			"key.ers.attack4",
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_LALT,
			CATEGORY
	);
}
