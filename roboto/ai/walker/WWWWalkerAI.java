package dre.elfocrash.roboto.ai.walker;

import java.util.LinkedList;
import java.util.Queue;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.FFFFakePlayerAI;
import dre.elfocrash.roboto.model.WWWWalkNode;
import dre.elfocrash.roboto.model.WWWWalkerType;

import net.sf.l2j.commons.random.Rnd;

public abstract class WWWWalkerAI extends FFFFakePlayerAI {

	protected Queue<WWWWalkNode> _walkNodes;
	private WWWWalkNode _currentWalkNode;
	private int currentStayIterations = 0;
	protected boolean isWalking = false;
	
	public WWWWalkerAI(FFFFakePlayer character) {
		super(character);
	}
	
	public Queue<WWWWalkNode> getWalkNodes(){
		return _walkNodes;
	}
	
	protected void addWalkNode(WWWWalkNode walkNode) {
		_walkNodes.add(walkNode);
	}
	
	@Override
	public void setup() {
		super.setup();		
		_walkNodes = new LinkedList<>();
		setWalkNodes();
	}
	
	@Override
	public void thinkAndAct() {
		setBusyThinking(true);		
		handleDeath();
		
		if(_walkNodes.isEmpty())
			return;
		
		if(isWalking) {
			if(userReachedDestination(_currentWalkNode)) {
				if(currentStayIterations < _currentWalkNode.getStayIterations() ) {
					currentStayIterations++;
					setBusyThinking(false);
					return;
				}				
				_currentWalkNode = null;
				currentStayIterations = 0;
				isWalking = false;
			}			
		}
		
		if(!isWalking && _currentWalkNode == null) {
			switch(getWalkerType()) {
				case RANDOM:
					_currentWalkNode = (WWWWalkNode) getWalkNodes().toArray()[Rnd.get(0, getWalkNodes().size() - 1)];
					break;
				case LINEAR:
					_currentWalkNode = getWalkNodes().poll();
					_walkNodes.add(_currentWalkNode);
					break;
			}
			_ffffakePlayer.getFFFFakeAi().moveTo(_currentWalkNode.getX(), _currentWalkNode.getY(), _currentWalkNode.getZ());	
			isWalking = true;
		}
		
		setBusyThinking(false);
	}

	@Override
	protected int[][] getBuffs() {
		return new int[0][0]; 
	}

	protected boolean userReachedDestination(WWWWalkNode targetWalkNode) {
		//TODO: Improve this with approximate equality and not strict
		if(_ffffakePlayer.getX() == targetWalkNode.getX()
			&& _ffffakePlayer.getY() == targetWalkNode.getY() 
			&& _ffffakePlayer.getZ() == targetWalkNode.getZ())
			return true;
		
		return false;
	}
	
	protected abstract WWWWalkerType getWalkerType();
	protected abstract void setWalkNodes();
}
