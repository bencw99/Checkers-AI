package game.player.ai;

import game.Game;
import game.Move;
import game.piece.Piece;
import game.piece.Piece.Loyalty;
import game.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * A class representing a Human player associated with a checkers game
 * 
 * @author Benjamin Cohen-Wang
 */
public class AI extends Player
{
	/** The depth of the minimax search **/
	private static final int DEFAULT_MINIMAX_DEPTH = 7;
	
	private final int minimaxDepth;
	
	/**
	 * Parameterized constructor, initializes name, pieces, and loyalty
	 * 
	 * @param name	the name of this player
	 * @param loyalty	the loyalty of this player
	 * @param pieces	the pieces of this player
	 */
	public AI(String name, Loyalty loyalty, ArrayList<Piece> pieces)
	{
		this(name, loyalty, pieces, DEFAULT_MINIMAX_DEPTH);
	}
	
	/**
	 * Parameterized constructor, initializes name, pieces, and loyalty
	 * 
	 * @param name	the name of this player
	 * @param loyalty	the loyalty of this player
	 * @param pieces	the pieces of this player
	 * @param minimaxDepth	the minimaxDepth of this ai
	 */
	public AI(String name, Loyalty loyalty, ArrayList<Piece> pieces, int minimaxDepth)
	{
		super(name, loyalty, pieces);
		this.minimaxDepth = minimaxDepth;
	}

	/**
	 * Returns the move executed this turn
	 * 
	 * @return	the move to be executed this turn
	 * @throws IOException 
	 */
	public Move getThisTurnMove() throws IOException
	{	
		ArrayList<Move> possibleMoves = getPossibleMoves();
		
		if(isDefeated())
		{
			return null;
		}
		
		Move[] possibleMovesArray = new Move[possibleMoves.size()];
		
		MinimaxNode[] possibleNextNodes = new MinimaxNode[possibleMoves.size()];
		
		MinimaxNode currentNode = new MinimaxNode(0, new Game(getPieces().get(0).getNode().getBoard().getGame()));
		
		for(int i = 0; i < possibleMoves.size(); i ++)
		{
			possibleMovesArray[i] = possibleMoves.get(i);
			possibleNextNodes[i] = currentNode.getNextNode(possibleMoves.get(i));
		}
		
		double maxMinimaxVal = getMinimaxVal(possibleNextNodes[0]);
		
		ArrayList<Integer> maxMovesIndeces = new ArrayList<Integer>();
		maxMovesIndeces.add(0);
		
		for(int i = 1; i < possibleNextNodes.length; i ++)
		{
			double currentVal = getMinimaxVal(possibleNextNodes[i]);
			
			if(currentVal > maxMinimaxVal)
			{
				maxMovesIndeces.clear();
				maxMovesIndeces.add(i);
				maxMinimaxVal = currentVal;
			}
			
			if(currentVal == maxMinimaxVal)
			{
				maxMovesIndeces.add(i);
			}
		}
		
		int random = (int)(maxMovesIndeces.size()*Math.random());
		
		return possibleMovesArray[maxMovesIndeces.get(random)];
	}
	
	/**
	 * Returns the minimax val of the given move
	 * 
	 * @param minimaxNode	the move whose minimax val is evaluated
	 * @return	the minimax val of the given move
	 * @throws IOException 
	 */
	private double getMinimaxVal(MinimaxNode node) throws IOException
	{
		if(node.getMinimaxDepth() >= minimaxDepth)
		{
		  	return functionVal(node);
		}
		
		ArrayList<Move> nextMoves = node.getNextMoves();
		
		if(node.getGame().getPlayers()[node.getGame().getTurn().getVal()].isDefeated())
		{
		  	return functionVal(node);
		}
		
		double extreme = getMinimaxVal(node.getNextNode(nextMoves.get(0)));
		  
		if(getLoyalty().getVal() == node.getGame().getTurn().getVal())
		{
			for(Move nextMove : nextMoves)
			{
				double maxCand = getMinimaxVal(node.getNextNode(nextMove));
				
				if(maxCand > extreme)
		  		{
		  			extreme = maxCand;
		  		}
			}
		} 
		else
		{
			for(Move nextMove : nextMoves)
			{
				double minCand = getMinimaxVal(node.getNextNode(nextMove));
					
				if(minCand < extreme)
				{
					extreme = minCand;
				}
			}
		}
		
		return extreme;
	}
	
	/**
	 * Returns the minimax val of the given node
	 * 
	 * @param node	the minimaxNode whose minimax value is evaluated
	 * @throws IOException 
	 */
	private double getMinimaxVal(MinimaxNode node, int sameMethodSignaturePreventing) throws IOException
	{
		Stack<MinimaxNode> stack = new Stack<MinimaxNode>();
		
		stack.push(node);
		
		MinimaxNode currentNode = stack.peek();
		
		ArrayList<Move> nextMoves = currentNode.getNextMoves();
		
		double extreme = getMinimaxVal(currentNode.getNextNode(nextMoves.get(0)));
		
		while(!stack.isEmpty())
		{
			currentNode = stack.pop();
			
			nextMoves = currentNode.getNextMoves();
			
			if(getLoyalty().getVal() == currentNode.getGame().getTurn().getVal())
			{
				for(Move nextMove : nextMoves)
				{
					//Evaluate minimax of nextMove
					//Set extreme to max
				}
			} 
			else
			{
				for(Move nextMove : nextMoves)
				{
					//Evaluate minimax of nextMove
					//Set extreme to min
				}
			}
		}
		
		return extreme;
	}
	
	/**
	 * Returns the function value of the given move
	 * 
	 * @param move	the move whose function value is evaluated
	 * @return	the function value of the given move
	 */
	private double functionVal(MinimaxNode node)
	{
		Player[] players = node.getGame().getPlayers();
		double functionVal = node.getGame().getTurn().getVal() == getLoyalty().getVal() ? 3 : -3;
		
		if(isDefeated())
		{
			return Double.MIN_VALUE;
		}
		
		for(Player player : players)
		{
			if(player.getLoyalty().getVal() == getLoyalty().getVal())
			{
				for(Piece piece : player.getPieces())
				{
					functionVal += piece.getWorth();
				}
			}
			else
			{
				for(Piece piece : player.getPieces())
				{
					functionVal -= piece.getWorth();
				}
			}
		}
		
		return functionVal;
	}
}
