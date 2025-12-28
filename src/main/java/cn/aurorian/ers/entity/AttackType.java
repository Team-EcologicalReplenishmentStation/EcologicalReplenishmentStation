package cn.aurorian.ers.entity;

import static cn.aurorian.ers.entity.AttackStrategy.*;

public enum AttackType{
    EMPTY("empty", 0, EMPTY_STRATEGY,true),
    SWAMP_DRAGON_ATTACK("attack",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_ATTACK_WALK("attack-walk",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_ATTACK_RUN("attack-run",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_ATTACK_QUICK_SWIMMING("attack_quickly_swimming",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_ATTACK_SWIM_SHALLOW("attack_swim_shallow",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_ATTACK_SWIM_MID("attack_swim_mid",  20, SWAMP_DRAGON_ATTACK_STRATEGY,true),
    SWAMP_DRAGON_JUDGEMENT("attack2",  75, SWAMP_DRAGON_JUDGEMENT_STRATEGY,10),
    SWAMP_DRAGON_SPECIAL_ATTACK("attack3",  85, SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY),
    SWAMP_DRAGON_ATTACK_TURN("attack_turn",35 , SWAMP_DRAGON_TURNING_ATTACK_STRATEGY,false,5,true),
    SWAMP_DRAGON_ATTACK_SWIM_TURN("attack_swim_turn",30, SWAMP_DRAGON_TURNING_ATTACK_STRATEGY,false,5,true),
    SWAMP_DRAGON_SWIM_JUDGEMENT("swim_attack-placeholder",  80, SWAMP_DRAGON_JUDGEMENT_STRATEGY),
    SWAMP_DRAGON_SWIM_SPECIAL_ATTACK("swim_attack2",  85, SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY),
    SWAMP_DRAGON_JUMP_ATTACK("jump_attack",20, SWAMP_DRAGON_JUMP_ATTACK_STRATEGY,true,8),
    SWAMP_DRAGON_AFTER_JUMP_LAND("after_jump_land", 52, SWAMP_DRAGON_AFTER_JUMP_ATTACK_STRATEGY,false),
    SWAMP_DRAGON_CATCH_FISH_SMALL("catch_fish-small",600, SWAMP_DRAGON_CATCH_FISH_SMALL_STRATEGY,true),
    SWAMP_DRAGON_CATCH_FISH_MIDDLE("catch_fish-middle",600, SWAMP_DRAGON_CATCH_FISH_MIDDLE_STRATEGY,true),

    TUBUNASUS_ATTACK("attack", 20, TUBUNASUS_ATTACK_STRATEGY,true),
    TUBUNASUS_ATTACK_TURN("attack_turn", 45, TUBUNASUS_TURNING_ATTACK_STRATEGY,false,15,true),

    SAEVUS_ATTACK("attack", 35, SAEVUS_ATTACK_STRATEGY,true),
    SAEVUS_STRIKE("strike", 40, SAEVUS_STRIKE_STRATEGY,true,15),
    SAEVUS_ROAR("roar", 60, SAEVUS_ROAR_STRATEGY,true,5),
    SAEVUS_ATTACK_TURN("attack_turn", 50, SAEVUS_ATTACK_TURN_STRATEGY,false,15,true),
    SUCHOMIMUS_HOLD("hold", 300, SUCHOMIMUS_HOLD_STRATEGY,true),
    KNOCK_DOWN_LEFT("knockdown_left", 115, FALL_DOWN_STRATEGY,false),
    KNOCK_DOWN_RIGHT("knockdown_right", 115, FALL_DOWN_STRATEGY,false),
    JUMP("jump",15,EMPTY_STRATEGY,true,3);

    private final String animName;
    private final int animationLength;
    private final AttackExecutor executor;
    private final boolean canMove;
    private final int stamina;
    private final boolean turning;

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove, int stamina, boolean turning) {
        this.animName = animName;
        this.animationLength = animationLength;
        this.executor = executor;
        this.canMove = canMove;
        this.stamina = stamina;
        this.turning = turning;
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove, int stamina){
        this(animName, animationLength, executor, canMove, stamina, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, boolean canMove) {
        this(animName, animationLength, executor, canMove, 0, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor, int stamina){
        this(animName, animationLength, executor,false, stamina, false);
    }

    AttackType(String animName, int animationLength, AttackExecutor executor) {
        this(animName, animationLength, executor,false);
    }

    public String getAnimName() { return animName; }
    public int getAnimationLength() { return animationLength; }
    public boolean canMove() { return canMove; }
    public int getStaminaCost() { return stamina; }
    public boolean isTurning(){ return turning;}
    public void execute(MobAttack attack, ErsTamable<?> mount) {
        executor.execute(attack, mount);
    }
}
