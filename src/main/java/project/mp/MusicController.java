package project.mp;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MusicController {

    @FXML
    private ButtonBar button;

    @FXML
    private Button playBtn;

    @FXML
    private ImageView playImg;
    @FXML
    private ListView<String> list;
    @FXML
            private MenuItem uploadSongBtn;

    Media media;
    MediaPlayer mediaPlayer;
    String currentSong;




//    public void
    public void playMusic(ActionEvent e){
        currentSong = list.getSelectionModel().getSelectedItem();
        if (mediaPlayer != null){
        mediaPlayer.stop();}
        media = new Media(currentSong);

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void getSong(ActionEvent e){
        FileChooser f = new FileChooser();
        File song = f.showOpenDialog(new Stage());
        list.getItems().add(song.toURI().toString());


    }
}
