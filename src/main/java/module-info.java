module org.example.javafx_jpa_assurance {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires java.validation;

    opens org.example.javafx_jpa_assurance to javafx.fxml;
    opens org.example.javafx_jpa_assurance.entity to org.hibernate.orm.core, jakarta.persistence;
    exports org.example.javafx_jpa_assurance;
    // Export entities package so ByteBuddy/Hibernate-generated classes in the unnamed module can access it
    exports org.example.javafx_jpa_assurance.entity;
}