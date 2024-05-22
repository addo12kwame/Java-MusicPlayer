package project.mp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class MusicController {


    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView imgView;

    @FXML
    private Button playBtn1;

    @FXML
    private Button nextBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button previousBtn;

    @FXML
    private Slider volumeSlide;
    @FXML
    private Label playLabel;

    @FXML
    private ListView<String> musicList;

    @FXML
            private ProgressBar progressBar;
    @FXML
            private Button quit;
    File file;

    boolean isPause;
    MediaPlayer mp;
    Media m ;

    volatile String currentSong;

    ArrayList<File> musicArray = new ArrayList<>();
    ArrayList<String> showMusicArray = new ArrayList<>();
    ObservableList<String> obList ;

    int songNumber  = 0;
    int scrollCheck = 0;
    boolean running;

    boolean isStart = true;
    Timeline t;
    FilenameFilter typeMp3;
    boolean isPlayDisabled = true;


    /**
     * Volume Property is defined in this initializer
     * Background image is also set to fit the parent window
     */
    public void initialize() {
        imgView.fitHeightProperty().bind(mainPane.heightProperty());
        imgView.fitWidthProperty().bind(mainPane.widthProperty());
        if (mp != null){
            mp.volumeProperty().set(0.5);}
        volumeSlide.valueProperty().addListener((observableValue, number, t1) -> {
            if (mp != null){
            mp.setVolume(volumeSlide.getValue()*0.01);}
        });

        typeMp3 = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("mp3");
            }
        };


    }

    /**
     * Pauses the media
     */
    public void pauseMusic(){
        if (mp != null) {
            mp.pause();
            isPause = true;

        }}


    /**
     * This function stops the music from playing and resets timer , progress bar and music
     */
    public void stopSong(){

            if (mp != null) {
                mp.seek(Duration.ZERO);
                mp.stop();
                progressBar.setProgress(0);
            }

    }

    /**
     *  PlayMusic() handles the music player and also sets labels accordingly
     *  It also handles when the music resuming from pause
     */
    public void playMusic(){
        if (!musicArray.isEmpty()){
        currentSong = musicArray.get(musicList.getSelectionModel().getSelectedIndex()).getName();
//        System.out.printf("current is %s",currentSong);

        if (mp == null){
        m = new Media(musicArray.get(musicList.getSelectionModel().getSelectedIndex()).toURI().toString());
        mp = new MediaPlayer(m);
        mp.play();

        }
        if (isPause){
            mp.play();
            isPause = false;
//            System.out.println(mp.getCurrentTime());
        }
    else
    {
            m = new Media(musicArray.get(musicList.getSelectionModel().getSelectedIndex()).toURI().toString());
            mp.stop();
            mp = new MediaPlayer(m);
            mp.play();
            currentSong = musicArray.get(musicList.getSelectionModel().getSelectedIndex()).getName();
    }

        if (songNumber >= 0)
        {
            playLabel.setText(musicList.getSelectionModel().getSelectedItem());
        }
        isStart = false;

beginTimer();}
    }


    /**
     * Helper function to call the next function
     * @param isClickNext is boolean value that when true calls the nextSong method
     */
    public void move(boolean isClickNext ){
        if (isClickNext) nextSong();
    }


    /**
     * This handler handles the choosing of music directory
     * Makes the music play automatically when we choose directory
     */
    public void upload(){
        if (mp != null)mp.stop();
        DirectoryChooser dialog = new DirectoryChooser();
        try {

        file = dialog.showDialog(musicList.getScene().getWindow());
            for (File f: Objects.requireNonNull(file.listFiles(typeMp3))){

                System.out.println(f.getName());
                System.out.println(f.getName().endsWith("mp3"));
                musicArray.add(f);
                showMusicArray.add(f.getName());


            }
            obList = FXCollections.observableList(showMusicArray);

            musicList.setItems(obList);
            songNumber = 0;
            musicList.getSelectionModel().select(songNumber);


            playLabel.setText(showMusicArray.get(songNumber));
            mp = new MediaPlayer(new Media(musicArray.get(0).toURI().toString()));

            playMusic();

            playBtn1.setDisable(false);

        }
        catch (Exception t){
            System.out.println("No directory Selected");
            playBtn1.setDisable(true);
        }

    }

    /**
     * Next handles the next function for the music player
     */
    public void nextSong(){
        if (!musicArray.isEmpty()) {
            isPause = false;
            songNumber = musicList.getSelectionModel().getSelectedIndex();

            if (isStart) {
                songNumber++;
                scrollCheck = songNumber;
                isStart = false;
                musicList.getSelectionModel().select(songNumber);
                mp.stop();

                if ((scrollCheck) % 3 == 0) {
                    musicList.scrollTo(songNumber);
                }
            } else if (songNumber < musicArray.size() - 1) {
                songNumber++;
                scrollCheck = songNumber;
                mp.stop();
                musicList.getSelectionModel().select(songNumber);
                if ((scrollCheck) % 3 == 0) {
                    musicList.scrollTo(songNumber);
                }

            } else if (songNumber == musicArray.size() - 1) {
                songNumber = 0;
                scrollCheck = songNumber;
                musicList.getSelectionModel().select(songNumber);
                musicList.scrollTo(songNumber);
                mp.stop();
            }
            playMusic();
        }
    }


    /**
     * This function handles the previous function
     * It makes sure song and labels are set appropriately when we go the previous song
      */
    public void previousSong(){
        if (!musicArray.isEmpty()) {
            isPause = false;

            if (songNumber > 0) {
                songNumber--;
                scrollCheck = songNumber;
                mp.stop();
                musicList.getSelectionModel().select(songNumber);
                playMusic();
                musicList.scrollTo(songNumber);
            } else if (songNumber == 0) {

                scrollCheck = 0;

                mp.stop();
                playMusic();
            }
        }
    }

    /**
     * This function starts our timer async so that we can track the progress of our song
     *
     */
    public void beginTimer(){
        t = new Timeline(new KeyFrame(Duration.seconds(1),e->{

            running = true;
            double current = mp.getCurrentTime().toSeconds();
            double end = m.getDuration().toSeconds();
            progressBar.setProgress(current/end);
            if (current/end == 1){
                move(true);
            }
        }));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }


    public void cancelTimer(){
        t.stop();
        running = false;
    }


    public void clear() {
        if (obList != null) {

            musicList.getItems().clear();
            obList.clear();
            musicArray.clear();
            showMusicArray.clear();
            playLabel.setText("Kwame");
        }
    }

        /**
         * close the application gracefully
         **/
    public void close(){
        Platform.exit();
    }

}