package com.ia.demoiii.controllers;

import com.ia.demoiii.dao.StudentDao;
import com.ia.demoiii.models.Student;
import com.ia.demoiii.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class StudentController {
    @FXML
    public Label formTitle;
    @FXML
    public TextField name;
    @FXML
    public TextField email;
    @FXML
    public ChoiceBox<String> group;
    @FXML
    public TextArea description;
    @FXML
    public Button registerBtn;
    @FXML
    public TableView<Student> studentsTable;
    @FXML
    public TableColumn<Student, String> nameColumn;
    @FXML
    public TableColumn<Student, String> emailColumn;
    @FXML
    public TableColumn<Student, String> groupColumn;
    @FXML
    public TableColumn<Student, String> descriptionColumn;
    @FXML
    public TableColumn<Student, Void> actionsColumn;

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final StudentDao studentDao = new StudentDao();
    private Student editingStudent;

    @FXML
    public void initialize() {
        group.getItems().addAll("IF-1", "IF-2", "IF-3", "KT-1");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        setupActionsColumn();

        studentsTable.setItems(students);
        loadStudents();
    }

    @FXML
    public void saveOrUpdate(ActionEvent actionEvent) {
        try {
            if (editingStudent != null) {
                updateStudent();
            } else {
                createStudent();
            }
            resetForm();
        } catch (RuntimeException e) {
            AlertUtil.show(AlertUtil.Status.ERROR, "Klaida", e.getMessage());
        }
    }

    private void createStudent() {
        Student student = new Student(
                name.getText(),
                email.getText(),
                group.getValue(),
                description.getText()
        );
        student.setId(studentDao.create(student));
        students.add(student);
        AlertUtil.show(AlertUtil.Status.INFO, "Pavyko", "Studentas užregistruotas.");
    }

    private void updateStudent() {
        editingStudent.setName(name.getText());
        editingStudent.setEmail(email.getText());
        editingStudent.setGroup(group.getValue());
        editingStudent.setDescription(description.getText());

        studentDao.update(editingStudent);
        studentsTable.refresh();
        AlertUtil.show(AlertUtil.Status.INFO, "Pavyko", "Studento duomenys atnaujinti.");
    }

    private void openEditForm(Student student) {
        editingStudent = student;
        formTitle.setText("Studento redagavimas");
        registerBtn.setText("Atnaujinti");

        name.setText(student.getName());
        email.setText(student.getEmail());
        description.setText(student.getDescription());
        group.setValue(student.getGroup());
    }

    private void deleteStudent(Student student) {
        boolean confirmed = AlertUtil.confirm(
                "Patvirtinimas",
                "Ar tikrai norite ištrinti studentą \"" + student.getName() + "\"?"
        );

        if (!confirmed) {
            return;
        }

        try {
            studentDao.delete(student.getId());
            students.remove(student);

            if (editingStudent != null && editingStudent.getId() == student.getId()) {
                resetForm();
            }

            AlertUtil.show(AlertUtil.Status.INFO, "Sėkmė", "Studentas ištrintas.");
        } catch (RuntimeException e) {
            AlertUtil.show(AlertUtil.Status.ERROR, "Klaida", e.getMessage());
        }
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Redaguoti");
            private final Button deleteButton = new Button("Šalinti");
            private final HBox actions = new HBox(5, editButton, deleteButton);

            {
                actions.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                Student student = getTableRow().getItem();
                if (empty || student == null) {
                    setGraphic(null);
                    return;
                }
                editButton.setOnAction(event -> openEditForm(student));
                deleteButton.setOnAction(event -> deleteStudent(student));
                setGraphic(actions);
            }
        });
    }

    private void loadStudents() {
        try {
            students.setAll(studentDao.findAll());
        } catch (RuntimeException e) {
            AlertUtil.show(AlertUtil.Status.ERROR, "Klaida", e.getMessage());
        }
    }

    private void resetForm() {
        editingStudent = null;
        formTitle.setText("Studento registracija");
        registerBtn.setText("Registruoti");

        name.clear();
        email.clear();
        description.clear();
        group.getSelectionModel().clearSelection();
    }
}
