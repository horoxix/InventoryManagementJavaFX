package biz;

import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import java.util.function.UnaryOperator;

public class Helper {
    public static class TextHelper {
        private static final String numericRegex = "[0-9]*";
        private static final String nonNumericRegex = "[a-zA-Z]*";
        private static final String decimalRegex = "\\d*\\.?\\d*";

        /**
         * Returns the text update if it is a number, matching the numericRegex.
         */
        public static final UnaryOperator<TextFormatter.Change> numericFilter = change -> {
            String text = change.getText();

            if (text.matches(numericRegex)) {
                return change;
            }
            return null;
        };

        /**
         * Returns the text update if it is an alphabetical character, matching the nonNumericRegex
         */
        public static final UnaryOperator<TextFormatter.Change> nonNumericFilter = change -> {
            String text = change.getText();

            if (text.matches(nonNumericRegex)) {
                return change;
            }
            return null;
        };

        /**
         * Returns the text update if it matches a decimal value, matching the decimalRegex
         */
        public static final UnaryOperator<TextFormatter.Change> decimalFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(decimalRegex)) {
                return change;
            }
            return null;
        };

        /**
         *
         * @return A label designed for no search results found.
         */
        public static Label getNoResultsLabel(){
            Label noResultsLabel = new Label();
            noResultsLabel.setTextFill(Color.web("red"));
            noResultsLabel.setText("No Results Found");
            return noResultsLabel;
        }
    }
}
