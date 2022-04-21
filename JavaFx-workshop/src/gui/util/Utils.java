package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static Stage currentStage(ActionEvent actionEvent) {

        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }

    public static Integer stringParseInt(String string) {

        try {

            return Integer.parseInt(string);
        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {

        tableColumn.setCellFactory(x -> {

            return new TableCell<T, Double>() {

                @Override
                protected void updateItem(Double item, boolean empty) {

                    super.updateItem(item, empty);

                    if (empty) {

                        setText(null);
                    } else {

                        Locale.setDefault(Locale.US);
                        setText(String.format("%." + decimalPlaces + "f", item));
                    }
                }
            };
        });
    }

    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {

        tableColumn.setCellFactory(x -> {

            return new TableCell<T, Date>() {

                private final SimpleDateFormat SDF = new SimpleDateFormat(format);

                @Override
                protected void updateItem(Date item, boolean empty) {

                    super.updateItem(item, empty);

                    if (empty) {

                        setText(null);
                    } else {

                        setText(SDF.format(item));
                    }
                }
            };
        });
    }
}
