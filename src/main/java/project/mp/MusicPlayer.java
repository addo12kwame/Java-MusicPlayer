package project.mp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MusicPlayer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicPlayer.class.getResource("music-player.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),500,500);

        stage.setScene(scene);
        stage.show();
    }
}
