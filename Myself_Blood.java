package Monster;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.LetterOpener;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import powers.bossbuff.InnerEvil_Power;
import powers.bossbuff.YourPowerComesFromMe;

import java.util.ArrayList;
import java.util.Iterator;

public class Myself_Blood extends CustomMonster {

    public static final String ID = "Myself";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Myself");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private final AbstractPlayer p = AbstractDungeon.player;
    private boolean firstTurn;
    private boolean firstTurn1;
    private boolean attacked;
    private int attackDmg;
    private int debuff;
    private boolean talky = true;
    private boolean firstMove = true;
    private int idleCount = 0;
    private boolean STAGE = true;
    private final int AT0 = 6;
    private int AT1;
    private int AT2 = 12;
    private int AT3 = 16;
    private int AT4 = 20;
    private int AT5 = 24;
    private int AT6 = 50;
    private int AT7 = 17;
    private int AT8 = 20;
    private int AT9 = 21;
    private int turns = 0;
    private boolean BUFF1 = false;
    private boolean BUFF2 = false;
    private boolean ATTACK1 = false;
    private boolean ATTACK2 = false;
    private boolean ATTACK3 = false;
    private boolean BLOCK1 = false;
    private boolean UNKNOWN1 = false;
    private boolean STUN1 = false;
    private boolean TURNSTART = false;
    private int STAGEturn = 1;
    private int DMGtimes1 = 3;
    private int LARGEDMG = 50;
    private int BLOCKREDUCE = 15;
    private String TEXT1 ;
    private String TEXT2 ;
    private String TEXT3 ;
    private String TEXT4 ;
    private String TEXT5 ;
    private String TEXT6 ;
    private AnimationState.TrackEntry e;
    private AnimationState.TrackEntry e1;
    private int invincibleAmt;

