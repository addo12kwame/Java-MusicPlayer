module project.mp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;




    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens project.mp to javafx.fxml;
    exports project.mp;
}