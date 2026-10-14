package org.example.viewServices.statisticsViews;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.Notifications;
import org.example.dataServices.CrossAnalysisService;
import org.example.entities.statisticsEntities.CrossAnalysisEntity;

import java.io.IOException;
import java.sql.SQLException;

public class CrossAnalysisView
{
  private TableView<CrossAnalysisEntity> view;
  private final ObservableList<CrossAnalysisEntity> data = FXCollections.observableArrayList();
  private final ComboBox<String> entityFilter = new ComboBox<>();
  private StackPane content;
  private final Spinner<Integer> yearFromField = new Spinner<>(2000, 2100, 2023);
  private final Spinner<Integer> yearToField = new Spinner<>(2000, 2100, 2025);
  private final RadioButton tableRadio = new RadioButton("Таблица");
  private final RadioButton chartRadio = new RadioButton("График");


  public Node createView()
  {
    entityFilter.getItems().addAll("Группа", "Студент", "Предмет", "Преподаватель");
    entityFilter.setValue("Группа");

    view = buildTable();

    tableRadio.setSelected(true);
    ToggleGroup tg = new ToggleGroup();
    tableRadio.setToggleGroup(tg);
    chartRadio.setToggleGroup(tg);

    content = new StackPane(view);
    content.setPadding(new Insets(10));

    tableRadio.setOnAction(e -> refreshContent());
    chartRadio.setOnAction(e -> refreshContent());

    yearFromField.valueProperty().addListener((o,a,b) ->
    {
      try
      {
        data.setAll(CrossAnalysisService.loadData(
            yearFromField.getValue(),
            yearToField.getValue(),
            getEntityCode()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    yearToField.valueProperty().addListener((o,a,b) ->
    {
      try
      {
        data.setAll(CrossAnalysisService.loadData(
            yearFromField.getValue(),
            yearToField.getValue(),
            getEntityCode()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    entityFilter.valueProperty().addListener((o,a,b) ->
    {
      try
      {
        data.setAll(CrossAnalysisService.loadData(
            yearFromField.getValue(),
            yearToField.getValue(),
            getEntityCode()));
      }
      catch (SQLException | IOException e)
      {
        Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
      }
    });

    HBox filters = new HBox(
        10,
        new Label("Годы:"),
        yearFromField,
        new Label("—"),
        yearToField,
        new Label("Сущность:"),
        entityFilter
    );

    filters.setPadding(new Insets(10));

    HBox switcher = new HBox(15, tableRadio, chartRadio);
    switcher.setPadding(new Insets(0, 10, 10, 10));

    try
    {
      data.setAll(CrossAnalysisService.loadData(
          yearFromField.getValue(),
          yearToField.getValue(),
          getEntityCode()));
    }
    catch (SQLException | IOException e)
    {
      Notifications.notify(Alert.AlertType.ERROR, e.getMessage());
    }

    VBox root = new VBox(5, filters, switcher, content);
    VBox.setVgrow(content, Priority.ALWAYS);
    return root;
  }

  private TableView<CrossAnalysisEntity> buildTable()
  {
    TableView<CrossAnalysisEntity> t = new TableView<>();

    TableColumn<CrossAnalysisEntity, Integer> yearCol = new TableColumn<>("Год");
    yearCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().year()).asObject());
    yearCol.setPrefWidth(80);

    TableColumn<CrossAnalysisEntity, String> entityCol = new TableColumn<>("Сущность");
    entityCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().entityName()));

    TableColumn<CrossAnalysisEntity, Double> avgCol = new TableColumn<>("Средний балл");
    avgCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().avgMark()).asObject());

    t.getColumns().add(yearCol);
    t.getColumns().add(entityCol);
    t.getColumns().add(avgCol);
    t.setItems(data);
    return t;
  }

  private void refreshContent()
  {
    if (tableRadio.isSelected())
    {
      content.getChildren().setAll(view);
    }
    else
    {
      content.getChildren().setAll(buildChart());
    }
  }

  private Chart buildChart()
  {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Сущность");
    NumberAxis yAxis = new NumberAxis(0, 5, 1);
    yAxis.setLabel("Средний балл");

    BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
    chart.setTitle("Средний балл по сущностям и годам");

    data.stream().map(CrossAnalysisEntity::year).distinct().sorted().forEach(year -> {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName(String.valueOf(year));
      data.stream()
          .filter(r -> r.year().equals(year))
          .forEach(r -> series.getData().add(new XYChart.Data<>(r.entityName(), r.avgMark()))
      );
      chart.getData().add(series);
    });
    return chart;
  }

  private String getEntityCode()
  {
    return switch (entityFilter.getValue())
    {
      case "Группа"        -> "group";
      case "Студент"       -> "student";
      case "Предмет"       -> "subject";
      case "Преподаватель" -> "teacher";
      default -> "group";
    };
  }
}