    public Myself_Blood(){
        super(NAME, ID, 495, 0.0F, 0.0F, 255.0F, 386.0F,
                "img/MonsterIMG/Myself.png");
        this.type = EnemyType.BOSS;
        this.firstTurn = true;
        this.firstTurn1 = true;
        this.attacked = false;

        /*
        loadAnimation("img/MonstersIMG/uuzboss.atlas", "img/MonstersIMG/uuzboss.json", 1.0F);
        e = this.state.setAnimation(0, "normal-1", true);;
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.0F);

         */


        if(Settings.language == Settings.GameLanguage.ZHS){
            TEXT1 = "让我们来互相杀戮吧！";
            TEXT2 = "你该不会还想着可以复活吧？";
            TEXT3 = "杀掉！杀掉！ ~哈哈哈哈哈哈~ ";
            TEXT4 = "还没完呢！";
            TEXT5 = "等我完全占据了你，我要把红魔馆的所有人都杀了！";
            TEXT6 = "我必然会战胜你，回到姐姐大人身边的！";
        }

        if (AbstractDungeon.ascensionLevel >= 9) {
            if(AbstractDungeon.ascensionLevel >= 19){
                setHp(1200);
                this.invincibleAmt = 300;
            }else
            setHp(1000);
            this.invincibleAmt = 350;
        } else {
            setHp(800);
            this.invincibleAmt = 400;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {


            this.AT1 = 35;
        } else {
            this.debuff = 2;

            this.AT1 = 20;
        }
        if(AbstractDungeon.ascensionLevel >= 19){
            this.debuff = 4;
        } else {
            this.debuff = 3;
        }
        this.damage.add(new DamageInfo(this, AT0));
        this.damage.add(new DamageInfo(this, AT1));
        this.damage.add(new DamageInfo(this, AT2));
        this.damage.add(new DamageInfo(this, AT3));
        this.damage.add(new DamageInfo(this, AT4));
        this.damage.add(new DamageInfo(this, AT5));
        this.damage.add(new DamageInfo(this, AT6));
        this.damage.add(new DamageInfo(this, AT7));
        this.damage.add(new DamageInfo(this, AT8));
        this.damage.add(new DamageInfo(this, AT9));

        this.attackDmg = 10;
    }

    public void takeTurn() {
        switch (this.nextMove)
        {
            case 1:
                addToBot(new TalkAction(this,TEXT1));
                addToBot(new VFXAction(new HeartMegaDebuffEffect()));
                addToBot(new ApplyPowerAction(this,this,new ArtifactPower(this,2)));
                addToBot(new ApplyPowerAction(p,this,new WeakPower(p,this.debuff,true)));
                addToBot(new ApplyPowerAction(p,this,new FrailPower(p, this.debuff, true)));
                this.BUFF1 = false;
                this.BUFF2 = true;
                addToBot(new RollMoveAction(this));
                break;
            case 2:
                ArrayList<AbstractRelic> R = new ArrayList();
                for(AbstractRelic r:p.relics){
                    if(!r.relicId.equals("TrueAncestorsBlood") && !r.relicId.equals("Lizard Tail")){
                        R.add(r);
                    }
                }
                p.relics.clear();
                for(AbstractRelic r:R){
                    r.instantObtain();
                }
                for(AbstractPotion potions : p.potions){
                    if(potions.ID.equals("FairyPotion")){
                        p.removePotion(potions);
                    }
                }
                addToBot(new TalkAction(this,TEXT2));
                addToBot(new ApplyPowerAction(this,this, new InnerEvil_Power(this)));
                addToBot(new ApplyPowerAction(this,this, new YourPowerComesFromMe(this)));
                addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmt), invincibleAmt));
                this.BUFF2 = false;
                this.ATTACK1 = true;
                addToBot(new RollMoveAction(this));
                break;
            case 3:
                addToBot(new DamageAction(p, new DamageInfo(this, 7, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new DamageAction(p, new DamageInfo(this, 7, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                this.ATTACK1 = false;
                this.ATTACK2 = true;
                addToBot(new RollMoveAction(this));
                break;
            case 4:
                addToBot(new DamageAction(p, new DamageInfo(this, 40, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.ATTACK2 = false;
                this.BLOCK1 = true;
                addToBot(new RollMoveAction(this));
                break;
            case 5:
                addToBot(new GainBlockAction(this,this,20));
                addToBot(new ApplyPowerAction(p,this,new WeakPower(this,1,true)));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,1)));
                this.BLOCK1 = false;
                this.UNKNOWN1 = true;
                addToBot(new RollMoveAction(this));
                break;
            case 6:
                addToBot(new TalkAction(this,TEXT3));
                this.UNKNOWN1 = false;
                this.ATTACK3 = true;
                addToBot(new RollMoveAction(this));
            case 7:
                for(int i=0;i<17;i++){
                    addToBot(new DamageAction(p, new DamageInfo(this, 1, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                this.ATTACK3 = false;
                this.STUN1 = true;
                addToBot(new RollMoveAction(this));
            case 8:
                this.STUN1 = false;
                this.TURNSTART = true;
                addToBot(new RollMoveAction(this));
            case 9:
                if(this.STAGEturn == 1) {
                    for(int AMT=0;AMT<DMGtimes1;AMT++){
                        addToBot(new DamageAction(p, new DamageInfo(this, 7, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                    }
                }
                if(this.STAGEturn == 2) {
                    addToBot(new DamageAction(p, new DamageInfo(this, this.LARGEDMG, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                if(this.STAGEturn == 3){
                    if(this.BLOCKREDUCE>0)
                    addToBot(new GainBlockAction(this,this,BLOCKREDUCE));
                    addToBot(new ApplyPowerAction(p,this,new WeakPower(this,1,true)));
                    addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,1)));
                }
                this.STAGEturn ++;
                if(STAGEturn >3) {
                    this.STAGEturn = 1;
                    this.DMGtimes1 ++;
                    this.LARGEDMG += 10;
                    this.BLOCKREDUCE -= 5;
                }
                addToBot(new RollMoveAction(this));
                break;

            case 10:

        }

    }


    protected void getMove(int i) {
        if(STAGE){
            if (this.firstMove)
            {
                this.firstMove = false;
                this.BUFF1 = true;
                setMove((byte)1, AbstractMonster.Intent.STRONG_DEBUFF);
            }
            if(BUFF2){
                setMove((byte)2, Intent.UNKNOWN);
            }
            if(ATTACK1){
                setMove((byte)3, Intent.ATTACK,7,2,true);
            }
            if(ATTACK2){
                setMove((byte)4, Intent.ATTACK,40);
            }
            if(BLOCK1){
                setMove((byte)5, Intent.DEFEND_BUFF);
            }
            if(UNKNOWN1){
                setMove((byte)6, Intent.UNKNOWN);
            }
            if(ATTACK3){
                setMove((byte)7, Intent.ATTACK,1,17,true);
            }
            if(STUN1){
                setMove((byte)8, Intent.STUN);
            }
            if(TURNSTART){
                if(this.STAGEturn == 1) {
                    setMove((byte)9, Intent.ATTACK,7,DMGtimes1,true);
                }
                if(this.STAGEturn == 2) {
                    setMove((byte)9, Intent.ATTACK,LARGEDMG);
                }
                if(this.STAGEturn == 3){
                    if(this.BLOCKREDUCE >0)
                    setMove((byte)9, Intent.DEFEND_BUFF);
                    else setMove((byte)9, Intent.STRONG_DEBUFF);
                }
            }
        }
    }

    public void damage(DamageInfo info)
    {
        if(this.currentHealth - info.output > 0)
            super.damage(info);
        else if(STAGE){
            this.isDying = true;
            addToBot(new TalkAction(this,TEXT4));
            setMove((byte)10, Intent.UNKNOWN);
        }else {
            super.damage(info);
        }
    }
}
