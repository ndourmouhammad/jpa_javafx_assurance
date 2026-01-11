package org.example.javafx_jpa_assurance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.javafx_jpa_assurance.entity.Assurance;
import org.example.javafx_jpa_assurance.repository.implement.AssuranceRepository;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur principal de l'application Assurance.
 *
 * Objectifs de refactorisation pour débutants:
 * - Regrouper les responsabilités dans de petites méthodes très lisibles.
 * - Ajouter des commentaires simples qui expliquent l'intention.
 * - Utiliser des types génériques clairs pour les colonnes du tableau.
 */
public class HelloController implements Initializable {

    private AssuranceRepository assuranceRepository;
    // Source principale des données affichées (non filtrées)
    private final ObservableList<Assurance> masterData = FXCollections.observableArrayList();

    @FXML
    private Button bt_add;

    @FXML
    private Button bt_clear;

    @FXML
    private Button bt_delete;

    @FXML
    private Button bt_update;

    @FXML
    // Colonnes du tableau (types explicites pour plus de clarté)
    private TableColumn<Assurance, Integer> id_tab;

    @FXML
    private TextField montant_input;

    @FXML
    private TableColumn<Assurance, Double> montant_tab;

    @FXML
    private TextField nom_input;

    @FXML
    private TableColumn<Assurance, String> nom_tab;

    @FXML
    private TableColumn<Assurance, String> numero_tab;

    @FXML
    private TableView<Assurance> tabAssurance;

    @FXML
    private TextField recherche_input;


    public HelloController(){
        assuranceRepository = new AssuranceRepository();
        // getAllAssurance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1) Colonnes du tableau
        setupColumns();

        // 2) Barre de recherche (filtre) + tri
        setupSearch();

        // 3) Charger les données depuis la base
        getAllAssurance();

        // 4) Gestion de la sélection d'une ligne
        setupSelection();

        // 5) État initial des boutons
        updateActionButtonsState(false);
    }


    @FXML
    void addAssurance(ActionEvent event) {
        if (!isFormValid()) return;

        String nom = nom_input.getText().trim();
        double montant = parseMontant(montant_input.getText().trim());

        Assurance assurance = new Assurance(nom, montant);
        assuranceRepository.insert(assurance);
        getAllAssurance();
        clearFormAndSelection();
    }

    @FXML
    void clearInterface(ActionEvent event) {
        clearFormAndSelection();
    }

    @FXML
    void deleteAssurance(ActionEvent event) {
        Assurance selected = getSelectedAssuranceOrInform("Veuillez sélectionner une assurance à supprimer.");
        if (selected == null) return;
        assuranceRepository.delete(selected);
        getAllAssurance();
        tabAssurance.getSelectionModel().clearSelection();
    }

    @FXML
    void updateAssurance(ActionEvent event) {
        Assurance selected = getSelectedAssuranceOrInform("Veuillez sélectionner une assurance à modifier.");
        if (selected == null) return;

        if (!isFormValid()) return; // validation simple et centralisée

        String nom = nom_input.getText().trim();
        double montant = parseMontant(montant_input.getText().trim());

        selected.setNomClient(nom);
        selected.setMontant(montant);
        assuranceRepository.update(selected);
        getAllAssurance();
        // Rester sur la ligne courante si possible
        // Rechercher l'élément avec le même id et le sélectionner
        for (Assurance a : tabAssurance.getItems()) {
            if (a.getId() == selected.getId()) {
                tabAssurance.getSelectionModel().select(a);
                break;
            }
        }
    }

    void getAllAssurance() {
        java.util.List<Assurance> list = assuranceRepository.getAll();
        // Rafraîchir la source de données principale (la vue filtrée/sortée se met à jour automatiquement)
        masterData.setAll(list);
    }

    // ===================== Méthodes utilitaires lisibles =====================

    private void setupColumns() {
        id_tab.setCellValueFactory(new PropertyValueFactory<>("id"));
        nom_tab.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        montant_tab.setCellValueFactory(new PropertyValueFactory<>("montant"));
        numero_tab.setCellValueFactory(new PropertyValueFactory<>("numero"));
    }

    private void setupSearch() {
        // Liste filtrée (recherche) puis triée (clic entête de colonne)
        FilteredList<Assurance> filtered = new FilteredList<>(masterData, a -> true);

        if (recherche_input != null) {
            recherche_input.textProperty().addListener((obs, oldV, newV) -> {
                String filter = newV == null ? "" : newV.trim().toLowerCase();
                if (filter.isEmpty()) {
                    filtered.setPredicate(a -> true);
                } else {
                    filtered.setPredicate(a -> {
                        if (a == null) return false;
                        String idStr = String.valueOf(a.getId());
                        String numero = a.getNumero() == null ? "" : a.getNumero().toLowerCase();
                        String nom = a.getNomClient() == null ? "" : a.getNomClient().toLowerCase();
                        String montantStr = String.valueOf(a.getMontant()).toLowerCase();
                        return idStr.contains(filter)
                                || numero.contains(filter)
                                || nom.contains(filter)
                                || montantStr.contains(filter);
                    });
                }
            });
        }

        SortedList<Assurance> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tabAssurance.comparatorProperty());
        tabAssurance.setItems(sorted);
    }

    private void setupSelection() {
        tabAssurance.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nom_input.setText(newSel.getNomClient());
                montant_input.setText(String.valueOf(newSel.getMontant()));
                updateActionButtonsState(true);
            } else {
                updateActionButtonsState(false);
            }
        });
    }

    private void updateActionButtonsState(boolean hasSelection) {
        if (bt_update != null) bt_update.setDisable(!hasSelection);
        if (bt_delete != null) bt_delete.setDisable(!hasSelection);
    }

    private void clearFormAndSelection() {
        montant_input.clear();
        nom_input.clear();
        tabAssurance.getSelectionModel().clearSelection();
    }

    private Assurance getSelectedAssuranceOrInform(String messageIfNull) {
        Assurance selected = tabAssurance.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.INFORMATION, messageIfNull);
            return null;
        }
        return selected;
    }

    private boolean isFormValid() {
        String nom = nom_input.getText() == null ? "" : nom_input.getText().trim();
        String montantStr = montant_input.getText() == null ? "" : montant_input.getText().trim();
        if (nom.isEmpty() || montantStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Le nom et le montant sont obligatoires.");
            return false;
        }
        try {
            Double.parseDouble(montantStr);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Le montant doit être un nombre valide.");
            return false;
        }
        return true;
    }

    private double parseMontant(String montantStr) {
        return Double.parseDouble(montantStr);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }


}
