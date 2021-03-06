package com.shatranjava.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.shatranjava.engine.Alliance;
import com.shatranjava.engine.Coordinate;
import com.shatranjava.engine.board.Board;
import com.shatranjava.engine.board.Move;
import com.shatranjava.engine.board.Move.*;
import com.shatranjava.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private static final Coordinate[] CANDIDATE_MOVE_COORDINATES = {
            new Coordinate(1, 0),
            new Coordinate(2, 0),
            new Coordinate(1, 1),
            new Coordinate(1, -1)
    };

    public Pawn(Coordinate pieceCoordinate, Alliance pieceAlliance) {
        super(pieceCoordinate, pieceAlliance, true);
    }

    public Pawn(Coordinate pieceCoordinate, Alliance pieceAlliance, boolean isFirstMove) {
        super(pieceCoordinate, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        Coordinate candidateDestinationCoordinate;
        List<Move> legalMoves = new ArrayList<>();

        for (final Coordinate currentCoordinate : CANDIDATE_MOVE_COORDINATES) {
            Coordinate directedCoordinate = Coordinate.mult(
                    currentCoordinate,
                    getPieceAlliance().getDirection()
            );
            candidateDestinationCoordinate = Coordinate.add(
                    getPieceCoordinate(),
                    directedCoordinate
            );

            if (candidateDestinationCoordinate.isValidCoordinate()) {

                final Tile candidateDestinationTile =
                        board.getTile(candidateDestinationCoordinate);
                //Non attacking move(forward)
                if ((currentCoordinate.getX() == 1 ||
                        currentCoordinate.getX() == -1) &&
                        currentCoordinate.getY() == 0) {
                    //Forward by 1, no attacking
                    if (!candidateDestinationTile.isTileOccupied()) {
                        if (getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnMove(board,
                                    this,
                                    candidateDestinationCoordinate))
                            );
                        } else {
                            legalMoves.add(new PawnMove(board,
                                    this,
                                    candidateDestinationCoordinate)
                            );
                        }
                    }
                    //Non attacking move(jump)
                } else if ((currentCoordinate.getX() == 2 ||
                        currentCoordinate.getX() == -2) &&
                        isFirstMove()) {
                    final Coordinate behindCandidateDestinationCoordinate =
                            Coordinate.add(
                                    getPieceCoordinate(),
                                    Coordinate.mult(
                                            new Coordinate(1, 0),
                                            getPieceAlliance().getDirection()
                                    )
                            );
                    if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                            !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        legalMoves.add(new PawnJump(board,
                                this,
                                candidateDestinationCoordinate)
                        );
                    }
                } else if (currentCoordinate.getX() == currentCoordinate.getY()) {
                    if (candidateDestinationTile.isTileOccupied()) {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (pieceAlliance != getPieceAlliance()) {
                            if (getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(
                                        board,
                                        this,
                                        candidateDestinationCoordinate,
                                        pieceAtDestination))
                                );
                            } else {
                                legalMoves.add(new PawnAttackMove(
                                        board,
                                        this,
                                        candidateDestinationCoordinate,
                                        pieceAtDestination)
                                );
                            }
                        }
                    }
                } /*else {
                    if (candidateDestinationTile.isTileOccupied()) {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (pieceAlliance != getPieceAlliance()) {
                            legalMoves.add(new PawnAttackMove(
                                    board,
                                    this,
                                    candidateDestinationCoordinate,
                                    pieceAtDestination)
                            );
                        }
                    }
                }**/

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getPieceMoved().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(getPieceCoordinate(), getPieceAlliance());
    }
}
