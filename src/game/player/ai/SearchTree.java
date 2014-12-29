package game.player.ai;

import game.Move;
import game.piece.Piece;
import game.player.Player;

import java.util.ArrayList;

/**
 * A class representing a search tree of the minimax algorithm
 * 
 * @author Benjamin Cohen-Wang
 */
public class SearchTree
{
	/** The root node of this minimax search tree **/
	private MinimaxNode root;
	
	/** The depth of this search tree **/
	private int depth;
	
	/** The player using this search tree **/
	private AI player;
	
	/**
	 * Parameterized constructor, initializes search tree to given root node
	 * 
	 * @param root	the root node this tree is initialized to
	 */
	public SearchTree(MinimaxNode root, AI player)
	{
		this.root = root;
		this.depth = 0;
		this.player = player;
	}
	
	/**
	 * Executes the next iteration of iterative deepening
	 */
	public void increaseDepth()
	{
		depth ++;
		
		performMinimax(root, Integer.MIN_VALUE);
	}
	
	/**
	 * Assigns minimax values to all nodes covered by the given depth
	 * 
	 * @param minimaxDepth the depth to be searched
	 */
	public void performMinimax(MinimaxNode node, double alphaBetaVal)
	{	
		if(node.getMinimaxDepth() >= depth)
		{
		  	node.setValue(functionVal(node));
		  	return;
		}
	
		ArrayList<MinimaxNode> children = node.getChildren();
		
		if(node.getGame().getPlayers()[node.getGame().getTurn().getVal()].isDefeated())
		{
		  	node.setValue(functionVal(node));
		  	return;
		}
		
		boolean thisPlayersTurn = player.getLoyalty().getVal() == node.getGame().getTurn().getVal();
		
		double extreme;
		
		if(node.getMinimaxDepth() + 1 < depth)
		{
			children = heuristicSort(children);
		}
		
		if(thisPlayersTurn)
		{
			extreme = Integer.MIN_VALUE;
			
			for(MinimaxNode nextNode : children)
			{	
				performMinimax(nextNode, extreme);
				
				if(nextNode.getValue() > extreme)
		  		{
		  			extreme = nextNode.getValue();
		  		}
				
				if(alphaBetaVal <= nextNode.getValue())
				{
					break;
				}
			}
		} 
		else
		{
			extreme = Integer.MAX_VALUE;
			
			for(MinimaxNode nextNode : children)
			{
				performMinimax(nextNode, extreme);

				if(nextNode.getValue() < extreme)
				{
					extreme = nextNode.getValue();
				}
				
				if(alphaBetaVal >= nextNode.getValue())
				{
					break;
				}
			}
		}
		
		node.setValue(extreme);
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
		double functionVal = 0;
		
		boolean hasWon = true;
		
		for(Player enemy : players)
		{
			if(enemy.getLoyalty() != player.getLoyalty())
			{
				if(!enemy.isDefeated())
				{
					hasWon = false;
				}
			}
		}
		
		if(hasWon)
		{
			return Double.MAX_VALUE;
		}
		
		if(player.isDefeated())
		{
			return Double.MIN_VALUE;
		}
		
		for(Player player : players)
		{
			if(player.getLoyalty() == player.getLoyalty())
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
	
	private ArrayList<MinimaxNode> heuristicSort(ArrayList<MinimaxNode> children)
	{
		if(children.isEmpty())
		{
			return children;
		}
		
		ArrayList<MinimaxNode> sorted = new ArrayList<MinimaxNode>();
		
		sorted.add(children.get(0));
		
		for(int i = 1; i < children.size(); i ++)
		{
			for(int j = sorted.size() - 1; j >= 0; j --)
			{
				if(children.get(i).getValue() <= sorted.get(j).getValue())
				{
					sorted.add(j + 1, children.get(i));
				}
				else if(j == 0)
				{
					sorted.add(0, children.get(i));
				}
			}
		}
		
		return sorted;
	}
	
	/**
	 * Returns the node with the given identification
	 * 
	 * @return	the node containing the given identification string
	 */
	public MinimaxNode getNode(String identification)
	{
		MinimaxNode currentNode = root;
		
		for(int i = 0; i < identification.length(); i ++)
		{
			int currentIdentification = (int)identification.charAt(i) - 48;
			
			currentNode = currentNode.getChildren().get(currentIdentification);
		}
		
		return currentNode;
	}
}
