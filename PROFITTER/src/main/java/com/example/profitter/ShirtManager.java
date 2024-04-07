package com.example.profitter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

public class ShirtManager {

    private ObservableList<Image> shirtImages = FXCollections.observableArrayList();
    private GridPane shirtsGridPane;
    private ImageView shirtmodel;

    public void setShirtModel(ImageView shirtmodel) {
        this.shirtmodel = shirtmodel;
    }

    public void setShirtsGridPane(GridPane shirtsGridPane) {
        this.shirtsGridPane = shirtsGridPane;
    }

    public void addShirt(File file) {
        Image image = new Image(file.toURI().toString());
        shirtImages.add(image);
        updateShirtGrid();
    }
    void updateShirtGrid() {
        if (shirtsGridPane != null) {
            shirtsGridPane.getChildren().clear();

            int col = 0;
            int row = 0;
            for (Image image : shirtImages) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                imageView.setOnMouseClicked(event -> {
                    shirtmodel.setImage(image);
                });

                GridPane.setRowIndex(imageView, row);
                GridPane.setColumnIndex(imageView, col);
                shirtsGridPane.getChildren().add(imageView);

                row++;
            }
        }
    }

}