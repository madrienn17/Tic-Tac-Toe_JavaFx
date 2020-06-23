package tictactoegui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adry - madrienn17
 * Player 1 - X (right click)
 * Player 2 - O (left click)
 **/

public class TicTacToeApp  extends Application {
    private boolean turnX = true;
    private boolean over = false;
    private Tile[][] board = new Tile[3][3];
    private List<Combination> comboList = new ArrayList<>();

    private  Pane root = new Pane();

    private Parent create() {
        // let the boards size be 600x600
        root.setPrefSize(600,600);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);
                board[j][i] = tile;
            }
        }

        // horizontal strikes
        for(int i = 0; i < 3; ++i) {
            comboList.add(new Combination(board[0][i], board[1][i], board[2][i]));
        }

        // vertical strikes
        for(int j = 0; j < 3; ++j) {
            comboList.add(new Combination(board[j][0], board[j][1], board[j][2]));
        }

        // diagonal strikes
        comboList.add(new Combination(board[0][0], board[1][1], board[2][2]));
        comboList.add(new Combination(board[2][0], board[1][1], board[0][2]));

        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic - Tac - Toe  Game");
        primaryStage.setScene(new Scene(create()));
        primaryStage.show();
    }

    private void checkIfWon() {
        for(Combination combo : comboList) {
            if(combo.isComplete()) {
                over = true; // then the game is over
                playWinAnimation(combo); // trace a line along the winner combination
                PopUpWindow.display(); // display a popup window with a message
                break;
            }
        }
    }

    private void playWinAnimation(Combination combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCentreX());
        line.setStartY(combo.tiles[0].getCentreY());

        line.setEndX(combo.tiles[0].getCentreX());
        line.setEndY(combo.tiles[0].getCentreY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCentreX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCentreY())));
        timeline.play();
    }

    private class Combination {
        private Tile[] tiles;
        public Combination(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if(tiles[0].getValue().isEmpty()) {
                return false;
            }
            // returning true or false depending on equality of the 3 elements
            return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile() {
            Rectangle border = new Rectangle(200,200); // let each rectangle be 200x200 fo proper arranging
            border.setFill(Color.LAVENDER);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            text.setFont(Font.font(84));

            setOnMouseClicked(mouseEvent -> {
                if(over) {
                    return;
                }

                if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if(!turnX) {
                        return;
                    }
                    fillX();
                    turnX = false;
                    checkIfWon();
                }

                else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                    if(turnX) {
                        return;
                    }
                    fillO();
                    turnX = true;
                    checkIfWon();
                }
            });
        }

        public double getCentreX() {
            return getTranslateX() + 100;
        }

        public double getCentreY() {
            return getTranslateY() + 100;
        }

        public String getValue() {
            return text.getText();
        }

        private void fillX() {
            text.setText("X");
        }

        private void fillO() {
            text.setText("O");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}