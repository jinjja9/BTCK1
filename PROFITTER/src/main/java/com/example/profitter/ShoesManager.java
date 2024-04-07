package com.example.profitter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

public class ShoesManager {

    private ObservableList<Image> shoesImages = FXCollections.observableArrayList();
    private GridPane shoesGridPane;
    private ImageView shoesModel;

    public void setShoesGridPane(GridPane shoesGridPane) {
        this.shoesGridPane = shoesGridPane;
    }

    public void setShoesModel(ImageView shoesModel) {
        this.shoesModel = shoesModel;
    }

    public void addShoes(File file) {
        Image image = new Image(file.toURI().toString());
        shoesImages.add(image);
        updateShoesGrid();
    }

    void updateShoesGrid() {
        if (shoesGridPane != null) {
            shoesGridPane.getChildren().clear();

            int col = 0;
            int row = 0;
            for (Image image : shoesImages) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(140);
                imageView.setFitHeight(80);

                // Gắn sự kiện nhấp chuột
                imageView.setOnMouseClicked(event -> {
                    // Hiển thị ảnh trong ImageView shoesModel khi nhấp chuột vào ảnh
                    shoesModel.setImage(image);
                });

                GridPane.setRowIndex(imageView, row);
                GridPane.setColumnIndex(imageView, col);
                shoesGridPane.getChildren().add(imageView);

                col++;
            }
        }
    }
}
