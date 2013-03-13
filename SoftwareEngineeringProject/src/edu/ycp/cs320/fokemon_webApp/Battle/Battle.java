package edu.ycp.cs320.fokemon_webApp.Battle;

import java.util.Random;

import edu.ycp.cs320.fokemon_webApp.shared.MoveClasses.EffectType;
import edu.ycp.cs320.fokemon_webApp.shared.MoveClasses.Move;
import edu.ycp.cs320.fokemon_webApp.shared.PokemonClasses.PokeType;
import edu.ycp.cs320.fokemon_webApp.shared.PokemonClasses.Pokemon;
import edu.ycp.cs320.fokemon_webApp.shared.PokemonClasses.Status;
import edu.ycp.cs320.fokemon_webApp.shared.player.Player;

public class Battle {
	private Player user;
	private Player opponent;
	private Weather weather;
	private Random rand;
	
	
	public Battle(Player user, Player opponent){
		this.setUser(user);
		this.setOpponent(opponent);
		setInitialCurrentPokemon(user,0);
		setInitialCurrentPokemon(opponent,0);
	}
	public void Turn(){
		Pokemon userPoke=user.getTeam(user.getCurrentPokemonIndex());
		Pokemon oppPoke=opponent.getTeam(user.getCurrentPokemonIndex());
		switch(user.getChoice()){
		 case MOVE:
			 if (opponent.getChoice()==TurnChoice.MOVE){
				 double userSpeed=userPoke.getStats().getSpd()*
						 getStatMod(userPoke.getTempBattleStats().getSPDBoost() + userPoke.getMove(user.getMoveIndex()).getMovePriority());
				 //*SpeedAbilityMod*userPoke.getItem.getSpeedMod();
				 if(userPoke.getStats().getStatus()==Status.PRL)userSpeed*=.25;
				 
				 double oppSpeed=oppPoke.getStats().getSpd()*
						 getStatMod(oppPoke.getTempBattleStats().getSPDBoost() + oppPoke.getMove(opponent.getMoveIndex()).getMovePriority());
				 //*SpeedAbilityMod*oppPoke.getItem.getSpeedMod();
				 if(oppPoke.getStats().getStatus()==Status.PRL)oppSpeed*=.25;
				 
				 if(userSpeed>=oppSpeed){
					 attack(userPoke,oppPoke,userPoke.getMove(user.getMoveIndex()));
					 if(oppPoke.getStats().getStatus()!=Status.FNT)attack(oppPoke,userPoke,oppPoke.getMove(opponent.getMoveIndex()));
				 }else{
					 attack(oppPoke,userPoke,oppPoke.getMove(opponent.getMoveIndex()));
					 if(userPoke.getStats().getStatus()!=Status.FNT)attack(userPoke,oppPoke,userPoke.getMove(user.getMoveIndex()));
				 }
				 
				 
			 }else{
				 
			 }
	        	break;
	     default:
	    	 	break;        
	    	 	
		}
	}
	public int CalcDamage(Pokemon attacker, Pokemon defender, Move move){
		Random rand=new Random();
		int atk=0, def=0, R=100, CH=1, basePower=0;
		double mod1=1, mod2=1, mod3=1, stab=1, type=1;
		int Num=rand.nextInt(100);
		if(attacker.getInfo().getType().contains(move.getPokeType()))stab=1.5;
		if(Num>=100-Math.pow(6.25, attacker.getTempBattleStats().getCRITBoost())) CH=2;
		R-=rand.nextInt(15);
		type=CalcSuperEffective(defender,move);
		basePower=move.getDamage();//times itemmultiplier,user and foe ability
		if(attacker.getStats().getStatus()==Status.BRN)mod1*=.5;
		if((move.getPokeType()==PokeType.FIRE && weather==Weather.SUNNY)|| (move.getPokeType()==PokeType.WATER && weather==Weather.RAINY))mod1*=1.5;
		if((move.getPokeType()==PokeType.WATER && weather==Weather.SUNNY)|| (move.getPokeType()==PokeType.FIRE && weather==Weather.RAINY))mod1*=.5;
		//mod2=1.3 if holding life orb
		//mod3 is special abilities we might not be using
		if (move.getPhysical()==true){
			atk=(int) (attacker.getStats().getAtk()*getStatMod(attacker.getTempBattleStats().getATKBoost())); //abilityMod,ItemMod
			def=(int) (defender.getStats().getDef()*getStatMod(defender.getTempBattleStats().getDEFBoost())); //abilityMod,ItemMod
		}else{
			atk=(int) (attacker.getStats().getSpAtk()*getStatMod(attacker.getTempBattleStats().getSPATKBoost())); //abilityMod,ItemMod
			def=(int) (defender.getStats().getSpDef()*getStatMod(defender.getTempBattleStats().getSPDEFBoost())); //abilityMod,ItemMod
		}
		
		int damage=(int) ((attacker.getInfo().getLvl()*2/5 +2)*basePower*atk/50/def*mod1+2);
		damage=(int) (damage*CH*mod2*R/100*stab*type*mod3);
		return damage;
	}
	public void attack(Pokemon attacker, Pokemon defender, Move move){
		double accuracy, evasion;
		int damage=0;
		Random rand=new Random();
		if(attacker.getTempBattleStats().getACCBoost()>=0){
			if(attacker.getTempBattleStats().getACCBoost()>6)attacker.getTempBattleStats().setACCBoost(6);
			accuracy=(attacker.getTempBattleStats().getACCBoost()+3)/3;
		}else{
			if(attacker.getTempBattleStats().getACCBoost()<-6)attacker.getTempBattleStats().setACCBoost(-6);
			accuracy=3/(-1*attacker.getTempBattleStats().getACCBoost()+3);
		}
		if(defender.getTempBattleStats().getEVABoost()>=0){
			if(defender.getTempBattleStats().getEVABoost()>6)defender.getTempBattleStats().setEVABoost(6);
			evasion=(defender.getTempBattleStats().getEVABoost()+3)/3;
		}
		else{
			if(defender.getTempBattleStats().getEVABoost()<-6)defender.getTempBattleStats().setEVABoost(-6);
			evasion=3/(-1*defender.getTempBattleStats().getEVABoost()+3);
		}
		
		if(move.getAccuracy()*accuracy/evasion>=rand.nextInt(100) || move.getAccuracy()<0){//move hits
			damage=CalcDamage(attacker, defender, move);
			defender.getStats().setCurHp(defender.getStats().getCurHp()-damage);
			//effect
			if(defender.getStats().getCurHp()<=0){
				defender.getStats().setCurHp(0);
				defender.getStats().setStatus(Status.FNT);
			}
				
		}
	}
	public double CalcSuperEffective(Pokemon defender, Move move){
		return 1;
	}
	public double getStatMod(int modLevel){
		if (modLevel>=0){
			if(modLevel>6)modLevel=6;
			return (modLevel+2)/2;
		}else
			if(modLevel<-6)modLevel=-6;
			return 2/(-1*modLevel+2);
	}
	public void setInitialCurrentPokemon(Player player, int index){
		if(player.getTeam(index).getStats().getStatus()==Status.FNT){
			setInitialCurrentPokemon(player, index++);
		}else{
			player.setCurrentPokemonIndex(index);
		}
	}
	public Player getUser() {
		return user;
	}


	public void setUser(Player user) {
		this.user = user;
	}


	public Player getOpponent() {
		return opponent;
	}


	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

}
