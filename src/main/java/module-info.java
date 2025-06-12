module pl.edu.wat.wcy.edp.bd.formulino.formulino {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires sql2o;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.net.http;


    opens pl.edu.wat.wcy.edp.bd.formulino to javafx.fxml;
    opens pl.edu.wat.wcy.edp.bd.formulino.controller to javafx.fxml;
    opens pl.edu.wat.wcy.edp.bd.formulino.model to com.fasterxml.jackson.databind, javafx.base;
    opens pl.edu.wat.wcy.edp.bd.formulino.dao to sql2o;

    exports pl.edu.wat.wcy.edp.bd.formulino;
}