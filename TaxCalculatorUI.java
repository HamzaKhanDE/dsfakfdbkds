package my.taxcalculation;
//import the required packages
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

//The class to cretate the application
public class TaxCalculationUI extends Application{
//The main
    public static void main(String[] args) {
        launch(args);
    }
//method to launch the application
    @Override
    public void start(Stage stageObj) throws Exception {
//set the tittle
        stageObj.setTitle("Tax Estimator");
        stageObj.show();
//create the grid pane an allign it
        GridPane gridObj = new GridPane();
        gridObj.setAlignment(Pos.CENTER);
        gridObj.setHgap(10);
        gridObj.setVgap(10);
        gridObj.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(gridObj, 800, 600);
//set the title
        Text titttleScene = new Text("Income Tax Calculator");
        titttleScene.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        gridObj.add(titttleScene, 0, 0, 2, 1);
//set the gross salary field
        Label salaryGross = new Label("Income:");
        gridObj.add(salaryGross, 0, 1);
        final TextField grossSalaryField = new TextField();
        gridObj.add(grossSalaryField, 1, 1);
//to set the marriSlidertial status slider

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

                    case "HeadofHousehold":
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

//set the dependecies

        Label NumOfDep = new Label("Number of depend:");

        gridObj.add(NumOfDep, 0, 3);

        final ComboBox depComb = new ComboBox();

        depComb.getItems().addAll(

                "0",

                "1",

                "2",

                "3",

                "4",

                "5",

                "6"

        );

//set the default box

        depComb.setValue("0");

        gridObj.add(depComb, 1,3);

//set the rateUp taxEs

        Label upperTaxRate = new Label("Upper rates of taxEs :");

        gridObj.add(upperTaxRate, 0, 4);

        final TextField upperTex = new TextField();

        upperTex.setText("40");

        gridObj.add(upperTex, 1, 4);

//set the rateLow rates of taxEs

        Label lowerTaxRate = new Label("Lower rates of taxEs :");

        gridObj.add(lowerTaxRate, 0, 5);

        final TextField lowTex = new TextField();

//set the default value

        lowTex.setText("20");

        gridObj.add(lowTex, 1, 5);

//Tax allowance button to choose the allowence taking

        Label taxAllow = new Label("Tax allowance: ");

        gridObj.add(taxAllow, 0, 6);

        ToggleGroup group = new ToggleGroup();

        RadioButton button1 = new RadioButton("$ 3300");

        button1.setToggleGroup(group);

        button1.setSelected(true);

        RadioButton button2 = new RadioButton("$ 4950");

        button2.setToggleGroup(group);

        gridObj.add(button1, 1, 6);

        gridObj.add(button2, 1, 7);

//Crate a text area

        TextArea textAreaObj = new TextArea();



        textAreaObj.setEditable(false);

        textAreaObj.setWrapText(true);

        textAreaObj.wrapTextProperty();

        textAreaObj.setPrefColumnCount(0);

        gridObj.add(textAreaObj, 1, 10);

        final Label taxLblObj = new Label();

        final Label netSalery = new Label();

//The calculate button

        Button calcButtn = new Button("Calculate Tax");

        HBox hbBtn = new HBox(10);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        hbBtn.getChildren().add(calcButtn);

        gridObj.add(hbBtn, 0, 8);

//Add the reset button

        Button btnReset = new Button("Reset");

        HBox resatButton = new HBox(10);

        resatButton.setAlignment(Pos.BOTTOM_LEFT);

        resatButton.getChildren().add(btnReset);

        gridObj.add(resatButton, 1, 8);

        btnReset.setOnAction(new EventHandler<ActionEvent>() {

            @Override

            public void handle(ActionEvent e) {

                grossSalaryField.setText("");

//cmb.setValue("Single");

                depComb.setValue("0");

                upperTex.setText("40");

                lowTex.setText("20");

                taxLblObj.setText("");

                netSalery.setText("");

            }

        });

        calcButtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override

            public void handle(ActionEvent e) {

//the local variables

                int rateUp=0;

                int rateLow = 0;

                int grossSalar = 0;

                int depend = 0;

                int taxSal = 0;

                double taxEs = 0.00;

                double netSal = 0.00;

//take the rates

                if ((null!=upperTex.getText() && !upperTex.getText().isEmpty())) {

                    rateUp = Integer.parseInt(upperTex.getText());

                }

                if ((null!=lowTex.getText() && !lowTex.getText().isEmpty())) {

                    rateLow = Integer.parseInt(lowTex.getText());

                }

//calculate the tax

                if ((null!=lowTex.getText() && !grossSalaryField.getText().isEmpty())) {

                    String output = depComb.getSelectionModel().getSelectedItem().toString();

                    depend=Integer.parseInt(output);

                    int ded=0;

                    if(FilingSlider.getValue()==1)

                    {

                        ded=4950;

                    }

                    else

                    {

                        ded=3300;

                    }

                    grossSalar = Integer.parseInt(grossSalaryField.getText());

                    System.out.println("gross"+grossSalar);

                    System.out.println("depe"+depend);

                    ded=ded+depend*100;

                    taxSal = grossSalar - ded;

                    if(taxSal<33800)

                    {

                        taxEs = taxSal*rateLow/100;

                    }

                    else

                    {

                        taxEs = taxSal*rateUp/100;

                    }

                    netSal = (int) (grossSalar - taxEs);

                }

//diaply the results

                textAreaObj.setText("The tax calculated: $ "+taxEs);

                textAreaObj.appendText("\nThe net salary: $ "+netSal);

            }

        });

        stageObj.setScene(scene);

    }

}
