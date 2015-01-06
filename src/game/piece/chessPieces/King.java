package game.piece.chessPieces;

import java.io.IOException;
import java.util.ArrayList;

import game.board.node.Location;
import game.board.node.Node;
import game.move.ChessMove;
import game.move.Move;
import game.piece.Piece;
import game.piece.Piece.Loyalty;

/**
 * A class representing a chess king
 * 
 * @author Benjamin Cohen-Wang
 */
public class King extends Piece
{
	/**
	 * Default constructor
	 * 
	 * @param loyalty	the value the loyalty of this piece
	 * @throws IOException 
	 */
	public King(Loyalty loyalty) throws IOException
	{
		super(loyalty);
		
		if(!imagesInitialized)
		{
			imagesInit();
		}
		
		image = (getLoyalty() == Loyalty.RED) ? WHITE_KING: BLACK_KING;
	}
	
	/**
	 * Parameterized constructor, initializes loyalty and node to given variables
	 * 
	 * @param node	the node of this instance on the board
	 * @param loyalty	the value the loyalty is set to
	 * @throws IOException 
	 */
	public King(Loyalty loyalty, Node node) throws IOException
	{
		super(loyalty, node);
		
		if(!imagesInitialized)
		{
			imagesInit();
		}
		
		image = (getLoyalty() == Loyalty.RED) ? WHITE_KING: BLACK_KING;
	}
	
	/**
	 * Parameterized constructor, initializes this to a copy of the given piece
	 * 
	 * @param piece	the piece to be copied
	 * @param node	the node to be added to
	 * @throws IOException 
	 */
	public King(Piece piece, Node node) throws IOException
	{
		super(piece.getLoyalty(), node);
		
		if(!imagesInitialized)
		{
			imagesInit();
		}
		
		image = (getLoyalty() == Loyalty.RED) ? WHITE_KING: BLACK_KING;
	}

	/**
	 * Returns the possible move this piece can do
	 * 
	 * @return	the array list of possible moves this piece execute
	 */
	public ArrayList<Move> getPossibleMoves()
	{
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		for(int i = -1; i <= 1; i ++)
		{
			for (int j = -1; j <= 1; j ++)
			{
				Location moveLoc = new Location(getNode().getLoc().getRow() + i, getNode().getLoc().getCol() + j);
				
				if(!moveLoc.equals(getNode().getLoc()))
				{
					if(getNode().getBoard().isValid(moveLoc) && (getNode().getBoard().getNode(moveLoc).getPiece() == null || getNode().getBoard().getNode(moveLoc).getPiece().getLoyalty() != this.getLoyalty()))
					{
						ArrayList<Node> move = new ArrayList<Node>();
						
						move.add(getNode());
						move.add(getNode().getBoard().getNode(moveLoc));
						
						possibleMoves.add(new ChessMove(move, getNode().getBoard(), getLoyalty()));
					}
				}
			}
		}
		
		return possibleMoves;
	}
}