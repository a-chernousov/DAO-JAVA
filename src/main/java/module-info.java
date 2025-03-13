module com.example.dao {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens com.example.dao.BD to javafx.base;
    opens com.example.dao to javafx.fxml;
    exports com.example.dao;
    exports com.example.dao.buttonCell;
    opens com.example.dao.buttonCell to javafx.fxml;
    opens com.example.dao.product to javafx.base;
}