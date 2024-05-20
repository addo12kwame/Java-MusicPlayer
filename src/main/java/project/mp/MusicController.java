package project.mp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MusicController {

    @FXML
    private AnchorPane opa;

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


    String a = "";
    boolean isPause;
    MediaPlayer mp;
    Media m;

    String currentSong;

    ArrayList<File> musicArray = new ArrayList<>();
    ArrayList<String> showMusicArray = new ArrayList<>();
    ObservableList<String> obList ;

    int songNumber  = 0;
    int scrollCheck = 0;
    boolean running;

    boolean isStart = true;
    Timer timer;
    TimerTask task;



    public void initialize() {
        imgView.fitHeightProperty().bind(mainPane.heightProperty());
        imgView.fitWidthProperty().bind(mainPane.widthProperty());
        volumeSlide.valueProperty().addListener((observableValue, number, t1) -> {
            if (mp != null){
                System.out.println(volumeSlide.getValue());
            mp.setVolume(volumeSlide.getValue()*0.01);}
        });
    }

    public void stopMusic(ActionEvent e){
        cancelTimer();
        if (mp != null) {
            mp.pause();
            isPause = true;
        }




    }public void playMusic(ActionEvent e){
        beginTimer();
        System.out.println(musicList.getSelectionModel().getSelectedIndex());
        m = new Media(musicArray.get(musicList.getSelectionModel().getSelectedIndex()).toURI().toString());
        currentSong = musicArray.get(musicList.getSelectionModel().getSelectedIndex()).getName();



        if (isPause){

            mp.play();
            isPause = false;
        }else if (mp == null){
            mp = new MediaPlayer(m);}
    else
    {
            mp.stop();
            mp = new MediaPlayer(m);
        }
        mp.play();
        playLabel.setText(showMusicArray.get(songNumber));

    }

    public void upload(ActionEvent e){

        if (mp != null)mp.stop();

        DirectoryChooser dialog = new DirectoryChooser();
        try {
        file = dialog.showDialog(musicList.getScene().getWindow());
//            System.out.println(file.toURI().toString());

            for (File f: Objects.requireNonNull(file.listFiles())){
                musicArray.add(f);
                showMusicArray.add(f.getName());
            System.out.println(f.getName());

            }
            obList = FXCollections.observableList(showMusicArray);

            musicList.setItems(obList);

        }
        catch (Exception t){
            System.out.println("No directory Selected");
        }
System.out.println(showMusicArray);
        songNumber = 0;
        musicList.getSelectionModel().select(songNumber);


        playLabel.setText(showMusicArray.get(songNumber));
        mp = new MediaPlayer(new Media(musicArray.get(0).toURI().toString()));
        beginTimer();
        mp.play();

    }

    public void nextSong(ActionEvent e){

        if (isStart){
            songNumber++;
            scrollCheck = songNumber;
            isStart = false;
            musicList.getSelectionModel().select(songNumber);
            mp.stop();



            m = new Media(musicArray.get(songNumber).toURI().toString());
            mp = new MediaPlayer(m);
            mp.play();
            playLabel.setText(showMusicArray.get(songNumber));


            if ((scrollCheck)%3 == 0){
                musicList.scrollTo(songNumber);}
                return;
        }

        else if (songNumber < musicArray.size()-1){
            songNumber++;
            scrollCheck = songNumber;
            mp.stop();
            m = new Media(musicArray.get(songNumber).toURI().toString());
            mp = new MediaPlayer(m);
            mp.play();
            musicList.getSelectionModel().select(songNumber);
            if ((scrollCheck)%3 == 0){
                musicList.scrollTo(songNumber);}

            playLabel.setText(showMusicArray.get(songNumber));



            if (isPause) isPause = false;
        }
        else if (songNumber == musicArray.size()-1){
            songNumber = 0;
            scrollCheck = songNumber;
            musicList.getSelectionModel().select(songNumber);
            musicList.scrollTo(songNumber);
            mp.stop();
            m = new Media(musicArray.get(songNumber).toURI().toString());
            mp = new MediaPlayer(m);
            mp.play();
            playLabel.setText(showMusicArray.get(songNumber));

        }


    }


    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mp.getCurrentTime().toSeconds();
                double end = m.getDuration().toSeconds();
                progressBar.setProgress(current/end);

                if (current/end == 1){
                    cancelTimer();
                }
            }
        };
timer.scheduleAtFixedRate(task,1000,1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }










}