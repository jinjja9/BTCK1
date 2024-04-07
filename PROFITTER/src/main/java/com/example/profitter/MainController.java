package com.example.profitter;

import javafx.application.Platform;
//import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;
    @FXML
    private BorderPane bp;

    private Scene scene;

    @FXML
    void logIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (checkCredentials(username, password) || (username.equals("admin") && password.equals("admin"))) {
            if (username.equals("admin") && password.equals("admin"))
                Main.isAdminLoggedIn = true;
            try {
                Stage mainStage = new Stage();
                mainStage.setTitle("PROFITTER");
                scene = new Scene(FXMLLoader.load(getClass().getResource("MainView.fxml")));
                mainStage.setScene(scene);
                mainStage.show();

                Stage loginStage = (Stage) loginButton.getScene().getWindow();
                loginStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Tài khoản hoặc Mật khẩu chưa đúng", Alert.AlertType.ERROR);
        }
    }

    private boolean checkCredentials(String username, String password) {
        List<User> users = UserFileHandler.loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        switch (alertType) {
            case ERROR:
                alert.setTitle("Error");
                break;
            case INFORMATION:
                alert.setTitle("Information");
                break;
            case WARNING:
                alert.setTitle("Warning");
                break;
            case CONFIRMATION:
                alert.setTitle("Confirmation");
                break;
            default:
                break;
        }
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void openRegisterView() {
        try {
            Stage registerStage = new Stage();
            registerStage.setTitle("Register");
            registerStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Register.fxml"))));
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Registration() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!username.isEmpty() && !password.isEmpty()) {
            User newUser = new User(username, password);
            List<User> users = UserFileHandler.loadUsers();
            users.add(newUser);
            UserFileHandler.saveUsers(users);
            showAlert("Đăng kí thành công!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Vui lòng điền đầy đủ thông tin đăng kí.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void closeMainView() {
        try {
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Main.fxml"))));
            loginStage.show();

            Stage mainStage = (Stage) bp.getScene().getWindow();
            mainStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //------------------SAVE----------------------
    @FXML
    private Pane centerPane;

    @FXML
    void savePhoto(ActionEvent actionEvent) {
        try {
            WritableImage writableImage = centerPane.snapshot(new SnapshotParameters(), null);
            File file = new FileChooser().showSaveDialog(null);
            if (file != null) { // Kiểm tra xem file có null hay không
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                showAlert("Ảnh đã được lưu vào: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } else {
                showAlert("Hủy lưu ảnh.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            showAlert("Lỗi khi lưu ảnh.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    //-----------------------KHO AO-------------------
    @FXML
    private GridPane shirtsGridPane;

    @FXML
    private GridPane pantsGridPane;

    @FXML
    private GridPane shoesGridPane;

    @FXML
    private ImageView shirtmodel;

    @FXML
    private ImageView pantmodel;
    @FXML
    private ImageView shoesmodel;

    private ShirtManager shirtManager;
    private PantsManager pantsManager;

    private ShoesManager shoesManager;

    private int col = 0;
    private int row = 0;

    public void initialize() {
        shirtManager = new ShirtManager();
        shirtManager.setShirtsGridPane(shirtsGridPane);
        shirtManager.setShirtModel(shirtmodel);

        pantsManager = new PantsManager();
        pantsManager.setPantsGridPane(pantsGridPane);
        pantsManager.setPantModel(pantmodel);

        shoesManager = new ShoesManager();
        shoesManager.setShoesGridPane(shoesGridPane);
        shoesManager.setShoesModel(shoesmodel);

        if (++col == 4) {
            col = 0;
            row++;
        }


        loadShirtImagesFromDirectory("ShirtImage");
        loadPantImagesFromDirectory("PantImage");
        loadShoesImagesFromDirectory("ShoesImage");
    }

    private void loadShirtImagesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    shirtManager.addShirt(file);
                    setDraggable(shirtmodel);
                }
            }
        }
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("gif");
    }

    @FXML
    public void addShirtAction() {
        if (Main.isAdminLoggedIn) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(null);

            if (shirtmodel != null) {
                setDraggable(shirtmodel);
            }

            if (file != null) {
                String sourceImagePath = file.getAbsolutePath();

                String destinationDirectory = "ShirtImage";
                String destinationImagePath = destinationDirectory + "\\" + file.getName();

                File destinationFile = new File(destinationImagePath);

                try {
                    Files.copy(Paths.get(sourceImagePath), destinationFile.toPath());

                    Image image = new Image(file.toURI().toString());
                    shirtmodel.setImage(image);

                    shirtManager.addShirt(file);
                    shirtManager.updateShirtGrid();

                    col++;
                    if (col == 4) {
                        col = 0;
                    }
                } catch (IOException e) {
                    showAlert("Lỗi khi sao chép ảnh.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Bạn cần đăng nhập bằng tài khoản admin để thực hiện thao tác này.", Alert.AlertType.WARNING);
        }
    }


    private void setDraggable(ImageView imageView) {
        if (imageView != null) {
            final double[] mouseX = new double[1];
            final double[] mouseY = new double[1];

            imageView.setOnMousePressed(event -> {
                mouseX[0] = event.getSceneX() - imageView.getTranslateX();
                mouseY[0] = event.getSceneY() - imageView.getTranslateY();
                event.consume();
            });

            imageView.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - mouseX[0];
                double offsetY = event.getSceneY() - mouseY[0];
                imageView.setTranslateX(offsetX);
                imageView.setTranslateY(offsetY);
            });
        }
    }


    //-----------------------KHO QUAN-------------------
    @FXML
    public void addPantsAction() {
        if (Main.isAdminLoggedIn) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(null);

            if (pantmodel != null) {
                setDraggable(pantmodel);
            }

            if (file != null) {
                String sourceImagePath = file.getAbsolutePath();
                String destinationDirectory = "PantImage";
                String destinationImagePath = destinationDirectory + "\\" + file.getName();
                File destinationFile = new File(destinationImagePath);

                try {
                    Files.copy(Paths.get(sourceImagePath), destinationFile.toPath());
                    Image image = new Image(file.toURI().toString());
                    pantmodel.setImage(image);
                    pantsManager.addPants(file);
                    pantsManager.updatePantsGrid();

                    col++;
                    if (col == 4) {
                        col = 0;
                    }
                } catch (IOException e) {
                    showAlert("Lỗi khi sao chép ảnh.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Bạn cần đăng nhập bằng tài khoản admin để thực hiện thao tác này.", Alert.AlertType.WARNING);
        }
    }

    private void loadPantImagesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    pantsManager.addPants(file);
                    setDraggable(pantmodel);
                }
            }
        }
    }

    //--------------------KHO GIAY---------------------------
    @FXML
    public void addShoesAction() {
        if (Main.isAdminLoggedIn) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(null);

            if (shoesmodel != null) {
                setDraggable(shoesmodel);
            }

            if (file != null) {
                String sourceImagePath = file.getAbsolutePath();
                String destinationDirectory = "ShoesImage";
                String destinationImagePath = destinationDirectory + File.separator + file.getName();
                File destinationFile = new File(destinationImagePath);

                try {
                    Files.copy(Paths.get(sourceImagePath), destinationFile.toPath());
                    Image image = new Image(file.toURI().toString());
                    shoesmodel.setImage(image);
                    shoesManager.addShoes(file);
                    shoesManager.updateShoesGrid();
                } catch (IOException e) {
                    showAlert("Lỗi khi sao chép ảnh.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Bạn cần đăng nhập bằng tài khoản admin để thực hiện thao tác này.", Alert.AlertType.WARNING);
        }
    }

    private void loadShoesImagesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    shoesManager.addShoes(file);
                    setDraggable(shoesmodel);
                }
            }
        }
    }

    //-----------------FACE-----------------
    @FXML
    private ImageView facemodel;

    @FXML
    public void addFaceAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            facemodel.setImage(image);
            setDraggable(facemodel);
        }
    }

}
