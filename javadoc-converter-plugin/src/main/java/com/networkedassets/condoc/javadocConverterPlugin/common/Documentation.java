package com.networkedassets.condoc.javadocConverterPlugin.common;

import java.util.ArrayList;
import java.util.List;

public class Documentation {
    private List<DocumentationPiece> pieces = new ArrayList<>();

    public Documentation() {
    }

    public List<DocumentationPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<DocumentationPiece> pieces) {
        this.pieces = pieces;
    }
}
