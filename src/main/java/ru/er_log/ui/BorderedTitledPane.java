/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 + Copyright (c) 2019, Eldar Timraleev (aka CRaFT4ik).
 +
 + Licensed under the Apache License, Version 2.0 (the "License");
 + you may not use this file except in compliance with the License.
 + You may obtain a copy of the License at
 +
 +     http://www.apache.org/licenses/LICENSE-2.0
 +
 + Unless required by applicable law or agreed to in writing, software
 + distributed under the License is distributed on an "AS IS" BASIS,
 + WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 + See the License for the specific language governing permissions and
 + limitations under the License.
 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

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
