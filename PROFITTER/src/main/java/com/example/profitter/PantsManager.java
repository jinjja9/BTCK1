package com.example.profitter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

public class PantsManager {

    private ObservableList<Image> pantsImages = FXCollections.observableArrayList();
    private GridPane pantsGridPane;

    public void setPantModel(ImageView shirtmodel) {
        this.pantmodel = shirtmodel;
    }

    private ImageView pantmodel;

    public void setPantsGridPane(GridPane pantsGridPane) {
        this.pantsGridPane = pantsGridPane;
    }

    public void addPants(File file) {
        Image image = new Image(file.toURI().toString());
        pantsImages.add(image);
        updatePantsGrid();
    }


    public void updatePantsGrid() {
        if (pantsGridPane!= null){
            pantsGridPane.getChildren().clear();

            int col = 0;
            int row = 0;
            for (Image image : pantsImages) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                // Add mouse click event to display the image in the shirtmodel ImageView when clicked
                imageView.setOnMouseClicked(event -> {
                    // Display the image in the shirtmodel ImageView when clicked
                    pantmodel.setImage(image);
                });

                GridPane.setRowIndex(imageView, row);
                GridPane.setColumnIndex(imageView, col);
                pantsGridPane.getChildren().add(imageView);

                row++;
            }
        }
    }
}
