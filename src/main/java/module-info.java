module org.example.projectbidding {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    exports org.example.projectbidding.main;
    opens org.example.projectbidding.main to javafx.fxml;
    exports org.example.projectbidding.controller;
    opens org.example.projectbidding.controller to javafx.fxml;
}