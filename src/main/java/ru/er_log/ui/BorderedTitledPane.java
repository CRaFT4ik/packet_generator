package ru.er_log.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class BorderedTitledPane extends StackPane
{
    private StringProperty title = new SimpleStringProperty();

    public BorderedTitledPane()
    {
        this("Title");
    }

    public BorderedTitledPane(Node... children)
    {
        this("Title", children);
    }

    private BorderedTitledPane(String titleString, Node... content)
    {
        super();

        Label titleLabel = new Label(" " + titleString + " ");
        titleLabel.getStyleClass().add("bordered-titled-label");
        StackPane.setAlignment(titleLabel, Pos.TOP_LEFT);

        this.title.set(titleString);
        this.title.bindBidirectional(titleLabel.textProperty());

        StackPane contentPane = new StackPane();
        for (Node nd : content)
            nd.getStyleClass().add("bordered-titled-content");
        contentPane.getChildren().addAll(content);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(titleLabel, contentPane);
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public StringProperty titleProperty()
    {
        return this.title;
    }
}
