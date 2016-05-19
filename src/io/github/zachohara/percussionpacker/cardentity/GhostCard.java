package io.github.zachohara.percussionpacker.cardentity;

import io.github.zachohara.percussionpacker.util.GraphicsUtil;
import javafx.scene.paint.Color;

public class GhostCard extends CardEntity {

	public static final double BACKGROUND_OPACITY = 0.10;

	public static final String DECORATION_COLOR = "dodgerblue";

	public static final Color BORDER_COLOR = Color.web(DECORATION_COLOR);
	public static final Color BACKGROUND_COLOR = Color.web(DECORATION_COLOR, BACKGROUND_OPACITY);

	public static final double RADIUS = 2;
	public static final double INSET = 3;

	public static final String STYLE = GhostCard.generateBackgroundColorString()
			+ "; -fx-background-radius: " + RADIUS + "; -fx-background-insets: " + INSET
			+ "; -fx-border-radius: " + RADIUS + "; -fx-border-insets: " + INSET
			+ "; -fx-border-color: " + DECORATION_COLOR;

	public GhostCard(CardEntity sizingCard) {
		super(false, false, false);
		
		GraphicsUtil.copySizing(sizingCard, this);

		this.setStyle(STYLE);
	}

	private static String generateBackgroundColorString() {
		return "-fx-background-color: rgba("
				+ (255 * BACKGROUND_COLOR.getRed()) + ", "
				+ (255 * BACKGROUND_COLOR.getGreen()) + ", "
				+ (255 * BACKGROUND_COLOR.getBlue()) + ", "
				+ BACKGROUND_COLOR.getOpacity() + ")";
	}

}
