package com.shatranjava.engine.board;

import com.shatranjava.engine.pieces.Piece;

/**
 * Created by Ankur Gupta on 15/8/17.
 * guptaankur.gupta0@gmail.com
 *
 * This class represent a single tile of 64 tiles in chess
 */
public abstract class Tile {

    protected final int mTileCoordinate;

    public Tile(int tileCoordinate) {
        mTileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();


    public abstract Piece getPiece();

    /**
     * A tile can be empty
     */
    public static class EmptyTile extends Tile {

        public EmptyTile(final int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    /**
     * A tile can be occupied by {@link Piece}
     */
    public static class OccupiedTile extends Tile {

        private final Piece mPieceOnTile;

        public OccupiedTile(Piece pieceOnTile, int tileCoordinate) {
            super(tileCoordinate);
            mPieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return mPieceOnTile;
        }
    }

}
