module com.example.wk3icfuelconsumptionloc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.wk3icfuelconsumptionloc to javafx.fxml;
    exports com.example.wk3icfuelconsumptionloc;
}