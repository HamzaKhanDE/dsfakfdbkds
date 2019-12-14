package com.company;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.collections.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.NumberFormatException;
import java.text.DecimalFormat;
import javafx.beans.value.*;
import javafx.util.StringConverter;

import javax.swing.*;

import static javafx.application.Application.launch;


public class IncomeTaxCalculator extends Application {
    private int filingStatus = 0; // Default Filing Status: Single
    private double stateTaxRate = 0;
    private double federalTaxRate = 0;

    @Override
    public void start(Stage stageObj) {
        // Creating Label with Text Input Field
        Text text1 = new Text("Gross Income:");
        TextField grossIncome = new TextField();

        // Creating Buttons
        Button buttonCalculate = new Button("Calculate");
        Button buttonClear = new Button("Clear");
        Button buttonExit = new Button("Exit");
        
        // Creating Radio Buttons
        final ToggleGroup tg = new ToggleGroup();
        RadioButton rSingle = new RadioButton("Single");
        rSingle.setUserData("0");
        rSingle.setToggleGroup(tg);
        rSingle.setSelected(true);
        RadioButton rMarried = new RadioButton("Married");
        rMarried.setUserData("1");
        rMarried.setToggleGroup(tg);
        RadioButton rMFS = new RadioButton("Married Filing Separately");
        rMFS.setUserData("2");
        rMFS.setToggleGroup(tg);
        RadioButton rHoH = new RadioButton("Head of Household");
        rHoH.setUserData("3");
        rHoH.setToggleGroup(tg);
        HBox taxFilingStatus = new HBox(10, rSingle, rMarried, rMFS, rHoH);
        

            // Creating a Grid Pane
        GridPane gridObj = new GridPane();
        gridObj.setMinSize(400, 200);
        gridObj.setPadding(new Insets(10, 10, 10, 10));
        gridObj.setVgap(5);
        gridObj.setHgap(5);
        gridObj.setAlignment(Pos.CENTER);




        Label FilingStatus = new Label("Filing Status:");
        gridObj.add(FilingStatus, 0, 2);
        Slider FilingSlider = new Slider(0, 3, 0);

        FilingSlider.setMin(0);

        FilingSlider.setMax(3);

        FilingSlider.setValue(0);

        FilingSlider.setMinorTickCount(0);

        FilingSlider.setMajorTickUnit(1);

        FilingSlider.setSnapToTicks(true);

        FilingSlider.setShowTickMarks(true);

        FilingSlider.setShowTickLabels(true);
//to set the values
        FilingSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 0.5) return "Single";
                if (n < 1.5) return "Married";
                if(n < 2.5) return "Separated";
                return "Head of Household";
            }
            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "Single":
                        return 0d;
                    case "Married":
                        return 1d;
                    case "Head of Household":
                        return 2d;
                    default:
                        return 3d;
                }
            }
        });
        FilingSlider.setMinWidth(350);
        HBox layout = new HBox(FilingSlider);
        layout.setPadding(new Insets(30));
        stageObj.setScene(new Scene(layout));
        stageObj.show();
        gridObj.add(FilingSlider, 1, 2);

        
        
        
        

        // Adding Grid Objects
        gridObj.add(text1, 0, 0);
        gridObj.add(grossIncome, 1, 0);
        gridObj.add(buttonCalculate, 3, 0);
        gridObj.add(buttonClear, 3, 1);
        gridObj.add(buttonExit, 3, 2);
        gridObj.add(taxFilingStatus, 1, 1);


        // Creating Overall Results Output
        final Label label = new Label();
        label.setWrapText(true);
        gridObj.setConstraints(label, 1, 5);
        gridObj.setColumnSpan(label, 2);
        gridObj.getChildren().add(label);

        // Creating State Tax Output
        final Label label2 = new Label();
        label.setWrapText(true);
        gridObj.setConstraints(label2, 1, 2);
        gridObj.setColumnSpan(label2, 2);
        gridObj.getChildren().add(label2);

        // Creating Federal Tax Output
        final Label label3 = new Label();
        label.setWrapText(true);
        gridObj.setConstraints(label3, 1, 3);
        gridObj.setColumnSpan(label3, 2);
        gridObj.getChildren().add(label3);

        // Adding a Radio Button Listener
        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (tg.getSelectedToggle() != null) {
                    filingStatus = Integer.parseInt(tg.getSelectedToggle()
                            .getUserData().toString());
                }
            }
        });

        // Setting an Action for the Calculate button
        buttonCalculate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) throws NumberFormatException {
                try {
                    double grossIncomeValue;
                    double stTax;
                    double fedTax;
                    double total = 0;
                    DecimalFormat df = new DecimalFormat("#.00");
                    df.setGroupingUsed(true);
                    df.setGroupingSize(3);

                    // Data Validation on Null Values
                    if ((grossIncome.getText() != null &&
                            !grossIncome.getText().isEmpty())) {
                        grossIncomeValue = Double.parseDouble(grossIncome.getText());

                        // Data Validation on Negative Values
                        if (grossIncomeValue <= 0) {
                            label.setText("Value must be positive.");
                        } else {
                            stTax = stateTax(grossIncomeValue, filingStatus);
                            label2.setText("State Tax (" + stateTaxRate + "%): $"
                                    + df.format(stTax));
                            fedTax = federalTax(grossIncomeValue, filingStatus);
                            label3.setText("Federal Tax (" + federalTaxRate + "%): $"
                                    + df.format(fedTax));
                            total = stTax + fedTax;
                            label.setText("Your gross income is $" + df.format(grossIncomeValue)
                                    + " and the total tax amount due is $" + df.format(total));
                        }
                    } else {
                        label.setText("Please input a valid number.");
                    }
                } catch (NumberFormatException error) {
                    label.setText("Please input a valid number.");
                }
            }
        });

        // Setting an Action for the Clear button
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                grossIncome.clear();
                rSingle.setSelected(false);
                rMarried.setSelected(false);
                rMFS.setSelected(false);
                rHoH.setSelected(false);
                label.setText(null);
                label2.setText(null);
                label3.setText(null);
            }
        });

        // Setting an Action for the Exit button
        buttonExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stageObj.close();
            }
        });

        // Creating a Scene and Adding to the Stage
        Scene scene = new Scene(gridObj);
        stageObj.setTitle("Income Tax Calculator");
        stageObj.setScene(scene);
        stageObj.show();
    }

    // Calculates 2018 New York State Income Tax
    public double stateTax(double income, int filingStatus) {
        double tax = 0;

        // Single or Married Filing Separately
        if (filingStatus == 0 || filingStatus == 2) {
            if (income <= 8500) {
                tax = income * 0.04;
                stateTaxRate = 4;
            } else if (income > 8500 && income < 11701) {
                tax = (income - 8500) * 0.045 + 340;
                stateTaxRate = 4.5;
            } else if (income > 11700 && income < 13901) {
                tax = (income - 11700) * 0.0525 + 484;
                stateTaxRate = 5.25;
            } else if (income > 13900 && income < 21401) {
                tax = (income - 13900) * 0.059 + 600;
                stateTaxRate = 5.9;
            } else if (income > 21400 && income < 80651) {
                tax = (income - 21400) * 0.0633 + 1042;
                stateTaxRate = 6.33;
            } else if (income > 80650 && income < 215401) {
                tax = (income - 80650) * 0.0657 + 4793;
                stateTaxRate = 6.57;
            } else if (income > 215400 && income < 1077551) {
                tax = (income - 215400) * 0.0685 + 13646;
            } else {
                tax = (income - 1077550) * 0.0882 + 72703;
                stateTaxRate = 8.82;
            }
        }

        // Married
        if (filingStatus == 1) {
            if (income <= 17150) {
                tax = income * 0.04;
                stateTaxRate = 4;
            } else if (income > 17150 && income < 23601) {
                tax = (income - 17150) * 0.045 + 686;
                stateTaxRate = 4.5;
            } else if (income > 23600 && income < 27901) {
                tax = (income - 23600) * 0.0525 + 976;
                stateTaxRate = 5.25;
            } else if (income > 27900 && income < 43001) {
                tax = (income - 27900) * 0.059 + 1202;
                stateTaxRate = 5.9;
            } else if (income > 43000 && income < 161551) {
                tax = (income - 43000) * 0.0633 + 2093;
                stateTaxRate = 6.33;
            } else if (income > 161550 && income < 323201) {
                tax = (income - 161550) * 0.0657 + 9597;
                stateTaxRate = 6.57;
            } else if (income > 323200 && income < 2155351) {
                tax = (income - 323200) * 0.0685 + 20218;
                stateTaxRate = 6.85;
            } else {
                tax = (income - 2155350) * 0.0882 + 145720;
                stateTaxRate = 8.82;
            }
        }

        // Head of Household
        if (filingStatus == 3) {
            if (income <= 12800) {
                tax = income * 0.04;
                stateTaxRate = 4;
            } else if (income > 12800 && income < 17651) {
                tax = (income - 12800) * 0.045 + 512;
                stateTaxRate = 4.5;
            } else if (income > 17650 && income < 20901) {
                tax = (income - 17650) * 0.0525 + 730;
                stateTaxRate = 5.25;
            } else if (income > 20900 && income < 32201) {
                tax = (income - 20900) * 0.059 + 901;
                stateTaxRate = 5.9;
            } else if (income > 32200 && income < 107651) {
                tax = (income - 32200) * 0.0633 + 1568;
                stateTaxRate = 6.33;
            } else if (income > 107650 && income < 269301) {
                tax = (income - 107650) * 0.0657 + 6344;
                stateTaxRate = 6.57;
            } else if (income > 269300 && income < 1616451) {
                tax = (income - 269300) * 0.0685 + 16964;
                stateTaxRate = 6.85;
            } else {
                tax = (income - 1616450) * 0.0882 + 109244;
                stateTaxRate = 8.82;
            }
        }

        return tax;
    }

    // Calculates 2018 Federal Income Tax
    public double federalTax(double income, int filingStatus) {
        double tax = 0;

        // Single
        if (filingStatus == 0) {
            if (income <= 9526) {
                tax = income * 0.1;
                federalTaxRate = 10;
            } else if (income < 38701) {
                tax = 9525 * 0.1 + (income - 9525) * 0.12;
                federalTaxRate = 12;
            } else if (income < 82501) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (income - 38700) * 0.22;
                federalTaxRate = 22;
            } else if (income < 157501) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (income - 82500) * 0.24;
                federalTaxRate = 24;
            } else if (income < 200001) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (income - 157500) * 0.32;
                federalTaxRate = 32;
            } else if (income < 500001) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (income - 200000) * 0.35;
                federalTaxRate = 35;
            } else {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (500000 - 200000) * 0.35 + (income - 500000) * 0.37;
                federalTaxRate = 37;
            }
        }

        // Married
        if (filingStatus == 1) {
            if (income <= 19051) {
                tax = income * 0.1;
                federalTaxRate = 10;
            } else if (income < 77401) {
                tax = 19050 * 0.1 + (income - 19050) * 0.12;
                federalTaxRate = 12;
            } else if (income < 165001) {
                tax = 19050 * 0.1 + (77400 - 19050) * 0.12 + (income - 77400) * 0.22;
                federalTaxRate = 22;
            } else if (income < 315001) {
                tax = 19050 * 0.1 + (77400 - 19050) * 0.12 + (165000 - 77400) * 0.22
                        + (income - 165000) * 0.24;
                federalTaxRate = 24;
            } else if (income < 400001) {
                tax = 19050 * 0.1 + (77400 - 19050) * 0.12 + (165000 - 77400) * 0.22
                        + (315000 - 165000) * 0.24 + (income - 315000) * 0.32;
                federalTaxRate = 32;
            } else if (income < 600001) {
                tax = 19050 * 0.1 + (77400 - 19050) * 0.12 + (165000 - 77400) * 0.22
                        + (315000 - 165000) * 0.24 + (400000 - 315000) * 0.32
                        + (income - 400000) * 0.35;
                federalTaxRate = 35;
            } else {
                tax = 19050 * 0.1 + (77400 - 19050) * 0.12 + (165000 - 77400) * 0.22
                        + (315000 - 165000) * 0.24 + (400000 - 315000) * 0.32
                        + (600000 - 400000) * 0.35 + (income - 600000) * 0.37;
                federalTaxRate = 37;
            }
        }

        // Married Filing Separately
        if (filingStatus == 2) {
            if (income <= 9526) {
                tax = income * 0.1;
                federalTaxRate = 10;
            } else if (income < 38701) {
                tax = 9525 * 0.1 + (income - 9525) * 0.12;
                federalTaxRate = 12;
            } else if (income < 82501) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (income - 38700) * 0.22;
                federalTaxRate = 22;
            } else if (income < 157501) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (income - 82500) * 0.24;
                federalTaxRate = 24;
            } else if (income < 200001) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (income - 157500) * 0.32;
                federalTaxRate = 32;
            } else if (income < 300001) {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (income - 200000) * 0.35;
                federalTaxRate = 35;
            } else {
                tax = 9525 * 0.1 + (38700 - 9525) * 0.12 + (82500 - 38700) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (300000 - 200000) * 0.35 + (income - 300000) * 0.37;
                federalTaxRate = 37;
            }
        }

        // Head of Household
        if (filingStatus == 3) {
            if (income <= 13601) {
                tax = income * 0.1;
                federalTaxRate = 10;
            } else if (income < 51801) {
                tax = 13600 * 0.1 + (income - 13600) * 0.12;
                federalTaxRate = 12;
            } else if (income < 82501) {
                tax = 13600 * 0.1 + (51800 - 13600) * 0.12 + (income - 51800) * 0.22;
                federalTaxRate = 22;
            } else if (income < 157501) {
                tax = 13600 * 0.1 + (51800 - 13600) * 0.12 + (82500 - 51800) * 0.22
                        + (income - 82500) * 0.24;
                federalTaxRate = 24;
            } else if (income < 200001) {
                tax = 13600 * 0.1 + (51800 - 13600) * 0.12 + (82500 - 51800) * 0.22
                        + (157500 - 82500) * 0.24 + (income - 157500) * 0.32;
                federalTaxRate = 32;
            } else if (income < 500001) {
                tax = 13600 * 0.1 + (51800 - 13600) * 0.12 + (82500 - 51800) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (income - 200000) * 0.35;
                federalTaxRate = 35;
            } else {
                tax = 13600 * 0.1 + (51800 - 13600) * 0.12 + (82500 - 51800) * 0.22
                        + (157500 - 82500) * 0.24 + (200000 - 157500) * 0.32
                        + (500000 - 200000) * 0.35 + (income - 500000) * 0.37;
                federalTaxRate = 37;
            }
        }

        return tax;
    }

    public static void main(String args[]) {
        launch(args);
    }
}
