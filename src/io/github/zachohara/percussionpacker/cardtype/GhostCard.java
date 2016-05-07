package io.github.zachohara.percussionpacker.cardtype;

import io.github.zachohara.percussionpacker.card.Card;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class GhostCard extends Card {

	public GhostCard(Card sizingCard) {
		super(sizingCard.getHeight(), false, false);
		this.copySizing(sizingCard);
		this.hideChildren();

		// for testing
		this.setStyle("-fx-background-color: pink");
	}

	private void copySizing(Region copyFrom) {
		this.setMinWidth(copyFrom.getMinWidth());
		this.setMinHeight(copyFrom.getMinHeight());
		this.setPrefWidth(copyFrom.getPrefWidth());
		this.setPrefHeight(copyFrom.getPrefHeight());
		this.setMaxWidth(copyFrom.getMaxWidth());
		this.setMaxHeight(copyFrom.getMaxHeight());
	}

	private void hideChildren() {
		for (Node n : this.getChildren()) {
			n.setVisible(false);
		}
	}

}
