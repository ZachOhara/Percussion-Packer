package io.github.zachohara.percussionpacker.cardtype;

import io.github.zachohara.percussionpacker.card.Card;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class GhostCard extends Card {
	
	public static final double BACKGROUND_OPACITY = 0.10;
	
	public static final String DECORATION_COLOR = "dodgerblue";
	
	public static final Color BORDER_COLOR = Color.web(DECORATION_COLOR);
	public static final Color BACKGROUND_COLOR = Color.web(DECORATION_COLOR, BACKGROUND_OPACITY);
	
	public static final double RADIUS = 2;
	
	public static final String STYLE = generateBackgroundColorString() + "-fx-background-radius: "
			+ RADIUS + RADIUS + RADIUS + RADIUS + "; -fx-border-color: " + DECORATION_COLOR
			+ "; -fx-border-radius: " + RADIUS + RADIUS + RADIUS + RADIUS;

	public GhostCard(Card sizingCard) {
		super(sizingCard.getHeight(), false, false);
		this.copySizing(sizingCard);
		this.hideChildren();
		
		this.setStyle(STYLE);
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
	
	private static String generateBackgroundColorString() {
		return "-fx-background-color: rgba("
				+ (255 * BACKGROUND_COLOR.getRed()) + ", "
				+ (255 * BACKGROUND_COLOR.getGreen()) + ", "
				+ (255 * BACKGROUND_COLOR.getBlue()) + ", "
				+ BACKGROUND_COLOR.getOpacity() + "); ";
	}

}
