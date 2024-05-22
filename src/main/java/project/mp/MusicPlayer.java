/**
 * Author : Kwame
 * Runs the main application
 */

package project.mp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MusicPlayer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicPlayer.class.getResource("music-player.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),631,357);
        Image icon = new Image(String.valueOf(MusicController.class.getResource("listen.png")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("K_A-Player");



        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
