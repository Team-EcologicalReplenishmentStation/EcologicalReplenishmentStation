package cn.aurorian.ers.entity;

import static cn.aurorian.ers.entity.AttackStrategy.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum AttackType implements ErsAttackType {
    EMPTY("empty", 0, EMPTY_STRATEGY, true),
    SWAMP_DRAGON_ATTACK(20, SWAMP_DRAGON_ATTACK_STRATEGY, true, "attack", "attack2"),
    SWAMP_DRAGON_ATTACK_WALK(20, SWAMP_DRAGON_ATTACK_STRATEGY, true, "attack-walk", "attack-walk2"),
    SWAMP_DRAGON_ATTACK_RUN(20, SWAMP_DRAGON_ATTACK_STRATEGY, true, "attack-run", "attack-run2"),
    SWAMP_DRAGON_ATTACK_QUICK_SWIMMING("attack_quickly_swimming", 20, SWAMP_DRAGON_ATTACK_STRATEGY, true),
    SWAMP_DRAGON_ATTACK_SWIM_SHALLOW("attack_swim_shallow", 20, SWAMP_DRAGON_ATTACK_STRATEGY, true),
    SWAMP_DRAGON_ATTACK_SWIM_MID("attack_swim_mid", 20, SWAMP_DRAGON_ATTACK_STRATEGY, true),
    SWAMP_DRAGON_JUDGEMENT("attack2", 75, SWAMP_DRAGON_JUDGEMENT_STRATEGY, 10),
    SWAMP_DRAGON_SPECIAL_ATTACK("attack3", 85, SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY),
    SWAMP_DRAGON_ATTACK_TURN("attack_turn", 35, SWAMP_DRAGON_TURNING_ATTACK_STRATEGY, false, 5, true, false),
    SWAMP_DRAGON_ATTACK_SWIM_TURN("attack_swim_turn", 30, SWAMP_DRAGON_TURNING_ATTACK_STRATEGY, false, 5, true, false),
    SWAMP_DRAGON_SWIM_JUDGEMENT("swim_attack-placeholder", 80, SWAMP_DRAGON_JUDGEMENT_STRATEGY),
    SWAMP_DRAGON_SWIM_SPECIAL_ATTACK("swim_attack2", 85, SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY),
    SWAMP_DRAGON_JUMP_ATTACK("jump_attack", 20, SWAMP_DRAGON_JUMP_ATTACK_STRATEGY, true, 8),
    SWAMP_DRAGON_AFTER_JUMP_LAND("after_jump_land", 52, SWAMP_DRAGON_AFTER_JUMP_ATTACK_STRATEGY, false),
    SWAMP_DRAGON_CATCH_FISH_SMALL("catch_fish-small", 600, SWAMP_DRAGON_CATCH_FISH_SMALL_STRATEGY, true),
    SWAMP_DRAGON_CATCH_FISH_MIDDLE("catch_fish-middle", 600, SWAMP_DRAGON_CATCH_FISH_MIDDLE_STRATEGY, true),
    SWAMP_DRAGON_JUMP("jump", 15, EMPTY_STRATEGY, true, 5),

    SAEVUS_ATTACK(35, SAEVUS_ATTACK_STRATEGY, true, "attack", "attack2"),
    SAEVUS_STRIKE("strike", 40, SAEVUS_STRIKE_STRATEGY, true, 15),
    SAEVUS_ROAR("roar", 60, SAEVUS_ROAR_STRATEGY, true, 5),
    SAEVUS_ATTACK_TURN("attack_turn", 50, SAEVUS_ATTACK_TURN_STRATEGY, false, 15, true, false),

    SUCHOMIMUS_HOLD("hold", 300, SUCHOMIMUS_HOLD_STRATEGY, true),
    PLANIDENS_HOLD("hold", 120, PLANIDENS_HOLD_STRATEGY, true),

    DINOSAURIFORMIS_ATTACK(18, DINOSAURIFORMIS_ATTACK_STRATEGY, true, "attack", "attack2"),
    DINOSAURIFORMIS_JUMP("jump", 15, EMPTY_STRATEGY, true, 5),

    ANTIQUUS_ATTACK(18, ANTIQUUS_ATTACK_STRATEGY, true, "attack"),
    ANTIQUUS_ATTACK_TURN("attack_turn", 25, ANTIQUUS_ATTACK_TURN_STRATEGY, false, 5, true, false),

    CRISTATODROMEUS_KICK("attack", 24, CRISTATODROMEUS_KICK_STRATEGY, false),

    TUBUNASUS_ATTACK("attack", 20, TUBUNASUS_ATTACK_STRATEGY, true),
    TUBUNASUS_ATTACK_TURN("attack_turn", 45, TUBUNASUS_TURNING_ATTACK_STRATEGY, false, 15, true, false),
    TUBUNASUS_STRUGGLE("struggle", 35, TUBUNASUS_STRUGGLE_STRATEGY, false, 5, false, false),

    REGIUS_ATTACK("attack", 20, REGUIS_ATTACK_STRATEGY, true),
    REGIUS_ATTACK_TURN("attack_turn", 35, REGUIS_ATTACK_TURN_STRATEGY, false, 5, true, false),
    REGIUS_JUMP_ATTACK("hold", 30, REGUIS_JUMP_ATTACK_STRATEGY, false, 3, false, true),
    REGIUS_ROAR("roar", 65, REGUIS_ROAR_STRATEGY, true, 0),
    REGIUS_JUMP("jump", 40, EMPTY_STRATEGY, true, 2, false, true),

    KNOCK_DOWN_LEFT("knockdown_left", 115, FALL_DOWN_STRATEGY, false, true),
    KNOCK_DOWN_RIGHT("knockdown_right", 115, FALL_DOWN_STRATEGY, false, true);

    private final String[] animName = new String[3];
    private final int animationLength;
    private final AttackExecutor executor;
    private final boolean canMove;
    private final int stamina;
    private final boolean turning;
    private final boolean cancelable;
    private final boolean forcePlay;

    AttackType(
            String animName,
            int animationLength,
            AttackExecutor executor,
            boolean canMove,
            int stamina,
            boolean turning,
            boolean cancelable) {
        this.animName[0] = animName;
        this.animationLength = animationLength;
        this.executor = executor;
        this.canMove = canMove;
        this.stamina = stamina;
        this.turning = turning;
        this.cancelable = cancelable;
        this.forcePlay = false;
    }

    AttackType(
            int animationLength,
            AttackExecutor executor,
            boolean canMove,
            int stamina,
            boolean turning,
            boolean cancelable,
            String... animNames) {
        System.arraycopy(animNames, 0, this.animName, 0, Math.min(animNames.length, 3));
        this.animationLength = animationLength;
        this.executor = executor;
        this.canMove = canMove;
        this.stamina = stamina;
        this.turning = turning;
        this.cancelable = cancelable;
        this.forcePlay = false;
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove, int stamina) {
        this(animName, animationLength, executor, canMove, stamina, false, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove) {
        this(animName, animationLength, executor, canMove, 0, false, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove, boolean forcePlay) {
        this.animName[0] = animName;
        this.animationLength = animationLength;
        this.executor = executor;
        this.canMove = canMove;
        this.stamina = 0;
        this.turning = false;
        this.cancelable = false;
        this.forcePlay = forcePlay;
    }

    AttackType(int animationLength, AttackExecutor executor, boolean canMove, String... animNames) {
        this(animationLength, executor, canMove, 0, false, false, animNames);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, int stamina) {
        this(animName, animationLength, executor, false, stamina, false, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor) {
        this(animName, animationLength, executor, false);
    }

    @Override
    public String getId() {
        return name().toLowerCase();
    }

    public String getAnimName() {
        return animName[
                (int) (Math.random()
                        * Arrays.stream(animName).filter(Objects::nonNull).count())];
    }

    public int getAnimationLength() {
        return animationLength;
    }

    public boolean canMove() {
        return canMove;
    }

    public int getStaminaCost() {
        return stamina;
    }

    public boolean isTurning() {
        return turning;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public boolean isForcePlay() {
        return forcePlay;
    }

    public void execute(MobAttack attack, ErsTamable<?> mount) {
        executor.execute(attack, mount);
    }

    private static final Map<String, AttackType> NAME_MAP = new HashMap<>();

    static {
        for (AttackType type : AttackType.values()) {
            for (String name : type.animName) {
                if (name != null) {
                    NAME_MAP.put(name.toLowerCase(), type);
                }
            }
        }
    }

    public static AttackType fromAnimName(String name) {
        if (name == null) return EMPTY;
        return NAME_MAP.getOrDefault(name.toLowerCase(), EMPTY);
    }
}
