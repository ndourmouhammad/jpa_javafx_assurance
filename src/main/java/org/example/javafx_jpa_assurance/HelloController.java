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
import javafx.beans.property.ReadOnlyStringWrapper;
import org.example.javafx_jpa_assurance.entity.Assurance;
import org.example.javafx_jpa_assurance.repository.implement.AssuranceRepository;
import org.example.javafx_jpa_assurance.entity.TypeAssurance;
import org.example.javafx_jpa_assurance.repository.implement.TypeAssuranceRepository;

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
    private TypeAssuranceRepository typeAssuranceRepository;
    // Source principale des données affichées (non filtrées)
    private final ObservableList<Assurance> masterData = FXCollections.observableArrayList();

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    // Colonnes du tableau (types explicites pour plus de clarté)
    private TableColumn<Assurance, Integer> idColumn;

    @FXML
    private TextField montantInput;

    @FXML
    private TableColumn<Assurance, Double> montantColumn;

    @FXML
    private TextField nomInput;

    @FXML
    private TableColumn<Assurance, String> nomColumn;

    @FXML
    private TableColumn<Assurance, String> numeroColumn;

    @FXML
    private TableColumn<Assurance, String> typeAssuranceColumn;

    @FXML
    private TableView<Assurance> tabAssurance;

    @FXML
    private TextField rechercheInput;

    @FXML
    private ComboBox<TypeAssurance> typeAssuranceSelect;


    public HelloController(){
        assuranceRepository = new AssuranceRepository();
        typeAssuranceRepository = new TypeAssuranceRepository();
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

        // 6) Charger les types d'assurance dans la ComboBox
        loadTypeAssurances();
        configureTypeAssuranceComboRendering();
    }


    @FXML
    void addAssurance(ActionEvent event) {
        if (!isFormValid()) return;

        String nom = nomInput.getText().trim();
        double montant = parseMontant(montantInput.getText().trim());

        Assurance assurance = new Assurance(nom, montant);
        TypeAssurance ta = typeAssuranceSelect.getValue();
        assurance.setTypeAssurance(ta);
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

        String nom = nomInput.getText().trim();
        double montant = parseMontant(montantInput.getText().trim());

        selected.setNomClient(nom);
        selected.setMontant(montant);
        selected.setTypeAssurance(typeAssuranceSelect.getValue());
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        // Colonne affichant le nom du type d'assurance (gestion du null incluse)
        if (typeAssuranceColumn != null) {
            typeAssuranceColumn.setCellValueFactory(cell -> {
                TypeAssurance ta = cell.getValue() == null ? null : cell.getValue().getTypeAssurance();
                String value = (ta == null || ta.getName() == null) ? "" : ta.getName();
                return new ReadOnlyStringWrapper(value);
            });
        }
    }

    private void setupSearch() {
        // Liste filtrée (recherche) puis triée (clic entête de colonne)
        FilteredList<Assurance> filtered = new FilteredList<>(masterData, a -> true);

        if (rechercheInput != null) {
            rechercheInput.textProperty().addListener((obs, oldV, newV) -> {
                String filter = normalizeFilter(newV);
                filtered.setPredicate(a -> matchesFilter(a, filter));
            });
        }

        SortedList<Assurance> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tabAssurance.comparatorProperty());
        tabAssurance.setItems(sorted);
    }

    // Normalise la valeur de recherche pour réduire la complexité de la logique appelante
    private String normalizeFilter(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    // Vérifie si une assurance correspond au filtre fourni (gestion des nulls incluse)
    private boolean matchesFilter(Assurance a, String filter) {
        if (a == null) return false;
        if (filter == null || filter.isEmpty()) return true;

        String idStr = String.valueOf(a.getId());
        String numero = a.getNumero() == null ? "" : a.getNumero().toLowerCase();
        String nom = a.getNomClient() == null ? "" : a.getNomClient().toLowerCase();
        String montantStr = String.valueOf(a.getMontant()).toLowerCase();
        String typeName = (a.getTypeAssurance() == null || a.getTypeAssurance().getName() == null)
                ? ""
                : a.getTypeAssurance().getName().toLowerCase();

        return idStr.contains(filter)
                || numero.contains(filter)
                || nom.contains(filter)
                || montantStr.contains(filter)
                || typeName.contains(filter);
    }

    private void setupSelection() {
        tabAssurance.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> onSelectionChanged(newSel));
    }

    // Gestion centralisée de la sélection pour réduire la complexité
    private void onSelectionChanged(Assurance newSel) {
        boolean hasSelection = newSel != null;
        updateActionButtonsState(hasSelection);
        if (!hasSelection) return;

        updateFormFromAssurance(newSel);
        // Synchroniser la ComboBox du type
        syncTypeAssuranceSelection(newSel.getTypeAssurance());
    }

    private void updateFormFromAssurance(Assurance a) {
        nomInput.setText(a.getNomClient());
        montantInput.setText(String.valueOf(a.getMontant()));
    }

    private void syncTypeAssuranceSelection(TypeAssurance ta) {
        if (typeAssuranceSelect == null) return;
        if (ta == null) {
            clearTypeSelection();
            return;
        }

        TypeAssurance match = typeAssuranceSelect.getItems().stream()
                .filter(t -> t != null && t.getId() == ta.getId())
                .findFirst()
                .orElse(null);
        if (match != null) {
            typeAssuranceSelect.getSelectionModel().select(match);
        } else {
            clearTypeSelection();
        }
    }

    private void clearTypeSelection() {
        typeAssuranceSelect.getSelectionModel().clearSelection();
    }

    private void updateActionButtonsState(boolean hasSelection) {
        if (updateButton != null) updateButton.setDisable(!hasSelection);
        if (deleteButton != null) deleteButton.setDisable(!hasSelection);
    }

    private void clearFormAndSelection() {
        montantInput.clear();
        nomInput.clear();
        tabAssurance.getSelectionModel().clearSelection();
        if (typeAssuranceSelect != null) {
            typeAssuranceSelect.getSelectionModel().clearSelection();
        }
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
        String nom = nomInput.getText() == null ? "" : nomInput.getText().trim();
        String montantStr = montantInput.getText() == null ? "" : montantInput.getText().trim();
        if (nom.isEmpty() || montantStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Le nom et le montant sont obligatoires.");
            return false;
        }
        if (typeAssuranceSelect == null || typeAssuranceSelect.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un type d'assurance.");
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

    private void loadTypeAssurances() {
        if (typeAssuranceSelect == null) return;
        java.util.List<TypeAssurance> types = typeAssuranceRepository.getAll();
        typeAssuranceSelect.setItems(FXCollections.observableArrayList(types));
    }

    private void configureTypeAssuranceComboRendering() {
        if (typeAssuranceSelect == null) return;
        typeAssuranceSelect.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TypeAssurance item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
        typeAssuranceSelect.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(TypeAssurance item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }


}
